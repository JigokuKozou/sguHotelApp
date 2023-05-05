package ru.shchelkin.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class Dao<T> {
    private final Class<T> entityClass;
    private final String tableName;
    private final JdbcTemplate jdbcTemplate;


    public Dao(Class<T> entityClass, JdbcTemplate jdbcTemplate) {
        this.entityClass = entityClass;
        this.tableName = entityClass.getSimpleName().toLowerCase() + 's';
        this.jdbcTemplate = jdbcTemplate;
    }

    public Class<T> getObjectClass() {
        return entityClass;
    }

    public List<T> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM " + tableName,
                new BeanPropertyRowMapper<>(entityClass));
    }

    public void save(T entity) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        namedParameterJdbcTemplate.update(getInsertQuery(), parameterSource);
    }

    public void update(T entity) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        namedParameterJdbcTemplate.update(getUpdateQuery(), parameterSource);
    }

    public void delete(T entity) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        namedParameterJdbcTemplate.update(getDeleteQuery(), parameterSource);
    }

    private String[] getFieldNames(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .filter(fieldName -> !fieldName.equals("id"))
                .toArray(String[]::new);
    }

    private String[] getUpdateFields(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(Field::getName)
                .filter(fieldName -> !fieldName.equals("id"))
                .toArray(String[]::new);
    }

    private String getInsertQuery() {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ")
                .append(tableName).append(" (");

        String[] fields = getFieldNames(entityClass);
        for (String fieldName : fields) {
            queryBuilder.append(fieldName).append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append(") VALUES (");

        for (String field : fields) {
            queryBuilder.append(":").append(field).append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append(")");

        return queryBuilder.toString();
    }

    private String getUpdateQuery() {
        StringBuilder queryBuilder = new StringBuilder("UPDATE ")
                .append(tableName).append(" SET ");

        String[] fields = getUpdateFields(entityClass);
        for (String field : fields) {
            queryBuilder.append(field).append("=:").append(field).append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append(" WHERE id=:id");

        return queryBuilder.toString();
    }

    private String getDeleteQuery() {
        return "DELETE FROM " + tableName + " WHERE id=:id";
    }

}
