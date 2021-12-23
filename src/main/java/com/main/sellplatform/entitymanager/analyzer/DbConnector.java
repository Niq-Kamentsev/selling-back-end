package com.main.sellplatform.entitymanager.analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class DbConnector {
    /*private final String db_name = "XE";
    private final String url = "jdbc:oracle:thin:@localhost:1521/" + db_name;
    private Connection con;
    private final String user = "system", password = "p1234";*/

    private final Connection con;
    private final Queries queries;

    @Autowired
    public DbConnector(Connection con, Queries queries) {
        this.con = con;
        this.queries = queries;
    }



    public void disconnect() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeGet(String sql) {
        try {
            ResultSet rs;
            Statement stat = con.createStatement();
            rs = stat.executeQuery(sql);
            stat.closeOnCompletion();
            return rs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ResultSet executeGet(String sql, List<Object> statements) {
        try {
            ResultSet rs;
            PreparedStatement st = con.prepareStatement(sql);
            if(statements!=null)
                for (int i = 0; i < statements.size(); ++i) {
                    st.setObject(i + 1, statements.get(i));
                }
            rs = st.executeQuery();
            return rs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void executeQuery(String sql, List<Object> statements) {
        try {
//            Statement stat = con.createStatement();
//            String[] sqls = sql.split(";");
//            for (String sq : sqls) {
//                stat.executeUpdate(sq);
//            }
            PreparedStatement st = con.prepareStatement(sql);
            if(statements!=null)
                for (int i = 0; i < statements.size(); ++i) {
                    st.setObject(i + 1, statements.get(i));
                }
            st.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void executeQuery(String sql) {
        try {
            Statement stat = con.createStatement();
            String[] sqls = sql.split(";");
            for (String sq : sqls) {
                stat.executeUpdate(sq);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void test() {
        try {
            String sql = "Select * from waybill";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {

            }
            System.out.println(sql);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    public boolean getId(Long id) {
        Long object = null;
        try {
            String sql = "select * from OBJECTS where OBJECT_ID = " + id;
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                object = rs.getLong(1);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return !Objects.isNull(object);
    }


    public Long saveObjects(String sql, List<Object> values, String columnId) {
        System.out.println("Prepared statement");
        System.out.println(sql);
        String returnCols[] = {columnId};
        Long id = null;
        try (PreparedStatement preparedStatement = con.prepareStatement(sql, returnCols)) {
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i) instanceof Integer) {

                    preparedStatement.setInt(i + 1, (Integer) values.get(i));

                }
                if (values.get(i) instanceof Long) {

                    preparedStatement.setLong(i + 1, (Long) values.get(i));

                }
                if (values.get(i) instanceof String) {

                    preparedStatement.setNString(i + 1, (String) values.get(i));

                }
            }
            preparedStatement.execute();
            if (Objects.isNull(columnId)) {
                return null;
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getLong(1);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }


    public void updateObject(String sql, List<Object> values) {
        System.out.println("update");
        System.out.println(sql);
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i) instanceof Integer) {
                    preparedStatement.setInt(i + 1, (Integer) values.get(i));
                }
                if (values.get(i) instanceof Long) {
                    preparedStatement.setLong(i + 1, (Long) values.get(i));

                }
                if (values.get(i) instanceof String) {
                    preparedStatement.setNString(i + 1, (String) values.get(i));

                }
                if (values.get(i) instanceof java.util.Date) {
                    preparedStatement.setDate(i + 1, new java.sql.Date(((Date) values.get(i)).getTime()));
                }
                if (values.get(i) instanceof Double) {
                    preparedStatement.setDouble(i + 1, (Double) values.get(i));

                }

            }
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void saveObjectsNotId(String sql, List<Object> values) {
        System.out.println("Prepared statement");
        System.out.println(sql);
        try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i) instanceof Integer) {
                    preparedStatement.setInt(i + 1, (Integer) values.get(i));
                }
                if (values.get(i) instanceof Long) {
                    preparedStatement.setLong(i + 1, (Long) values.get(i));

                }
                if (values.get(i) instanceof String) {
                    preparedStatement.setNString(i + 1, (String) values.get(i));

                }
                if (values.get(i) instanceof java.util.Date) {
                    preparedStatement.setDate(i + 1, new java.sql.Date(((Date) values.get(i)).getTime()));
                }
                if (values.get(i) instanceof Double) {
                    preparedStatement.setDouble(i + 1, (Double) values.get(i));

                }

            }
            preparedStatement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }



    public void deleteObjectFromObjReference(String sql , Object id){
        try(PreparedStatement preparedStatement = con.prepareStatement(sql)) {
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


    public List<Long> getParentsIds(Long parentId) {
        List<Long> ids = new ArrayList<>();
        try (PreparedStatement preparedStatement = con.prepareStatement(queries.getChilds())) {
            preparedStatement.setLong(1, parentId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                ids.add(rs.getLong("OBJECT_ID"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            return ids;
        }
    }
}
