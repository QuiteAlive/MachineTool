package cn.qzd.machinetool.base;

/**
 * Created by Administrator on 2018/6/13.
 */

public class Position {
    public double X;
    public double Y;
    public double Z;

    public boolean isZero()
    {
        if (X == 0 && Y == 0 && Z == 0) { return true; }
        else { return false; }
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

    public double getZ() {
        return Z;
    }

    public void setZ(double z) {
        Z = z;
    }
}

