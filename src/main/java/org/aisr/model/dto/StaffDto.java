package org.aisr.model.dto;

import org.aisr.model.Staff;
import org.aisr.model.User;
import org.aisr.model.constants.Branch;
import org.aisr.model.constants.ManagementLevel;
import org.aisr.model.constants.PositionType;
import org.aisr.model.constants.Role;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StaffDto  implements Serializable {
    private static final long serialVersionUID = 1L;
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

    public static StaffDto init(Staff staff){
        StaffDto dto = new StaffDto();
        dto.setId(staff.getId());
        dto.setRole(staff.getRole());
        dto.setFullName(staff.getFullName());
        dto.setAddress(staff.getAddress());
        dto.setPhoneNumber(staff.getPhoneNumber());
        dto.setEmail(staff.getEmail());
        dto.setUsername(staff.getUsername());
        dto.setStaffId(staff.getStaffId());
        dto.setBranch(staff.getBranch());
        dto.setManagementLevel(staff.getManagementLevel());
        dto.setPositionType(staff.getPositionType());
        return dto;
    }

    @Override
    public boolean equals(Object o) {


        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaffDto staffDto = (StaffDto) o;
        return getId() == staffDto.getId() && getRole() == staffDto.getRole() && Objects.equals(getFullName(), staffDto.getFullName()) && Objects.equals(getAddress(), staffDto.getAddress()) && Objects.equals(getPhoneNumber(), staffDto.getPhoneNumber()) && Objects.equals(getEmail(), staffDto.getEmail()) && Objects.equals(getUsername(), staffDto.getUsername()) && Objects.equals(getPassword(), staffDto.getPassword()) && Objects.equals(getStaffId(), staffDto.getStaffId()) && getBranch() == staffDto.getBranch() && Objects.equals(getUser(), staffDto.getUser()) && getManagementLevel() == staffDto.getManagementLevel() && getPositionType() == staffDto.getPositionType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRole(), getFullName(), getAddress(), getPhoneNumber(), getEmail(), getUsername(), getPassword(), getStaffId(), getBranch(), getUser(), getManagementLevel(), getPositionType());
    }

    public static List<StaffDto> initList(List<Staff> staffList) {
        return staffList.stream()
                .map(StaffDto ::init)
                .collect(Collectors.toList());
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
