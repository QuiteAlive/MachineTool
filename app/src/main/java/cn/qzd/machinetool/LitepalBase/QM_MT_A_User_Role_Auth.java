package cn.qzd.machinetool.LitepalBase;

import org.litepal.crud.LitePalSupport;

/**
 * Created by admin on 2018/7/20.
 */

public class QM_MT_A_User_Role_Auth extends LitePalSupport{
    private int UserID;
    private int RoleId;

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getRoleId() {
        return RoleId;
    }

    public void setRoleId(int roleId) {
        RoleId = roleId;
    }
}
