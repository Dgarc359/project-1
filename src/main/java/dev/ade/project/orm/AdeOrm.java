package dev.ade.project.orm;

import dev.ade.project.annotations.ColumnName;
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
    private static Connection conn;
    // POJO class mirror with a table in the db
    private Class<?> clazz;

    public AdeOrm() {
    }

    public AdeOrm(Class<?> clazz) {
        this.clazz = clazz;
    }

    // static method to set the base connection of all orm instances to the db
    public static void setConnection(String url, String username, String password) {
        conn = ConnectionUtil.getConnection(url, username, password);
    }

    public static Connection getConn() {
        return conn;
    }

    /**
     * Get a generic type column value of a record by a primary key of any type
     *
     * @param tableName table to be read
     * @param columnName column to be retrieve
     * @param pkName column name of the primary key
     * @param pkValue primary key value of a record to be retrieve
     * @return
     */
    public <T> T get(String tableName, String columnName, String pkName, Object pkValue) throws ArgumentFormatException {
        if (tableName == null || columnName == null || pkName == null || pkValue == null) {
            return null;
        }
        String sql = "select " + columnName + " from " + tableName + " where " + pkName + "=?";
        T result = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            Class<?> clazz = PreparedStatement.class;
            MapperUtil.setPs(ps, pkValue);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = (T)rs.getString(columnName);
            }
        } catch (SQLException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
        return result;
    }



    /**
     * Get a record of a table by the primary key.
     *
     * @param pkName column name of the primary key
     * @param pkValue primary key value of a record to be retrieve
     * @return
     */
    public Object getById(String pkName, Object pkValue) throws ArgumentFormatException {
        if (pkName == null || pkValue == null) {
            return null;
        }
        TableName table = clazz.getDeclaredAnnotation(TableName.class);
        String sql = "select * from " + table.key() + " where " + pkName + "=?";
        Object object = null;
        try {
            Constructor<?> constructor = clazz.getConstructor();
            object = constructor.newInstance();

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
        Field[] fields = clazz.getDeclaredFields();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            MapperUtil.setPs(ps, pkValue);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                for (int i = 0; i < fields.length; i++) {
                    ColumnName c = fields[i].getDeclaredAnnotation(ColumnName.class);
                    MapperUtil.setField(object, fields[i], rs.getString(c.key()));
                }
            }
        } catch (SQLException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
        return object;
    }

    /**
     * Get generic type columns' values of record(s) by a column value of any type.
     * If the primary key is used, only return one record, if other non unique value
     * column is used, return all records with the column value
     *
     * @param tableName table to be read
     * @param columnNames a list of column names of the table to retrieve
     * @param fieldName a column name
     * @param fieldValue the column value of record(s) to be retrieve
     * @return
     */

    public List<List<Object>> get(String tableName, List<String> columnNames, Object fieldValue, String fieldName) throws ArgumentFormatException {
        if (tableName == null || columnNames == null || fieldName == null || fieldValue == null) {
            return null;
        }
        String colNames = String.join(", ", columnNames);
        String sql = "select " + colNames + " from " + tableName + " where " + fieldName + "=?";
        List<List<Object>> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
     * Get generic type columns' values of record(s) by a column value of any type.
     * If the primary key is used, only return one record, if other non unique value
     * column is used, return all records with the column value in the order specify
     *
     * @param tableName table to be read
     * @param columnNames a list of column names of the table to retrieve
     * @param fieldName a column name
     * @param fieldValue the column value of record(s) to be retrieve
     * @param orderCol the column to order by
     * @param order "asc" for ascending, "desc" for descending
     * @return
     */

    public List<List<Object>>get(String tableName, List<String> columnNames, Object fieldValue,
                                 String fieldName, String orderCol, String order) throws ArgumentFormatException {
        if (tableName == null || columnNames == null || fieldName == null || fieldValue == null ||
                orderCol == null || order == null) {
            return null;
        }
        String colNames = String.join(", ", columnNames);
        String sql = "select " + colNames + " from " + tableName + " where " + fieldName + "=?";
        sql += " order by " + orderCol + " " + order;
        List<List<Object>> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
     * Get generic type columns' values of all records in a table in order
     *
     * @param tableName the name of a table
     * @param orderCol the column to order by
     * @param order "asc" for ascending, "desc" for descending
     * @return
     */
    public List<List<Object>>get(String tableName, List<String> columnNames, String orderCol, String order) throws ArgumentFormatException {
        if (tableName == null || columnNames == null || orderCol == null || order == null) {
            return null;
        }
        String colNames = String.join(", ", columnNames);
        String sql = "select " + colNames + " from " + tableName + " order by " + orderCol + " " + order;
        List<List<Object>> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
     * Get generic type columns' values of all records in a table
     *
     * @return
     */
    public List<Object> getAll() throws ArgumentFormatException {
        TableName table = clazz.getDeclaredAnnotation(TableName.class);
        String sql = "select * from " + table.key();
        Object object = null;
        Constructor<?> constructor;
        Field[] fields = clazz.getDeclaredFields();

        List<Object> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            constructor = clazz.getConstructor();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                object = constructor.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    ColumnName c = fields[i].getDeclaredAnnotation(ColumnName.class);
                    MapperUtil.setField(object, fields[i], rs.getString(c.key()));
                }
                result.add(object);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new ArgumentFormatException("Argument formats are not correct", e);
        }
        return result;
    }

    /**
     * Get generic type values of record(s) by a list of fields (key, value) pairs under
     * "and" or "or" relationship.
      *
     * @param tableName table to be read
     * @param columnNames a list of column names of the table to retrieve
     * @param fieldPairs a list of Field objects with a key and a value of a field
     * @param criteria "and" or "or" to specific the fields criteria
     * @return
     */
    public List<List<Object>> get(String tableName, List<String> columnNames,
                                  List<FieldPair> fieldPairs, String criteria) throws ArgumentFormatException {
        if (tableName == null || columnNames == null || fieldPairs == null || criteria == null) {
            return null;
        }
        String colNames = String.join(", ", columnNames);
        String sql = "select " + colNames + " from " + tableName + " where ";

        if (criteria.equals("and")) {
            String s = fieldPairs.stream().map(FieldPair::getName).collect(Collectors.joining("=? and "));
            sql += s + "=?";
        }

        if (criteria.equals("or")) {
            String s = fieldPairs.stream().map(FieldPair::getName).collect(Collectors.joining("=? or "));
            sql += s + "=?";
        }

        Object[] fieldValues = fieldPairs.stream().map(FieldPair::getValue).toArray();
        List<List<Object>> result = new ArrayList<>();
        Method method = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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

        try(PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, fieldValues);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throw new ArgumentFormatException("Arguments format are not correct", throwables);
        }
        return true;
    }

    /**
     * Get generic type columns' values of record(s) of joint tables by a column value of any type.
     *
     * @param jType inner, left, right
     * @param tableA left table to be join
     * @param tableB right table to be join
     * @param pkA primary key of left table
     * @param fkA foreign key of right table reference left table
     * @param columnNames a list of column names of the table to retrieve
     * @param fieldName a column name
     * @param fieldValue the column value of record(s) to be retrieve
     * @return
     */

    public List<List<Object>>get(String jType, String tableA, String pkA, String tableB, String fkA,
                                 List<String> columnNames, String fieldName, Object fieldValue) throws ArgumentFormatException {
        if (tableA == null || pkA == null || tableB == null || fkA == null || columnNames == null ||
                fieldName == null || fieldValue == null) {
            return null;
        }
        String colNames = String.join(", ", columnNames);
        String sql = "select " + colNames + " from " + tableA + " " + jType + " join " + tableB +
                " on " + pkA + " = " + fkA + " where " + fieldName + "=?";
        List<List<Object>> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
     * Get generic type columns' values of record(s) of joint tables.
     *
     * @param jType inner, left, right
     * @param tableA left table to be join
     * @param tableB right table to be join
     * @param pkA primary key of left table
     * @param fkA foreign key of right table reference left table
     * @param columnNames a list of column names of the table to retrieve
     *
     * @return
     */

    public List<List<Object>>get(String jType, String tableA, String pkA, String tableB, String fkA,
                                 List<String> columnNames) throws ArgumentFormatException {
        if (tableA == null || pkA == null || tableB == null || fkA == null || columnNames == null) {
            return null;
        }
        String colNames = String.join(", ", columnNames);
        String sql = "select " + colNames + " from " + tableA + " " + jType + " join " + tableB +
                " on " + pkA + " = " + fkA;
        List<List<Object>> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            MapperUtil.setPs(ps, newColumnValue, idValue);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
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

        try(PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, fieldValues);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throw new ArgumentFormatException("Arguments format are not correct", throwables);
        }
        return true;
    }

    public boolean add(Object pojo) throws ArgumentFormatException{


        List<FieldPair> pojoFieldPairs = MapperUtil.parseFields(pojo);
        Object[] fieldValues = pojoFieldPairs.stream().map(FieldPair::getValue).toArray();
        String sql = "insert into " + pojo.getClass().getSimpleName().toLowerCase(Locale.ROOT) +" values(";

        System.out.println(Arrays.toString(fieldValues));

        String[] questionArray = new String[pojoFieldPairs.size()];
        Arrays.fill(questionArray, "?");
        String s;

        s = Arrays.stream(questionArray).collect(Collectors.joining(", ", "", ");"));

        sql += s;
        System.out.println(sql);

        try(PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, fieldValues);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throw new ArgumentFormatException("Arguments format are not correct", throwables);
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

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
     * @param tableName table to be deleted
     * @param id column name of the primary key
     * @param idValue primary key value of a record to be deleted
     * @return
     */
    public boolean delete(String tableName, String id, Object idValue) throws ArgumentFormatException {
        if (tableName == null || id == null || idValue == null) {
            return false;
        }
        String sql = "delete from " + tableName + " where " + id + "=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            MapperUtil.setPs(ps, idValue);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
        return true;
    }

    /**
     * delete a record from the db given an object
     *
     * @param object object representation of record to be deleted
     * @return
     */
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
        try (Statement s = conn.createStatement()){
            s.execute(sql);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
