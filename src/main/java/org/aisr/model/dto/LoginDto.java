package org.aisr.model.dto;

import java.io.Serializable;
import java.util.Objects;

public class LoginDto implements Serializable {
    private static final long serialVersionUID = 1L;


    private String username;
    private String password;
    private String token;

    public LoginDto(String password, String token, String username) {
        this.password = password;
        this.token = token;
        this.username = username;
    }

    public LoginDto(String password, String username) {
        this.password = password;
        this.username = username;
    }

    @Override
    public String toString() {
        return "LoginDto{" +
                "password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginDto loginDto = (LoginDto) o;
        return Objects.equals(getUsername(), loginDto.getUsername()) && Objects.equals(getPassword(), loginDto.getPassword()) && Objects.equals(getToken(), loginDto.getToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getToken());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
