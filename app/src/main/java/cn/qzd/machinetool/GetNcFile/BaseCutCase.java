package cn.qzd.machinetool.GetNcFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.qzd.machinetool.base.CutParameter;
import cn.qzd.machinetool.base.PathABVal;
import cn.qzd.machinetool.base.Position;
import cn.qzd.machinetool.base.WasteAndroid;

/**
 * Created by Administrator on 2018/7/3.
 */

public class BaseCutCase {
    public static final double _KNIFELIFT = 10;

    //region 获取XY平面顺时针圆弧刀路,起终点一致为一整圆
    // <param name="from">起点</param>
    // <param name="to">终点</param>
    // <param name="centerPoint">圆心坐标</param>
    // <param name="radius">半径</param>
    // <param name="cutDeep">车削深度</param>
    // <returns></returns>
    public static ArrayList getCurveClockPathDataXY(ArrayList arrConFst, PathABVal pathAB, Position from, Position to, Position centerPoint, double radius, double cutDeep)
    {
        boolean flag = true;
        double perArcLength = 0.5;//每一段的弧长度
        double Z = centerPoint.Z;

        double stepAngle = (perArcLength * 180) / (Math.PI * radius);
//        BigDecimal b2 = new BigDecimal(new Double(stepAngle).toString());
//        stepAngle = b2.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();

        double vF = (from.X - centerPoint.X) / radius;
        double vT = (to.X - centerPoint.X) / radius;

        double angleFrom =(Math.acos(vF) * (180.0 / Math.PI));
//        BigDecimal b = new BigDecimal(new Double(angleFrom).toString());
//        angleFrom = b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();

        double angleTo = (Math.acos(vT) * (180.0 / Math.PI));
//        BigDecimal b1 = new BigDecimal(new Double(angleTo).toString());
//        angleTo = b1.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();


        if (from.Y - centerPoint.Y < 0)
        {
            angleFrom = -angleFrom;
            if (to.Y - centerPoint.Y < 0) angleTo = -angleTo;
            if (angleTo > angleFrom) angleTo -= 360;
        }
        else
        {
            if (to.Y - centerPoint.Y < 0) { angleTo = -angleTo; }
            else if (angleTo > angleFrom) { angleTo -= 360; }
        }
        if (from.X+from.Y+from.Z == to.X+to.Y+to.Z){angleTo = angleTo - 360;}

        stepAngle = Math.round(stepAngle);
        double currentAngle = angleFrom + stepAngle;
        while (flag)
        {
            Position point = new Position();
            point.Z = Z;
            if (angleTo < currentAngle)
            {
                //判断是否满足一次的车削量,不满足就给剩余的值
                stepAngle = currentAngle - angleTo > stepAngle ? stepAngle : currentAngle - angleTo;
                currentAngle -= stepAngle;
                point.X = centerPoint.X + Math.cos(currentAngle * (Math.PI / 180)) * radius;
                point.Y = centerPoint.Y + Math.sin(currentAngle * (Math.PI / 180)) * radius;
                arrConFst.add(NCParameterHandle.outGoto(point, pathAB, true));
            }
            else
            {
                flag = false;
            }
        }
        return arrConFst;
    }
    //endregion

    //region 获取XY平面逆时针圆弧刀路,起终点一致为一整圆
    /// <param name="from">起点</param>
    /// <param name="to">终点</param>
    /// <param name="centerPoint">圆心坐标</param>
    /// <param name="radius">半径</param>
    /// <param name="cutDeep">车削深度</param>
    /// <returns></returns>
    public static ArrayList getCurveAntiClockPathDataXY(ArrayList arrConFst, PathABVal pathAB, Position from, Position to, Position centerPoint, double radius, double cutDeep)
    {
        boolean flag = true;
        double perArcLength = 0.5;//每一段的弧长度
        double Z = centerPoint.Z - cutDeep;
        double stepAngle = (perArcLength * 180) / (Math.PI * radius);
        double angleFrom = Math.acos((from.X - centerPoint.X) / radius) * (180 / Math.PI);
        double angleTo = Math.acos((to.X - centerPoint.X) / radius) * (180 / Math.PI);
        if (from.Y - centerPoint.Y < 0)
        {
            angleFrom = -angleFrom;
            if (to.Y - centerPoint.Y < 0) angleTo = -angleTo;
            if (angleTo < angleFrom) angleTo += 360;
        }
        else
        {
            if (to.Y - centerPoint.Y < 0) { angleTo = 360 - angleTo; }
            else if (angleTo < angleFrom) { angleTo += 360; }
        }
        if (from.X+from.Y+from.Z==to.X+to.Y+to.Z){ angleTo = angleTo + 360; }

        stepAngle = Math.round(stepAngle);
        double currentAngle = angleFrom - stepAngle;
        while (flag)
        {
            Position point = new Position();
            point.Z = Z;
            if (angleTo > currentAngle)
            {
                //判断是否满足一次的车削量,不满足就给剩余的值
                stepAngle = angleTo - currentAngle > stepAngle ? stepAngle : angleTo - currentAngle;
                currentAngle += stepAngle;
                point.X = centerPoint.X + Math.cos(currentAngle * (Math.PI / 180)) * radius;
                point.Y = centerPoint.Y + Math.sin(currentAngle * (Math.PI / 180)) * radius;
                arrConFst.add(NCParameterHandle.outGoto(point, pathAB, true));
            }
            else
            {
                flag = false;
            }
        }
        return arrConFst;
    }
    //endregion

    //region 获取XY平面顺时针圆弧刀路,起终点一致为一整圆
    /// <param name="from">起点</param>
    /// <param name="to">终点</param>
    /// <param name="centerPoint">圆心坐标</param>
    /// <param name="radius">半径</param>
    /// <param name="cutDeep">车削深度</param>
    /// <returns></returns>
    public static List<Position> getCurveClockPathDataXY(Position from, Position to, Position centerPoint, double radius, double cutDeep)
    {
        List<Position> pointData = new ArrayList<Position>();

        if (radius == 0) { pointData.add(centerPoint); return pointData; }

        boolean flag = true;
        double perArcLength = 0.5D;//每一段的弧长度
        double Z = centerPoint.Z - cutDeep;
        double stepAngle = (perArcLength * 180) / (Math.PI * radius);
        double vF = (from.X - centerPoint.X) / radius;
        double vT = (to.X - centerPoint.X) / radius;
        double angleFrom = Math.acos(vF) * (180.0D / Math.PI);
        double angleTo = Math.acos(vT) * (180.0D / Math.PI);

        if (from.Y - centerPoint.Y < 0)
        {
            angleFrom = -angleFrom;
            if (to.Y - centerPoint.Y < 0) angleTo = -angleTo;
            if (angleTo > angleFrom) angleTo -= 360;
        }
        else
        {
            if (to.Y - centerPoint.Y < 0) { angleTo = -angleTo; }
            else if (angleTo > angleFrom) { angleTo -= 360; }
        }

        if (from.X + from.Y + from.Z == to.X + to.Y + to.Z) { angleTo = angleTo - 360; }
        double currentAngle = angleFrom + stepAngle;

        while (flag)
        {
            Position point = new Position();
            point.Z = Z;
            if (angleTo < currentAngle)
            {
                //判断是否满足一次的车削量,不满足就给剩余的值
                stepAngle = currentAngle - angleTo > stepAngle ? stepAngle : currentAngle - angleTo;
                currentAngle -= stepAngle;
                point.X = centerPoint.X + Math.cos(currentAngle * (Math.PI / 180)) * radius;
                point.Y = centerPoint.Y + Math.sin(currentAngle * (Math.PI / 180)) * radius;
                pointData.add(point);
            }
            else
            {
                flag = false;
            }
        }
        return pointData;
    }
    //endregion

