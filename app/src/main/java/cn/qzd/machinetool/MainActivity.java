package cn.qzd.machinetool;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import cn.qzd.machinetool.ImageSlideshow.ImageSlideshow;
import cn.qzd.machinetool.helper.TitleBar;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    private ImageSlideshow imageSlideshow;
    private List<String> imageUrlList;
    private List<String> titleList;
    protected ImageLoader imageLoader;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.main_layout);
        title();
        /******/
//        pref=getSharedPreferences("data", Context.MODE_PRIVATE);
//        editor=pref.edit();
//        editor.putString("Biased_information","");
//        editor.commit();
        /****************/
        requestEachRxPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        imageSlideshow = (ImageSlideshow) findViewById(R.id.is_gallery);
        imageUrlList = new ArrayList<>();
        titleList = new ArrayList<>();
        initData();
        // 为ImageSlideshow设置数据
        imageSlideshow.setDotSpace(12);
        imageSlideshow.setDotSize(12);
        imageSlideshow.setDelay(4000);
        imageSlideshow.setOnItemClickListener(new ImageSlideshow.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                      /*  Uri uri = Uri.parse("http://www.qzdjx.cn/page/zpzs/shukongfangkongzuan/2015/0720/35.html");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);*/
                        //startActivity(new Intent(MainActivity.this,Activity_1.class));
                        break;
                    case 1:
                       // startActivity(new Intent(MainActivity.this,Activity_2.class));
                        break;
                    case 2:
                        //startActivity(new Intent(MainActivity.this,Activity_3.class));
                        break;
                    case 3:
                      //  startActivity(new Intent(MainActivity.this,Activity_4.class));
                        break;
                    case 4:
                      //  startActivity(new Intent(MainActivity.this,Activity_5.class));
                        break;
                }
            }
        });
        imageSlideshow.commit();
//小五轴机床
        final Button xwz = (Button) findViewById(R.id.btn_five_axle);
        xwz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JointList.class);
                intent.putExtra("Machinetype",1);
                startActivity(intent);
            }
        });
        //榫头机机床
        final Button stj = (Button) findViewById(R.id.btn_tenonmachine);
        stj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JointList.class);//
                intent.putExtra("Machinetype",2);
                startActivity(intent);
            }
        });
        final Button dwz = (Button) findViewById(R.id.btn_big_five_axle);
        dwz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Stfile.class);//
                intent.putExtra("Machinetype",3);
                startActivity(intent);
            }
        });
        final Button scj = (Button) findViewById(R.id.btn_slotmortiser);
        scj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Stfile.class);//
                intent.putExtra("Machinetype",4);
                startActivity(intent);
            }
        });
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
        titleBar.setTitle("机床类型\n基于android5.1");
        titleBar.setTitleColor(Color.WHITE);
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String[] imageUrls = {"http://www.qzdjx.cn/uploads/150616/1-150616144R3264.jpg",
                "http://www.qzdjx.cn/uploads/170208/1-1F20Q52601Q8-lp.jpg",
                "http://www.qzdjx.cn/uploads/allimg/161228/1_1457343782-lp.jpg",
                "http://www.qzdjx.cn/uploads/150720/1-150H0144404551.jpg",
                "http://www.qzdjx.cn/uploads/150720/1-150H014430M08.jpg"};
        String[] titles = {"公司理念",
                "数控榫头加工中心 CNC-1200B　",
                "数控榫头加工中心 CNC-1200C",
                "数控榫眼机",
                "数控方孔钻　"};
        for (int i = 0; i < 5; i++) {

            imageSlideshow.addImageTitle(imageUrls[i], titles[i]);
        }
    }
    private void requestEachRxPermission(String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(permissions).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(@NonNull Permission permission) throws Exception {
                if (permission.granted) {
                    // Toast.makeText(ZXingActivity.this, "已获取权限"+ permission.name , Toast.LENGTH_SHORT).show();
                } else if (permission.shouldShowRequestPermissionRationale){
                    //拒绝权限请求
                    Toast.makeText(MainActivity.this, "已拒绝权限"+ permission.name , Toast.LENGTH_SHORT).show();
                } else {
                    // 拒绝权限请求,并不再询问
                    // 可以提醒用户进入设置界面去设置权限
                    Toast.makeText(MainActivity.this, "已拒绝权限"+ permission.name +"并不再询问", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}