package com.main.sellplatform.entitymanager.analyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
            while(rs.next()){

            }
            System.out.println(sql);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
