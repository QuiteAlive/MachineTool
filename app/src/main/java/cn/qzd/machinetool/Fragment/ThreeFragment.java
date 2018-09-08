package cn.qzd.machinetool.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import cn.qzd.machinetool.R;
import cn.qzd.machinetool.helper.SQLHandle;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Created by admin on 2018/5/25.
 */

public class ThreeFragment extends Fragment {
    private Spinner spinner;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;
    //private SQLHandle sqlHandle;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.three_fragment, container, false);
        spinner = (Spinner) rootView.findViewById(R.id.spinner_jcwj);
       // sqlHandle = new SQLHandle();

      /*  list = new ArrayList();
        for (int i = 0; i < sqlHandle.jcwj().size(); i++) {
            list.add(sqlHandle.jcwj().toArray()[i].toString());
        }
        Toast.makeText(getActivity(), sqlHandle.top(53).toString(), Toast.LENGTH_LONG);
        Log.d("123", sqlHandle.top(53).toString());*/
        pref=getActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sqlHandle.jcwj(sqlHandle.widther(sqlHandle.userid(pref.getString("account",""))),sqlHandle.company(pref.getString("account",""))));
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
       // spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(listener);
        return rootView;
    }

    OnItemSelectedListener listener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String data = spinner.getSelectedItem().toString();
           // int fileid=  sqlHandle.select_filenameid(data);
          /*  pref=getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
          //  pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
            editor=pref.edit();
            editor.putString("top",sqlHandle.top(fileid).toString());//文件头
            editor.putString("bottom",sqlHandle.bottom(fileid).toString());//文件尾
           /* editor.putString("直线快速定位",sqlHandle.Gcode().toArray()[0].toString()+"0"+sqlHandle.Gcode().toArray()[1].toString());
            editor.putString("直线插补，切削进给",sqlHandle.Gcode().toArray()[2].toString()+"0"+sqlHandle.Gcode().toArray()[3].toString());
            editor.putString("顺时针圆弧插补",sqlHandle.Gcode().toArray()[4].toString()+"0"+sqlHandle.Gcode().toArray()[5].toString());
            editor.putString("逆时针圆弧插补",sqlHandle.Gcode().toArray()[6].toString()+"0"+sqlHandle.Gcode().toArray()[7].toString());
            editor.putString("程序暂停",sqlHandle.Gcode().toArray()[8].toString()+sqlHandle.Gcode().toArray()[9].toString());
            editor.putString("XY平面",sqlHandle.Gcode().toArray()[10].toString()+sqlHandle.Gcode().toArray()[11].toString());
            editor.putString("XZ平面",sqlHandle.Gcode().toArray()[12].toString()+sqlHandle.Gcode().toArray()[13].toString());
            editor.putString("YZ平面",sqlHandle.Gcode().toArray()[14].toString()+sqlHandle.Gcode().toArray()[15].toString());
            editor.putString("会机床参考点",sqlHandle.Gcode().toArray()[16].toString()+sqlHandle.Gcode().toArray()[17].toString());
            editor.putString("加工坐标系设定1",sqlHandle.Gcode().toArray()[18].toString()+sqlHandle.Gcode().toArray()[19].toString());
            editor.putString("加工坐标系设定2",sqlHandle.Gcode().toArray()[20].toString()+sqlHandle.Gcode().toArray()[21].toString());
            editor.putString("加工坐标系设定3",sqlHandle.Gcode().toArray()[22].toString()+sqlHandle.Gcode().toArray()[23].toString());
            editor.putString("加工坐标系设定4",sqlHandle.Gcode().toArray()[24].toString()+sqlHandle.Gcode().toArray()[25].toString());
            editor.putString("加工坐标系设定5",sqlHandle.Gcode().toArray()[26].toString()+sqlHandle.Gcode().toArray()[27].toString());
            editor.putString("加工坐标系设定6",sqlHandle.Gcode().toArray()[28].toString()+sqlHandle.Gcode().toArray()[29].toString());
            editor.putString("程序暂停",sqlHandle.Gcode().toArray()[30].toString()+sqlHandle.Gcode().toArray()[31].toString());
            editor.putString("英制",sqlHandle.Gcode().toArray()[32].toString()+sqlHandle.Gcode().toArray()[33].toString());
            editor.putString("公制",sqlHandle.Gcode().toArray()[34].toString()+sqlHandle.Gcode().toArray()[35].toString());
            editor.putString("绝对位置",sqlHandle.Gcode().toArray()[36].toString()+sqlHandle.Gcode().toArray()[37].toString());
            editor.putString("相对(增量)位置",sqlHandle.Gcode().toArray()[38].toString()+sqlHandle.Gcode().toArray()[39].toString());
            editor.putString("程序暂停",sqlHandle.Gcode().toArray()[40].toString()+"0"+sqlHandle.Gcode().toArray()[41].toString());
            editor.putString("程序选择性暂停",sqlHandle.Gcode().toArray()[42].toString()+"0"+sqlHandle.Gcode().toArray()[43].toString());
            editor.putString("程序结束",sqlHandle.Gcode().toArray()[44].toString()+"0"+sqlHandle.Gcode().toArray()[45].toString());
            editor.putString("主轴正转",sqlHandle.Gcode().toArray()[46].toString()+"0"+sqlHandle.Gcode().toArray()[47].toString());
            editor.putString("主轴停止",sqlHandle.Gcode().toArray()[48].toString()+"0"+sqlHandle.Gcode().toArray()[49].toString());
            editor.putString("夹具夹紧",sqlHandle.Gcode().toArray()[50].toString()+sqlHandle.Gcode().toArray()[51].toString());
            editor.putString("夹具松开",sqlHandle.Gcode().toArray()[52].toString()+sqlHandle.Gcode().toArray()[53].toString());
            editor.putString("等待机械手信号",sqlHandle.Gcode().toArray()[54].toString()+sqlHandle.Gcode().toArray()[55].toString());
            editor.putString("执行换刀动作",sqlHandle.Gcode().toArray()[56].toString());
            editor.putString("定义插补切削速度",sqlHandle.Gcode().toArray()[58].toString());
            editor.putString("定义主轴转速",sqlHandle.Gcode().toArray()[60].toString());*/
          /*  editor.putString("每分钟进给",sqlHandle.Gcode().toArray()[62].toString()+sqlHandle.Gcode().toArray()[63].toString());
            editor.putString("Biased_information",sqlHandle.lsxy(fileid).toString());
            editor.commit();
          //  String lsxy=sqlHandle.Gcode().toArray()[0].toString()+sqlHandle.Gcode().toArray()[1].toString();
           /* Toast.makeText(getActivity(), sqlHandle.top(fileid).toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), sqlHandle.bottom(fileid).toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(),lsxy, Toast.LENGTH_SHORT).show();*/
         //   Toast.makeText(getActivity(),sqlHandle.lsxy(fileid).toString(), Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}



