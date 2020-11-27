package com.github.oxal.JMysql.util;

import com.github.oxal.JBDD.JBDD;

import java.sql.*;
import java.util.Optional;

public class JMysqlBDD implements JBDD<ResultSet> {

    private String url, user, mdp, database;
    private Statement statement;
    private Connection connection;

    public JMysqlBDD(String url, String user, String mdp) {
        this.url = url;
        this.user = user;
        this.mdp = mdp;
    }

    public JMysqlBDD(String url, String user, String mdp, String database) {
        this.url = url + database;
        this.user = user;
        this.mdp = mdp;
        this.database = database;
    }

    public JMysqlBDD(String url, String user, String mdp, String database, String option) {
        this.url = url + database + option;
        this.user = user;
        this.mdp = mdp;
        this.database = database;
    }

    @Override
    public Optional<ResultSet> getQuery(String req) {
        Optional<ResultSet> resultSet = Optional.empty();
        try {
            resultSet = Optional.of(connection.createStatement().executeQuery(req));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    @Override
    public boolean updateQuery(String req) {
        try {
            connection.createStatement().executeUpdate(req);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean connexion() {
        try {
            System.out.println("connexion à la bdd ...");
            connection = DriverManager.getConnection(url, user, mdp);
            statement = connection.createStatement();
            System.out.println("connexion réussi");

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean close() {
        try {
            connection.close();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }
}
