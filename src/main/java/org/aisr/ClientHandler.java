package org.aisr;

import com.google.gson.GsonBuilder;
import org.aisr.model.Recruit;
import org.aisr.model.Staff;
import org.aisr.model.User;
import org.aisr.model.constants.Role;
import org.aisr.model.dto.LoginDto;
import com.google.gson.Gson;
import org.aisr.model.dto.RecruitDto;
import org.aisr.model.dto.StaffDto;
import org.aisr.service.RecruitService;
import org.aisr.service.StaffService;
import org.aisr.service.UserService;
import org.aisr.util.LocalDateAdapter;
import org.aisr.util.LocalDateTimeAdapter;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class ClientHandler extends Thread {
    private Socket clientSocket;
    private Connection dbConnection;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private final UserService userService = new UserService();
    private final StaffService staffService = new StaffService();
    private final RecruitService recruitService = new RecruitService();

    // server response list
    static final String SERVER_ERROR = "INTERNAL_SERVER_ERROR";
    static final String OK = "OK";
    static final String CREATED = "CREATED";
    static final String BAD_REQUEST = "CREATED";
    static final String UNAUTHORIZED = "UNAUTHORIZED";
    static final String FORBIDDEN = "FORBIDDEN";
    static final String NOT_FOUND = "NOT_FOUND";

    // server request list
    static final String TEST_CONNECTION = "TEST_CONNECTION";
    static final String CLOSE_CONNECTION = "CLOSE_CONNECTION";
    static final String AUTHENTICATE = "AUTHENTICATE";
    static final String CREATE_RECRUIT = "CREATE_RECRUIT";
    static final String CREATE_STAFF = "CREATE_STAFF";
    static final String UPDATE_RECRUIT = "UPDATE_RECRUIT";
    static final String UPDATE_STAFF = "UPDATE_STAFF";
    static final String UPDATE_USER = "UPDATE_USER";
    static final String GET_ALL_STAFF = "GET_ALL_STAFF";
    static final String GET_ALL_RECRUIT = "GET_ALL_RECRUIT";

    public ClientHandler(Socket socket, Connection connection) {
        try {
            this.clientSocket = socket;
            this.dbConnection = connection;
            in = new ObjectInputStream( clientSocket.getInputStream());
            out = new ObjectOutputStream( clientSocket.getOutputStream());
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            Object request = in.readObject();
            System.out.println("server event recived. request: "+request);
            if (TEST_CONNECTION.equals(request)){
                out.writeObject(OK);
                out.flush();
                System.out.println("server connection establish with client success");
                return;
            } else if (AUTHENTICATE.equals(request)){
                try {
                    Object dto = in.readObject();
                    LoginDto loginDto = gson.fromJson(dto.toString(),LoginDto.class);
                    Optional<User> userOptional;
                    if (loginDto.getToken() != null && !loginDto.getToken().isEmpty()){
                        userOptional = this.userService.findUserByToken(loginDto.getToken());
                        if (userOptional.isPresent()){
                            User user = userOptional.get();
                            out.writeObject(OK);
                            out.writeObject(gson.toJson(user));
                            if (user.getRole().equals(Role.ADMIN_STAFF) || user.getRole().equals(Role.MANAGEMENT_STAFF)){
                                out.writeObject(gson.toJson(this.staffService.findByUser(user.getId()).get()));
                            } else  if (user.getRole().equals(Role.RECRUIT)){
                                out.writeObject(gson.toJson(this.recruitService.findByUser(user.getId()).get()));
                            }
                        }else {
                            out.writeObject(UNAUTHORIZED);
                        }
                        out.flush();
                    }else {
                        userOptional = this.userService.findUserByUsername(loginDto.getUsername());
                        if (userOptional.isPresent()){
                            User user = userOptional.get();
                            boolean matches = BCrypt.checkpw(loginDto.getPassword(), user.getPassword());
                            if (matches){
                                out.writeObject(OK);
                                out.writeObject(gson.toJson(user));
                                if (user.getRole().equals(Role.ADMIN_STAFF) || user.getRole().equals(Role.MANAGEMENT_STAFF)){
                                    out.writeObject(gson.toJson(this.staffService.findByUser(user.getId()).get()));
                                } else  if (user.getRole().equals(Role.RECRUIT)){
                                    out.writeObject(gson.toJson(this.recruitService.findByUser(user.getId()).get()));
                                }
                            }else {
                                out.writeObject(UNAUTHORIZED);
                            }
                        }else {
                            out.writeObject(UNAUTHORIZED);
                        }
                        out.flush();
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    out.writeObject(SERVER_ERROR);
                    out.writeObject(e.getMessage());
                    out.flush();
                }
                return;
            } else if (GET_ALL_STAFF.equals(request)){
                try {
                    List<StaffDto> staffList = StaffDto.initList(this.staffService.getAllStaff());
                    out.writeObject(OK);
                    out.writeObject(gson.toJson(staffList));
                    out.flush();
                }catch (SQLException e){
                    e.printStackTrace();
                    out.writeObject(SERVER_ERROR);
                    out.writeObject(e.getMessage());
                    out.flush();
                }

            } else if (GET_ALL_RECRUIT.equals(request)){
                try {
                    List<RecruitDto> recruits = RecruitDto.initList(this.recruitService.getAllRecruits());
                    out.writeObject(OK);
                    out.writeObject(gson.toJson(recruits));
                    out.flush();
                }catch (Exception e){
                    e.printStackTrace();
                    out.writeObject(SERVER_ERROR);
                    out.writeObject(e.getMessage());
                    out.flush();
                }
            } else if (CREATE_STAFF.equals(request)){
                try {
                    Object requestBody = in.readObject();
                    StaffDto staffDto = gson.fromJson(requestBody.toString(),StaffDto.class);
                    Staff staff = this.staffService.createStaff(staffDto);
                    out.writeObject(OK);
                    out.writeObject(gson.toJson(StaffDto.init(staff)));
                    out.flush();
                }catch (Exception e){
                    e.printStackTrace();
                    out.writeObject(SERVER_ERROR);
                    out.writeObject(e.getMessage());
                    out.flush();
                }
            } else if (CREATE_RECRUIT.equals(request)){
                try {
                    Object requestBody = in.readObject();
                    RecruitDto dto = gson.fromJson(requestBody.toString(),RecruitDto.class);
                    Recruit recruit = this.recruitService.createRecruit(dto);
                    out.writeObject(OK);
                    out.writeObject(gson.toJson(RecruitDto.init(recruit)));
                    out.flush();
                }catch (Exception e){
                    e.printStackTrace();
                    out.writeObject(SERVER_ERROR);
                    out.writeObject(e.getMessage());
                    out.flush();
                }
            } else if (UPDATE_STAFF.equals(request)){
                try {
                    Object requestBody = in.readObject();
                    StaffDto dto = gson.fromJson(requestBody.toString(),StaffDto.class);
                    dto = this.staffService.updateStaff(dto);
                    out.writeObject(OK);
                    out.writeObject(gson.toJson(dto));
                    out.flush();
                }catch (Exception e){
                    e.printStackTrace();
                    out.writeObject(SERVER_ERROR);
                    out.writeObject(e.getMessage());
                    out.flush();
                }
            } else if (UPDATE_RECRUIT.equals(request)){
                try {
                    Object requestBody = in.readObject();
                    Recruit recruit = gson.fromJson(requestBody.toString(),Recruit.class);
                    recruit = this.recruitService.updateRecruit(recruit);
                    out.writeObject(OK);
                    out.writeObject(gson.toJson(RecruitDto.init(recruit)));
                    out.flush();
                }catch (Exception e){
                    e.printStackTrace();
                    out.writeObject(SERVER_ERROR);
                    out.writeObject(e.getMessage());
                    out.flush();
                }
            } else if (UPDATE_USER.equals(request)){
                try {
                    Object userid = in.readObject();
                    Object requestBody = in.readObject();
                    Optional<User> userOptional = this.userService.findUserById(Integer.parseInt(userid.toString()));
                    if (userOptional.isPresent()){
                        User user = userOptional.get();
                        this.userService.updatePassword(user.getId(),requestBody.toString());
                        out.writeObject(OK);
                        out.writeObject(gson.toJson(this.userService.findUserById(Integer.parseInt(userid.toString())).get()));
                        out.flush();
                    }else {
                        out.writeObject(NOT_FOUND);
                        out.writeObject("can not find user with id: "+userid);
                        out.flush();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    out.writeObject(SERVER_ERROR);
                    out.writeObject(e.getMessage());
                    out.flush();
                }
            } else {
                out.writeObject(NOT_FOUND);
                out.flush();
                return;
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}


