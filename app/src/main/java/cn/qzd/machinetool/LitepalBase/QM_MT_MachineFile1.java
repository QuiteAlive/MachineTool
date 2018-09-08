package cn.qzd.machinetool.LitepalBase;

/**
 * Created by admin on 2018/7/18.
 */

import org.litepal.LitePal;


import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

/**
 * Created by admin on 2018/7/18.
 */

public class QM_MT_MachineFile1 extends LitePalSupport {
    private int ShaftId;
    private String FileName;
    private String Company;
    private int MachinefileId;
    private int ShaftType;

    public int getShaftType() {
        return ShaftType;
    }

    public void setShaftType(int shaftType) {
        ShaftType = shaftType;
    }

    public int getMachinefileId() {
        return MachinefileId;
    }

    public void setMachinefileId(int machinefileId) {
        MachinefileId = machinefileId;
    }

    public int getShaftId() {
        return ShaftId;
    }

    public void setShaftId(int shaftId) {
        ShaftId = shaftId;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

}

