package cn.qzd.machinetool.base;

/**
 * Created by mb on 2018/8/24.
 */

public class LMode {
    public double sWidth ;//料宽
    public double sThickness ;//料厚

    public double mtHeight ;//榫头高度(界面显示为高度)

    public double L_mtWidth ;//L榫头宽
    public double L_mtLength ;//L榫头长
    public double L_mtThickness ;//L榫头厚

    public double leftMargin ;//左边间距
    public double bottomMargin ;//下边间距
    public double zSafeDist ;//Z安全距离

    public int iCutter ;//刀具编号
    public double knifeDiameter ;//刀具直径
    public double depthPercent ; //切削百分比
    public double cutDepth ;//每刀深度
    public double cutSpeed ; //切削速度
    public double spindalSpeed ;//主轴转速

    public PathABVal pathAB ;//AB轴对象
}
