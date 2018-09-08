package cn.qzd.machinetool.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.qzd.machinetool.DataActivity;
import cn.qzd.machinetool.FileIO.Fileservice;
import cn.qzd.machinetool.GetNcFile.GetNCFile;
import cn.qzd.machinetool.GetNcFile.NCParameterHandle;
import cn.qzd.machinetool.GetNcFile.SunMaoDaoLuMode;
import cn.qzd.machinetool.LitepalBase.QM_MT_ABaxisVal;
import cn.qzd.machinetool.LitepalBase.QM_MT_Knifeproperty_path_R;
import cn.qzd.machinetool.LitepalBase.QM_MT_SunMao_Path_R;
import cn.qzd.machinetool.SunMaoData;
import cn.qzd.machinetool.base.Char7Mode;
import cn.qzd.machinetool.base.LMode;
import cn.qzd.machinetool.base.MuYuanSunMode;
import cn.qzd.machinetool.base.PathABVal;
import cn.qzd.machinetool.base.PathToolSetup;
import cn.qzd.machinetool.base.Position;
import cn.qzd.machinetool.base.SpindleAB;
import cn.qzd.machinetool.base.SunCaoMode;
import cn.qzd.machinetool.base.VMode;
import cn.qzd.machinetool.base.ZhiBianMuYuanMode;
import cn.qzd.machinetool.base.ZhiBianYuanMode;
import cn.qzd.machinetool.base.ZhiFangMode;
import cn.qzd.machinetool.base.ZhiLineMode;
import cn.qzd.machinetool.R;
import cn.qzd.machinetool.base.ZhiUMode;
import cn.qzd.machinetool.base.ZhiYuanMode;
import cn.qzd.machinetool.helper.Loading_view;
import cn.qzd.machinetool.helper.SQLHandle;
import cn.qzd.machinetool.util.PreferencesUtils;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.tokenizer.ParseException;

import static cn.qzd.machinetool.SunMaoData.CalculationFunctionNew;
import static cn.qzd.machinetool.SunMaoData.GetValByPathParaNew;
import static cn.qzd.machinetool.SunMaoData.sunmaono;


/**
 * Created by admin on 2018/5/25.
 */

