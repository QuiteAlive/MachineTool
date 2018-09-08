package cn.qzd.machinetool.GetNcFile;

import java.util.ArrayList;
import java.util.List;

import cn.qzd.machinetool.base.Char7Mode;
import cn.qzd.machinetool.base.CutParameter;
import cn.qzd.machinetool.base.LMode;
import cn.qzd.machinetool.base.MuYuanSunMode;
import cn.qzd.machinetool.base.PathABVal;
import cn.qzd.machinetool.base.Position;
import cn.qzd.machinetool.base.SunCaoMode;
import cn.qzd.machinetool.base.VMode;
import cn.qzd.machinetool.base.WasteAndroid;
import cn.qzd.machinetool.base.ZhiBianMuYuanMode;
import cn.qzd.machinetool.base.ZhiBianYuanMode;
import cn.qzd.machinetool.base.ZhiFangMode;
import cn.qzd.machinetool.base.ZhiFangMuMode;
import cn.qzd.machinetool.base.ZhiLineMode;
import cn.qzd.machinetool.base.ZhiUMode;
import cn.qzd.machinetool.base.ZhiYuanMode;

/**
 * Created by Administrator on 2018/7/3.
 */

public class SunMaoDaoLuMode {
    private static Position samePos = new Position();
    private static Position samePos2 = new Position();
    private static ArrayList pointList;
    private static MuYuanSunMode mymode;
    public static final String _GROOVE_A = "1";
    public static final String _GROOVE_H_R = "2";
    public static final String _GROOVE_H_C = "3";
    public static final double _CUTLESS = 2;
    private static Position samePos_fst = new Position();
    private static Position samePos_four = new Position();
    private static Position samePos_Sed = new Position();
    private static Position samePos_Three = new Position();

    private static Position samePosS_3 = new Position();
    private static Position samePosS_2 = new Position();
    private static Position samePosS_1 = new Position();
    private static Position samePosS2 = new Position();
    private static Position samePosS_4 = new Position();

    private static Position samePosS = new Position();

    //regionU型刀路1
    //region顺时针U型刀路
    public static ArrayList ZhiUPath_Clockwise( ArrayList pointList, PathABVal pathAB, ZhiUMode mode)
    {
        Position endPos = new Position();//刀具开始坐标
        double leftMargin = mode.leftMargion;//右边距
        double topMargin = mode.sThickness - mode.mtThickness;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.mtWidth;//左边距
        double radius = mode.knifeDiameter / 2;
        double mtLength = mode.mtLength;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = radius * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        boolean bAngle = true;

        Position ZsafeStartPos = new Position();//刀具开始坐标
        ZsafeStartPos.Z = mode.zSafeDist;
        ZsafeStartPos.X = radius;
        ZsafeStartPos.Y = radius - 5;
        pointList.add(NCParameterHandle.outPset(ZsafeStartPos, pathAB, bAngle));

        while (finishedDepth < mtLength)
        {
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mtLength - finishedDepth);
            }

