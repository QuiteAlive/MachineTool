package cn.qzd.machinetool.LitepalBase;

import org.litepal.crud.LitePalSupport;

/**
 * Created by admin on 2018/7/18.
 */

public class QM_MT_SunMaoPathParameterVal extends LitePalSupport{
    private String property;
    private String size;
    private int SunMao_Path_ID;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getSunMao_Path_ID() {
        return SunMao_Path_ID;
    }

    public void setSunMao_Path_ID(int sunMao_Path_ID) {
        SunMao_Path_ID = sunMao_Path_ID;
    }
}
