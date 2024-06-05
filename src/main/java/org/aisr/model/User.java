package org.aisr.model;

import org.aisr.model.constants.Role;
import org.aisr.model.constants.UserStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class User {
    private int id;
    private int referenceId;
    private UserStatus status;
    private Role role;
    private String username;
    private String password;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(ResultSet resultSet) throws SQLException {
        System.out.println(resultSet.getString("role"));
        this.id = resultSet.getInt("id");
        this.referenceId = resultSet.getInt("reference_id");
        this.role = Role.of(resultSet.getString("role"));
        this.status = UserStatus.of(resultSet.getString("status"));
        this.username = resultSet.getString("username");
        this.password =  resultSet.getString("password");
        this.token = resultSet.getString("token");
        this.createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        this.updatedAt = resultSet.getTimestamp("updated_at").toLocalDateTime();
    }

    public User(LocalDateTime createdAt, int id, String password, int referenceId, Role role, UserStatus status, String token, LocalDateTime updatedAt, String username) {
        this.createdAt = createdAt;
        this.id = id;
        this.password = password;
        this.referenceId = referenceId;
        this.role = role;
        this.status = status;
        this.token = token;
        this.updatedAt = updatedAt;
        this.username = username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
