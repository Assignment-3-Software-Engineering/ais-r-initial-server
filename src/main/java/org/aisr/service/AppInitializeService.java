package org.aisr.service;

import org.aisr.model.Staff;
import org.aisr.model.User;
import org.aisr.model.constants.Branch;
import org.aisr.model.constants.PositionType;
import org.aisr.model.constants.Role;

public class AppInitializeService {
    private static final String defaultStaffMemberEmail = "admin@aisrinitialclient.com";
    private static final String defaultStaffMemberPassword = "TestUser1";
    private static final String defaultStaffMemberStaffID = "EMP0001";
    private static final String defaultStaffMemberStaffUsername = "ADMINUSER";
    private static final String defaultStaffMemberFullName = "AIS R Initial - Administrator";


    public AppInitializeService() {}

    public static void createAdmin(){
        StaffService staffService = new StaffService();
        if (staffService.findUserByUsername(defaultStaffMemberStaffUsername).isEmpty()){
            Staff admin = new Staff(
                    null,
                    Branch.ADELAIDE,
                    defaultStaffMemberEmail,
                    defaultStaffMemberFullName,
                    0,defaultStaffMemberPassword,
                    null,
                    Role.ADMIN_STAFF,
                    defaultStaffMemberStaffID,
                    new User(),
                    defaultStaffMemberStaffUsername,
                    PositionType.FULL_TIME);
            staffService.createAdminStaff(admin);
        }
    }

}