    //region 获取XY平面逆时针圆弧刀路,起终点一致为一整圆
    /// <param name="from">起点</param>
    /// <param name="to">终点</param>
    /// <param name="centerPoint">圆心坐标</param>
    /// <param name="radius">半径</param>
    /// <param name="cutDeep">车削深度</param>
    /// <returns></returns>
    public static List<Position> getCurveAntiClockPathDataXY(Position from, Position to, Position centerPoint, double radius,double cutDeep)
    {
        List<Position> pointData = new ArrayList<Position>();
        if (radius == 0) { pointData.add(centerPoint); return pointData; }

        boolean flag = true;
        double perArcLength = 0.5;//每一段的弧长度
        double Z = centerPoint.Z - cutDeep;
        double stepAngle = (perArcLength * 180) / (Math.PI * radius );
        double vF = (from.X - centerPoint.X) / radius;
        double vT = (to.X - centerPoint.X) / radius;
        double angleFrom =(Math.acos(vF) * (180.0 / Math.PI));

        BigDecimal b = new BigDecimal(new Double(angleFrom).toString());
        angleFrom = b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
        double angleTo = (Math.acos(vT) * (180.0 / Math.PI));
        BigDecimal b1 = new BigDecimal(new Double(angleTo).toString());
        angleTo = b1.setScale(0,BigDecimal.ROUND_HALF_UP).doubleValue();

        if (from.Y - centerPoint.Y < 0)
        {
            angleFrom = -angleFrom;
            if (to.Y - centerPoint.Y < 0) angleTo = -angleTo;
            if (angleTo < angleFrom) angleTo += 360;

        }
        else
        {
            if (to.Y - centerPoint.Y < 0) { angleTo = 360 - angleTo; }
            else if (angleTo < angleFrom) { angleTo += 360; }
        }
        if (from.X + from.Y + from.Z == to.X + to.Y + to.Z) { angleTo = angleTo + 360; }

        double currentAngle = angleFrom - stepAngle;
        System.out.println("angleFrom:"+angleFrom+"  angleTo"+angleTo);
        while (flag)
        {
            Position point = new Position();
            point.Z = Z;
            if (angleTo > currentAngle)
            {
                //判断是否满足一次的车削量,不满足就给剩余的值
                stepAngle = angleTo - currentAngle > stepAngle ? stepAngle : angleTo - currentAngle;
                currentAngle += stepAngle;

                System.out.println("currentAngle: "+currentAngle);
                point.X = centerPoint.X + Math.cos(currentAngle * (Math.PI / 180)) * radius;
                point.Y = centerPoint.Y + Math.sin(currentAngle * (Math.PI / 180)) * radius;
                pointData.add(point);
            }
            else
            {
                flag = false;
            }
        }
        return pointData;
    }
    //endregion

    //region 全开口榫槽逆时针,宽度大于刀具直径ok
    /// </summary>
    /// <param name="sidePoint">榫槽左侧边缘点为基准点</param>
    /// <param name="grooveDeep">榫槽深度</param>
    /// <param name="grooveWidth">榫槽宽度</param>
    /// <param name="grooveLength">榫槽长度</param>
    /// <param name="cutParameter"></param>
    /// <returns></returns>
    public static void getAntiClockGroovePathData(ArrayList pointList,Position sidePoint, WasteAndroid w)
    {
        double perval = (w.cup.KnifeDiameter / 2);
        if (w.cup.CutPercent != 0.0D)
            perval = (w.cup.KnifeDiameter * w.cup.CutPercent);//进刀量
        double halfWidth = (w.Width - w.cup.KnifeDiameter) / 2;//一半宽度
        double startZ = sidePoint.Z;//Z轴起点坐标值
        double centerWidthX = sidePoint.X - w.Width / 2;//宽度中心点的X坐标值
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标值
        double currentWidth = 0.0D;//当前车削的宽度值
        double stepWidth = perval;//宽度方向上每一次的进刀量

        stepWidth = halfWidth - currentWidth > perval ? perval : halfWidth - currentWidth;
        Position initP = new Position();
        initP.X = centerWidthX + stepWidth;
        initP.Y = sidePoint.Y - w.cup.KnifeDiameter;
        initP.Z = w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));

        Position safePoint2 = new Position();//安全点2

        while (targetDeepZ < sidePoint.Z)
        {
            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z);
            sidePoint.Z -= w.cup.CutDeep;
            currentWidth = 0.0D; stepWidth = perval;

            while (currentWidth < halfWidth)
            {
                //1.确定安全点1;2.设定深度点;3进刀点;4.去程终止点;5.返程起点;6.返程终止点;7.安全点2;

                //判断 槽宽当前的值
                stepWidth = halfWidth - currentWidth > perval ? perval : halfWidth - currentWidth;
                currentWidth += stepWidth;

                Position safePoint1 = new Position();//安全点1
                safePoint1.X = safePoint2.isZero() ? initP.X : centerWidthX + currentWidth;
                safePoint1.Y = initP.Y;
                safePoint1.Z = safePoint2.isZero() ? startZ : safePoint2.Z;
                pointList.add(NCParameterHandle.outPset(safePoint1, w.pathAB, true));

                Position deepPoint = new Position();//设定深度点
                deepPoint.X = safePoint1.X; deepPoint.Y = safePoint1.Y; deepPoint.Z = sidePoint.Z;
                pointList.add(NCParameterHandle.outPset(deepPoint, w.pathAB, true));

                Position startCutPoint = new Position();//进刀点
                startCutPoint.X = deepPoint.X; startCutPoint.Y = deepPoint.Y + w.cup.KnifeDiameter;
                startCutPoint.Z = deepPoint.Z;
                pointList.add(NCParameterHandle.outGoto(startCutPoint, w.pathAB, true));

                Position endCutPoint = new Position();//去程终止点
                endCutPoint.X = startCutPoint.X; endCutPoint.Y = startCutPoint.Y + w.Thickness;
                endCutPoint.Z = startCutPoint.Z;
                pointList.add(NCParameterHandle.outGoto(endCutPoint, w.pathAB, true));

                Position middTurnPoint = new Position();//返程起点
                middTurnPoint.X = centerWidthX - currentWidth; middTurnPoint.Y = endCutPoint.Y;
                middTurnPoint.Z = endCutPoint.Z;
                pointList.add(NCParameterHandle.outGoto(middTurnPoint, w.pathAB, true));

                Position turnEndPoint = new Position();//返程终止点
                turnEndPoint.X = middTurnPoint.X; turnEndPoint.Y = middTurnPoint.Y - w.Thickness; turnEndPoint.Z = middTurnPoint.Z;
                pointList.add(NCParameterHandle.outGoto(turnEndPoint, w.pathAB, true));

                safePoint2 = new Position();
                safePoint2.X = turnEndPoint.X; safePoint2.Y = turnEndPoint.Y - w.cup.KnifeDiameter;
                safePoint2.Z = turnEndPoint.Z;
                pointList.add(NCParameterHandle.outPset(safePoint2, w.pathAB, true));

            }
        }
        Position resetPoint = new Position();
        resetPoint.X = safePoint2.X;
        resetPoint.Y = safePoint2.Y;
        resetPoint.Z = w.Deep + w.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(resetPoint, w.pathAB, true));
    }
    //endregion***

    //region 全开口榫槽逆时针,宽度等于刀具直径混合铣ok
    /// </summary>
    /// <param name="sidePoint">榫槽左侧边缘点为基准点</param>
    /// <param name="grooveDeep">榫槽深度</param>
    /// <param name="grooveLength">榫槽长度</param>
    /// <param name="cutParameter"></param>
    /// <returns></returns>
    public static void getAntiOpenGrooveMixPathData( ArrayList pointList,Position sidePoint,WasteAndroid w)
    {
        double Z = sidePoint.Z;
        double targetDeepZ = Z - w.Deep;//目标深度Z轴坐标
        int i = 1;

        Position initP = new Position();//初始点
        initP.X = sidePoint.X - w.cup.KnifeDiameter / 2;
        initP.Y = sidePoint.Y - w.cup.KnifeDiameter;
        initP.Z = w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));

        Position endCutPoint = new Position();//车削停止点
        while (sidePoint.Z > targetDeepZ)
        {
            int isGoBack = i % 2 == 0 ? -1 : 1;
            //判断剩余车削量是否满足一个车削量
            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z);
            sidePoint.Z -= w.cup.CutDeep;

            //1.安全位置1;2.首次深度点;3.开始车削点;4.车削停止点;

            Position safePoint1 = new Position();//安全位置
            safePoint1.X = initP.X;
            safePoint1.Y = endCutPoint.isZero() ? initP.Y : endCutPoint.Y;
            safePoint1.Z = endCutPoint.isZero() ? Z : sidePoint.Z;
            pointList.add(NCParameterHandle.outPset(safePoint1, w.pathAB, true));

            Position initDeepPoint = safePoint1;//首次深度点
            if (i == 1)
            {
                initDeepPoint.Z = sidePoint.Z;
                pointList.add(NCParameterHandle.outPset(initDeepPoint, w.pathAB, true));
            }

            Position startPoint = new Position();//开始车削点
            startPoint.X = initDeepPoint.X;
            startPoint.Y = initDeepPoint.Y + isGoBack * w.cup.KnifeDiameter;
            startPoint.Z = initDeepPoint.Z;
            pointList.add(NCParameterHandle.outGoto(startPoint, w.pathAB, true));

            endCutPoint = new Position();
            endCutPoint.X = startPoint.X;
            endCutPoint.Y = startPoint.Y + isGoBack * (w.Thickness + w.cup.KnifeDiameter);
            endCutPoint.Z = startPoint.Z;
            pointList.add(NCParameterHandle.outGoto(endCutPoint, w.pathAB, true));//车削停止点

            i++;
        }
        Position lastPoint = endCutPoint;
        lastPoint.Z =  w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(lastPoint, w.pathAB, true));
    }
    //endregion

    //region 锯片拉槽,逆时针,槽宽大于锯片厚度ok
    /// </summary>
    /// <param name="sidePoint"></param>
    /// <param name="grooveDeep">槽深</param>
    /// <param name="grooveWidth">槽宽</param>
    /// <param name="grooveLength">槽长</param>
    /// <param name="cutParam"></param>
    public static void getAntiSawBigGroovePathData(ArrayList pointList,Position sidePoint,WasteAndroid w)
    {
        double perval = w.cup.KnifeThick;
        if (w.cup.CutPercent != 0.0D)
            perval = (w.cup.KnifeThick * w.cup.CutPercent);//进刀量
        double stepWidth = 0;//宽度方向上每一次的进刀量
        double constLength = 5;
        Position safePoint2 = new Position();//结束点

        int i = 1;

        Position initPoint = new Position();//初始点
        initPoint.X = sidePoint.X - (w.CLkd + w.cup.KnifeDiameter / 2) + w.Deep;
        initPoint.Y = sidePoint.Y - (w.cup.KnifeDiameter / 2 + constLength);
        initPoint.Z = sidePoint.Z + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initPoint, w.pathAB, true));

        while (w.Width > Math.abs(safePoint2.Z))
        {
            int isGoBack = i % 2 == 0 ? -1 : 1;
            //1.设定深度点; 2.开始车削点; 3.去程结束点; 4.去程结束安全点;

            Position setDeepPoint = new Position();//设定深度点
            setDeepPoint.X = initPoint.X;
            setDeepPoint.Y = i == 1 ? initPoint.Y : safePoint2.Y;
            setDeepPoint.Z = i == 1 ? sidePoint.Z - w.cup.KnifeThick : safePoint2.Z - stepWidth;
            pointList.add(NCParameterHandle.outPset(setDeepPoint, w.pathAB, true));

            Position startCutPoint = new Position();//开始切削点
            startCutPoint.X = setDeepPoint.X; startCutPoint.Y = setDeepPoint.Y + isGoBack * constLength; startCutPoint.Z = setDeepPoint.Z;
            pointList.add(NCParameterHandle.outGoto(startCutPoint, w.pathAB, true));

            Position midEndPoint = new Position();//去程结束点
            midEndPoint.X = startCutPoint.X; midEndPoint.Y = startCutPoint.Y + isGoBack * (w.Thickness + w.cup.KnifeDiameter); midEndPoint.Z = startCutPoint.Z;
            pointList.add(NCParameterHandle.outGoto(midEndPoint, w.pathAB, true));

            safePoint2.X = midEndPoint.X; safePoint2.Y = midEndPoint.Y + isGoBack * constLength;
            safePoint2.Z = midEndPoint.Z;//去程结束安全点
            pointList.add(NCParameterHandle.outPset(safePoint2, w.pathAB, true));

            //判断是否满足一个车削量
            stepWidth = w.Width - Math.abs(safePoint2.Z) > perval ? perval : w.Width - Math.abs(safePoint2.Z);

            i++;
        }

        Position outPoint = new Position();//退出点
        outPoint.X = safePoint2.X - w.cup.KnifeDiameter; outPoint.Y = safePoint2.Y; outPoint.Z = safePoint2.Z;
        pointList.add(NCParameterHandle.outPset(outPoint, w.pathAB, true));

        Position liftPoint = new Position();//提升点
        liftPoint.X = outPoint.X; liftPoint.Y = outPoint.Y; liftPoint.Z = outPoint.Z + w.cup.KnifeDiameter;
        pointList.add(NCParameterHandle.outPset(liftPoint, w.pathAB, true));

    }
    //endregion

    //region 锯片逆时针拉槽,锯片厚度与槽宽相等时ok
    /// </summary>
    /// <param name="sidePoint"></param>
    /// <param name="grooveDeep">槽深</param>
    /// <param name="grooveWidth">槽宽</param>
    /// <param name="grooveLength">槽长</param>
    /// <param name="cutParam"></param>
    public static void getAntiSawGroovepathData(ArrayList pointList,Position sidePoint,WasteAndroid w)
    {
        double constLength = 5;
        //1.安全位置1; 2.初始点; 3.设定深度点; 4.开始车削点; 5.车削结束点; 6.安全点; 7.提升点;
        Position initPoint = new Position();//初始点
        initPoint.X = sidePoint.X - (w.CLkd + w.cup.KnifeDiameter / 2) + w.Deep;
        initPoint.Y = sidePoint.Y - (w.cup.KnifeDiameter / 2 + constLength);
        initPoint.Z = sidePoint.Z + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initPoint, w.pathAB, true));

        Position setDeepPoint = new Position();//设定深度点
        setDeepPoint.X = initPoint.X; setDeepPoint.Y = initPoint.Y; setDeepPoint.Z = initPoint.Z - w.Deep - w.cup.KnifeThick;
        pointList.add(NCParameterHandle.outPset(initPoint, w.pathAB, true));

        Position startCutPoint = new Position();//开始车削点
        startCutPoint.X = setDeepPoint.X; startCutPoint.Y = setDeepPoint.Y + constLength; startCutPoint.Z = setDeepPoint.Z;
        pointList.add(NCParameterHandle.outGoto(startCutPoint, w.pathAB, true));

        Position endCutPoint = new Position();//车削结束点
        endCutPoint.X = startCutPoint.X; endCutPoint.Y = startCutPoint.Y + (w.Thickness + w.cup.KnifeDiameter); endCutPoint.Z = startCutPoint.Z;
        pointList.add(NCParameterHandle.outGoto(endCutPoint, w.pathAB, true));

        Position safePoint2 = new Position();//安全位置2
        safePoint2.X = endCutPoint.X; safePoint2.Y = endCutPoint.Y + constLength; safePoint2.Z = endCutPoint.Z;
        pointList.add(NCParameterHandle.outPset(safePoint2, w.pathAB, true));

        Position outPoint = new Position();//退出点
        outPoint.X = safePoint2.X - w.Deep; outPoint.Y = safePoint2.Y; outPoint.Z = safePoint2.Z;
        pointList.add(NCParameterHandle.outPset(outPoint, w.pathAB, true));

        Position liftPoint = new Position();//提升点
        liftPoint.X = outPoint.X; liftPoint.Y = outPoint.Y; liftPoint.Z = outPoint.Z + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(liftPoint, w.pathAB, true));
    }
