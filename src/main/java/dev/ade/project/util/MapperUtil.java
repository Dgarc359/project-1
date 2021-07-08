package dev.ade.project.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.ade.project.orm.Field;

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

    public static List<Field> parseObject(Object object) {
        List<Field> fieldList = new ArrayList<>();
        Class<?> objectClass = object.getClass();
        /*String className = objectClass.getSimpleName().toLowerCase(Locale.ROOT);

        Field table = new Field("tableName", className);
        fieldList.add(table);*/

        java.lang.reflect.Field[] fields = objectClass.getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            String fieldName = field.getName();

            // access method(s)? --- getId(), getName(), getBirthday()
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
            // maybe we can access the getter directly based on the field name
            try {
                Method getterMethod = objectClass.getMethod(getterName);

                // if we invoke the getter, we get the field value
                Object fieldValue = getterMethod.invoke(object);

                Field newField = new Field(fieldName, fieldValue);
                fieldList.add(newField);

            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fieldList;
    }

}
