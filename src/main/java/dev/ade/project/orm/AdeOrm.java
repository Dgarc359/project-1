package dev.ade.project.orm;

import dev.ade.project.exception.ArgumentFormatException;
import dev.ade.project.util.MapperUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdeOrm implements Mapper {
    private Connection conn;

    public AdeOrm() {
    }

    public AdeOrm(Connection connection) {
        conn = connection;
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
     * Get generic type columns' values of a record by a primary key of any type.
     *
     * @param tableName table to be read
     * @param columnNames a list of column names of the table to retrieve
     * @param pkName column name of the primary key
     * @param pkValue primary key value of a record to be retrieve
     * @return
     */
    @Override
    public List<Object>get(String tableName, List<String> columnNames, String pkName, Object pkValue) throws ArgumentFormatException {
        if (tableName == null || columnNames == null || pkName == null || pkValue == null) {
            return null;
        }
        String colNames = String.join(", ", columnNames);
        String sql = "select " + colNames + " from " + tableName + " where " + pkName + "=?";
        List<Object> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            MapperUtil.setPs(ps, pkValue);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                for (int i = 0; i < columnNames.size(); i++) {
                    result.add(rs.getString(columnNames.get(i)));
                }
            }
        } catch (SQLException e) {
            throw new ArgumentFormatException("Arguments format are not correct", e);
        }
        return result;
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

    public List<List<Object>>get(String tableName, List<String> columnNames, Object fieldValue, String fieldName) throws ArgumentFormatException {
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
     * Get generic type columns' values of all records in a table
     *
     * @param tableName the name of a table
     * @return
     */
    public List<List<Object>>get(String tableName, List<String> columnNames) throws ArgumentFormatException {
        if (tableName == null || columnNames == null) {
            return null;
        }
        String colNames = String.join(", ", columnNames);
        String sql = "select " + colNames + " from " + tableName;
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
     * Get generic type values of record(s) by a list of fields (key, value) pairs under
     * "and" or "or" relationship.
      *
     * @param tableName table to be read
     * @param columnNames a list of column names of the table to retrieve
     * @param fields a list of Field objects with a key and a value of a field
     * @param criteria "and" or "or" to specific the fields criteria
     * @return
     */
    public List<List<Object>> get(String tableName, List<String> columnNames,
                                         List<Field> fields, String criteria) throws ArgumentFormatException {
        if (tableName == null || columnNames == null || fields == null || criteria == null) {
            return null;
        }
        String colNames = String.join(", ", columnNames);
        String sql = "select " + colNames + " from " + tableName + " where ";

        if (criteria.equals("and")) {
            String s = fields.stream().map(Field::getName).collect(Collectors.joining("=? and "));
            sql += s + "=?";
        }
        if (criteria.equals("or")) {
            String s = fields.stream().map(Field::getName).collect(Collectors.joining("=? or "));
            sql += s + "=?";
        }

        Object[] fieldValues = fields.stream().map(Field::getValue).toArray();
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
     * @param fields a list of field objects with a key and a value of field
     * @param idCriteria idCriteria, can either be a custom value, or it can be set to "default"
     *                   for default database primary key
     * @return
     * @throws ArgumentFormatException
     */


    @Override
    public boolean add(String tableName, List<Field> fields, String idCriteria) throws ArgumentFormatException{
        if (tableName == null || fields == null || idCriteria == null){
            throw new ArgumentFormatException();
        }

        String sql = "insert into " + tableName + " values (";

        if(idCriteria.equals("default")){
            String[] questionArray = new String[fields.size()];
            Arrays.fill(questionArray, "?");

            String s = Arrays.stream(questionArray).collect(Collectors.joining(", ","",");"));
            sql += "default, " + s;
        }
        else { return false; }

        Object[] fieldValues = fields.stream().map(Field::getValue).toArray();

        try(PreparedStatement ps = conn.prepareStatement(sql)){
            MapperUtil.setPs(ps, fieldValues);

            ps.executeUpdate();

        } catch (SQLException throwables) {
            throw new ArgumentFormatException("Arguments format are not correct", throwables);
        }
        return true;
    }

    // Add multiple users

    /*public boolean add(String tableName, List<List<String>> fields) throws ArgumentFormatException{
        if (tableName == null || fields == null){ return false; }
        String sql = "insert into " + tableName + " values ";
        String s =

        return false;
    }*/

}