//endregion

    //region 半开口方形闭合榫槽逆时针,宽度大于刀具直径ok
    /// </summary>
    /// <param name="sidePoint">榫槽左侧边缘点为基准点</param>
    /// <param name="grooveDeep">榫槽深度</param>
    /// <param name="grooveWidth">榫槽宽度</param>
    /// <param name="grooveLength">榫槽长度</param>
    /// <param name="cutParameter"></param>
    /// <returns></returns>
    public static void getAntiClockHalfOpenGroovePathData(ArrayList pointList, Position sidePoint,WasteAndroid w)
    {

        double perval = (w.cup.KnifeDiameter / 2);//每次进刀量
        if (w.cup.CutPercent != 0.0)
            perval = w.cup.CutPercent * w.cup.KnifeDiameter;
        double halfGrooveWidth = (w.Width - w.cup.KnifeDiameter) / 2;//槽宽中心到一侧边缘的距离
        double Z = sidePoint.Z;//Z轴的起始坐标值
        double grooveWidthCenterX = sidePoint.X - w.Width / 2;//槽宽中心X坐标值
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标值
        double currentWidth = 0.0D;//当前的宽度车削量
        double stepWidth = perval;//步宽,默认车削量

        //从一侧加工方案
        Position initP = new Position();
        initP.X = -w.cup.KnifeDiameter/2;
        initP.Y = sidePoint.Y - w.cup.KnifeDiameter;
        initP.Z = w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));

        Position safePoint2 = new Position();//
        while (sidePoint.Z > targetDeepZ)
        {
                //region 从一侧加工方案
            //判断是否满足一次的车削深度
            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z);
            sidePoint.Z -= w.cup.CutDeep;
            currentWidth = 0;//当前的宽度复位
            stepWidth = perval;
            safePoint2 = new Position();
            do{
                //1.确定安全位置1;2.设定深度点;3.进刀点;4.去程结束点;5.去程结束点;6.回程起点;7.回程结束点;8.安全位置2
                if (!safePoint2.isZero())
                {
                    //判断是否满足一次车削的量
                    stepWidth = (halfGrooveWidth - w.cup.KnifeDiameter / 2) - currentWidth  > stepWidth ? stepWidth :
                            (halfGrooveWidth - w.cup.KnifeDiameter / 2) - currentWidth;
                    currentWidth += stepWidth;
                }

                Position safePoint1 = new Position();//安全位置
                safePoint1.X = initP.X - currentWidth;
                safePoint1.Y = initP.Y;
                safePoint1.Z = safePoint2.isZero() ? Z : safePoint2.Z;
                pointList.add(NCParameterHandle.outPset(safePoint1, w.pathAB, true));

                Position deepPoint = new Position();//设定深度点
                deepPoint.X = safePoint1.X; deepPoint.Y = safePoint1.Y; deepPoint.Z = sidePoint.Z;
                pointList.add(NCParameterHandle.outPset(deepPoint, w.pathAB, true));

                Position startCutPoint = new Position();//进刀点
                startCutPoint.X = deepPoint.X; startCutPoint.Y = deepPoint.Y + w.cup.KnifeDiameter;
                startCutPoint.Z = deepPoint.Z;
                pointList.add(NCParameterHandle.outGoto(startCutPoint, w.pathAB, true));

                Position middTurnPoint = new Position();//去程结束点
                middTurnPoint.X = startCutPoint.X; middTurnPoint.Y = startCutPoint.Y + (w.Thickness - w.cup.KnifeDiameter / 2);
                middTurnPoint.Z = startCutPoint.Z;
                pointList.add(NCParameterHandle.outGoto(middTurnPoint, w.pathAB, true));

                Position middbackPoint = new Position();//回程起点
                middbackPoint.X = (-w.Width+w.cup.KnifeDiameter/2) + currentWidth;
                middbackPoint.Y = middTurnPoint.Y;
                middbackPoint.Z = middTurnPoint.Z;
                pointList.add(NCParameterHandle.outGoto(middbackPoint, w.pathAB, true));

                Position backEndpoint = new Position();//回程结束点
                backEndpoint.X = middbackPoint.X; backEndpoint.Y = middbackPoint.Y - (w.Thickness - w.cup.KnifeDiameter / 2);
                backEndpoint.Z = middbackPoint.Z;
                pointList.add(NCParameterHandle.outGoto(backEndpoint, w.pathAB, true));

                safePoint2.X = backEndpoint.X; safePoint2.Y = backEndpoint.Y - w.cup.KnifeDiameter;
                safePoint2.Z = backEndpoint.Z;//安全位置2
                pointList.add(NCParameterHandle.outPset(safePoint2, w.pathAB, true));
            }
            while (currentWidth+w.cup.KnifeDiameter / 2 < halfGrooveWidth);
                //endregion

                //region  从中间加工方案

            //判断是否满足一次的车削深度
            //cutParameter.CutDeep = Math.Abs(targetDeepZ - sidePoint.Z) > cutParameter.CutDeep ? cutParameter.CutDeep : Math.Abs(targetDeepZ - sidePoint.Z);
            //sidePoint.Z -= cutParameter.CutDeep;
            //currentWidth = 0.0D;//当前的宽度复位
            //stepWidth = perval;
            //safePoint2 = new Position();//
            //while (currentWidth < halfGrooveWidth)
            //{
            //1.确定安全位置1;2.设定深度点;3.进刀点;4.去程结束点;5.去程结束点;6.回程起点;7.回程结束点;8.安全位置2

            //if (!safePoint2.isZero()) {
            //判断是否满足一次车削的量
            //stepWidth = halfGrooveWidth - currentWidth > stepWidth ? stepWidth : halfGrooveWidth - currentWidth;
            //currentWidth += stepWidth;
            //}

            //    Position safePoint1 = new Position();//安全位置
            //    safePoint1.X = safePoint2.isZero() ? initP.X : grooveWidthCenterX + currentWidth;
            //    safePoint1.Y = initP.Y;
            //    safePoint1.Z = safePoint2.isZero() ? Z : safePoint2.Z;
            //    pointList.add(NCParameterHandle.outPset(safePoint1, pathAB, true));

            //    Position deepPoint = new Position();//设定深度点
            //    deepPoint.X = safePoint1.X; deepPoint.Y = safePoint1.Y; deepPoint.Z = sidePoint.Z;
            //    pointList.add(NCParameterHandle.outPset(deepPoint, pathAB, true));

            //    Position startCutPoint = new Position();//进刀点
            //    startCutPoint.X = deepPoint.X; startCutPoint.Y = deepPoint.Y + cutParameter.KnifeDiameter;
            //    startCutPoint.Z = deepPoint.Z;
            //    pointList.add(NCParameterHandle.outGoto(startCutPoint, pathAB, true));

            //    Position middTurnPoint = new Position();//去程结束点
            //    middTurnPoint.X = startCutPoint.X; middTurnPoint.Y = startCutPoint.Y + (grooveLength - cutParameter.KnifeDiameter / 2);
            //    middTurnPoint.Z = startCutPoint.Z;
            //    pointList.add(NCParameterHandle.outGoto(middTurnPoint, pathAB, true));

            //    Position middbackPoint = new Position();//回程起点
            //    middbackPoint.X = grooveWidthCenterX - currentWidth;
            //    middbackPoint.Y = middTurnPoint.Y;
            //    middbackPoint.Z = middTurnPoint.Z;
            //    pointList.add(NCParameterHandle.outGoto(middbackPoint, pathAB, true));

            //    Position backEndpoint = new Position();//回程结束点
            //    backEndpoint.X = middbackPoint.X; backEndpoint.Y = middbackPoint.Y - (grooveLength - cutParameter.KnifeDiameter / 2);
            //    backEndpoint.Z = middbackPoint.Z;
            //    pointList.add(NCParameterHandle.outGoto(backEndpoint, pathAB, true));

            //    safePoint2.X = backEndpoint.X; safePoint2.Y = backEndpoint.Y - cutParameter.KnifeDiameter;
            //    safePoint2.Z = backEndpoint.Z;//安全位置2
            //    pointList.add(NCParameterHandle.outPset(safePoint2, pathAB, true));
            //}
                //endregion
        }
        Position lastPoint = new Position();//复原
        lastPoint.X = safePoint2.X;
        lastPoint.Y = safePoint2.Y;
        lastPoint.Z = w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(lastPoint, w.pathAB, true));

