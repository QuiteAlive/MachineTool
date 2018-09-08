package cn.qzd.machinetool.LitepalBase;

import org.litepal.crud.LitePalSupport;

/**
 * Created by admin on 2018/7/18.
 */

public class QM_MT_SunMao_Porperty_R extends LitePalSupport{
    private String SunMaoNo;
    private String SunMaoPorperty;
    private String porpertyNo;
    private String PorpertyVal;
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getSunMaoNo() {
        return SunMaoNo;
    }

    public void setSunMaoNo(String sunMaoNo) {
        SunMaoNo = sunMaoNo;
    }

    public String getSunMaoPorperty() {
        return SunMaoPorperty;
    }

    public void setSunMaoPorperty(String sunMaoPorperty) {
        SunMaoPorperty = sunMaoPorperty;
    }

    public String getPorpertyNo() {
        return porpertyNo;
    }

    public void setPorpertyNo(String porpertyNo) {
        this.porpertyNo = porpertyNo;
    }

    public String getPorpertyVal() {
        return PorpertyVal;
    }

    public void setPorpertyVal(String porpertyVal) {
        PorpertyVal = porpertyVal;
    }
}
