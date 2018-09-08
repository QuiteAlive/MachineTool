package cn.qzd.machinetool.GetNcFile;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


import cn.qzd.machinetool.LitepalBase.QM_MT_KnifePoint;
import cn.qzd.machinetool.LitepalBase.QM_MT_UserLatheCode;
import cn.qzd.machinetool.SunMaoData;
import cn.qzd.machinetool.base.MachineFileContent;
import cn.qzd.machinetool.helper.SQLHandle;

/**
 * Created by admin on 2018/6/22.
 */

public class GetNCFile {
    private double x1, y1, z1;
    private NCParameterHandle ncParameterHandle;
    private ArrayList lstFileContent;
    private SharedPreferences pref;

    //Y轴安全距离
    public static String safeY(MachineFileContent content, int cutter)
    {
        double L = content.tL;
        double Y = content.tY;
        double safeY = 0;// content.tL[cutter];
        switch (cutter) {
            case 1:
                safeY = Math.sqrt(Y * Y + L * L) + Y + 20;
                break;
            case 2:
                safeY = Math.sqrt(Y * Y + L * L) + Y + 25;
                break;
            case 4:
                safeY = Math.sqrt(Y * Y + L * L) + Y + 40;
                break;
            default:
                break;
        }
        String strSafeY = roundByScale(safeY,3);
        if(!strSafeY.contains(".")) strSafeY += ".";
        return strSafeY;
    }
    //Z轴安全距离
    public static String safeZ(MachineFileContent content, int cutter, String CLHD)
    {
        double safeZ = content.tL;
        switch (cutter)
        {
            case 1:
                safeZ += (20 + Double.valueOf(CLHD));
                break;
            case 2:
                safeZ += (25 + Double.valueOf(CLHD));
                break;
            case 4:
                safeZ += (40 + Double.valueOf(CLHD));
                break;
            default:
                break;
        }
        String strSafeZ = roundByScale(safeZ,3);
        if (!strSafeZ.contains(".")) strSafeZ += ".";
        return strSafeZ;
    }