//        stepWidth = halfGrooveWidth - currentWidth > stepWidth ? stepWidth : halfGrooveWidth - currentWidth;
//
//        Position initP = new Position();
//        initP.X = grooveWidthCenterX + stepWidth;
//        initP.Y = sidePoint.Y - w.cup.KnifeDiameter;
//        initP.Z = w.zSafeDist;
//        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));
//
//        Position safePoint2 = new Position();//
//        while (sidePoint.Z > targetDeepZ)
//        {
//            //判断是否满足一次的车削深度
//            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z);
//            sidePoint.Z -= w.cup.CutDeep;
//            currentWidth = 0.0D;//当前的宽度复位
//            stepWidth = perval;
//
//            while (currentWidth < halfGrooveWidth)
//            {
//                //1.确定安全位置1;2.设定深度点;3.进刀点;4.去程结束点;5.去程结束点;6.回程起点;7.回程结束点;8.安全位置2
//
//                //判断是否满足一次车削的量
//                stepWidth = halfGrooveWidth - currentWidth > stepWidth ? stepWidth : halfGrooveWidth - currentWidth;
//                currentWidth += stepWidth;
//
//                Position safePoint1 = new Position();//安全位置
//                safePoint1.X = safePoint2.isZero() ? initP.X : grooveWidthCenterX + currentWidth;
//                safePoint1.Y = initP.Y;
//                safePoint1.Z = safePoint2.isZero() ? Z : safePoint2.Z;
//                pointList.add(NCParameterHandle.outPset(safePoint1, w.pathAB, true));
//
//                Position deepPoint = new Position();//设定深度点
//                deepPoint.X = safePoint1.X; deepPoint.Y = safePoint1.Y; deepPoint.Z = sidePoint.Z;
//                pointList.add(NCParameterHandle.outPset(deepPoint, w.pathAB, true));
//
//                Position startCutPoint = new Position();//进刀点
//                startCutPoint.X = deepPoint.X; startCutPoint.Y = deepPoint.Y + w.cup.KnifeDiameter;
//                startCutPoint.Z = deepPoint.Z;
//                pointList.add(NCParameterHandle.outGoto(startCutPoint, w.pathAB, true));
//
//                Position middTurnPoint = new Position();//去程结束点
//                middTurnPoint.X = startCutPoint.X; middTurnPoint.Y = startCutPoint.Y + (w.Thickness - w.cup.KnifeDiameter / 2);
//                middTurnPoint.Z = startCutPoint.Z;
//                pointList.add(NCParameterHandle.outGoto(middTurnPoint, w.pathAB, true));
//
//                Position middbackPoint = new Position();//回程起点
//                middbackPoint.X = grooveWidthCenterX - currentWidth;
//                middbackPoint.Y = middTurnPoint.Y;
//                middbackPoint.Z = middTurnPoint.Z;
//                pointList.add(NCParameterHandle.outGoto(middbackPoint, w.pathAB, true));
//
//                Position backEndpoint = new Position();//回程结束点
//                backEndpoint.X = middbackPoint.X; backEndpoint.Y = middbackPoint.Y - (w.Thickness - w.cup.KnifeDiameter / 2);
//                backEndpoint.Z = middbackPoint.Z;
//                pointList.add(NCParameterHandle.outGoto(backEndpoint, w.pathAB, true));
//
//                safePoint2 = new Position();
//                safePoint2.X = backEndpoint.X; safePoint2.Y = backEndpoint.Y - w.cup.KnifeDiameter;
//                safePoint2.Z = backEndpoint.Z;//安全位置2
//                pointList.add(NCParameterHandle.outPset(safePoint2, w.pathAB, true));
//            }
//        }
//        Position lastPoint = new Position();//复原
//        lastPoint.X = safePoint2.X;
//        lastPoint.Y = safePoint2.Y;
//        lastPoint.Z = w.Deep + w.zSafeDist;
//        pointList.add(NCParameterHandle.outPset(lastPoint, w.pathAB, true));
    }
    //endregion

    //region 半开口榫槽逆时针,宽度等于刀具直径混合铣,只适用于钻刀ok
    ///
    /// </summary>
    /// <param name="sidePoint">榫槽左侧边缘点为基准点</param>
    /// <param name="grooveDeep">榫槽深度</param>
    /// <param name="grooveLength">榫槽长度</param>
    /// <param name="cutParameter"></param>
    /// <returns></returns>
    public static void getAntiHalfOpenGrooveMixPathData(ArrayList pointList,Position sidePoint,WasteAndroid w)
    {
        double Z = sidePoint.Z;
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标值

        int i = 1;

        Position initP = new Position();
        initP.X = sidePoint.X - w.cup.KnifeDiameter / 2;
        initP.Y = sidePoint.Y - w.cup.KnifeDiameter;
        initP.Z = w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));
        Position endCutPoint = new Position();//车削终止点
        while (sidePoint.Z > targetDeepZ)
        {
            int isGoBack = i % 2 == 0 ? -1 : 1;

            //判断是否满足一次进刀量
            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z);
            sidePoint.Z -= w.cup.CutDeep;

            Position startCutPoint = new Position();//起始点
            startCutPoint.X = initP.X;
            startCutPoint.Y = isGoBack == 1 ? initP.Y : endCutPoint.Y;
            startCutPoint.Z = endCutPoint.isZero() ? Z : isGoBack == 1 ? endCutPoint.Z : sidePoint.Z;
            if (isGoBack == 1)
            {
                pointList.add(NCParameterHandle.outPset(startCutPoint, w.pathAB, true));
            }
            else { pointList.add(NCParameterHandle.outGoto(startCutPoint, w.pathAB, true)); }



            Position setDeepPoint = startCutPoint;//设定深度点
            Position beginCutPoint = setDeepPoint;//开始车削点

            if (isGoBack == 1)
            {
                setDeepPoint.Z = sidePoint.Z;
                pointList.add(NCParameterHandle.outPset(setDeepPoint, w.pathAB, true));

                beginCutPoint.X = setDeepPoint.X;
                beginCutPoint.Y = setDeepPoint.Y + w.cup.KnifeDiameter;
                beginCutPoint.Z = sidePoint.Z;
                pointList.add(NCParameterHandle.outGoto(beginCutPoint, w.pathAB, true));
            }
            double tempLength = isGoBack == 1 ? -w.cup.KnifeDiameter / 2 : w.cup.KnifeDiameter;
            endCutPoint = new Position();
            endCutPoint.X = beginCutPoint.X;
            endCutPoint.Y = beginCutPoint.Y + isGoBack * (w.Thickness + tempLength);
            endCutPoint.Z = beginCutPoint.Z;//结束点
            pointList.add(NCParameterHandle.outGoto(endCutPoint, w.pathAB, true));
            i++;
        }
        Position lastPoint = new Position();
        lastPoint.X = endCutPoint.X;
        lastPoint.Y = endCutPoint.Y;
        lastPoint.Z = w.Deep + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(lastPoint, w.pathAB, true));
    }
    //endregion

    //region 半圆开口榫槽逆时针,宽度大于刀具直径ok
    /// </summary>
    /// <param name="sidePoint">榫槽左侧边缘点为基准点</param>
    /// <param name="grooveDeep">榫槽深度</param>
    /// <param name="grooveWidth">榫槽宽度</param>
    /// <param name="grooveLength">榫槽长度</param>
    /// <param name="cutParameter"></param>
    /// <returns></returns>
    public static void getAntiClockHalfOpenGroovePathDataWithCircle(ArrayList pointList,Position sidePoint, WasteAndroid w)
    {
        double perval = (w.cup.KnifeDiameter / 2);//每次进刀量
        if (w.cup.CutPercent != 0.0)
            perval = w.cup.CutPercent * w.cup.KnifeDiameter;
        double halfGrooveWidth = (w.Width - w.cup.KnifeDiameter) / 2;//槽宽中心到一侧边缘的距离
        double Z = sidePoint.Z;//Z轴的起始坐标值
        double grooveWidthCenterX = sidePoint.X - w.Width / 2;//槽宽中心X坐标值
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标值
        double currentWidth = 0.0D;//当前的宽度车削量
        double stepWidth = perval;//步宽,默认车削量

        Position liftPoint = new Position();//提升点

        stepWidth = halfGrooveWidth - currentWidth > stepWidth ? stepWidth : halfGrooveWidth - currentWidth;
        Position initP = new Position();
        initP.X = grooveWidthCenterX + stepWidth;
        initP.Y = sidePoint.Y - w.cup.KnifeDiameter;
        initP.Z = w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));

        Position safePoint2 = new Position();//安全位置2
        while (sidePoint.Z > targetDeepZ)
        {
            //判断是否满足一次的车削深度
            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z);
            sidePoint.Z -= w.cup.CutDeep;
            currentWidth = 0.0D;//当前的宽度复位
            stepWidth = perval;

            while (halfGrooveWidth > currentWidth)
            {
                //1.确定安全位置1;2.设定深度点;3.进刀点;4.去程结束点;5.画圆弧;6.返程起始点;7.返程结束点;8.安全位置2;

                //判断是否满足一次车削的量
                stepWidth = halfGrooveWidth - currentWidth > stepWidth ? stepWidth : halfGrooveWidth - currentWidth;

                currentWidth += stepWidth;
                Position safePoint1 = new Position();//安全位置1
                safePoint1.X = safePoint2.isZero() ? initP.X : grooveWidthCenterX + currentWidth;
                safePoint1.Y = initP.Y;
                safePoint1.Z = liftPoint.equals(null) ? Z : liftPoint.Z;
                pointList.add(NCParameterHandle.outPset(safePoint1, w.pathAB, true));

                Position deepPoint = new Position();//设定深度点
                deepPoint.X = safePoint1.X; deepPoint.Y = safePoint1.Y; deepPoint.Z = sidePoint.Z;
                pointList.add(NCParameterHandle.outPset(deepPoint, w.pathAB, true));

                Position startCutPoint = new Position();//进刀点
                startCutPoint.X = deepPoint.X; startCutPoint.Y = deepPoint.Y + w.cup.KnifeDiameter;
                startCutPoint.Z = deepPoint.Z;
                pointList.add(NCParameterHandle.outGoto(startCutPoint, w.pathAB, true));

                Position middTurnPoint = new Position();//去程结束点
                middTurnPoint.X = startCutPoint.X; middTurnPoint.Y = startCutPoint.Y + (w.Thickness - w.Width / 2);
                middTurnPoint.Z = startCutPoint.Z;
                pointList.add(NCParameterHandle.outGoto(middTurnPoint, w.pathAB, true));

                Position middbackPoint = new Position();//回程起点
                middbackPoint.X = grooveWidthCenterX - currentWidth; middbackPoint.Y = middTurnPoint.Y; middbackPoint.Z = middTurnPoint.Z;

                Position center = new Position();
                center.X = grooveWidthCenterX; center.Y = middTurnPoint.Y; center.Z = middTurnPoint.Z;
                double radius = currentWidth;
                getCurveAntiClockPathDataXY(pointList, w.pathAB, middTurnPoint, middbackPoint, center, radius, 0);//画圆
                pointList.add(NCParameterHandle.outGoto(middbackPoint, w.pathAB, true));

                Position backEndpoint = new Position();//回程结束点
                backEndpoint.X = middbackPoint.X; backEndpoint.Y = middbackPoint.Y - (w.Thickness - w.Width / 2);
                backEndpoint.Z = middbackPoint.Z;
                pointList.add(NCParameterHandle.outGoto(backEndpoint, w.pathAB, true));

                safePoint2 = new Position();
                safePoint2.X = backEndpoint.X; safePoint2.Y = backEndpoint.Y - w.cup.KnifeDiameter; safePoint2.Z = backEndpoint.Z;
                pointList.add(NCParameterHandle.outPset(safePoint2, w.pathAB, true));//安全位置2
            }
        }
        Position lastPoint = new Position();//复原
        lastPoint.X = safePoint2.X;
        lastPoint.Y = safePoint2.Y;
        lastPoint.Z = w.Deep + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(lastPoint, w.pathAB, true));

    }
    //endregion

    //region 全开口榫槽顺时针,宽度大于刀具直径ok
    /// </summary>
    /// <param name="sidePoint">榫槽左侧边缘点为基准点</param>
    /// <param name="grooveDeep">榫槽深度</param>
    /// <param name="grooveWidth">榫槽宽度</param>
    /// <param name="grooveLength">榫槽长度</param>
    /// <param name="cutParameter"></param>
    /// <returns></returns>
    public static void getClockGroovePathData(ArrayList pointList,Position sidePoint, WasteAndroid w)
    {
        double perval = (w.cup.KnifeDiameter / 2);
        if (w.cup.CutPercent != 0.0D)
            perval = (w.cup.KnifeDiameter * w.cup.CutPercent);//进刀量
        double halfWidth = (w.Width - w.cup.KnifeDiameter) / 2;//一半宽度
        double startZ = sidePoint.Z;//Z轴起点坐标值
        double centerWidthX = sidePoint.X - w.Width / 2;//宽度中心点的X坐标值
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标值
        double currentWidth = 0.0D;//当前车削的宽度值
        double stepWidth = perval;//宽度方向上每一次的进刀量

        //stepWidth = halfWidth - currentWidth > perval ? perval : halfWidth - currentWidth;

        Position initP = new Position();
        initP.X = centerWidthX ;
        initP.Y = sidePoint.Y - w.cup.KnifeDiameter;
        initP.Z = w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));

        Position safePoint2 = new Position();//安全点2

        while (targetDeepZ < sidePoint.Z)
        {
            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z);
            sidePoint.Z -= w.cup.CutDeep;
            currentWidth = 0.0D; stepWidth = perval;
            safePoint2 = new Position();//安全点2
            while (currentWidth < halfWidth)
            {
                //1.安全点1;2.设定深度点;3进刀点;4.去程终止点;5.返程起点;6.返程终止点;7.安全点2;8.安全点1
                if (!safePoint2.isZero()) {
                    //判断 槽宽当前的值
                    stepWidth = halfWidth - currentWidth > perval ? perval : halfWidth - currentWidth;
                    currentWidth += stepWidth;
                }

                Position safePoint1 = new Position();//安全点1
                safePoint1.X = safePoint2.isZero() ? initP.X : centerWidthX - currentWidth;
                safePoint1.Y = initP.Y;
                safePoint1.Z = safePoint2.isZero() ? startZ : safePoint2.Z;
                pointList.add(NCParameterHandle.outPset(safePoint1, w.pathAB, true));

                Position deepPoint = new Position();//设定深度点
                deepPoint.X = safePoint1.X; deepPoint.Y = safePoint1.Y; deepPoint.Z = sidePoint.Z;
                pointList.add(NCParameterHandle.outPset(deepPoint, w.pathAB, true));

                Position startCutPoint = new Position();//进刀点
                startCutPoint.X = deepPoint.X; startCutPoint.Y = deepPoint.Y + w.cup.KnifeDiameter;
                startCutPoint.Z = deepPoint.Z;
                pointList.add(NCParameterHandle.outGoto(startCutPoint, w.pathAB, true));

                Position endCutPoint = new Position();//去程终止点
                endCutPoint.X = startCutPoint.X; endCutPoint.Y = startCutPoint.Y + w.Thickness;
                endCutPoint.Z = startCutPoint.Z;
                pointList.add(NCParameterHandle.outGoto(endCutPoint, w.pathAB, true));

                Position middTurnPoint = new Position();//返程起点
                middTurnPoint.X = safePoint2.isZero() ? endCutPoint.X : (centerWidthX + currentWidth); middTurnPoint.Y = endCutPoint.Y;
                middTurnPoint.Z = endCutPoint.Z;
                pointList.add(NCParameterHandle.outGoto(middTurnPoint, w.pathAB, true));

                Position turnEndPoint = new Position();//返程终止点
                turnEndPoint.X = middTurnPoint.X; turnEndPoint.Y = middTurnPoint.Y - w.Thickness; turnEndPoint.Z = middTurnPoint.Z;
                pointList.add(NCParameterHandle.outGoto(turnEndPoint, w.pathAB, true));

                safePoint2.X = turnEndPoint.X; safePoint2.Y = turnEndPoint.Y - w.cup.KnifeDiameter;
                safePoint2.Z = turnEndPoint.Z;
                pointList.add(NCParameterHandle.outPset(safePoint2, w.pathAB, true)); ;

            }
        }
        Position resetPoint = new Position();
        resetPoint.X = safePoint2.X;
        resetPoint.Y = safePoint2.Y;
        resetPoint.Z = w.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(resetPoint, w.pathAB, true));
    }
    //endregion

    //region 全开口榫槽顺时针,宽度等于刀具直径混合铣ok
    /// </summary>
    /// <param name="sidePoint">榫槽左侧边缘点为基准点</param>
    /// <param name="grooveDeep">榫槽深度</param>
    /// <param name="grooveLength">榫槽长度</param>
    /// <param name="cutParameter"></param>
    /// <returns></returns>
    public static void getOpenGrooveMixPathData(ArrayList pointList,Position sidePoint,WasteAndroid w)
    {
        double perval = w.cup.KnifeDiameter / 2;//每次进刀量
        if (w.cup.CutPercent != 0.0)
            perval = w.cup.CutPercent * w.cup.KnifeDiameter;
        double Z = sidePoint.Z;
        double targetDeepZ = Z - w.Deep;//目标深度Z轴坐标
        int i = 1;

        sidePoint.Y += w.Thickness + w.cup.KnifeDiameter;

        Position initP = new Position();//初始点
        initP.X = sidePoint.X - w.cup.KnifeDiameter / 2;
        initP.Y = sidePoint.Y;
        initP.Z = w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));

        Position endCutPoint = new Position();//车削停止点
        while (sidePoint.Z > targetDeepZ)
        {
            int isGoBack = i % 2 == 0 ? -1 : 1;
            //判断剩余车削量是否满足一个车削量
            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z);
            sidePoint.Z -= w.cup.CutDeep;

            //1.安全位置1;2.;3.车削深度点;4.车削开始点;5.车削停止点;
            Position safePoint1 = new Position();//安全点1
            safePoint1.X = initP.X;
            safePoint1.Y = endCutPoint.isZero() ? initP.Y : endCutPoint.Y;
            safePoint1.Z = endCutPoint.isZero() ? Z : sidePoint.Z;
            pointList.add(NCParameterHandle.outPset(safePoint1, w.pathAB, true));

            Position initDeepPoint = safePoint1;//首次深度点
            if (i == 1)
            {
                initDeepPoint.Z = sidePoint.Z;
                pointList.add(NCParameterHandle.outPset(initDeepPoint, w.pathAB, true));
            }

            Position startPoint = new Position();//开始车削点
            startPoint.X = initDeepPoint.X;
            startPoint.Y = initDeepPoint.Y - isGoBack * w.cup.KnifeDiameter;
            startPoint.Z = initDeepPoint.Z;
            pointList.add(NCParameterHandle.outGoto(startPoint, w.pathAB, true));

            endCutPoint = new Position();
            endCutPoint.X = startPoint.X;
            endCutPoint.Y = startPoint.Y - isGoBack * (w.Thickness + w.cup.KnifeDiameter);
            endCutPoint.Z = startPoint.Z;
            pointList.add(NCParameterHandle.outGoto(endCutPoint, w.pathAB, true));//车削停止点

            i++;
        }
        Position lastPoint = endCutPoint;
        lastPoint.Z = 2 * w.Deep + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(lastPoint, w.pathAB, true));
    }
    //endregion

    //region锯片拉槽,顺时针,槽宽大于锯片厚度ok
    /// </summary>
    /// <param name="sidePoint"></param>
    /// <param name="grooveDeep">槽深</param>
    /// <param name="grooveWidth">槽宽</param>
    /// <param name="grooveLength">槽长</param>
    /// <param name="cutParam"></param>
    public static void getSawBigGroovePathData(ArrayList pointList, Position sidePoint, WasteAndroid w)
    {
        double perval = w.cup.KnifeThick;
        if (w.cup.CutPercent != 0.0D)
            perval = (w.cup.KnifeThick * w.cup.CutPercent);//进刀量
        double stepWidth = 0;//宽度方向上每一次的进刀量
        double constLength = 5;
        Position safePoint2 = new Position();//结束点
        sidePoint.Y += w.Thickness;
        int i = 1;

        Position initPoint = new Position();//初始点
        initPoint.X = sidePoint.X - (w.CLkd + w.cup.KnifeDiameter / 2) + w.Deep;
        initPoint.Y = sidePoint.Y + (w.cup.KnifeDiameter / 2 + constLength);
        initPoint.Z = sidePoint.Z + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initPoint, w.pathAB, true));

        while (w.Width > Math.abs(safePoint2.Z))
        {
            int isGoBack = i % 2 == 0 ? -1 : 1;
            //1.设定深度点; 2.开始车削点; 3.去程结束点; 4.去程结束安全点;

            Position setDeepPoint = new Position();//设定深度点
            setDeepPoint.X = initPoint.X;
            setDeepPoint.Y = i == 1 ? initPoint.Y : safePoint2.Y;
            setDeepPoint.Z = i == 1 ? sidePoint.Z - w.cup.KnifeThick : safePoint2.Z - stepWidth;
            pointList.add(NCParameterHandle.outPset(setDeepPoint, w.pathAB, true));

            Position startCutPoint = new Position();//开始切削点
            startCutPoint.X = setDeepPoint.X; startCutPoint.Y = setDeepPoint.Y - isGoBack * constLength; startCutPoint.Z = setDeepPoint.Z;
            pointList.add(NCParameterHandle.outGoto(startCutPoint, w.pathAB, true));

            Position midEndPoint = new Position();//去程结束点
            midEndPoint.X = startCutPoint.X; midEndPoint.Y = startCutPoint.Y - isGoBack * (w.Thickness + w.cup.KnifeDiameter); midEndPoint.Z = startCutPoint.Z;
            pointList.add(NCParameterHandle.outGoto(midEndPoint, w.pathAB, true));

            safePoint2.X = midEndPoint.X; safePoint2.Y = midEndPoint.Y - isGoBack * constLength;
            safePoint2.Z = midEndPoint.Z;//去程结束安全点
            pointList.add(NCParameterHandle.outPset(safePoint2, w.pathAB, true));

            //判断是否满足一个车削量
            stepWidth = w.Width - Math.abs(safePoint2.Z) > perval ? perval : w.Width - Math.abs(safePoint2.Z);

            i++;
        }

        Position outPoint = new Position();//退出点
        outPoint.X = safePoint2.X - w.cup.KnifeDiameter; outPoint.Y = safePoint2.Y; outPoint.Z = safePoint2.Z;
        pointList.add(NCParameterHandle.outPset(outPoint, w.pathAB, true));

        Position liftPoint = new Position();//提升点
        liftPoint.X = outPoint.X; liftPoint.Y = outPoint.Y; liftPoint.Z = outPoint.Z + w.cup.KnifeDiameter;
        pointList.add(NCParameterHandle.outPset(liftPoint, w.pathAB, true));

    }
