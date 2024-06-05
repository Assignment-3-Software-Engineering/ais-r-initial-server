package org.aisr.model;

import org.aisr.model.constants.Branch;
import org.aisr.model.constants.ManagementLevel;
import org.aisr.model.constants.PositionType;
import org.aisr.model.constants.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Staff {
    private int id;
    private Role role;
    private String fullName;
    private String address;
    private String phoneNumber; // Must contain 10 digits
    private String email;
    private String username;
    private String password;
    private String staffId;
    private Branch branch;
    private User user;
    private ManagementLevel managementLevel;
    private PositionType positionType;

    public Staff(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.role = Role.valueOf(resultSet.getString("role"));
        this.fullName = resultSet.getString("full_name");
        this.address = resultSet.getString("address");
        this.phoneNumber = resultSet.getString("phone_number");
        this.email = resultSet.getString("email");
        this.username = resultSet.getString("username");
        this.staffId = resultSet.getString("staff_id");
        this.branch = Branch.valueOf(resultSet.getString("branch"));
        if (this.role.equals(Role.ADMIN_STAFF)){
            this.positionType = PositionType.valueOf(resultSet.getString("position_type"));
            this.managementLevel = null;
        }
        if (this.role.equals(Role.MANAGEMENT_STAFF)){
            this.managementLevel = ManagementLevel.valueOf(resultSet.getString("management_level"));
            this.positionType = null;
        }
        //  this.user =  resultSet.getString("user_id");
    }

    public Staff(String address, Branch branch, String email, String fullName, int id, ManagementLevel managementLevel, String password, String phoneNumber, PositionType positionType, Role role, String staffId, User user, String username) {
        this.address = address;
        this.branch = branch;
        this.email = email;
        this.fullName = fullName;
        this.id = id;
        this.managementLevel = managementLevel;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.positionType = positionType;
        this.role = role;
        this.staffId = staffId;
        this.user = user;
        this.username = username;
    }

    // AdminStaff
    public Staff(String address, Branch branch, String email, String fullName, int id , String password, String phoneNumber, Role role, String staffId, User user, String username, PositionType positionType) {
        this.address = address;
        this.branch = branch;
        this.email = email;
        this.fullName = fullName;
        this.id = id;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.positionType = positionType;
        this.role = role;
        this.staffId = staffId;
        this.user = user;
        this.username = username;
    }

    // ManagementStaff
    public Staff(String address, Branch branch, String email, String fullName, int id , String password, String phoneNumber, Role role, String staffId, User user, String username, ManagementLevel managementLevel) {
        this.address = address;
        this.branch = branch;
        this.email = email;
        this.fullName = fullName;
        this.id = id;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.managementLevel = managementLevel;
        this.role = role;
        this.staffId = staffId;
        this.user = user;
        this.username = username;
    }

    public Staff() {}
    public Staff(int id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return getId() == staff.getId() && getRole() == staff.getRole() && Objects.equals(getFullName(), staff.getFullName()) && Objects.equals(getAddress(), staff.getAddress()) && Objects.equals(getPhoneNumber(), staff.getPhoneNumber()) && Objects.equals(getEmail(), staff.getEmail()) && Objects.equals(getUsername(), staff.getUsername()) && Objects.equals(getPassword(), staff.getPassword()) && Objects.equals(getStaffId(), staff.getStaffId()) && getBranch() == staff.getBranch() && Objects.equals(getUser(), staff.getUser()) && getManagementLevel() == staff.getManagementLevel() && getPositionType() == staff.getPositionType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRole(), getFullName(), getAddress(), getPhoneNumber(), getEmail(), getUsername(), getPassword(), getStaffId(), getBranch(), getUser(), getManagementLevel(), getPositionType());
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ManagementLevel getManagementLevel() {
        return managementLevel;
    }

    public void setManagementLevel(ManagementLevel managementLevel) {
        this.managementLevel = managementLevel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PositionType getPositionType() {
        return positionType;
    }

    public void setPositionType(PositionType positionType) {
        this.positionType = positionType;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
