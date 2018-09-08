package cn.qzd.machinetool.base;

/**
 * Created by admin on 2018/6/22.
 */

public class PathABVal {
    public int ID;
    public int iNumber;
    public int SunMao_Path_ID;
    public int ISRad1Check;//是否选中
    public String ProfileFormula;//姿态公式
    public Double ProfileScope1;//姿态范围1
    public Double ProfileScope2;//姿态范围2
    public int ISRad2Check;
    public String AaxleFormula;//A轴公式
    public Double AaxleScope1;//A轴范围1
    public Double AaxleScope2;//A轴范围2
    public int ISRad3Check;
    public String BaxleFormula;//B轴公式
    public Double BaxleScope1;//A轴范围1
    public Double BaxleScope2;//A轴范围2
    public String StartDot;//起点坐标
    public int ISClockWise;//是否顺时针

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setiNumber(int iNumber) {
        this.iNumber = iNumber;
    }

    public void setSunMao_Path_ID(int sunMao_Path_ID) {
        SunMao_Path_ID = sunMao_Path_ID;
    }

    public void setISRad1Check(int ISRad1Check) {
        this.ISRad1Check = ISRad1Check;
    }

    public void setProfileFormula(String profileFormula) {
        ProfileFormula = profileFormula;
    }

    public void setProfileScope1(Double profileScope1) {
        ProfileScope1 = profileScope1;
    }

    public void setProfileScope2(Double profileScope2) {
        ProfileScope2 = profileScope2;
    }

    public void setISRad2Check(int ISRad2Check) {
        this.ISRad2Check = ISRad2Check;
    }

    public void setAaxleFormula(String aaxleFormula) {
        AaxleFormula = aaxleFormula;
    }

    public void setAaxleScope1(Double aaxleScope1) {
        AaxleScope1 = aaxleScope1;
    }

    public void setAaxleScope2(Double aaxleScope2) {
        AaxleScope2 = aaxleScope2;
    }

    public void setISRad3Check(int ISRad3Check) {
        this.ISRad3Check = ISRad3Check;
    }

    public void setBaxleFormula(String baxleFormula) {
        BaxleFormula = baxleFormula;
    }

    public void setBaxleScope1(Double baxleScope1) {
        BaxleScope1 = baxleScope1;
    }

    public void setBaxleScope2(Double baxleScope2) {
        BaxleScope2 = baxleScope2;
    }

    public void setStartDot(String startDot) {
        StartDot = startDot;
    }

    public void setISClockWise(int ISClockWise) {
        this.ISClockWise = ISClockWise;
    }

    public int getSunMao_Path_ID() {
        return SunMao_Path_ID;
    }

    public int getISRad1Check() {
        return ISRad1Check;
    }

    public String getProfileFormula() {
        return ProfileFormula;
    }

    public Double getProfileScope1() {
        return ProfileScope1;
    }

    public Double getProfileScope2() {
        return ProfileScope2;
    }

    public int getISRad2Check() {
        return ISRad2Check;
    }

    public String getAaxleFormula() {
        return AaxleFormula;
    }

    public Double getAaxleScope1() {
        return AaxleScope1;
    }

    public Double getAaxleScope2() {
        return AaxleScope2;
    }

    public int getISRad3Check() {
        return ISRad3Check;
    }

    public String getBaxleFormula() {
        return BaxleFormula;
    }

    public Double getBaxleScope1() {
        return BaxleScope1;
    }

    public Double getBaxleScope2() {
        return BaxleScope2;
    }

    public String getStartDot() {
        return StartDot;
    }

    public int getISClockWise() {
        return ISClockWise;
    }

    public int getiNumber() {
        return iNumber;
    }

    public int getID() {
        return ID;
    }
}