//endregion

    //region锯片顺时针拉槽,锯片厚度与槽宽相等时ok
    /// </summary>
    /// <param name="sidePoint"></param>
    /// <param name="grooveDeep">槽深</param>
    /// <param name="grooveWidth">槽宽</param>
    /// <param name="grooveLength">槽长</param>
    /// <param name="cutParam"></param>
    public static void getSawGroovePathData(ArrayList pointList, Position sidePoint, WasteAndroid w)
    {
        double constLength = 5;
        //1.安全位置1; 2.初始点; 3.设定深度点; 4.开始车削点; 5.车削结束点; 6.安全点; 7.提升点;
        Position initPoint = new Position();//初始点
        initPoint.X = sidePoint.X - (w.CLkd + w.cup.KnifeDiameter / 2) + w.Deep;
        initPoint.Y = sidePoint.Y + w.Thickness + (w.cup.KnifeDiameter / 2 + constLength);
        initPoint.Z = sidePoint.Z + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initPoint, w.pathAB, true));

        Position setDeepPoint = new Position();//设定深度点
        setDeepPoint.X = initPoint.X; setDeepPoint.Y = initPoint.Y; setDeepPoint.Z = initPoint.Z - w.Deep - w.cup.KnifeThick;
        pointList.add(NCParameterHandle.outPset(initPoint, w.pathAB, true));

        Position startCutPoint = new Position();//开始车削点
        startCutPoint.X = setDeepPoint.X; startCutPoint.Y = setDeepPoint.Y - constLength; startCutPoint.Z = setDeepPoint.Z;
        pointList.add(NCParameterHandle.outGoto(startCutPoint, w.pathAB, true));

        Position endCutPoint = new Position();//车削结束点
        endCutPoint.X = startCutPoint.X; endCutPoint.Y = startCutPoint.Y - (w.Thickness + w.cup.KnifeDiameter); endCutPoint.Z = startCutPoint.Z;
        pointList.add(NCParameterHandle.outGoto(endCutPoint, w.pathAB, true));

        Position safePoint2 = new Position();//安全位置2
        safePoint2.X = endCutPoint.X; safePoint2.Y = endCutPoint.Y - constLength; safePoint2.Z = endCutPoint.Z;
        pointList.add(NCParameterHandle.outPset(safePoint2, w.pathAB, true));

        Position outPoint = new Position();//退出点
        outPoint.X = safePoint2.X - w.Deep; outPoint.Y = safePoint2.Y; outPoint.Z = safePoint2.Z;
        pointList.add(NCParameterHandle.outPset(outPoint, w.pathAB, true));

        Position liftPoint = new Position();//提升点
        liftPoint.X = outPoint.X; liftPoint.Y = outPoint.Y; liftPoint.Z = outPoint.Z + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(liftPoint, w.pathAB, true));
    }
