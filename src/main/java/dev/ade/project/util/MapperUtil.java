package dev.ade.project.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dev.ade.project.annotations.ColumnName;
import dev.ade.project.annotations.PrimaryKey;

import dev.ade.project.orm.FieldPair;

public class MapperUtil {

    public static int setPs (PreparedStatement ps, Object... fieldValues) throws SQLException {
        if (ps == null || fieldValues == null) {
            return 0;
        }
        int i = 1;
        for (Object value : fieldValues) {
            if (value instanceof Boolean) {
                ps.setBoolean(i++, (Boolean) value);
            } else if (value instanceof Byte) {
                ps.setByte(i++, (Byte) value);
            } else if (value instanceof Short) {
                ps.setShort(i++, (Short) value);
            } else if (value instanceof Integer) {
                ps.setInt(i++, (Integer) value);
            } else if (value instanceof Long) {
                ps.setLong(i++, (Long) value);
            } else if (value instanceof Float) {
                ps.setFloat(i++, (Float) value);
            } else if (value instanceof Double) {
                ps.setDouble(i++, (Double) value);
            } else if (value instanceof BigDecimal) {
                ps.setBigDecimal(i++, (BigDecimal) value);
            } else if (value instanceof LocalDate) {
                ps.setDate(i++, Date.valueOf((LocalDate) value));
            } else if (value instanceof LocalDateTime) {
                ps.setTimestamp(i++, Timestamp.valueOf((LocalDateTime) value));
            } else if (value instanceof Clob) {
                ps.setClob(i++, (Clob) value);
            } else if (value instanceof Blob) {
                ps.setBlob(i++, (Blob) value);
            } else if (value instanceof Array) {
                ps.setArray(i++, (Array) value);
            } else if (value instanceof String) {
                ps.setString(i++, (String) value);
            } else {
                return 0;
            }
        }
        return 1;
    }

    public static List<FieldPair> parseFields(Object object) {
        List<FieldPair> fieldPairList = new ArrayList<>();
        Class<?> objectClass = object.getClass();

        Field[] fields = objectClass.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String columnName = "";
            boolean isPrimaryKey = false;
            Annotation a = field.getDeclaredAnnotation(PrimaryKey.class);
            Annotation b = field.getDeclaredAnnotation(ColumnName.class);
            PrimaryKey pk = (PrimaryKey)a;
            ColumnName cn = (ColumnName)b;
            if (pk!=null) {
                isPrimaryKey = true;
            }

            if (cn!=null) {
                columnName = cn.key();
            }

            String getterName = field.getType().getSimpleName().matches("boolean") ?
                    "is" + fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1) :
                    "get" + fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);

            try {
                Method getterMethod = objectClass.getMethod(getterName);
                Object fieldValue = getterMethod.invoke(object);
                FieldPair newFieldPair = new FieldPair(columnName, fieldValue, isPrimaryKey);
                fieldPairList.add(newFieldPair);

            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fieldPairList;
    }

}
