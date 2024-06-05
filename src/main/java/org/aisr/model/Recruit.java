package org.aisr.model;

import org.aisr.model.constants.Department;
import org.aisr.model.constants.Qualification;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Recruit {
    private int id;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private LocalDate interviewDate;
    private Qualification highestQualificationLevel;
    private Department department;
    private Staff createdBy;
    private User user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Recruit() {
    }

    public Recruit(String address, LocalDateTime createdAt, Staff createdBy, Department department, String email, String fullName, Qualification highestQualificationLevel, int id, LocalDate interviewDate, String phoneNumber, LocalDateTime updatedAt, User user, String username) {
        this.address = address;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.department = department;
        this.email = email;
        this.fullName = fullName;
        this.highestQualificationLevel = highestQualificationLevel;
        this.id = id;
        this.interviewDate = interviewDate;
        this.phoneNumber = phoneNumber;
        this.updatedAt = updatedAt;
        this.user = user;
        this.username = username;
    }

    public Recruit(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.fullName = resultSet.getString("full_name");
        this.address = resultSet.getString("address");
        this.phoneNumber = resultSet.getString("phone_number");
        this.email = resultSet.getString("email");
        this.username = resultSet.getString("username");
        if (resultSet.getDate("interview_date") != null){
            this.interviewDate = resultSet.getDate("interview_date").toLocalDate();
        }
        if (resultSet.getString("department") != null){
            this.department = Department.valueOf(resultSet.getString("department"));
        }
        this.highestQualificationLevel = Qualification.valueOf(resultSet.getString("highest_qualification_level"));
        this.createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        this.updatedAt = resultSet.getTimestamp("updated_at").toLocalDateTime();
        this.createdBy = new Staff(resultSet.getInt("created_by"));
    }

    @Override
    public String toString() {
        return "Recruit{" +
                "address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", interviewDate=" + interviewDate +
                ", highestQualificationLevel=" + highestQualificationLevel +
                ", department=" + department +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recruit recruit = (Recruit) o;
        return getId() == recruit.getId() && Objects.equals(getFullName(), recruit.getFullName()) && Objects.equals(getAddress(), recruit.getAddress()) && Objects.equals(getPhoneNumber(), recruit.getPhoneNumber()) && Objects.equals(getEmail(), recruit.getEmail()) && Objects.equals(getUsername(), recruit.getUsername()) && Objects.equals(getInterviewDate(), recruit.getInterviewDate()) && getHighestQualificationLevel() == recruit.getHighestQualificationLevel() && getDepartment() == recruit.getDepartment() && Objects.equals(getCreatedBy(), recruit.getCreatedBy()) && Objects.equals(user, recruit.user) && Objects.equals(getCreatedAt(), recruit.getCreatedAt()) && Objects.equals(getUpdatedAt(), recruit.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFullName(), getAddress(), getPhoneNumber(), getEmail(), getUsername(), getInterviewDate(), getHighestQualificationLevel(), getDepartment(), getCreatedBy(), user, getCreatedAt(), getUpdatedAt());
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Staff getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Staff createdBy) {
        this.createdBy = createdBy;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Qualification getHighestQualificationLevel() {
        return highestQualificationLevel;
    }

    public void setHighestQualificationLevel(Qualification highestQualificationLevel) {
        this.highestQualificationLevel = highestQualificationLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDate interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