            startPos.X = startPos.X - mode.sWidth - radius;
            startPos.Y = startPos.Y - radius - 5;

            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));
            while ((finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {
                //3左下点
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X + finishedLeft;
                prevPos.Y = startPos.Y;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //2左上点
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));


                //1右上点
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }

                prevPos.X = startPos.X + mode.sWidth + radius * 2 - finishedRight;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //右下点
                prevPos.Y = startPos.Y;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //安全距离
                prevPos.Y -= radius - 5;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                prevPos.X -= mode.sWidth + mode.knifeDiameter;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                endPos = prevPos;
            }

        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
    }

    //endregion
    //region逆时针U型刀路
    public static ArrayList ZhiUPath_AntiClockwise(ArrayList pointList, PathABVal pathAB, ZhiUMode mode)
    {
        Position endPos = new Position();//刀具开始坐标
        double leftMargin = mode.leftMargion;//右边距
        double topMargin = mode.sThickness - mode.mtThickness;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.mtWidth;//左边距
        double radius = mode.knifeDiameter / 2;
        double mtLength = mode.mtLength;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = radius * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        boolean bAngle = true;


        Position ZsafeStartPos = new Position();//刀具开始坐标
        ZsafeStartPos.Z = mode.zSafeDist;
        ZsafeStartPos.X = radius;
        ZsafeStartPos.Y = radius - 5;
        pointList.add(NCParameterHandle.outPset(ZsafeStartPos, pathAB, bAngle));
        while (finishedDepth < mtLength)
        {
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mtLength - finishedDepth);
            }

            startPos.X = startPos.X + radius;
            startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));
            while ((finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {
                //1右边
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }
                prevPos.X = startPos.X - finishedRight;
                prevPos.Y = startPos.Y;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //2上边
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //3左边
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //底边
                prevPos.Y = startPos.Y;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //安全距离
                prevPos.Y -= radius - 5;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                prevPos.X += mode.sWidth + mode.knifeDiameter;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                endPos = prevPos;
            }
        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
    }
    //endregion
    //endregion

    //region边线加工刀路1
    //region顺时针边线加工刀路
    public static ArrayList ZhiLinePath_Clockwise(ArrayList pointList, PathABVal pathAB, ZhiLineMode mode)
    {
        Position endPos = new Position();//刀具开始坐标
        double cutDepth = mode.cutDepth;//每刀深度z
        double finishedWidth = 0;
        double finishedDepth = 0;//已完成长度(z轴)
        double cutWidth = mode.knifeDiameter / 2 * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        boolean bAngle = true;//20180615
        double radius = mode.knifeDiameter / 2;
        boolean zIsEnd = false;
        boolean xIsEnd = false;

        Position ZsafeStartPos = new Position();//刀具开始坐标
        ZsafeStartPos.Z = mode.zSafeDist;
        ZsafeStartPos.X = radius;
        ZsafeStartPos.Y = mode.thickness + radius + 5;
        pointList.add(NCParameterHandle.outPset(ZsafeStartPos, pathAB, bAngle));
        while (finishedDepth < mode.length)
        {
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedWidth = 0;
            if ((finishedDepth + cutDepth) <= mode.length)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                zIsEnd = true;
                finishedDepth += (mode.length - finishedDepth);
            }
            startPos.X = startPos.X + radius;
            startPos.Y = startPos.Y + mode.thickness + radius + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y - 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle)); ;
            while (finishedWidth < mode.width)
            {
                if ((finishedWidth + cutWidth) <= mode.width)
                {
                    finishedWidth += cutWidth;
                }
                else
                {
                    if (zIsEnd)
                    {
                        xIsEnd = true;
                    }
                    finishedWidth += (mode.width - finishedWidth);
                }
                prevPos.X = startPos.X - finishedWidth;
                prevPos.Y = startPos.Y;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                prevPos.Y = startPos.Y - mode.thickness - radius * 2;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                endPos = prevPos;
                if (zIsEnd && xIsEnd)
                {
                }
                else
                {
                    prevPos.Z += finishedDepth;
                    pointList.add(NCParameterHandle.outPset(prevPos, pathAB, bAngle));
                    prevPos.Y += mode.thickness + 2 * radius;
                    pointList.add(NCParameterHandle.outPset(prevPos, pathAB, bAngle));
                }
            }
        }
        if (mode.iCutter == 4)
        {
        }
        else
        {
            endPos.Z += mode.zSafeDist; ;
            pointList.add(NCParameterHandle.outPset(endPos, pathAB, bAngle));
        }
        pointList.add("safeY:");
        return pointList;
    }
    //endregion
    //region逆时针边线加工刀路
    public static ArrayList ZhiLinePath_AntiClockwise(ArrayList pointList, PathABVal pathAB, ZhiLineMode mode)
    {
        Position endPos = new Position();//刀具开始坐标
        double cutDepth = mode.cutDepth;//每刀深度z
        double finishedWidth = 0;
        double finishedDepth = 0;//已完成长度(z轴)
        double cutWidth = mode.knifeDiameter / 2 * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        boolean bAngle = true;//20180615
        double radius = mode.knifeDiameter / 2;
        boolean zIsEnd = false;
        boolean xIsEnd = false;

        Position ZsafeStartPos = new Position();//刀具开始坐标
        ZsafeStartPos.Z = mode.zSafeDist;
        ZsafeStartPos.X = radius;
        ZsafeStartPos.Y = -radius - 5;
        pointList.add(NCParameterHandle.outPset(ZsafeStartPos, pathAB, bAngle));
        while (finishedDepth < mode.length)
        {
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedWidth = 0;
            if ((finishedDepth + cutDepth) <= mode.length)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                zIsEnd = true;
                finishedDepth += (mode.length - finishedDepth);
            }
            startPos.X = startPos.X + radius;
            startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle)); ;
            while (finishedWidth < mode.width)
            {
                if ((finishedWidth + cutWidth) <= mode.width)
                {
                    finishedWidth += cutWidth;
                }
                else
                {
                    if (zIsEnd)
                    {
                        xIsEnd = true;
                    }
                    finishedWidth += (mode.width - finishedWidth);
                }
                prevPos.X = startPos.X - finishedWidth;
                prevPos.Y = startPos.Y;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                prevPos.Y = startPos.Y + mode.thickness + radius * 2;

                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                endPos = prevPos;
                if (zIsEnd && xIsEnd)
                {
                }
                else
                {
                    prevPos.Z += finishedDepth;
                    pointList.add(NCParameterHandle.outPset(prevPos, pathAB, bAngle));
                    prevPos.Y += -mode.thickness - 2 * radius;
                    pointList.add(NCParameterHandle.outPset(prevPos, pathAB, bAngle));
                }
            }
        }
        if (mode.iCutter == 4)
        {
        }
        else
        {
            endPos.Z += mode.zSafeDist; ;
            pointList.add(NCParameterHandle.outPset(endPos, pathAB, bAngle));
        }
        pointList.add("safeY:");
        return pointList;
    }
    //endregion
    //endregion

    //region方型刀路ok
    //region顺时针方型刀路
    public static ArrayList FangXingToolPath_Clockwise(ArrayList pointList, PathABVal pathAB, ZhiFangMode mode)
    {
        Position endPos = new Position();//刀具开始坐标
        double leftMargin = mode.leftMargion;//右边距
        double bottomMargin = mode.bottomMargion;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.mtThickness;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.mtWidth;//左边距
        double radius = mode.knifeDiameter / 2;

        double finishedBottom = 0;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = radius * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        boolean bAngle = true;//20180615
        while (finishedDepth < mode.mtLength)
        {
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedBottom = 0;
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mode.mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mode.mtLength - finishedDepth);
            }
            startPos.X = startPos.X + radius;
            startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));
            while ((finishedBottom < bottomMargin) || (finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {

                //1右下角
                if ((finishedBottom + cutWidth) <= bottomMargin)
                {
                    finishedBottom += cutWidth;
                }
                else
                {
                    finishedBottom += (bottomMargin - finishedBottom);
                }

                prevPos.X = startPos.X;
                prevPos.Y = startPos.Y + finishedBottom;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //2左下角坐标
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //3左上角坐标
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //4右上角坐标
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }

                prevPos.X = startPos.X - finishedRight;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                prevPos.Y = startPos.Y + finishedBottom;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                endPos = prevPos;
            }
        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
    }
    //endregion
    //region逆时针方型刀路
    public static ArrayList FangXingToolPath_AntiClockwise( ArrayList pointList, PathABVal pathAB, ZhiFangMode mode)
    {
        Position endPos = new Position();//刀具开始坐标

        double leftMargin = mode.leftMargion;//右边距
        double bottomMargin = mode.bottomMargion;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.mtThickness;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.mtWidth;//左边距
        double radius = mode.knifeDiameter / 2;

        double finishedBottom = 0;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = mode.knifeDiameter / 2 * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        boolean bAngle = true;//20180615
        while (finishedDepth < mode.mtLength)
        {
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedBottom = 0;
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mode.mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mode.mtLength - finishedDepth);
            }
            startPos.X = startPos.X + radius;
            startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            endPos = prevPos;
            while ((finishedBottom < bottomMargin) || (finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {
                //1右下角坐标
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }
                prevPos.X = startPos.X - finishedRight;
                prevPos.Y = startPos.Y;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //2右上角坐标
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //3左上角坐标
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //4左下角
                if ((finishedBottom + cutWidth) <= bottomMargin)
                {
                    finishedBottom += cutWidth;
                }
                else
                {
                    finishedBottom += (bottomMargin - finishedBottom);
                }

                //prevPos.Z += finishedDepth + mode.zSafeDist;
                //pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                prevPos.Y = startPos.Y + finishedBottom;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                prevPos.X = startPos.X - finishedRight;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                endPos = prevPos;
            }
        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
    }
    //endregion
    //endregion

    //region带边方型刀路ok
    //region顺时针带边方型刀路
    public static ArrayList FangXingToolPath_ClockwiseHasEdge(ArrayList pointList, PathABVal pathAB, ZhiFangMode mode)
    {
        Position endPos = new Position();//刀具开始坐标

        double leftMargin = mode.leftMargion;//右边距
        double bottomMargin = mode.bottomMargion;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.mtThickness;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.mtWidth;//左边距
        double radius = mode.knifeDiameter / 2;

        double finishedBottom = 0;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = radius * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        boolean bAngle = true;//20180615

        String message = "ok";

        while (finishedDepth < mode.mtLength)
        {
            int iwCount = 0;
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedBottom = 0;
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mode.mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mode.mtLength - finishedDepth);
            }
            startPos.X = startPos.X + radius;
            startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));
            while ((finishedBottom < bottomMargin) || (finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {
                iwCount++;
                //1右下角
                if ((finishedBottom + cutWidth) <= bottomMargin)
                {
                    finishedBottom += cutWidth;
                }
                else
                {
                    finishedBottom += (bottomMargin - finishedBottom);
                }

                prevPos.X = startPos.X;
                prevPos.Y = startPos.Y + finishedBottom;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //2左下角坐标
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //3左上角坐标
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }

                //方向刀路上面一个边刀保存在边线内>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew = new Position();
                        pNew.X = prevPos.X;
                        pNew.Y = prevPos.Y - radius;
                        pNew.Z = prevPos.Z;
                        samePosS = pNew;
                        pointList.add(NCParameterHandle.outGoto(samePosS, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        samePosS.X = prevPos.X;
                        samePosS.Z = prevPos.Z;
                        pointList.add(NCParameterHandle.outGoto(samePosS, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }


                //方向刀路上面一个边刀保存在边线内>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                //4右上角坐标
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }
                prevPos.X = startPos.X - finishedRight;
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew2 = new Position();
                        pNew2.X = prevPos.X;
                        pNew2.Y = prevPos.Y - radius;
                        pNew2.Z = prevPos.Z;
                        samePosS2 = pNew2;
                        pointList.add(NCParameterHandle.outGoto(samePosS2, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        samePosS2.X = prevPos.X;
                        samePosS2.Z = prevPos.Z;
                        pointList.add(NCParameterHandle.outGoto(samePosS2, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }

                prevPos.Y = startPos.Y + finishedBottom;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                endPos = prevPos;
            }

        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
    }
    //endregion
    //region逆时针带边方型刀路
    public static ArrayList FangXingToolPath_AntiClockwiseHasEdge(ArrayList pointList, PathABVal pathAB, ZhiFangMode mode)
    {
        Position endPos = new Position();//刀具开始坐标

        double leftMargin = mode.leftMargion;//右边距
        double bottomMargin = mode.bottomMargion;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.mtThickness;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.mtWidth;//左边距
        double radius = mode.knifeDiameter / 2;

        double finishedBottom = 0;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = mode.knifeDiameter / 2 * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        boolean bAngle = true;//20180615

        String message = "ok";

        while (finishedDepth < mode.mtLength)
        {
            int iwCount = 0;
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedBottom = 0;
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mode.mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mode.mtLength - finishedDepth);
            }
            startPos.X = startPos.X + radius;
            startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));
            while ((finishedBottom < bottomMargin) || (finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {
                iwCount++;
                //1右下角坐标
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }
                prevPos.X = startPos.X - finishedRight;
                prevPos.Y = startPos.Y;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //2右上角坐标
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }

                //方向刀路上面一个边刀保存在边线内>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew = new Position();
                        pNew.X = prevPos.X;
                        pNew.Y = prevPos.Y - radius;
                        pNew.Z = prevPos.Z;
                        samePos = pNew;
                        pointList.add(NCParameterHandle.outGoto(samePos, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        samePos.X = prevPos.X;
                        samePos.Z = prevPos.Z;
                        pointList.add(NCParameterHandle.outGoto(samePos, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }


                //3左上角坐标
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;

                //方向刀路上面一个边刀保存在边线内>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew2 = new Position();
                        pNew2.X = prevPos.X;
                        pNew2.Y = prevPos.Y - radius;
                        pNew2.Z = prevPos.Z;
                        samePos2 = pNew2;
                        pointList.add(NCParameterHandle.outGoto(samePos2, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        samePos2.X = prevPos.X;
                        samePos2.Z = prevPos.Z;
                        pointList.add(NCParameterHandle.outGoto(samePos2, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }

                //4左下角
                if ((finishedBottom + cutWidth) <= bottomMargin)
                {
                    finishedBottom += cutWidth;
                }
                else
                {
                    finishedBottom += (bottomMargin - finishedBottom);
                }
                prevPos.Y = startPos.Y + finishedBottom;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                prevPos.X = startPos.X - finishedRight;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                endPos = prevPos;
            }

        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
    }
    //endregion
    //endregion
    
    //region 带三边边直扁
    //逆时针带边  
    public static ArrayList ZBYToolPath_AntiClockwiseHasThreeEdge(ArrayList pointList, PathABVal pathAB, ZhiBianYuanMode mode)
    {
        Position endPos = new Position();//刀具开始坐标

        double leftMargin = mode.leftMargion;//右边距
        double bottomMargin = mode.bottomMargion;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.mtThickness;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.mtWidth;//左边距
        double radius = mode.knifeDiameter / 2;

        double finishedBottom = 0;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = mode.knifeDiameter / 2 * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        String message = "ok";
        if (topMargin < radius * 2)
        {
            message = "直扁圆刀路:上边距小于钻刀直径，刀路无法执行";
            //return message;
        }

        Boolean bAngle = true;//20180615

        Position ZsafeStartPos = new Position();//刀具开始坐标
        ZsafeStartPos.Z = mode.zSafeDist;
        ZsafeStartPos.X = -radius;
        ZsafeStartPos.Y = -radius - 5;
        pointList.add(NCParameterHandle.outPset(ZsafeStartPos, pathAB, bAngle));
        while (finishedDepth < mode.mtLength)
        {
            int iwCount = 0;
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedBottom = 0;
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mode.mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mode.mtLength - finishedDepth);
            }
            startPos.X = startPos.X + radius;//之前写的起始点，作为下面点变化的参照点
            startPos.Y = startPos.Y - radius - 5;
            //pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            //现在真实起点，不能越过材料边界的点>>
            Position pFactStart = new Position();
            pFactStart.X = startPos.X - 2 * radius;
            pFactStart.Y = startPos.Y;
            pFactStart.Z = startPos.Z;
            pointList.add(NCParameterHandle.outPset(pFactStart, pathAB, bAngle));
            //<<

            startPos.Z = startPos.Z - finishedDepth;
            pFactStart.Z = startPos.Z;
            pointList.add(NCParameterHandle.outPset(pFactStart, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pFactStart.Y = startPos.Y;
            pointList.add(NCParameterHandle.outPset(pFactStart, pathAB, bAngle));
            while ((finishedBottom < bottomMargin) || (finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {
                iwCount++;
                //1右下角坐标
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }
                prevPos.X = startPos.X - finishedRight;
                prevPos.Y = startPos.Y;
                prevPos.Z = startPos.Z;
                //pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNewFst = new Position();
                        pNewFst.X = prevPos.X - radius;
                        pNewFst.Y = prevPos.Y;
                        pNewFst.Z = prevPos.Z;
                        samePos_fst = pNewFst;
                        pointList.add(NCParameterHandle.outGoto(samePos_fst, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        pointList.add(NCParameterHandle.outGoto(samePos_fst, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }
                //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\\

                //2右上角坐标
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }
                //prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                //pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //方向刀路上面一个边刀保存在边线内>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNewSed = new Position();
                        pNewSed.X = samePos_fst.X;
                        pNewSed.Y = prevPos.Y - radius;
                        pNewSed.Z = prevPos.Z;
                        samePos_Sed = pNewSed;
                        pointList.add(NCParameterHandle.outGoto(samePos_Sed, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        samePos_Sed.X = samePos_fst.X;
                        samePos_Sed.Z = prevPos.Z;
                        pointList.add(NCParameterHandle.outGoto(samePos_Sed, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }
                //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\\

                //3左上角坐标
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                //pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //方向刀路上面一个边刀保存在边线内>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                //prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew_Three = new Position();
                        pNew_Three.X = prevPos.X + radius;
                        pNew_Three.Y = prevPos.Y - radius;
                        pNew_Three.Z = prevPos.Z;
                        samePos_Three = pNew_Three;
                        pointList.add(NCParameterHandle.outGoto(samePos_Three, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        pointList.add(NCParameterHandle.outGoto(samePos_Three, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }


                //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\\
                //4左下角
                if ((finishedBottom + cutWidth) <= bottomMargin)
                {
                    finishedBottom += cutWidth;
                }
                else
                {
                    finishedBottom += (bottomMargin - finishedBottom);
                }
                prevPos.Y = startPos.Y + finishedBottom;
                //pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew2_For = new Position();
                        pNew2_For.X = samePos_Three.X;
                        pNew2_For.Y = prevPos.Y;
                        pNew2_For.Z = prevPos.Z;
                        samePos_four = pNew2_For;
                        pointList.add(NCParameterHandle.outGoto(samePos_four, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        samePos_four.Y = prevPos.Y;
                        pointList.add(NCParameterHandle.outGoto(samePos_four, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }



                //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\\
                prevPos.X = startPos.X - finishedRight;
                pFactStart = prevPos;
                pFactStart.X = startPos.X - 2 * radius;
                pointList.add(NCParameterHandle.outGoto(pFactStart, pathAB, bAngle));
                endPos = prevPos;

                //startPos = prevPos;

                ArrayList tempList = new ArrayList();
                if ((finishedBottom == bottomMargin) && (finishedLeft == leftMargin) && (finishedTop == topMargin) && (finishedRight == rightMargin))
                {
                    double c_radius = mode.mtThickness / 2;
                    Position from = new Position();
                    from.X = -c_radius - rightMargin;
                    from.Y = startPos.Y + mode.bottomMargion;
                    from.Z = prevPos.Z;
                    Position to = new Position();
                    to.X = from.X;
                    to.Y = from.Y + mode.mtThickness + mode.knifeDiameter;
                    to.Z = from.Z;

                    Position circlePoint = new Position();
                    circlePoint.X = from.X;
                    circlePoint.Y = from.Y + c_radius + radius;
                    circlePoint.Z = from.Z;
                    tempList = BaseCutCase.getCurveAntiClockPathDataXY(pointList, pathAB, from, to, circlePoint, c_radius + radius, 0);

                    //////////////////////////////////////////////////////////////////////////////////////////////
                    Position from2 = new Position();
                    from2.X = to.X - (mode.mtWidth - 2 * c_radius);
                    from2.Y = to.Y;
                    from2.Z = to.Z;
                    pointList.add(NCParameterHandle.outGoto(from2, pathAB, bAngle));

                    Position to2 = new Position();
                    to2.X = from2.X;
                    to2.Y = from.Y;
                    to2.Z = from2.Z;

                    Position circlePoint2 = new Position();
                    circlePoint2.X = circlePoint.X - (mode.mtWidth - 2 * c_radius);
                    circlePoint2.Y = circlePoint.Y;
                    circlePoint2.Z = circlePoint.Z;
                    tempList = BaseCutCase.getCurveAntiClockPathDataXY(pointList, pathAB, from2, to2, circlePoint2, c_radius + radius, 0);

                    //to2.Z = from.Z + finishedDepth + 5;
                    //to2.Y = from.Y - finishedDepth - 20;
                    //pointList.add(NCParameterHandle.outGoto(to2, pathAB, bAngle));
                }

                //prevPos.Z += finishedDepth + mode.zSafeDist;
                //pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
            }

        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
       // return message;
    }

    //顺时针带边   
    //public static ArrayList ZBYToolPath_ClockwiseHasEdge(ref ArrayList pointList, pathABVal pathAB, ZhiBianYuanMode mode)
    public static ArrayList ZBYToolPath_ClockwiseHasThreeEdge(ArrayList pointList, PathABVal pathAB, ZhiBianYuanMode mode)
    {
        Position endPos = new Position();//刀具开始坐标
        String message = "ok";


        double leftMargin = mode.leftMargion;//右边距
        double bottomMargin = mode.bottomMargion;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.mtThickness;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.mtWidth;//左边距
        double radius = mode.knifeDiameter / 2;

        double finishedBottom = 0;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = radius * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        if (topMargin < radius * 2)
        {
            message = "直扁圆刀路:上边距小于钻刀直径，刀路无法执行";
           // return message;
        }

        Boolean bAngle = true;//20180615

        Position ZsafeStartPos = new Position();//刀具开始坐标
        ZsafeStartPos.Z = mode.zSafeDist;
        ZsafeStartPos.X = -radius;
        ZsafeStartPos.Y = -radius - 5;
        pointList.add(NCParameterHandle.outPset(ZsafeStartPos, pathAB, bAngle));
        while (finishedDepth < mode.mtLength)
        {
            int iwCount = 0;
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedBottom = 0;
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mode.mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mode.mtLength - finishedDepth);
            }

            startPos.X = startPos.X + radius;//之前写的起始点，作为下面点变化的参照点
            startPos.Y = startPos.Y - radius - 5;
            //pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            //现在真实起点，不能越过材料边界的点>>
            Position pFactStart = new Position();
            pFactStart.X = startPos.X - 2 * radius;
            pFactStart.Y = startPos.Y;
            pFactStart.Z = startPos.Z;
            pointList.add(NCParameterHandle.outPset(pFactStart, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pFactStart.Z = startPos.Z;
            pointList.add(NCParameterHandle.outPset(pFactStart, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pFactStart.Y = startPos.Y;
            pointList.add(NCParameterHandle.outPset(pFactStart, pathAB, bAngle));
            while ((finishedBottom < bottomMargin) || (finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {
                iwCount++;
                //1右下角
                if ((finishedBottom + cutWidth) <= bottomMargin)
                {
                    finishedBottom += cutWidth;
                }
                else
                {
                    finishedBottom += (bottomMargin - finishedBottom);
                }

                prevPos.X = startPos.X;
                prevPos.Y = startPos.Y + finishedBottom;
                prevPos.Z = startPos.Z;
                //pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                pFactStart.X = prevPos.X - 2 * radius;
                pFactStart.Y = prevPos.Y;
                pFactStart.Z = prevPos.Z;
                pointList.add(NCParameterHandle.outGoto(pFactStart, pathAB, bAngle));
                //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\\

                //2左下角坐标
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                //pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew1 = new Position();
                        pNew1.X = prevPos.X + radius;
                        pNew1.Y = prevPos.Y;
                        pNew1.Z = prevPos.Z;
                        samePosS_1 = pNew1;
                        pointList.add(NCParameterHandle.outGoto(samePosS_1, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        samePosS_1.Y = prevPos.Y;
                        pointList.add(NCParameterHandle.outGoto(samePosS_1, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }
                //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\\

                //3左上角坐标
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }
                //方向刀路上面一个边刀保存在边线内>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew2 = new Position();
                        //pNew.X = prevPos.X;
                        pNew2.X = samePosS_1.X;
                        pNew2.Y = prevPos.Y - radius;
                        pNew2.Z = prevPos.Z;
                        samePosS_2 = pNew2;
                        pointList.add(NCParameterHandle.outGoto(samePosS_2, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        pointList.add(NCParameterHandle.outGoto(samePosS_2, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    //prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }


                //方向刀路上面一个边刀保存在边线内>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                //4右上角坐标
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }
                prevPos.X = startPos.X - finishedRight;
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew3 = new Position();
                        pNew3.X = prevPos.X - radius;
                        pNew3.Y = prevPos.Y - radius;
                        pNew3.Z = prevPos.Z;
                        samePosS_3 = pNew3;
                        pointList.add(NCParameterHandle.outGoto(samePosS_3, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        samePosS2.X = prevPos.X;
                        samePosS2.Z = prevPos.Z;
                        pointList.add(NCParameterHandle.outGoto(samePosS_3, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }

                //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&\\
                prevPos.Y = startPos.Y + finishedBottom;
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew4 = new Position();
                        pNew4.X = samePosS_3.X;
                        pNew4.Y = prevPos.Y;
                        pNew4.Z = prevPos.Z;
                        samePosS_4 = pNew4;
                        pointList.add(NCParameterHandle.outGoto(samePosS_4, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        pointList.add(NCParameterHandle.outGoto(samePosS_4, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }

                //pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                endPos = prevPos;

                ArrayList tempList = new ArrayList();
                if ((finishedBottom == bottomMargin) && (finishedLeft == leftMargin) && (finishedTop == topMargin) && (finishedRight == rightMargin))
                {
                    double c_radius = mode.mtThickness / 2;
                    Position from = new Position();
                    from.X = -mode.sWidth + c_radius + leftMargin;
                    from.Y = startPos.Y + mode.bottomMargion;
                    from.Z = prevPos.Z;
                    Position to = new Position();
                    to.X = from.X;
                    to.Y = from.Y + mode.mtThickness + mode.knifeDiameter;
                    to.Z = from.Z;

                    Position circlePoint = new Position();
                    circlePoint.X = from.X;
                    circlePoint.Y = from.Y + c_radius + radius;
                    circlePoint.Z = from.Z;
                    tempList = BaseCutCase.getCurveClockPathDataXY(pointList, pathAB, from, to, circlePoint, c_radius + radius, 0);

                    //////////////////////////////////////////////////////////////////////////////////////////////
                    Position from2 = new Position();
                    from2.X = to.X + (mode.mtWidth - 2 * c_radius);
                    from2.Y = to.Y;
                    from2.Z = to.Z;
                    pointList.add(NCParameterHandle.outGoto(from2, pathAB, bAngle));

                    Position to2 = new Position();
                    to2.X = from2.X;
                    to2.Y = from.Y;
                    to2.Z = from2.Z;

                    Position circlePoint2 = new Position();
                    circlePoint2.X = circlePoint.X + (mode.mtWidth - 2 * c_radius);
                    circlePoint2.Y = circlePoint.Y;
                    circlePoint2.Z = circlePoint.Z;
                    tempList = BaseCutCase.getCurveClockPathDataXY(pointList, pathAB, from2, to2, circlePoint2, c_radius + radius, 0);

                    //to2.Z = from.Z + finishedDepth + 5;
                    //to2.Y = from.Y - finishedDepth - 20;
                    //pointList.add(NCParameterHandle.outGoto(to2, pathAB, bAngle));
                }

                //prevPos.Z += finishedDepth + mode.zSafeDist;
                //pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
            }

        }

        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
       // return message;
    }
    //endregion

    //region带边直扁圆榫头刀路ok
    //region顺时针带边直扁圆榫头刀路
    public static ArrayList ZBYToolPath_ClockwiseHasEdge(ArrayList pointList, PathABVal pathAB, ZhiBianYuanMode mode)
    {
        Position endPos = new Position();//刀具开始坐标
        String message = "ok";


        double leftMargin = mode.leftMargion;//右边距
        double bottomMargin = mode.bottomMargion;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.mtThickness;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.mtWidth;//左边距
        double radius = mode.knifeDiameter / 2;

        double finishedBottom = 0;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = radius * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        boolean bAngle = true;//20180615
        while (finishedDepth < mode.mtLength)
        {
            int iwCount = 0;
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedBottom = 0;
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mode.mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mode.mtLength - finishedDepth);
            }
            startPos.X = startPos.X + radius;
            startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));
            while ((finishedBottom < bottomMargin) || (finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {
                iwCount++;

                //1右下角
                if ((finishedBottom + cutWidth) <= bottomMargin)
                {
                    finishedBottom += cutWidth;
                }
                else
                {
                    finishedBottom += (bottomMargin - finishedBottom);
                }


                prevPos.X = startPos.X;
                prevPos.Y = startPos.Y + finishedBottom;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //2左下角坐标
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //3左上角坐标
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }
                //方向刀路上面一个边刀保存在边线内>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew = new Position();
                        pNew.X = prevPos.X;
                        pNew.Y = prevPos.Y - radius;
                        pNew.Z = prevPos.Z;
                        samePosS = pNew;
                        pointList.add(NCParameterHandle.outGoto(samePosS, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        samePosS.X = prevPos.X;
                        samePosS.Z = prevPos.Z;
                        pointList.add(NCParameterHandle.outGoto(samePosS, pathAB, bAngle));


                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }


                //方向刀路上面一个边刀保存在边线内>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                //4右上角坐标
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }
                prevPos.X = startPos.X - finishedRight;
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew2 = new Position();
                        pNew2.X = prevPos.X;
                        pNew2.Y = prevPos.Y - radius;
                        pNew2.Z = prevPos.Z;
                        samePosS2 = pNew2;
                        pointList.add(NCParameterHandle.outGoto(samePosS2, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        samePosS2.X = prevPos.X;
                        samePosS2.Z = prevPos.Z;
                        pointList.add(NCParameterHandle.outGoto(samePosS2, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }
                prevPos.Y = startPos.Y + finishedBottom;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                endPos = prevPos;

                ArrayList tempList = new ArrayList();
                if ((finishedBottom == bottomMargin) && (finishedLeft == leftMargin) && (finishedTop == topMargin) && (finishedRight == rightMargin))
                {
                    double c_radius = mode.mtThickness / 2;
                    Position from = new Position();
                    from.X = -mode.sWidth + c_radius + leftMargin;
                    from.Y = startPos.Y + mode.bottomMargion;
                    from.Z = prevPos.Z;
                    Position to = new Position();
                    to.X = from.X;
                    to.Y = from.Y + mode.mtThickness + mode.knifeDiameter;
                    to.Z = from.Z;

                    Position circlePoint = new Position();
                    circlePoint.X = from.X;
                    circlePoint.Y = from.Y + c_radius + radius;
                    circlePoint.Z = from.Z;
                    tempList = BaseCutCase.getCurveClockPathDataXY(pointList, pathAB, from, to, circlePoint, c_radius + radius, 0);

                    //////////////////////////////////////////////////////////////////////////////////////////////
                    Position from2 = new Position();
                    from2.X = to.X + (mode.mtWidth - 2 * c_radius);
                    from2.Y = to.Y;
                    from2.Z = to.Z;
                    pointList.add(NCParameterHandle.outGoto(from2, pathAB, bAngle));

                    Position to2 = new Position();
                    to2.X = from2.X;
                    to2.Y = from.Y;
                    to2.Z = from2.Z;

                    Position circlePoint2 = new Position();
                    circlePoint2.X = circlePoint.X + (mode.mtWidth - 2 * c_radius);
                    circlePoint2.Y = circlePoint.Y;
                    circlePoint2.Z = circlePoint.Z;
                    tempList = BaseCutCase.getCurveClockPathDataXY(pointList, pathAB, from2, to2, circlePoint2, c_radius + radius, 0);

                }

            }
        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
    }
    //endregion
    //region逆时针带边直扁圆榫头刀路
    public static ArrayList ZBYToolPath_AntiClockwiseHasEdge(ArrayList pointList, PathABVal pathAB, ZhiBianYuanMode mode)
    {
        Position endPos = new Position();//刀具开始坐标

        double leftMargin = mode.leftMargion;//右边距
        double bottomMargin = mode.bottomMargion;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.mtThickness;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.mtWidth;//左边距
        double radius = mode.knifeDiameter / 2;

        double finishedBottom = 0;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = mode.knifeDiameter / 2 * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        String message = "ok";

        boolean bAngle = true;//20180615
        while (finishedDepth < mode.mtLength)
        {
            int iwCount = 0;
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedBottom = 0;
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mode.mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mode.mtLength - finishedDepth);
            }
            startPos.X = startPos.X + radius;
            startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));
            while ((finishedBottom < bottomMargin) || (finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {
                iwCount++;
                //1右下角坐标
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }
                prevPos.X = startPos.X - finishedRight;
                prevPos.Y = startPos.Y;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //2右上角坐标
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }
                //prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                //pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //方向刀路上面一个边刀保存在边线内>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew = new Position();
                        pNew.X = prevPos.X;
                        pNew.Y = prevPos.Y - radius;
                        pNew.Z = prevPos.Z;
                        samePos = pNew;
                        pointList.add(NCParameterHandle.outGoto(samePos, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        samePos.X = prevPos.X;
                        samePos.Z = prevPos.Z;
                        pointList.add(NCParameterHandle.outGoto(samePos, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }


                //3左上角坐标
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;

                //方向刀路上面一个边刀保存在边线内>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                if (mode.iCutter == 1)
                {
                    if (iwCount == 1)
                    {
                        Position pNew2 = new Position();
                        pNew2.X = prevPos.X;
                        pNew2.Y = prevPos.Y - radius;
                        pNew2.Z = prevPos.Z;
                        samePos2 = pNew2;
                        pointList.add(NCParameterHandle.outGoto(samePos2, pathAB, bAngle));
                    }
                    else if (iwCount == 2)
                    {
                        samePos2.X = prevPos.X;
                        samePos2.Z = prevPos.Z;
                        pointList.add(NCParameterHandle.outGoto(samePos2, pathAB, bAngle));
                    }
                    else
                    {
                        pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                    }
                }
                else
                {
                    pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                }

                //4左下角
                if ((finishedBottom + cutWidth) <= bottomMargin)
                {
                    finishedBottom += cutWidth;
                }
                else
                {
                    finishedBottom += (bottomMargin - finishedBottom);
                }
                prevPos.Y = startPos.Y + finishedBottom;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                prevPos.X = startPos.X - finishedRight;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                endPos = prevPos;

                ArrayList tempList = new ArrayList();
                if ((finishedBottom == bottomMargin) && (finishedLeft == leftMargin) && (finishedTop == topMargin) && (finishedRight == rightMargin))
                {
                    double c_radius = mode.mtThickness / 2;
                    Position from = new Position();
                    from.X = -c_radius - rightMargin;
                    from.Y = startPos.Y + mode.bottomMargion;
                    from.Z = prevPos.Z;
                    Position to = new Position();
                    to.X = from.X;
                    to.Y = from.Y + mode.mtThickness + mode.knifeDiameter;
                    to.Z = from.Z;

                    Position circlePoint = new Position();
                    circlePoint.X = from.X;
                    circlePoint.Y = from.Y + c_radius + radius;
                    circlePoint.Z = from.Z;
                    tempList = BaseCutCase.getCurveAntiClockPathDataXY(pointList, pathAB, from, to, circlePoint, c_radius + radius, 0);

                    //////////////////////////////////////////////////////////////////////////////////////////////
                    Position from2 = new Position();
                    from2.X = to.X - (mode.mtWidth - 2 * c_radius);
                    from2.Y = to.Y;
                    from2.Z = to.Z;
                    pointList.add(NCParameterHandle.outGoto(from2, pathAB, bAngle));

                    Position to2 = new Position();
                    to2.X = from2.X;
                    to2.Y = from.Y;
                    to2.Z = from2.Z;

                    Position circlePoint2 = new Position();
                    circlePoint2.X = circlePoint.X - (mode.mtWidth - 2 * c_radius);
                    circlePoint2.Y = circlePoint.Y;
                    circlePoint2.Z = circlePoint.Z;
                    tempList = BaseCutCase.getCurveAntiClockPathDataXY(pointList, pathAB, from2, to2, circlePoint2, c_radius + radius, 0);
                }

            }

        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
    }
    //endregion
    //endregion

    //region直扁圆榫头刀路ok
    //region顺时针直扁圆榫头刀路
    public static ArrayList ZhiBianToolPath_Clockwise(ArrayList pointList, PathABVal pathAB, ZhiBianYuanMode mode)
    {
        Position endPos = new Position();//刀具开始坐标
        double leftMargin = mode.leftMargion;//右边距
        double bottomMargin = mode.bottomMargion;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.mtThickness;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.mtWidth;//左边距
        double radius = mode.knifeDiameter / 2;

        double finishedBottom = 0;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = radius * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        boolean bAngle = true;//20180615
        while (finishedDepth < mode.mtLength)
        {
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedBottom = 0;
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mode.mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mode.mtLength - finishedDepth);
            }
            startPos.X = startPos.X + radius;
            startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));
            while ((finishedBottom < bottomMargin) || (finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {

                //1右下角
                if ((finishedBottom + cutWidth) <= bottomMargin)
                {
                    finishedBottom += cutWidth;
                }
                else
                {
                    finishedBottom += (bottomMargin - finishedBottom);
                }

                prevPos.X = startPos.X;
                prevPos.Y = startPos.Y + finishedBottom;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //2左下角坐标
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //3左上角坐标
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //4右上角坐标
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }
                prevPos.X = startPos.X - finishedRight;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                prevPos.Y = startPos.Y + finishedBottom;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                endPos = prevPos;
                ArrayList tempList = new ArrayList();
                if ((finishedBottom == bottomMargin) && (finishedLeft == leftMargin) && (finishedTop == topMargin) && (finishedRight == rightMargin))
                {
                    double c_radius = mode.mtThickness / 2;
                    Position from = new Position();
                    from.X = -mode.sWidth + c_radius + leftMargin;
                    from.Y = startPos.Y + mode.bottomMargion;
                    from.Z = prevPos.Z;
                    Position to = new Position();
                    to.X = from.X;
                    to.Y = from.Y + mode.mtThickness + mode.knifeDiameter;
                    to.Z = from.Z;

                    Position circlePoint = new Position();
                    circlePoint.X = from.X;
                    circlePoint.Y = from.Y + c_radius + radius;
                    circlePoint.Z = from.Z;
                    tempList = BaseCutCase.getCurveClockPathDataXY(pointList, pathAB, from, to, circlePoint, c_radius + radius, 0);

                    //////////////////////////////////////////////////////////////////////////////////////////////
                    Position from2 = new Position();
                    from2.X = to.X + (mode.mtWidth - 2 * c_radius);
                    from2.Y = to.Y;
                    from2.Z = to.Z;
                    pointList.add(NCParameterHandle.outGoto(from2, pathAB, bAngle));

                    Position to2 = new Position();
                    to2.X = from2.X;
                    to2.Y = from.Y;
                    to2.Z = from2.Z;

                    Position circlePoint2 = new Position();
                    circlePoint2.X = circlePoint.X + (mode.mtWidth - 2 * c_radius);
                    circlePoint2.Y = circlePoint.Y;
                    circlePoint2.Z = circlePoint.Z;
                    tempList = BaseCutCase.getCurveClockPathDataXY(pointList, pathAB, from2, to2, circlePoint2, c_radius + radius, 0);
                }
            }
        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
    }
    //endregion
    //region逆时针直扁圆榫头刀路
    public static ArrayList ZhiBianToolPath_AntiClockwise(ArrayList pointList, PathABVal pathAB, ZhiBianYuanMode mode)
    {
        Position endPos = new Position();//刀具开始坐标
        double leftMargin = mode.leftMargion;//右边距
        double bottomMargin = mode.bottomMargion;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.mtThickness;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.mtWidth;//左边距
        double radius = mode.knifeDiameter / 2;

        double finishedBottom = 0;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = mode.knifeDiameter / 2 * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        boolean bAngle = true;//20180615
        while (finishedDepth < mode.mtLength)
        {
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedBottom = 0;
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mode.mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mode.mtLength - finishedDepth);
            }
            startPos.X = startPos.X + radius;
            startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));
            while ((finishedBottom < bottomMargin) || (finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {
                //1右下角坐标
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }
                prevPos.X = startPos.X - finishedRight;
                prevPos.Y = startPos.Y;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //2右上角坐标
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //3左上角坐标
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //4左下角
                if ((finishedBottom + cutWidth) <= bottomMargin)
                {
                    finishedBottom += cutWidth;
                }
                else
                {
                    finishedBottom += (bottomMargin - finishedBottom);
                }
                prevPos.Y = startPos.Y + finishedBottom;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                prevPos.X = startPos.X - finishedRight;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                endPos = prevPos;

                ArrayList tempList = new ArrayList();
                if ((finishedBottom == bottomMargin) && (finishedLeft == leftMargin) && (finishedTop == topMargin) && (finishedRight == rightMargin))
                {
                    double c_radius = mode.mtThickness / 2;
                    Position from = new Position();
                    from.X = -c_radius - rightMargin;
                    from.Y = startPos.Y + mode.bottomMargion;
                    from.Z = prevPos.Z;
                    Position to = new Position();
                    to.X = from.X;
                    to.Y = from.Y + mode.mtThickness + mode.knifeDiameter;
                    to.Z = from.Z;

                    Position circlePoint = new Position();
                    circlePoint.X = from.X;
                    circlePoint.Y = from.Y + c_radius + radius;
                    circlePoint.Z = from.Z;
                    tempList = BaseCutCase.getCurveAntiClockPathDataXY(pointList, pathAB, from, to, circlePoint, c_radius + radius, 0);

                    //////////////////////////////////////////////////////////////////////////////////////////////
                    Position from2 = new Position();
                    from2.X = to.X - (mode.mtWidth - 2 * c_radius);
                    from2.Y = to.Y;
                    from2.Z = to.Z;
                    pointList.add(NCParameterHandle.outGoto(from2, pathAB, bAngle));

                    Position to2 = new Position();
                    to2.X = from2.X;
                    to2.Y = from.Y;
                    to2.Z = from2.Z;

                    Position circlePoint2 = new Position();
                    circlePoint2.X = circlePoint.X - (mode.mtWidth - 2 * c_radius);
                    circlePoint2.Y = circlePoint.Y;
                    circlePoint2.Z = circlePoint.Z;
                    tempList = BaseCutCase.getCurveAntiClockPathDataXY(pointList, pathAB, from2, to2, circlePoint2, c_radius + radius, 0);
                }

            }

        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
    }
    //endregion
    //endregion

    //region直圆榫头刀路ok
    //region顺时针直圆榫头刀路
    public static ArrayList ZhiYuanToolPath_Clockwise(ArrayList pointList, PathABVal pathAB, ZhiYuanMode mode)
    {
        Position endPos = new Position();//刀具开始坐标
        double leftMargin = mode.leftMargion;//右边距
        double bottomMargin = mode.bottomMargion;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.circleDiameter;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.circleDiameter;//左边距
        double radius = mode.knifeDiameter / 2;
        double mtLength = mode.mtLength;
        double finishedBottom = 0;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = radius * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)
        boolean bAngle = true;//20180615

        while (finishedDepth < mode.mtLength)
        {
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedBottom = 0;
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mode.mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mode.mtLength - finishedDepth);
            }


            startPos.X = startPos.X + radius;
            startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));
            while ((finishedBottom < bottomMargin) || (finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {

                //1右下角
                if ((finishedBottom + cutWidth) <= bottomMargin)
                {
                    finishedBottom += cutWidth;
                }
                else
                {
                    finishedBottom += (bottomMargin - finishedBottom);
                }

                prevPos.X = startPos.X;
                prevPos.Y = startPos.Y + finishedBottom;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //2左下角坐标
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //3左上角坐标
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //4右上角坐标
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }
                prevPos.X = startPos.X - finishedRight;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                prevPos.Y = startPos.Y + finishedBottom;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                endPos = prevPos;

                // 画圆
                ArrayList tempList = new ArrayList();
                if ((finishedBottom == bottomMargin) && (finishedLeft == leftMargin) && (finishedTop == topMargin) && (finishedRight == rightMargin))
                {
                    double c_radius = mode.circleDiameter / 2;
                    Position from = new Position();
                    from.X = -mode.sWidth + c_radius + leftMargin;
                    from.Y = prevPos.Y + radius;
                    from.Z = prevPos.Z;// +2 * (c_radius + radius);

                    Position to = new Position();
                    to.X = from.X;
                    to.Y = from.Y + mode.circleDiameter;
                    to.Z = from.Z;

                    Position circlePoint = new Position();
                    circlePoint.X = from.X;
                    circlePoint.Y = from.Y + c_radius;
                    circlePoint.Z = from.Z;
                    tempList = BaseCutCase.getCurveClockPathDataXY(pointList, pathAB, from, to, circlePoint, c_radius + radius, 0);

                    //////////////////////////////////////////////////////////////////////////////////////////
                    Position from2 = new Position();
                    from2.X = to.X;
                    from2.Y = to.Y;
                    from2.Z = to.Z;

                    Position to2 = new Position();
                    to2.X = from.X;
                    to2.Y = from.Y;
                    to2.Z = from.Z;

                    Position circlePoint2 = new Position();
                    circlePoint2.X = circlePoint.X;
                    circlePoint2.Y = circlePoint.Y;
                    circlePoint2.Z = circlePoint.Z;
                    tempList = BaseCutCase.getCurveClockPathDataXY(pointList, pathAB, from2, to2, circlePoint2, c_radius + radius, 0);

                    to2.Z = from.Z + finishedDepth + 5;
                    to2.Y = from.Y - finishedDepth - 20;
                    pointList.add(NCParameterHandle.outGoto(to2, pathAB, bAngle));
                }
            }
        }

        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
    }
    //endregion
    //region逆时针直圆榫头刀路
    public static ArrayList ZhiYuanToolPath_AntiClockwise(ArrayList pointList, PathABVal pathAB, ZhiYuanMode mode)
    {
        Position endPos = new Position();//刀具开始坐标
        double leftMargin = mode.leftMargion;//右边距
        double bottomMargin = mode.bottomMargion;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.circleDiameter;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.circleDiameter;//左边距
        double radius = mode.knifeDiameter / 2;
        double mtLength = mode.mtLength;
        double finishedBottom = 0;
        double finishedLeft = 0;
        double finishedTop = 0;
        double finishedRight = 0;

        double cutWidth = radius * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)
        boolean bAngle = true;//20180615
        while (finishedDepth < mode.mtLength)
        {
            Position startPos = new Position();//刀具开始坐标
            Position prevPos = new Position();//刀具下一个坐标
            finishedBottom = 0;
            finishedLeft = 0;
            finishedTop = 0;
            finishedRight = 0;
            //深度
            if ((finishedDepth + cutDepth) <= mode.mtLength)
            {
                finishedDepth += cutDepth;
            }
            else
            {
                finishedDepth += (mode.mtLength - finishedDepth);
            }
            startPos.X = startPos.X + radius;
            startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            startPos.Y = startPos.Y + 5;
            pointList.add(NCParameterHandle.outPset(startPos, pathAB, bAngle));

            while ((finishedBottom < bottomMargin) || (finishedLeft < leftMargin) || (finishedTop < topMargin) || (finishedRight < rightMargin))
            {
                //1右下角坐标
                if ((finishedRight + cutWidth) <= rightMargin)
                {
                    finishedRight += cutWidth;
                }
                else
                {
                    finishedRight += (rightMargin - finishedRight);
                }
                prevPos.X = startPos.X - finishedRight;
                prevPos.Y = startPos.Y;
                prevPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //2右上角坐标
                if ((finishedTop + cutWidth) <= topMargin)
                {
                    finishedTop += cutWidth;
                }
                else
                {
                    finishedTop += (topMargin - finishedTop);
                }
                prevPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //3左上角坐标
                if ((finishedLeft + cutWidth) <= leftMargin)
                {
                    finishedLeft += cutWidth;
                }
                else
                {
                    finishedLeft += (leftMargin - finishedLeft);
                }
                prevPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                //4左下角
                if ((finishedBottom + cutWidth) <= bottomMargin)
                {
                    finishedBottom += cutWidth;
                }
                else
                {
                    finishedBottom += (bottomMargin - finishedBottom);
                }
                prevPos.Y = startPos.Y + finishedBottom;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));

                prevPos.X = startPos.X - finishedRight;
                pointList.add(NCParameterHandle.outGoto(prevPos, pathAB, bAngle));
                endPos = prevPos;
                ArrayList tempList = new ArrayList();
                if ((finishedBottom == bottomMargin) && (finishedLeft == leftMargin) && (finishedTop == topMargin) && (finishedRight == rightMargin))
                {
                    //画2个半圆
                    double c_radius = mode.circleDiameter / 2;
                    Position from = new Position();
                    from.X = -c_radius - rightMargin;
                    from.Y = prevPos.Y + radius;
                    from.Z = prevPos.Z;

                    Position to = new Position();
                    to.X = from.X;
                    to.Y = from.Y + mode.circleDiameter;
                    to.Z = from.Z;

                    Position circlePoint = new Position();
                    circlePoint.X = from.X;
                    circlePoint.Y = from.Y + c_radius;
                    circlePoint.Z = from.Z;
                    tempList = BaseCutCase.getCurveAntiClockPathDataXY(pointList, pathAB, from, to, circlePoint, c_radius + radius, 0);

                    //////////////////////////////////////////////////////////////////////////////////////////////
                    Position from2 = new Position();
                    from2.X = to.X;
                    from2.Y = to.Y;
                    from2.Z = to.Z;

                    Position to2 = new Position();
                    to2.X = from.X;
                    to2.Y = from.Y;
                    to2.Z = from.Z;

                    Position circlePoint2 = new Position();
                    circlePoint2.X = circlePoint.X;
                    circlePoint2.Y = circlePoint.Y;
                    circlePoint2.Z = circlePoint.Z;
                    tempList = BaseCutCase.getCurveAntiClockPathDataXY(pointList, pathAB, from2, to2, circlePoint2, c_radius + radius, 0);
                }
            }

        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, pathAB, bAngle));
        pointList.add("safeY:");
        return pointList;
    }
    //endregion
    //endregion

    //region直圆母榫刀路ok
    public static ArrayList CircleFemalePath(ArrayList pointList, MuYuanSunMode mymode)
    {
        WasteAndroid w=new WasteAndroid();
        double cDeep = mymode.GrooveDeep;//槽深
        double radius = mymode.Radius;
        double zSafe = mymode.zSafeDist;

        CutParameter cup = new CutParameter();
        cup.KnifeDiameter = mymode.knifeDiameter;
        cup.CutDeep = mymode.cutDepth;
        PathABVal pABV = mymode.pathAB;
        w.Deep = cDeep;
        w.zSafeDist = zSafe;
        w.Radius = radius;
        w.cup = cup;
        w.pathAB = pABV;
        if (pABV.ISClockWise == 0)//逆时针
        {
            getAntiClockCircleHolePathData(pointList, mymode.CentrePoint, w);
        }
        else //顺时针
        {
            getClockCircleHolePathData(pointList, mymode.CentrePoint,w);
        }

        pointList.add("safeY:");
        return pointList;
    }
    //region顺时针直圆母榫刀路
    public static void getClockCircleHolePathData(ArrayList pointList, Position center, WasteAndroid w)
    {
        double currentRadius = 0.0D;//当前一个循环圆的半径
        double stepR = w.cup.KnifeDiameter / 2;//半径步宽
        double targetDeepZ = center.Z - w.Deep;// 目标深度Z轴坐标值
        double Z = center.Z;

        Position initPoint = new Position();
        initPoint.X = center.X; initPoint.Y = center.Y; initPoint.Z = center.Z + w.Deep;
        pointList.add(NCParameterHandle.outPset(initPoint, w.pathAB, true));
        pointList.add(NCParameterHandle.outPset(center, w.pathAB, true));
        w.Radius -= w.cup.KnifeDiameter / 2;
        Position startPoint = new Position();//起止点
        while (targetDeepZ < Z)
        {
            //判断是否满足一个车削深度
            w.cup.CutDeep = Math.abs(targetDeepZ - Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - Z);
            Z -= w.cup.CutDeep;

            Position centerPoint = new Position();//圆心点
            centerPoint.X = center.X; centerPoint.Y = center.Y; centerPoint.Z = startPoint.isZero() ? Z : startPoint.Z;
            pointList.add(NCParameterHandle.outGoto(centerPoint, w.pathAB, true));

            Position deepPoint = new Position();
            deepPoint.X = centerPoint.X; deepPoint.Y = centerPoint.Y; deepPoint.Z = Z;
            pointList.add(NCParameterHandle.outGoto(deepPoint, w.pathAB, true));

            while (w.Radius > currentRadius)
            {
                //判断是否满足一个半径车削量
                stepR = w.Radius - currentRadius > stepR ? stepR : (w.Radius - currentRadius);
                currentRadius += stepR;

                startPoint.X = center.X + currentRadius; startPoint.Y = center.Y; startPoint.Z = Z;
                BaseCutCase.getCurveClockPathDataXY(pointList, w.pathAB, startPoint, startPoint, deepPoint, currentRadius, 0);
            }
            currentRadius = 0.0D; stepR = w.cup.KnifeDiameter / 2;
        }
        center.Z = startPoint.Z;
        pointList.add(NCParameterHandle.outPset(center, w.pathAB, true));
        center.Z = w.Deep + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(center, w.pathAB, true));
    }
    //endregion
    //region逆时针直圆母榫刀路
    public static void getAntiClockCircleHolePathData(ArrayList pointList,Position center, WasteAndroid w)
    {
        double currentRadius = 0.0D;//当前一个循环圆的半径
        double stepR = w.cup.KnifeDiameter / 2;//半径步宽
        double targetDeepZ = center.Z - w.Deep;// 目标深度Z轴坐标值
        double Z = center.Z;

        Position initPoint = new Position();
        initPoint.X = center.X; initPoint.Y = center.Y; initPoint.Z = center.Z + w.Deep;
        pointList.add(NCParameterHandle.outPset(initPoint, w.pathAB, true));
        pointList.add(NCParameterHandle.outPset(center, w.pathAB, true));
        w.Radius -= w.cup.KnifeDiameter / 2;
        Position startPoint = new Position();//起止点
        while (targetDeepZ < Z)
        {
            //判断是否满足一个车削深度
            w.cup.CutDeep = Math.abs(targetDeepZ - Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - Z);
            Z -= w.cup.CutDeep;

            Position centerPoint = new Position();//圆心点
            centerPoint.X = center.X; centerPoint.Y = center.Y; centerPoint.Z = startPoint.isZero() ? Z : startPoint.Z;
            pointList.add(NCParameterHandle.outGoto(centerPoint, w.pathAB, true));

            Position deepPoint = new Position();
            deepPoint.X = centerPoint.X; deepPoint.Y = centerPoint.Y; deepPoint.Z = Z;
            pointList.add(NCParameterHandle.outGoto(deepPoint, w.pathAB, true));

            while (w.Radius > currentRadius)
            {
                //判断是否满足一个半径车削量
                stepR = w.Radius - currentRadius > stepR ? stepR : (w.Radius - currentRadius);
                currentRadius += stepR;

                startPoint.X = center.X + currentRadius; startPoint.Y = center.Y; startPoint.Z = Z;
                BaseCutCase.getCurveAntiClockPathDataXY(pointList, w.pathAB, startPoint, startPoint, deepPoint, currentRadius, 0);
            }
            currentRadius = 0.0D; stepR = w.cup.KnifeDiameter / 2;
        }
        center.Z = startPoint.Z;
        pointList.add(NCParameterHandle.outPset(center, w.pathAB, true));
        center.Z = w.Deep + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(center, w.pathAB, true));
    }
    //endregion
    //endregion

    //region直方母榫刀路ok
    public static void StraightRectFemalePath(ArrayList pointList, ZhiFangMuMode zfmmode)
    {
        WasteAndroid w = new WasteAndroid();
        double srfDeep = zfmmode.GrooveDeep;
        double srfThick = zfmmode.GrooveWidth;
        double zSafe = zfmmode.zSafeDist;

        CutParameter cup = new CutParameter();
        cup.KnifeDiameter = zfmmode.knifeDiameter;
        cup.CutDeep = zfmmode.cutDepth;
        PathABVal pABV = zfmmode.pathAB;
        w.Deep = srfDeep;
        w.Thickness = srfThick;
        w.zSafeDist = zSafe;
        w.cup = cup;
        w.pathAB = pABV;

        Position sidepoint = new Position();
        sidepoint.X = 0; sidepoint.Y = 0; sidepoint.Z = 0;

        if (pABV.ISClockWise == 0) //逆时针
        {
            getAntiClockStraightRectPathData(pointList,sidepoint, w);
        }
        else//顺时针
        {
            getClockStraightRectPathData(pointList,sidepoint,w);
        }

        pointList.add("safeY:");
    }
    //region逆时针直方母榫刀路
    public static void getAntiClockStraightRectPathData( ArrayList result,Position sidePoint, WasteAndroid w)
    {
        double halfWidthVal = (w.Width - w.cup.KnifeDiameter) / 2;
        double perval = w.cup.KnifeDiameter / 2;//每一次的进刀量
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标
        double centerWidthX = sidePoint.X - w.Width / 2;//母榫宽度中心X坐标值
        double centerLengthY = sidePoint.Y + w.Width / 2;//母榫长度中心Y坐标值
        double Z = sidePoint.Z;
        double stepWidth = perval;//步宽
        double currentWidth = 0.0D;//当前的宽度值;
        while (targetDeepZ < Z)
        {
            //判断是否满足一个车削深度
            w.cup.CutDeep = Math.abs(targetDeepZ - Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - Z);
            Z -= w.cup.CutDeep;
            currentWidth = 0.0D;
            stepWidth = perval;

            while (halfWidthVal > currentWidth)
            {
                //1.开始车削点;2.转向点;3.转向回程点;4.回程转向点1;5.回程转向点2;
                //判断是否满足一个车削量
                stepWidth = halfWidthVal - currentWidth > perval ? perval : halfWidthVal - currentWidth;

                currentWidth += stepWidth;
                Position cutStartPoint = new Position();//开始车削点
                cutStartPoint.X = centerWidthX - currentWidth; cutStartPoint.Y = centerLengthY;
                cutStartPoint.Z = Z;
                result.add(cutStartPoint);

                Position middTurn = new Position();//转向点
                middTurn.X = cutStartPoint.X; middTurn.Y = centerLengthY - currentWidth; middTurn.Z = cutStartPoint.Z;
                result.add(middTurn);

                Position turnBack = new Position();//转向回程点
                turnBack.X = centerWidthX + currentWidth; turnBack.Y = middTurn.Y; turnBack.Z = middTurn.Z;
                result.add(turnBack);

                Position backTurnPoint1 = new Position();//回程转向点1
                backTurnPoint1.X = turnBack.X; backTurnPoint1.Y = centerLengthY + currentWidth; backTurnPoint1.Z = turnBack.Z;
                result.add(backTurnPoint1);

                Position backTurnPoint2 = new Position();//回程转向点2
                backTurnPoint2.X = centerWidthX - currentWidth; backTurnPoint2.Y = backTurnPoint1.Y; backTurnPoint2.Z = backTurnPoint1.Z;
                result.add(backTurnPoint2);

                Position cutEndPoint = new Position();//车削结束点
                cutEndPoint.X = backTurnPoint2.X; cutEndPoint.Y = centerLengthY; cutEndPoint.Z = backTurnPoint2.Z;
                result.add(cutEndPoint);
            }
        }
    }
    //endregion
    //region顺时针直方母榫刀路
    public static void getClockStraightRectPathData(ArrayList result, Position sidePoint, WasteAndroid w)
    {
        double halfWidthVal = (w.Width - w.cup.KnifeDiameter) / 2;
        double perval = w.cup.KnifeDiameter / 2;//每一次的进刀量
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标
        double centerWidthX = sidePoint.X - w.Width / 2;//母榫宽度中心X坐标值
        double centerLengthY = sidePoint.Y + w.Width / 2;//母榫长度中心Y坐标值
        double Z = sidePoint.Z;
        double stepWidth = perval;//步宽
        double currentWidth = 0.0D;//当前的宽度值;
        while (targetDeepZ < Z)
        {

            //判断是否满足一个车削深度
            w.cup.CutDeep = Math.abs(targetDeepZ - Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - Z);
            Z -= w.cup.CutDeep;
            currentWidth = 0.0D;
            stepWidth = perval;

            while (halfWidthVal > currentWidth)
            {
                //1.开始车削点;2.转向点;3.转向回程点;4.回程转向点1;5.回程转向点2;
                //判断是否满足一个车削量
                stepWidth = halfWidthVal - currentWidth > perval ? perval : halfWidthVal - currentWidth;

                currentWidth += stepWidth;
                Position cutStartPoint = new Position();//开始车削点
                cutStartPoint.X = centerWidthX - currentWidth; cutStartPoint.Y = centerLengthY;
                cutStartPoint.Z = Z;
                result.add(cutStartPoint);

                Position middTurn = new Position();//转向点
                middTurn.X = cutStartPoint.X; middTurn.Y = centerLengthY + currentWidth; middTurn.Z = cutStartPoint.Z;
                result.add(middTurn);

                Position turnBack = new Position();//转向回程点
                turnBack.X = centerWidthX + currentWidth; turnBack.Y = middTurn.Y; turnBack.Z = middTurn.Z;
                result.add(turnBack);

                Position backTurnPoint1 = new Position();//回程转向点1
                backTurnPoint1.X = turnBack.X; backTurnPoint1.Y = centerLengthY - currentWidth; backTurnPoint1.Z = turnBack.Z;
                result.add(backTurnPoint1);

                Position backTurnPoint2 = new Position();//回程转向点2
                backTurnPoint2.X = centerWidthX - currentWidth; backTurnPoint2.Y = backTurnPoint1.Y; backTurnPoint2.Z = backTurnPoint1.Z;
                result.add(backTurnPoint2);

                Position cutEndPoint = new Position();//车削结束点
                cutEndPoint.X = backTurnPoint2.X; cutEndPoint.Y = centerLengthY; cutEndPoint.Z = backTurnPoint2.Z;
                result.add(cutEndPoint);
            }
        }
    }
    //endregion

    //endregion

    //region直扁圆母榫刀路ok
    public static ArrayList StraightFlatFemalePath(ArrayList pointList, ZhiBianMuYuanMode zbmymode)
    {
        WasteAndroid w=new WasteAndroid();
        double sffDeep = zbmymode.GrooveDeep;
        double sffLength = zbmymode.GrooveThick;
        double sffWidth = zbmymode.GrooveWidth;
        double zSafe = zbmymode.zSafeDist;
        CutParameter cup = new CutParameter();
        cup.KnifeDiameter = zbmymode.knifeDiameter;
        cup.CutDeep = zbmymode.cutDepth;
        PathABVal pABV = zbmymode.pathAB;
        w.Deep = sffDeep;
        w.Width = sffLength;
        w.Thickness = sffWidth;
        w.zSafeDist = zSafe;
        w.cup = cup;
        w.pathAB = pABV;
        Position sidepoint = new Position();
        sidepoint.X = 0; sidepoint.Y = 0; sidepoint.Z = 0;

        if (pABV.ISClockWise == 0)//逆时针
        {
            if (w.Width == cup.KnifeDiameter)
                getAntiClockStraightFlatHolePathData(pointList,sidepoint, w);
            if (w.Width > cup.KnifeDiameter)
                getAntiClockBigStraightFlatHolePathData(pointList, sidepoint, w);
        }
        else//顺时针
        {
            if (w.Width == cup.KnifeDiameter)
                getClockStraightFlatHolePathData(pointList,sidepoint,w);
            if (w.Width > cup.KnifeDiameter)
                getClockBigStraightFlatHolePathData(pointList,sidepoint,w);
        }

        pointList.add("safeY:");
        return pointList;

    }


    //region顺时针直扁圆母榫刀路,榫宽度等于刀具宽度
    public static void getClockStraightFlatHolePathData(ArrayList pointList,Position sidePoint, WasteAndroid w)
    {
        double Z = sidePoint.Z;
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标值

        int i = 1;
        Position endCutPoint = new Position();//车削终止点
        sidePoint.Y += (w.Thickness - w.cup.KnifeDiameter / 2);

        Position initP = new Position();
        initP.X = sidePoint.X - w.cup.KnifeDiameter / 2;
        initP.Y = sidePoint.Y;
        initP.Z = Z + w.Deep;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));

        while (sidePoint.Z > targetDeepZ)
        {
            int isGoBack = i % 2 == 0 ? -1 : 1;

            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z);
            sidePoint.Z -= w.cup.CutDeep;

            Position startPoint = new Position();//起始点
            startPoint.X = sidePoint.X - w.cup.KnifeDiameter / 2;
            startPoint.Y = endCutPoint.isZero() ? sidePoint.Y : endCutPoint.Y;
            startPoint.Z = endCutPoint.isZero() ? Z : sidePoint.Z;
            pointList.add(NCParameterHandle.outGoto(startPoint, w.pathAB, true));

            if (i == 1)
            {
                startPoint.Z = sidePoint.Z;
                pointList.add(NCParameterHandle.outGoto(startPoint, w.pathAB, true));
            }
            endCutPoint = new Position();
            endCutPoint.X = startPoint.X;
            endCutPoint.Y = startPoint.Y - isGoBack * (w.Thickness - w.cup.KnifeDiameter);
            endCutPoint.Z = startPoint.Z;
            pointList.add(NCParameterHandle.outGoto(endCutPoint, w.pathAB, true));//车削结束点

            i++;
        }
        Position safePoint = new Position();
        safePoint.X = endCutPoint.X;
        safePoint.Y = endCutPoint.Y;
        safePoint.Z = w.Deep + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(safePoint, w.pathAB, true));
    }
    //endregion
    //region顺时针直扁圆母榫刀路,榫宽度大于刀具宽度
    public static void getClockBigStraightFlatHolePathData(ArrayList pointList, Position sidePoint, WasteAndroid w)
    {
        double halfWidthVal = (w.Width - w.cup.KnifeDiameter) / 2;
        double perval = (w.cup.KnifeDiameter / 2);
        if (w.cup.CutPercent != 0.0D)
            perval = w.cup.KnifeDiameter * w.cup.CutPercent;//每一次的进刀量
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标
        double centerWidthX = sidePoint.X - w.Width / 2;//母榫宽度中心X坐标值
        double centerLengthY = sidePoint.Y + w.Thickness / 2;//母榫长度中心Y坐标值
        double Z = sidePoint.Z;
        double stepWidth = perval;//步宽
        double currentWidth = 0.0D;//当前的宽度值;

        Position initP = new Position();
        initP.X = centerWidthX - currentWidth; initP.Y = centerLengthY;
        initP.Z = Z + w.Deep;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));

        Position cutEndPoint = new Position();//车削结束点

        while (sidePoint.Z > targetDeepZ)
        {
            //判断是否满足一个车削深度
            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z); ;
            sidePoint.Z -= w.cup.CutDeep;
            currentWidth = 0.0D; stepWidth = perval;

            while (halfWidthVal > currentWidth)
            {
                //1.开始车削点;2.转向点;3.半圆1;4.转向回程点;5.回程转向点1;6.半圆2;7.回程转向点2;8.深度增加
                //判断是否满足一个车削量
                stepWidth = halfWidthVal - currentWidth > perval ? perval : halfWidthVal - currentWidth;
                currentWidth += stepWidth;

                Position cutStartPoint = new Position();//开始车削点
                cutStartPoint.X = centerWidthX - currentWidth; cutStartPoint.Y = centerLengthY;
                cutStartPoint.Z = cutEndPoint.isZero() ? Z : sidePoint.Z;
                pointList.add(NCParameterHandle.outGoto(cutStartPoint, w.pathAB, true));

                if (cutEndPoint.isZero())
                {
                    cutStartPoint.Z = sidePoint.Z;
                    pointList.add(NCParameterHandle.outGoto(cutStartPoint, w.pathAB, true));
                }

                Position middTurn = new Position();//转向点
                middTurn.X = cutStartPoint.X; middTurn.Y = centerLengthY + w.Thickness / 2 - w.Width / 2; middTurn.Z = cutStartPoint.Z;
                pointList.add(NCParameterHandle.outGoto(middTurn, w.pathAB, true));

                Position turnBack = new Position();//转向回程点
                turnBack.X = centerWidthX + currentWidth; turnBack.Y = middTurn.Y; turnBack.Z = middTurn.Z;

                Position center1 = new Position();//圆心1坐标
                center1.X = centerWidthX; center1.Y = middTurn.Y; center1.Z = middTurn.Z;
                BaseCutCase.getCurveClockPathDataXY( pointList, w.pathAB, middTurn, turnBack, center1, currentWidth, 0);
                pointList.add(NCParameterHandle.outGoto(turnBack, w.pathAB, true));

                Position backTurnPoint1 = new Position();//回程转向点1
                backTurnPoint1.X = turnBack.X; backTurnPoint1.Y = centerLengthY - w.Thickness / 2 + w.Width / 2; backTurnPoint1.Z = turnBack.Z;
                pointList.add(NCParameterHandle.outGoto(backTurnPoint1, w.pathAB, true));

                Position backTurnPoint2 = new Position();//回程转向点2
                backTurnPoint2.X = centerWidthX - currentWidth; backTurnPoint2.Y = backTurnPoint1.Y; backTurnPoint2.Z = backTurnPoint1.Z;

                Position center2 = new Position();//圆心2坐标
                center2.X = centerWidthX; center2.Y = backTurnPoint1.Y; center2.Z = backTurnPoint2.Z;
                BaseCutCase.getCurveClockPathDataXY( pointList, w.pathAB, backTurnPoint1, backTurnPoint2, center2, currentWidth, 0);
                pointList.add(NCParameterHandle.outGoto(backTurnPoint2, w.pathAB, true));
                cutEndPoint = new Position();
                cutEndPoint.X = backTurnPoint2.X; cutEndPoint.Y = centerLengthY; cutEndPoint.Z = backTurnPoint2.Z;
                pointList.add(NCParameterHandle.outGoto(cutEndPoint, w.pathAB, true));
            }

        }

        Position lastPoint = new Position();
        lastPoint.X = cutEndPoint.X; lastPoint.Y = cutEndPoint.Y;
        lastPoint.Z = w.Deep + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(lastPoint, w.pathAB, true));
    }
    //endregion
    //region逆时针直扁圆母榫刀路,榫宽度等于刀具宽度
    public static void getAntiClockStraightFlatHolePathData(ArrayList pointList,Position sidePoint,WasteAndroid w)
    {
        double Z = sidePoint.Z;
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标值
        int i = 1;

        sidePoint.Y += w.cup.KnifeDiameter / 2;

        Position initP = new Position();
        initP.X = sidePoint.X - w.cup.KnifeDiameter / 2;
        initP.Y = sidePoint.Y;
        initP.Z = Z + w.Deep;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));
        Position endCutPoint = new Position();//车削终止点
        while (sidePoint.Z > targetDeepZ)
        {
            int isGoBack = i % 2 == 0 ? -1 : 1;

            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z);
            sidePoint.Z -= w.cup.CutDeep;

            Position startPoint = new Position();//起始点
            startPoint.X = sidePoint.X - w.cup.KnifeDiameter / 2;
            startPoint.Y = endCutPoint.isZero() ? sidePoint.Y : endCutPoint.Y;
            startPoint.Z = endCutPoint.isZero() ? Z : sidePoint.Z;
            pointList.add(NCParameterHandle.outGoto(startPoint, w.pathAB, true));

            if (i == 1)
            {
                startPoint.Z = sidePoint.Z;
                pointList.add(NCParameterHandle.outGoto(startPoint, w.pathAB, true));
            }
            endCutPoint = new Position();
            endCutPoint.X = startPoint.X; endCutPoint.Y = startPoint.Y + isGoBack * (w.Thickness - w.cup.KnifeDiameter);
            endCutPoint.Z = startPoint.Z;
            pointList.add(NCParameterHandle.outGoto(endCutPoint, w.pathAB, true));//车削结束点

            i++;
        }
        Position safePoint = new Position();
        safePoint.X = endCutPoint.X;
        safePoint.Y = endCutPoint.Y;
        safePoint.Z = w.Deep + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(safePoint, w.pathAB, true));
    }
    //endregion
    //region逆时针直扁圆母榫刀路,榫宽度大于刀具宽度
    public static void getAntiClockBigStraightFlatHolePathData( ArrayList pointList, Position sidePoint,WasteAndroid w)
    {
        double halfWidthVal = (w.Width - w.cup.KnifeDiameter) / 2.0D;
        double perval = (w.cup.KnifeDiameter / 2);
        if (w.cup.CutPercent != 0.0D)
            perval = w.cup.KnifeDiameter * w.cup.CutPercent;//每一次的进刀量
        double targetDeepZ = sidePoint.Z - w.Deep;//目标深度Z轴坐标
        double centerWidthX = sidePoint.X - (w.Width / 2);//母榫宽度中心X坐标值
        double centerLengthY = sidePoint.Y + w.Thickness / 2;//母榫长度中心Y坐标值
        double Z = sidePoint.Z;
        double stepWidth = perval;//步宽
        double currentWidth = 0.0D;//当前的宽度值;

        Position initP = new Position();
        initP.X = centerWidthX - currentWidth; initP.Y = centerLengthY;
        initP.Z = Z + w.Deep;
        pointList.add(NCParameterHandle.outPset(initP, w.pathAB, true));

        Position cutEndPoint = new Position();//车削结束点
        while (sidePoint.Z > targetDeepZ)
        {
            //判断是否满足一个车削深度
            w.cup.CutDeep = Math.abs(targetDeepZ - sidePoint.Z) > w.cup.CutDeep ? w.cup.CutDeep : Math.abs(targetDeepZ - sidePoint.Z); ;
            sidePoint.Z -= w.cup.CutDeep;
            currentWidth = 0.0D; stepWidth = perval;

            while (halfWidthVal > currentWidth)
            {
                //1.开始车削点;2.转向点;3.半圆1;4.转向回程点;5.回程转向点1;6.半圆2;7.回程转向点2;8.深度增加
                //判断是否满足一个车削量
                stepWidth = halfWidthVal - currentWidth > perval ? perval : halfWidthVal - currentWidth;
                currentWidth += stepWidth;

                Position cutStartPoint = new Position();//开始车削点
                cutStartPoint.X = centerWidthX - currentWidth; cutStartPoint.Y = centerLengthY;
                cutStartPoint.Z = cutEndPoint.isZero() ? Z : sidePoint.Z;
                pointList.add(NCParameterHandle.outGoto(cutStartPoint, w.pathAB, true));

                if (cutEndPoint.isZero())
                {
                    cutStartPoint.Z = sidePoint.Z;
                    pointList.add(NCParameterHandle.outGoto(cutStartPoint, w.pathAB, true));
                }

                Position middTurn = new Position();//转向点
                middTurn.X = cutStartPoint.X; middTurn.Y = centerLengthY - w.Thickness / 2 + w.Width / 2;
                middTurn.Z = cutStartPoint.Z;
                pointList.add(NCParameterHandle.outGoto(middTurn, w.pathAB, true));

                Position turnBack = new Position();//转向回程点
                turnBack.X = centerWidthX + currentWidth; turnBack.Y = middTurn.Y; turnBack.Z = middTurn.Z;

                Position center1 = new Position();//圆心1坐标
                center1.X = centerWidthX; center1.Y = middTurn.Y; center1.Z = middTurn.Z;
                BaseCutCase.getCurveAntiClockPathDataXY(pointList, w.pathAB, middTurn, turnBack, center1, currentWidth, 0);
                pointList.add(NCParameterHandle.outGoto(turnBack, w.pathAB, true));

                Position backTurnPoint1 = new Position();//回程转向点1
                backTurnPoint1.X = turnBack.X; backTurnPoint1.Y = centerLengthY + w.Thickness / 2 - w.Width / 2;
                backTurnPoint1.Z = turnBack.Z;
                pointList.add(NCParameterHandle.outGoto(backTurnPoint1, w.pathAB, true));

                Position backTurnPoint2 = new Position();//回程转向点2
                backTurnPoint2.X = centerWidthX - currentWidth; backTurnPoint2.Y = backTurnPoint1.Y; backTurnPoint2.Z = backTurnPoint1.Z;

                Position center2 = new Position();//圆心2坐标
                center2.X = centerWidthX; center2.Y = backTurnPoint1.Y; center2.Z = backTurnPoint2.Z;
                BaseCutCase.getCurveAntiClockPathDataXY(pointList, w.pathAB, backTurnPoint1, backTurnPoint2, center2, currentWidth, 0);
                pointList.add(NCParameterHandle.outGoto(backTurnPoint2, w.pathAB, true));

                cutEndPoint = new Position();
                cutEndPoint.X = backTurnPoint2.X; cutEndPoint.Y = centerLengthY;
                cutEndPoint.Z = backTurnPoint2.Z;
                pointList.add(NCParameterHandle.outGoto(cutEndPoint, w.pathAB, true));
            }

        }

        Position lastPoint = new Position();
        lastPoint.X = cutEndPoint.X; lastPoint.Y = cutEndPoint.Y;
        lastPoint.Z = w.Deep + w.zSafeDist;
        pointList.add(NCParameterHandle.outPset(lastPoint, w.pathAB, true));
    }
    //endregion
    //endregion

    //region榫槽刀路
    public static ArrayList GroovePath(ArrayList pointList, SunCaoMode scmode)
    {
        WasteAndroid w = new WasteAndroid();

        double mWidth = scmode.MaterialWidth;
        double mThick = scmode.MaterialThick;
        double gDeep = scmode.GrooveDeep;
        double gWidth = scmode.GrooveThick;
        double gLength = scmode.GrooveWidth;
        double zSafe = scmode.zSafeDist;

        CutParameter cup = new CutParameter();
        cup.KnifeDiameter = scmode.knifeDiameter;
        cup.CutDeep = scmode.cutDepth;
        cup.KnifeThick = scmode.cutThick;
        cup.CutPercent = scmode.cutPercent / 100;

        PathABVal pABV = scmode.pathAB;
        w.CLkd = mWidth;
        w.CLhd = mThick;
        w.Deep = gDeep;
        w.Width = gWidth;
        w.Thickness = gLength;
        w.zSafeDist = zSafe;
        w.cup = cup;
        w.pathAB = pABV;

        Position sidepoint = new Position();
        sidepoint.X = 0; sidepoint.Y = 0; sidepoint.Z = 0;
        if (pABV.ISClockWise == 0)
        {//逆时针
            switch (scmode.grooveType)
            {
                case _GROOVE_A://全开口
                    if (scmode.iCutter == 1)
                    {//钻刀
                        if (w.Width > scmode.knifeDiameter) BaseCutCase.getAntiClockGroovePathData(pointList, sidepoint,w);
                        if (w.Width == scmode.knifeDiameter) BaseCutCase.getAntiOpenGrooveMixPathData(pointList,sidepoint,w);
                    }
                    if (scmode.iCutter == 4)
                    { //锯片
                        if (w.Width > scmode.cutThick) BaseCutCase.getAntiSawBigGroovePathData(pointList,sidepoint, w);
                        if (w.Width == scmode.cutThick) BaseCutCase.getAntiSawGroovepathData(pointList,sidepoint, w);
                    }
                    break;
                case _GROOVE_H_R://半开口方形
                    if (w.Width > scmode.knifeDiameter) BaseCutCase.getAntiClockHalfOpenGroovePathData(pointList,sidepoint, w);
                    if (w.Width == scmode.knifeDiameter) BaseCutCase.getAntiHalfOpenGrooveMixPathData(pointList,sidepoint,w);
                    break;
                case _GROOVE_H_C://半开口圆形
                    if (w.Width > scmode.knifeDiameter) BaseCutCase.getAntiClockHalfOpenGroovePathDataWithCircle(pointList, sidepoint, w);
                    if (w.Width == scmode.knifeDiameter) BaseCutCase.getAntiHalfOpenGrooveMixPathData(pointList, sidepoint,w);
                    break;
                default: break;
            }
        }
        else
        {//顺时针
            switch (scmode.grooveType)
            {
                case _GROOVE_A://全开口
                    if (scmode.iCutter == 1)
                    {//钻刀
                        if (w.Width > scmode.knifeDiameter) BaseCutCase.getClockGroovePathData(pointList,sidepoint,w);
                        if (w.Width == scmode.knifeDiameter) BaseCutCase.getOpenGrooveMixPathData(pointList, sidepoint, w);
                    }
                    if (scmode.iCutter == 4)
                    {//锯片
                        if (w.Width > scmode.cutThick) BaseCutCase.getSawBigGroovePathData( pointList, sidepoint,w);
                        if (w.Width == scmode.cutThick) BaseCutCase.getSawGroovePathData( pointList, sidepoint,w);
                    }
                    break;
                case _GROOVE_H_R://半开口方形
                    if (w.Width > scmode.knifeDiameter) BaseCutCase.getClockHalfOpenGroovePathData( pointList,  sidepoint,w);
                    if (w.Width == scmode.knifeDiameter) BaseCutCase.getHalfOpenGrooveMixPathData( pointList,  sidepoint,w);
                    break;
                case _GROOVE_H_C://半开口圆形
                    if (w.Width > scmode.knifeDiameter) BaseCutCase.getClockHalfOpenGroovePathDataWithCircle( pointList,  sidepoint,w);
                    if (w.Width == scmode.knifeDiameter) BaseCutCase.getHalfOpenGrooveMixPathData( pointList,  sidepoint, w);
                    break;
                default: break;
            }
        }

        pointList.add("safeY:");
        return pointList;
    }
    //endregion

    //region7字形刀路
    /// <summary>
    /// 7字形刀路 逆时针 刀具直径大于宽度 wh
    /// </summary>
    /// <param name="pointList"></param>
    /// <param name="mode"></param>
    public static void RunBig7ShapeAntiClock(ArrayList pointList,Char7Mode mode) {

        double currentDepth = 0; double currentWidth = 0;
        double perval = mode.depthPercent/100 * mode.knifeDiameter;
        double stepWidth = perval;

        Position endCutPoint = new Position();//车削结束点
        while (currentDepth<mode.mtHight)
        {
            //1.安全位置 2.深度点 3.开始车削点 4.7字中间拐点 5.车削结束点 
            int i = 1;
            //判断深度是否满足一个加工量
            mode.cutDepth = mode.mtHight - currentDepth > mode.cutDepth ? mode.cutDepth : mode.mtHight - currentDepth;
            currentDepth += mode.cutDepth;

            //判断宽度是否满足一个加工量
            stepWidth = mode.mtThickness - currentWidth > perval ? perval : mode.mtThickness - currentWidth;

            Position safePoint = new Position();
            safePoint.X = -mode.knifeDiameter/2;
            safePoint.Y = -mode.knifeDiameter; safePoint.Z = mode.mtHight;
            pointList.add(NCParameterHandle.outPset(safePoint, mode.pathAB, true));

            currentWidth = 0; stepWidth = 0;
            while((currentWidth + mode.knifeDiameter) < mode.mtThickness){
                int isGoBack = i % 2 == 0 ? -1 : 1;
                //判断宽度是否满足一个加工量
                if (i > 1) {
                    stepWidth = mode.mtThickness - (currentWidth + mode.knifeDiameter) > perval ? perval :
                            mode.mtThickness - (currentWidth + mode.knifeDiameter);
                    currentWidth += stepWidth;
                }

                Position setDeepPoint = new Position();//深度点
                //三目运算  去程偏置刀具半径+累计槽宽   回程重新定位为7字宽                       
                setDeepPoint.X = isGoBack == 1 ? -mode.knifeDiameter / 2 - currentWidth : -mode.mtWidth;
                //三目运算  去程Y负方向偏置一个刀具半径  回程重新定位7字正方向-累计槽宽
                setDeepPoint.Y = isGoBack == 1 ? safePoint.Y : (mode.mtLength - mode.knifeDiameter / 2) - currentWidth;
                setDeepPoint.Z = -currentDepth;
                if (isGoBack == 1) { pointList.add(NCParameterHandle.outPset(setDeepPoint, mode.pathAB, true)); }
                else { pointList.add(NCParameterHandle.outGoto(setDeepPoint, mode.pathAB, true)); }

                Position startCutPoint = new Position();//开始车削点
                startCutPoint.X = setDeepPoint.X;
                //三目运算 去程Y正方向偏置一个刀具直径  回程继承自上个点位
                startCutPoint.Y = isGoBack == 1 ? setDeepPoint.Y + mode.knifeDiameter : setDeepPoint.Y;
                startCutPoint.Z = setDeepPoint.Z;
                pointList.add(NCParameterHandle.outGoto(startCutPoint, mode.pathAB, true));

                Position goTurnPoint = new Position();//7字中间拐点
                goTurnPoint.X = -mode.knifeDiameter / 2 - currentWidth;
                goTurnPoint.Y = (mode.mtLength - mode.knifeDiameter / 2) - currentWidth;
                goTurnPoint.Z = startCutPoint.Z;
                pointList.add(NCParameterHandle.outGoto(goTurnPoint, mode.pathAB, true));
                //三目运算 去程7字形宽度  回程重新定位       
                endCutPoint.X = isGoBack == 1 ? -mode.mtWidth : -mode.knifeDiameter / 2 - currentWidth;
                //三目运算 去程7字形长度 - 一个刀具半径 回程重新定位
                endCutPoint.Y = isGoBack == 1 ? (mode.mtLength - mode.knifeDiameter / 2) - currentWidth : -mode.knifeDiameter/2;
                endCutPoint.Z = goTurnPoint.Z;
                pointList.add(NCParameterHandle.outGoto(endCutPoint, mode.pathAB, true));//车削停止点
                i++;
            }
            Position outPoint = new Position();
            outPoint.X = endCutPoint.X; outPoint.Y = endCutPoint.Y; outPoint.Z = mode.mtHight;
            pointList.add(NCParameterHandle.outPset(outPoint, mode.pathAB, true));
        }
    }

    /// <summary>
    /// 7字形刀路 顺时针 刀具直径大于宽度  wh
    /// </summary>
    /// <param name="pointList"></param>
    /// <param name="mode"></param>
    public static void RunBig7ShapeClock(ArrayList pointList, Char7Mode mode)
    {
        double currentDepth = 0; double currentWidth = 0;
        double perval = mode.depthPercent/ 100 * mode.knifeDiameter;
        double stepWidth = perval;

        Position endCutPoint = new Position();//车削结束点
        while (currentDepth < mode.mtHight)
        {
            //1.安全位置 2.深度点 3.开始车削点 4.7字中间拐点 5.车削结束点 
            int i = 1;
            //判断深度是否满足一个加工量
            mode.cutDepth = mode.mtHight - currentDepth > mode.cutDepth ? mode.cutDepth : mode.mtHight - currentDepth;
            currentDepth += mode.cutDepth;

            //判断宽度是否满足一个加工量
            stepWidth = mode.mtThickness - currentWidth > perval ? perval : mode.mtThickness - currentWidth;

            Position safePoint = new Position();
            safePoint.X = -mode.mtWidth - mode.knifeDiameter;
            safePoint.Y = (mode.mtLength - mode.knifeDiameter / 2);
            safePoint.Z = mode.mtHight;
            pointList.add(NCParameterHandle.outPset(safePoint, mode.pathAB, true));

            currentWidth = 0; stepWidth = 0;
            while ((currentWidth + mode.knifeDiameter) < mode.mtThickness)
            {
                int isGoBack = i % 2 == 0 ? -1 : 1;
                //判断宽度是否满足一个加工量
                if (i > 1) {
                    stepWidth = mode.mtThickness - (currentWidth + mode.knifeDiameter) > perval ? perval : mode.mtThickness - (currentWidth + mode.knifeDiameter);
                    currentWidth += stepWidth;
                }

                Position setDeepPoint = new Position();//深度点
                setDeepPoint.X = isGoBack == 1 ? safePoint.X : -mode.knifeDiameter/2- currentWidth;
                setDeepPoint.Y = isGoBack == 1 ? (mode.mtLength - mode.knifeDiameter / 2) - currentWidth : 0;
                setDeepPoint.Z = -currentDepth;
                if (isGoBack == 1) { pointList.add(NCParameterHandle.outPset(setDeepPoint, mode.pathAB, true)); }
                else { pointList.add(NCParameterHandle.outGoto(setDeepPoint, mode.pathAB, true)); }

                Position startCutPoint = new Position();//开始车削点
                startCutPoint.X = isGoBack == 1 ? setDeepPoint.X + mode.knifeDiameter : setDeepPoint.X;
                startCutPoint.Y = setDeepPoint.Y;
                startCutPoint.Z = setDeepPoint.Z;
                pointList.add(NCParameterHandle.outGoto(startCutPoint, mode.pathAB, true));

                Position goTurnPoint = new Position();//7字中间拐点
                goTurnPoint.X = -mode.knifeDiameter / 2 - currentWidth;
                goTurnPoint.Y = (mode.mtLength - mode.knifeDiameter / 2) - currentWidth;
                goTurnPoint.Z = startCutPoint.Z;
                pointList.add(NCParameterHandle.outGoto(goTurnPoint, mode.pathAB, true));

                endCutPoint.X = isGoBack == 1 ? -mode.knifeDiameter / 2 - currentWidth : -mode.mtWidth;
                endCutPoint.Y = isGoBack == 1 ? 0 : goTurnPoint.Y;
                endCutPoint.Z = goTurnPoint.Z;
                pointList.add(NCParameterHandle.outGoto(endCutPoint, mode.pathAB, true));
                i++;
            }
            Position outPoint = new Position();
            outPoint.X = endCutPoint.X; outPoint.Y = endCutPoint.Y; outPoint.Z = mode.mtHight;
            pointList.add(NCParameterHandle.outPset(outPoint, mode.pathAB, true));
        }
    }

    /// <summary>
    ///  7字形刀路 逆时针 刀具直径与宽度相同  wh
    /// </summary>
    /// <param name="pointList"></param>
    /// <param name="mode"></param>
    public static void Run7ShapeAntiClock(ArrayList pointList, Char7Mode mode)
    {
        double currentDepth = 0;
        double perval = mode.depthPercent / 100 * mode.knifeDiameter;

        Position safePoint = new Position();
        safePoint.X = -mode.knifeDiameter / 2; safePoint.Y = - mode.knifeDiameter; safePoint.Z = mode.mtHight;
        pointList.add(NCParameterHandle.outPset(safePoint, mode.pathAB, true));

        Position endCutPoint = new Position();//车削结束点
        int i = 1;
        while (currentDepth < mode.mtHight)
        {
            //1.安全位置 2.深度点 3.开始车削点 4.7字中间拐点 5.车削结束点 
            int isGoBack = i % 2 == 0 ? -1 : 1;

            //判断深度是否满足一个加工量
            mode.cutDepth = mode.mtHight - currentDepth > mode.cutDepth ? mode.cutDepth : mode.mtHight - currentDepth;
            currentDepth += mode.cutDepth;

            Position setDeepPoint = new Position();//深度点
            setDeepPoint.X = isGoBack == 1 ? safePoint.X : -mode.mtWidth;
            setDeepPoint.Y = isGoBack == 1 ? safePoint.Y : endCutPoint.Y;
            setDeepPoint.Z = -currentDepth;
            if (isGoBack == 1) { pointList.add(NCParameterHandle.outPset(setDeepPoint, mode.pathAB, true)); }
            else { pointList.add(NCParameterHandle.outGoto(setDeepPoint, mode.pathAB, true)); }

            Position startCutPoint = new Position();//开始车削点
            startCutPoint.X = setDeepPoint.X;
            startCutPoint.Y = isGoBack == 1 ? setDeepPoint.Y + mode.knifeDiameter : setDeepPoint.Y;
            startCutPoint.Z = setDeepPoint.Z;
            pointList.add(NCParameterHandle.outGoto(startCutPoint, mode.pathAB, true));

            Position goTurnPoint = new Position();//7字中间拐点
            goTurnPoint.X = -mode.knifeDiameter / 2;
            goTurnPoint.Y = mode.mtLength - mode.knifeDiameter / 2;
            goTurnPoint.Z = startCutPoint.Z;
            pointList.add(NCParameterHandle.outGoto(goTurnPoint, mode.pathAB, true));

            endCutPoint.X = isGoBack == 1 ? -mode.mtWidth+mode.knifeDiameter/2 : -mode.knifeDiameter / 2;
            endCutPoint.Y = isGoBack == 1 ? mode.mtLength - mode.knifeDiameter / 2 : -mode.knifeDiameter / 2;
            endCutPoint.Z = goTurnPoint.Z;
            pointList.add(NCParameterHandle.outGoto(endCutPoint, mode.pathAB, true));

            i++;
        }
        Position outPoint = new Position();
        outPoint.X = endCutPoint.X; outPoint.Y = endCutPoint.Y; outPoint.Z = mode.mtHight;
        pointList.add(NCParameterHandle.outPset(outPoint, mode.pathAB, true));
    }

    /// <summary>
    /// 7字形刀路 顺时针 刀具直径与宽度相同  wh 
    /// </summary>
    /// <param name="pointList"></param>
    /// <param name="mode"></param>
    public static void Run7ShapeClock(ArrayList pointList, Char7Mode mode)
    {
        double currentDepth = 0;
        double perval = mode.depthPercent / 100 * mode.knifeDiameter;

        Position safePoint = new Position();
        safePoint.X = -mode.mtWidth-mode.knifeDiameter;
        safePoint.Y = mode.mtLength - mode.knifeDiameter/2;
        safePoint.Z = mode.mtHight;
        pointList.add(NCParameterHandle.outPset(safePoint, mode.pathAB, true));

        Position endCutPoint = new Position();//车削结束点
        int i = 1;
        while (currentDepth < mode.mtHight)
        {
            //1.安全位置 2.深度点 3.开始车削点 4.7字中间拐点 5.车削结束点 
            int isGoBack = i % 2 == 0 ? -1 : 1;

            //判断深度是否满足一个加工量
            mode.cutDepth = mode.mtHight - currentDepth > mode.cutDepth ? mode.cutDepth : mode.mtHight - currentDepth;
            currentDepth += mode.cutDepth;

            Position setDeepPoint = new Position();//深度点
            setDeepPoint.X = isGoBack == 1 ? safePoint.X : -mode.knifeDiameter / 2;
            setDeepPoint.Y = isGoBack == 1 ? safePoint.Y : endCutPoint.Y;
            setDeepPoint.Z = -currentDepth;
            if (isGoBack == 1) { pointList.add(NCParameterHandle.outPset(setDeepPoint, mode.pathAB, true)); }
            else { pointList.add(NCParameterHandle.outGoto(setDeepPoint, mode.pathAB, true)); }

            Position startCutPoint = new Position();//开始车削点
            startCutPoint.X = isGoBack == 1 ? setDeepPoint.X + mode.knifeDiameter : setDeepPoint.X;
            startCutPoint.Y = setDeepPoint.Y;
            startCutPoint.Z = setDeepPoint.Z;
            pointList.add(NCParameterHandle.outGoto(startCutPoint, mode.pathAB, true));

            Position goTurnPoint = new Position();//7字中间拐点
            goTurnPoint.X = -mode.knifeDiameter / 2;
            goTurnPoint.Y = mode.mtLength - mode.knifeDiameter / 2;
            goTurnPoint.Z = startCutPoint.Z;
            pointList.add(NCParameterHandle.outGoto(goTurnPoint, mode.pathAB, true));

            endCutPoint.X = isGoBack == 1 ? -mode.knifeDiameter / 2 : -mode.mtWidth-mode.knifeDiameter;
            endCutPoint.Y = isGoBack == 1 ? 0 : mode.mtLength - mode.knifeDiameter / 2;
            endCutPoint.Z = goTurnPoint.Z;
            pointList.add(NCParameterHandle.outGoto(endCutPoint, mode.pathAB, true));

            i++;
        }
        Position outPoint = new Position();
        outPoint.X = endCutPoint.X; outPoint.Y = endCutPoint.Y; outPoint.Z = mode.mtHight;
        pointList.add(NCParameterHandle.outPset(outPoint, mode.pathAB, true));
    }
//endregion

    //regionL形刀路
    //L型刀路 逆时针    wh
    public static void LToolPath_AntiClockwise(ArrayList pointList, LMode mode)
    {
        Position endPos = new Position();//刀具开始坐标
        double leftMargin = mode.leftMargin;//左边距
        double bottomMargin = mode.bottomMargin;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.L_mtLength;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.L_mtWidth;//右边距
        double radius = mode.knifeDiameter / 2;
        //已完成距离
        double finishedBottom = 0, finishedLeft = 0, finishedTop = 0, finishedRight = 0;
        //各边每步进刀量
        double perStepR = 0, perStepT = 0, perStepL = 0, perStepB = 0;

        double cutWidth = mode.knifeDiameter / 2 * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        Position ZsafeStartPos = new Position();//刀具开始坐标
        ZsafeStartPos.X = radius; ZsafeStartPos.Y = -radius - 5; ZsafeStartPos.Z = mode.zSafeDist;
        pointList.add(NCParameterHandle.outPset(ZsafeStartPos, mode.pathAB, true));

        while (finishedDepth < mode.mtHeight)
        {
            Position startPos = new Position();//刀具开始坐标
            Position nextPos = new Position();//刀具下一个坐标
            //已完成距离归零
            finishedBottom = 0; finishedLeft = 0; finishedTop = 0; finishedRight = 0;
            //步长归零
            perStepR = 0; perStepT = 0; perStepL = 0; perStepB = 0;
            //深度判断
            cutDepth = (finishedDepth + cutDepth) <= mode.mtHeight ? cutDepth : (mode.mtHeight - finishedDepth);
            finishedDepth += cutDepth;

            startPos.X += radius; startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, mode.pathAB, true));//安全位置

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, mode.pathAB, true));//预车深度

            startPos.Y += 5;
            pointList.add(NCParameterHandle.outPset(startPos, mode.pathAB, true));//进刀位置
            while ((finishedBottom + _CUTLESS) < bottomMargin || (finishedRight + _CUTLESS) < rightMargin
                    || finishedTop < topMargin || finishedLeft < leftMargin)
            {
                //1右下角坐标
                perStepR = (finishedRight + cutWidth + _CUTLESS) <= rightMargin ? cutWidth : (rightMargin - (finishedRight + _CUTLESS));
                finishedRight += perStepR;
                nextPos.X = startPos.X - finishedRight; nextPos.Y = startPos.Y + finishedBottom; nextPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));

                //2右上角坐标
                perStepT = (finishedTop + cutWidth) <= topMargin ? cutWidth : (topMargin - finishedTop);
                finishedTop += perStepT;
                nextPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));

                //3左上角坐标
                perStepL = (finishedLeft + cutWidth) <= leftMargin ? cutWidth : (leftMargin - finishedLeft);
                finishedLeft += perStepL;
                nextPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));

                //画J
                if (finishedLeft == leftMargin && (finishedRight + _CUTLESS) == rightMargin &&
                        finishedTop == topMargin && (finishedBottom + _CUTLESS) == bottomMargin)
                {
                    //精加工,细车
                    ReTouchJAntiClock(nextPos, pointList, mode);

                    nextPos.X = -(mode.sWidth - leftMargin) - mode.knifeDiameter / 2;//宽度方向退出
                    pointList.add(NCParameterHandle.outPset(nextPos, mode.pathAB, true));

                    nextPos.Y = bottomMargin - mode.knifeDiameter / 2;//长度方向退出
                    pointList.add(NCParameterHandle.outPset(nextPos, mode.pathAB, true));

                    finishedRight = rightMargin - _CUTLESS; finishedBottom = bottomMargin - _CUTLESS;
                }
                else
                {
                    //4左下角
                    perStepB = (finishedBottom + cutWidth + _CUTLESS) < bottomMargin ? cutWidth : (bottomMargin - (finishedBottom + _CUTLESS));
                    finishedBottom += perStepB;

                    nextPos.Y = startPos.Y + finishedBottom;
                    pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));
                }
                nextPos.X = startPos.X - finishedRight;
                pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));
                endPos = nextPos;
            }
        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, mode.pathAB, true));
        pointList.add("safeY:");
    }

    //逆时针细车,描出J形 wh
    private static void ReTouchJAntiClock(Position nextPos, ArrayList pointList, LMode mode)
    {
        double leftMargin = mode.leftMargin;//左边距
        double bottomMargin = mode.bottomMargin;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.L_mtLength;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.L_mtWidth;//右边距

        double yCutL = bottomMargin + mode.L_mtThickness + mode.knifeDiameter / 2;//y
        double xCutEndL = -(rightMargin + mode.L_mtThickness + _CUTLESS + mode.knifeDiameter / 2);//xEnd
        double yCutEndL = mode.sThickness - topMargin;//yend

        nextPos.Y = yCutL + _CUTLESS;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));//J左起

        nextPos.X = xCutEndL;//LXend
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));//J右终

        nextPos.Y = yCutEndL;//LYend
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));//J上终

        nextPos.X += _CUTLESS;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));//J上起

        nextPos.Y = yCutL;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));//J下终

        nextPos.X = -mode.sWidth + leftMargin + mode.L_mtThickness / 2;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));//J左终

        Position centerB = new Position();//圆心点
        centerB.X = nextPos.X; centerB.Y = nextPos.Y - (mode.L_mtThickness + mode.knifeDiameter) / 2;
        centerB.Z = nextPos.Z;

        Position curvBE = new Position();//弧终点
        curvBE.X = centerB.X; curvBE.Y = centerB.Y - (mode.L_mtThickness + mode.knifeDiameter) / 2; curvBE.Z = centerB.Z;
        BaseCutCase.getCurveAntiClockPathDataXY(pointList, mode.pathAB, nextPos, curvBE, centerB, (mode.L_mtThickness + mode.knifeDiameter) / 2, 0);

        nextPos = curvBE;//弧起点
        nextPos.X = -rightMargin - mode.L_mtThickness / 2;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));

        Position centerA = new Position();//圆心点
        centerA.X = nextPos.X; centerA.Y = bottomMargin + mode.L_mtThickness / 2;
        centerA.Z = nextPos.Z;

        Position curvAE = new Position();//弧终点
        curvAE.X = centerA.X + (mode.L_mtThickness + mode.knifeDiameter) / 2; curvAE.Y = centerA.Y; curvAE.Z = centerA.Z;
        BaseCutCase.getCurveAntiClockPathDataXY(pointList, mode.pathAB, nextPos, curvAE, centerA, (mode.L_mtThickness + mode.knifeDiameter) / 2, 0);

        nextPos = curvAE;//弧起点
        nextPos.Y = mode.sThickness - topMargin - mode.L_mtThickness / 2;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));

        Position centerC = new Position();//圆心点
        centerC.X = nextPos.X - (mode.L_mtThickness + mode.knifeDiameter) / 2; centerC.Y = nextPos.Y; centerC.Z = nextPos.Z;

        Position curvCE = new Position();//弧终点
        curvCE.X = centerC.X - (mode.L_mtThickness + mode.knifeDiameter) / 2; curvCE.Y = centerC.Y; curvCE.Z = centerC.Z;
        BaseCutCase.getCurveAntiClockPathDataXY(pointList, mode.pathAB, nextPos, curvCE, centerC, (mode.L_mtThickness + mode.knifeDiameter) / 2, 0);

        double lessW = mode.L_mtWidth - mode.L_mtThickness - _CUTLESS - mode.knifeDiameter;//宽度余量
        double lessL = mode.L_mtLength - mode.L_mtThickness - mode.knifeDiameter - _CUTLESS;//长度余量

        nextPos = curvCE;//切削点
        int i = 1;

        nextPos.X = curvCE.X - (_CUTLESS);
        nextPos.Y = mode.sThickness - topMargin + _CUTLESS;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));
        while (lessW >= 0)
        {
            lessW -= mode.knifeDiameter / 2;
            int isGoBack = i % 2 == 0 ? -1 : 1;
            nextPos.X = nextPos.X - mode.knifeDiameter / 2;
            nextPos.Y = isGoBack == 1 ? mode.sThickness-topMargin+_CUTLESS : nextPos.Y;
            pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));

            nextPos.Y = nextPos.Y - isGoBack * lessL;
            pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));
            i++;
        }
    }
    //L型刀路 顺时针    wh  
    public static void LToolPath_Clockwise(ArrayList pointList, LMode mode)
    {
        Position endPos = new Position();//刀具开始坐标
        double leftMargin = mode.leftMargin;//左边距
        double bottomMargin = mode.bottomMargin;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.L_mtLength;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.L_mtWidth;//右边距
        double radius = mode.knifeDiameter / 2;
        //已完成距离
        double finishedBottom = 0, finishedLeft = 0, finishedTop = 0, finishedRight = 0;
        //各边每步进刀量
        double perStepR = 0, perStepT = 0, perStepL = 0, perStepB = 0;

        double cutWidth = mode.knifeDiameter / 2 * mode.depthPercent / 100;//每刀长度=刀具直径*百分比
        double cutDepth = mode.cutDepth;
        double finishedDepth = 0;//已完成长度(z轴)

        Position ZsafeStartPos = new Position();//刀具开始坐标
        ZsafeStartPos.X = radius; ZsafeStartPos.Y = -radius - 5; ZsafeStartPos.Z = mode.zSafeDist;
        pointList.add(NCParameterHandle.outPset(ZsafeStartPos, mode.pathAB, true));

        while (finishedDepth < mode.mtHeight)
        {
            Position startPos = new Position();//刀具开始坐标
            Position nextPos = new Position();//刀具下一个坐标
            //已完成距离归零
            finishedBottom = 0; finishedLeft = 0; finishedTop = 0; finishedRight = 0;
            //步长归零
            perStepR = 0; perStepT = 0; perStepL = 0; perStepB = 0;
            //深度判断
            cutDepth = (finishedDepth + cutDepth) <= mode.mtHeight ? cutDepth : (mode.mtHeight - finishedDepth);
            finishedDepth += cutDepth;

            startPos.X += radius; startPos.Y = startPos.Y - radius - 5;
            pointList.add(NCParameterHandle.outPset(startPos, mode.pathAB, true));//安全位置

            startPos.Z = startPos.Z - finishedDepth;
            pointList.add(NCParameterHandle.outPset(startPos, mode.pathAB, true)); //设定深度

            startPos.Y += 5;
            pointList.add(NCParameterHandle.outPset(startPos, mode.pathAB, true));//进刀点

            while ((finishedBottom + _CUTLESS) < bottomMargin || finishedLeft < leftMargin ||
                    finishedTop < topMargin || (finishedRight + _CUTLESS) < rightMargin)
            {
                //1右下角
                perStepB = (finishedBottom + cutWidth + _CUTLESS) <= bottomMargin ? cutWidth : (bottomMargin - (finishedBottom + _CUTLESS));
                finishedBottom += perStepB;
                nextPos.X = startPos.X - finishedRight; nextPos.Y = startPos.Y + finishedBottom; nextPos.Z = startPos.Z;
                pointList.add(NCParameterHandle.outPset(nextPos, mode.pathAB, true));

                //2左下角坐标
                perStepL = (finishedLeft + cutWidth) <= leftMargin ? cutWidth : (leftMargin - finishedLeft);
                finishedLeft += perStepL;
                nextPos.X = startPos.X - mode.sWidth - radius * 2 + finishedLeft;
                pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));

                //3左上角坐标
                perStepT = (finishedTop + cutWidth) <= topMargin ? cutWidth : (topMargin - finishedTop);
                finishedTop += perStepT;
                nextPos.Y = startPos.Y + mode.sThickness + radius * 2 - finishedTop;
                pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));

                //4右上角坐标
                perStepR = (finishedRight + cutWidth + _CUTLESS) < rightMargin ? cutWidth : (rightMargin - (finishedRight + _CUTLESS));
                finishedRight += perStepR;
                if (finishedLeft == leftMargin && (finishedRight + _CUTLESS) == rightMargin &&
                        finishedTop == topMargin && (finishedBottom + _CUTLESS) == bottomMargin)
                {   //精细加工,描出J
                    ReTouchJClock(nextPos, pointList, mode);
                    nextPos.Y = mode.sThickness - topMargin + mode.knifeDiameter / 2;//长度方向退出
                    pointList.add(NCParameterHandle.outPset(nextPos, mode.pathAB, true));
                    nextPos.X = -rightMargin + mode.knifeDiameter / 2;//宽度方向退出
                    pointList.add(NCParameterHandle.outPset(nextPos, mode.pathAB, true));

                    finishedRight = rightMargin - _CUTLESS; finishedBottom = bottomMargin - _CUTLESS;
                }
                else
                {
                    nextPos.X = startPos.X - finishedRight;
                    pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));
                }
                nextPos.Y = startPos.Y + finishedBottom;
                pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));
                endPos = nextPos;
            }
        }
        endPos.Z += finishedDepth + mode.zSafeDist;
        pointList.add(NCParameterHandle.outGoto(endPos, mode.pathAB, true));
        pointList.add("safeY:");
    }

    //顺时针细车,描出J形 wh
    private static void ReTouchJClock(Position nextPos, ArrayList pointList, LMode mode)
    {
        double leftMargin = mode.leftMargin;//左边距
        double bottomMargin = mode.bottomMargin;//下边距
        double topMargin = mode.sThickness - bottomMargin - mode.L_mtLength;//上边距
        double rightMargin = mode.sWidth - leftMargin - mode.L_mtWidth;//右边距

        double xCutEndL = -(rightMargin + mode.L_mtThickness + _CUTLESS + mode.knifeDiameter / 2);//xEnd
        double yCutL = bottomMargin + mode.L_mtThickness + mode.knifeDiameter / 2 + _CUTLESS;//y
        double yCutEndL = mode.sThickness - topMargin;//yend

        nextPos.X = xCutEndL;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));//J上起

        nextPos.Y = yCutL;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));//J下终

        nextPos.X = -mode.sWidth + leftMargin;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));//J左终

        nextPos.Y = yCutL - _CUTLESS;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));//J左起

        nextPos.X = xCutEndL + _CUTLESS;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));//J右下终

        nextPos.Y = yCutEndL - mode.L_mtThickness / 2; //弧起点
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));

        Position centerA = new Position();//圆心点
        centerA.X = -rightMargin-mode.L_mtThickness/2; centerA.Y = nextPos.Y; centerA.Z = nextPos.Z;

        Position curvAE = new Position();//弧终点
        curvAE.X = -rightMargin+mode.knifeDiameter/2; curvAE.Y = centerA.Y; curvAE.Z = centerA.Z;
        BaseCutCase.getCurveClockPathDataXY(pointList, mode.pathAB, nextPos, curvAE, centerA, (mode.L_mtThickness + mode.knifeDiameter) / 2, 0);

        nextPos = curvAE;//弧起点
        nextPos.Y = bottomMargin + mode.L_mtThickness / 2;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));

        Position centerB = new Position();//圆心点
        centerB.X = nextPos.X - (mode.L_mtThickness + mode.knifeDiameter) / 2; centerB.Y = nextPos.Y; centerB.Z = nextPos.Z;

        Position curvBE = new Position();//弧终点
        curvBE.X = centerB.X; curvBE.Y = bottomMargin - mode.knifeDiameter / 2; curvBE.Z = centerB.Z;
        BaseCutCase.getCurveClockPathDataXY(pointList, mode.pathAB, nextPos, curvBE, centerB, (mode.L_mtThickness + mode.knifeDiameter) / 2, 0);

        nextPos = curvBE;//弧起点
        nextPos.X = -mode.sWidth + leftMargin + mode.L_mtThickness / 2;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));

        Position centerC = new Position();//圆心点
        centerC.X = nextPos.X; centerC.Y = bottomMargin + mode.L_mtThickness / 2; centerC.Z = nextPos.Z;

        Position curvCE = new Position();//弧终点
        curvCE.X = centerC.X; curvCE.Y = centerC.Y + (mode.L_mtThickness + mode.knifeDiameter) / 2; curvCE.Z = centerC.Z;
        BaseCutCase.getCurveClockPathDataXY(pointList, mode.pathAB, nextPos, curvCE, centerC, (mode.L_mtThickness + mode.knifeDiameter) / 2, 0);

        double lessL = mode.L_mtLength - mode.L_mtThickness - mode.knifeDiameter - _CUTLESS;//长度余量
        double lessW = mode.L_mtWidth - mode.L_mtThickness - mode.knifeDiameter - _CUTLESS;//宽度余量

        nextPos = curvCE;//切削点
        int i = 1;
        nextPos.X = -mode.sWidth + leftMargin - _CUTLESS;
        nextPos.Y = curvCE.Y + _CUTLESS;
        pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));
        while (lessL > 0)
        {
            lessL -= mode.knifeDiameter / 2;
            int isGoBack = i % 2 == 0 ? -1 : 1;

            nextPos.X = isGoBack == 1 ? nextPos.X = -mode.sWidth + leftMargin - _CUTLESS : nextPos.X;
            nextPos.Y = nextPos.Y + mode.knifeDiameter / 2;
            pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));

            nextPos.X = nextPos.X + isGoBack * lessW;
            pointList.add(NCParameterHandle.outGoto(nextPos, mode.pathAB, true));
            i++;
        }
    }
    //endregion
    
    //regionV字形刀路
    //逆时针V字形刀路
    public static void RunVShapeAntiClockPathData(ArrayList result, VMode mode)
    {
        //KnifeTool kt = mode.knife;
        double perval = (mode.depthPercent/100) * mode.knifeDiameter;
        double vWidth = mode.mtWidth; double vDeep = mode.mtDeep; double Zsafe = mode.zSafeDist;
        double currentWidth = 0; double currentDeep = 0; double stepWidth = 0;
        Position startPos = new Position();
        startPos.X = 0; startPos.Y = 0; startPos.Z = 0;
        double centerX = (startPos.X - vWidth) / 2;
        double targetW=(vWidth-mode.knifeDiameter)/2;
        double makeupVal=(Math.sqrt(2)-1)*mode.knifeDiameter/2;//补偿差值,与刀具直径有关

        Position initPoint = new Position();//初始点
        initPoint.X = centerX + perval; initPoint.Y = (startPos.Y - mode.knifeDiameter) / 2; initPoint.Z = startPos.Z + Zsafe;
        result.add(NCParameterHandle.outPset(initPoint, mode.pathAB, true));
        Position endCutPoint = new Position();//结束点
        while (currentDeep < vDeep)
        {
            mode.cutDepth = vDeep - currentDeep > mode.cutDepth ? mode.cutDepth : vDeep - currentDeep;
            currentDeep += mode.cutDepth;
            while (currentWidth < targetW)
            {
                stepWidth = targetW - currentWidth > perval ? perval : targetW - currentWidth;
                currentWidth += stepWidth;

                //1.安全位置  2.中间点 3.结束点
                Position safePoint1 = new Position();//安全位置
                safePoint1.X = centerX + (currentWidth + mode.knifeDiameter / 2) - makeupVal; safePoint1.Y = initPoint.Y; safePoint1.Z = -currentDeep;
                result.add(NCParameterHandle.outPset(safePoint1, mode.pathAB, true));

                Position middlePoint = new Position();//中间点
                middlePoint.X = centerX; middlePoint.Y = currentWidth - makeupVal; middlePoint.Z = safePoint1.Z;
                result.add(NCParameterHandle.outGoto(middlePoint, mode.pathAB, true));

                endCutPoint = new Position();//结束点
                endCutPoint.X = centerX - (currentWidth + mode.knifeDiameter / 2) + makeupVal; endCutPoint.Y = initPoint.Y; endCutPoint.Z = middlePoint.Z;
                result.add(NCParameterHandle.outGoto(endCutPoint, mode.pathAB, true));
            }
        }
        Position lastPoint = endCutPoint;
        lastPoint.Z = Zsafe;
        result.add(NCParameterHandle.outPset(lastPoint, mode.pathAB, true));

    }

    //顺时针V字形刀路
    public static void RunVShapeClockPathData(ArrayList result, VMode mode)
    {
     //   KnifeTool kt = mode.knife;
        double vWidth = mode.mtWidth; double vDeep = mode.mtDeep; double Zsafe = mode.zSafeDist;
        double perval = (mode.depthPercent / 100) * mode.knifeDiameter;
        double currentWidth = 0; double currentDeep = 0; double stepWidth = 0;
        Position startPos = new Position();
        startPos.X = 0; startPos.Y = 0; startPos.Z = 0;
        double centerX = (startPos.X - vWidth) / 2;
        double targetW = (vWidth - mode.knifeDiameter) / 2;
        double makeupVal = (Math.sqrt(2) - 1) * mode.knifeDiameter / 2;//补偿差值,与刀具直径有关

        Position initPoint = new Position();//初始点
        initPoint.X = centerX - perval; initPoint.Y = (startPos.Y - mode.knifeDiameter) / 2; initPoint.Z = startPos.Z + Zsafe;
        result.add(NCParameterHandle.outPset(initPoint, mode.pathAB, true));
        Position endCutPoint = new Position();//结束点
        while (currentDeep < vDeep)
        {
            mode.cutDepth = vDeep - currentDeep > mode.cutDepth ? mode.cutDepth : vDeep - currentDeep;
            currentDeep += mode.cutDepth;
            while (currentWidth < targetW)
            {
                stepWidth = targetW - currentWidth > perval ? perval : targetW - currentWidth;
                currentWidth += stepWidth;

                //1.安全位置 3.中间点 4.结束点 
                Position safePoint1 = new Position();//安全位置
                safePoint1.X = centerX - (currentWidth+mode.knifeDiameter/2) + makeupVal; safePoint1.Y = initPoint.Y; safePoint1.Z = -currentDeep;
                result.add(NCParameterHandle.outPset(safePoint1, mode.pathAB, true));

                Position middlePoint = new Position();//中间点
                middlePoint.X = centerX; middlePoint.Y = currentWidth - makeupVal; middlePoint.Z = safePoint1.Z;
                result.add(NCParameterHandle.outGoto(middlePoint, mode.pathAB, true));

                endCutPoint = new Position();//结束点
                endCutPoint.X = centerX + (currentWidth + mode.knifeDiameter / 2) - makeupVal; endCutPoint.Y = initPoint.Y; endCutPoint.Z = middlePoint.Z;
                result.add(NCParameterHandle.outGoto(endCutPoint, mode.pathAB, true));
            }
        }
        Position lastPoint = endCutPoint;
        lastPoint.Z = Zsafe;
        result.add(NCParameterHandle.outPset(lastPoint, mode.pathAB, true));
    }

//endregion

}
