package cn.qzd.machinetool.GetNcFile;

import android.content.Context;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.qzd.machinetool.base.PathABVal;
import cn.qzd.machinetool.base.PathToolSetup;
import cn.qzd.machinetool.base.Position;
import cn.qzd.machinetool.base.SpindleAB;
import cn.qzd.machinetool.base.WorkOne;
import cn.qzd.machinetool.util.PreferencesUtils;

/**
 * Created by Administrator on 2018/7/3.
 */

public class NCParameterHandle {
    //输出坐标
    public static String outGoto1(Position pos)
    {
        String x1=roundByScale(pos.X,6);
        String y1=roundByScale(pos.Y,6);
        String z1=roundByScale(pos.Z,6);
        return String.format("goto:%s %s %s", x1, y1, z1);
    }
    //输出坐标
    public static String outGoto(Position pos, PathABVal pathAB, boolean b)
    {
        if (b)
        {
            Position posNew = ConvertPointByAB(pos, pathAB);
            pos = posNew;
        }
        String x=roundByScale(pos.X,6);
        String y=roundByScale(pos.Y,6);
        String z=roundByScale(pos.Z,6);
        return String.format("goto:%s %s %s", x, y, z);

    }
    //输出刀具
    public static String outCutter(int type) {
        String t = String.valueOf(type);
        return String.format("cutter:%s", type);
    }
    //输出AB轴
    public static String outAB(SpindleAB AB) {
        String a = String.valueOf(AB.A);
        String b = String.valueOf(AB.B);
        return String.format("AB:%s %s", a, b);
    }
    public static int outPostion(WorkOne postion) {
        int postion1=postion.postion;
        return  postion1;

    }
    //输出坐标
    public static String outPset(Position pos, PathABVal pathAB, boolean b)
    {
        if (b)
        {
            Position posNew = ConvertPointByAB(pos, pathAB);
            pos = posNew;
        }
        String x=roundByScale(pos.X,6);
        String y=roundByScale(pos.Y,6);
        String z=roundByScale(pos.Z,6);
        return String.format("pset:%s %s %s", x, y, z);
    }
    //输出切削速度
    public static String outCutSpeed(double speed) {
        String s = String.valueOf(speed);
        return String.format("cutSpeed:%s", s);
    }
    //输出主轴转速
    public static String outSpindleSpeed(double speed) {
        String s = String.valueOf(speed);
        return String.format("spindleSpeed:%s", s);
    }
    //输出安全坐标
    public static String outSafeY() {
        return "safeY";
    }

    public static PathToolSetup GetPathParameter(int iCutter, SpindleAB ab, double cutSpeed, double SpindleSpeed)
    {
        PathToolSetup pathTool = new PathToolSetup();
        pathTool.Cutter = iCutter;
        pathTool.AB = ab;
        pathTool.CutSpeed = cutSpeed;
        pathTool.SpindleSpeed = SpindleSpeed;
        return pathTool;
    }