public class ToolFragment extends Fragment {
    private Loading_view loading;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    private TextView ImageView2;
    private EditText T1_Diameter, T1_Depth, T1_Speed, T1_Rpm,T1_Percent, T2_Diameter, T2_Depth, T2_Speed, T2_Percent, T2_Rpm, T4_Diameter, T4_Percent, T4_Speed, T4_Rpm, T4_Thickness;
    private Button daolu, daolu1, daolu2;
    private LinearLayout ll_T1_Diameter, ll_T1_Depth, ll_T1_Speed, ll_T1_Rpm, ll_T2_Diameter, ll_T2_Depth, ll_T2_Speed, ll_T2_Percent, ll_T2_Rpm, ll_T4_Diameter, ll_T4_Percent, ll_T4_Speed, ll_T4_Rpm, ll_T4_Thickness, ll_knife_zd, ll_knife_lxd, ll_knife_jp;
    private View rootView;
    private String[] allsonid1, childno;
    private String pathname;
    private String newstat, ProfileFormula, ProfileScope1, ProfileScope2, AaxleFormula, AaxleScope1, AaxleScope2, BaxleFormula, BaxleScope1, BaxleScope2, StartDot, knifename;
    //private static SQLHandle sqlHandle;
    private List patharray, newlist;
    private int ISRad1Check, ISRad2Check, ISRad3Check, ISClockWise, SunMao_Path_ID, iCutter;
    private NCParameterHandle ncParameterHandle;
    private ArrayList lstFileContent;
    GetNCFile getNCFile;
    PathABVal pathABVal;
    private double cutSpeed, spindleSpeed, knifeDiameter, Depth, Percent;
    private RadioButton cb_test,cb_real;
    private int cb_falg=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tool_fragment, container, false);
        initView();//
        //sqlHandle=new SQLHandle();
        newlist = new ArrayList();
        patharray = new ArrayList();
        getNCFile = new GetNCFile();
        //pathname子榫卯的代号集

        String all = pathname.replaceAll("([\\]\\[])", "");
        allsonid1 = all.split(",");
        childno = pathname.split(",");
        if (childno.length == 1) {
            daolu.setText(allsonid1[0]);
            daolu1.setVisibility(View.GONE);
            daolu2.setVisibility(View.GONE);
        } else if (childno.length == 2) {
            daolu2.setVisibility(View.GONE);
            daolu.setText(allsonid1[0]);
            daolu1.setText(allsonid1[1]);
        } else if (childno.length == 3) {
            daolu.setText(allsonid1[0]);
            daolu1.setText(allsonid1[1]);
            daolu2.setText(allsonid1[2]);
        }

        //sqlite查询查询组合榫或者单榫的所有path_r_id
        List allpath_r_id = new ArrayList();
        for (int i = 0; i < childno.length; i++) {
            //sqlite获取一个榫卯里面的所有刀路
            List allpath = new ArrayList();
            List<QM_MT_SunMao_Path_R> list = LitePal.select("pathname")
                    .where("sunmaonameno = ?", childno[i].trim())
                    .find(QM_MT_SunMao_Path_R.class);
            for (QM_MT_SunMao_Path_R path_rh : list) {
                allpath.add(path_rh.getPathName());
            }
            for (int j = 0; j < allpath.size(); j++) {
                //一个榫卯的path_r_id
                List<QM_MT_SunMao_Path_R> list1 = LitePal.select("path_r_id")
                        .where("sunmaonameno = ? and pathname=?", childno[i].trim(), allpath.get(j).toString())
                        .find(QM_MT_SunMao_Path_R.class);
                for (QM_MT_SunMao_Path_R path_rh : list1) {
                    allpath_r_id.add(path_rh.getPath_r_id());
                }
            }
        }
        //查找组合榫或者单榫的道具名称以及默认数据
        List allknife = new ArrayList();
        for (int k = 0; k < allpath_r_id.size(); k++) {
            List<QM_MT_Knifeproperty_path_R> list2 = LitePal.select("knifename,size")
                    .where("sunmao_path_id = ?", String.valueOf(allpath_r_id.get(k)))
                    .find(QM_MT_Knifeproperty_path_R.class);
            for (QM_MT_Knifeproperty_path_R knife_path : list2) {
                allknife.add(knife_path.getKnifeName());
                allknife.add(knife_path.getSize());
            }
        }
        // List allknife=sqlHandle.select_KnideName1(fsunmaono);
        if (allknife.contains("钻刀")) {
            Log.d("knife", "钻刀");
            ll_knife_zd.setVisibility(View.VISIBLE);
            int indexzd = allknife.indexOf("钻刀");
            Log.d("第一次出现", String.valueOf(indexzd));
            //List knifeinfo//sqlHandle.select_Knideinfo(fsunmaono,"钻刀");
            T1_Diameter.setText(allknife.get(indexzd + 1).toString());
            T1_Depth.setText(allknife.get(indexzd + 3).toString());
            T1_Percent.setText(allknife.get(indexzd + 5).toString());
            T1_Speed.setText(allknife.get(indexzd + 7).toString());
            T1_Rpm.setText(allknife.get(indexzd + 9).toString());
        }
        if (allknife.contains("螺旋刀")) {
            Log.d("knife", "螺旋刀");
            ll_knife_lxd.setVisibility(View.VISIBLE);
            int indexlxd = allknife.indexOf("螺旋刀");
            // List knifeinfo1=sqlHandle.select_Knideinfo(fsunmaono,"螺旋刀");
            T2_Diameter.setText(allknife.get(indexlxd + 1).toString());
            T2_Depth.setText(allknife.get(indexlxd + 3).toString());
            T2_Percent.setText(allknife.get(indexlxd + 5).toString());
            T2_Speed.setText(allknife.get(indexlxd + 7).toString());
            T2_Rpm.setText(allknife.get(indexlxd + 9).toString());

        }
        if (allknife.contains("锯片")) {
            Log.d("knife", "锯片");
            ll_knife_jp.setVisibility(View.VISIBLE);
            int indexjp = allknife.indexOf("锯片");
            //List knifeinfo2=sqlHandle.select_Knideinfo(fsunmaono,"锯片");
            T4_Diameter.setText(allknife.get(indexjp + 1).toString());
            T4_Speed.setText(allknife.get(indexjp + 3).toString());
            T4_Percent.setText(allknife.get(indexjp + 5).toString());
            T4_Rpm.setText(allknife.get(indexjp + 7).toString());

        }
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        daolu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onclick", "1" + newlist.toString());
                ExportData(childno[0].trim(),1);
                Log.d("onclick", "2" + newlist.toString());
            }
        });

        daolu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExportData(childno[1].trim(),2);
            }
        });

        daolu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExportData(childno[2].trim(),3);
            }
        });
    }

    public void initView() {
        ImageView2 = (TextView) rootView.findViewById(R.id.ImageView5);
        T1_Diameter = (EditText) rootView.findViewById(R.id.et_T1_Diameter);//t1直径
        T1_Depth = (EditText) rootView.findViewById(R.id.et_T1_Depth);//t1切削深度
        T1_Speed = (EditText) rootView.findViewById(R.id.et_T1_Speed);//t1切削速度
        T1_Rpm = (EditText) rootView.findViewById(R.id.et_T1_Rpm);//t1主轴转速
        T1_Percent = (EditText) rootView.findViewById(R.id.et_T1_Percent);//切削百分比
        T2_Diameter = (EditText) rootView.findViewById(R.id.et_T2_Diameter);//t2直径
        T2_Depth = (EditText) rootView.findViewById(R.id.et_T2_Depth);//t2切削深度
        T2_Speed = (EditText) rootView.findViewById(R.id.et_T2_Speed);//t2切削速度
        T2_Percent = (EditText) rootView.findViewById(R.id.et_T2_Percent);//t2切削百分比
        T2_Rpm = (EditText) rootView.findViewById(R.id.et_T2_Rpm);//t2主轴转速
        T4_Diameter = (EditText) rootView.findViewById(R.id.et_T4_Diameter);//t4直径
        T4_Speed = (EditText) rootView.findViewById(R.id.et_T4_Speed);//t4切削速度
        T4_Percent = (EditText) rootView.findViewById(R.id.et_T4_Percent);//t4切削百分比
        T4_Rpm = (EditText) rootView.findViewById(R.id.et_T4_Rpm);//主轴转速
        T4_Thickness = (EditText) rootView.findViewById(R.id.et_T4_Thickness);//T4厚度
        daolu = (Button) rootView.findViewById(R.id.data_daolu);
        daolu1 = (Button) rootView.findViewById(R.id.data_daolu1);
        daolu2 = (Button) rootView.findViewById(R.id.data_daolu2);
        ll_knife_zd = (LinearLayout) rootView.findViewById(R.id.knife_zd);
        ll_knife_lxd = (LinearLayout) rootView.findViewById(R.id.knife_lxd);
        ll_knife_jp = (LinearLayout) rootView.findViewById(R.id.knife_jp);
        cb_test = (RadioButton) rootView.findViewById(R.id.test);
        cb_real = (RadioButton) rootView.findViewById(R.id.real);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        pathname = ((DataActivity) activity).getPath();
        /*if(activity instanceof FragmentInteraction) {
            listterner = (FragmentInteraction)activity; // 2.2 获取到宿主activity并赋值
        } else{
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }*/
    }

    //获取NC文件内容(未经机床文件处理过的内容)
    public List GetNCFileContent(String pathname, String childno) {
        lstFileContent = new ArrayList();
        ncParameterHandle = new NCParameterHandle();
        int iCountPaht = 0;//包含刀路数量
        String messageShow = "";
        try {
            iCountPaht++;
            /***/
            SpindleAB spAB = new SpindleAB();
            if (PreferencesUtils.getInt(getContext(),"machinetype")==2){
                spAB.A = Double.valueOf(pathABVal.ISRad2Check);//工位
                spAB.B = Double.valueOf(pathABVal.BaxleFormula);//角度
            }else {
                spAB.A = Double.valueOf(pathABVal.AaxleFormula);
                spAB.B = Double.valueOf(pathABVal.BaxleFormula);
            }

            if (knifename.equals("螺旋刀")) {
                iCutter = 2;
                Depth = Double.valueOf(T2_Depth.getText().toString());
                knifeDiameter = Double.valueOf(T2_Diameter.getText().toString());
                cutSpeed = Double.valueOf(T2_Speed.getText().toString());
                spindleSpeed = Double.valueOf(T2_Rpm.getText().toString());
                Percent = Double.valueOf(T2_Percent.getText().toString());
            } else if (knifename.equals("钻刀")) {
                iCutter = 1;
                Depth = Double.valueOf(T1_Depth.getText().toString());
                knifeDiameter = Double.valueOf(T1_Diameter.getText().toString());
                cutSpeed = Double.valueOf(T1_Speed.getText().toString());
                spindleSpeed = Double.valueOf(T1_Rpm.getText().toString());
                Percent =Double.valueOf(T1_Percent.getText().toString());
            } else if (knifename.equals("锯片")) {
                iCutter = 4;
                knifeDiameter = Double.valueOf(T4_Diameter.getText().toString());
                cutSpeed = Double.valueOf(T4_Speed.getText().toString());
                spindleSpeed = Double.valueOf(T4_Rpm.getText().toString());
                Percent = Double.valueOf(T4_Percent.getText().toString());
            }
            /***/
            PathToolSetup pathtool = ncParameterHandle.GetPathParameter(iCutter, spAB, cutSpeed, spindleSpeed);
            //刀具信息
            lstFileContent = ncParameterHandle.SetParameterToArray(lstFileContent, pathtool);
            if (pathname.contains("直圆榫头刀路")) {
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("直圆榫头刀路", "zys"), Context.MODE_PRIVATE);
                ZhiYuanMode zhiYuanMode = new ZhiYuanMode();
                zhiYuanMode.sThickness = Double.valueOf(pref.getString("zys_MaterialHeight", "0"));
                zhiYuanMode.sWidth = Double.valueOf(pref.getString("zys_MaterialWidth", "0"));
                zhiYuanMode.mtLength = Double.valueOf(pref.getString("zys_SunMaoLength", "0"));
                zhiYuanMode.bottomMargion = Double.valueOf(pref.getString("zys_BottonMargin", "0"));
                zhiYuanMode.leftMargion = Double.valueOf(pref.getString("zys_LeftMargin", "0"));
                zhiYuanMode.circleDiameter = Double.valueOf(pref.getString("zys_SunMaoD", "0"));
                zhiYuanMode.zSafeDist = Double.valueOf(pref.getString("zys_zsafe", "0"));
                zhiYuanMode.iCutter = iCutter;
                zhiYuanMode.knifeDiameter = knifeDiameter;//刀具直径
                zhiYuanMode.depthPercent = Percent; //切削百分比
                zhiYuanMode.cutDepth = Depth;//每刀深度
                zhiYuanMode.cutSpeed = cutSpeed; //切削速度
                zhiYuanMode.spindalSpeed = spindleSpeed;//主轴转速

                if (ISClockWise == 1) {//顺时针
                    lstFileContent = SunMaoDaoLuMode.ZhiYuanToolPath_Clockwise(lstFileContent, pathABVal, zhiYuanMode);//1ge
                } else {//逆时针
                    lstFileContent = SunMaoDaoLuMode.ZhiYuanToolPath_AntiClockwise(lstFileContent, pathABVal, zhiYuanMode);//1ge
                }

            }
            /*if (pathone.contains("多边直扁圆刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("多边直扁圆刀路", "dbzbys"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("dbzbys_MaterialHeight", newlists.toArray()[1].toString());
                    editor.putString("dbzbys_MaterialWidth", newlists.toArray()[0].toString());
                    editor.putString("dbzbysSunMaoLength", newlists.toArray()[2].toString());//2
                    editor.putString("dbzbys_SunMaoWidth", newlists.toArray()[3].toString());
                    editor.putString("dbzbys_SunMaoThickness", newlists.toArray()[4].toString());//4
                    editor.putString("dbzbys_BottonMargin", newlists.toArray()[6].toString());
                    editor.putString("dbzbys_LeftMargin", newlists.toArray()[5].toString());
                    editor.putString("dbzbys_zsafe", newlists.toArray()[7].toString());*/
            if(pathname.contains("多边直扁圆刀路")){
                ZhiBianYuanMode zhiBianYuanMode = new ZhiBianYuanMode();
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("多边直扁圆刀路", "dbzbys"), Context.MODE_PRIVATE);
                zhiBianYuanMode.sWidth = Double.valueOf(pref.getString("dbzbys_MaterialWidth", "0"));//料宽
                zhiBianYuanMode.sThickness = Double.valueOf(pref.getString("dbzbys_MaterialHeight", "0"));//料厚
                zhiBianYuanMode.mtLength = Double.valueOf(pref.getString("dbzbys_SunMaoLength", "0"));//榫头长度
                zhiBianYuanMode.mtWidth = Double.valueOf(pref.getString("dbzbys_SunMaoWidth", "0"));//榫头宽
                zhiBianYuanMode.mtThickness = Double.valueOf(pref.getString("dbzbys_SunMaoThickness", "0"));//榫头厚
                zhiBianYuanMode.leftMargion = Double.valueOf(pref.getString("dbzbys_LeftMargin", "0"));//左边间距
                zhiBianYuanMode.bottomMargion = Double.valueOf(pref.getString("dbzbys_BottonMargin", "0"));//右边间距
                zhiBianYuanMode.zSafeDist = Double.valueOf(pref.getString("dbzbys_zsafe", "0"));//Zsafe

                zhiBianYuanMode.iCutter = iCutter;
                zhiBianYuanMode.knifeDiameter = knifeDiameter;//刀具直径
                zhiBianYuanMode.depthPercent = Percent; //切削百分比
                zhiBianYuanMode.cutDepth = Depth;//每刀深度
                zhiBianYuanMode.cutSpeed = cutSpeed; //切削速度
                zhiBianYuanMode.spindalSpeed = spindleSpeed;//主轴转速
                //8.24新增
                if (ISClockWise == 1) {//顺时针
                    lstFileContent = SunMaoDaoLuMode.ZBYToolPath_ClockwiseHasThreeEdge(lstFileContent, pathABVal, zhiBianYuanMode);;
                } else {//逆时针
                    lstFileContent = SunMaoDaoLuMode.ZBYToolPath_AntiClockwiseHasThreeEdge(lstFileContent, pathABVal, zhiBianYuanMode);
                }
            }
            if (pathname.contains("直扁圆榫头刀路")) {
                ZhiBianYuanMode zhiBianYuanMode = new ZhiBianYuanMode();
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("直扁圆榫头刀路", "zbys"), Context.MODE_PRIVATE);
                zhiBianYuanMode.sWidth = Double.valueOf(pref.getString("zbys_MaterialWidth", "0"));//料宽
                zhiBianYuanMode.sThickness = Double.valueOf(pref.getString("zbys_MaterialHeight", "0"));//料厚
                zhiBianYuanMode.mtLength = Double.valueOf(pref.getString("zbys_SunMaoLength", "0"));//榫头长度
                zhiBianYuanMode.mtWidth = Double.valueOf(pref.getString("zbys_SunMaoWidth", "0"));//榫头宽
                zhiBianYuanMode.mtThickness = Double.valueOf(pref.getString("zbys_SunMaoThickness", "0"));//榫头厚
                zhiBianYuanMode.leftMargion = Double.valueOf(pref.getString("zbys_LeftMargin", "0"));//左边间距
                zhiBianYuanMode.bottomMargion = Double.valueOf(pref.getString("zbys_BottonMargin", "0"));//右边间距
                zhiBianYuanMode.zSafeDist = Double.valueOf(pref.getString("zys_zsafe", "0"));//Zsafe
                zhiBianYuanMode.iCutter = iCutter;
                zhiBianYuanMode.knifeDiameter = knifeDiameter;//刀具直径
                zhiBianYuanMode.depthPercent = Percent; //切削百分比
                zhiBianYuanMode.cutDepth = Depth;//每刀深度
                zhiBianYuanMode.cutSpeed = cutSpeed; //切削速度
                zhiBianYuanMode.spindalSpeed = spindleSpeed;//主轴转速
                if (ISClockWise == 1) {//顺时针
                    lstFileContent = SunMaoDaoLuMode.ZhiBianToolPath_Clockwise(lstFileContent, pathABVal, zhiBianYuanMode);
                } else {//逆时针
                    lstFileContent = SunMaoDaoLuMode.ZhiBianToolPath_AntiClockwise(lstFileContent, pathABVal, zhiBianYuanMode);
                }
            }
            if (pathname.contains("带边直扁圆刀路")) {
                ZhiBianYuanMode zhiBianYuanMode = new ZhiBianYuanMode();
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("带边直扁圆刀路", "dbzbys"), Context.MODE_PRIVATE);
                zhiBianYuanMode.sWidth = Double.valueOf(pref.getString("dbzbys_MaterialWidth", "0"));//料宽
                zhiBianYuanMode.sThickness = Double.valueOf(pref.getString("dbzbys_MaterialHeight", "0"));//料厚
                zhiBianYuanMode.mtLength = Double.valueOf(pref.getString("dbzbys_SunMaoLength", "0"));//榫头长度
                zhiBianYuanMode.mtWidth = Double.valueOf(pref.getString("dbzbys_SunMaoWidth", "0"));//榫头宽
                zhiBianYuanMode.mtThickness = Double.valueOf(pref.getString("dbzbys_SunMaoThickness", "0"));//榫头厚
                zhiBianYuanMode.leftMargion = Double.valueOf(pref.getString("dbzbys_LeftMargin", "0"));//左边间距
                zhiBianYuanMode.bottomMargion = Double.valueOf(pref.getString("dbzbys_BottonMargin", "0"));//右边间距
                zhiBianYuanMode.zSafeDist = Double.valueOf(pref.getString("dbzbys_zsafe", "0"));//zsafe
                zhiBianYuanMode.iCutter = iCutter;
                zhiBianYuanMode.knifeDiameter = knifeDiameter;//刀具直径
                zhiBianYuanMode.depthPercent = Percent; //切削百分比
                zhiBianYuanMode.cutDepth = Depth;//每刀深度
                zhiBianYuanMode.cutSpeed = cutSpeed; //切削速度
                zhiBianYuanMode.spindalSpeed = spindleSpeed;//主轴转速
                zhiBianYuanMode.iCutter = iCutter;
                if (ISClockWise == 1) {//顺时针
                    lstFileContent =SunMaoDaoLuMode.ZBYToolPath_ClockwiseHasEdge(lstFileContent, pathABVal, zhiBianYuanMode);
                } else {//逆时针
                    lstFileContent =SunMaoDaoLuMode.ZBYToolPath_AntiClockwiseHasEdge(lstFileContent, pathABVal, zhiBianYuanMode);
                }
            }
            if (pathname.contains("直扁母榫刀路")) {
                ZhiBianMuYuanMode zbmy = new ZhiBianMuYuanMode();
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("直扁母榫刀路", "zbms"), Context.MODE_PRIVATE);
                zbmy.knifeDiameter = knifeDiameter;
                zbmy.cutDepth = Depth;
                zbmy.cutSpeed = cutSpeed;
                zbmy.spindalSpeed = spindleSpeed;
                zbmy.cutPercent = Percent;
                zbmy.iCutter = iCutter;

                zbmy.GrooveWidth = Double.valueOf(pref.getString("zbms_Groovelength", "0"));
                zbmy.GrooveDeep = Double.valueOf(pref.getString("zbms_Groovedepth", "0"));
                zbmy.GrooveThick = Double.valueOf(pref.getString("zbms_Groovewidth", "0"));
                zbmy.zSafeDist = Double.valueOf(pref.getString("zbms_zsafe", "0"));
                zbmy.pathAB = pathABVal;
                lstFileContent =SunMaoDaoLuMode.StraightFlatFemalePath(lstFileContent, zbmy);//2
            }
            if (pathname.contains("半开口弧形闭合榫槽刀路")) {
                SunCaoMode sunCaoMode = new SunCaoMode();
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("半开口弧形闭合榫槽刀路", "bhscdl"), Context.MODE_PRIVATE);
                sunCaoMode.GrooveWidth = Double.valueOf(pref.getString("bhscdl_Groovewidth", "0"));
                sunCaoMode.GrooveDeep = Double.valueOf(pref.getString("bhscdl_Groovedepth", "0"));
                sunCaoMode.GrooveThick = Double.valueOf(pref.getString("bhscdl_Groovelength", "0"));
                sunCaoMode.MaterialThick = Double.valueOf(pref.getString("bhscdl_MaterialHeight", "0"));
                sunCaoMode.MaterialWidth = Double.valueOf(pref.getString("bhscdl_MaterialWidth", "0"));
                sunCaoMode.zSafeDist = Double.valueOf(pref.getString("bhscdl_zsafe", "0"));
                sunCaoMode.cutThick = Double.valueOf(T4_Thickness.getText().toString());
                sunCaoMode.iCutter = iCutter;
                sunCaoMode.pathAB = pathABVal;
                sunCaoMode.knifeDiameter = knifeDiameter;//刀具直径
                sunCaoMode.cutDepth = Depth;//每刀深度
                sunCaoMode.cutSpeed = cutSpeed; //切削速度
                sunCaoMode.spindalSpeed = spindleSpeed;//主轴转速
                sunCaoMode.cutPercent = Percent;
                sunCaoMode.pathAB = pathABVal;
                sunCaoMode.grooveType = SunMaoDaoLuMode._GROOVE_H_C;
                lstFileContent =SunMaoDaoLuMode.GroovePath(lstFileContent, sunCaoMode);//2
            }
            if (pathname.contains("半开口方形闭合榫槽刀路")) {
                SunCaoMode sunCaoMode = new SunCaoMode();
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("半开口方形闭合榫槽刀路", "bfscdl"), Context.MODE_PRIVATE);
                sunCaoMode.GrooveWidth = Double.valueOf(pref.getString("bfscdl_Groovewidth", "0"));
                sunCaoMode.GrooveDeep = Double.valueOf(pref.getString("bfscdl_Groovedepth", "0"));
                sunCaoMode.GrooveThick = Double.valueOf(pref.getString("bfscdl_Groovelength", "0"));
                sunCaoMode.MaterialThick = Double.valueOf(pref.getString("bfscdl_MaterialHeight", "0"));
                sunCaoMode.MaterialWidth = Double.valueOf(pref.getString("bfscdl_MaterialWidth", "0"));
                sunCaoMode.zSafeDist = Double.valueOf(pref.getString("bfscdl_zsafe", "0"));
                sunCaoMode.cutThick = Double.valueOf(T4_Thickness.getText().toString());
                sunCaoMode.iCutter = iCutter;
                sunCaoMode.pathAB = pathABVal;
                sunCaoMode.cutPercent = Percent;
                sunCaoMode.knifeDiameter = knifeDiameter;//刀具直径
                sunCaoMode.cutDepth = Depth;//每刀深度
                sunCaoMode.cutSpeed = cutSpeed; //切削速度
                sunCaoMode.spindalSpeed = spindleSpeed;//主轴转速
                sunCaoMode.pathAB = pathABVal;
                sunCaoMode.grooveType = SunMaoDaoLuMode._GROOVE_H_R;
                lstFileContent =SunMaoDaoLuMode.GroovePath(lstFileContent, sunCaoMode);//2
            }
            if (pathname.contains("全开口榫槽刀路")) {
                SunCaoMode sunCaoMode = new SunCaoMode();
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("全开口榫槽刀路", "qkscdl"), Context.MODE_PRIVATE);
                sunCaoMode.GrooveWidth = Double.valueOf(pref.getString("qkscdl_Groovewidth", "0"));
                sunCaoMode.GrooveDeep = Double.valueOf(pref.getString("qkscdl_Groovedepth", "0"));
                sunCaoMode.GrooveThick = Double.valueOf(pref.getString("qkscdl_Groovelength", "0"));
                sunCaoMode.MaterialThick = Double.valueOf(pref.getString("bfscdl_MaterialHeight", "0"));
                sunCaoMode.MaterialWidth = Double.valueOf(pref.getString("bfscdl_MaterialWidth", "0"));
                sunCaoMode.zSafeDist = Double.valueOf(pref.getString("qkscdl_zsafe", "0"));
                sunCaoMode.cutThick = Double.valueOf(T4_Thickness.getText().toString());
                sunCaoMode.iCutter = iCutter;
                sunCaoMode.pathAB = pathABVal;
                sunCaoMode.cutPercent = Percent;
                sunCaoMode.knifeDiameter = knifeDiameter;//刀具直径
                sunCaoMode.cutDepth = Depth;//每刀深度
                sunCaoMode.cutSpeed = cutSpeed; //切削速度
                sunCaoMode.spindalSpeed = spindleSpeed;//主轴转速
                sunCaoMode.pathAB = pathABVal;
                sunCaoMode.grooveType = SunMaoDaoLuMode._GROOVE_A;
                lstFileContent =SunMaoDaoLuMode.GroovePath(lstFileContent, sunCaoMode);//2
            }
