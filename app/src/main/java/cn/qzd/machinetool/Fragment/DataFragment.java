package cn.qzd.machinetool.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qzd.machinetool.DataActivity;
import cn.qzd.machinetool.R;
import cn.qzd.machinetool.SpaceImageDetailActivity;
import cn.qzd.machinetool.helper.CustomerAdapter;
import cn.qzd.machinetool.helper.SQLHandle;
import cn.qzd.machinetool.helper.SquareCenterImageView;


/**
 * Created by admin on 2018/5/25.
 */

public class DataFragment extends Fragment implements CustomerAdapter.SaveEditListener{
    //region description
    Map<Integer, String> map = new HashMap<>();
    String[] infos;
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/pics";
    private List<String> lists, datas;
    int zystdl = 1, zystdl1 = 1, zby = 1, zby1 = 1, zbm = 1, zbm1 = 1, scdl = 1, mysdl = 1, fxdl = 1, bxjg = 1, uxdl = 1, fxdl1 = 1, bxjg1 = 1, bxjg2 = 1, bxjg3 = 1, scdl1 = 1;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
   // SQLHandle sqlHandle = new SQLHandle();
    private Spinner spinner;
    private GridView mGridView;
//    private Context context;
private Context context;
    protected ImageLoader imageLoader;
    public View rootView;
    private String pathname;
    private String data;
    private Button save, daolu,id_save;
    private EditText zys_MaterialHeight, zys_MaterialWidth, zys_SunMaoLength, zys_SunMaoD, zys_LeftMargin, zys_BottonMargin,
            zbys_MaterialHeight, zbys_MaterialWidth, zbys_SunMaoLength, zbys_SunMaoWidth, zbys_SunMaoThickness, zbys_LeftMargin, zbys_BottonMargin,
            zbms_Groovewidth, zbms_Groovedepth, zbms_Groovelength, zbms_MaterialHeight, zbms_MaterialWidth, zbms_LeftMargin, zbms_BottonMargin,
            scdl_Groovewidth, scdl_Groovedepth, scdl_Groovelength, scdl_MaterialHeight, scdl_MaterialWidth, scdl_LeftMargin, scdl_BottonMargin,
            mys_Groovedepth, mys_CentrePoint, mys_Radius, mys_MaterialHeight, mys_MaterialWidth,
            fxdl_MaterialHeight, fxdl_MaterialWidth, fxdl_SunMaoLength, fxdl_SunMaoWidth, fxdl_SunMaoThickness, fxdl_LeftMargin, fxdl_BottonMargin,
            bxjg_Groovedepth, bxjg_Length, bxjg_Width, bxjg_thickness,
            uxdl_MaterialHeight, uxdl_MaterialWidth, uxdl_SunMaoLength, uxdl_SunMaoWidth, uxdl_SunMaoThickness, uxdl_LeftMargin;
    private LinearLayout ll_zys_MaterialHeight, ll_zys_MaterialWidth, ll_zys_SunMaoLength, ll_zys_SunMaoD, ll_zys_LeftMargin, ll_zys_BottonMargin,
            ll_zbys_MaterialHeight, ll_zbys_MaterialWidth, ll_zbys_SunMaoLength, ll_zbys_SunMaoWidth, ll_zbys_SunMaoThickness, ll_zbys_LeftMargin, ll_zbys_BottonMargin,
            ll_zbms_Groovewidth, ll_zbms_Groovedepth, ll_zbms_Groovelength, ll_zbms_MaterialHeight, ll_zbms_MaterialWidth, ll_zbms_LeftMargin, ll_zbms_BottonMargin,
            ll_scdl_Groovewidth, ll_scdl_Groovedepth, ll_scdl_Groovelength, ll_scdl_MaterialHeight, ll_scdl_MaterialWidth, ll_scdl_LeftMargin, ll_scdl_BottonMargin,
            ll_mys_Groovedepth, ll_mys_CentrePoint, ll_mys_Radius, ll_mys_MaterialHeight, ll_mys_MaterialWidth,
            ll_fxdl_MaterialHeight, ll_fxdl_MaterialWidth, ll_fxdl_SunMaoLength, ll_fxdl_SunMaoWidth, ll_fxdl_SunMaoThickness, ll_fxdl_LeftMargin, ll_fxdl_BottonMargin,
            ll_bxjg_Groovedepth, ll_bxjg_Length, ll_bxjg_Width, ll_bxjg_thickness,
            ll_uxdl_MaterialHeight, ll_uxdl_MaterialWidth, ll_uxdl_SunMaoLength, ll_uxdl_SunMaoWidth, ll_uxdl_SunMaoThickness, ll_uxdl_LeftMargin;
    RecyclerView recyclerView;
    //endregion description
    // 2.1 定义用来与外部activity交互，获取到宿主activity
  //  private FragmentInteraction listterner;


    // 1 定义了所有activity必须实现的接口方法
  /*  public interface FragmentInteraction {
        void process(String str);
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.data_frament, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.multi_photo_grid);
        spinner = (Spinner) rootView.findViewById(R.id.spinner_pathtype);
        datas = new ArrayList<String>();
       // Bitmap bitmap //sqlHandle.sunmao_type_image(pathname);//0:代号1：mianid
        //saveBitmapToLocal(pathname, bitmap);//保存从数据库获取的图片
        //Log.d("lujing",Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache/pics"+pathname);
        datas.add("file:///storage/emulated/0/cache/pics/" + pathname);
      /*  mGridView.setAdapter(new DataFragment.ImagesInnerGridViewAdapter(datas));
        //ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sqlHandle.select_pathtype(pathname));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sqlHandle.select_pathtype(pathname));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter1);*/
     //   spinner.setOnItemSelectedListener(listener);
//        Toast.makeText(getActivity(),pathname,0).show();
       // recyclerView= (RecyclerView)rootView.findViewById(R.id.id_recyclerciew);
        //设置客户信息界面
//        for (int f=0;f<sqlHandle.path_val(pathname).size();f++){
//            infos[f]=sqlHandle.path_val(pathname).get(f);
//        }
        //infos=sqlHandle.sunmao_val_Porperty(pathname).toArray(new String[sqlHandle.sunmao_val_Porperty(pathname).size()]);
        // infos=sqlHandle.path_val(pathname);

