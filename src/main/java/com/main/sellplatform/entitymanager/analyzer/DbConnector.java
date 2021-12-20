package com.main.sellplatform.entitymanager.analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class DbConnector {
    /*private final String db_name = "XE";
    private final String url = "jdbc:oracle:thin:@localhost:1521/" + db_name;
    private Connection con;
    private final String user = "system", password = "p1234";*/

    private final Connection connection;

//    @Autowired
//    public DbConnector(@Qualifier("myConnection") Connection connection, JdbcTemplate jdbcTemplate) {
//        this.connection = connection;
//    }


    @Autowired
    public DbConnector(Connection connection) throws SQLException {
        this.connection = connection;
    }


    /*public void connect() {
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }*/

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeGet(String sql) {
        System.out.println("executeGet, executeGet" + sql);
        try {
            ResultSet rs;
            connection.setReadOnly(true);
            Statement stat = connection.createStatement();
            rs = stat.executeQuery(sql);
            return rs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ResultSet executeGet(String sql, List<Object> statements) {
        try {
            ResultSet rs;
            PreparedStatement st = connection.prepareStatement(sql);
            if(statements!=null)
                for (int i = 0; i < statements.size(); ++i) {
                    st.setObject(i + 1, statements.get(i));
                }
            rs = st.executeQuery();
            st.closeOnCompletion();
            return rs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void test() {
        try {
            String sql = "Select * from waybill";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){

            }
            System.out.println(sql);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    public boolean getId(Long id) {
        Long object = null;
        try {
            String sql = "select * from OBJECTS where OBJECT_ID = "  + id;
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                object = rs.getLong(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return !Objects.isNull(object);
    }


    public Long saveObjects(String sql, List<Object> values, String columnId){
        System.out.println("Prepared statement");
        System.out.println(sql);
        String returnCols[] = { columnId };
        Long id = null;
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql, returnCols)) {
            for(int i = 0;i < values.size(); i++){
                if (values.get(i) instanceof Integer){

                    preparedStatement.setInt(i+1,(Integer) values.get(i));

                }
                if(values.get(i) instanceof Long){

                    preparedStatement.setLong(i+1, (Long) values.get(i));

                }
                if (values.get(i) instanceof String){

                    preparedStatement.setNString(i+1, (String) values.get(i));

                }
            }
            preparedStatement.execute();
            if(Objects.isNull(columnId)){
                return null;
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                id = generatedKeys.getLong(1);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }


    public void updateObject(String sql , List<Object> values){
        System.out.println("update");
        System.out.println(sql);
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for(int i = 0;i < values.size(); i++){
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

            }
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }




    public void saveObjectsNotId(String sql, List<Object> values){
        System.out.println("Prepared statement");
        System.out.println(sql);
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for(int i = 0;i < values.size(); i++){
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

            }
            preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }



    public void deleteObjectFromObjReference(String sql , Object id){
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (id instanceof Long){
                preparedStatement.setLong(1,(Long) id);
            }
            else if(id instanceof Integer){
                preparedStatement.setInt(1, (Integer) id);
            }
            int i = preparedStatement.executeUpdate();
            System.out.println("row -> " + i);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }











}
