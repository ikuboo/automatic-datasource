package com.jd.auction.common.automatic.datasouce;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class AutoMaticDataSourceTest {

    @Resource(name = "autoMaticDataSource")
    private AutoMaticDataSource autoMaticDataSource;

    @Resource(name = "dataSource_m")
    private DataSource m_datasource;

    @Test
    public void getConnection() throws Exception {

        while (true) {
            AutoMaticDataSource.userMaster();
            Connection connection = autoMaticDataSource.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from t_user where id = 1");

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                System.out.println(String.format("id={%d},name={%s}", id, name));
            }

            resultSet.close();
            statement.close();
            connection.close();

            Thread.sleep(1000);
        }
    }

    @Test
    public void tranactionTest() throws Exception {
        Connection connection = autoMaticDataSource.getConnection();
        connection.setAutoCommit(false);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from t_user where id = 1");
        while (resultSet.next()) {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            System.out.println(String.format("id={%d},name={%s}", id, name));
        }
        resultSet.close();
        statement.close();

        statement = connection.createStatement();
        boolean execute = statement.execute("update t_user set name='test_v3' where id = 15");
        System.out.println("update:" + execute);
        statement.close();

        statement = connection.createStatement();
        execute = statement.execute("update t_user set name='test_v3' where id = 16");
        System.out.println("update:" + execute);
        statement.close();


        statement = connection.createStatement();
        resultSet = statement.executeQuery("select * from t_user where id = 1");
        while (resultSet.next()) {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            System.out.println(String.format("id={%d},name={%s}", id, name));
        }
        resultSet.close();
        statement.close();
        connection.commit();
        connection.close();

    }

    @Test
    public void rollbackTest() throws Exception {

        Connection connection = autoMaticDataSource.getConnection();
        connection.setAutoCommit(false);

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from t_user where id = 1");
        while (resultSet.next()) {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            System.out.println(String.format("id={%d},name={%s}", id, name));
        }
        resultSet.close();
        statement.close();

        statement = connection.createStatement();
        boolean execute = statement.execute("update t_user set name='test_v4' where id = 15");
        System.out.println("update:" + execute);
        statement.close();

        statement = connection.createStatement();
        execute = statement.execute("update t_user set name='test_v4' where id = 16");
        System.out.println("update:" + execute);
        statement.close();


        statement = connection.createStatement();
        resultSet = statement.executeQuery("select * from t_user where id = 1");
        while (resultSet.next()) {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            System.out.println(String.format("id={%d},name={%s}", id, name));
        }
        resultSet.close();
        statement.close();
        connection.rollback();
        connection.close();

    }
}