        // infos = getResources().getStringArray(R.array.customer_info);
        //   String[] hints = getResources().getStringArray(R.array.customer_info_hint);
      /*  String[] hints = sqlHandle.sunmao_val(pathname).toArray(new String[sqlHandle.sunmao_val(pathname).size()]);;
        CustomerAdapter customerAdapter = new CustomerAdapter(infos, hints, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(customerAdapter);*/
        //Button save= (Button) findViewById(R.id.id_save);
        //动态存储RecyclerView每个item上EditText的内容
        //map.clear();
        initView();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        id_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //处理存储edittext的map
//                //如判断客户名称是否填写且不为空格
//                if (map.get(0) != null && !map.get(0).trim().equals("")) {
//                    //遍历处理map存储的内容
//                    for (int i = 0; i < infos.length; i++) {
//                        //do something
//                    }
//
//                } else {
//                    Toast.makeText(getActivity(), "客户名称必填且不可为空", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.equals("直圆榫头刀路")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    //Toast.makeText(getActivity(),MaterialHeight.getText().toString(),Toast.LENGTH_LONG).show();
                    editor.putString("zys_MaterialHeight", zys_MaterialHeight.getText().toString());
                    editor.putString("zys_MaterialWidth", zys_MaterialWidth.getText().toString());
                    editor.putString("zys_SunMaoLength", zys_SunMaoLength.getText().toString());
                    editor.putString("zys_LeftMargin", zys_LeftMargin.getText().toString());
                    editor.putString("zys_BottonMargin", zys_BottonMargin.getText().toString());
                    editor.putString("zys_SunMaoD", zys_SunMaoD.getText().toString());
                    editor.commit();
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    zys_MaterialHeight.setText(pref.getString("zys_MaterialHeight", ""));//材料高度
                    zys_MaterialWidth.setText(pref.getString("zys_MaterialWidth", ""));//材料宽度
                    zys_SunMaoLength.setText(pref.getString("zys_SunMaoLength", ""));//榫卯长度
                    zys_BottonMargin.setText(pref.getString("zys_BottonMargin", ""));//下边距
                    zys_LeftMargin.setText(pref.getString("zys_LeftMargin", ""));//左边距
                    zys_SunMaoD.setText(pref.getString("zys_SunMaoD", ""));//榫头半径
                } else if (data.equals("直圆榫头刀路1")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("zys_MaterialHeight", zys_MaterialHeight.getText().toString());
                    editor.putString("zys_MaterialWidth", zys_MaterialWidth.getText().toString());
                    editor.putString("zys_SunMaoLength", zys_SunMaoLength.getText().toString());
                    editor.putString("zys_LeftMargin", zys_LeftMargin.getText().toString());
                    editor.putString("zys_BottonMargin", zys_BottonMargin.getText().toString());
                    editor.putString("zys_SunMaoD", zys_SunMaoD.getText().toString());
                    editor.commit();
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    zys_MaterialHeight.setText(pref.getString("zys_MaterialHeight", ""));//材料高度
                    zys_MaterialWidth.setText(pref.getString("zys_MaterialWidth", ""));//材料宽度
                    zys_SunMaoLength.setText(pref.getString("zys_SunMaoLength", ""));//榫卯长度
                    zys_BottonMargin.setText(pref.getString("zys_BottonMargin", ""));//下边距
                    zys_LeftMargin.setText(pref.getString("zys_LeftMargin", ""));//左边距
                    zys_SunMaoD.setText(pref.getString("zys_SunMaoD", ""));//榫头半径
                } else if (data.equals("直扁圆榫头刀路")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("zbys_MaterialHeight", zbys_MaterialHeight.getText().toString());
                    editor.putString("zbys_MaterialWidth", zbys_MaterialWidth.getText().toString());
                    editor.putString("zbys_SunMaoLength", zbys_SunMaoLength.getText().toString());
                    editor.putString("zbys_SunMaoWidth", zbys_SunMaoWidth.getText().toString());
                    editor.putString("zbys_SunMaoThickness", zbys_SunMaoThickness.getText().toString());
                    editor.putString("zbys_BottonMargin", zbys_BottonMargin.getText().toString());
                    editor.putString("zbys_LeftMargin", zbys_LeftMargin.getText().toString());
                    editor.commit();
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    zbys_MaterialHeight.setText(pref.getString("zbys_MaterialHeight", ""));//材料高度
                    zbys_MaterialWidth.setText(pref.getString("zbys_MaterialWidth", ""));//材料宽度
                    zbys_SunMaoLength.setText(pref.getString("zbys_SunMaoLength", ""));//榫卯长度
                    zbys_BottonMargin.setText(pref.getString("zbys_BottonMargin", ""));//下边距
                    zbys_SunMaoWidth.setText(pref.getString("zbys_SunMaoWidth", ""));//左边距
                    zbys_SunMaoThickness.setText(pref.getString("zbys_SunMaoThickness", ""));//左边距
                    zbys_LeftMargin.setText(pref.getString("zbys_LeftMargin", ""));//左边距
                } else if (data.equals("方型刀路")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("fxdl_MaterialHeight", fxdl_MaterialHeight.getText().toString());
                    editor.putString("fxdl_MaterialWidth", fxdl_MaterialWidth.getText().toString());
                    editor.putString("fxdl_SunMaoLength", fxdl_SunMaoLength.getText().toString());
                    editor.putString("fxdl_SunMaoWidth", fxdl_SunMaoWidth.getText().toString());
                    editor.putString("fxdl_SunMaoThickness", fxdl_SunMaoThickness.getText().toString());
                    editor.putString("fxdl_BottonMargin", fxdl_BottonMargin.getText().toString());
                    editor.putString("fxdl_LeftMargin", fxdl_LeftMargin.getText().toString());
                    editor.commit();
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    fxdl_MaterialHeight.setText(pref.getString("fxdl_MaterialHeight", ""));//材料高度
                    fxdl_MaterialWidth.setText(pref.getString("fxdl_MaterialWidth", ""));//材料宽度
                    fxdl_SunMaoLength.setText(pref.getString("fxdl_SunMaoLength", ""));//榫卯长度
                    fxdl_BottonMargin.setText(pref.getString("fxdl_BottonMargin", ""));//下边距
                    fxdl_LeftMargin.setText(pref.getString("fxdl_LeftMargin", ""));//左边距
                    fxdl_SunMaoWidth.setText(pref.getString("fxdl_SunMaoWidth", ""));//榫头半径
                    fxdl_SunMaoThickness.setText(pref.getString("fxdl_SunMaoThickness", ""));//榫头半径
                } else if (data.equals("直扁母榫刀路")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("zbms_Groovedepth", zbms_Groovedepth.getText().toString());
                    editor.putString("zbms_Groovelength", zbms_Groovelength.getText().toString());//宽
                    editor.putString("zbms_Groovewidth", zbms_Groovewidth.getText().toString());//厚
                    editor.putString("zbms_MaterialHeight", zbms_MaterialHeight.getText().toString());
                    editor.putString("zbms_MaterialWidth", zbms_MaterialWidth.getText().toString());
                    editor.putString("zbms_LeftMargin", zbms_LeftMargin.getText().toString());
                    editor.putString("zbms_BottonMargin", zbms_BottonMargin.getText().toString());
                    editor.commit();
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    zbms_Groovedepth.setText(pref.getString("zbms_Groovedepth", ""));//槽深
                    zbms_Groovelength.setText(pref.getString("zbms_Groovelength", ""));//槽长
                    zbms_Groovewidth.setText(pref.getString("zbms_Groovewidth", ""));//槽宽
                    zbms_MaterialHeight.setText(pref.getString("zbms_MaterialHeight", ""));//槽宽
                    zbms_MaterialWidth.setText(pref.getString("zbms_MaterialWidth", ""));//槽宽
                    zbms_LeftMargin.setText(pref.getString("zbms_LeftMargin", ""));//槽宽
                    zbms_BottonMargin.setText(pref.getString("zbms_BottonMargin", ""));//槽宽
                } else if (data.equals("直扁母榫刀路1")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("zbms_Groovedepth", zbms_Groovedepth.getText().toString());
                    editor.putString("zbms_Groovelength", zbms_Groovelength.getText().toString());//宽
                    editor.putString("zbms_Groovewidth", zbms_Groovewidth.getText().toString());//厚
                    editor.putString("zbms_MaterialHeight", zbms_MaterialHeight.getText().toString());
                    editor.putString("zbms_MaterialWidth", zbms_MaterialWidth.getText().toString());
                    editor.putString("zbms_LeftMargin", zbms_LeftMargin.getText().toString());
                    editor.putString("zbms_BottonMargin", zbms_BottonMargin.getText().toString());
                    editor.commit();
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    zbms_Groovedepth.setText(pref.getString("zbms_Groovedepth", ""));//槽深
                    zbms_Groovelength.setText(pref.getString("zbms_Groovelength", ""));//槽长
                    zbms_Groovewidth.setText(pref.getString("zbms_Groovewidth", ""));//槽宽
                    zbms_MaterialHeight.setText(pref.getString("zbms_MaterialHeight", ""));//槽宽
                    zbms_MaterialWidth.setText(pref.getString("zbms_MaterialWidth", ""));//槽宽
                    zbms_LeftMargin.setText(pref.getString("zbms_LeftMargin", ""));//槽宽
                    zbms_BottonMargin.setText(pref.getString("zbms_BottonMargin", ""));//槽宽
                } else if (data.equals("榫槽刀路")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("scdl_Groovewidth", scdl_Groovewidth.getText().toString());
                    editor.putString("scdl_Groovedepth", scdl_Groovedepth.getText().toString());
                    editor.putString("scdl_Groovelength", scdl_Groovelength.getText().toString());
                    editor.putString("scdl_MaterialHeight", scdl_MaterialHeight.getText().toString());
                    editor.putString("scdl_MaterialWidth", scdl_MaterialWidth.getText().toString());
                    editor.putString("scdl_LeftMargin", scdl_LeftMargin.getText().toString());
                    editor.putString("scdl_BottonMargin", scdl_BottonMargin.getText().toString());
                    editor.commit();
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    scdl_Groovewidth.setText(pref.getString("scdl_Groovewidth", ""));//材料高度
                    scdl_Groovedepth.setText(pref.getString("scdl_Groovedepth", ""));//材料宽度
                    scdl_Groovelength.setText(pref.getString("scdl_Groovelength", ""));//榫卯长度
                    scdl_MaterialHeight.setText(pref.getString("scdl_MaterialHeight", ""));//榫卯长度
                    scdl_MaterialWidth.setText(pref.getString("scdl_MaterialWidth", ""));//榫卯长度
                    scdl_LeftMargin.setText(pref.getString("scdl_LeftMargin", ""));//榫卯长度
                    scdl_BottonMargin.setText(pref.getString("scdl_BottonMargin", ""));//榫卯长度
                } else if (data.equals("榫槽刀路1")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("scdl_Groovewidth", scdl_Groovewidth.getText().toString());
                    editor.putString("scdl_Groovedepth", scdl_Groovedepth.getText().toString());
                    editor.putString("scdl_Groovelength", scdl_Groovelength.getText().toString());
                    editor.putString("scdl_MaterialHeight", scdl_MaterialHeight.getText().toString());
                    editor.putString("scdl_MaterialWidth", scdl_MaterialWidth.getText().toString());
                    editor.putString("scdl_LeftMargin", scdl_LeftMargin.getText().toString());
                    editor.putString("scdl_BottonMargin", scdl_BottonMargin.getText().toString());
                    editor.commit();
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    scdl_Groovewidth.setText(pref.getString("scdl_Groovewidth", ""));//材料高度
                    scdl_Groovedepth.setText(pref.getString("scdl_Groovedepth", ""));//材料宽度
                    scdl_Groovelength.setText(pref.getString("scdl_Groovelength", ""));//榫卯长度
                    scdl_MaterialHeight.setText(pref.getString("scdl_MaterialHeight", ""));//榫卯长度
                    scdl_MaterialWidth.setText(pref.getString("scdl_MaterialWidth", ""));//榫卯长度
                    scdl_LeftMargin.setText(pref.getString("scdl_LeftMargin", ""));//榫卯长度
                    scdl_BottonMargin.setText(pref.getString("scdl_BottonMargin", ""));//榫卯长度
                } else if (data.equals("母圆榫刀路")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("mys_Groovedepth", mys_Groovedepth.getText().toString());
                    editor.putString("mys_CentrePoint", mys_CentrePoint.getText().toString());
                    editor.putString("mys_Radius", mys_Radius.getText().toString());
                    editor.putString("mys_MaterialHeight", mys_MaterialHeight.getText().toString());
                    editor.putString("mys_MaterialWidth", mys_MaterialWidth.getText().toString());
                    editor.commit();
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    mys_Groovedepth.setText(pref.getString("mys_Groovedepth", ""));//槽深
                    mys_CentrePoint.setText(pref.getString("mys_CentrePoint", ""));//槽长
                    mys_Radius.setText(pref.getString("mys_Radius", ""));//槽宽
                    mys_MaterialHeight.setText(pref.getString("mys_MaterialHeight", ""));//槽宽
                    mys_MaterialWidth.setText(pref.getString("mys_MaterialWidth", ""));//槽宽
                } else if (data.equals("边线加工刀路")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("bxjg_thickness", bxjg_thickness.getText().toString());
                    editor.putString("bxjg_Width", bxjg_Width.getText().toString());
                    editor.putString("bxjg_Length", bxjg_Length.getText().toString());
                    editor.commit();
                    bxjg++;
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    bxjg_thickness.setText(pref.getString("bxjg_thickness", ""));//槽长
                    bxjg_Width.setText(pref.getString("bxjg_Width", ""));//槽宽
                    bxjg_Length.setText(pref.getString("bxjg_Length", ""));//槽宽
                } else if (data.equals("边线加工刀路1")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("bxjg_thickness", bxjg_thickness.getText().toString());
                    editor.putString("bxjg_Width", bxjg_Width.getText().toString());
                    editor.putString("bxjg_Length", bxjg_Length.getText().toString());
                    editor.commit();
                    bxjg++;
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    bxjg_thickness.setText(pref.getString("bxjg_thickness", ""));//槽长
                    bxjg_Width.setText(pref.getString("bxjg_Width", ""));//槽宽
                    bxjg_Length.setText(pref.getString("bxjg_Length", ""));//槽宽
                } else if (data.equals("边线加工刀路2")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("bxjg_thickness", bxjg_thickness.getText().toString());
                    editor.putString("bxjg_Width", bxjg_Width.getText().toString());
                    editor.putString("bxjg_Length", bxjg_Length.getText().toString());
                    editor.commit();
                    bxjg++;
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    bxjg_thickness.setText(pref.getString("bxjg_thickness", ""));//槽长
                    bxjg_Width.setText(pref.getString("bxjg_Width", ""));//槽宽
                    bxjg_Length.setText(pref.getString("bxjg_Length", ""));//槽宽
                } else if (data.equals("边线加工刀路3")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("bxjg_thickness", bxjg_thickness.getText().toString());
                    editor.putString("bxjg_Width", bxjg_Width.getText().toString());
                    editor.putString("bxjg_Length", bxjg_Length.getText().toString());
                    editor.commit();
                    bxjg++;
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    bxjg_thickness.setText(pref.getString("bxjg_thickness", ""));//槽长
                    bxjg_Width.setText(pref.getString("bxjg_Width", ""));//槽宽
                    bxjg_Length.setText(pref.getString("bxjg_Length", ""));//槽宽
                } else if (data.equals("U型刀路")) {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("uxdl_MaterialHeight", uxdl_MaterialHeight.getText().toString());
                    editor.putString("uxdl_MaterialWidth", uxdl_MaterialWidth.getText().toString());
                    editor.putString("uxdl_SunMaoLength", uxdl_SunMaoLength.getText().toString());
                    editor.putString("uxdl_SunMaoWidth", uxdl_SunMaoWidth.getText().toString());
                    editor.putString("uxdl_SunMaoThickness", uxdl_SunMaoThickness.getText().toString());
                    editor.putString("uxdl_LeftMargin", uxdl_LeftMargin.getText().toString());
                    editor.commit();
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    uxdl_MaterialHeight.setText(pref.getString("uxdl_MaterialHeight", ""));//槽深
                    uxdl_MaterialWidth.setText(pref.getString("uxdl_MaterialWidth", ""));//槽长
                    uxdl_SunMaoLength.setText(pref.getString("uxdl_SunMaoLength", ""));//槽宽
                    uxdl_SunMaoWidth.setText(pref.getString("uxdl_SunMaoWidth", ""));//槽宽
                    uxdl_SunMaoThickness.setText(pref.getString("uxdl_SunMaoThickness", ""));//槽宽
                    uxdl_LeftMargin.setText(pref.getString("uxdl_LeftMargin", ""));//槽宽
                }
            }
        });
    }

    public class ImagesInnerGridViewAdapter extends BaseAdapter {

        private List<String> datas;

        public ImagesInnerGridViewAdapter(List<String> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final SquareCenterImageView imageView = new SquareCenterImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
            imageLoader.displayImage(datas.get(position), imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(), SpaceImageDetailActivity.class);
                    intent.putExtra("images", (ArrayList<String>) datas);
                    intent.putExtra("position", position);
                    int[] location = new int[2];
                    imageView.getLocationOnScreen(location);
                    intent.putExtra("locationX", location[0]);
                    intent.putExtra("locationY", location[1]);

                    intent.putExtra("width", imageView.getWidth());
                    intent.putExtra("height", imageView.getHeight());
                    startActivity(intent);
                    ((Activity) getActivity()).overridePendingTransition(0, 0);
                }
            });
            return imageView;
        }
    }

  /*  AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //  String data1 = match(spinner.getSelectedItem().toString());
            data = spinner.getSelectedItem().toString();
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id(data, pathname));
            //Toast.makeText(getActivity(), sqlHandle.sunmao_AB(sqlHandle.select_path_r_id(data, pathname)).toString(), Toast.LENGTH_LONG).show();//查询数据库里面的默认数据;
            List ab = new ArrayList();
            ab = sqlHandle.sunmao_AB(sqlHandle.select_path_r_id(data, pathname));
            for (int i = 0; i < ab.size(); i++) {
                Log.d("AB", data + ab.get(i).toString() + "\n");
            }
            if (data.equals("直圆榫头刀路") || data.equals("直圆榫头刀路1")) {
                //  listterner.process("直圆榫头刀路");传递到activity
                // Toast.makeText(getActivity(), "123456", Toast.LENGTH_SHORT).show();
//            pathid=sqlHandle.select_pathid(data);
//            sqlHandle.select_data(pathid);
//            Toast.makeText(getActivity(),sqlHandle.select_data(pathid).toString(),Toast.LENGTH_LONG).show();
                //region description
                ll_zys_MaterialHeight.setVisibility(view.VISIBLE);
                ll_zys_MaterialWidth.setVisibility(view.VISIBLE);
                ll_zys_SunMaoLength.setVisibility(view.VISIBLE);
                ll_zys_SunMaoD.setVisibility(view.VISIBLE);
                ll_zys_LeftMargin.setVisibility(view.VISIBLE);
                ll_zys_BottonMargin.setVisibility(view.VISIBLE);
                ll_zbys_MaterialHeight.setVisibility(view.GONE);
                ll_zbys_MaterialWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoLength.setVisibility(view.GONE);
                ll_zbys_SunMaoWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoThickness.setVisibility(view.GONE);
                ll_zbys_LeftMargin.setVisibility(view.GONE);
                ll_zbys_BottonMargin.setVisibility(view.GONE);
                ll_zbms_Groovewidth.setVisibility(view.GONE);
                ll_zbms_Groovedepth.setVisibility(view.GONE);
                ll_zbms_Groovelength.setVisibility(view.GONE);
                ll_zbms_MaterialHeight.setVisibility(view.GONE);
                ll_zbms_MaterialWidth.setVisibility(view.GONE);
                ll_zbms_LeftMargin.setVisibility(view.GONE);
                ll_zbms_BottonMargin.setVisibility(view.GONE);
                ll_scdl_Groovewidth.setVisibility(view.GONE);
                ll_scdl_Groovedepth.setVisibility(view.GONE);
                ll_scdl_Groovelength.setVisibility(view.GONE);
                ll_scdl_MaterialHeight.setVisibility(view.GONE);
                ll_scdl_MaterialWidth.setVisibility(view.GONE);
                ll_scdl_LeftMargin.setVisibility(view.GONE);
                ll_scdl_BottonMargin.setVisibility(view.GONE);
                ll_mys_Groovedepth.setVisibility(view.GONE);
                ll_mys_MaterialHeight.setVisibility(view.GONE);
                ll_mys_MaterialWidth.setVisibility(view.GONE);
                ll_mys_CentrePoint.setVisibility(view.GONE);
                ll_mys_Radius.setVisibility(view.GONE);
                ll_fxdl_MaterialHeight.setVisibility(view.GONE);
                ll_fxdl_MaterialWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoLength.setVisibility(view.GONE);
                ll_fxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_fxdl_LeftMargin.setVisibility(view.GONE);
                ll_fxdl_BottonMargin.setVisibility(view.GONE);
                ll_bxjg_Groovedepth.setVisibility(view.GONE);
                ll_bxjg_Length.setVisibility(view.GONE);
                ll_bxjg_Width.setVisibility(view.GONE);
                ll_bxjg_thickness.setVisibility(view.GONE);
                ll_uxdl_MaterialHeight.setVisibility(view.GONE);
                ll_uxdl_MaterialWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoLength.setVisibility(view.GONE);
                ll_uxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_uxdl_LeftMargin.setVisibility(view.GONE);
                //endregion description
                if (data.equals("直圆榫头刀路") && zystdl == 1) {
                    zys_MaterialHeight.setText(lists.toArray()[3].toString());//材料高度
                    zys_MaterialWidth.setText(lists.toArray()[1].toString());//材料宽度
                    zys_SunMaoLength.setText(lists.toArray()[5].toString());//榫卯长度
                    zys_BottonMargin.setText(lists.toArray()[11].toString());//下边距
                    zys_LeftMargin.setText(lists.toArray()[9].toString());//左边距
                    zys_SunMaoD.setText(lists.toArray()[7].toString());//榫头半径
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    //Toast.makeText(getActivity(),MaterialHeight.getText().toString(),Toast.LENGTH_LONG).show();
                    editor.putString("zys_MaterialHeight", zys_MaterialHeight.getText().toString());
                    editor.putString("zys_MaterialWidth", zys_MaterialWidth.getText().toString());
                    editor.putString("zys_SunMaoLength", zys_SunMaoLength.getText().toString());
                    editor.putString("zys_LeftMargin", zys_LeftMargin.getText().toString());
                    editor.putString("zys_BottonMargin", zys_BottonMargin.getText().toString());
                    editor.putString("zys_SunMaoD", zys_SunMaoD.getText().toString());
                    editor.commit();
                    zystdl++;
                } else if (data.equals("直圆榫头刀路1") && zystdl1 == 1) {
                    zys_MaterialHeight.setText(lists.toArray()[3].toString());//材料高度
                    zys_MaterialWidth.setText(lists.toArray()[1].toString());//材料宽度
                    zys_SunMaoLength.setText(lists.toArray()[5].toString());//榫卯长度
                    zys_BottonMargin.setText(lists.toArray()[11].toString());//下边距
                    zys_LeftMargin.setText(lists.toArray()[9].toString());//左边距
                    zys_SunMaoD.setText(lists.toArray()[7].toString());//榫头半径
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("zys_MaterialHeight", zys_MaterialHeight.getText().toString());
                    editor.putString("zys_MaterialWidth", zys_MaterialWidth.getText().toString());
                    editor.putString("zys_SunMaoLength", zys_SunMaoLength.getText().toString());
                    editor.putString("zys_LeftMargin", zys_LeftMargin.getText().toString());
                    editor.putString("zys_BottonMargin", zys_BottonMargin.getText().toString());
                    editor.putString("zys_SunMaoD", zys_SunMaoD.getText().toString());
                    editor.commit();
                    zystdl1++;
                } else {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    zys_MaterialHeight.setText(pref.getString("zys_MaterialHeight", ""));//材料高度
                    zys_MaterialWidth.setText(pref.getString("zys_MaterialWidth", ""));//材料宽度
                    zys_SunMaoLength.setText(pref.getString("zys_SunMaoLength", ""));//榫卯长度
                    zys_BottonMargin.setText(pref.getString("zys_BottonMargin", ""));//下边距
                    zys_LeftMargin.setText(pref.getString("zys_LeftMargin", ""));//左边距
                    zys_SunMaoD.setText(pref.getString("zys_SunMaoD", ""));//榫头半径
                }
            } else if (data.equals("直扁圆榫头刀路") || data.equals("直扁圆榫头刀路1")) {
                //region description
                ll_zbys_MaterialHeight.setVisibility(view.VISIBLE);
                ll_zbys_MaterialWidth.setVisibility(view.VISIBLE);
                ll_zbys_SunMaoLength.setVisibility(view.VISIBLE);
                ll_zbys_SunMaoWidth.setVisibility(view.VISIBLE);
                ll_zbys_SunMaoThickness.setVisibility(view.VISIBLE);
                ll_zbys_LeftMargin.setVisibility(view.VISIBLE);
                ll_zbys_BottonMargin.setVisibility(view.VISIBLE);
                ll_zys_MaterialHeight.setVisibility(view.GONE);
                ll_zys_MaterialWidth.setVisibility(view.GONE);
                ll_zys_SunMaoLength.setVisibility(view.GONE);
                ll_zys_SunMaoD.setVisibility(view.GONE);
                ll_zys_LeftMargin.setVisibility(view.GONE);
                ll_zys_BottonMargin.setVisibility(view.GONE);
                ll_zbms_Groovewidth.setVisibility(view.GONE);
                ll_zbms_Groovedepth.setVisibility(view.GONE);
                ll_zbms_Groovelength.setVisibility(view.GONE);
                ll_zbms_MaterialHeight.setVisibility(view.GONE);
                ll_zbms_MaterialWidth.setVisibility(view.GONE);
                ll_zbms_LeftMargin.setVisibility(view.GONE);
                ll_zbms_BottonMargin.setVisibility(view.GONE);
                ll_scdl_Groovewidth.setVisibility(view.GONE);
                ll_scdl_Groovedepth.setVisibility(view.GONE);
                ll_scdl_Groovelength.setVisibility(view.GONE);
                ll_scdl_MaterialHeight.setVisibility(view.GONE);
                ll_scdl_MaterialWidth.setVisibility(view.GONE);
                ll_scdl_LeftMargin.setVisibility(view.GONE);
                ll_scdl_BottonMargin.setVisibility(view.GONE);
                ll_mys_Groovedepth.setVisibility(view.GONE);
                ll_mys_CentrePoint.setVisibility(view.GONE);
                ll_mys_MaterialHeight.setVisibility(view.GONE);
                ll_mys_MaterialWidth.setVisibility(view.GONE);
                ll_mys_Radius.setVisibility(view.GONE);
                ll_fxdl_MaterialHeight.setVisibility(view.GONE);
                ll_fxdl_MaterialWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoLength.setVisibility(view.GONE);
                ll_fxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_fxdl_LeftMargin.setVisibility(view.GONE);
                ll_fxdl_BottonMargin.setVisibility(view.GONE);
                ll_bxjg_Groovedepth.setVisibility(view.GONE);
                ll_bxjg_Length.setVisibility(view.GONE);
                ll_bxjg_Width.setVisibility(view.GONE);
                ll_bxjg_thickness.setVisibility(view.GONE);
                ll_uxdl_MaterialHeight.setVisibility(view.GONE);
                ll_uxdl_MaterialWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoLength.setVisibility(view.GONE);
                ll_uxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_uxdl_LeftMargin.setVisibility(view.GONE);
//endregion description
                if (data.equals("直扁圆榫头刀路") && zby == 1) {
                    zbys_MaterialHeight.setText(lists.toArray()[3].toString());//材料高度
                    zbys_MaterialWidth.setText(lists.toArray()[1].toString());//材料宽度
                    zbys_SunMaoLength.setText(lists.toArray()[5].toString());//榫卯长度
                    zbys_SunMaoWidth.setText(lists.toArray()[7].toString());//榫卯宽度
                    zbys_SunMaoThickness.setText(lists.toArray()[9].toString());//榫卯厚度
                    zbys_BottonMargin.setText(lists.toArray()[13].toString());//下边距
                    zbys_LeftMargin.setText(lists.toArray()[11].toString());//左边距
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("zbys_MaterialHeight", zbys_MaterialHeight.getText().toString());
                    editor.putString("zbys_MaterialWidth", zbys_MaterialWidth.getText().toString());
                    editor.putString("zbys_SunMaoLength", zbys_SunMaoLength.getText().toString());
                    editor.putString("zbys_SunMaoWidth", zbys_SunMaoWidth.getText().toString());
                    editor.putString("zbys_SunMaoThickness", zbys_SunMaoThickness.getText().toString());
                    editor.putString("zbys_BottonMargin", zbys_BottonMargin.getText().toString());
                    editor.putString("zbys_LeftMargin", zbys_LeftMargin.getText().toString());
                    editor.commit();
                    zby++;
                } else if (data.equals("直扁圆榫头刀路1") && zby1 == 1) {
                    zbys_MaterialHeight.setText(lists.toArray()[3].toString());//材料高度
                    zbys_MaterialWidth.setText(lists.toArray()[1].toString());//材料宽度
                    zbys_SunMaoLength.setText(lists.toArray()[5].toString());//榫卯长度
                    zbys_SunMaoWidth.setText(lists.toArray()[7].toString());//榫卯宽度
                    zbys_SunMaoThickness.setText(lists.toArray()[9].toString());//榫卯厚度
                    zbys_BottonMargin.setText(lists.toArray()[13].toString());//下边距
                    zbys_LeftMargin.setText(lists.toArray()[11].toString());//左边距
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("zbys_MaterialHeight", zbys_MaterialHeight.getText().toString());
                    editor.putString("zbys_MaterialWidth", zbys_MaterialWidth.getText().toString());
                    editor.putString("zbys_SunMaoLength", zbys_SunMaoLength.getText().toString());
                    editor.putString("zbys_SunMaoWidth", zbys_SunMaoWidth.getText().toString());
                    editor.putString("zbys_SunMaoThickness", zbys_SunMaoThickness.getText().toString());
                    editor.putString("zbys_BottonMargin", zbys_BottonMargin.getText().toString());
                    editor.putString("zbys_LeftMargin", zbys_LeftMargin.getText().toString());
                    editor.commit();
                    zby1++;
                } else {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    zbys_MaterialHeight.setText(pref.getString("zbys_MaterialHeight", ""));//材料高度
                    zbys_MaterialWidth.setText(pref.getString("zbys_MaterialWidth", ""));//材料宽度
                    zbys_SunMaoLength.setText(pref.getString("zbys_SunMaoLength", ""));//榫卯长度
                    zbys_BottonMargin.setText(pref.getString("zbys_BottonMargin", ""));//下边距
                    zbys_LeftMargin.setText(pref.getString("zbys_LeftMargin", ""));//左边距
                    zbys_SunMaoWidth.setText(pref.getString("zbys_SunMaoWidth", ""));//榫头半径
                    zbys_SunMaoThickness.setText(pref.getString("zbys_SunMaoThickness", ""));//榫头半径
                }
            } else if (data.contains("直扁母榫刀路")) {
                //region description
                ll_zbys_MaterialHeight.setVisibility(view.GONE);
                ll_zbys_MaterialWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoLength.setVisibility(view.GONE);
                ll_zbys_SunMaoWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoThickness.setVisibility(view.GONE);
                ll_zbys_LeftMargin.setVisibility(view.GONE);
                ll_zbys_BottonMargin.setVisibility(view.GONE);
                ll_zys_MaterialHeight.setVisibility(view.GONE);
                ll_zys_MaterialWidth.setVisibility(view.GONE);
                ll_zys_SunMaoLength.setVisibility(view.GONE);
                ll_zys_SunMaoD.setVisibility(view.GONE);
                ll_zys_LeftMargin.setVisibility(view.GONE);
                ll_zys_BottonMargin.setVisibility(view.GONE);
                ll_zbms_Groovewidth.setVisibility(view.VISIBLE);
                ll_zbms_Groovedepth.setVisibility(view.VISIBLE);
                ll_zbms_Groovelength.setVisibility(view.VISIBLE);
                ll_zbms_MaterialHeight.setVisibility(view.VISIBLE);
                ll_zbms_MaterialWidth.setVisibility(view.VISIBLE);
                ll_zbms_LeftMargin.setVisibility(view.VISIBLE);
                ll_zbms_BottonMargin.setVisibility(view.VISIBLE);
                ll_scdl_Groovewidth.setVisibility(view.GONE);
                ll_scdl_Groovedepth.setVisibility(view.GONE);
                ll_scdl_Groovelength.setVisibility(view.GONE);
                ll_scdl_MaterialHeight.setVisibility(view.GONE);
                ll_scdl_MaterialWidth.setVisibility(view.GONE);
                ll_scdl_LeftMargin.setVisibility(view.GONE);
                ll_scdl_BottonMargin.setVisibility(view.GONE);
                ll_mys_Groovedepth.setVisibility(view.GONE);
                ll_mys_MaterialHeight.setVisibility(view.GONE);
                ll_mys_MaterialWidth.setVisibility(view.GONE);
                ll_mys_CentrePoint.setVisibility(view.GONE);
                ll_mys_Radius.setVisibility(view.GONE);
                ll_fxdl_MaterialHeight.setVisibility(view.GONE);
                ll_fxdl_MaterialWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoLength.setVisibility(view.GONE);
                ll_fxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_fxdl_LeftMargin.setVisibility(view.GONE);
                ll_fxdl_BottonMargin.setVisibility(view.GONE);
                ll_bxjg_Groovedepth.setVisibility(view.GONE);
                ll_bxjg_Length.setVisibility(view.GONE);
                ll_bxjg_Width.setVisibility(view.GONE);
                ll_bxjg_thickness.setVisibility(view.GONE);
                ll_uxdl_MaterialHeight.setVisibility(view.GONE);
                ll_uxdl_MaterialWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoLength.setVisibility(view.GONE);
                ll_uxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_uxdl_LeftMargin.setVisibility(view.GONE);
                //endregion description

                if (data.equals("直扁母榫刀路") && zbm == 1) {
                    zbms_MaterialHeight.setText(lists.toArray()[3].toString());
                    zbms_MaterialWidth.setText(lists.toArray()[1].toString());
                    zbms_LeftMargin.setText(lists.toArray()[5].toString());
                    zbms_BottonMargin.setText(lists.toArray()[7].toString());
                    zbms_Groovedepth.setText(lists.toArray()[11].toString());//槽深
                    zbms_Groovelength.setText(lists.toArray()[9].toString());//槽宽
                    zbms_Groovewidth.setText(lists.toArray()[13].toString());//槽厚

                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("zbms_Groovedepth", zbms_Groovedepth.getText().toString());
                    editor.putString("zbms_Groovelength", zbms_Groovelength.getText().toString());//宽
                    editor.putString("zbms_Groovewidth", zbms_Groovewidth.getText().toString());//厚
                    editor.putString("zbms_MaterialHeight", zbms_MaterialHeight.getText().toString());
                    editor.putString("zbms_MaterialWidth", zbms_MaterialWidth.getText().toString());
                    editor.putString("zbms_LeftMargin", zbms_LeftMargin.getText().toString());
                    editor.putString("zbms_BottonMargin", zbms_BottonMargin.getText().toString());
                    editor.commit();
                    zbm++;
                }
                if (data.equals("直扁母榫刀路1") && zbm1 == 1) {
                    zbms_MaterialHeight.setText(lists.toArray()[3].toString());
                    zbms_MaterialWidth.setText(lists.toArray()[1].toString());
                    zbms_LeftMargin.setText(lists.toArray()[5].toString());
                    zbms_BottonMargin.setText(lists.toArray()[7].toString());
                    zbms_Groovedepth.setText(lists.toArray()[11].toString());//槽深
                    zbms_Groovelength.setText(lists.toArray()[9].toString());//槽宽
                    zbms_Groovewidth.setText(lists.toArray()[13].toString());//槽厚

                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("zbms_Groovedepth", zbms_Groovedepth.getText().toString());
                    editor.putString("zbms_Groovelength", zbms_Groovelength.getText().toString());//宽
                    editor.putString("zbms_Groovewidth", zbms_Groovewidth.getText().toString());//厚
                    editor.putString("zbms_MaterialHeight", zbms_MaterialHeight.getText().toString());
                    editor.putString("zbms_MaterialWidth", zbms_MaterialWidth.getText().toString());
                    editor.putString("zbms_LeftMargin", zbms_LeftMargin.getText().toString());
                    editor.putString("zbms_BottonMargin", zbms_BottonMargin.getText().toString());
                    editor.commit();
                    zbm1++;
                } else {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    zbms_Groovedepth.setText(pref.getString("zbms_Groovedepth", ""));//槽深
                    zbms_Groovelength.setText(pref.getString("zbms_Groovelength", ""));//槽长
                    zbms_Groovewidth.setText(pref.getString("zbms_Groovewidth", ""));//槽宽
                    zbms_MaterialHeight.setText(pref.getString("zbms_MaterialHeight", ""));//槽宽
                    zbms_MaterialWidth.setText(pref.getString("zbms_MaterialWidth", ""));//槽宽
                    zbms_LeftMargin.setText(pref.getString("zbms_LeftMargin", ""));//槽宽
                    zbms_BottonMargin.setText(pref.getString("zbms_BottonMargin", ""));//槽宽
                }
            } else if (data.contains("榫槽刀路")) {
                //region description
                ll_zbys_MaterialHeight.setVisibility(view.GONE);
                ll_zbys_MaterialWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoLength.setVisibility(view.GONE);
                ll_zbys_SunMaoWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoThickness.setVisibility(view.GONE);
                ll_zbys_LeftMargin.setVisibility(view.GONE);
                ll_zbys_BottonMargin.setVisibility(view.GONE);
                ll_zys_MaterialHeight.setVisibility(view.GONE);
                ll_zys_MaterialWidth.setVisibility(view.GONE);
                ll_zys_SunMaoLength.setVisibility(view.GONE);
                ll_zys_SunMaoD.setVisibility(view.GONE);
                ll_zys_LeftMargin.setVisibility(view.GONE);
                ll_zys_BottonMargin.setVisibility(view.GONE);
                ll_zbms_Groovewidth.setVisibility(view.GONE);
                ll_zbms_Groovedepth.setVisibility(view.GONE);
                ll_zbms_Groovelength.setVisibility(view.GONE);
                ll_zbms_MaterialHeight.setVisibility(view.GONE);
                ll_zbms_MaterialWidth.setVisibility(view.GONE);
                ll_zbms_LeftMargin.setVisibility(view.GONE);
                ll_zbms_BottonMargin.setVisibility(view.GONE);
                ll_scdl_Groovewidth.setVisibility(view.VISIBLE);
                ll_scdl_Groovedepth.setVisibility(view.VISIBLE);
                ll_scdl_Groovelength.setVisibility(view.VISIBLE);
                ll_scdl_MaterialHeight.setVisibility(view.VISIBLE);
                ll_scdl_MaterialWidth.setVisibility(view.VISIBLE);
                ll_scdl_LeftMargin.setVisibility(view.VISIBLE);
                ll_scdl_BottonMargin.setVisibility(view.VISIBLE);
                ll_mys_Groovedepth.setVisibility(view.GONE);
                ll_mys_CentrePoint.setVisibility(view.GONE);
                ll_mys_Radius.setVisibility(view.GONE);
                ll_mys_MaterialHeight.setVisibility(view.GONE);
                ll_mys_MaterialWidth.setVisibility(view.GONE);
                ll_fxdl_MaterialHeight.setVisibility(view.GONE);
                ll_fxdl_MaterialWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoLength.setVisibility(view.GONE);
                ll_fxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_fxdl_LeftMargin.setVisibility(view.GONE);
                ll_fxdl_BottonMargin.setVisibility(view.GONE);
                ll_bxjg_Groovedepth.setVisibility(view.GONE);
                ll_bxjg_Length.setVisibility(view.GONE);
                ll_bxjg_Width.setVisibility(view.GONE);
                ll_bxjg_thickness.setVisibility(view.GONE);
                ll_uxdl_MaterialHeight.setVisibility(view.GONE);
                ll_uxdl_MaterialWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoLength.setVisibility(view.GONE);
                ll_uxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_uxdl_LeftMargin.setVisibility(view.GONE);
                //endregion description
                if (data.equals("榫槽刀路") && scdl == 1) {
                    scdl_MaterialHeight.setText(lists.toArray()[3].toString());
                    scdl_MaterialWidth.setText(lists.toArray()[1].toString());
                    scdl_LeftMargin.setText(lists.toArray()[5].toString());
                    scdl_BottonMargin.setText(lists.toArray()[7].toString());
                    scdl_Groovewidth.setText(lists.toArray()[13].toString());
                    scdl_Groovedepth.setText(lists.toArray()[11].toString());
                    scdl_Groovelength.setText(lists.toArray()[9].toString());
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("scdl_Groovewidth", scdl_Groovewidth.getText().toString());
                    editor.putString("scdl_Groovedepth", scdl_Groovedepth.getText().toString());
                    editor.putString("scdl_Groovelength", scdl_Groovelength.getText().toString());
                    editor.putString("scdl_MaterialHeight", scdl_MaterialHeight.getText().toString());
                    editor.putString("scdl_MaterialWidth", scdl_MaterialWidth.getText().toString());
                    editor.putString("scdl_LeftMargin", scdl_LeftMargin.getText().toString());
                    editor.putString("scdl_BottonMargin", scdl_BottonMargin.getText().toString());
                    editor.commit();
                    scdl++;
                }
                if (data.equals("榫槽刀路1") && scdl1 == 1) {
                    scdl_MaterialHeight.setText(lists.toArray()[3].toString());
                    scdl_MaterialWidth.setText(lists.toArray()[1].toString());
                    scdl_LeftMargin.setText(lists.toArray()[5].toString());
                    scdl_BottonMargin.setText(lists.toArray()[7].toString());
                    scdl_Groovewidth.setText(lists.toArray()[13].toString());
                    scdl_Groovedepth.setText(lists.toArray()[11].toString());
                    scdl_Groovelength.setText(lists.toArray()[9].toString());
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("scdl_Groovewidth", scdl_Groovewidth.getText().toString());
                    editor.putString("scdl_Groovedepth", scdl_Groovedepth.getText().toString());
                    editor.putString("scdl_Groovelength", scdl_Groovelength.getText().toString());
                    editor.putString("scdl_MaterialHeight", scdl_MaterialHeight.getText().toString());
                    editor.putString("scdl_MaterialWidth", scdl_MaterialWidth.getText().toString());
                    editor.putString("scdl_LeftMargin", scdl_LeftMargin.getText().toString());
                    editor.putString("scdl_BottonMargin", scdl_BottonMargin.getText().toString());
                    editor.commit();
                    scdl1++;
                } else {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    scdl_Groovewidth.setText(pref.getString("scdl_Groovewidth", ""));//槽深
                    scdl_Groovedepth.setText(pref.getString("scdl_Groovedepth", ""));//槽长
                    scdl_Groovelength.setText(pref.getString("scdl_Groovelength", ""));//槽宽
                    scdl_MaterialHeight.setText(pref.getString("scdl_MaterialHeight", ""));//槽宽
                    scdl_MaterialWidth.setText(pref.getString("scdl_MaterialWidth", ""));//槽宽
                    scdl_LeftMargin.setText(pref.getString("scdl_LeftMargin", ""));//槽宽
                    scdl_BottonMargin.setText(pref.getString("scdl_BottonMargin", ""));//槽宽
                }
            } else if (data.equals("母圆榫刀路")) {
                //region description
                ll_zbys_MaterialHeight.setVisibility(view.GONE);
                ll_zbys_MaterialWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoLength.setVisibility(view.GONE);
                ll_zbys_SunMaoWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoThickness.setVisibility(view.GONE);
                ll_zbys_LeftMargin.setVisibility(view.GONE);
                ll_zbys_BottonMargin.setVisibility(view.GONE);
                ll_zys_MaterialHeight.setVisibility(view.GONE);
                ll_zys_MaterialWidth.setVisibility(view.GONE);
                ll_zys_SunMaoLength.setVisibility(view.GONE);
                ll_zys_SunMaoD.setVisibility(view.GONE);
                ll_zys_LeftMargin.setVisibility(view.GONE);
                ll_zys_BottonMargin.setVisibility(view.GONE);
                ll_zbms_Groovewidth.setVisibility(view.GONE);
                ll_zbms_Groovedepth.setVisibility(view.GONE);
                ll_zbms_Groovelength.setVisibility(view.GONE);
                ll_zbms_MaterialHeight.setVisibility(view.GONE);
                ll_zbms_MaterialWidth.setVisibility(view.GONE);
                ll_zbms_LeftMargin.setVisibility(view.GONE);
                ll_zbms_BottonMargin.setVisibility(view.GONE);
                ll_scdl_Groovewidth.setVisibility(view.GONE);
                ll_scdl_Groovedepth.setVisibility(view.GONE);
                ll_scdl_Groovelength.setVisibility(view.GONE);
                ll_scdl_MaterialHeight.setVisibility(view.GONE);
                ll_scdl_MaterialWidth.setVisibility(view.GONE);
                ll_scdl_LeftMargin.setVisibility(view.GONE);
                ll_scdl_BottonMargin.setVisibility(view.GONE);
                ll_mys_Groovedepth.setVisibility(view.VISIBLE);
                ll_mys_CentrePoint.setVisibility(view.VISIBLE);
                ll_mys_Radius.setVisibility(view.VISIBLE);
                ll_mys_MaterialHeight.setVisibility(view.VISIBLE);
                ll_mys_MaterialWidth.setVisibility(view.VISIBLE);
                ll_fxdl_MaterialHeight.setVisibility(view.GONE);
                ll_fxdl_MaterialWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoLength.setVisibility(view.GONE);
                ll_fxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_fxdl_LeftMargin.setVisibility(view.GONE);
                ll_fxdl_BottonMargin.setVisibility(view.GONE);
                ll_bxjg_Groovedepth.setVisibility(view.GONE);
                ll_bxjg_Length.setVisibility(view.GONE);
                ll_bxjg_Width.setVisibility(view.GONE);
                ll_bxjg_thickness.setVisibility(view.GONE);
                ll_uxdl_MaterialHeight.setVisibility(view.GONE);
                ll_uxdl_MaterialWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoLength.setVisibility(view.GONE);
                ll_uxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_uxdl_LeftMargin.setVisibility(view.GONE);
                //endregion description
                if (data.equals("母圆榫刀路") && mysdl == 1) {
                    mys_Groovedepth.setText(lists.toArray()[5].toString());//槽深
                    mys_CentrePoint.setText(lists.toArray()[7].toString());//圆心点
                    mys_Radius.setText(lists.toArray()[9].toString());//半径
                    mys_MaterialHeight.setText(lists.toArray()[3].toString());
                    mys_MaterialWidth.setText(lists.toArray()[1].toString());
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("mys_Groovedepth", mys_Groovedepth.getText().toString());
                    editor.putString("mys_CentrePoint", mys_CentrePoint.getText().toString());
                    editor.putString("mys_Radius", mys_Radius.getText().toString());
                    editor.putString("mys_MaterialHeight", mys_MaterialHeight.getText().toString());
                    editor.putString("mys_MaterialWidth", mys_MaterialWidth.getText().toString());
                    editor.commit();
                    mysdl++;
                } else {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    mys_Groovedepth.setText(pref.getString("mys_Groovedepth", ""));//槽深
                    mys_CentrePoint.setText(pref.getString("mys_CentrePoint", ""));//槽长
                    mys_Radius.setText(pref.getString("mys_Radius", ""));//槽宽
                    mys_MaterialHeight.setText(pref.getString("mys_MaterialHeight", ""));//槽宽
                    mys_MaterialWidth.setText(pref.getString("mys_MaterialWidth", ""));//槽宽
                }

            } else if (data.equals("方型刀路")) {
                //region description
                ll_zbys_MaterialHeight.setVisibility(view.GONE);
                ll_zbys_MaterialWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoLength.setVisibility(view.GONE);
                ll_zbys_SunMaoWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoThickness.setVisibility(view.GONE);
                ll_zbys_LeftMargin.setVisibility(view.GONE);
                ll_zbys_BottonMargin.setVisibility(view.GONE);
                ll_zys_MaterialHeight.setVisibility(view.GONE);
                ll_zys_MaterialWidth.setVisibility(view.GONE);
                ll_zys_SunMaoLength.setVisibility(view.GONE);
                ll_zys_SunMaoD.setVisibility(view.GONE);
                ll_zys_LeftMargin.setVisibility(view.GONE);
                ll_zys_BottonMargin.setVisibility(view.GONE);
                ll_zbms_Groovewidth.setVisibility(view.GONE);
                ll_zbms_Groovedepth.setVisibility(view.GONE);
                ll_zbms_MaterialHeight.setVisibility(view.GONE);
                ll_zbms_MaterialWidth.setVisibility(view.GONE);
                ll_zbms_LeftMargin.setVisibility(view.GONE);
                ll_zbms_BottonMargin.setVisibility(view.GONE);
                ll_zbms_Groovelength.setVisibility(view.GONE);
                ll_scdl_Groovewidth.setVisibility(view.GONE);
                ll_scdl_Groovedepth.setVisibility(view.GONE);
                ll_scdl_Groovelength.setVisibility(view.GONE);
                ll_scdl_MaterialHeight.setVisibility(view.GONE);
                ll_scdl_MaterialWidth.setVisibility(view.GONE);
                ll_scdl_LeftMargin.setVisibility(view.GONE);
                ll_scdl_BottonMargin.setVisibility(view.GONE);
                ll_mys_Groovedepth.setVisibility(view.GONE);
                ll_mys_CentrePoint.setVisibility(view.GONE);
                ll_mys_Radius.setVisibility(view.GONE);
                ll_mys_MaterialHeight.setVisibility(view.GONE);
                ll_mys_MaterialWidth.setVisibility(view.GONE);
                ll_fxdl_MaterialHeight.setVisibility(view.VISIBLE);
                ll_fxdl_MaterialWidth.setVisibility(view.VISIBLE);
                ll_fxdl_SunMaoLength.setVisibility(view.VISIBLE);
                ll_fxdl_SunMaoWidth.setVisibility(view.VISIBLE);
                ll_fxdl_SunMaoThickness.setVisibility(view.VISIBLE);
                ll_fxdl_LeftMargin.setVisibility(view.VISIBLE);
                ll_fxdl_BottonMargin.setVisibility(view.VISIBLE);
                ll_bxjg_Groovedepth.setVisibility(view.GONE);
                ll_bxjg_Length.setVisibility(view.GONE);
                ll_bxjg_Width.setVisibility(view.GONE);
                ll_bxjg_thickness.setVisibility(view.GONE);
                ll_uxdl_MaterialHeight.setVisibility(view.GONE);
                ll_uxdl_MaterialWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoLength.setVisibility(view.GONE);
                ll_uxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_uxdl_LeftMargin.setVisibility(view.GONE);
                //endregion description
                if (data.equals("方型刀路") && fxdl == 1) {
                    fxdl_MaterialHeight.setText(lists.toArray()[3].toString());//材料高度
                    fxdl_MaterialWidth.setText(lists.toArray()[1].toString());//材料厚度
                    fxdl_SunMaoLength.setText(lists.toArray()[5].toString());//榫卯长度
                    fxdl_SunMaoWidth.setText(lists.toArray()[7].toString());//榫卯宽度
                    fxdl_SunMaoThickness.setText(lists.toArray()[9].toString());//榫卯厚度
                    fxdl_BottonMargin.setText(lists.toArray()[13].toString());//下边距
                    fxdl_LeftMargin.setText(lists.toArray()[11].toString());//左边距
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("fxdl_MaterialHeight", fxdl_MaterialHeight.getText().toString());
                    editor.putString("fxdl_MaterialWidth", fxdl_MaterialWidth.getText().toString());
                    editor.putString("fxdl_SunMaoLength", fxdl_SunMaoLength.getText().toString());
                    editor.putString("fxdl_SunMaoWidth", fxdl_SunMaoWidth.getText().toString());
                    editor.putString("fxdl_SunMaoThickness", fxdl_SunMaoThickness.getText().toString());
                    editor.putString("fxdl_BottonMargin", fxdl_BottonMargin.getText().toString());
                    editor.putString("fxdl_LeftMargin", fxdl_LeftMargin.getText().toString());
                    editor.commit();
                    fxdl++;
                }
                if (data.equals("方型刀路1") && fxdl1 == 1) {
                    fxdl_MaterialHeight.setText(lists.toArray()[3].toString());//材料高度
                    fxdl_MaterialWidth.setText(lists.toArray()[1].toString());//材料厚度
                    fxdl_SunMaoLength.setText(lists.toArray()[5].toString());//榫卯长度
                    fxdl_SunMaoWidth.setText(lists.toArray()[7].toString());//榫卯宽度
                    fxdl_SunMaoThickness.setText(lists.toArray()[9].toString());//榫卯厚度
                    fxdl_BottonMargin.setText(lists.toArray()[13].toString());//下边距
                    fxdl_LeftMargin.setText(lists.toArray()[11].toString());//左边距
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("fxdl_MaterialHeight", fxdl_MaterialHeight.getText().toString());
                    editor.putString("fxdl_MaterialWidth", fxdl_MaterialWidth.getText().toString());
                    editor.putString("fxdl_SunMaoLength", fxdl_SunMaoLength.getText().toString());
                    editor.putString("fxdl_SunMaoWidth", fxdl_SunMaoWidth.getText().toString());
                    editor.putString("fxdl_SunMaoThickness", fxdl_SunMaoThickness.getText().toString());
                    editor.putString("fxdl_BottonMargin", fxdl_BottonMargin.getText().toString());
                    editor.putString("fxdl_LeftMargin", fxdl_LeftMargin.getText().toString());
                    editor.commit();
                    fxdl1++;
                } else {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    fxdl_MaterialHeight.setText(pref.getString("fxdl_MaterialHeight", ""));//槽深
                    fxdl_MaterialWidth.setText(pref.getString("fxdl_MaterialWidth", ""));//槽长
                    fxdl_SunMaoLength.setText(pref.getString("fxdl_SunMaoLength", ""));//槽宽
                    fxdl_SunMaoWidth.setText(pref.getString("fxdl_SunMaoWidth", ""));//槽宽
                    fxdl_SunMaoThickness.setText(pref.getString("fxdl_SunMaoThickness", ""));//槽宽
                    fxdl_BottonMargin.setText(pref.getString("fxdl_BottonMargin", ""));//槽宽
                    fxdl_LeftMargin.setText(pref.getString("fxdl_LeftMargin", ""));//槽宽
                }

            } else if (data.contains("边线加工刀路")) {
                //region description
                ll_zbys_MaterialHeight.setVisibility(view.GONE);
                ll_zbys_MaterialWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoLength.setVisibility(view.GONE);
                ll_zbys_SunMaoWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoThickness.setVisibility(view.GONE);
                ll_zbys_LeftMargin.setVisibility(view.GONE);
                ll_zbys_BottonMargin.setVisibility(view.GONE);
                ll_zys_MaterialHeight.setVisibility(view.GONE);
                ll_zys_MaterialWidth.setVisibility(view.GONE);
                ll_zys_SunMaoLength.setVisibility(view.GONE);
                ll_zys_SunMaoD.setVisibility(view.GONE);
                ll_zys_LeftMargin.setVisibility(view.GONE);
                ll_zys_BottonMargin.setVisibility(view.GONE);
                ll_zbms_MaterialHeight.setVisibility(view.GONE);
                ll_zbms_MaterialWidth.setVisibility(view.GONE);
                ll_zbms_LeftMargin.setVisibility(view.GONE);
                ll_zbms_BottonMargin.setVisibility(view.GONE);
                ll_zbms_Groovewidth.setVisibility(view.GONE);
                ll_zbms_Groovedepth.setVisibility(view.GONE);
                ll_zbms_Groovelength.setVisibility(view.GONE);
                ll_scdl_Groovewidth.setVisibility(view.GONE);
                ll_scdl_Groovedepth.setVisibility(view.GONE);
                ll_scdl_Groovelength.setVisibility(view.GONE);
                ll_scdl_MaterialHeight.setVisibility(view.GONE);
                ll_scdl_MaterialWidth.setVisibility(view.GONE);
                ll_scdl_LeftMargin.setVisibility(view.GONE);
                ll_scdl_BottonMargin.setVisibility(view.GONE);
                ll_mys_Groovedepth.setVisibility(view.GONE);
                ll_mys_CentrePoint.setVisibility(view.GONE);
                ll_mys_Radius.setVisibility(view.GONE);
                ll_mys_MaterialHeight.setVisibility(view.GONE);
                ll_mys_MaterialWidth.setVisibility(view.GONE);
                ll_fxdl_MaterialHeight.setVisibility(view.GONE);
                ll_fxdl_MaterialWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoLength.setVisibility(view.GONE);
                ll_fxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_fxdl_LeftMargin.setVisibility(view.GONE);
                ll_fxdl_BottonMargin.setVisibility(view.GONE);
                ll_bxjg_Groovedepth.setVisibility(view.GONE);
                ll_bxjg_Length.setVisibility(view.VISIBLE);
                ll_bxjg_Width.setVisibility(view.VISIBLE);
                ll_bxjg_thickness.setVisibility(view.VISIBLE);
                ll_uxdl_MaterialHeight.setVisibility(view.GONE);
                ll_uxdl_MaterialWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoLength.setVisibility(view.GONE);
                ll_uxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_uxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_uxdl_LeftMargin.setVisibility(view.GONE);
                //endregion description
                if (data.equals("边线加工刀路") && bxjg == 1) {
                    bxjg_thickness.setText(lists.toArray()[5].toString());//厚度
                    bxjg_Width.setText(lists.toArray()[3].toString());//宽度
                    bxjg_Length.setText(lists.toArray()[1].toString());//长度
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("bxjg_thickness", bxjg_thickness.getText().toString());
                    editor.putString("bxjg_Width", bxjg_Width.getText().toString());
                    editor.putString("bxjg_Length", bxjg_Length.getText().toString());
                    editor.commit();
                    bxjg++;
                } else if (data.equals("边线加工刀路1") && bxjg1 == 1) {
                    bxjg_thickness.setText(lists.toArray()[5].toString());//厚度
                    bxjg_Width.setText(lists.toArray()[3].toString());//宽度
                    bxjg_Length.setText(lists.toArray()[1].toString());//长度
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    ;
                    editor.putString("bxjg_thickness", bxjg_thickness.getText().toString());
                    editor.putString("bxjg_Width", bxjg_Width.getText().toString());
                    editor.putString("bxjg_Length", bxjg_Length.getText().toString());
                    editor.commit();
                    bxjg1++;
                } else if (data.equals("边线加工刀路2") && bxjg2 == 1) {
                    bxjg_thickness.setText(lists.toArray()[5].toString());//厚度
                    bxjg_Width.setText(lists.toArray()[3].toString());//宽度
                    bxjg_Length.setText(lists.toArray()[1].toString());//长度
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    ;
                    editor.putString("bxjg_thickness", bxjg_thickness.getText().toString());
                    editor.putString("bxjg_Width", bxjg_Width.getText().toString());
                    editor.putString("bxjg_Length", bxjg_Length.getText().toString());
                    editor.commit();
                    bxjg2++;
                } else if (data.equals("边线加工刀路3") && bxjg3 == 1) {
                    bxjg_thickness.setText(lists.toArray()[5].toString());//厚度
                    bxjg_Width.setText(lists.toArray()[3].toString());//宽度
                    bxjg_Length.setText(lists.toArray()[1].toString());//长度
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    ;
                    editor.putString("bxjg_thickness", bxjg_thickness.getText().toString());
                    editor.putString("bxjg_Width", bxjg_Width.getText().toString());
                    editor.putString("bxjg_Length", bxjg_Length.getText().toString());
                    editor.commit();
                    bxjg3++;
                } else {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    bxjg_thickness.setText(pref.getString("bxjg_thickness", ""));//槽长
                    bxjg_Width.setText(pref.getString("bxjg_Width", ""));//槽宽
                    bxjg_Length.setText(pref.getString("bxjg_Length", ""));//槽宽
                }
            } else if (data.equals("U型刀路")) {
                //region description
                ll_zbys_MaterialHeight.setVisibility(view.GONE);
                ll_zbys_MaterialWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoLength.setVisibility(view.GONE);
                ll_zbys_SunMaoWidth.setVisibility(view.GONE);
                ll_zbys_SunMaoThickness.setVisibility(view.GONE);
                ll_zbys_LeftMargin.setVisibility(view.GONE);
                ll_zbys_BottonMargin.setVisibility(view.GONE);
                ll_zys_MaterialHeight.setVisibility(view.GONE);
                ll_zys_MaterialWidth.setVisibility(view.GONE);
                ll_zys_SunMaoLength.setVisibility(view.GONE);
                ll_zys_SunMaoD.setVisibility(view.GONE);
                ll_zys_LeftMargin.setVisibility(view.GONE);
                ll_zys_BottonMargin.setVisibility(view.GONE);
                ll_zbms_Groovewidth.setVisibility(view.GONE);
                ll_zbms_Groovedepth.setVisibility(view.GONE);
                ll_zbms_Groovelength.setVisibility(view.GONE);
                ll_zbms_MaterialHeight.setVisibility(view.GONE);
                ll_zbms_MaterialWidth.setVisibility(view.GONE);
                ll_zbms_LeftMargin.setVisibility(view.GONE);
                ll_zbms_BottonMargin.setVisibility(view.GONE);
                ll_scdl_Groovewidth.setVisibility(view.GONE);
                ll_scdl_Groovedepth.setVisibility(view.GONE);
                ll_scdl_Groovelength.setVisibility(view.GONE);
                ll_scdl_MaterialHeight.setVisibility(view.GONE);
                ll_scdl_MaterialWidth.setVisibility(view.GONE);
                ll_scdl_LeftMargin.setVisibility(view.GONE);
                ll_scdl_BottonMargin.setVisibility(view.GONE);
                ll_mys_Groovedepth.setVisibility(view.GONE);
                ll_mys_CentrePoint.setVisibility(view.GONE);
                ll_mys_Radius.setVisibility(view.GONE);
                ll_mys_MaterialHeight.setVisibility(view.GONE);
                ll_mys_MaterialWidth.setVisibility(view.GONE);
                ll_fxdl_MaterialHeight.setVisibility(view.GONE);
                ll_fxdl_MaterialWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoLength.setVisibility(view.GONE);
                ll_fxdl_SunMaoWidth.setVisibility(view.GONE);
                ll_fxdl_SunMaoThickness.setVisibility(view.GONE);
                ll_fxdl_LeftMargin.setVisibility(view.GONE);
                ll_fxdl_BottonMargin.setVisibility(view.GONE);
                ll_bxjg_Groovedepth.setVisibility(view.GONE);
                ll_bxjg_Length.setVisibility(view.GONE);
                ll_bxjg_Width.setVisibility(view.GONE);
                ll_bxjg_thickness.setVisibility(view.GONE);
                ll_uxdl_MaterialHeight.setVisibility(view.VISIBLE);
                ll_uxdl_MaterialWidth.setVisibility(view.VISIBLE);
                ll_uxdl_SunMaoLength.setVisibility(view.VISIBLE);
                ll_uxdl_SunMaoWidth.setVisibility(view.VISIBLE);
                ll_uxdl_SunMaoThickness.setVisibility(view.VISIBLE);
                ll_uxdl_LeftMargin.setVisibility(view.VISIBLE);
                //endregion description
                if (data.equals("U型刀路") && uxdl == 1) {
                    uxdl_MaterialHeight.setText(lists.toArray()[3].toString());//材料高度
                    uxdl_MaterialWidth.setText(lists.toArray()[1].toString());//材料厚度
                    uxdl_SunMaoLength.setText(lists.toArray()[5].toString());//榫卯长度
                    uxdl_SunMaoWidth.setText(lists.toArray()[7].toString());//榫卯宽度
                    uxdl_SunMaoThickness.setText(lists.toArray()[9].toString());//榫卯厚度
                    uxdl_LeftMargin.setText(lists.toArray()[11].toString());//左边距
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("uxdl_MaterialHeight", uxdl_MaterialHeight.getText().toString());
                    editor.putString("uxdl_MaterialWidth", uxdl_MaterialWidth.getText().toString());
                    editor.putString("uxdl_SunMaoLength", uxdl_SunMaoLength.getText().toString());
                    editor.putString("uxdl_SunMaoWidth", uxdl_SunMaoWidth.getText().toString());
                    editor.putString("uxdl_SunMaoThickness", uxdl_SunMaoThickness.getText().toString());
                    editor.putString("uxdl_LeftMargin", uxdl_LeftMargin.getText().toString());
                    editor.commit();
                    uxdl++;
                } else {
                    pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
                    uxdl_MaterialHeight.setText(pref.getString("uxdl_MaterialHeight", ""));//槽深
                    uxdl_MaterialWidth.setText(pref.getString("uxdl_MaterialWidth", ""));//槽长
                    uxdl_SunMaoLength.setText(pref.getString("uxdl_SunMaoLength", ""));//槽宽
                    uxdl_SunMaoWidth.setText(pref.getString("uxdl_SunMaoWidth", ""));//槽宽
                    uxdl_SunMaoThickness.setText(pref.getString("uxdl_SunMaoThickness", ""));//槽宽
                    uxdl_LeftMargin.setText(pref.getString("uxdl_LeftMargin", ""));//槽宽
                }

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
*/
    private void initView() {
        //一系列fandview，默认全部显示visibility="visible"invisible不显示但是占用空间 gone不显示不占用空间
        zys_MaterialHeight = (EditText) rootView.findViewById(R.id.et_zys_MaterialHeight);
        zys_MaterialWidth = (EditText) rootView.findViewById(R.id.et_zys_MaterialWidth);
        zys_SunMaoLength = (EditText) rootView.findViewById(R.id.et_zys_SunMaoLength);
        zys_SunMaoD = (EditText) rootView.findViewById(R.id.et_zys_SunMaoD);
        zys_LeftMargin = (EditText) rootView.findViewById(R.id.et_zys_LeftMargin);
        zys_BottonMargin = (EditText) rootView.findViewById(R.id.et_zys_BottonMargin);
        zbys_MaterialHeight = (EditText) rootView.findViewById(R.id.et_zbys_MaterialHeight);
        zbys_MaterialWidth = (EditText) rootView.findViewById(R.id.et_zbys_MaterialWidth);
        zbys_SunMaoLength = (EditText) rootView.findViewById(R.id.et_zbys_SunMaoLength);
        zbys_SunMaoWidth = (EditText) rootView.findViewById(R.id.et_zbys_SunMaoWidth);
        zbys_SunMaoThickness = (EditText) rootView.findViewById(R.id.et_zbys_SunMaoThickness);
        zbys_LeftMargin = (EditText) rootView.findViewById(R.id.et_zbys_LeftMargin);
        zbys_BottonMargin = (EditText) rootView.findViewById(R.id.et_zbys_BottonMargin);
        zbms_Groovewidth = (EditText) rootView.findViewById(R.id.et_zbms_Groovewidth);
        zbms_Groovedepth = (EditText) rootView.findViewById(R.id.et_zbms_Groovedepth);
        zbms_Groovelength = (EditText) rootView.findViewById(R.id.et_zbms_Groovelength);
        zbms_MaterialHeight = (EditText) rootView.findViewById(R.id.et_zbmss_MaterialHeight);
        zbms_MaterialWidth = (EditText) rootView.findViewById(R.id.et_zbms_MaterialWidth);
        zbms_LeftMargin = (EditText) rootView.findViewById(R.id.et_zbms_LeftMargin);
        zbms_BottonMargin = (EditText) rootView.findViewById(R.id.et_zbmss_BottonMargin);
        scdl_Groovewidth = (EditText) rootView.findViewById(R.id.et_scdl_Groovewidth);
        scdl_Groovedepth = (EditText) rootView.findViewById(R.id.et_scdl_Groovedepth);
        scdl_Groovelength = (EditText) rootView.findViewById(R.id.et_scdl_Groovelength);
        scdl_MaterialHeight = (EditText) rootView.findViewById(R.id.et_scdl_MaterialHeight);
        scdl_MaterialWidth = (EditText) rootView.findViewById(R.id.et_scdl_MaterialWidth);
        scdl_LeftMargin = (EditText) rootView.findViewById(R.id.et_scdl_LeftMargin);
        scdl_BottonMargin = (EditText) rootView.findViewById(R.id.et_scdl_BottonMargin);
        mys_Groovedepth = (EditText) rootView.findViewById(R.id.et_mys_Groovedepth);
        mys_CentrePoint = (EditText) rootView.findViewById(R.id.et_mys_CentrePoint);
        mys_Radius = (EditText) rootView.findViewById(R.id.et_mys_Radius);
        mys_MaterialHeight = (EditText) rootView.findViewById(R.id.et_mys_MaterialHeight);
        mys_MaterialWidth = (EditText) rootView.findViewById(R.id.et_mys_MaterialWidth);
        fxdl_MaterialHeight = (EditText) rootView.findViewById(R.id.et_fxdl_MaterialHeight);
        fxdl_MaterialWidth = (EditText) rootView.findViewById(R.id.et_fxdl_MaterialWidth);
        fxdl_SunMaoLength = (EditText) rootView.findViewById(R.id.et_fxdl_SunMaoLength);
        fxdl_SunMaoWidth = (EditText) rootView.findViewById(R.id.et_fxdl_SunMaoWidth);
        fxdl_SunMaoThickness = (EditText) rootView.findViewById(R.id.et_fxdl_SunMaoThickness);
        fxdl_LeftMargin = (EditText) rootView.findViewById(R.id.et_fxdl_LeftMargin);
        fxdl_BottonMargin = (EditText) rootView.findViewById(R.id.et_fxdl_BottonMargin);
        bxjg_Groovedepth = (EditText) rootView.findViewById(R.id.et_bxjg_Groovedepth);
        bxjg_Length = (EditText) rootView.findViewById(R.id.et_bxjg_Length);
        bxjg_Width = (EditText) rootView.findViewById(R.id.et_bxjg_Width);
        bxjg_thickness = (EditText) rootView.findViewById(R.id.et_bxjg_Thickness);
        uxdl_MaterialHeight = (EditText) rootView.findViewById(R.id.et_uxdl_MaterialHeight);
        uxdl_MaterialWidth = (EditText) rootView.findViewById(R.id.et_uxdl_MaterialWidth);
        uxdl_SunMaoLength = (EditText) rootView.findViewById(R.id.et_uxdl_SunMaoLength);
        uxdl_SunMaoWidth = (EditText) rootView.findViewById(R.id.et_uxdl_SunMaoWidth);
        uxdl_SunMaoThickness = (EditText) rootView.findViewById(R.id.et_uxdl_SunMaoThickness);
        uxdl_LeftMargin = (EditText) rootView.findViewById(R.id.et_uxdl_LeftMargin);
        save = (Button) rootView.findViewById(R.id.data_save);
        id_save=(Button)rootView.findViewById(R.id.id_save);

        /*******************************************/
        ll_zys_MaterialHeight = (LinearLayout) rootView.findViewById(R.id.lin_zys_MaterialThickness);
        ll_zys_MaterialWidth = (LinearLayout) rootView.findViewById(R.id.lin_zys_MaterialWidth);
        ll_zys_SunMaoLength = (LinearLayout) rootView.findViewById(R.id.lin_zys_SunMaoLength);
        ll_zys_SunMaoD = (LinearLayout) rootView.findViewById(R.id.lin_zys_SunMaoD);
        ll_zys_LeftMargin = (LinearLayout) rootView.findViewById(R.id.lin_zys_LeftMargin);
        ll_zys_BottonMargin = (LinearLayout) rootView.findViewById(R.id.lin_zys_BottonMargin);
        ll_zbys_MaterialHeight = (LinearLayout) rootView.findViewById(R.id.lin_zbys_MaterialThickness);
        ll_zbys_MaterialWidth = (LinearLayout) rootView.findViewById(R.id.lin_zbys_MaterialWidth);
        ll_zbys_SunMaoLength = (LinearLayout) rootView.findViewById(R.id.lin_zbys_SunMaoLength);
        ll_zbys_SunMaoWidth = (LinearLayout) rootView.findViewById(R.id.lin_zbys_SunMaoWidth);
        ll_zbys_SunMaoThickness = (LinearLayout) rootView.findViewById(R.id.lin_zbys_SunMaoThickness);
        ll_zbys_LeftMargin = (LinearLayout) rootView.findViewById(R.id.lin_zbys_LeftMargin);
        ll_zbys_BottonMargin = (LinearLayout) rootView.findViewById(R.id.lin_zbys_BottonMargin);
        ll_zbms_Groovewidth = (LinearLayout) rootView.findViewById(R.id.lin_zbms_Groovewidth);
        ll_zbms_Groovedepth = (LinearLayout) rootView.findViewById(R.id.lin_zbms_Groovedepth);
        ll_zbms_Groovelength = (LinearLayout) rootView.findViewById(R.id.lin_zbms_Groovelength);
        ll_zbms_MaterialHeight = (LinearLayout) rootView.findViewById(R.id.lin_zbms_MaterialThickness);
        ll_zbms_MaterialWidth = (LinearLayout) rootView.findViewById(R.id.lin_zbms_MaterialWidth);
        ll_zbms_LeftMargin = (LinearLayout) rootView.findViewById(R.id.lin_zbms_LeftMargin);
        ll_zbms_BottonMargin = (LinearLayout) rootView.findViewById(R.id.lin_zbmss_BottonMargin);
        ll_scdl_Groovewidth = (LinearLayout) rootView.findViewById(R.id.lin_scdl_Groovewidth);
        ll_scdl_Groovedepth = (LinearLayout) rootView.findViewById(R.id.lin_scdl_Groovedepth);
        ll_scdl_Groovelength = (LinearLayout) rootView.findViewById(R.id.lin_scdl_Groovelength);
        ll_scdl_MaterialHeight = (LinearLayout) rootView.findViewById(R.id.lin_scdl_MaterialThickness);
        ll_scdl_MaterialWidth = (LinearLayout) rootView.findViewById(R.id.lin_scdl_MaterialWidth);
        ll_scdl_LeftMargin = (LinearLayout) rootView.findViewById(R.id.lin_scdl_LeftMargin);
        ll_scdl_BottonMargin = (LinearLayout) rootView.findViewById(R.id.lin_scdl_BottonMargin);
        ll_mys_Groovedepth = (LinearLayout) rootView.findViewById(R.id.lin_mys_Groovedepth);
        ll_mys_CentrePoint = (LinearLayout) rootView.findViewById(R.id.lin_mys_CentrePoint);
        ll_mys_Radius = (LinearLayout) rootView.findViewById(R.id.lin_mys_Radius);
        ll_mys_MaterialHeight = (LinearLayout) rootView.findViewById(R.id.lin_mys_MaterialThickness);
        ll_mys_MaterialWidth = (LinearLayout) rootView.findViewById(R.id.lin_mys_MaterialWidth);
        ll_fxdl_MaterialHeight = (LinearLayout) rootView.findViewById(R.id.lin_fxdl_MaterialThickness);
        ll_fxdl_MaterialWidth = (LinearLayout) rootView.findViewById(R.id.lin_fxdl_MaterialWidth);
        ll_fxdl_SunMaoLength = (LinearLayout) rootView.findViewById(R.id.lin_fxdl_SunMaoLength);
        ll_fxdl_SunMaoWidth = (LinearLayout) rootView.findViewById(R.id.lin_fxdl_SunMaoWidth);
        ll_fxdl_SunMaoThickness = (LinearLayout) rootView.findViewById(R.id.lin_fxdl_SunMaoThickness);
        ll_fxdl_LeftMargin = (LinearLayout) rootView.findViewById(R.id.lin_fxdl_LeftMargin);
        ll_fxdl_BottonMargin = (LinearLayout) rootView.findViewById(R.id.lin_fxdl_BottonMargin);
        ll_bxjg_thickness = (LinearLayout) rootView.findViewById(R.id.lin_bxjg_Thickness);
        ll_bxjg_Groovedepth = (LinearLayout) rootView.findViewById(R.id.lin_bxjg_Groovedepth);
        ll_bxjg_Length = (LinearLayout) rootView.findViewById(R.id.lin_bxjg_Length);
        ll_bxjg_Width = (LinearLayout) rootView.findViewById(R.id.lin_bxjg_Width);

        ll_uxdl_MaterialHeight = (LinearLayout) rootView.findViewById(R.id.lin_uxdl_MaterialThickness);
        ll_uxdl_MaterialWidth = (LinearLayout) rootView.findViewById(R.id.lin_uxdl_MaterialWidth);
        ll_uxdl_SunMaoLength = (LinearLayout) rootView.findViewById(R.id.lin_uxdl_SunMaoLength);
        ll_uxdl_SunMaoWidth = (LinearLayout) rootView.findViewById(R.id.lin_uxdl_SunMaoWidth);
        ll_uxdl_SunMaoThickness = (LinearLayout) rootView.findViewById(R.id.lin_uxdl_SunMaoThickness);
        ll_uxdl_LeftMargin = (LinearLayout) rootView.findViewById(R.id.lin_uxdl_LeftMargin);
        //  Toast.makeText(getActivity(),MaterialHeight.getText().toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        pathname = ((DataActivity) activity).getPath();
       /* if(activity instanceof FragmentInteraction) {
            listterner = (FragmentInteraction)activity; // 2.2 获取到宿主activity并赋值
        } else{
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }*/
    }

   /* @Override
    public void onStart() {
        super.onStart();
        List list3 = sqlHandle.select_pathtype(pathname);
        if (list3.contains("直圆榫头刀路")) {
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("直圆榫头刀路", pathname));
            pref = getActivity().getSharedPreferences("直圆榫头刀路", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("zys_MaterialHeight", lists.toArray()[3].toString());
            editor.putString("zys_MaterialWidth", lists.toArray()[1].toString());
            editor.putString("zys_SunMaoLength", lists.toArray()[5].toString());
            editor.putString("zys_BottonMargin", lists.toArray()[11].toString());
            editor.putString("zys_LeftMargin", lists.toArray()[9].toString());
            editor.putString("zys_SunMaoD", lists.toArray()[7].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("直圆榫头刀路", pathname)).toString());
            editor.commit();
        }
        if (list3.contains("直圆榫头刀路1")) {
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("直圆榫头刀路1", pathname));
            pref = getActivity().getSharedPreferences("直圆榫头刀路1", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("zys_MaterialHeight", lists.toArray()[3].toString());
            editor.putString("zys_MaterialWidth", lists.toArray()[1].toString());
            editor.putString("zys_SunMaoLength", lists.toArray()[5].toString());
            editor.putString("zys_BottonMargin", lists.toArray()[11].toString());
            editor.putString("zys_LeftMargin", lists.toArray()[9].toString());
            editor.putString("zys_SunMaoD", lists.toArray()[7].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("直圆榫头刀路1", pathname)).toString());
            editor.commit();
        }
        if (list3.contains("直扁圆榫头刀路")) {
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("直扁圆榫头刀路", pathname));
            pref = getActivity().getSharedPreferences("直扁圆榫头刀路", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("zbys_MaterialHeight", lists.toArray()[3].toString());
            editor.putString("zbys_MaterialWidth", lists.toArray()[1].toString());
            editor.putString("zbys_SunMaoLength", lists.toArray()[5].toString());
            editor.putString("zbys_SunMaoWidth", lists.toArray()[7].toString());
            editor.putString("zbys_SunMaoThickness", lists.toArray()[9].toString());
            editor.putString("zbys_BottonMargin", lists.toArray()[13].toString());
            editor.putString("zbys_LeftMargin", lists.toArray()[11].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("直扁圆榫头刀路", pathname)).toString());
//                    editor.putString("LeftMargin",lists.toArray()[13].toString());
            editor.commit();
        }
        if (list3.contains("方型刀路")) {
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("方型刀路", pathname));
            pref = getActivity().getSharedPreferences("方型刀路", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("fxdl_MaterialHeight", lists.toArray()[3].toString());
            editor.putString("fxdl_MaterialWidth", lists.toArray()[1].toString());
            editor.putString("fxdl_SunMaoLength", lists.toArray()[5].toString());
            editor.putString("fxdl_SunMaoWidth", lists.toArray()[7].toString());
            editor.putString("fxdl_SunMaoThickness", lists.toArray()[9].toString());
            editor.putString("fxdl_BottonMargin", lists.toArray()[13].toString());
            editor.putString("fxdl_LeftMargin", lists.toArray()[11].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("方型刀路", pathname)).toString());
            editor.commit();
        }
        if (list3.contains("U型刀路")) {
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("U型刀路", pathname));
            pref = getActivity().getSharedPreferences("U型刀路", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("uxdl_MaterialHeight", lists.toArray()[3].toString());
            editor.putString("uxdl_MaterialWidth", lists.toArray()[1].toString());
            editor.putString("uxdl_SunMaoLength", lists.toArray()[5].toString());
            editor.putString("uxdl_SunMaoWidth", lists.toArray()[7].toString());
            editor.putString("uxdl_SunMaoThickness", lists.toArray()[9].toString());
            editor.putString("uxdl_LeftMargin", lists.toArray()[11].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("U型刀路", pathname)).toString());
            editor.commit();
        }
        if (list3.contains("边线加工刀路")) {
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("边线加工刀路", pathname));
            pref = getActivity().getSharedPreferences("边线加工刀路", Context.MODE_PRIVATE);
            editor = pref.edit();
//            editor.putString("bxjg_Groovedepth", lists.toArray()[3].toString());
            editor.putString("bxjg_thickness", lists.toArray()[5].toString());
            editor.putString("bxjg_Width", lists.toArray()[3].toString());
            editor.putString("bxjg_Length", lists.toArray()[1].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("边线加工刀路", pathname)).toString());
            editor.commit();
        }
        if (list3.contains("边线加工刀路1")) {
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("边线加工刀路1", pathname));
            pref = getActivity().getSharedPreferences("边线加工刀路1", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("bxjg_thickness", lists.toArray()[5].toString());
            editor.putString("bxjg_Width", lists.toArray()[3].toString());
            editor.putString("bxjg_Length", lists.toArray()[1].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("边线加工刀路1", pathname)).toString());
            editor.commit();
        }
        if (list3.contains("边线加工刀路2")) {
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("边线加工刀路2", pathname));
            pref = getActivity().getSharedPreferences("边线加工刀路2", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("bxjg_thickness", lists.toArray()[5].toString());
            editor.putString("bxjg_Width", lists.toArray()[3].toString());
            editor.putString("bxjg_Length", lists.toArray()[1].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("边线加工刀路2", pathname)).toString());
            editor.commit();
        }
        if (list3.contains("边线加工刀路3")) {
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("边线加工刀路3", pathname));
            pref = getActivity().getSharedPreferences("边线加工刀路3", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("bxjg_thickness", lists.toArray()[5].toString());
            editor.putString("bxjg_Width", lists.toArray()[3].toString());
            editor.putString("bxjg_Length", lists.toArray()[1].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("边线加工刀路3", pathname)).toString());
            editor.commit();
        }
        if (list3.contains("母圆榫刀路")) {
            pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("母圆榫刀路", pathname));
            pref = getActivity().getSharedPreferences("母圆榫刀路", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("mys_CentrePoint", lists.toArray()[7].toString());
            editor.putString("mys_Groovedepth", lists.toArray()[5].toString());
            editor.putString("mys_Radius", lists.toArray()[9].toString());
            editor.putString("mys_MaterialHeight", lists.toArray()[3].toString());
            editor.putString("mys_MaterialWidth", lists.toArray()[1].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("母圆榫刀路", pathname)).toString());
            editor.commit();
        }
        if (list3.contains("榫槽刀路")) {
            pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("榫槽刀路", pathname));
            pref = getActivity().getSharedPreferences("榫槽刀路", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("scdl_Groovewidth", lists.toArray()[13].toString());
            editor.putString("scdl_Groovedepth", lists.toArray()[11].toString());
            editor.putString("scdl_Groovelength", lists.toArray()[9].toString());
            editor.putString("scdl_MaterialHeight", lists.toArray()[3].toString());
            editor.putString("scdl_MaterialWidth", lists.toArray()[1].toString());
            editor.putString("scdl_LeftMargin", lists.toArray()[5].toString());
            editor.putString("scdl_BottonMargin", lists.toArray()[7].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("榫槽刀路", pathname)).toString());
            editor.commit();
        }
        if (list3.contains("榫槽刀路1")) {
            pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("榫槽刀路1", pathname));
            pref = getActivity().getSharedPreferences("榫槽刀路1", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("scdl_Groovewidth", lists.toArray()[13].toString());
            editor.putString("scdl_Groovedepth", lists.toArray()[11].toString());
            editor.putString("scdl_Groovelength", lists.toArray()[9].toString());
            editor.putString("scdl_MaterialHeight", lists.toArray()[3].toString());
            editor.putString("scdl_MaterialWidth", lists.toArray()[1].toString());
            editor.putString("scdl_LeftMargin", lists.toArray()[5].toString());
            editor.putString("scdl_BottonMargin", lists.toArray()[7].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("榫槽刀路1", pathname)).toString());
            editor.commit();
        }
        if (list3.contains("直扁母榫刀路")) {
            pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("直扁母榫刀路", pathname));
            pref = getActivity().getSharedPreferences("直扁母榫刀路", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("zbms_Groovewidth", lists.toArray()[13].toString());
            editor.putString("zbms_Groovedepth", lists.toArray()[11].toString());
            editor.putString("zbms_Groovelength", lists.toArray()[9].toString());
            editor.putString("zbms_MaterialHeight", lists.toArray()[3].toString());
            editor.putString("zbms_MaterialWidth", lists.toArray()[1].toString());
            editor.putString("zbms_LeftMargin", lists.toArray()[5].toString());
            editor.putString("zbms_BottonMargin", lists.toArray()[7].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("直扁母榫刀路", pathname)).toString());
            editor.commit();
        }
        if (list3.contains("直扁母榫刀路1")) {
            pref = getActivity().getSharedPreferences(data, Context.MODE_PRIVATE);
            lists = new ArrayList<String>();
            lists = sqlHandle.sunmao_parameter(sqlHandle.select_path_r_id("直扁母榫刀路1", pathname));
            pref = getActivity().getSharedPreferences("直扁母榫刀路1", Context.MODE_PRIVATE);
            editor = pref.edit();
            editor.putString("zbms_Groovewidth", lists.toArray()[13].toString());
            editor.putString("zbms_Groovedepth", lists.toArray()[11].toString());
            editor.putString("zbms_Groovelength", lists.toArray()[9].toString());
            editor.putString("zbms_MaterialHeight", lists.toArray()[3].toString());
            editor.putString("zbms_MaterialWidth", lists.toArray()[1].toString());
            editor.putString("zbms_LeftMargin", lists.toArray()[5].toString());
            editor.putString("zbms_BottonMargin", lists.toArray()[7].toString());
            editor.putString("ab", sqlHandle.sunmao_AB(sqlHandle.select_path_r_id("直扁母榫刀路1", pathname)).toString());
            editor.commit();
        }

    }
*/
    public static void saveBitmapToLocal(String fileName, Bitmap bitmap) {
        try {
            // 创建文件流，指向该路径，文件名叫做fileName
            File file = new File(FILE_PATH, fileName);
            // file其实是图片，它的父级File是文件夹，判断一下文件夹是否存在，如果不存在，创建文件夹
            File fileParent = file.getParentFile();
            if (!fileParent.exists()) {
                // 文件夹不存在
                fileParent.mkdirs();// 创建文件夹
            }
            // 将图片保存到本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地SD卡获取缓存的bitmap
     */
    public static Bitmap getBitmapFromLocal(String fileName) {
        try {
            File file = new File(FILE_PATH, fileName);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(
                        file));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void SaveEdit(int position, String string) {
        //回调处理edittext内容，使用map的好处在于：position确定的情况下，string改变，只会动态改变string内容
        map.put(position, string);
    }
}