package org.aisr.service;

import org.aisr.DbConnectionManager;
import org.aisr.model.Recruit;
import org.aisr.model.Staff;
import org.aisr.model.User;
import org.aisr.model.constants.Role;
import org.aisr.model.constants.UserStatus;
import org.aisr.model.dto.RecruitDto;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RecruitService {
    private Connection dbConnection;
    private final UserService userService = new UserService();
    private final StaffService staffService = new StaffService();

    public RecruitService() {
        this.dbConnection = DbConnectionManager.getInstance().getConnection();
    }

    public Recruit createRecruit(RecruitDto dto) {
        String sql = "INSERT INTO recruits (id, full_name, address, phone_number, email, username, highest_qualification_level, created_by, created_at, updated_at, user_id) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try  {
            int user_id = this.userService.createUser(Role.RECRUIT, UserStatus.ACTIVE,dto.getUsername(),null, UUID.randomUUID().toString());

            PreparedStatement pstmt = dbConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, dto.getId());
            pstmt.setString(2, dto.getFullName());
            pstmt.setString(3, dto.getAddress());
            pstmt.setString(4, dto.getPhoneNumber());
            pstmt.setString(5, dto.getEmail());
            pstmt.setString(6, dto.getUsername());
            pstmt.setString(7, dto.getHighestQualificationLevel().name());
            pstmt.setInt(8, dto.getCreatedBy().getId());
            pstmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(11,user_id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating dto failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    Recruit recruit = new Recruit();
                    recruit.setId(id);
                    recruit.setFullName(dto.getFullName());
                    recruit.setAddress(dto.getAddress());
                    recruit.setPhoneNumber(dto.getPhoneNumber());
                    recruit.setEmail(dto.getEmail());
                    recruit.setUsername(dto.getUsername());
                    recruit.setHighestQualificationLevel(dto.getHighestQualificationLevel());
                    recruit.setCreatedBy(new Staff( dto.getCreatedBy().getId()));
                    // todo
                  //  recruit.setBranch(dto.getBranch());
                    return recruit;
                } else {
                    throw new SQLException("Creating staff failed, no ID obtained.");
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Recruit updateRecruit(Recruit recruit) {
        String sql = "UPDATE recruits SET full_name = ?, address = ?, phone_number = ?, email = ?, interview_date = ?, department = ?, updated_at = ? WHERE id = ?";

        try  {
            PreparedStatement pstmt = dbConnection.prepareStatement(sql);
            pstmt.setString(1, recruit.getFullName());
            pstmt.setString(2, recruit.getAddress());
            pstmt.setString(3, recruit.getPhoneNumber());
            pstmt.setString(4, recruit.getEmail());
            pstmt.setDate(5, recruit.getInterviewDate() != null ? Date.valueOf(recruit.getInterviewDate()) : null);
            pstmt.setString(6,recruit.getDepartment() != null ?  recruit.getDepartment().name() : null);
            pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(8, recruit.getId());

            pstmt.executeUpdate();
            return recruit;
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Recruit findRecruitById(String id) throws SQLException {
        String sql = "SELECT * FROM recruits WHERE id = ?";
        Recruit recruit = null;

        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setString(1, id);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    recruit = new Recruit(resultSet);
                }
            }
        }

        return recruit;
    }

    public List<Recruit> getAllRecruits() throws SQLException {
        List<Recruit> recruitList = new ArrayList<>();
        String sql = "SELECT * FROM recruits";

        try (Statement stmt = dbConnection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {

            while (resultSet.next()) {
                Recruit recruit = new Recruit(resultSet);
                recruit.setCreatedBy(this.staffService.findStaffById(recruit.getCreatedBy().getId()));
                recruitList.add(recruit);
            }
        }

        return recruitList;
    }

    public Optional<Recruit> findByUser(int id) {
        String sql = "SELECT * FROM recruits WHERE user_id = ?";
        try  {
            PreparedStatement statement = this.dbConnection.prepareStatement(sql);
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Recruit(resultSet));
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
