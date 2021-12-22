package com.main.sellplatform.entitymanager.analyzer;

import com.main.sellplatform.persistence.entity.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.SavepointManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class DbConnector2 {


    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertInto;
    private final Queries queries;
    private final DbConnector dbConnector;


    @Autowired
    public DbConnector2(DataSource dataSource, Queries queries, DbConnector dbConnector) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertInto = new SimpleJdbcInsert(jdbcTemplate);
        this.queries = queries;
        this.dbConnector = dbConnector;
    }

    @Transactional
    public boolean getId(Long id){
        String sql = "select count (*) from OBJECTS where OBJECT_ID =  ?";
        Long object = jdbcTemplate.queryForObject(sql, Long.class, id);
        return object > 0;
    }


    @Transactional
    public Long saveObjects(String sql, List<Object> values, String columnId){

        System.out.println(sql);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
           PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[] {columnId});
           for(int i = 0;i < values.size(); i++){
               System.out.println("values for -> " + values.get(i));
                if(values.get(i) instanceof String){
                    preparedStatement.setString(i+1, (String) values.get(i));
                }
                if (values.get(i) instanceof Integer){
                    preparedStatement.setInt(i+1,(Integer) values.get(i));
                }
                if(values.get(i) instanceof Long){
                    preparedStatement.setLong(i+1, (Long) values.get(i));

                }
                if (values.get(i) instanceof String){
                    preparedStatement.setNString(i+1, (String) values.get(i));

                }
                if(values.get(i) instanceof java.util.Date){
                    preparedStatement.setDate(i+1,  new java.sql.Date( ((Date) values.get(i)).getTime()));
                }
                if (values.get(i) instanceof Double){
                    preparedStatement.setDouble(i+1, (Double) values.get(i));

                }
                if(Objects.isNull(values.get(i))){
                    preparedStatement.setNull(i+1, Types.NULL );
                }

            }
           return preparedStatement;
        },  keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }



    @Transactional
    public void updateObject(String sql , List<Object> values){
        System.out.println(sql);
        Object[] objects = values.stream().map(value -> {
            if (value instanceof Enum) {
                return ((Enum) value).name();
            }
            return value;
        }).toArray();
        jdbcTemplate.update(sql, objects);

    }

    @Transactional
    public void saveObjectsNotId(String sql, List<Object> values){
        System.out.println(sql + "\n");
        Object[] objects = values.stream().map(value -> {
            if (value instanceof Enum) {
                return ((Enum) value).name();
            }
            return value;
        }).toArray();
        Arrays.stream(objects).forEach(System.out::println);
        jdbcTemplate.update(sql, objects);
    }

    @Transactional
    public void deleteObjectFromObjReference(String sql, Object id){
        System.out.println(sql);
        jdbcTemplate.update(sql, id);
    }

    @Transactional
    public void deleteObject(Long id) {
        System.out.println("id = " + id);
        jdbcTemplate.update(queries.deleteReferences(), id, id);
        jdbcTemplate.update(queries.deleteAttributes(), id);
        deleteParent(id);
        jdbcTemplate.update(queries.deleteChilds(), id);
        int res = jdbcTemplate.update(queries.deleteObjects(), id);
        if (res < 1) {
            throw new IllegalArgumentException("No such object in the DB");
        }
    }

    @Transactional
    private void deleteParent(Long parentId) {
        List<Long> ids = dbConnector.getParentsIds(parentId);
        for (Long id : ids) {
            deleteObject(id);
        }
    }

}
