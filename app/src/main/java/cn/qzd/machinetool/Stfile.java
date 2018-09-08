package cn.qzd.machinetool;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import cn.qzd.machinetool.LitepalBase.QM_MT_SunMaoInfo;
import cn.qzd.machinetool.helper.TitleBar;


/**
 * 显示所有的榫头列表
 * Created by Administrator on 2018/1/26 0026.
 */

public class Stfile extends AppCompatActivity {
    ListView listView1;
    EditText editText;
    private List list2 = new ArrayList();
    private List<String> list = new ArrayList<String>();
    private SwipeRefreshLayout swipeRefreshLayout;
    Bundle bundle;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.stfile_layout);
        title();
//       int machion= bundle.getInt("Machinetype");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        listView1 = (ListView) findViewById(R.id.listView1);
        editText = (EditText) findViewById(R.id.editText);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        //添加榫头的类型
        //从数据库获取
      /*  SQLHandle sqlHandle=new SQLHandle();
        list=sqlHandle.fathername();//榫卯总名*/
        //从本地数据库获取数据
        List<QM_MT_SunMaoInfo> list1 = LitePal.select("SunMaoName")
                .where("(ParentID = 0 or ParentID = infoID) and IsUse= 1").find(QM_MT_SunMaoInfo.class);
        for (QM_MT_SunMaoInfo qm_mt_sunMaoInfo : list1) {
            list2.add(qm_mt_sunMaoInfo.getSunMaoName());
        }
        /**************************************************/
        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(Stfile.this, android.R.layout.simple_list_item_1,
                list2);
        listView1.setAdapter(myArrayAdapter);
        listView1.setTextFilterEnabled(true);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //以及传递值往下一个页面显示不同的图片以及名称即ZsIfm
                String dddd = list2.get(position).toString();
                Toast.makeText(Stfile.this, dddd, 0).show();

                List<QM_MT_SunMaoInfo> list1 = LitePal.select("InfoID")
                        .where("SunMaoName = ?", dddd)
                        .limit(1)
                        .find(QM_MT_SunMaoInfo.class);
                int infoid = 0;
                for (QM_MT_SunMaoInfo qm_mt_sunMaoInfo : list1) {
                    infoid = qm_mt_sunMaoInfo.getInfoID();
                }
                // int infoid = sqlHandle.infoid(dddd);
                List list4 = new ArrayList();
                List<QM_MT_SunMaoInfo> list3 = LitePal.select("SunMaoName")
                        .where("ParentID=?", String.valueOf(infoid)).find(QM_MT_SunMaoInfo.class);
                for (QM_MT_SunMaoInfo qm_mt_sunMaoInfo : list3) {
                    list4.add(qm_mt_sunMaoInfo.getSunMaoName());
                }
                //List childname_no = sqlHandle.info_parent(infoid);//子榫卯的名称以及代号
                List list5 = new ArrayList();
                List<QM_MT_SunMaoInfo> list6 = LitePal.select("SunMaoNo")
                        .where("ParentID=?", String.valueOf(infoid)).find(QM_MT_SunMaoInfo.class);
                for (QM_MT_SunMaoInfo qm_mt_sunMaoInfo : list6) {
                    list5.add(qm_mt_sunMaoInfo.getSunMaoNo());
                }

                Intent intent = new Intent(Stfile.this, ZsIfm.class);
                intent.putExtra("childname_name", list4.toString());//子榫卯名称
                intent.putExtra("childname_no", list5.toString());//子榫卯的代号
                intent.putExtra("fathername", dddd);//父榫卯的名称
                startActivity(intent);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            //注册编辑框文本发生变化时的监听方法
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String key = s.toString();
                if (TextUtils.isEmpty(key)) {
                    listView1.clearTextFilter();
                } else {
                    listView1.setFilterText(key);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      /*  SQLHandle sqlHandle=new SQLHandle();
                        list=sqlHandle.fathername();*/
                        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(Stfile.this, android.R.layout.simple_list_item_1,
                                list2);
                        listView1.setAdapter(myArrayAdapter);
                        listView1.setTextFilterEnabled(true);
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(Stfile.this, "刷新完成", 0).show();
                    }
                });
            }
        }).start();
    }

    private void title() {
        boolean isImmersive = false;
        if (hasKitKat() && !hasLollipop()) {
            isImmersive = true;
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else if (hasLollipop()) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            isImmersive = true;
        }
        final TitleBar titleBar = (TitleBar) findViewById(R.id.title_bar);
        titleBar.setImmersive(isImmersive);
        titleBar.setBackgroundColor(Color.parseColor("#64b4ff"));


        titleBar.setLeftImageResource(R.mipmap.back_green);
        titleBar.setLeftText("返回");
        titleBar.setLeftTextColor(Color.WHITE);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setTitle("榫头类型");
        titleBar.setTitleColor(Color.WHITE);
    }

    //获取sdcard路径
    public static String getExternalStoragePath() {
        //获取状态
        String state = Environment.getExternalStorageState();
        //判断是否可读
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Environment.getExternalStorageDirectory().canRead()) {
                return Environment.getExternalStorageDirectory().getPath();
            }
        }
        return null;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}