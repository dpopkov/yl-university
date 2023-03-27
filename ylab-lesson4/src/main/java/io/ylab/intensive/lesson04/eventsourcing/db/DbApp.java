package io.ylab.intensive.lesson04.eventsourcing.db;

import com.rabbitmq.client.ConnectionFactory;
import io.ylab.intensive.lesson04.DbUtil;
import io.ylab.intensive.lesson04.RabbitMQUtil;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DbApp {

    public static void main(String[] args) throws Exception {
        DataSource dataSource = initDb();
        ConnectionFactory connectionFactory = initMQ();

        ModifyingPersonDao personDao = new ModifyingPersonDao(dataSource);
        MessageProcessor processor = new MessageProcessor(connectionFactory, personDao);
        System.out.println("Waiting for messages. To exit press Ctrl+C");
        processor.start();
    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }

    private static DataSource initDb() throws SQLException {
        /*
            Из-за ошибок в PostgreSQL строка
            "create if not exists table person"
            в исходном файле задания была заменена на
            "create table if not exists person"
         */
        String ddl = ""
                + "drop table if exists person;"
                + "create table if not exists person (\n"
                + "person_id bigint primary key,\n"
                + "first_name varchar,\n"
                + "last_name varchar,\n"
                + "middle_name varchar\n"
                + ")";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(ddl, dataSource);
        return dataSource;
    }
}
