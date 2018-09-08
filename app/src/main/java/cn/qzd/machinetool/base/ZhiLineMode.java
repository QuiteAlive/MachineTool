package cn.qzd.machinetool.base;

import cn.qzd.machinetool.base.PathABVal;

/**
 * Created by Administrator on 2018/6/14.
 */

public class ZhiLineMode {
    public int pathID;//刀路id
    public double width;//料宽
    public double thickness;//料厚
    public double length;//榫头长度
    public double GrooveDeep;//槽深
    public double Adot;//A点
    public double zSafeDist;//Z安全距离
    public int iCutter ;//刀具编号
    public double knifeDiameter;//刀具直径
    public double depthPercent; //切削百分比
    public double cutDepth;//每刀深度
    public double cutSpeed; //切削速度
    public double spindalSpeed ;//主轴转速

    public PathABVal pathAB;//AB轴对象

    public int getPathID() {
        return pathID;
    }

    public void setPathID(int pathID) {
        this.pathID = pathID;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getGrooveDeep() {
        return GrooveDeep;
    }

    public void setGrooveDeep(double grooveDeep) {
        GrooveDeep = grooveDeep;
    }

    public double getAdot() {
        return Adot;
    }

    public void setAdot(double adot) {
        Adot = adot;
    }

    public double getKnifeDiameter() {
        return knifeDiameter;
    }

    public void setKnifeDiameter(double knifeDiameter) {
        this.knifeDiameter = knifeDiameter;
    }

    public double getDepthPercent() {
        return depthPercent;
    }

    public void setDepthPercent(double depthPercent) {
        this.depthPercent = depthPercent;
    }

    public double getCutDepth() {
        return cutDepth;
    }

    public void setCutDepth(double cutDepth) {
        this.cutDepth = cutDepth;
    }

    public double getCutSpeed() {
        return cutSpeed;
    }

    public void setCutSpeed(double cutSpeed) {
        this.cutSpeed = cutSpeed;
    }

    public double getSpindalSpeed() {
        return spindalSpeed;
    }

    public void setSpindalSpeed(double spindalSpeed) {
        this.spindalSpeed = spindalSpeed;
    }

    public PathABVal getPathAB() {
        return pathAB;
    }

    public void setPathAB(PathABVal pathAB) {
        this.pathAB = pathAB;
    }
}
