package cn.qzd.machinetool.LitepalBase;

import org.litepal.crud.LitePalSupport;

/**
 * Created by admin on 2018/7/19.
 */

public class QM_MT_Knifeproperty_path_R extends LitePalSupport{
    private  int SunMao_Path_ID;
    private String knifeName;
    private String property;
    private Double size;

    public int getSunMao_Path_ID() {
        return SunMao_Path_ID;
    }

    public void setSunMao_Path_ID(int sunMao_Path_ID) {
        SunMao_Path_ID = sunMao_Path_ID;
    }

    public String getKnifeName() {
        return knifeName;
    }

    public void setKnifeName(String knifeName) {
        this.knifeName = knifeName;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }
}
