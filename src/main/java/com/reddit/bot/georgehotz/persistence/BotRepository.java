package com.reddit.bot.georgehotz.persistence;

import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import static com.reddit.bot.georgehotz.constant.ApplicationConstants.*;

/**
 * This class contains the JDBC Database Operation Logic
 */

public class BotRepository {

    private static BotRepository botRepositoryInstance = null;
    private static final Object threadLock = new Object();
    Connection connection;

    /**
     * Create object and opens a connection to the database
     */
    public BotRepository() {
        openConnection();
    }

    public void openConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);

            if (connection == null) {
                System.err.println("DBConnect: getConnection: connection null ");
            }

        } catch (Exception e) {
            System.err.println("DBConnect: getConnection: exception: " + e.getMessage());
        }
    }

    /**
     * Return an instance of BotRepository class
     * @return botRepository instance
     */
    public static BotRepository getInstance() {
        synchronized (threadLock) {
            return botRepositoryInstance == null ? botRepositoryInstance = new BotRepository() : botRepositoryInstance;
        }
    }

    /**
     * Closes a database connection
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            System.err.println("DBConnect::closeConnection: Exception: " + e.getMessage());
        }
    }

    /**
     * Cleans up system resources after database connection closes
     * @throws Throwable
     */
    protected void finalize() throws Throwable {
        closeConnection();
        super.finalize();
    }

    /**
     * Generic database method for retrieving sensitive bot information
     *
     * @param key information reference
     * @return requested sensitive value
     */
    public String retrieveBotInformation(String key) {
        if(StringUtils.isEmpty(key) || "null".equals(key)) {
            return "Cannot retrieve DB Value with an empty or null key";
        }
        String value = null;
        String query = "select config_value from bot_config where config_key=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                value = rs.getString(1);
            }

        } catch (Exception exception){
            System.out.println(exception);
        }

        return value;
    }

    /**
     * Checks if specified comment id exists in comment_tracker db table
     *
     * @param commentId unique id of reddit comment
     * @return id if true
     */
    public Optional<String> checkIfCommentIdExist(String commentId) {
        String value = null;
        String query = "select tracker_value from comment_tracker where tracker_value=?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, commentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                value = rs.getString(1);
            }

        } catch (Exception exception){
            System.out.println(exception);
        }

        if (value == null) {
            return Optional.empty();
        }

        return Optional.of(value);
    }

    /**
     * Inserts unique comment id into comment_tracker db table
     *
     * @param commentId unique id of reddit comment
     */
    public void saveCommentId(String commentId) {
        String query = "insert into comment_tracker(tracker_value)" + " values (?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, commentId);
            stmt.execute();
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

}
