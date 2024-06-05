package org.aisr.service;

import org.aisr.DbConnectionManager;
import org.aisr.model.User;
import org.aisr.model.constants.Role;
import org.aisr.model.constants.UserStatus;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Optional;

public class UserService {
    private Connection dbConnection;

    public UserService() {
        this.dbConnection = DbConnectionManager.getInstance().getConnection();
    }

    public int createUser(Role role, UserStatus status, String username, String password, String token) throws SQLException {
        String sql = "INSERT INTO user (role, status, username, password, token, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = dbConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, role.getValue());
            statement.setString(2, status.getValue());
            statement.setString(3, username);
            statement.setString(4,  password != null ? BCrypt.hashpw(password, BCrypt.gensalt()) : null);
            statement.setString(5, token);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            statement.setTimestamp(6, now);
            statement.setTimestamp(7, now);
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        }
    }
    public Optional<User> findUserByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try  {
            PreparedStatement statement = this.dbConnection.prepareStatement(sql);
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new User(resultSet));
                } else {
                    return Optional.empty(); // User not found
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findUserByToken(String token) {
        String sql = "SELECT * FROM user WHERE token = ?";
        try  {
            PreparedStatement statement = this.dbConnection.prepareStatement(sql);
            statement.setString(1, token);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new User(resultSet));
                } else {
                    return Optional.empty(); // User not found
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findUserById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try  {
            PreparedStatement statement = this.dbConnection.prepareStatement(sql);
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new User(resultSet));
                } else {
                    return Optional.empty(); // User not found
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean updatePassword(int userId, String password) {
        String sql = "UPDATE user SET password = ?, token = NULL WHERE id = ?";
        try (PreparedStatement pstmt = this.dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, BCrypt.hashpw(password, BCrypt.gensalt()));
            pstmt.setInt(2, userId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
           throw new RuntimeException(e);
        }
    }


}
