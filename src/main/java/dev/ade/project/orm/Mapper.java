package dev.ade.project.orm;

import dev.ade.project.exception.ArgumentFormatException;

import java.util.List;

public interface Mapper {
    List<Object> get(String tableName, List<String> columnNames, String pkName, Object pkValue) throws ArgumentFormatException;
//    return 1 for success, 0 for fail
//    int add(String tableName, List<Field> fields, String pkName, Object pkValue) throws ArgumentFormatException;
    boolean add(String tableName, List<Field> fields, int idCriteria) throws ArgumentFormatException;
//    int update(String tableName, List<Field> fields, String pkName, Object pkValue) throws ArgumentFormatException;
//    int delete(String tableName, String pkName, Object pkValue) throws ArgumentFormatException;
}
