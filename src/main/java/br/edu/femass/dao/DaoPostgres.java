package br.edu.femass.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public abstract class DaoPostgres {
    protected String ADDRESS = "localhost";
    protected String DB = "FilmeVelado";
    protected String PORT = "5432";
    protected String USER = "postgres";
    protected String PASSWORD = "postgres";

    private static final Logger logger = LoggerFactory.getLogger(DaoPostgres.class);
    private static Connection conn = null;
    protected Connection getConnection() throws SQLException{
        if(conn != null) return conn;

        String url = "jdbc:postgresql://" + ADDRESS + ":" + PORT + "/" + DB;
        conn = DriverManager.getConnection(url, USER, PASSWORD);
        logger.debug("Conexão estabelecida em " + url);

        return conn;
    }

    protected PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return getConnection().prepareStatement(sql);
    }

    protected PreparedStatement getPreparedStatement(String sql, boolean insertion) throws SQLException {
        if(insertion) return getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        else return getPreparedStatement(sql);
    }
}
