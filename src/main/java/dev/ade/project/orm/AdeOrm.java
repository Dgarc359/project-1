package dev.ade.project.orm;

import dev.ade.project.annotations.ColumnName;
import dev.ade.project.annotations.PrimaryKey;
import dev.ade.project.annotations.TableName;
import dev.ade.project.exception.ArgumentFormatException;
import dev.ade.project.util.ConnectionUtil;
import dev.ade.project.util.MapperUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class AdeOrm implements Mapper {
    // A POJO class mirror with a table in the db
    private Class<?> clazz;
    private Connection conn;
    private boolean isTransaction;
    private List<Boolean> completes = new ArrayList<>();

    public AdeOrm() {}

    /**
     * Constructor for create an orm instance for one POJO class
     *
     * @param clazz the class of a POJO class
     * @return AdeOrm instance
     */
    public AdeOrm(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * Set connection to the database
     *
     * @param url jdbc driver + db_endpoint + schema_name
     * @return 0 for failure, 1 for success
     */
    public int setConnection(String url) {
        int success;
        if (url == null) {
            return 0;
        }
        success = ConnectionUtil.setConnection(url);
        return success;
    }

    /**
     * Get connection to the db, control the connection
     * autoCommit status
     *
     * @return Connection instance
     */
    public Connection getConnection() {
        conn = ConnectionUtil.getConnection();
        if (isTransaction) {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    /**
     * Set the transaction status to true
     */
    public void openTransaction() {
        isTransaction = true;
        completes.forEach(e -> e = true);
    }

    /**
     * Commit the transaction
     */
    public void commitTransaction() throws Exception {
        if (completes.contains(false)) {
            throw new Exception();
        } else {
            try {
                conn.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Roll back the transaction
     */
    public void rollbackTransaction() {
        try {
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the connection, reset default status
     */
    public void closeTransaction() {
        try {
            if (conn != null) {
                conn.close();
            }
            isTransaction = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean add(Object pojo) throws ArgumentFormatException{

        TableName table = clazz.getDeclaredAnnotation(TableName.class);
        String sql = "insert into " + table.tableName();

        Boolean primaryKeyIsIntCheck = false;
        int pkVal = -1;

        for(Field field : clazz.getDeclaredFields()){
            if(field.isAnnotationPresent(PrimaryKey.class)){
                Class<?> fieldType = field.getType();
                if(int.class.isAssignableFrom(fieldType)){
                    primaryKeyIsIntCheck = true;
                    String pkGetter = "get" + field.getName().substring(0,1).toUpperCase(Locale.ROOT) +
                            field.getName().substring(1);

                    try {
                        Method getPk = pojo.getClass().getMethod(pkGetter);
                        pkVal = (Integer) getPk.invoke(pojo);

                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        System.out.println(primaryKeyIsIntCheck);
        System.out.println(pkVal);

        List<FieldPair> pojoFieldPairs = MapperUtil.parseFields(pojo);
        Object[] fieldValues;
        String[] questionArray;
        String s;

        if (pkVal == 0) { // pkVal has been changed to 0 and is therefore default

            Object[] colNames = pojoFieldPairs.stream().filter(w -> !w.isPrimaryKey()).map(FieldPair::getName).toArray();
            sql += pojoFieldPairs.stream()
                    .filter(w -> !w.isPrimaryKey())
                    .map(FieldPair::getName)
                    .collect(Collectors.joining(", "," ( "," )"));

            System.out.println(sql);

            fieldValues = pojoFieldPairs.stream().filter(w -> !w.isPrimaryKey()).map(FieldPair::getValue).toArray();

            System.out.println(Arrays.toString(fieldValues));
            questionArray = new String[pojoFieldPairs.size() -1 ];

        }else{ // pkVal has been changed to non-zero and therefore sql statement needs to involve it

            fieldValues = pojoFieldPairs.stream().map(FieldPair::getValue).toArray();
            questionArray = new String[pojoFieldPairs.size()];

        }

        sql += " values (";

        Arrays.fill(questionArray, "?");
        s = Arrays.stream(questionArray).collect(Collectors.joining(", ", "", ");"));

        sql += s;
        System.out.println(sql);
        System.out.println(Arrays.toString(fieldValues));

        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, fieldValues);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throw new ArgumentFormatException("Arguments format are not correct", throwables);
        }

        return true;
    }


    public boolean add(String tableName, List<FieldPair> fieldPairs) throws ArgumentFormatException{
        if (tableName == null || fieldPairs == null){
            throw new ArgumentFormatException();
        }

        String sql = "insert into " + tableName + " values (";

        String[] questionArray = new String[fieldPairs.size()];
        Arrays.fill(questionArray, "?");
        String s;

        if(fieldPairs.size() > 1) {
            s = Arrays.stream(questionArray).collect(Collectors.joining(", ", "", ");"));
        }
        else{ s = Arrays.stream(questionArray).collect(Collectors.joining("","",");"));}

        sql += s;
        Object[] fieldValues = fieldPairs.stream().map(FieldPair::getValue).toArray();
        System.out.println(Arrays.toString(fieldValues));

        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, fieldValues);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throw new ArgumentFormatException("Arguments format are not correct", throwables);
        }
        return true;
    }


    /**
     *
     * Add a row to a database table using String tableName, List of fields (Key, values) for
     * populating the rows, and idCriteria to set primary key
     *
     * @param tableName table to be read
     * @param fieldPairs a list of field objects with a key and a value of field
     * @param idCriteria idCriteria, can either be a custom value, or it can be set to "default"
     *                   for default database primary key
     * @return
     * @throws ArgumentFormatException
     */

    public boolean add(String tableName, List<FieldPair> fieldPairs, int idCriteria) throws ArgumentFormatException{
        if (tableName == null || fieldPairs == null){
            throw new ArgumentFormatException();
        }

        String sql = "insert into " + tableName + " values (";

        String[] questionArray = new String[fieldPairs.size()];
        Arrays.fill(questionArray, "?");

        String s = Arrays.stream(questionArray).collect(Collectors.joining(", ","",");"));

        if(idCriteria == -1){
            sql += "default, " + s;
        }
        else {
            sql += idCriteria + ", " + s;
        }

        Object[] fieldValues = fieldPairs.stream().map(FieldPair::getValue).toArray();

        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, fieldValues);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throw new ArgumentFormatException("Arguments format are not correct", throwables);
        }
        return true;
    }


    /**
     * Update a generic type column value of a record by a primary key of any type
     *
     * @param tableName table to be updated
     * @param columnName name of column being updated
     * @param id column name of the primary key
     * @param idValue primary key value of a record to be updated
     * @param newColumnValue updating columnName w/ this value
     * @return
     */
    public boolean update(String tableName, String columnName, String id, Object idValue, Object newColumnValue) throws ArgumentFormatException {
        if (tableName == null || columnName == null || id == null || idValue == null) {
            return false;
        }
        String sql = "update " + tableName + " set " + columnName + "= ? " + " where " + id + "=?";

        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, newColumnValue, idValue);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
        return true;
    }


    /**
     * Update multiple generic type columns values of a record by a primary key of any type
     *
     * @param tableName table to be updated
     * @param fieldPairs columns being updated along with their values
     * @param pk column name and value of the primary key
     * @return
     */
    public boolean update(String tableName, List<FieldPair> fieldPairs, FieldPair pk) throws ArgumentFormatException {
        if (tableName == null || fieldPairs == null || pk == null) {
            return false;
        }

        String sql = "update " + tableName + " set ";

        sql += fieldPairs.stream().map(FieldPair::getName).collect(Collectors.joining(" = ? , ", "", " = ? "));
        sql += "where " + pk.getName() + " = " + pk.getValue() + ";";

        Object[] fieldValues = fieldPairs.stream().map(FieldPair::getValue).toArray();

        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, fieldValues);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
    }


    /**
     * delete a generic type column value of a record by a primary key of any type
     *
     * @param tableName table to be updated
     * @param id column name of the primary key
     * @param idValue primary key value of a record to be updated
     * @return
     */
    public boolean delete(String tableName, String id, Object idValue) throws ArgumentFormatException {
        if (tableName == null || id == null || idValue == null) {
            return false;
        }
        String sql = "delete from " + tableName + " where " + id + "=?";

        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, idValue);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
        return true;
    }


    public boolean delete(Object object) {
        if (object == null) return false;

        List<FieldPair> fieldPairList = MapperUtil.parseFields(object);
        String tableName = object.getClass().getSimpleName();
        String sql = "delete from ";

        for (int i = 0; i< fieldPairList.size(); i++) {
            if (fieldPairList.get(i).isPrimaryKey()) {
                String id = fieldPairList.get(i).getName();
                Object pk = fieldPairList.get(i).getValue();
                sql += tableName + " where " + id + " = " + pk;
            }
        }
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }


    /**
     * Get a record of a table by the primary key.
     *
     * @param pkName column name of the primary key
     * @param pkValue primary key value of a record to be retrieve
     * @return an object of the default pojo class for the record
     */
    @Override
    public Object get(String pkName, Object pkValue) throws ArgumentFormatException {
        if (pkName == null || pkValue == null) {
            return null;
        }
        if (!MapperUtil.isPrimaryKey(clazz, pkName)) {
            throw new ArgumentFormatException("The method only accepts using primary key to query");
        }

        TableName table = clazz.getDeclaredAnnotation(TableName.class);
        String sql = "select * from " + table.tableName() + " where " + pkName + "=?";
        Object object = null;
        try {
            Constructor<?> constructor = clazz.getConstructor();
            object = constructor.newInstance();

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
        Field[] fields = clazz.getDeclaredFields();
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, pkValue);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                for (int i = 0; i < fields.length; i++) {
                    ColumnName c = fields[i].getDeclaredAnnotation(ColumnName.class);
                    MapperUtil.setField(object, fields[i], rs.getString(c.columnName()));
                }
            }
        } catch (SQLException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
        return object;
    }


    /**
     * Get values of a record by primary key
     *
     * @param columnNames column(s) to be retrieve
     * @param pkName column name of the primary key
     * @param pkValue primary key value of a record to be retrieve
     * @return a list of field values required by user for a record
     */
    public List<Object> getColumns(String pkName, Object pkValue, String... columnNames) throws ArgumentFormatException {
        if (columnNames == null || pkName == null || pkValue == null) {
            return null;
        }
        if (!MapperUtil.isPrimaryKey(clazz, pkName)) {
            throw new ArgumentFormatException("The method only accepts using primary key to query");
        }

        TableName table = clazz.getDeclaredAnnotation(TableName.class);

        String s = Arrays.stream(columnNames).collect(Collectors.joining(", ","",""));
        String sql = "select " + s + " from " + table.tableName() + " where " + pkName + "=?";
        List<Object> result = new ArrayList<>();
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            Class<?> clazz = PreparedStatement.class;
            MapperUtil.setPs(ps, pkValue);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                for (int i = 0; i < columnNames.length; i++) {
                    result.add(rs.getString(columnNames[i]));
                }
            }
        } catch (SQLException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
        return result;
    }


    /**
     * Get record(s) filter by a column value.
     * If primary key or unique column is used, only return one record, else
     * return all records with the column value in the order specify
     *
     * @param columnNames a list of column names of the table to retrieve
     * @param fieldName a column name
     * @param fieldValue the column value of record(s) to be retrieve
     * @param orderCol the column to order by
     * @param order "asc" for ascending, "desc" for descending
     * @return a list of field values of records in specified order
     */
    public List<List<Object>> getRecordsInOrder(List<String> columnNames, String fieldName, Object fieldValue,
                                        String orderCol, String order) throws ArgumentFormatException {
        if (fieldName == null || fieldValue == null || orderCol == null || order == null) {
            return null;
        }
        if (!order.equals("asc") && !order.equals("desc")) {
            throw new ArgumentFormatException("Order must be in \"asc\" or \"desc\"");
        }

        TableName table = clazz.getDeclaredAnnotation(TableName.class);
        String colNames = String.join(", ", columnNames);

        String sql = "select " + colNames + " from " + table.tableName() + " where " + fieldName + "=?" +
                        " order by " + orderCol + " " + order;

        List<List<Object>> result = new ArrayList<>();
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, fieldValue);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                List<Object> record = new ArrayList<>();
                for (int i = 0; i < columnNames.size(); i++) {
                    record.add(rs.getString(columnNames.get(i)));
                }
                result.add(record);
            }
        } catch (SQLException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
        return result;
    }


    /**
     * Get all records in a table
     *
     * @return all records of the table
     */
    public List<Object> getAll() throws ArgumentFormatException {
        TableName table = clazz.getDeclaredAnnotation(TableName.class);
        String sql = "select * from " + table.tableName();
        Object object = null;
        Constructor<?> constructor;
        Field[] fields = clazz.getDeclaredFields();

        List<Object> result = new ArrayList<>();
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            constructor = clazz.getConstructor();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                object = constructor.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    ColumnName c = fields[i].getDeclaredAnnotation(ColumnName.class);
                    MapperUtil.setField(object, fields[i], rs.getString(c.columnName()));
                }
                result.add(object);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new ArgumentFormatException("Argument formats are not correct", e);
        }
        return result;
    }


    /**
     * Get generic type columns' values of all records in a table in order
     *
     * @param orderCol the column to order by
     * @param order "asc" for ascending, "desc" for descending
     * @return all records in specified order
     */
    public List<Object> getAllInOrder(String orderCol, String order) throws ArgumentFormatException {
        if (orderCol == null || order == null) {
            return null;
        }
        if (!order.equals("asc") && !order.equals("desc")) {
            throw new ArgumentFormatException("Order must be in \"asc\" or \"desc\"");
        }

        TableName table = clazz.getDeclaredAnnotation(TableName.class);
        String sql = "select * from " + table.tableName() + " order by " + orderCol + " " + order;
        Object object = null;
        Constructor<?> constructor;
        Field[] fields = clazz.getDeclaredFields();
        List<Object> result = new ArrayList<>();
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            constructor = clazz.getConstructor();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                object = constructor.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    ColumnName c = fields[i].getDeclaredAnnotation(ColumnName.class);
                    MapperUtil.setField(object, fields[i], rs.getString(c.columnName()));
                }
                result.add(object);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new ArgumentFormatException("Argument formats are not correct", e);
        }
        return result;
    }


    /**
     * Get specific columns of records filter by a list of fields (key, value) pairs under
     * "and" or "or" relationship.
     *
     * @param columnNames a list of column names of the table to retrieve
     * @param fieldPairs a list of Field objects with a key and a value of a field
     * @param criterion "and" or "or" to specific relationship between field
     * @return a list of field values of records fulfill the criterion
     */
    public List<List<Object>> getWithCriterion(List<String> columnNames,
                                  List<FieldPair> fieldPairs, String criterion) throws ArgumentFormatException {
        if (columnNames == null || fieldPairs == null || criterion == null) {
            return null;
        }
        TableName table = clazz.getDeclaredAnnotation(TableName.class);
        String colNames = String.join(", ", columnNames);
        String sql = "select " + colNames + " from " + table.tableName() + " where ";

        if (criterion.equals("and")) {
            sql += fieldPairs.stream().map(FieldPair::getName).collect(Collectors.joining("=? and ")) + "=?";
        }

        if (criterion.equals("or")) {
            sql += fieldPairs.stream().map(FieldPair::getName).collect(Collectors.joining("=? or ")) + "=?";
        }

        Object[] fieldValues = fieldPairs.stream().map(FieldPair::getValue).toArray();
        List<List<Object>> result = new ArrayList<>();
        Method method = null;

        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, fieldValues);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                List<Object> record = new ArrayList<>();
                for (int i = 0; i < columnNames.size(); i++) {
                    record.add(rs.getString(columnNames.get(i)));
                }
                result.add(record);
            }
        } catch (SQLException e) {
            throw new ArgumentFormatException("Argument formats are not correct", e);
        }
        return result;
    }


    /**
     * Get all records of join-tables.
     *
     * @param jType inner, left, right
     * @param tableB right table to be join
     * @param pkA primary key of left table
     * @param fkA foreign key of right table reference left table
     * @param columnNames a list of column names of the table to retrieve
     * @return a list of all records of join-tables
     */
    public List<List<Object>> getJoint(String jType, String pkA, String tableB, String fkA,
                                       List<String> columnNames) throws ArgumentFormatException {
        if (pkA == null || tableB == null || fkA == null || columnNames == null) {
            return null;
        }

        TableName tableA = clazz.getDeclaredAnnotation(TableName.class);

        String colNames = String.join(", ", columnNames);
        String sql = "select " + colNames + " from " + tableA.tableName() + " " + jType + " join " + tableB +
                " on " + pkA + " = " + fkA;
        List<List<Object>> result = new ArrayList<>();
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                List<Object> record = new ArrayList<>();
                for (int i = 0; i < columnNames.size(); i++) {
                    record.add(rs.getString(columnNames.get(i)));
                }
                result.add(record);
            }
        } catch (SQLException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
        return result;
    }


    /**
     * Get record(s) of joint tables filter by a column value.
     *
     * @param jType inner, left, right
     * @param tableB right table to be join
     * @param pkA primary key of default table
     * @param fkA foreign key of right table reference left table
     * @param columnNames a list of column names of the table to retrieve
     * @param fieldName a column name
     * @param fieldValue the column value of record(s) to be retrieve
     * @return a list of field values of records of join-tables fulfill the criterion
     */
    public List<List<Object>> getJointWhere(String jType, String pkA, String tableB, String fkA,
                                 List<String> columnNames, String fieldName, Object fieldValue) throws ArgumentFormatException {
        if (pkA == null || tableB == null || fkA == null || columnNames == null ||
                fieldName == null || fieldValue == null) {
            return null;
        }

        TableName tableA = clazz.getDeclaredAnnotation(TableName.class);
        String colNames = String.join(", ", columnNames);
        String sql = "select " + colNames + " from " + tableA.tableName() + " " + jType + " join " + tableB +
                " on " + pkA + " = " + fkA + " where " + fieldName + "=?";
        List<List<Object>> result = new ArrayList<>();
        try(Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, fieldValue);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                List<Object> record = new ArrayList<>();
                for (int i = 0; i < columnNames.size(); i++) {
                    record.add(rs.getString(columnNames.get(i)));
                }
                result.add(record);
            }
        } catch (SQLException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
        return result;
    }


    public boolean update2(String columnName, String id, Object idValue, Object newColumnValue) throws ArgumentFormatException {
        if (columnName == null || id == null || idValue == null) {
            return false;
        }
        TableName table = clazz.getDeclaredAnnotation(TableName.class);
        String sql = "update " + table.tableName() + " set " + columnName + "= ? " + " where " + id + "=?";

        if (!isTransaction) {
            try (Connection conn = getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                MapperUtil.setPs(ps, newColumnValue, idValue);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new ArgumentFormatException("Arguments format are not correct", e);
            }
        } else {
            PreparedStatement ps = null;
            try {
                Connection conn = getConnection();
                ps = conn.prepareStatement(sql);
                MapperUtil.setPs(ps, newColumnValue, idValue);
                if (ps.executeUpdate() == 0) {
                    completes.add(false);
                }
            } catch (SQLException e) {
                throw new ArgumentFormatException("Arguments format are not correct", e);
            } finally {
                try {
                    assert ps != null;
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

}
