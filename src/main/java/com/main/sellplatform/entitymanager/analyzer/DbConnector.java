package com.main.sellplatform.entitymanager.analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;

@Component
public class DbConnector {
    /*private final String db_name = "XE";
    private final String url = "jdbc:oracle:thin:@localhost:1521/" + db_name;
    private Connection con;
    private final String user = "system", password = "p1234";*/

    private final Connection con;

    @Autowired
    public DbConnector(Connection con) {
        this.con = con;
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
}