    //region  普通五轴机床文件
    public String GetNCFile_c(ArrayList<String> lstFileContent, Context context,String CLHD,int cb_flag) {
        pref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String fileid = pref.getString("fileid","");
        String biasdata = pref.getString("Biased_information", "");
        biasdata = biasdata.replaceAll("([\\]\\[])", "");
        String[] biaslsxy = biasdata.split(",");
        ArrayList lstFileContentNew = new ArrayList();
        MachineFileContent content = new MachineFileContent();
        //sqliteG代码
        List GCode=new ArrayList();
        List<QM_MT_UserLatheCode> list3= LitePal.select("codetype,number")
                .where("FileId = ?",fileid)
                .find(QM_MT_UserLatheCode.class);
        for (QM_MT_UserLatheCode gcode:list3) {
            GCode.add(gcode.getCodeType());
            GCode.add(gcode.getNumber());
        }
        content.mSpindleOff = Integer.valueOf(GCode.get(49).toString());
        content.gMotionRapid = Integer.valueOf(GCode.get(1).toString());
        content.oChangeCutter = GCode.get(56).toString();
        content.oSpindleSpeed = GCode.get(60).toString();
        content.mSpindleOnCLW = Integer.valueOf(GCode.get(7).toString());
        content.gMotionLinear = Integer.valueOf(GCode.get(3).toString());
        content.oCutSpeed = GCode.get(58).toString();
        String messageShow = "ok";//导出成功
        String strTmp = "";//当前行
        int iCutter = 0;
        int iNewCutter = 0;
        boolean brealFirstPos = true;//真实开始切
        boolean bFirstPos = true;
        boolean bFirstCutter = true;//是否第一次使用刀
        boolean bFirstAB = true;//是否第一次AB轴
        boolean bChangeCutter = false;//是否换刀
        boolean bSpindleStoped = true;
        double cutSpeed = 0;
        double spindalSpeed = 0;//主轴转速
        double angleA = 0;//A轴角度
        double angleB = 0;//B轴角度
        double angleA1 = 0;//A轴弧度
        double angleB1 = 0;//B轴弧度
        int lastMoveCode = -1;
        double firstangleA = 1987;
        double firstangleB = 1987;
        int firsttool = -1;
        int indexz = -1;
        int indexy = -1;
        boolean bFirstPath = true;//第一条刀路
        String safey = biaslsxy[4];
        String safez = biaslsxy[5];
        boolean flag = true;


        for (int i = 0; i < lstFileContent.size(); i++) {
            strTmp = lstFileContent.get(i);
            if (strTmp.indexOf("cutter:") != -1) {
                int index1 = strTmp.indexOf(':');
                int index2 = strTmp.length();
                int type = Integer.parseInt(String.valueOf(strTmp.substring(index1 + 1, index2)));
                iNewCutter = type;
                if (type == 1) {
                    content.tL = Double.valueOf(biaslsxy[0]);
                    content.tX = Double.valueOf(biaslsxy[2]);
                    content.tY = Double.valueOf(biaslsxy[3]);
                    content.tS = Double.valueOf(biaslsxy[1]);
                } else if (type == 2) {
                    content.tL = Double.valueOf(biaslsxy[12]);
                    content.tX = Double.valueOf(biaslsxy[14]);
                    content.tY = Double.valueOf(biaslsxy[15]);
                    content.tS = Double.valueOf(biaslsxy[13]);
                } else if (type == 4) {
                    content.tL = Double.valueOf(biaslsxy[6]);
                    content.tX = Double.valueOf(biaslsxy[8]);
                    content.tY = Double.valueOf(biaslsxy[9]);
                    content.tS = Double.valueOf(biaslsxy[7]);
                }

                if (iNewCutter < 0) {
                    lstFileContentNew.clear();
                }
                if (bFirstCutter) {
                    firsttool = 0;
                    bFirstCutter = false;
                } else {
                    lstFileContentNew.add(String.format("M%02d", content.mSpindleOff));//%04d
                    bSpindleStoped = true;//主轴停止
                }
                bChangeCutter = true;
            }
            if (strTmp.indexOf("AB:") != -1) {
                if (bFirstAB) {
                    bFirstAB = false;
                } else {
                    //SafeY:一
                    //快速直线定位
                  //  lstFileContentNew.add(String.format("G0%d Y%s", content.gMotionRapid, safeY(content, iCutter)));
                }
                if (bChangeCutter) {
                    if (iCutter != iNewCutter) {

                        iCutter = iNewCutter;
                        //换刀
                        lstFileContentNew.add(String.format("%s%02d", content.oChangeCutter, iCutter));
                    }
                    //***************仿真时使用 实际机床加工不需要*********************\\
                    if (cb_flag==1) {
                        lstFileContentNew.add("M06");
                        int coordinate = 0;
                        if (iCutter == 1)
                            coordinate = 54;
                        else if (iCutter == 2)
                            coordinate = 55;
                        else if (iCutter == 4)
                            coordinate = 56;
                        lstFileContentNew.add(String.format("G00 G90 G%d", coordinate));
                    }
                    //***************仿真时使用 实际机床加工不需要*********************\\

                    bChangeCutter = false;
                }
                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.indexOf(" ", index1 + 1);
                int index3 = strTmp.length();
                String strtempA = strTmp.substring(index1 + 1, index2);
                String strtempB = strTmp.substring(index2 + 1, index3);
                angleA = Double.valueOf(strtempA);
                angleB = Double.valueOf(strtempB);
                angleA1 = angleA * Math.PI / 180;
                angleB1 = angleB * Math.PI / 180;


                String strAngleA = roundByScale(angleA, 3);
                String strAngleB = roundByScale(angleB, 3);
                if (!strAngleA.contains(".")) strAngleA += ".";
                if (!strAngleB.contains(".")) strAngleB += ".";
                if (firsttool == iNewCutter && firstangleA == angleA && firstangleB == angleB) {
                    if (indexz != -1) {
                        lstFileContentNew.remove(indexz);
                    }
                    if (indexy != -1) {
                        lstFileContentNew.remove(indexy);
                    }
                    flag = false;
                } else {
                    flag = true;
                    firstangleA = angleA;
                    firstangleB = angleB;
                    firsttool = iNewCutter;
                    lstFileContentNew.add(String.format("A%s B%s", strAngleA, strAngleB));
                  //  lstFileContentNew.add(String.format("G%02d Y%s", content.gMotionRapid, GetNCFile.safeY(content, iCutter), GetNCFile.safeZ(content, iCutter,CLHD)));
                }
                lastMoveCode = content.gMotionRapid;//最后一次G代码(例如:快速直线定位)
                bFirstPos = true;                   //是否第一次坐标
            }
            if (strTmp.indexOf("cutSpeed:") != -1) {
                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.length();
                cutSpeed = Double.parseDouble(strTmp.substring(index1 + 1, index2));
            }
            if (strTmp.indexOf("spindleSpeed:") != -1) {
                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.length();
                spindalSpeed = Double.parseDouble(String.valueOf(strTmp.substring(index1 + 1, index2)));
            }
            if (strTmp.indexOf("pset:") != -1) {
                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.indexOf(" ", index1 + 1);
                int index3 = strTmp.indexOf(" ", index2 + 1);
                int index4 = strTmp.length();
                double x = Double.parseDouble(String.valueOf(strTmp.substring(index1 + 1, index2)));
                double y = Double.parseDouble(String.valueOf(strTmp.substring(index2 + 1, index3)));
                double z = Double.parseDouble(String.valueOf(strTmp.substring(index3 + 1, index4)));


                x += (content.tS + content.tX + content.tY * Math.sin(angleA1) - content.tX * Math.cos(angleA1) - content.tS * Math.cos(angleA1) * Math.cos(angleB1) + content.tL * Math.cos(angleA1) * Math.sin(angleB1));
                y += (content.tY - content.tY * Math.cos(angleA1) - content.tX * Math.sin(angleA1) - content.tS * Math.sin(angleA1) * Math.cos(angleB1) + content.tL * Math.sin(angleA1) * Math.sin(angleB1));
                z += (content.tS * Math.sin(angleB1) + content.tL * Math.cos(angleB1));

                String strX = roundByScale(x, 3);
                String strY = roundByScale(y, 3);
                String strZ = roundByScale(z, 3);

                if (bFirstPos && bFirstPath)//第一个刀路
                {
                    bFirstPos = false;
                    bFirstPath = false;
                    if (angleB >= -90 && angleB <= 90) {
                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {
                                lstFileContentNew.add(String.format("X%s Y%s %s%f M%02d", strX, strY, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
                                lstFileContentNew.add(String.format("G%02d X%s Y%s %s%f M%s", content.gMotionRapid,strX, strY, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Z%s", strZ));
                            bSpindleStoped = false;
                        }
                    } else if (angleB < -90 || angleB > 90) {
                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {
                               // lstFileContentNew.add(String.format("G%02d Y%s ", content.gMotionRapid, safeY(content, iCutter)));
                                lstFileContentNew.add(String.format("X%s Z%s %s%f M%s", strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
                               // lstFileContentNew.add(String.format("G%02d Y%s ", content.gMotionRapid, safeY(content, iCutter)));
                                lstFileContentNew.add(String.format("G%02d X%s Z%s %s%f M%s", content.gMotionRapid, strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Y%s", strY));
                            bSpindleStoped = false;
                        }
                    }

                } else if (bFirstPos == true && bFirstPath == false) //不是第一个刀路
                {
                    bFirstPos = false;
                    if (angleB >= -90 && angleB <= 90 && firstangleA != angleA && firstangleB == angleB) {

                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {

                               // lstFileContentNew.add(String.format("G%02d Y%s ", content.gMotionRapid, safeY(content, iCutter)));
                                lstFileContentNew.add(String.format("X%s Z%s %s%f M%s", strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
                               // lstFileContentNew.add(String.format("G%02d Y%s ", content.gMotionRapid, safeY(content, iCutter)));
                                lstFileContentNew.add(String.format("G%02d X%s Z%s %s%f M%s", content.gMotionRapid, strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Y%s", strY));
                            bSpindleStoped = false;
                        }
                    } else if (angleB >= -90 && angleB <= 90 && firstangleA == angleA && firstangleB != angleB) {

                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {

                              //  lstFileContentNew.add(String.format("G%02d Z%s", content.gMotionRapid, safeZ(content, iCutter, CLHD)));
                                lstFileContentNew.add(String.format("X%s Y%s %s%f M%s", strX, strY, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
                               // lstFileContentNew.add(String.format("G%02d Z%s", content.gMotionRapid, safeZ(content, iCutter, CLHD)));
                                lstFileContentNew.add(String.format("G%02d X%s Y%s %s%f M%s", content.gMotionRapid, strX, strY, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Z%s", strZ));
                            bSpindleStoped = false;
                        }
                    } else if ((firstangleA < -90 || firstangleB > 90) && angleB >= -90 && angleB <= 90) {
                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {
//                                lstFileContentNew.add(String.format("G%02d Y%s ", content.gMotionRapid, safeY(content, iCutter)));
//                                lstFileContentNew.add(String.format("G%02d Z%s", content.gMotionRapid, safeZ(content, iCutter, CLHD)));
                                lstFileContentNew.add(String.format("X%s Y%s %s%f M%s", strX, strY, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
//                                lstFileContentNew.add(String.format("G%02d Y%s ", content.gMotionRapid, safeY(content, iCutter)));
//                                lstFileContentNew.add(String.format("G%02d Z%s", content.gMotionRapid, safeZ(content, iCutter, CLHD)));
                                lstFileContentNew.add(String.format("G%02d X%s Y%s %s%f M%s", content.gMotionRapid, strX, strY, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Z%s", strZ));
                            bSpindleStoped = false;
                        }
                    } else if ((angleB < -90 || angleB > 90) && firstangleA >= -90 && firstangleB <= 90) {
                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {
//                                lstFileContentNew.add(String.format("G%02d Y%s ", content.gMotionRapid, safeY(content, iCutter)));
//                                lstFileContentNew.add(String.format("G%02d Z%s", content.gMotionRapid, safeZ(content, iCutter, CLHD)));
                                lstFileContentNew.add(String.format("X%s Z%s %s%f M%s", strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
//                                lstFileContentNew.add(String.format("G%02d Y%s ", content.gMotionRapid, safeY(content, iCutter)));
//                                lstFileContentNew.add(String.format("G%02d Z%s", content.gMotionRapid, safeZ(content, iCutter, CLHD)));
                                lstFileContentNew.add(String.format("G%02d X%s Z%s %s%f M%s", content.gMotionRapid, strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Y%s", strY));
                            bSpindleStoped = false;
                        }
                    }
                    if ((angleB >= -90 && angleB <= 90) && firstangleA >= -90 && firstangleB <= 90)
                    {
                        if (bSpindleStoped)
                        {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {
                               // lstFileContentNew.add(String.format("G%02d Z%s", content.gMotionRapid, safeZ(content, iCutter, CLHD)));
                                lstFileContentNew.add(String.format("X%s Z%s %s%f M%s", strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            else
                            {
                               // lstFileContentNew.add(String.format("G%02d Z%s", content.gMotionRapid, safeZ(content, iCutter, CLHD)));
                                lstFileContentNew.add(String.format("G%02d X%s Z%s %s%f M%s", content.gMotionRapid, strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Y%s", strY));
                            bSpindleStoped = false;
                        }
                    }
                    if ((firstangleB >= -90 || firstangleB <= 90) && angleB >= -180 && angleB < -90 || angleB > 90 && angleB <= 180 )
                    {
                        if (bSpindleStoped)
                        {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位';
                            {
                                lstFileContentNew.add(String.format("X%s Z%s %s%f M%s", strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            else
                            {
                                lstFileContentNew.add(String.format("G%02d X%s Z%s %s%f M%s", content.gMotionRapid, strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Y%s", strY));
                            bSpindleStoped = false;
                        }
                    }

                     if (angleB >= -90 && angleB <= 90) {
                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {
                                if (firstangleA != angleA || firstangleB != angleB) {/* lstFileContentNew.add(string.Format("{0}", safez));*/ }
                                lstFileContentNew.add(String.format("X%s Z%s %s%f M%s", strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
                                if (firstangleA != angleA || firstangleB != angleB) { /*lstFileContentNew.add(string.Format("{0}", safez)); */}
                                lstFileContentNew.add(String.format("G%02d X%s Z%s %s%f M%s", content.gMotionRapid, strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Y%s", strY));
                            bSpindleStoped = false;
                        }
                    }

                    firstangleA = angleA;
                    firstangleB = angleB;
                    firsttool = iNewCutter;
                } else if (lastMoveCode == content.gMotionRapid) {
                    lstFileContentNew.add(String.format("X%s Y%s Z%s", strX, strY, strZ));
                } else {
                    lastMoveCode = content.gMotionRapid;//直线快速定位
                    lstFileContentNew.add(String.format("G%02d X%s Y%s Z%s", content.gMotionRapid, strX, strY, strZ));
                }
            }
            if (strTmp.indexOf("goto:") != -1)
            {
                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.indexOf(" ", index1 + 1);
                int index3 = strTmp.indexOf(" ", index2 + 1);
                int index4 = strTmp.length();
                double x = Double.parseDouble(String.valueOf(strTmp.substring(index1 + 1, index2 )));
                double y = Double.parseDouble(String.valueOf(strTmp.substring(index2 + 1, index3 )));
                double z = Double.parseDouble(String.valueOf(strTmp.substring(index3 + 1, index4 )));

                x += (content.tS + content.tX + content.tY * Math.sin(angleA1) - content.tX * Math.cos(angleA1) - content.tS * Math.cos(angleA1) * Math.cos(angleB1) + content.tL * Math.cos(angleA1) * Math.sin(angleB1));
                y += (content.tY - content.tY * Math.cos(angleA1) - content.tX * Math.sin(angleA1) - content.tS * Math.sin(angleA1) * Math.cos(angleB1) + content.tL * Math.sin(angleA1) * Math.sin(angleB1));
                z += (content.tS * Math.sin(angleB1) + content.tL * Math.cos(angleB1));

                String strX = roundByScale(x,3);
                String strY = roundByScale(y,3);
                String strZ = roundByScale(z,3);


                if (brealFirstPos)
                {
                    brealFirstPos = false;

                    if (lastMoveCode == content.gMotionRapid)//直线快速定位
                    {
                        lstFileContentNew.add(String.format("G%02d X%s Y%s Z%s %s%f", content.gMotionLinear,strX, strY, strZ,content.oCutSpeed, cutSpeed));
                        lastMoveCode = content.gMotionLinear;//直线插补,切削进给
                    }
                }
                else
                {
                    if (lastMoveCode == content.gMotionLinear)//直线插补,切削进给
                    {
                        lstFileContentNew.add(String.format(" X%s Y%s Z%s", strX, strY, strZ));
                    }
                    else
                    {
                        lastMoveCode = content.gMotionLinear;//直线插补,切削进给
                        lstFileContentNew.add(String.format("G%d X%s Y%s Z%s", content.gMotionLinear, strX, strY, strZ));
                    }
                }
            }
            if (strTmp.indexOf("safeY:") != -1)
            {
                lstFileContentNew.add(String.format("%s", safey));
                indexy = lstFileContentNew.lastIndexOf(String.format("%s", safey));
                lstFileContentNew.add(String.format("%s", safez));
                indexz = lstFileContentNew.lastIndexOf(String.format("%s", safez));
                firstangleA = angleA;
                firstangleB = angleB;
                bFirstPos = true;
                brealFirstPos = true;
                firsttool = iNewCutter;
                lastMoveCode = content.gMotionRapid;
            }
//            else
//            {
//                lstFileContentNew.add(strTmp);
//            }
        }
        lstFileContent.clear();
        String top = pref.getString("top", "").replaceAll("([+*/^()\\]\\[])", "") + "\n";
        String bottom = "\n" + pref.getString("bottom", "").replaceAll("([+*/^()\\]\\[])", "");
        return String.valueOf(top + ArrayList2String(lstFileContentNew) + bottom);
    }
    //endregion


    //region  RTCP五轴机床文件
    public String NORTCPGetNCFile_c(ArrayList<String> lstFileContent, Context context) {
        pref = context.getSharedPreferences("thickness", Context.MODE_PRIVATE);
        String thickness = pref.getString("thickness", "");
        double stockthickness = SunMaoData.CalculationFunctionNew(thickness);
        pref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String fileid = pref.getString("fileid","");
        //sqlite安全代码
        List safeyz=new ArrayList();
        List<QM_MT_KnifePoint> safe_list= LitePal.select("safey,safez")
                .where("FileId = ?",fileid)
                .find(QM_MT_KnifePoint.class);
        for (QM_MT_KnifePoint knifePoint:safe_list) {
            safeyz.add(knifePoint.getSafey());
            safeyz.add(knifePoint.getSafez());
        }
        ArrayList lstFileContentNew = new ArrayList();
        MachineFileContent content = new MachineFileContent();
        //sqliteG代码
        List GCode=new ArrayList();
        List<QM_MT_UserLatheCode> list3= LitePal.select("codetype,number")
                .where("FileId = ?",fileid)
                .find(QM_MT_UserLatheCode.class);
        for (QM_MT_UserLatheCode gcode:list3) {
            GCode.add(gcode.getCodeType());
            GCode.add(gcode.getNumber());
        }
        content.mSpindleOff = Integer.valueOf(GCode.get(49).toString());
        content.gMotionRapid = Integer.valueOf(GCode.get(1).toString());
        content.oChangeCutter = GCode.get(56).toString();
        content.oSpindleSpeed = GCode.get(60).toString();
        content.mSpindleOnCLW = Integer.valueOf(GCode.get(7).toString());
        content.gMotionLinear = Integer.valueOf(GCode.get(3).toString());
        content.oCutSpeed = GCode.get(58).toString();
        String messageShow = "ok";//导出成功
        String strTmp = "";//当前行
        int iCutter = 0;
        int iNewCutter = 0;
        boolean brealFirstPos = true;//真实开始切
        boolean bFirstPos = true;
        boolean bFirstCutter = true;//是否第一次使用刀
        boolean bFirstAB = true;//是否第一次AB轴
        boolean bChangeCutter = false;//是否换刀
        boolean bSpindleStoped = true;
        double cutSpeed = 0;
        double spindalSpeed = 0;//主轴转速
        double angleA = 0;//A轴角度
        double angleB = 0;//B轴角度
        double angleA1 = 0;//A轴弧度
        double angleB1 = 0;//B轴弧度
        int lastMoveCode = -1;
        double firstangleA = 1987;
        double firstangleB = 1987;
        int firsttool = 0;
        int indexz = 0;
        int indexy = 0;
        int safe = 0;
        boolean flag = true;
        int indextool = -1;
        boolean bFirstPath = true;//第一条刀路

        for (int i = 0; i < lstFileContent.size(); i++) {
            strTmp = lstFileContent.get(i);
            if (strTmp.indexOf("cutter:") != -1) {
                int index1 = strTmp.indexOf(':');
                int index2 = strTmp.length();
                int type = Integer.parseInt(String.valueOf(strTmp.substring(index1 + 1, index2)));


                iNewCutter = type;
                if (iNewCutter < 0) {
                    lstFileContentNew.clear();
                }
                if (bFirstCutter) {
                    firsttool = 0;
                    bFirstCutter = false;
                } else {
                    lstFileContentNew.add(String.format("M%02d", content.mSpindleOff));//%04d
                    bSpindleStoped = true;//主轴停止
                }
                bChangeCutter = true;
            }
            if (strTmp.indexOf("AB:") != -1) {
                if (bFirstAB) {
                    bFirstAB = false;
                } else {
                    //SafeY:一
                    //快速直线定位
                   // lstFileContentNew.add(String.format("G0%d Y%s", content.gMotionRapid, safeY(content, iCutter)));
                }
                if (bChangeCutter) {
                    if (iCutter != iNewCutter) {

                        iCutter = iNewCutter;
                        //换刀
                        lstFileContentNew.add(String.format("%s%02d", content.oChangeCutter, iCutter));
                        indextool = lstFileContentNew.lastIndexOf(String.format("%s%02d", content.oChangeCutter, iCutter));
                    }

                    bChangeCutter = false;
                }

                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.indexOf(" ", index1 + 1);
                int index3 = strTmp.length();
                String strtempA = strTmp.substring(index1 + 1, index2);
                String strtempB = strTmp.substring(index2 + 1, index3);
                angleA = Double.valueOf(strtempA);
                angleB = Double.valueOf(strtempB);
                angleA1 = angleA * Math.PI / 180;
                angleB1 = angleB * Math.PI / 180;


                String strAngleA = roundByScale(angleA, 3);
                String strAngleB = roundByScale(angleB, 3);
                if (!strAngleA.contains(".")) strAngleA += ".";
                if (!strAngleB.contains(".")) strAngleB += ".";
                if (firsttool == iNewCutter && firstangleA == angleA && firstangleB == angleB)
                {
                    if (indextool != -1)
                    {
                        lstFileContentNew.remove(indextool);
                    }
                    if (indexz != -1)
                    {
                        lstFileContentNew.remove(indexz);
                    }
                    if (indexy != -1)
                    {
                        lstFileContentNew.remove(indexy);
                    }
                    if (safe != -1)
                    {
                        lstFileContentNew.remove(safe);
                    }
                    flag = false;
                } else {
                    firstangleA = angleA;
                    firstangleB = angleB;
                    firsttool = iNewCutter;
                    lstFileContentNew.add(String.format("G00 G90 A%s B%s", strAngleA, strAngleB));
                    lstFileContentNew.add(String.format("%s","G9998"));
                    //  lstFileContentNew.add(String.format("G%02d Y%s", content.gMotionRapid, GetNCFile.safeY(content, iCutter), GetNCFile.safeZ(content, iCutter,CLHD)));
                }
                lastMoveCode = content.gMotionRapid;//最后一次G代码(例如:快速直线定位)
                bFirstPos = true;                   //是否第一次坐标
            }
            if (strTmp.indexOf("cutSpeed:") != -1) {
                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.length();
                cutSpeed = Double.parseDouble(strTmp.substring(index1 + 1, index2));
            }
            if (strTmp.indexOf("spindleSpeed:") != -1) {
                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.length();
                spindalSpeed = Double.parseDouble(String.valueOf(strTmp.substring(index1 + 1, index2)));
            }
            if (strTmp.indexOf("pset:") != -1) {
                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.indexOf(" ", index1 + 1);
                int index3 = strTmp.indexOf(" ", index2 + 1);
                int index4 = strTmp.length();
                double x = Double.parseDouble(String.valueOf(strTmp.substring(index1 + 1, index2)));
                double y = Double.parseDouble(String.valueOf(strTmp.substring(index2 + 1, index3)));
                double z = Double.parseDouble(String.valueOf(strTmp.substring(index3 + 1, index4)));


                String strX = roundByScale(x, 3);
                String strY = roundByScale(y, 3);
                String strZ = roundByScale(z, 3);

                if (bFirstPos && bFirstPath)//第一个刀路
                {
                    bFirstPos = false;
                    bFirstPath = false;
                    if (angleB >= -90 && angleB <= 90) {
                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {
                                lstFileContentNew.add(String.format("X%s Y%s %s%f M%02d", strX, strY, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
                                lstFileContentNew.add(String.format("G%02d X%s Z%s %s%f M%s", content.gMotionRapid, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Z%s", strZ));
                            bSpindleStoped = false;
                        }
                    } else if (angleB < -90 || angleB > 90) {
                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {
                               // lstFileContentNew.add(String.format("%s ",  safeyz.get(0)));
                                lstFileContentNew.add(String.format("X%s Z%s %s%f M%s", strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
                               // lstFileContentNew.add(String.format("%s ",  safeyz.get(0)));
                                lstFileContentNew.add(String.format("G%02d X%s Z%s %s%f M%s", content.gMotionRapid, strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Y%s", strY));
                            bSpindleStoped = false;
                        }
                    }

                } else if (bFirstPos == true && bFirstPath == false) //不是第一个刀路
                {
                    bFirstPos = false;
                    if (angleB >= -90 && angleB <= 90 && firstangleA != angleA && firstangleB == angleB) {

                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {

                               // lstFileContentNew.add(String.format("%s ",  safeyz.get(0)));
                                lstFileContentNew.add(String.format("X%s Z%s %s%f M%s", strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
                                //lstFileContentNew.add(String.format("%s ", safeyz.get(0)));
                                lstFileContentNew.add(String.format("G%02d X%s Z%s %s%f M%s", content.gMotionRapid, strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Y%s", strY));
                            bSpindleStoped = false;
                        }
                    } else if (angleB >= -90 && angleB <= 90 && firstangleA == angleA && firstangleB != angleB) {

                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {

                                //lstFileContentNew.add(String.format("%s", safeyz.get(1)));
                                lstFileContentNew.add(String.format("X%s Y%s %s%f M%s", strX, strY, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
                                //lstFileContentNew.add(String.format("%s",  safeyz.get(1)));
                                lstFileContentNew.add(String.format("G%02d X%s Y%s %s%f M%s", content.gMotionRapid, strX, strY, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Z%s", strZ));
                            bSpindleStoped = false;
                        }
                    } else if ((firstangleA < -90 || firstangleB > 90) && angleB >= -90 && angleB <= 90) {
                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {
                               /* lstFileContentNew.add(String.format("%s ",  safeyz.get(0)));
                                lstFileContentNew.add(String.format("%s",  safeyz.get(1)));*/
                                lstFileContentNew.add(String.format("X%s Y%s %s%f M%s", strX, strY, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
                               /* lstFileContentNew.add(String.format("%s ",  safeyz.get(0)));
                                lstFileContentNew.add(String.format("%s",  safeyz.get(1)));*/
                                lstFileContentNew.add(String.format("G%02d X%s Y%s %s%f M%s", content.gMotionRapid, strX, strY, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Z%s", strZ));
                            bSpindleStoped = false;
                        }
                    } else if ((angleB < -90 || angleB > 90) && firstangleA >= -90 && firstangleB <= 90) {
                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {
                                /*lstFileContentNew.add(String.format("%s ",  safeyz.get(0)));
                                lstFileContentNew.add(String.format("%s",  safeyz.get(1)));*/
                                lstFileContentNew.add(String.format("X%s Z%s %s%f M%s", strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
                                /*lstFileContentNew.add(String.format("%s ",  safeyz.get(0)));
                                lstFileContentNew.add(String.format("%s",  safeyz.get(1)));*/
                                lstFileContentNew.add(String.format("G%02d X%s Z%s %s%f M%s", content.gMotionRapid, strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Y%s", strY));
                            bSpindleStoped = false;
                        }
                    }
                    if ((angleB >= -90 && angleB <= 90) && firstangleA >= -90 && firstangleB <= 90)
                    {
                        if (bSpindleStoped)
                        {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {
                                //lstFileContentNew.add(String.format("%s", safeyz.get(1)));
                                lstFileContentNew.add(String.format("X%s Y%s %s%f M%s", strX, strY, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            else
                            {
                                //lstFileContentNew.add(String.format("%s", safeyz.get(1)));
                                lstFileContentNew.add(String.format("G%02d X%s Y%s %s%f M%s", content.gMotionRapid, strX, strY, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Y%s", strY));
                            bSpindleStoped = false;
                        }
                    }

                    firstangleA = angleA;
                    firstangleB = angleB;
                    firsttool = iNewCutter;
                } else if (lastMoveCode == content.gMotionRapid) {
                    lstFileContentNew.add(String.format("X%s Y%s Z%s", strX, strY, strZ));
                } else {
                    lastMoveCode = content.gMotionRapid;//直线快速定位
                    lstFileContentNew.add(String.format("G%02d X%s Y%s Z%s", content.gMotionRapid, strX, strY, strZ));
                }
            }
            if (strTmp.indexOf("goto:") != -1)
            {
                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.indexOf(" ", index1 + 1);
                int index3 = strTmp.indexOf(" ", index2 + 1);
                int index4 = strTmp.length();
                double x = Double.parseDouble(String.valueOf(strTmp.substring(index1 + 1, index2 )));
                double y = Double.parseDouble(String.valueOf(strTmp.substring(index2 + 1, index3 )));
                double z = Double.parseDouble(String.valueOf(strTmp.substring(index3 + 1, index4 )));

//                x += (content.tS + content.tX + content.tY * Math.sin(angleA1) - content.tX * Math.cos(angleA1) - content.tS * Math.cos(angleA1) * Math.cos(angleB1) + content.tL * Math.cos(angleA1) * Math.sin(angleB1));
//                y += (content.tY - content.tY * Math.cos(angleA1) - content.tX * Math.sin(angleA1) - content.tS * Math.sin(angleA1) * Math.cos(angleB1) + content.tL * Math.sin(angleA1) * Math.sin(angleB1));
//                z += (content.tS * Math.sin(angleB1) + content.tL * Math.cos(angleB1));

                String strX = roundByScale(x,3);
                String strY = roundByScale(y,3);
                String strZ = roundByScale(z,3);


                if (brealFirstPos)
                {
                    brealFirstPos = false;

                    if (lastMoveCode == content.gMotionRapid)//直线快速定位
                    {
                        lstFileContentNew.add(String.format("G%02d X%s Y%s Z%s %s%f", content.gMotionLinear,strX, strY, strZ,content.oCutSpeed, cutSpeed));
                        lastMoveCode = content.gMotionLinear;//直线插补,切削进给
                    }
                }
                else
                {
                    if (lastMoveCode == content.gMotionLinear)//直线插补,切削进给
                    {
                        lstFileContentNew.add(String.format(" X%s Y%s Z%s", strX, strY, strZ));
                    }
                    else
                    {
                        lastMoveCode = content.gMotionLinear;//直线插补,切削进给
                        lstFileContentNew.add(String.format("G%d X%s Y%s Z%s", content.gMotionLinear, strX, strY, strZ));
                    }
                }
            }
            if (strTmp.indexOf("safeY:") != -1)
            {
                lstFileContentNew.add(String.format("%s", "G9999"));
                safe = lstFileContentNew.lastIndexOf("G9999");
                lstFileContentNew.add(String.format("%s",  safeyz.get(0)));
                indexy = lstFileContentNew.lastIndexOf(String.format("%s", safeyz.get(0)));
                lstFileContentNew.add(String.format("%s",  safeyz.get(1)));
                indexz = lstFileContentNew.lastIndexOf(String.format("%s", safeyz.get(1)));

                firstangleA = angleA;
                firstangleB = angleB;
                bFirstPos = true;
                brealFirstPos = true;
                lastMoveCode = content.gMotionRapid;
            }
//            else
//            {
//                lstFileContentNew.add(strTmp);
//            }
        }
        lstFileContent.clear();
        String top = pref.getString("top", "").replaceAll("([+*/^()\\]\\[])", "") + "\n";
        String bottom = "\n" + pref.getString("bottom", "").replaceAll("([+*/^()\\]\\[])", "");
        return String.valueOf(top + ArrayList2String(lstFileContentNew) + bottom);
    }
    //endregion


    //region  X6榫头机机床文件
    public String XSIXGetNCFile_c(ArrayList<String> lstFileContent, Context context,String CLHD) {
        pref = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String fileid = pref.getString("fileid","");
        String biasdata = pref.getString("Biased_information", "");
        biasdata = biasdata.replaceAll("([\\]\\[])", "");
        String[] biaslsxy = biasdata.split(",");
        ArrayList lstFileContentNew = new ArrayList();
        MachineFileContent content = new MachineFileContent();
        //sqliteG代码
        List GCode=new ArrayList();
        List<QM_MT_UserLatheCode> list3= LitePal.select("codetype,number")
                .where("FileId = ?",fileid)
                .find(QM_MT_UserLatheCode.class);
        for (QM_MT_UserLatheCode gcode:list3) {
            GCode.add(gcode.getCodeType());
            GCode.add(gcode.getNumber());
        }
        content.mSpindleOff = Integer.valueOf(GCode.get(71).toString());
        content.gMotionRapid = Integer.valueOf(GCode.get(1).toString());
        content.oChangeCutter = GCode.get(96).toString();
        content.oSpindleSpeed = GCode.get(100).toString();
        content.mSpindleOnCLW = Integer.valueOf(GCode.get(7).toString());
        content.gMotionLinear = Integer.valueOf(GCode.get(3).toString());
        content.oCutSpeed = GCode.get(98).toString();
        content.gleftbyProces = Integer.valueOf(GCode.get(51).toString());
        content.gelevatebyProces = Integer.valueOf(GCode.get(53).toString());
        content.gdjosebyProces = Integer.valueOf(GCode.get(55).toString());
        content.grightbyProces = Integer.valueOf(GCode.get(57).toString());
        content.gleftKeepoff = Integer.valueOf(GCode.get(43).toString());content.mleftFootloosen = Integer.valueOf(GCode.get(73).toString());
        content.gelevateKeepoff = Integer.valueOf(GCode.get(45).toString());content.mcentreFootloosen = Integer.valueOf(GCode.get(75).toString());
        content.gdjoseKeepoff = Integer.valueOf(GCode.get(47).toString()); content.mcentreFootloosen = Integer.valueOf(GCode.get(75).toString());
        content.grightKeepoff = Integer.valueOf(GCode.get(49).toString());  content.mrightFootloosen = Integer.valueOf(GCode.get(77).toString());
        String messageShow = "ok";//导出成功
        String strTmp = "";//当前行
        int iCutter = 0;
        int iNewCutter = 0;
        boolean brealFirstPos = true;//真实开始切
        boolean bFirstPos = true;
        boolean bFirstCutter = true;//是否第一次使用刀
        boolean bFirstAB = true;//是否第一次AB轴
        boolean bChangeCutter = false;//是否换刀
        boolean bSpindleStoped = true;
        double cutSpeed = 0;
        double spindalSpeed = 0;//主轴转速
        int angleA = 0;//A轴角度
        double angleB = 0;//B轴角度
        double angleA1 = 0;//A轴弧度
        double angleB1 = 0;//B轴弧度
        int lastMoveCode = -1;
        double firstangleA = 1987;
        double firstangleB = 1987;
        int firsttool = 0;
        int indexcut = -1;
        int indextool = -1;
        int indexup = -1;
        int indexplace = -1;
        int keepoff = -1;
        int losser = -1;
        int indexangle = -1;
        int indexbyProces = -1;
        boolean bFirstPath = true;//第一条刀路
        String safey = biaslsxy[4];
        String safez = biaslsxy[5];
        for (int i = 0; i < lstFileContent.size(); i++) {
            strTmp = lstFileContent.get(i);
            if (strTmp.indexOf("cutter:") != -1) {
                int index1 = strTmp.indexOf(':');
                int index2 = strTmp.length();
                int type = Integer.parseInt(String.valueOf(strTmp.substring(index1 + 1, index2)));
                iNewCutter = type;

                if (iNewCutter < 0) {
                    lstFileContentNew.clear();
                }
                if (bFirstCutter) {
                    firsttool = 0;
                    bFirstCutter = false;
                } else {
                    lstFileContentNew.add(String.format("M%02d", content.mSpindleOff));//%04d
                    indexcut = lstFileContentNew.lastIndexOf(String.format("M%02d", content.mSpindleOff));
                    lstFileContentNew.add(String.format("M%d", losser));
                    indexplace = lstFileContentNew.lastIndexOf(String.format("M%d", losser));
                    bSpindleStoped = true;//主轴停止
                }
                bChangeCutter = true;
            }
            if (strTmp.indexOf("AB:") != -1) {
                if (bFirstAB) {
                    bFirstAB = false;
                }
                if (bChangeCutter) {
                    if (iCutter != iNewCutter) {
                        lstFileContentNew.add(String.format("%s","G28 G91 Z0"));
                        indexup = lstFileContentNew.lastIndexOf(String.format("%s", "G28 G91 Z0"));
                        iCutter = iNewCutter;
                        int byProces = 0;
                        switch (angleA)
                        {
                            case 1: byProces = content.gleftbyProces; break;
                            case 2: byProces = content.gelevatebyProces; break;
                            case 3: byProces = content.gdjosebyProces; break;
                            case 4: byProces = content.grightbyProces; break;
                        }
                        lstFileContentNew.add(String.format("G%s", byProces));
                        indexbyProces = lstFileContentNew.lastIndexOf(String.format("G%s", byProces));
                        //换刀
                        lstFileContentNew.add(String.format("%s%02d", content.oChangeCutter, iCutter));
                    }

                    bChangeCutter = false;
                }

                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.indexOf(" ", index1 + 1);
                int index3 = strTmp.length();
                String strtempA = strTmp.substring(index1 + 1, index2);
                String strtempB = strTmp.substring(index2 + 1, index3);
                angleA = Integer.valueOf(strtempA);
                angleB = Double.valueOf(strtempB);
                //angleA1 = angleA * Math.PI / 180;
                angleB1 = angleB * Math.PI / 180;

                String strAngleA = roundByScale(angleA, 3);
                String strAngleB = roundByScale(angleB, 3);
               // if (!strAngleA.contains(".")) strAngleA += ".";
                if (!strAngleB.contains(".")) strAngleB += ".";
                switch (angleA)
                {
                    case 1: keepoff = content.gleftKeepoff; losser = content.mleftFootloosen; break;
                    case 2: keepoff = content.gelevateKeepoff; losser = content.mcentreFootloosen; break;
                    case 3: keepoff = content.gdjoseKeepoff; losser = content.mcentreFootloosen; break;
                    case 4: keepoff = content.grightKeepoff; losser = content.mrightFootloosen; break;
                }
                lstFileContentNew.add(String.format("G%s R%s", keepoff, strAngleB));
                indexangle = lstFileContentNew.lastIndexOf(String.format("G%s R%s", keepoff, strAngleB));
                if (firsttool == iNewCutter && firstangleB == angleB)
                {
                    if (indextool != -1)//t
                    {
                        lstFileContentNew.remove(indextool);
                    }
                    if (indexbyProces != -1)//g201
                    {
                        lstFileContentNew.remove(indexbyProces);
                    }
                    if (indexup != -1)//g28g91z0
                    {
                        lstFileContentNew.remove(indexup);
                    }
                    if (indexangle != -1)
                    {
                        lstFileContentNew.remove(indexangle);//jiaodu
                    }
                    if (indexplace != -1)//M161
                    {
                        lstFileContentNew.remove(indexplace);
                    }
                    if (indexcut != -1)//m05
                    {
                        lstFileContentNew.remove(indexcut);
                    }
                }
                else if (firsttool != iNewCutter && firstangleB == angleB)
                {
                    if (indexplace != -1)//M161
                    {
                        lstFileContentNew.remove(indexplace);
                    }
                }
                lastMoveCode = content.gMotionRapid;//最后一次G代码(例如:快速直线定位)
                bFirstPos = true;                   //是否第一次坐标
            }
            if (strTmp.indexOf("cutSpeed:") != -1) {
                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.length();
                cutSpeed = Double.parseDouble(strTmp.substring(index1 + 1, index2));
            }
            if (strTmp.indexOf("spindleSpeed:") != -1) {
                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.length();
                spindalSpeed = Double.parseDouble(String.valueOf(strTmp.substring(index1 + 1, index2)));
            }
            if (strTmp.indexOf("pset:") != -1) {
                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.indexOf(" ", index1 + 1);
                int index3 = strTmp.indexOf(" ", index2 + 1);
                int index4 = strTmp.length();
                double x = Double.parseDouble(String.valueOf(strTmp.substring(index1 + 1, index2)));
                double y = Double.parseDouble(String.valueOf(strTmp.substring(index2 + 1, index3)));
                double z = Double.parseDouble(String.valueOf(strTmp.substring(index3 + 1, index4)));


                x += (content.tS + content.tX + content.tY * Math.sin(angleA1) - content.tX * Math.cos(angleA1) - content.tS * Math.cos(angleA1) * Math.cos(angleB1) + content.tL * Math.cos(angleA1) * Math.sin(angleB1));
                y += (content.tY - content.tY * Math.cos(angleA1) - content.tX * Math.sin(angleA1) - content.tS * Math.sin(angleA1) * Math.cos(angleB1) + content.tL * Math.sin(angleA1) * Math.sin(angleB1));
                z += (content.tS * Math.sin(angleB1) + content.tL * Math.cos(angleB1));

                String strX = roundByScale(x, 3);
                String strY = roundByScale(y, 3);
                String strZ = roundByScale(z, 3);

                if (bFirstPos && bFirstPath)//第一个刀路
                {
                    bFirstPos = false;
                    bFirstPath = false;
                } else if (bFirstPos == true && bFirstPath == false) //不是第一个刀路
                {
                    bFirstPos = false;
                        if (bSpindleStoped) {
                            if (lastMoveCode == content.gMotionRapid)//直线快速定位
                            {

                                //lstFileContentNew.add(String.format("G%02d Y%s ", content.gMotionRapid, safeY(content, iCutter)));
                                lstFileContentNew.add(String.format("X%s Z%s %s%f M%s", strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            } else {
                               // lstFileContentNew.add(String.format("G%02d Y%s ", content.gMotionRapid, safeY(content, iCutter)));
                                lstFileContentNew.add(String.format("G%02d X%s Z%s %s%f M%s", content.gMotionRapid, strX, strZ, content.oSpindleSpeed, spindalSpeed, content.mSpindleOnCLW));
                            }
                            lastMoveCode = content.gMotionRapid;//直线快速定位
                            lstFileContentNew.add(String.format("Y%s", strY));
                            bSpindleStoped = false;
                        }
                    firstangleB = angleB;
                    firsttool = iNewCutter;
                } else if (lastMoveCode == content.gMotionRapid) {
                    lstFileContentNew.add(String.format("X%s Y%s Z%s", strX, strY, strZ));
                } else {
                    lastMoveCode = content.gMotionRapid;//直线快速定位
                    lstFileContentNew.add(String.format("G%02d X%s Y%s Z%s", content.gMotionRapid, strX, strY, strZ));
                }
            }
            if (strTmp.indexOf("goto:") != -1)
            {
                int index1 = strTmp.indexOf(":");
                int index2 = strTmp.indexOf(" ", index1 + 1);
                int index3 = strTmp.indexOf(" ", index2 + 1);
                int index4 = strTmp.length();
                double x = Double.parseDouble(String.valueOf(strTmp.substring(index1 + 1, index2 )));
                double y = Double.parseDouble(String.valueOf(strTmp.substring(index2 + 1, index3 )));
                double z = Double.parseDouble(String.valueOf(strTmp.substring(index3 + 1, index4 )));

                String strX = roundByScale(x,3);
                String strY = roundByScale(y,3);
                String strZ = roundByScale(z,3);


                if (brealFirstPos)
                {
                    brealFirstPos = false;

                    if (lastMoveCode == content.gMotionRapid)//直线快速定位
                    {
                        lstFileContentNew.add(String.format("G%02d X%s Y%s Z%s %s%f", content.gMotionLinear,strX, strY, strZ,content.oCutSpeed, cutSpeed));
                        lastMoveCode = content.gMotionLinear;//直线插补,切削进给
                    }
                }
                else
                {
                    if (lastMoveCode == content.gMotionLinear)//直线插补,切削进给
                    {
                        lstFileContentNew.add(String.format(" X%s Y%s Z%s", strX, strY, strZ));
                    }
                    else
                    {
                        lastMoveCode = content.gMotionLinear;//直线插补,切削进给
                        lstFileContentNew.add(String.format("G%d X%s Y%s Z%s", content.gMotionLinear, strX, strY, strZ));
                    }
                }
            }
            if (strTmp.indexOf("safeY:") != -1)
            {
//
                firstangleA = angleA;
                firstangleB = angleB;
                firsttool = iNewCutter;
                bFirstPos = true;
                brealFirstPos = true;
                lastMoveCode = content.gMotionRapid;
            }
        }
        lstFileContent.clear();
        String top = pref.getString("top", "").replaceAll("([+*/^()\\]\\[])", "") + "\n";
        String bottom = "\n" + pref.getString("bottom", "").replaceAll("([+*/^()\\]\\[])", "");
        return String.valueOf(top + ArrayList2String(lstFileContentNew) + bottom);
    }
    //endregion



    //region other
    public static String ArrayList2String(ArrayList<String> arrayList) {
        String result = "";
        if (arrayList != null && arrayList.size() > 0) {
            for (String item : arrayList) {
                // 把列表中的每条数据用逗号分割开来，然后拼接成字符串
                result += item + "\n";
            }
            // 去掉最后一个逗号
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static String ArrayList3String(ArrayList<String> arrayList) {
        String result = "";
        if (arrayList != null && arrayList.size() > 0) {
            for (String item : arrayList) {
                // 把列表中的每条数据用逗号分割开来，然后拼接成字符串
                result += item + ",";
            }
            // 去掉最后一个逗号
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static String roundByScale(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        if (scale == 0) {
            return new DecimalFormat("0").format(v);
        }
        String formatStr = "0.";
        for (int i = 0; i < scale; i++) {
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }
    //endregion
}
