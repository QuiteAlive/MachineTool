package cn.qzd.machinetool.base;

/**
 * Created by Administrator on 2018/6/13.
 */

public class PathToolSetup {
    public int Cutter;
    public SpindleAB AB;
    public double CutSpeed;
    public double SpindleSpeed;

    public int getCutter() {
        return Cutter;
    }

    public void setCutter(int cutter) {
        Cutter = cutter;
    }

    public SpindleAB getAB() {
        return AB;
    }

    public void setAB(SpindleAB AB) {
        this.AB = AB;
    }

    public double getCutSpeed() {
        return CutSpeed;
    }

    public void setCutSpeed(double cutSpeed) {
        CutSpeed = cutSpeed;
    }

    public double getSpindleSpeed() {
        return SpindleSpeed;
    }

    public void setSpindleSpeed(double spindleSpeed) {
        SpindleSpeed = spindleSpeed;
    }
}

