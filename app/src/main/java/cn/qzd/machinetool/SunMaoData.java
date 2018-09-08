package cn.qzd.machinetool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qzd.machinetool.LitepalBase.QM_MT_SunMaoInfo;
import cn.qzd.machinetool.LitepalBase.QM_MT_SunMaoPathParameterVal;
import cn.qzd.machinetool.LitepalBase.QM_MT_SunMao_Path_R;
import cn.qzd.machinetool.LitepalBase.QM_MT_SunMao_Porperty_R;
import cn.qzd.machinetool.helper.CustomerAdapter;
import cn.qzd.machinetool.helper.Loading_view;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.tokenizer.ParseException;

import static cn.qzd.machinetool.Fragment.DataFragment.saveBitmapToLocal;
import static org.litepal.tablemanager.Connector.getWritableDatabase;

/**
 * 显示所有的榫卯数据
 * Created by admin on 2018/6/26.
 */

public class SunMaoData extends AppCompatActivity implements CustomerAdapter.SaveEditListener, OnBannerListener {
    private boolean flag = false;
    private Loading_view loading;
    Bundle bundle;
    Map<Integer, String> map = new HashMap<>();
    String[] infos, hints, porpertyNo, pathall, infos1;
    List<String> list_porpertyname;
    //  public static  SQLHandle sqlHandle;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static String fname, sunmaomain, child_name_no, sunmaotype, fno, child_name;
    private static String[] childc, childname;
    public static List sunmaopath, sunmaono;
    private static int index = -2;
    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sunmaodata);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//隐藏键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//键盘上移不影响布局
        //  sqlHandle=new SQLHandle();
        bundle = getIntent().getExtras();
       /* intent.putExtra("fsunmaoname",bundle.getString("fathername"));//父榫卯名称
        intent.putExtra("csunmao_no",bundle.getString("child_no"));//子榫卯的代号
        intent.putExtra("csunmao_name",bundle.getString("child_name"));//子榫卯的名称*/
        fname = bundle.getString("fsunmaoname");//父榫卯名称
        child_name_no = bundle.getString("csunmao_no");//子榫卯代号
        child_name = bundle.getString("csunmao_name");//子榫卯名称
        child_name_no = child_name_no.replaceAll("([\\]\\[])", "");
        childc = child_name_no.trim().split(",");
       // child_name = child_name.replaceAll("([+*/^()\\]\\[])", "");
        child_name = child_name.replaceAll("([\\]\\[])", "");
        childname = child_name.split(",");
        //父榫卯代号
        List<QM_MT_SunMaoInfo> list1 = LitePal.select("sunmaono")
                .where("sunmaoname=?", fname)
                .limit(1)
                .find(QM_MT_SunMaoInfo.class);
        for (QM_MT_SunMaoInfo qm_mt_a_user : list1) {
            fno = qm_mt_a_user.getSunMaoNo();
        }
        initView();
        /*fatherid=sqlHandle.infoid(fname);//父榫卯id
        fno=sqlHandle.fatherno_id(fatherid);*/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.id_recyclerciew);
        List<String> list_porpertyno = new ArrayList<>();
        List<QM_MT_SunMao_Porperty_R> list2 = LitePal.select("porpertyNo")
                .where("sunmaono=?", fno)
                .find(QM_MT_SunMao_Porperty_R.class);
        for (QM_MT_SunMao_Porperty_R qm_mt_sunMao_porperty_r : list2) {
            list_porpertyno.add(qm_mt_sunMao_porperty_r.getPorpertyNo());
        }
        // porpertyNo=sqlHandle.sunmao_val_porpertyNo(fno).toArray(new String[sqlHandle.sunmao_val_porpertyNo(fno).size()]);//榫卯参数代号
        porpertyNo = list_porpertyno.toArray(new String[list_porpertyno.size()]);
        infos = new String[porpertyNo.length];

        //infos1=sqlHandle.sunmao_val_Porperty(fno).toArray(new String[sqlHandle.sunmao_val_Porperty(fno).size()]);//榫卯参数名称
        infos1 = list_porpertyname.toArray(new String[list_porpertyname.size()]);
        for (int d = 0; d < infos1.length; d++) {
            pref = getSharedPreferences("thickness", Context.MODE_PRIVATE);
            editor = pref.edit();
            if (infos1[d].equals("材料厚度")) {
                editor.putString("thickness", porpertyNo[d]);
            }else if(infos1[d].equals("材料厚度二")){
                editor.putString("thickness2", porpertyNo[d]);
            }else if(infos1[d].equals("材料厚度三")){
                editor.putString("thickness3", porpertyNo[d]);
            }
                editor.commit();
            infos[d] = infos1[d] + "(" + porpertyNo[d] + ")";
        }
        List<String> list_porpertyhints = new ArrayList<>();
        List<QM_MT_SunMao_Porperty_R> list4 = LitePal.select("PorpertyVal")
                .where("sunmaono=?", fno)
                .find(QM_MT_SunMao_Porperty_R.class);
        for (QM_MT_SunMao_Porperty_R qm_mt_sunMao_porperty_r : list4) {
            list_porpertyhints.add(qm_mt_sunMao_porperty_r.getPorpertyVal());
        }
        // hints = sqlHandle.sunmao_val(fno).toArray(new String[sqlHandle.sunmao_val(fno).size()]);//榫卯参数默认值
        hints = list_porpertyhints.toArray(new String[list_porpertyhints.size()]);

        CustomerAdapter customerAdapter = new CustomerAdapter(infos, hints, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(customerAdapter);
        /***************************加载图片*/
        /*mGridView = (GridView)findViewById(R.id.multi_photo_grid);
        datas = new ArrayList<String>();
       for (int i=0;i < childc.length;i++) {
           Bitmap bitmap=null;
           bitmap = sqlHandle.sunmao_type_image(childc[i].trim());
           saveBitmapToLocal(childc[i], bitmap);//保存从数据库获取的图片
           //Log.d("lujing",Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/pics"+pathname);
           datas.add("file:///storage/emulated/0/cache/pics/" + childc[i]);
       }
        mGridView.setAdapter(new ImagesInnerGridViewAdapter(datas));*/
        //Button save= (Button) findViewById(R.id.id_save);
        //动态存储RecyclerView每个item上EditText的内容
        // map.clear();
        if (map.get(0) != null && !map.get(0).trim().equals("")) {
        } else {
            for (int i = 0; i < infos.length; i++) {
                map.put(i, hints[i]);
            }
        }
    }

    public void to_tool(View view) {
        if (flag) {
            Intent intent = new Intent(SunMaoData.this, DataActivity.class);
            intent.putExtra("csunmao_no", child_name_no);
            startActivity(intent);
        } else {
            Toast.makeText(this, "请先保存榫卯数据", 0).show();
        }
    }

    //监听保存
    public void OnSave1(View view) {
        flag = true;
        loading = new Loading_view(this, R.style.ImageloadingDialogStyle);
        loading.show();
        new Handler().postDelayed(new Runnable() {//定义延时任务模仿网络请求
            @Override
            public void run() {
                //处理存储edittext的map
                //如判断客户名称是否填写且不为空格
                //遍历处理map存储的内容既一个榫卯的所有数据
                sunmaopath = new ArrayList();
                sunmaono = new ArrayList();
                for (int i = 0; i < map.size(); i++) {
                    for (int key : map.keySet()) {
                        if (i == key) {
                            sunmaopath.add(map.get(key));
                            break;
                        }
                    }
                    sunmaono.add(porpertyNo[i]);
                    //    }
             /*   pref = getSharedPreferences(pathtype, Context.MODE_PRIVATE);
                editor = pref.edit();
                editor.putString(porpertyNo[i], map.get(i));//存储榫卯数据
                editor.commit();*/
                }
                path();
                loading.dismiss();//3秒后调用关闭加载的方法
            }
        }, 1000);
    }

    ;

    @Override
    public void SaveEdit(int position, String string) {
        //回调处理edittext内容，使用map的好处在于：position确定的情况下，string改变，只会动态改变string内容
        map.put(position, string);
    }

    public void path() {
        for (int j = 0; j < childc.length; j++) {
            //  sonid=sqlHandle.sonid(childc[j].trim(),sunmaomain);

            //pathall = sqlHandle.select_pathtype(childc[j].trim()).toArray(new String[sqlHandle.select_pathtype(childc[j].trim()).size()]);
            List<String> list_pathall = new ArrayList();
            List<QM_MT_SunMao_Path_R> list2 = LitePal.select("PathName")
                    .where("SunMaoNameNo = ?", childc[j].trim())
                    .find(QM_MT_SunMao_Path_R.class);
            for (QM_MT_SunMao_Path_R path_rh : list2) {
                list_pathall.add(path_rh.getPathName());
            }
            pathall = list_pathall.toArray(new String[list_pathall.size()]);
            for (int i = 0; i < pathall.length; i++) {
                String pathone = pathall[i];
                // sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id(pathone, childc[j]));
                //List lists = new ArrayList<String>();
                List sizelist = new ArrayList();
                List newlists = new ArrayList();
                //sqlite查询榫卯一个刀路唯一代号
                int SunMao_Path_ID = -1;
                List<QM_MT_SunMao_Path_R> list = LitePal.select("path_r_id")
                        .where("sunmaonameno = ? and pathname=?", childc[j].trim(), pathone)
                        .find(QM_MT_SunMao_Path_R.class);
                for (QM_MT_SunMao_Path_R path_rh : list) {
                    SunMao_Path_ID = path_rh.getPath_r_id();
                }
                //取得刀路里参数
                List<QM_MT_SunMaoPathParameterVal> list1 = LitePal.select("size")
                        .where("sunmao_path_id = ?", String.valueOf(SunMao_Path_ID))
                        .find(QM_MT_SunMaoPathParameterVal.class);
                for (QM_MT_SunMaoPathParameterVal pathParameterVal : list1) {
                    sizelist.add(pathParameterVal.getSize());
                }
                //lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id(pathone, childc[j].trim()));
                //sizelist = sqlHandle.sunmao_parameter_size(sqlHandle.select_path_r_id(pathone, childc[j].trim()));
                pref = getSharedPreferences(childc[j].trim(), Context.MODE_PRIVATE);

                for (int k = 0; k < sizelist.size(); k++) {
                    newlists.add(CalculationFunctionNew(sizelist.toArray()[k].toString()));

                }
                if (pathone.contains("直圆榫头刀路")) {

                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("直圆榫头刀路", "zys"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    //Toast.makeText(getActivity(),MaterialHeight.getText().toString(),Toast.LENGTH_LONG).show();
                    editor.putString("zys_MaterialHeight", newlists.toArray()[1].toString());
                    editor.putString("zys_MaterialWidth", newlists.toArray()[0].toString());
                    editor.putString("zys_SunMaoLength", newlists.toArray()[2].toString());
                    editor.putString("zys_LeftMargin", newlists.toArray()[5].toString());
                    editor.putString("zys_BottonMargin", newlists.toArray()[4].toString());
                    editor.putString("zys_SunMaoD", newlists.toArray()[3].toString());
                    if (newlists.size()==7) {
                        editor.putString("zys_zsafe", newlists.toArray()[6].toString());
                    }
                    editor.commit();
                }   else if (pathone.contains("多边直扁圆刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("多边直扁圆刀路", "dbzbys"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("dbzbys_MaterialHeight", newlists.toArray()[1].toString());
                    editor.putString("dbzbys_MaterialWidth", newlists.toArray()[0].toString());
                    editor.putString("dbzbysSunMaoLength", newlists.toArray()[2].toString());//2
                    editor.putString("dbzbys_SunMaoWidth", newlists.toArray()[3].toString());
                    editor.putString("dbzbys_SunMaoThickness", newlists.toArray()[4].toString());//4
                    editor.putString("dbzbys_BottonMargin", newlists.toArray()[6].toString());
                    editor.putString("dbzbys_LeftMargin", newlists.toArray()[5].toString());
                    editor.putString("dbzbys_zsafe", newlists.toArray()[7].toString());
                    editor.commit();
                } else if (pathone.contains("直扁圆榫头刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("直扁圆榫头刀路", "zbys"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("zbys_MaterialHeight", newlists.toArray()[1].toString());
                    editor.putString("zbys_MaterialWidth", newlists.toArray()[0].toString());
                    editor.putString("zbys_SunMaoLength", newlists.toArray()[2].toString());//2
                    editor.putString("zbys_SunMaoWidth", newlists.toArray()[3].toString());
                    editor.putString("zbys_SunMaoThickness", newlists.toArray()[4].toString());//4
                    editor.putString("zbys_BottonMargin", newlists.toArray()[6].toString());
                    editor.putString("zbys_LeftMargin", newlists.toArray()[5].toString());
                    if (newlists.size()==8) {
                        editor.putString("zbys_zsafe", newlists.toArray()[7].toString());
                    }
                    editor.commit();
                } else if (pathone.contains("带边直扁圆刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("带边直扁圆刀路", "dbzbys"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("dbzbys_MaterialHeight", newlists.toArray()[1].toString());
                    editor.putString("dbzbys_MaterialWidth", newlists.toArray()[0].toString());
                    editor.putString("dbzbys_SunMaoLength", newlists.toArray()[2].toString());
                    editor.putString("dbzbys_SunMaoWidth", newlists.toArray()[3].toString());
                    editor.putString("dbzbys_SunMaoThickness", newlists.toArray()[4].toString());
                    editor.putString("dbzbys_BottonMargin", newlists.toArray()[6].toString());
                    editor.putString("dbzbys_LeftMargin", newlists.toArray()[5].toString());
                    if (newlists.size()==8) {
                        editor.putString("dbzbys_zsafe", newlists.toArray()[7].toString());
                    }
                    editor.commit();
                } else if (pathone.contains("直扁母榫刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("直扁母榫刀路", "zbms"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("zbms_Groovedepth", newlists.toArray()[1].toString());
                    editor.putString("zbms_Groovelength", newlists.toArray()[0].toString());//宽
                    editor.putString("zbms_Groovewidth", newlists.toArray()[2].toString());//厚
                    if (newlists.size()==4) {
                        editor.putString("zbms_zsafe", newlists.toArray()[3].toString());
                    }
                    editor.commit();

                } else if (pathone.contains("半开口方形闭合榫槽刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("半开口方形闭合榫槽刀路", "bfscdl"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    if (newlists.size() == 4) {
                        editor.putString("bfscdl_Groovewidth", newlists.toArray()[0].toString());
                        editor.putString("bfscdl_Groovedepth", newlists.toArray()[1].toString());
                        editor.putString("bfscdl_Groovelength", newlists.toArray()[2].toString());
                        editor.putString("bfscdl_zsafe", newlists.toArray()[3].toString());
                    }else if (newlists.size() == 5) {
                        editor.putString("bfscdl_Groovewidth", newlists.toArray()[2].toString());
                        editor.putString("bfscdl_Groovedepth", newlists.toArray()[3].toString());
                        editor.putString("bfscdl_Groovelength", newlists.toArray()[4].toString());
                        editor.putString("bfscdl_MaterialHeight", newlists.toArray()[1].toString());
                        editor.putString("bfscdl_MaterialWidth", newlists.toArray()[0].toString());
                    }
//                    editor.putString("bfscdl_MaterialHeight", newlists.toArray()[1].toString());
//                    editor.putString("bfscdl_MaterialWidth", newlists.toArray()[0].toString());

                    editor.commit();
                } else if (pathone.contains("半开口弧形闭合榫槽刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("半开口弧形闭合榫槽刀路", "bhscdl"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    if (newlists.size() == 4) {
                        editor.putString("bhscdl_Groovewidth", newlists.toArray()[0].toString());
                        editor.putString("bhscdl_Groovedepth", newlists.toArray()[1].toString());
                        editor.putString("bhscdl_Groovelength", newlists.toArray()[2].toString());
                        editor.putString("bhscdl_zsafe", newlists.toArray()[3].toString());
                    }else if (newlists.size() == 5) {
                        editor.putString("bhscdl_Groovewidth", newlists.toArray()[2].toString());
                        editor.putString("bhscdl_Groovedepth", newlists.toArray()[3].toString());
                        editor.putString("bhscdl_Groovelength", newlists.toArray()[4].toString());
                        editor.putString("bhscdl_MaterialHeight", newlists.toArray()[1].toString());
                        editor.putString("bhscdl_MaterialWidth", newlists.toArray()[0].toString());
                    }
//                    editor.putString("bhscdl_MaterialHeight", newlists.toArray()[1].toString());
//                    editor.putString("bhscdl_MaterialWidth", newlists.toArray()[0].toString());
                    editor.commit();

                }
                else if (pathone.contains("全开口榫槽刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("全开口榫槽刀路", "qkscdl"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    if (newlists.size()==4) {
                        editor.putString("qkscdl_Groovewidth", newlists.toArray()[0].toString());
                        editor.putString("qkscdl_Groovedepth", newlists.toArray()[1].toString());
                        editor.putString("qkscdl_Groovelength", newlists.toArray()[2].toString());
                        editor.putString("qkscdl_zsafe", newlists.toArray()[3].toString());
                    }else if (newlists.size() == 5) {
                        editor.putString("qkscdl_Groovewidth", newlists.toArray()[2].toString());
                        editor.putString("qkscdl_Groovedepth", newlists.toArray()[3].toString());
                        editor.putString("qkscdl_Groovelength", newlists.toArray()[4].toString());
                        editor.putString("bhscdl_MaterialHeight", newlists.toArray()[1].toString());
                    editor.putString("bhscdl_MaterialWidth", newlists.toArray()[0].toString());
                    }
//                    editor.putString("bhscdl_MaterialHeight", newlists.toArray()[1].toString());
//                    editor.putString("bhscdl_MaterialWidth", newlists.toArray()[0].toString());
                    editor.commit();

                }/*else if (pathone.contains("榫槽刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("榫槽刀路", "scdl"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("scdl_Groovewidth", newlists.toArray()[2].toString());
                    editor.putString("scdl_Groovedepth", newlists.toArray()[3].toString());
                    editor.putString("scdl_Groovelength", newlists.toArray()[4].toString());
                    editor.putString("scdl_MaterialHeight", newlists.toArray()[1].toString());
                    editor.putString("scdl_MaterialWidth", newlists.toArray()[0].toString());
                    editor.commit();
                }*/else if (pathone.contains("T型槽刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("T型槽刀路", "txc"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("scdl_Groovewidth", newlists.toArray()[0].toString());
                    editor.putString("scdl_Groovedepth", newlists.toArray()[1].toString());
                    editor.putString("scdl_Groovelength", newlists.toArray()[2].toString());
                    if (newlists.size()==4) {
                        editor.putString("scdl_zsafe", newlists.toArray()[3].toString());
                    }
                    editor.commit();
                } else if (pathone.contains("母圆榫刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("母圆榫刀路", "mys"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("mys_Groovedepth", newlists.toArray()[4].toString());
                    editor.putString("mys_leftMargin", newlists.toArray()[3].toString());
                    editor.putString("mys_bottomMargin", newlists.toArray()[2].toString());
                    editor.putString("mys_Radius", newlists.toArray()[5].toString());
                    editor.putString("mys_MaterialHeight", newlists.toArray()[1].toString());
                    editor.putString("mys_MaterialWidth", newlists.toArray()[0].toString());
                    if (newlists.size()==7) {
                        editor.putString("mys_zsafe", newlists.toArray()[6].toString());
                    }
                    editor.commit();
                    //获取数据
                } else if (pathone.contains("带边方型刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("带边方型刀路", "dbfxdl"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("dbfxdl_MaterialHeight", newlists.toArray()[1].toString());
                    editor.putString("dbfxdl_MaterialWidth", newlists.toArray()[0].toString());
                    editor.putString("dbfxdl_SunMaoLength", newlists.toArray()[2].toString());
                    editor.putString("dbfxdl_SunMaoWidth", newlists.toArray()[3].toString());
                    editor.putString("dbfxdl_SunMaoThickness", newlists.toArray()[4].toString());
                    editor.putString("dbfxdl_BottonMargin", newlists.toArray()[6].toString());
                    editor.putString("dbfxdl_LeftMargin", newlists.toArray()[5].toString());
                    if (newlists.size()==8) {
                        editor.putString("dbfxdl_zsafe", newlists.toArray()[7].toString());
                    }
                    editor.commit();

                } else if (pathone.contains("方型刀路")) {
                    //pref = getSharedPreferences(childc[j]+pathone, Context.MODE_PRIVATE);
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("方型刀路", "fxdl"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("fxdl_MaterialHeight", newlists.toArray()[1].toString());
                    editor.putString("fxdl_MaterialWidth", newlists.toArray()[0].toString());
                    editor.putString("fxdl_SunMaoLength", newlists.toArray()[2].toString());
                    editor.putString("fxdl_SunMaoWidth", newlists.toArray()[3].toString());
                    editor.putString("fxdl_SunMaoThickness", newlists.toArray()[4].toString());
                    editor.putString("fxdl_BottonMargin", newlists.toArray()[6].toString());
                    editor.putString("fxdl_LeftMargin", newlists.toArray()[5].toString());
                    if (newlists.size()==8) {
                        editor.putString("fxdl_zsafe", newlists.toArray()[7].toString());
                    }
                    editor.commit();

                } else if (pathone.contains("边线加工刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("边线加工刀路", "bxjg"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("bxjg_thickness", newlists.toArray()[2].toString());
                    editor.putString("bxjg_Width", newlists.toArray()[1].toString());
                    editor.putString("bxjg_Length", newlists.toArray()[0].toString());
                    if (newlists.size()==4) {
                        editor.putString("bxjg_zsafe", newlists.toArray()[3].toString());
                    }
                    editor.commit();
                } else if (pathone.contains("U型刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("U型刀路", "uxdl"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("uxdl_MaterialHeight", newlists.toArray()[1].toString());
                    editor.putString("uxdl_MaterialWidth", newlists.toArray()[0].toString());
                    editor.putString("uxdl_SunMaoLength", newlists.toArray()[2].toString());
                    editor.putString("uxdl_SunMaoWidth", newlists.toArray()[3].toString());
                    editor.putString("uxdl_SunMaoThickness", newlists.toArray()[4].toString());
                    editor.putString("uxdl_LeftMargin", newlists.toArray()[5].toString());
                    if (newlists.size()==7) {
                        editor.putString("uxdl_zsafe", newlists.toArray()[6].toString());
                    }
                    editor.commit();
                }else if (pathone.contains("L型刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("L型刀路", "lxdl"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("lxdl_MaterialHeight", newlists.toArray()[1].toString());
                    editor.putString("lxdl_MaterialWidth", newlists.toArray()[0].toString());
                    editor.putString("lxdl_mtHeight", newlists.toArray()[2].toString());
                    editor.putString("lxdl_SunMaoLength", newlists.toArray()[4].toString());
                    editor.putString("lxdl_SunMaoWidth", newlists.toArray()[3].toString());
                    editor.putString("lxdl_SunMaoThickness", newlists.toArray()[7].toString());
                    editor.putString("lxdl_LeftMargin", newlists.toArray()[5].toString());
                    editor.putString("lxdl_BottomMargin", newlists.toArray()[6].toString());
                    editor.putString("lxdl_zsafe", newlists.toArray()[8].toString());
                    editor.commit();
                }else if (pathone.contains("7型刀路")) {
                    pref = getSharedPreferences(childc[j].trim() + pathone.replace("7型刀路", "sxdl"), Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("sxdl_MaterialHeight", newlists.toArray()[1].toString());
                    editor.putString("sxdl_MaterialWidth", newlists.toArray()[0].toString());
                    editor.putString("sxdl_SunMaoLength", newlists.toArray()[2].toString());
                    editor.putString("sxdl_SunMaoThickness", newlists.toArray()[3].toString());
                    editor.putString("sxdl_zsafe", newlists.toArray()[4].toString());
                    editor.commit();
                }else if (pathone.contains("V型刀路")) {
                pref = getSharedPreferences(childc[j].trim() + pathone.replace("V型刀路", "vxdl"), Context.MODE_PRIVATE);
                editor = pref.edit();
                editor.putString("vxdl_SunMaoWidth", newlists.toArray()[1].toString());
                editor.putString("vxdl_SunMaoThickness", newlists.toArray()[0].toString());
                editor.putString("vxdl_zsafe", newlists.toArray()[2].toString());
                editor.commit();
            }
            }
        }
    }

    //计算榫卯刀路材料尺寸公式
    public static double CalculationFunctionNew(String gongsi) {
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
                    double diret = Double.valueOf(iret);
                    int cL = val.length();
                    index = gongsi.indexOf(val, index + 2);
                    //index = gongsi.indexOf(val);
                    if (index > -1) {
                        StringBuffer temps1 = new StringBuffer(gongsi);
                        gongsi = temps1.replace(index, index + cL, String.valueOf(diret)).toString();
                    }
                }
            }
        }
        try {
            Expression expr = Parser.parse(gongsi);
            enddata = expr.evaluate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        index = -2;
        return enddata;
    }

    public static String GetValByPathParaNew(String paraS) {
        if (paraS.length() == 0) return "";
        boolean bSign = false;
        // double tVal = 0;
        String tVal = "";
        for (int k = 0; k < sunmaopath.size(); k++) {
            if (paraS.toLowerCase().equals((sunmaono.toArray()[k].toString()).toLowerCase())) {
                tVal = sunmaopath.toArray()[k].toString();
                bSign = true;
                break;
            }
        }
        if (!bSign) {
            tVal = paraS;
        }
        return tVal;
    }

    //    public class ImagesInnerGridViewAdapter extends BaseAdapter {
//
//        private List<String> datas;
//
//        public ImagesInnerGridViewAdapter(List<String> datas) {
//            this.datas = datas;
//        }
//
//        @Override
//        public int getCount() {
//            return datas.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return position;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//
//            final SquareCenterImageView imageView = new SquareCenterImageView(SunMaoData.this);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageLoader = ImageLoader.getInstance();
//            imageLoader.init(ImageLoaderConfiguration.createDefault(SunMaoData.this));
//            imageLoader.displayImage(datas.get(position), imageView);
//
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(SunMaoData.this, SpaceImageDetailActivity.class);
//                    intent.putExtra("images", (ArrayList<String>) datas);
//                    intent.putExtra("position", position);
//                    int[] location = new int[2];
//                    imageView.getLocationOnScreen(location);
//                    intent.putExtra("locationX", location[0]);
//                    intent.putExtra("locationY", location[1]);
//
//                    intent.putExtra("width", imageView.getWidth());
//                    intent.putExtra("height", imageView.getHeight());
//                    startActivity(intent);
//                    ((Activity) SunMaoData.this).overridePendingTransition(0, 0);
//                }
//            });
//            return imageView;
//        }
//    }
    private void initView() {
        //获取榫卯参数名
        list_porpertyname = new ArrayList<>();
        List<QM_MT_SunMao_Porperty_R> list3 = LitePal.select("SunMaoPorperty")
                .where("sunmaono=?", fno)
                .find(QM_MT_SunMao_Porperty_R.class);
        for (QM_MT_SunMao_Porperty_R qm_mt_sunMao_porperty_r : list3) {
            list_porpertyname.add(qm_mt_sunMao_porperty_r.getSunMaoPorperty());
        }

        banner = (Banner) findViewById(R.id.banner);
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();
        for (int j=0;j<list_porpertyname.size();j++) {
            Bitmap bitmap = null;
            List<QM_MT_SunMao_Porperty_R> list1 = LitePal.select("Image")
                    .where("SunMaoNo = ? and sunmaoporperty=?", fno.trim(),list_porpertyname.get(j) ).find(QM_MT_SunMao_Porperty_R.class);
            for (QM_MT_SunMao_Porperty_R qm_mt_sunMao_porperty_r : list1) {
                    byte[] in = qm_mt_sunMao_porperty_r.getImage();
                if (in==null){
                    Resources res = SunMaoData.this.getResources();
                    bitmap = BitmapFactory.decodeResource(res,R.drawable.nopictures);
                    //bitmap = new BitmapDrawable(R.drawable.nopictures);

                }else {
                    bitmap = BitmapFactory.decodeByteArray(in, 0, in.length);
                }
            }
            saveBitmapToLocal(fno + list_porpertyname.get(j), bitmap);//保存从数据库获取的图片
            //Log.d("lujing",Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/pics"+pathname);
            list_path.add("file:///storage/emulated/0/cache/pics/" + fno + list_porpertyname.get(j));
            list_title.add(list_porpertyname.get(j));
        }
//            List<QM_MT_SunMaoInfo> list1 = LitePal.select("sImage")
//                    .where("SunMaoName = ? and ParentID !=0", childname[i].trim()).find(QM_MT_SunMaoInfo.class);
//            for (QM_MT_SunMaoInfo qm_mt_sunMaoInfo : list1) {
//                byte[] in = qm_mt_sunMaoInfo.getsImage();
//                bitmap = BitmapFactory.decodeByteArray(in, 0, in.length);
//            }
            //bitmap = sqlHandle.sunmao_type_image(childname[i].trim());
            //saveBitmapToLocal(childc[i], bitmap);//保存从数据库获取的图片
            //Log.d("lujing",Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/pics"+pathname);
//            list_path.add("file:///storage/emulated/0/cache/pics/" + childc[i]);
//            list_title.add(childname[i]);
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
    }

    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        // Log.i("tag", "你点了第"+position+"张轮播图");
        // Toast.makeText(SunMaoData.this,"你点了第"+position+"张轮播图",0).show();
        Intent intent = new Intent(SunMaoData.this, SpaceImageDetailActivity.class);
        intent.putExtra("images", (ArrayList<String>) list_path);
        intent.putExtra("position", position);
        int[] location = new int[2];
        banner.getLocationOnScreen(location);
        intent.putExtra("locationX", location[0]);
        intent.putExtra("locationY", location[1]);

        intent.putExtra("width", banner.getWidth());
        intent.putExtra("height", banner.getHeight());
        startActivity(intent);
        ((Activity) SunMaoData.this).overridePendingTransition(0, 0);
    }

    //自定义的图片加载器
    private class MyLoader extends com.youth.banner.loader.ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }
}