//endregion

    //region 半开口方形闭合榫槽顺时针,宽度大于刀具直径ok
    /// </summary>
    /// <param name="sidePoint">榫槽左侧边缘点为基准点</param>
    /// <param name="grooveDeep">榫槽深度</param>
    /// <param name="grooveWidth">榫槽宽度</param>
    /// <param name="grooveLength">榫槽长度</param>
    /// <param name="cutParameter"></param>
    /// <returns></returns>
    public static void getClockHalfOpenGroovePathData(ArrayList pointList, Position sidePoint,WasteAndroid w)
    {
        double perval = (w.cup.KnifeDiameter / 2);//每次进刀量
        if (w.cup.CutPercent != 0.0)
            perval = w.cup.CutPercent * w.cup.KnifeDiameter;

        double halfGrooveWidth = (w.Width - w.cup.KnifeDiameter) / 2;//槽宽中心到一侧边缘的距离
        double Z = sidePoint.Z;//Z轴的起始坐标值
        double grooveWidthCenterX = sidePoint.X - w.Width / 2;//槽宽中心X坐标值
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标值
        double currentWidth = 0.0D;//当前的宽度车削量
        double stepWidth = perval;//步宽,默认车削量

//        stepWidth = halfGrooveWidth - currentWidth > stepWidth ? stepWidth : halfGrooveWidth - currentWidth;
//        Position initP = new Position();
//        initP.X = grooveWidthCenterX - stepWidth;
//        initP.Y = sidePoint.Y - w.cup.KnifeDiameter;
//        initP.Z = w.zSafeDist;
//        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));

        //一侧加工方案
        Position initP = new Position();
        initP.X = -w.Width+w.cup.KnifeDiameter/2;
        initP.Y = sidePoint.Y - w.cup.KnifeDiameter;
        initP.Z = w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));

        Position safePoint2 = new Position();//安全位置2
        while (sidePoint.Z > targetDeepZ)
        {
            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z);
            sidePoint.Z -= w.cup.CutDeep;
            currentWidth = 0.0D;//当前的宽度复位
            stepWidth = perval;
            safePoint2 = new Position();//安全位置2

                //region 一侧加工方案
            do{
                //1.确定安全位置1;2.设定深度点;3.进刀点;5.去程结束点;6.回程起点;7.回程结束点;8.安全位置2
                if (!safePoint2.isZero())
                {
                    //判断是否满足一次车削的量
                    stepWidth = (halfGrooveWidth - w.cup.KnifeDiameter / 2) - currentWidth > stepWidth ? stepWidth :
                            (halfGrooveWidth - w.cup.KnifeDiameter / 2) - currentWidth;
                    currentWidth += stepWidth;
                }

                Position safePoint1 = new Position();//安全位置
                safePoint1.X = initP.X + currentWidth;
                safePoint1.Y = initP.Y;
                safePoint1.Z = safePoint2.isZero() ? Z : safePoint2.Z;
                pointList.add(NCParameterHandle.outPset(safePoint1, w.pathAB, true));

                Position deepPoint = new Position();//设定深度点
                deepPoint.X = safePoint1.X; deepPoint.Y = safePoint1.Y; deepPoint.Z = sidePoint.Z;
                pointList.add(NCParameterHandle.outPset(deepPoint, w.pathAB, true));

                Position startCutPoint = new Position();//进刀点
                startCutPoint.X = deepPoint.X; startCutPoint.Y = deepPoint.Y + w.cup.KnifeDiameter;
                startCutPoint.Z = deepPoint.Z;
                pointList.add(NCParameterHandle.outGoto(startCutPoint, w.pathAB, true));

                Position middTurnPoint = new Position();//去程结束点
                middTurnPoint.X = startCutPoint.X; middTurnPoint.Y = startCutPoint.Y + (w.Thickness - w.cup.KnifeDiameter / 2);
                middTurnPoint.Z = startCutPoint.Z;
                pointList.add(NCParameterHandle.outGoto(middTurnPoint, w.pathAB, true));

                Position middbackPoint = new Position();//回程起点
                middbackPoint.X = -w.cup.KnifeDiameter/2 - currentWidth;
                middbackPoint.Y = middTurnPoint.Y;
                middbackPoint.Z = middTurnPoint.Z;
                pointList.add(NCParameterHandle.outGoto(middbackPoint, w.pathAB, true));

                Position backEndpoint = new Position();//回程结束点
                backEndpoint.X = middbackPoint.X; backEndpoint.Y = middbackPoint.Y - (w.Thickness - w.cup.KnifeDiameter / 2);
                backEndpoint.Z = middbackPoint.Z;
                pointList.add(NCParameterHandle.outGoto(backEndpoint, w.pathAB, true));

                safePoint2.X = backEndpoint.X; safePoint2.Y = backEndpoint.Y - w.cup.KnifeDiameter;
                safePoint2.Z = backEndpoint.Z;//安全位置2
                pointList.add(NCParameterHandle.outPset(safePoint2, w.pathAB, true));
            }
            while (currentWidth + w.cup.KnifeDiameter / 2 < halfGrooveWidth);
        }
        Position lastPoint = new Position();//复原
        lastPoint.X = safePoint2.X;
        lastPoint.Y = safePoint2.Y;
        lastPoint.Z = w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(lastPoint, w.pathAB, true));
    }
    //endregion

    //region 半开口榫槽顺时针,宽度等于刀具直径混合铣ok
    ///
    /// </summary>
    /// <param name="sidePoint">榫槽左侧边缘点为基准点</param>
    /// <param name="grooveDeep">榫槽深度</param>
    /// <param name="grooveLength">榫槽长度</param>
    /// <param name="cutParameter"></param>
    /// <returns></returns>
    public static void getHalfOpenGrooveMixPathData(ArrayList pointList, Position sidePoint, WasteAndroid w)
    {
        double Z = sidePoint.Z;
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标值

        int i = 1;

        sidePoint.Y += (w.Thickness - w.cup.KnifeDiameter / 2);

        Position initP = new Position();
        initP.X = sidePoint.X - w.cup.KnifeDiameter / 2;
        initP.Y = sidePoint.Y;
        initP.Z = w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));
        Position endCutPoint = new Position();//车削终止点
        while (sidePoint.Z > targetDeepZ)
        {
            int isGoBack = i % 2 == 0 ? -1 : 1;

            //1.起始点;2终点
            //判断是否满足一次进刀量
            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z);
            sidePoint.Z -= w.cup.CutDeep;

            Position startCutPoint = new Position();//起始点
            startCutPoint.X = initP.X;
            startCutPoint.Y = isGoBack == 1 ? initP.Y : endCutPoint.Y;
            startCutPoint.Z = endCutPoint.isZero() ? Z : isGoBack == 1 ? sidePoint.Z : endCutPoint.Z;
            if (isGoBack == 1)
            {
                pointList.add(NCParameterHandle.outGoto(startCutPoint, w.pathAB, true));
            }
            else
            {
                pointList.add(NCParameterHandle.outPset(startCutPoint, w.pathAB, true));
            }


            Position setDeepPoint = startCutPoint;//设定深度点
            setDeepPoint.Z = sidePoint.Z;
            if (isGoBack == 1)
            {
                pointList.add(NCParameterHandle.outGoto(setDeepPoint, w.pathAB, true));
            }
            else
            {
                pointList.add(NCParameterHandle.outPset(setDeepPoint, w.pathAB, true));
            }


            Position backCutPoint = setDeepPoint;//回程车削点
            if (isGoBack == -1)
            {
                backCutPoint.X = setDeepPoint.X;
                backCutPoint.Y = setDeepPoint.Y + w.cup.KnifeDiameter;
                backCutPoint.Z = sidePoint.Z;
                pointList.add(NCParameterHandle.outGoto(backCutPoint, w.pathAB, true));
            }

            double tempLength = isGoBack == 1 ? w.cup.KnifeDiameter / 2 : -w.cup.KnifeDiameter / 2;
            endCutPoint = new Position();
            endCutPoint.X = backCutPoint.X;
            endCutPoint.Y = backCutPoint.Y - isGoBack * (w.Thickness + tempLength);
            endCutPoint.Z = backCutPoint.Z;//车削结束点
            pointList.add(NCParameterHandle.outGoto(endCutPoint, w.pathAB, true));

            i++;
        }
        Position lastPoint = new Position();
        lastPoint.X = endCutPoint.X;
        lastPoint.Y = endCutPoint.Y;
        lastPoint.Z = w.Deep + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(lastPoint, w.pathAB, true));
    }
    //endregion

    //region 半圆开口榫槽顺时针,宽度大于刀具直径
    /// </summary>
    /// <param name="sidePoint">榫槽左侧边缘点为基准点</param>
    /// <param name="grooveDeep">榫槽深度</param>
    /// <param name="grooveWidth">榫槽宽度</param>
    /// <param name="grooveLength">榫槽长度</param>
    /// <param name="cutParameter"></param>
    /// <returns></returns>
    public static void getClockHalfOpenGroovePathDataWithCircle(ArrayList pointList, Position sidePoint, WasteAndroid w)
    {
        double perval = (w.cup.KnifeDiameter / 2);//每次进刀量
        if (w.cup.CutPercent != 0.0)
            perval = w.cup.CutPercent * w.cup.KnifeDiameter;

        double halfGrooveWidth = (w.Width - w.cup.KnifeDiameter) / 2;//槽宽中心到一侧边缘的距离
        double Z = sidePoint.Z;//Z轴的起始坐标值
        double grooveWidthCenterX = sidePoint.X - w.Width / 2;//槽宽中心X坐标值
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标值
        double currentWidth = 0.0D;//当前的宽度车削量
        double stepWidth = perval;//步宽,默认车削量

        stepWidth = halfGrooveWidth - currentWidth > stepWidth ? stepWidth : halfGrooveWidth - currentWidth;
        Position initP = new Position();
        initP.X = grooveWidthCenterX - stepWidth;
        initP.Y = sidePoint.Y - w.cup.KnifeDiameter;
        initP.Z = w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));

        Position safePoint2 = new Position();//安全位置2

        while (sidePoint.Z > targetDeepZ)
        {
            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z);
            sidePoint.Z -= w.cup.CutDeep;
            currentWidth = 0.0D;//当前的宽度复位
            stepWidth = perval;

            while (halfGrooveWidth >= currentWidth)
            {
                //1.安全位置1;2.设定深度点;3.进刀点;4.去程结束点;5.画圆弧;6.回程起点;7.回程结束点;8.安全位置2;

                //判断是否满足一次车削的量
                stepWidth = halfGrooveWidth - currentWidth > stepWidth ? stepWidth : halfGrooveWidth - currentWidth;

                currentWidth += stepWidth;
                Position safePoint1 = new Position();//安全位置1
                safePoint1.X = safePoint2.isZero() ? initP.X : grooveWidthCenterX - currentWidth;
                safePoint1.Y = initP.Y;
                safePoint1.Z = safePoint2.isZero() ? Z : safePoint2.Z;
                pointList.add(NCParameterHandle.outPset(safePoint1,  w.pathAB, true));

                Position deepPoint = new Position();//设定深度点
                deepPoint.X = safePoint1.X; deepPoint.Y = safePoint1.Y; deepPoint.Z = sidePoint.Z;
                pointList.add(NCParameterHandle.outPset(deepPoint,  w.pathAB, true));

                Position startCutPoint = new Position();//进刀点
                startCutPoint.X = deepPoint.X; startCutPoint.Y = deepPoint.Y + w.cup.KnifeDiameter;
                startCutPoint.Z = deepPoint.Z;
                pointList.add(NCParameterHandle.outGoto(startCutPoint,  w.pathAB, true));

                Position middTurnPoint = new Position();//去程结束点
                middTurnPoint.X = startCutPoint.X;
                middTurnPoint.Y = startCutPoint.Y + (w.Thickness - w.Width / 2);
                middTurnPoint.Z = startCutPoint.Z;
                pointList.add(NCParameterHandle.outGoto(middTurnPoint,  w.pathAB, true));

                Position middbackPoint = new Position();//回程起点
                middbackPoint.X = grooveWidthCenterX + currentWidth; middbackPoint.Y = middTurnPoint.Y; middbackPoint.Z = middTurnPoint.Z;

                Position center = new Position();
                center.X = grooveWidthCenterX; center.Y = middTurnPoint.Y; center.Z = middTurnPoint.Z;
                double radius = currentWidth;
                getCurveClockPathDataXY(pointList,  w.pathAB, middTurnPoint, middbackPoint, center, radius, 0);//画圆
                pointList.add(NCParameterHandle.outGoto(middbackPoint,  w.pathAB, true));

                Position backEndpoint = new Position();//回程结束点
                backEndpoint.X = middbackPoint.X;
                backEndpoint.Y = middbackPoint.Y - (w.Thickness - w.Width / 2);
                backEndpoint.Z = middbackPoint.Z;
                pointList.add(NCParameterHandle.outGoto(backEndpoint,  w.pathAB, true));

                safePoint2 = new Position();
                safePoint2.X = backEndpoint.X; safePoint2.Y = backEndpoint.Y - w.cup.KnifeDiameter; safePoint2.Z = backEndpoint.Z;
                pointList.add(NCParameterHandle.outPset(safePoint2,  w.pathAB, true));//安全位置2
            }
        }

        Position lastPoint = new Position();//复原
        lastPoint.X = safePoint2.X;
        lastPoint.Y = safePoint2.Y;
        lastPoint.Z = w.Deep + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(lastPoint,  w.pathAB, true));
    }
    //endregion

}
