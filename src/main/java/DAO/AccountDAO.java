package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    // Existing methods for accountExists, createAccount, and getAccountByUsername...

    public static boolean createAccount(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account(username, password) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                
                int affectedRows = preparedStatement.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            // Log or handle the exception according to your application's requirements
            e.printStackTrace();
        } finally {
            ConnectionUtil.closeConnection(connection);
        }
        return false;
    }

    public boolean validateLogin(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM account WHERE username=? AND password=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionUtil.closeConnection(connection);
        }
        return false;
    }

    public static boolean isUsernameAvailable(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM account WHERE username=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count == 0; // If count is 0, the username is available
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionUtil.closeConnection(connection);
        }
        return false;
    }

    public  Account getAccountByUsername(String username) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account WHERE username=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        int accountId = rs.getInt("account_id");
                        String password = rs.getString("password");
                        return new Account(accountId, username, password);
                    }
                }
            }
        } catch (SQLException e) {
            // Log or handle the exception according to your application's requirements
            e.printStackTrace();
        }
        return null;
    }
    public static boolean accountExists(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM account WHERE account_id=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, accountId);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionUtil.closeConnection(connection);
        }
        return false;
    }
    
    
    
    
    
    
}


