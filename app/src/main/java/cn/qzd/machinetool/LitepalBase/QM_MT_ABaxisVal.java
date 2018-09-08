package cn.qzd.machinetool.LitepalBase;

import org.litepal.crud.LitePalSupport;

/**
 * Created by admin on 2018/7/17.
 */

public class QM_MT_ABaxisVal extends LitePalSupport{
    private int SunMao_Path_ID;
    private int ISRad1Check;//是否选中
    private String ProfileFormula;//姿态公式
    private Double ProfileScope1;//姿态范围1
    private Double ProfileScope2;//姿态范围2
    private int ISRad2Check;
    private String AaxleFormula;//A轴公式
    private Double AaxleScope1;//A轴范围1
    private Double AaxleScope2;//A轴范围2
    private int ISRad3Check;
    private String BaxleFormula;//B轴公式
    private Double BaxleScope1;//A轴范围1
    private Double BaxleScope2;//A轴范围2
    private String StartDot;//起点坐标
    private int ISClockWise;//是否顺时针

    public int getSunMao_Path_ID() {
        return SunMao_Path_ID;
    }

    public void setSunMao_Path_ID(int sunMao_Path_ID) {
        SunMao_Path_ID = sunMao_Path_ID;
    }

    public int getISRad1Check() {
        return ISRad1Check;
    }

    public void setISRad1Check(int ISRad1Check) {
        this.ISRad1Check = ISRad1Check;
    }

    public String getProfileFormula() {
        return ProfileFormula;
    }

    public void setProfileFormula(String profileFormula) {
        ProfileFormula = profileFormula;
    }

    public Double getProfileScope1() {
        return ProfileScope1;
    }

    public void setProfileScope1(Double profileScope1) {
        ProfileScope1 = profileScope1;
    }

    public Double getProfileScope2() {
        return ProfileScope2;
    }

    public void setProfileScope2(Double profileScope2) {
        ProfileScope2 = profileScope2;
    }

    public int getISRad2Check() {
        return ISRad2Check;
    }

    public void setISRad2Check(int ISRad2Check) {
        this.ISRad2Check = ISRad2Check;
    }

    public String getAaxleFormula() {
        return AaxleFormula;
    }

    public void setAaxleFormula(String aaxleFormula) {
        AaxleFormula = aaxleFormula;
    }

    public Double getAaxleScope1() {
        return AaxleScope1;
    }

    public void setAaxleScope1(Double aaxleScope1) {
        AaxleScope1 = aaxleScope1;
    }

    public Double getAaxleScope2() {
        return AaxleScope2;
    }

    public void setAaxleScope2(Double aaxleScope2) {
        AaxleScope2 = aaxleScope2;
    }

    public int getISRad3Check() {
        return ISRad3Check;
    }

    public void setISRad3Check(int ISRad3Check) {
        this.ISRad3Check = ISRad3Check;
    }

    public String getBaxleFormula() {
        return BaxleFormula;
    }

    public void setBaxleFormula(String baxleFormula) {
        BaxleFormula = baxleFormula;
    }

    public Double getBaxleScope1() {
        return BaxleScope1;
    }

    public void setBaxleScope1(Double baxleScope1) {
        BaxleScope1 = baxleScope1;
    }

    public Double getBaxleScope2() {
        return BaxleScope2;
    }

    public void setBaxleScope2(Double baxleScope2) {
        BaxleScope2 = baxleScope2;
    }

    public String getStartDot() {
        return StartDot;
    }

    public void setStartDot(String startDot) {
        StartDot = startDot;
    }

    public int getISClockWise() {
        return ISClockWise;
    }

    public void setISClockWise(int ISClockWise) {
        this.ISClockWise = ISClockWise;
    }
}
