package com.jd.auction.common.automatic.datasouce;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class AutoMaticDataSourceTest {
    private static final Logger logger = LoggerFactory.getLogger(AutoMaticDataSource.class);

    @Resource(name = "autoMaticDataSource")
    private AutoMaticDataSource autoMaticDataSource;

    @Resource(name = "dataSource_m")
    private DataSource m_datasource;

    @Test
    public void getConnection() {

        for (int i = 0; i < 1000; i++) {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try{
                connection = autoMaticDataSource.getConnection();
                preparedStatement = connection.prepareStatement("select * from t_user where id = 1");
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    logger.info(String.format("------------------id=[%s],name=[%s]", id, name));
                }
            }catch (Exception e){
                logger.info(String.format("-----------------------访问异常"));
            }finally {
                try{
                    if(resultSet != null)
                        resultSet.close();
                    if(preparedStatement != null)
                        preparedStatement.close();
                    if(connection != null){
                        connection.close();
                    }
                }catch (Exception e){
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

