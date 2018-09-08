package cn.qzd.machinetool.base;

/**
 * Created by admin on 2018/6/23.
 */

public class MuYuanSunMode {
    public int pathID ;//刀路id

    public double MaterialWidth ;//材料宽度
    public double MaterialThick ;//材料厚度
    public double GrooveDeep ;//槽深
    public Position CentrePoint ;//圆心点
    public double Radius ;//半径
    public double leftMargin ;//左边间距
    public double bottomMargin ;//下边间距
    public double zSafeDist;//Z安全距离

    public int iCutter ;//刀具编号
    public double knifeDiameter ;//刀具直径
    public double cutDepth ;//每刀深度

    public double cutSpeed ; //切削速度
    public double spindalSpeed ;//主轴转速
    public PathABVal pathAB ;//AB轴对象

    public void setPathID(int pathID) {
        this.pathID = pathID;
    }

    public void setMaterialWidth(double materialWidth) {
        MaterialWidth = materialWidth;
    }

    public void setMaterialThick(double materialThick) {
        MaterialThick = materialThick;
    }

    public void setGrooveDeep(double grooveDeep) {
        GrooveDeep = grooveDeep;
    }

    public void setCentrePoint(Position centrePoint) {
        CentrePoint = centrePoint;
    }

    public void setRadius(double radius) {
        Radius = radius;
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

    public double getMaterialWidth() {
        return MaterialWidth;
    }

    public double getMaterialThick() {
        return MaterialThick;
    }

    public double getGrooveDeep() {
        return GrooveDeep;
    }

    public Position getCentrePoint() {
        return CentrePoint;
    }

    public double getRadius() {
        return Radius;
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
