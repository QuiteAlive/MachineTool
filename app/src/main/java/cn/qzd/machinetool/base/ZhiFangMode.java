package cn.qzd.machinetool.base;

/**
 * Created by admin on 2018/6/24.
 */

public class ZhiFangMode {
    public int pathID ;//刀路id

    public double sWidth ;//料宽
    public double sThickness ;//料厚

    public double mtWidth ;//榫头宽
    public double mtThickness ;//榫头厚
    public double mtLength ;//榫头长度

    public double leftMargion ;//左边间距
    public double bottomMargion ;//右边间距
    public double Adot ;//A点 
    public double zSafeDist;//Z安全距离

    public int iCutter ;//刀具编号
    public double knifeDiameter ;//刀具直径
    public double depthPercent ; //切削百分比
    public double cutDepth ;//每刀深度
    public double cutSpeed ; //切削速度
    public double spindalSpeed ;//主轴转速

    public PathABVal pathAB ;//AB轴对象
}
