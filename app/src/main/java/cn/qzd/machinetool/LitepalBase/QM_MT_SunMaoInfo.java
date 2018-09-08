package cn.qzd.machinetool.LitepalBase;

import org.litepal.crud.LitePalSupport;

import java.lang.ref.PhantomReference;

/**
 * Created by admin on 2018/7/17.
 */

public class QM_MT_SunMaoInfo extends LitePalSupport{
    private int infoID;
    private String SunMaoNo;
    private String SunMaoName;
    private int ParentID;
    private byte[] sImage;
    private int MachineType;
    private int IsUse;

    public int getIsUse() {
        return IsUse;
    }

    public void setIsUse(int isUse) {
        IsUse = isUse;
    }

    public int getInfoID() {
        return infoID;
    }

    public void setInfoID(int infoID) {
        this.infoID = infoID;
    }

    public String getSunMaoNo() {
        return SunMaoNo;
    }

    public void setSunMaoNo(String sunMaoNo) {
        SunMaoNo = sunMaoNo;
    }

    public String getSunMaoName() {
        return SunMaoName;
    }

    public void setSunMaoName(String sunMaoName) {
        SunMaoName = sunMaoName;
    }

    public int getParentID() {
        return ParentID;
    }

    public void setParentID(int parentID) {
        ParentID = parentID;
    }

    public byte[] getsImage() {
        return sImage;
    }

    public void setsImage(byte[] sImage) {
        this.sImage = sImage;
    }

    public int getMachineType() {
        return MachineType;
    }

    public void setMachineType(int machineType) {
        MachineType = machineType;
    }
}
