package cn.qzd.machinetool.base;

/**
 * Created by admin on 2018/6/22.
 */

public class CutParameter {
    public double CutDeep;
    public double CutPercent;
    public double KnifeDiameter ;
    public double KnifeThick;//锯片厚度

    public double getCutDeep() {
        return CutDeep;
    }

    public double getCutPercent() {
        return CutPercent;
    }

    public double getKnifeDiameter() {
        return KnifeDiameter;
    }

    public void setCutDeep(double cutDeep) {
        CutDeep = cutDeep;
    }

    public void setCutPercent(double cutPercent) {
        CutPercent = cutPercent;
    }

    public void setKnifeDiameter(double knifeDiameter) {
        KnifeDiameter = knifeDiameter;
    }
}