//            if (pathname.contains("榫槽刀路")) {
//                SunCaoMode sunCaoMode = new SunCaoMode();
//                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("榫槽刀路", "scdl"), Context.MODE_PRIVATE);
//                sunCaoMode.GrooveWidth = Double.valueOf(pref.getString("scdl_Groovewidth", ""));
//                sunCaoMode.GrooveDeep = Double.valueOf(pref.getString("scdl_Groovedepth", ""));
//                sunCaoMode.GrooveThick = Double.valueOf(pref.getString("scdl_Groovelength", ""));
//                sunCaoMode.MaterialThick = Double.valueOf(pref.getString("scdl_MaterialHeight", ""));
//                sunCaoMode.MaterialWidth = Double.valueOf(pref.getString("scdl_MaterialWidth", ""));
//                sunCaoMode.pathAB = pathABVal;
//                sunCaoMode.knifeDiameter = knifeDiameter;//刀具直径
//                sunCaoMode.cutDepth = Depth;//每刀深度
//                sunCaoMode.cutSpeed = cutSpeed; //切削速度
//                sunCaoMode.spindalSpeed = spindleSpeed;//主轴转速
//                sunCaoMode.pathAB = pathABVal;
//                sunCaoMode.grooveType = SunMaoDaoLuMode._GROOVE_A;
//                 SunMaoDaoLuMode.GroovePath(lstFileContent, sunCaoMode);//2
//            }
            if (pathname.contains("T型槽刀路")) {
                SunCaoMode sunCaoMode = new SunCaoMode();
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("T型槽刀路", "txc"), Context.MODE_PRIVATE);
                sunCaoMode.GrooveWidth = Double.valueOf(pref.getString("scdl_Groovewidth", "0"));
                sunCaoMode.GrooveDeep = Double.valueOf(pref.getString("scdl_Groovedepth", "0"));
                sunCaoMode.GrooveThick = Double.valueOf(pref.getString("scdl_Groovelength", "0"));
                sunCaoMode.zSafeDist = Double.valueOf(pref.getString("scdl_zsafe", ""));
                sunCaoMode.iCutter = iCutter;
                sunCaoMode.MaterialThick = Double.valueOf(pref.getString("scdl_MaterialHeight", "0"));
                sunCaoMode.MaterialWidth = Double.valueOf(pref.getString("scdl_MaterialWidth", "0"));
                sunCaoMode.pathAB = pathABVal;
                sunCaoMode.knifeDiameter = knifeDiameter;//刀具直径
                sunCaoMode.cutPercent = Percent;
                sunCaoMode.cutDepth = Depth;//每刀深度
                sunCaoMode.cutSpeed = cutSpeed; //切削速度
                sunCaoMode.spindalSpeed = spindleSpeed;//主轴转速
                sunCaoMode.pathAB = pathABVal;
                sunCaoMode.grooveType = SunMaoDaoLuMode._GROOVE_A;
                lstFileContent =SunMaoDaoLuMode.GroovePath(lstFileContent, sunCaoMode);//2
            }

            if (pathname.contains("母圆榫刀路")) {
                MuYuanSunMode muYuanSunMode = new MuYuanSunMode();
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("母圆榫刀路", "mys"), Context.MODE_PRIVATE);
                muYuanSunMode.knifeDiameter = knifeDiameter;
                muYuanSunMode.cutDepth = Depth;
                muYuanSunMode.cutSpeed = cutSpeed;
                muYuanSunMode.spindalSpeed = spindleSpeed;
                muYuanSunMode.iCutter = iCutter;
                muYuanSunMode.MaterialWidth = Double.valueOf(pref.getString("mys_MaterialWidth", "0"));//材料宽度
                muYuanSunMode.MaterialThick = Double.valueOf(pref.getString("mys_MaterialHeight", "0"));//材料厚度
                muYuanSunMode.GrooveDeep = Double.valueOf(pref.getString("mys_Groovedepth", "0"));//槽深
                muYuanSunMode.Radius = Double.valueOf(pref.getString("mys_Radius", "0"));//半径
                muYuanSunMode.leftMargin = Double.valueOf(pref.getString("mys_leftMargin", "0"));//左边距
                muYuanSunMode.bottomMargin = Double.valueOf(pref.getString("mys_bottomMargin", "0"));//下边距
                muYuanSunMode.zSafeDist =Double.valueOf(pref.getString("mys_zsafe","0"));
                muYuanSunMode.pathAB = pathABVal;

                Position p = new Position();
                p.X = -(muYuanSunMode.MaterialWidth - muYuanSunMode.leftMargin - muYuanSunMode.Radius);
                p.Y = muYuanSunMode.bottomMargin + muYuanSunMode.Radius;
                p.Z = 0;
                muYuanSunMode.CentrePoint = p;
                lstFileContent =SunMaoDaoLuMode.CircleFemalePath(lstFileContent, muYuanSunMode);//2

            }
            if (pathname.contains("带边方型刀路")) {
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("带边方型刀路", "dbfxdl"), Context.MODE_PRIVATE);
                ZhiFangMode zhiFangMode = new ZhiFangMode();
                zhiFangMode.sWidth = Double.valueOf(pref.getString("dbfxdl_MaterialWidth", "0"));//料宽
                zhiFangMode.sThickness = Double.valueOf(pref.getString("dbfxdl_MaterialHeight", "0"));//料厚
                zhiFangMode.mtWidth = Double.valueOf(pref.getString("dbfxdl_SunMaoWidth", "0"));//榫头宽
                zhiFangMode.mtThickness = Double.valueOf(pref.getString("dbfxdl_SunMaoThickness", "0"));//榫头厚
                zhiFangMode.mtLength = Double.valueOf(pref.getString("dbfxdl_SunMaoLength", "0"));//榫头长度
                zhiFangMode.leftMargion = Double.valueOf(pref.getString("dbfxdl_LeftMargin", "0"));//左边间距
                zhiFangMode.bottomMargion = Double.valueOf(pref.getString("dbfxdl_BottonMargin", "0"));//右边间距
                zhiFangMode.zSafeDist = Double.valueOf(pref.getString("dbfxdl_zsafe", "0"));//
                zhiFangMode.knifeDiameter = knifeDiameter;//刀具直径
                zhiFangMode.depthPercent = Percent; //切削百分比
                zhiFangMode.cutDepth = Depth;//每刀深度
                zhiFangMode.cutSpeed = cutSpeed; //切削速度
                zhiFangMode.spindalSpeed = spindleSpeed;//主轴转速
                zhiFangMode.pathAB = pathABVal;
                zhiFangMode.iCutter = iCutter;
                if (ISClockWise == 1) {//顺时针
                    lstFileContent =SunMaoDaoLuMode.FangXingToolPath_ClockwiseHasEdge(lstFileContent, pathABVal, zhiFangMode);
                } else {//逆时针
                    lstFileContent =SunMaoDaoLuMode.FangXingToolPath_AntiClockwiseHasEdge(lstFileContent, pathABVal, zhiFangMode);
                }
            }
            if (pathname.contains("方型刀路")) {
                pref = getActivity().getSharedPreferences(childno + pathname.replace("方型刀路", "fxdl"), Context.MODE_PRIVATE);
                //pref = getActivity().getSharedPreferences("fxdl", Context.MODE_PRIVATE);
                ZhiFangMode zhiFangMode = new ZhiFangMode();
                zhiFangMode.sWidth = Double.valueOf(pref.getString("fxdl_MaterialWidth", "0"));//料宽
                zhiFangMode.sThickness = Double.valueOf(pref.getString("fxdl_MaterialHeight", "0"));//料厚
                zhiFangMode.mtWidth = Double.valueOf(pref.getString("fxdl_SunMaoWidth", "0"));//榫头宽
                zhiFangMode.mtThickness = Double.valueOf(pref.getString("fxdl_SunMaoThickness", "0"));//榫头厚
                zhiFangMode.mtLength = Double.valueOf(pref.getString("fxdl_SunMaoLength", "0"));//榫头长度
                zhiFangMode.leftMargion = Double.valueOf(pref.getString("fxdl_LeftMargin", "0"));//左边间距
                zhiFangMode.bottomMargion = Double.valueOf(pref.getString("fxdl_BottonMargin", "0"));//右边间距
                zhiFangMode.zSafeDist = Double.valueOf(pref.getString("fxdl_zsafe", "0"));//右边间距
                zhiFangMode.iCutter = iCutter;
                zhiFangMode.knifeDiameter = knifeDiameter;//刀具直径
                zhiFangMode.depthPercent = Percent; //切削百分比
                zhiFangMode.cutDepth = Depth;//每刀深度
                zhiFangMode.cutSpeed = cutSpeed; //切削速度
                zhiFangMode.spindalSpeed = spindleSpeed;//主轴转速
                zhiFangMode.pathAB = pathABVal;
                if (ISClockWise == 1) {//顺时针
                    lstFileContent = SunMaoDaoLuMode.FangXingToolPath_Clockwise(lstFileContent, pathABVal, zhiFangMode);
                } else {//逆时针
                    lstFileContent = SunMaoDaoLuMode.FangXingToolPath_AntiClockwise(lstFileContent, pathABVal, zhiFangMode);
                }
            }
            if (pathname.contains("边线加工刀路")) {
                ZhiLineMode mode = new ZhiLineMode();
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("边线加工刀路", "bxjg"), Context.MODE_PRIVATE);
                //  pref = getActivity().getSharedPreferences("bxjg", Context.MODE_PRIVATE);
                mode.width = Double.valueOf(pref.getString("bxjg_Width", "0"));
                mode.thickness = Double.valueOf(pref.getString("bxjg_thickness", "0"));
                mode.length = Double.valueOf(pref.getString("bxjg_Length", "0"));
                mode.zSafeDist = Double.valueOf(pref.getString("bxjg_zsafe", "0"));
                mode.iCutter = iCutter;
                mode.cutDepth = Depth;
                mode.spindalSpeed = spindleSpeed;
                mode.depthPercent = Percent;
                mode.knifeDiameter = knifeDiameter;
                mode.pathAB = pathABVal;
                if (ISClockWise == 1) {
                    lstFileContent = SunMaoDaoLuMode.ZhiLinePath_Clockwise(lstFileContent, pathABVal, mode);
                } else {
                    lstFileContent = SunMaoDaoLuMode.ZhiLinePath_AntiClockwise(lstFileContent, pathABVal, mode);
                }
            }
            if (pathname.contains("U型刀路")) {
                ZhiUMode zhiUMode = new ZhiUMode();
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("U型刀路", "uxdl"), Context.MODE_PRIVATE);
                zhiUMode.sWidth = Double.valueOf(pref.getString("uxdl_MaterialWidth", "0"));//料宽
                zhiUMode.sThickness = Double.valueOf(pref.getString("uxdl_MaterialHeight", "0"));//料厚
                zhiUMode.mtWidth = Double.valueOf(pref.getString("uxdl_SunMaoWidth", "0"));//榫头宽
                zhiUMode.mtThickness = Double.valueOf(pref.getString("uxdl_SunMaoThickness", "0"));//榫头厚
                zhiUMode.mtLength = Double.valueOf(pref.getString("uxdl_SunMaoLength", "0"));//榫头长度
                zhiUMode.zSafeDist = Double.valueOf(pref.getString("uxdl_zsafe", "0"));//左边间距
                zhiUMode.leftMargion = Double.valueOf(pref.getString("uxdl_LeftMargin", "0"));//
                zhiUMode.iCutter = iCutter;
                zhiUMode.knifeDiameter = knifeDiameter;//刀具直径
                zhiUMode.depthPercent = Percent; //切削百分比
                zhiUMode.cutDepth = Depth;//每刀深度
                zhiUMode.cutSpeed = cutSpeed; //切削速度
                zhiUMode.spindalSpeed = spindleSpeed;//主轴转速
                zhiUMode.pathAB = pathABVal;
                if (ISClockWise == 1) {
                    lstFileContent = SunMaoDaoLuMode.ZhiUPath_Clockwise(lstFileContent, pathABVal, zhiUMode);
                } else {
                    lstFileContent = SunMaoDaoLuMode.ZhiUPath_AntiClockwise(lstFileContent, pathABVal, zhiUMode);
                }
            }
            if(pathname.contains("L型刀路")){
                LMode lMode = new LMode();
                pref = getActivity().getSharedPreferences(childno.trim() + pathname.replace("L型刀路", "lxdl"), Context.MODE_PRIVATE);
                lMode.sWidth=Double.valueOf(pref.getString("lxdl_MaterialWidth","0"));
                lMode.sThickness=Double.valueOf(pref.getString("lxdl_MaterialWidth","0"));
                lMode.mtHeight=Double.valueOf(pref.getString("lxdl_mtHeight","0"));
                lMode.L_mtLength=Double.valueOf(pref.getString("lxdl_SunMaoLength","0"));
                lMode.L_mtWidth=Double.valueOf(pref.getString("lxdl_SunMaoWidth","0"));
                lMode.L_mtThickness=Double.valueOf(pref.getString("lxdl_SunMaoThickness","0"));
                lMode.leftMargin=Double.valueOf(pref.getString("lxdl_LeftMargin","0"));
                lMode.bottomMargin = Double.valueOf(pref.getString("lxdl_BottomMargin","0"));
                lMode.zSafeDist = Double.valueOf(pref.getString("lxdl_zsafe","0"));

                lMode.iCutter = iCutter;
                lMode.knifeDiameter = knifeDiameter;//刀具直径
                lMode.depthPercent = Percent; //切削百分比
                lMode.cutDepth = Depth;//每刀深度
                lMode.cutSpeed = cutSpeed; //切削速度
                lMode.spindalSpeed = spindleSpeed;//主轴转速
                lMode.pathAB = pathABVal;
                //8.24新增
                if (ISClockWise == 1) {
                    SunMaoDaoLuMode.LToolPath_Clockwise(lstFileContent, lMode);
                } else {
                    SunMaoDaoLuMode.LToolPath_AntiClockwise(lstFileContent, lMode);
                }
            }
            if (pathname.contains("7型刀路")){
                Char7Mode char7Mode = new Char7Mode();
                char7Mode.mtHight = Double.valueOf(pref.getString("sxdl_MaterialHeight","0"));
                char7Mode.mtWidth= Double.valueOf(pref.getString("sxdl_MaterialWidth","0"));
                char7Mode.mtLength = Double.valueOf(pref.getString("sxdl_SunMaoLength","0"));
                char7Mode.mtThickness = Double.valueOf(pref.getString("sxdl_SunMaoThickness","0"));
                char7Mode.zSafeDist = Double.valueOf(pref.getString("sxdl_zsafe","0"));

                char7Mode.iCutter = iCutter;
                char7Mode.knifeDiameter = knifeDiameter;//刀具直径
                char7Mode.depthPercent = Percent; //切削百分比
                char7Mode.cutDepth = Depth;//每刀深度
                char7Mode.cutSpeed = cutSpeed; //切削速度
                char7Mode.spindalSpeed = spindleSpeed;//主轴转速
                char7Mode.pathAB = pathABVal;
                //8.24新增
                char7Mode.pathAB = pathABVal;
                if (ISClockWise == 1) {
                    if (char7Mode.mtThickness > char7Mode.knifeDiameter) { SunMaoDaoLuMode.RunBig7ShapeClock(lstFileContent, char7Mode); }
                    else if (char7Mode.mtThickness == char7Mode.knifeDiameter) { SunMaoDaoLuMode.Run7ShapeClock(lstFileContent, char7Mode); }
                } else {
                    if (char7Mode.mtThickness > char7Mode.knifeDiameter) { SunMaoDaoLuMode.RunBig7ShapeAntiClock(lstFileContent, char7Mode); }
                    else if (char7Mode.mtThickness == char7Mode.knifeDiameter) { SunMaoDaoLuMode.Run7ShapeAntiClock(lstFileContent, char7Mode); }
                }
            }
            if (pathname.contains("V型刀路")){
                VMode char7Mode = new VMode();
                char7Mode.mtWidth= Double.valueOf(pref.getString("vxdl_SunMaoWidth","0"));
                char7Mode.mtThickness = Double.valueOf(pref.getString("vxdl_SunMaoThickness","0"));
                char7Mode.zSafeDist = Double.valueOf(pref.getString("vxdl_zsafe","0"));

                char7Mode.iCutter = iCutter;
                char7Mode.knifeDiameter = knifeDiameter;//刀具直径
                char7Mode.depthPercent = Percent; //切削百分比
                char7Mode.cutDepth = Depth;//每刀深度
                char7Mode.cutSpeed = cutSpeed; //切削速度
                char7Mode.spindalSpeed = spindleSpeed;//主轴转速
                char7Mode.pathAB = pathABVal;
                //8.24新增
                char7Mode.pathAB = pathABVal;
                if (ISClockWise == 1) {
                    SunMaoDaoLuMode.RunVShapeClockPathData(lstFileContent,char7Mode);

                } else {
                    SunMaoDaoLuMode.RunVShapeAntiClockPathData(lstFileContent,char7Mode);
                }
            }

            if (iCountPaht == 0) {
                Toast.makeText(getActivity(), "无法导出", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lstFileContent;
    }

    //计算榫卯刀路材料尺寸公式
    public static double CalculationFunctionPathAngle(String gongsi) {
        String a = "sin";
        String b = "cos";
        String c = "tan";
        String d = "sqrt";
        gongsi = gongsi.toLowerCase();
        double enddata = 0;
        String IP6 = "[\\+\\-\\*\\/\\(\\)\\^\\u4e00-\\u9fa5]";
        String[] bit = gongsi.split(IP6);
        for (int i = 0; i < bit.length; i++) {
            if (!bit[i].contains(a) && !bit[i].contains(b) && !bit[i].contains(c) && !bit[i].contains(d)) {
                String val = bit[i].trim();
                if (!val.equals("")) {
                    String iret = GetValByPathParaNew(val);
                    int cL = val.length();
                    int index = gongsi.indexOf(val);
                    if (index > -1) {
                        StringBuffer temps1 = new StringBuffer(gongsi);
                        gongsi = temps1.replace(index, index + cL, iret).toString();
                    }
                }
            }
        }
        try {
            Expression expr = Parser.parse(gongsi + "*180/3.1415926");
            enddata = expr.evaluate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //  String str = sqlHandle.valdata(gongsi+ "*180/PI()");
        return enddata;//Double.valueOf(str);
    }

    public List ABdata(String SunMao_Path_ID1) {
        List ab = new ArrayList();
        List<QM_MT_ABaxisVal> list1 = LitePal.select("*")
                .where("SunMao_Path_ID = ?", SunMao_Path_ID1)
                .find(QM_MT_ABaxisVal.class);
        for (QM_MT_ABaxisVal qm_mt_aBaxisVal : list1) {
            ab.add(qm_mt_aBaxisVal.getISRad1Check());
            ab.add(qm_mt_aBaxisVal.getProfileFormula());
            ab.add(qm_mt_aBaxisVal.getProfileScope1());
            ab.add(qm_mt_aBaxisVal.getProfileScope2());
            ab.add(qm_mt_aBaxisVal.getISRad2Check());
            ab.add(qm_mt_aBaxisVal.getAaxleFormula());
            ab.add(qm_mt_aBaxisVal.getAaxleScope1());
            ab.add(qm_mt_aBaxisVal.getAaxleScope2());
            ab.add(qm_mt_aBaxisVal.getISRad3Check());
            ab.add(qm_mt_aBaxisVal.getBaxleFormula());
            ab.add(qm_mt_aBaxisVal.getBaxleScope1());
            ab.add(qm_mt_aBaxisVal.getBaxleScope2());
            ab.add(qm_mt_aBaxisVal.getStartDot());
            ab.add(qm_mt_aBaxisVal.getISClockWise());
        }
        return ab;
    }

    public String knifepath(String SunMao_Path_ID1) {
        String knifename = "";
        List<QM_MT_Knifeproperty_path_R> list = LitePal.select("knifename")
                .where("SunMao_Path_ID = ?", SunMao_Path_ID1)
                .limit(1)
                .find(QM_MT_Knifeproperty_path_R.class);
        for (QM_MT_Knifeproperty_path_R path_r : list) {
            knifename = path_r.getKnifeName();
        }
        return knifename;
    }

    public void ExportData(String childc,int selectth) {
        if (cb_test.isChecked()){
            cb_falg=1;
        }else if (cb_real.isChecked()){
            cb_falg=2;
        }
        patharray.clear();
        pref = getActivity().getSharedPreferences("tool_code", Context.MODE_PRIVATE);
        int biasdata = pref.getInt("which", -1);
        if (biasdata > -1) {
                       /* pref = getActivity().getSharedPreferences("tooldata", Context.MODE_PRIVATE);
                        editor = pref.edit();
                        editor.putString("T1_Diameter", T1_Diameter.getText().toString());
                        editor.putString("T1_Depth", T1_Depth.getText().toString());
                        editor.putString("T1_Speed", T1_Speed.getText().toString());
                        editor.putString("T1_Rpm", T1_Rpm.getText().toString());
                        editor.putString("T2_Diameter", T2_Diameter.getText().toString());
                        editor.putString("T2_Depth", T2_Depth.getText().toString());
                        editor.putString("T2_Speed", T2_Speed.getText().toString());
                        editor.putString("T2_Percent", T2_Percent.getText().toString());
                        editor.putString("T2_Rpm", T2_Rpm.getText().toString());
                        editor.putString("T4_Diameter", T4_Diameter.getText().toString());
                        editor.putString("T4_Diameter", T4_Diameter.getText().toString());
                        editor.putString("T4_Speed", T4_Speed.getText().toString());
                        editor.putString("T4_Percent", T4_Percent.getText().toString());
                        editor.putString("T4_Rpm", T4_Rpm.getText().toString());
                        editor.putString("T4_Thicknes", T4_Thickness.getText().toString());
                        editor.commit();*/
            //sqlite获取一个榫卯里面的所有刀路
            // List allpath=new ArrayList();
            List<QM_MT_SunMao_Path_R> list = LitePal.select("pathname")
                    .where("sunmaonameno = ?", childc.trim())
                    .find(QM_MT_SunMao_Path_R.class);
            for (QM_MT_SunMao_Path_R path_rh : list) {
                patharray.add(path_rh.getPathName());
            }
            //
            // patharray = sqlHandle.select_pathtype(childc[0].trim());
            loading = new Loading_view(getActivity(), R.style.ImageloadingDialogStyle);
            loading.show();
            new Handler().postDelayed(new Runnable() {//定义延时任务模仿网络请求
                @Override
                public void run() {
                    for (int i = 0; i < patharray.size(); i++) {
                        Log.d("onclick", "6" + patharray.toString());
                        pref = getActivity().getSharedPreferences(patharray.get(i).toString(), Context.MODE_PRIVATE);
                        //sqlite查询榫卯一个刀路唯一代号
                        List<QM_MT_SunMao_Path_R> list = LitePal.select("path_r_id")
                                .where("sunmaonameno = ? and pathname=?", childc, patharray.get(i).toString())
                                .find(QM_MT_SunMao_Path_R.class);
                        for (QM_MT_SunMao_Path_R path_rh : list) {
                            SunMao_Path_ID = path_rh.getPath_r_id();
                        }
                        //
                        //SunMao_Path_ID = sqlHandle.select_path_r_id(patharray.get(i).toString(), childno[0]);
                        //sqliteAB转向信息
                        List ab = new ArrayList();
                        ab = ABdata(String.valueOf(SunMao_Path_ID));
                        //   List ab = sqlHandle.sunmao_AB(SunMao_Path_ID);
                        knifename = knifepath(String.valueOf(SunMao_Path_ID));
                        // knifename = sqlHandle.select_KnideName(SunMao_Path_ID);
                        ISRad1Check = Integer.valueOf(ab.get(0).toString());//1：公式  0：角度
                        ProfileFormula = ab.get(1).toString();//姿态公式//
                        ProfileScope1 = ab.get(2).toString();//姿态范围1
                        ProfileScope2 = ab.get(3).toString();//姿态范围2
                        ISRad2Check = Integer.valueOf(ab.get(4).toString());//1：公式  0：角度
                        if (PreferencesUtils.getInt(getContext(),"machinetype")==2){
                            AaxleFormula = "none";
                        }else {
                            AaxleFormula = ab.get(5).toString();//A轴公式//
                        }
                        AaxleScope1 = ab.get(6).toString();//A轴范围1
                        AaxleScope2 = ab.get(7).toString();//A轴范围2
                        ISRad3Check = Integer.valueOf(ab.get(8).toString());//1：公式  0：角度
                        BaxleFormula = ab.get(9).toString();//B轴公式//
                        BaxleScope1 = ab.get(10).toString();//B轴范围1
                        BaxleScope2 = ab.get(11).toString();//B轴范围2
                        StartDot = ab.get(12).toString();//起点坐标//
                        String[] Start = StartDot.split(",");
                        newstat = "";
                        for (int p = 0; p < Start.length; p++) {
                            newstat += String.valueOf(CalculationFunctionNew(Start[p]) + ",");
                        }
                        ISClockWise = Integer.valueOf(ab.get(13).toString());
                        //是否顺时针 1:顺时针 0:逆时针
                        pathABVal = new PathABVal();
                        pathABVal.ISClockWise = ISClockWise;//是否顺逆
                        pathABVal.StartDot = newstat;
                        pathABVal.ProfileScope1 =Double.valueOf(ProfileScope1);
                        pathABVal.ProfileScope2 = Double.valueOf(ProfileScope2);
                        double Pmax = pathABVal.ProfileScope1 > pathABVal.ProfileScope2 ? pathABVal.ProfileScope1 : pathABVal.ProfileScope2;
                        double Pmin = pathABVal.ProfileScope1 > pathABVal.ProfileScope1 ? pathABVal.ProfileScope2 : pathABVal.ProfileScope1;
                        if (ISRad1Check == 0) {
                            pathABVal.ProfileFormula = ProfileFormula;
                        } else {
                            pathABVal.ProfileFormula = String.valueOf(CalculationFunctionPathAngle(ProfileFormula));
                            //根据范围取值>>>>
                            double ztAngle = Double.valueOf(pathABVal.ProfileFormula);
                            if (ztAngle <= Pmax && ztAngle >= Pmin)
                            {
                                pathABVal.ProfileFormula = String.valueOf(ztAngle);
                            }
                            else
                            {
                                if (ztAngle > 0)
                                {
                                    pathABVal.ProfileFormula =  String.valueOf(180 - ztAngle);
                                }
                                else if (ztAngle < 0)
                                {
                                    pathABVal.ProfileFormula =  String.valueOf(-180 - ztAngle);
                                }
                            }
                            //根据范围取值>>>>
                        }

                        //A轴
                        pathABVal.AaxleScope1 = Double.valueOf(AaxleScope1);
                        pathABVal.AaxleScope2 = Double.valueOf(AaxleScope2);
                        double Amax = pathABVal.AaxleScope1 > pathABVal.AaxleScope2 ? pathABVal.AaxleScope1 : pathABVal.AaxleScope2;
                        double Amin = pathABVal.AaxleScope1 > pathABVal.AaxleScope2 ? pathABVal.AaxleScope2 : pathABVal.AaxleScope1;
                        if (PreferencesUtils.getInt(getContext(),"machinetype")!=2) {
                            if (ISRad2Check == 0) {
                                pathABVal.AaxleFormula = AaxleFormula;
                            } else {
                                pathABVal.AaxleFormula = String.valueOf(CalculationFunctionPathAngle(AaxleFormula));
                                //根据范围取值>>>>
                                double aAngle = Double.valueOf(pathABVal.AaxleFormula);
                                if (aAngle <= Amax && aAngle >= Amin)
                                {
                                    pathABVal.AaxleFormula = String.valueOf(aAngle);
                                }
                                else
                                {
                                    if (aAngle > 0)
                                    {
                                        pathABVal.AaxleFormula = String.valueOf(180 - aAngle);
                                    }
                                    else if (aAngle < 0)
                                    {
                                        pathABVal.AaxleFormula = String.valueOf(-180 - aAngle);
                                    }
                                }
                                //根据范围取值>>>>
                            }
                        }else {
                            pathABVal.ISRad2Check = ISRad2Check;
                            pathABVal.AaxleFormula = AaxleFormula;
                        }
                        //B轴
                        pathABVal.BaxleScope1 = Double.valueOf(BaxleScope1);
                        pathABVal.BaxleScope2 = Double.valueOf(BaxleScope2);
                        double Bmax = pathABVal.BaxleScope1 > pathABVal.BaxleScope2 ? pathABVal.BaxleScope1 : pathABVal.BaxleScope2;
                        double Bmin = pathABVal.BaxleScope1 > pathABVal.BaxleScope2 ? pathABVal.BaxleScope2 : pathABVal.BaxleScope1;
                        if (ISRad3Check == 0) {
                            pathABVal.BaxleFormula = BaxleFormula;
                        } else {
                            pathABVal.BaxleFormula = String.valueOf(CalculationFunctionPathAngle(BaxleFormula));
                            //根据范围取值>>>>
                            double bAngle = Double.valueOf(pathABVal.BaxleFormula);
                            if (bAngle <= Bmax && bAngle >= Bmin)
                            {
                                pathABVal.BaxleFormula = String.valueOf(bAngle);
                            }
                            else
                            {
                                if (bAngle > 0)
                                {
                                    pathABVal.BaxleFormula = String.valueOf(180 - bAngle);
                                }
                                else if (bAngle < 0)
                                {
                                    pathABVal.BaxleFormula = String.valueOf(-180 - bAngle);
                                }
                            }
                            //根据范围取值>>>>
                        }
                        newlist.addAll(GetNCFileContent(patharray.get(i).toString(), childc.trim()));
                    }
                    Log.d("onclick", "5" + newlist.toString());
                    Fileservice fileservice = new Fileservice();
                    final EditText jurisdiction = new EditText(getActivity());
                    new AlertDialog.Builder(getActivity()).setTitle("输入要保存的文件名").setIcon(android.R.drawable.ic_dialog_info).setView(jurisdiction)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (!jurisdiction.getText().toString() .equals("")) {
                                        pref = getActivity().getSharedPreferences("thickness", Context.MODE_PRIVATE);
                                        String thickness = null;
                                        switch (selectth){
                                            case 1:thickness = pref.getString("thickness", "30");break;
                                            case 2:thickness = pref.getString("thickness2", "30");break;
                                            case 3:thickness = pref.getString("thickness3", "30");break;
                                            default:break;
                                        }
                                        double stockthickness = SunMaoData.CalculationFunctionNew(thickness);
                                        Log.d("onclick", "3" + newlist.toString());
                                        PreferencesUtils.PREFERENCE_NAME="MachineType";
                                        int stype = PreferencesUtils.getInt(getContext(),"shafttype");
                                        int sid = PreferencesUtils.getInt(getContext(),"machinetype");
                                        if (stype==1&&sid==1) {
                                            fileservice.saveContentTosdCard(jurisdiction.getText().toString() + ".ptp", getNCFile.GetNCFile_c((ArrayList<String>) newlist, getActivity(), String.valueOf(stockthickness), cb_falg));//普通五轴有rtcp
                                        }else if (stype==2&&sid==1){
                                            fileservice.saveContentTosdCard(jurisdiction.getText().toString() + ".ptp", getNCFile.NORTCPGetNCFile_c((ArrayList<String>) newlist, getActivity()));//RTCP五轴
                                        }else if (stype==1&&sid==2){
                                            fileservice.saveContentTosdCard(jurisdiction.getText().toString() + ".ptp", getNCFile.XSIXGetNCFile_c((ArrayList<String>) newlist, getActivity(), String.valueOf(stockthickness)));//X6榫头机
                                        }
                                        newlist.clear();
                                        Log.d("onclick", "4" + newlist.toString());
                                        Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "请输入文件名", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            newlist.clear();
                        }
                    }).show();
                    loading.dismiss();//3秒后调用关闭加载的方法
                }
            }, 1000);

        } else {
            Toast.makeText(getActivity(), "请选择一个机床文件", 0).show();
        }
    }


}