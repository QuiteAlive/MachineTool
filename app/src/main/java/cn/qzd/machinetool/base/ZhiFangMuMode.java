package cn.qzd.machinetool.base;

/**
 * Created by admin on 2018/6/23.
 */

public class ZhiFangMuMode {
    public int pathID;//刀路id

    public double Adot;//A点 
    public double MaterialWidth;//材料宽度
    public double MaterialThick;//材料厚度
    public double GrooveWidth;//槽宽
    public double GrooveDeep;//槽深
    public double GrooveThick;//槽厚
    public double leftMargin;//左边间距
    public double bottomMargin;//下边间距
    public double zSafeDist;//Z安全距离

    public int iCutter;//刀具编号
    public double knifeDiameter;//刀具直径
    public double cutDepth;//每刀深度
    public double cutSpeed; //切削速度
    public double spindalSpeed;//主轴转速
    public PathABVal pathAB;//AB轴对象

    public void setPathID(int pathID) {
        this.pathID = pathID;
    }

    public void setAdot(double adot) {
        Adot = adot;
    }

    public void setMaterialWidth(double materialWidth) {
        MaterialWidth = materialWidth;
    }

    public void setMaterialThick(double materialThick) {
        MaterialThick = materialThick;
    }

    public void setGrooveWidth(double grooveWidth) {
        GrooveWidth = grooveWidth;
    }

    public void setGrooveDeep(double grooveDeep) {
        GrooveDeep = grooveDeep;
    }

    public void setGrooveThick(double grooveThick) {
        GrooveThick = grooveThick;
    }

    public void setLeftMargin(double leftMargin) {
        this.leftMargin = leftMargin;
    }

    public void setBottomMargin(double bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public void setiCutter(int iCutter) {
        this.iCutter = iCutter;
    }

    public void setKnifeDiameter(double knifeDiameter) {
        this.knifeDiameter = knifeDiameter;
    }

    public void setCutDepth(double cutDepth) {
        this.cutDepth = cutDepth;
    }

    public void setCutSpeed(double cutSpeed) {
        this.cutSpeed = cutSpeed;
    }

    public void setSpindalSpeed(double spindalSpeed) {
        this.spindalSpeed = spindalSpeed;
    }

    public void setPathAB(PathABVal pathAB) {
        this.pathAB = pathAB;
    }

    public int getPathID() {
        return pathID;
    }

    public double getAdot() {
        return Adot;
    }

    public double getMaterialWidth() {
        return MaterialWidth;
    }

    public double getMaterialThick() {
        return MaterialThick;
    }

    public double getGrooveWidth() {
        return GrooveWidth;
    }

    public double getGrooveDeep() {
        return GrooveDeep;
    }

    public double getGrooveThick() {
        return GrooveThick;
    }

    public double getLeftMargin() {
        return leftMargin;
    }

    public double getBottomMargin() {
        return bottomMargin;
    }

    public int getiCutter() {
        return iCutter;
    }

    public double getKnifeDiameter() {
        return knifeDiameter;
    }

    public double getCutDepth() {
        return cutDepth;
    }

    public double getCutSpeed() {
        return cutSpeed;
    }

    public double getSpindalSpeed() {
        return spindalSpeed;
    }

    public PathABVal getPathAB() {
        return pathAB;
    }
}
