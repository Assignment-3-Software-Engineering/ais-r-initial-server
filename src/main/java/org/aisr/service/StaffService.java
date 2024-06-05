package org.aisr.service;

import org.aisr.DbConnectionManager;
import org.aisr.model.Staff;
import org.aisr.model.User;
import org.aisr.model.constants.Branch;
import org.aisr.model.constants.PositionType;
import org.aisr.model.constants.Role;
import org.aisr.model.constants.UserStatus;
import org.aisr.model.dto.StaffDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffService {
    private Connection dbConnection;
    private final UserService userService = new UserService();

    public StaffService() {
        this.dbConnection = DbConnectionManager.getInstance().getConnection();
    }

    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff";

        try (Statement stmt = dbConnection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {

            while (resultSet.next()) {
                staffList.add(new Staff(resultSet));
            }
        }

        return staffList;
    }

    public Staff createStaff(StaffDto staffDto) throws SQLException {
        System.out.println(staffDto.toString());
        // First, create the user
        int user_id = this.userService.createUser(staffDto.getRole(), UserStatus.ACTIVE,staffDto.getUsername(),staffDto.getPassword(),null);


        // Then, create the staff member linked to the user
        String sql = "INSERT INTO staff (role, full_name, address, phone_number, email, username, staff_id, branch, user_id, position_type, management_level) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = dbConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, staffDto.getRole().toString());
            statement.setString(2, staffDto.getFullName());
            statement.setString(3, staffDto.getAddress());
            statement.setString(4, staffDto.getPhoneNumber());
            statement.setString(5, staffDto.getEmail());
            statement.setString(6, staffDto.getUsername());
            statement.setString(7, staffDto.getStaffId());
            statement.setString(8, staffDto.getBranch().toString());
            statement.setInt(9, user_id);
            if (staffDto.getRole().equals(Role.ADMIN_STAFF)){
                statement.setString(10, staffDto.getPositionType().toString());
                statement.setString(11, null);
            }
            if (staffDto.getRole().equals(Role.MANAGEMENT_STAFF)){
                statement.setString(10, null);
                statement.setString(11, staffDto.getManagementLevel().toString());
            }


            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating staff failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    Staff staff = new Staff();
                    staff.setId(id);
                    staff.setFullName(staffDto.getFullName());
                    staff.setAddress(staffDto.getAddress());
                    staff.setPhoneNumber(staffDto.getPhoneNumber());
                    staff.setEmail(staffDto.getEmail());
                    staff.setUsername(staffDto.getUsername());
                    staff.setStaffId(staffDto.getStaffId());
                    staff.setBranch(staffDto.getBranch());
                    return staff;
                } else {
                    throw new SQLException("Creating staff failed, no ID obtained.");
                }
            }
        }
    }



    public void createAdminStaff(Staff adminStaff)   {
        String sql = "INSERT INTO staff (role, full_name, address, phone_number, email, username,  staff_id, branch, position_type, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try  {
            int user_id = this.userService.createUser(Role.ADMIN_STAFF, UserStatus.ACTIVE,adminStaff.getUsername(),adminStaff.getPassword(),null);

            PreparedStatement pstmt = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, adminStaff.getRole().name());
            pstmt.setString(2, adminStaff.getFullName());
            pstmt.setString(3, adminStaff.getAddress());
            pstmt.setString(4, adminStaff.getPhoneNumber());
            pstmt.setString(5, adminStaff.getEmail());
            pstmt.setString(6, adminStaff.getUsername());
            pstmt.setString(7, adminStaff.getStaffId());
            pstmt.setString(8, adminStaff.getBranch().name());
            pstmt.setString(9, adminStaff.getPositionType().name());
            pstmt.setInt(10,user_id);

            pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void createManagementStaff(Staff managementStaff) {
        String sql = "INSERT INTO staff (role, full_name, address, phone_number, email, username, staff_id, branch, management_level, user_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            int user_id = this.userService.createUser(Role.MANAGEMENT_STAFF, UserStatus.ACTIVE,managementStaff.getUsername(),managementStaff.getPassword(),null);

            PreparedStatement pstmt = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, managementStaff.getRole().name());
            pstmt.setString(2, managementStaff.getFullName());
            pstmt.setString(3, managementStaff.getAddress());
            pstmt.setString(4, managementStaff.getPhoneNumber());
            pstmt.setString(5, managementStaff.getEmail());
            pstmt.setString(6, managementStaff.getUsername());
            pstmt.setString(7, managementStaff.getStaffId());
            pstmt.setString(8, managementStaff.getBranch().name());
            pstmt.setString(9, managementStaff.getManagementLevel().name());
            pstmt.setInt(10,user_id);

            pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Staff findStaffById(int id) throws SQLException {
        String sql = "SELECT * FROM staff WHERE id = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                   return new Staff(resultSet);
                }
            }
        }

        return null;
    }

    public StaffDto updateStaff(StaffDto staff) throws SQLException {
        String sql = "UPDATE staff SET full_name = ?, address = ?, phone_number = ?, email = ? WHERE id = ?";
        try  {
            PreparedStatement pstmt = dbConnection.prepareStatement(sql);
            pstmt.setString(1, staff.getFullName());
            pstmt.setString(2, staff.getAddress());
            pstmt.setString(3, staff.getPhoneNumber());
            pstmt.setString(4, staff.getEmail());
            pstmt.setInt(5, staff.getId());
            pstmt.executeUpdate();
            return staff;
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Optional<Staff> findUserByUsername(String username) {
        String sql = "SELECT * FROM staff WHERE username = ?";
        try  {
            PreparedStatement statement = this.dbConnection.prepareStatement(sql);
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Staff(resultSet));
                } else {
                    return Optional.empty(); // User not found
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteStaff(int id) throws SQLException {
        String sql = "DELETE FROM staff WHERE id = ?";

        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }



    public Optional<Staff> findByUser(int id) {
        String sql = "SELECT * FROM staff WHERE user_id = ?";
        try  {
            PreparedStatement statement = this.dbConnection.prepareStatement(sql);
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Staff(resultSet));
                } else {
                    return Optional.empty(); // User not found
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
