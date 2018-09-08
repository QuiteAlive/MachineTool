package cn.qzd.machinetool.base;

/**
 * Created by admin on 2018/6/5.
 */

public class MachineFileContent {
    public int ID;

    public int curIndex;//方案id
    public String indexName;//方案名字
    public String heads;//文件头
    public String tails;//文件尾
    public String safey;
    public String safez;

    public double tL;
    public double tS;
    public double tX;
    public double tY;
    public int[] tCutterType = new int[4];

    //public int tCutterType;
    public int gMotionRapid;
    public int gMotionLinear;
    public int gCircularInterperlationCLW;
    public int gCircularInterperlationCCLW;
    public int gProgramPause;
    public int gPlaneXY;
    public int gPlaneZX;
    public int gPlaneYZ;
    public int gReturnHome;
    public int gProcessCoordinateSystem1;
    public int gProcessCoordinateSystem2;
    public int gProcessCoordinateSystem3;
    public int gProcessCoordinateSystem4;
    public int gProcessCoordinateSystem5;
    public int gProcessCoordinateSystem6;
    public int gMachineCoordinateSystem;
    public int gInchMode;
    public int gMetricMode;
    public int gAbsoluteMode;
    public int gIncrementalMode;
    public int mStop;
    public int mOpstop;
    public int mProgramEnd;
    public int mSpindleOnCLW;
    public int mSpindleOff;
    public int mClampOn;
    public int mClampOff;
    public int mWaittingSignal;
    public String oChangeCutter;
    public String oCutSpeed;
    public String oSpindleSpeed;
    public String host;
    public int port;
    public String user;
    public String password;
    public boolean anonymous;
    //X6+
    public int gleftKeepoff;
    public int gelevateKeepoff;
    public int gdjoseKeepoff;
    public int grightKeepoff;
    public int gleftbyProces;
    public int gelevatebyProces;
    public int gdjosebyProces;
    public int grightbyProces;
    public int gleftAngle;
    public int grightAngle;
    public int mselectT1;
    public int mselectT2;
    public int mselectT3;
    public int mleftFootdown;
    public int mcentreFootdown;
    public int mrightFootdown;
    public int mleftFootloosen;
    public int mcentreFootloosen;
    public int mrightFootloosen;
    public int mleftFootblow;
    public int mcentreFootblow;
    public int mrightFootblow;
    public int geveryMinute;

}
