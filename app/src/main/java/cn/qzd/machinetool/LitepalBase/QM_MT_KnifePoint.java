package cn.qzd.machinetool.LitepalBase;

import org.litepal.crud.LitePalSupport;


/**
 * Created by admin on 2018/7/19.
 */

public class QM_MT_KnifePoint extends LitePalSupport{
    private int FileId;
    private String KnifeName;
    private double L;
    private double S;
    private double X;
    private double Y;
    private String safey;
    private String safez;

    public String getSafey() {
        return safey;
    }

    public void setSafey(String safey) {
        this.safey = safey;
    }

    public String getSafez() {
        return safez;
    }

    public void setSafez(String safez) {
        this.safez = safez;
    }

    public int getFileId() {
        return FileId;
    }

    public void setFileId(int fileId) {
        FileId = fileId;
    }

    public String getKnifeName() {
        return KnifeName;
    }

    public void setKnifeName(String knifeName) {
        KnifeName = knifeName;
    }

    public double getL() {
        return L;
    }

    public void setL(double l) {
        L = l;
    }

    public double getS() {
        return S;
    }

    public void setS(double s) {
        S = s;
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        Y = y;
    }
}