    public static ArrayList SetParameterToArray(ArrayList arrConFst, PathToolSetup pathPar)
    {
        arrConFst.add(outCutter(pathPar.Cutter));
        arrConFst.add(outAB(pathPar.AB));
        arrConFst.add(outCutSpeed(pathPar.CutSpeed));
        arrConFst.add(outSpindleSpeed(pathPar.SpindleSpeed));
        return arrConFst;
    }
    public static Position ConvertPointByAB( Position point, PathABVal pathAB)
    {
        Position newPos = new Position();
        if ( pathAB.AaxleFormula == "none"){
            double x = point.X;
            double y = point.Y;
            double z = point.Z;

            //姿态:逆时针 90
            double angleZT = Double.valueOf(pathAB.ProfileFormula);
            double radianZT = Math.PI / 180 * angleZT;
            double x1 = x * Math.cos(radianZT) - y * Math.sin(radianZT);
            double y1 = y * Math.cos(radianZT) + x * Math.sin(radianZT);
            double z1 = z;

            Position p = new Position();
            String[] pathABStart = pathAB.StartDot.split(",");
            p.X = Double.valueOf(pathABStart[0]);
            p.Y = Double.valueOf(pathABStart[1]);
            p.Z = Double.valueOf(pathABStart[2]);

            newPos.X = x + p.X;
            newPos.Y = y + p.Y;
            newPos.Z = z + p.Z;

        }else {
            double x = point.X;
            double y = point.Y;
            double z = point.Z;

            //姿态:逆时针 90
            double angleZT = Double.valueOf(pathAB.ProfileFormula);
            double radianZT = Math.PI / 180 * angleZT;
            double x1 = x * Math.cos(radianZT) - y * Math.sin(radianZT);
            double y1 = y * Math.cos(radianZT) + x * Math.sin(radianZT);
            double z1 = z;

            //y轴：=b轴角度 顺时针 90
            double angleYB = Double.valueOf(pathAB.BaxleFormula);
            double radianYB = Math.PI / 180 * angleYB;
            double x2 = (x1 * Math.cos(radianYB) + z1 * Math.sin(radianYB));
            double y2 = y1;
            double z2 = (z1 * Math.cos(radianYB) - x1 * Math.sin(radianYB));


            //z轴 = a轴角度逆时针
            double angleZA = Double.valueOf(pathAB.AaxleFormula);
            double radianZA = Math.PI / 180 * angleZA;
            double x3 = (x2 * Math.cos(radianZA) - y2 * Math.sin(radianZA));
            double y3 = (y2 * Math.cos(radianZA) + x2 * Math.sin(radianZA));
            double z3 = z2;
            Position p = new Position();
            String[] pathABStart = pathAB.StartDot.split(",");
            p.X = Double.valueOf(pathABStart[0]);
            p.Y = Double.valueOf(pathABStart[1]);
            p.Z = Double.valueOf(pathABStart[2]);

            newPos.X = x3 + p.X;
            newPos.Y = y3 + p.Y;
            newPos.Z = z3 + p.Z;
        }
        return newPos;

    }
    public static String roundByScale(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        if(scale == 0){
            return new DecimalFormat("0").format(v);
        }
        String formatStr = "0.";
        for(int i=0;i<scale;i++){
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }
    /// <summary>
    /// 将List<Position> 数据转换成 ArrayList对象 WH ADD
    /// </summary>
    /// <param name="lisFileConent">引用外部对象</param>
    /// <param name="points">需要转换的点集</param>
    public static void ListTOOutGoArrayList(ArrayList lisFileConent, List<Position> points, PathABVal pABV)
    {

        int isRadP = pABV.ISRad1Check;
        double x1=0,y1=0,z1 = 0;
        Position startP = new Position();
        double pAngle = Double.valueOf(pABV.ProfileFormula);
        double aAngle = Double.valueOf(pABV.AaxleFormula);
        double bAngle = Double.valueOf(pABV.BaxleFormula);

        if (pABV.StartDot != "") {
            String[] pathABStart = pABV.StartDot.split(",");
            x1 =  Double.valueOf(pathABStart[0]);
            y1 =  Double.valueOf(pathABStart[1]);
            z1 =  Double.valueOf(pathABStart[2]);
        }

        if (isRadP == 0) {
            for (Position point:points
                    ) {
                Position  p = ShiftByZAxis(point, pAngle * Math.PI / 180.0);
                p = ShiftByYAxis(p, bAngle * Math.PI / 180.0);
                p = ShiftByZAxis(p, aAngle * Math.PI / 180.0);
                p.X += x1;
                p.Y += y1;
                p.Z += z1;
                lisFileConent.add(outGoto1(p));
            }
        }
    }
    public static Position ShiftByZAxis(Position p, double radian)
    {
        Position nP = new Position();
        nP.X = p.X * Math.cos(radian) - p.Y * Math.sin(radian);
        nP.Y = p.Y * Math.cos(radian) + p.X * Math.sin(radian);
        nP.Z = p.Z;
        return nP;
    }

    public static Position ShiftByYAxis(Position p, double radian)
    {
        Position nP = new Position();
        nP.X = p.X * Math.cos(radian) + p.Z * Math.sin(radian);
        nP.Y = p.Y;
        nP.Z = p.Z * Math.cos(radian) - p.X * Math.sin(radian);
        return nP;
    }
}
