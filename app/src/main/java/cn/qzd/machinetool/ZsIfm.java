package cn.qzd.machinetool;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
import java.util.List;

import cn.qzd.machinetool.LitepalBase.QM_MT_SunMaoInfo;
import cn.qzd.machinetool.helper.SQLHandle;
import cn.qzd.machinetool.helper.TitleBar;


/**
 * 显示榫卯组合图片
 * Created by Administrator on 2018/1/26 0026.
 */

public class ZsIfm extends AppCompatActivity implements View.OnClickListener {
    SQLHandle sqlHandle;
    Bundle bundle;
    String zs_type, xs_type;
    List sunmao_child;
    private Bitmap bitmap1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.zsifm_layout);
        title();
        final ImageView zsm = (ImageView) findViewById(R.id.zsmpt);
       // final ImageView zswm = (ImageView) findViewById(R.id.zswpt);
       // ImageView third = (ImageView) findViewById(R.id.third);
       // TextView joint_type3 = (TextView) findViewById(R.id.joint_type3);
       // TextView joint_type2 = (TextView) findViewById(R.id.joint_type2);
        TextView joint_type1 = (TextView) findViewById(R.id.joint_type1);
        LinearLayout ll_one = (LinearLayout) findViewById(R.id.one);
       // LinearLayout ll_two = (LinearLayout) findViewById(R.id.two);
      //  LinearLayout ll_three = (LinearLayout) findViewById(R.id.three);
        zsm.setOnClickListener(this);
       // zswm.setOnClickListener(this);
       /* third.setOnClickListener(this);
        ll_two.setVisibility(View.GONE);
        ll_three.setVisibility(View.GONE);*/

        bundle = getIntent().getExtras();
        sqlHandle = new SQLHandle();
        String father = bundle.getString("fathername");//父榫卯名称

        Toast.makeText(ZsIfm.this, father, 0).show();
        joint_type1.setText(father);
        /*******************本地数据库查询**********************/
        List<QM_MT_SunMaoInfo> list1 = LitePal.select("sImage")
                .where("SunMaoName = ?", father)
                .limit(1)
                .find(QM_MT_SunMaoInfo.class);
        for (QM_MT_SunMaoInfo qm_mt_sunMaoInfo : list1) {
            byte[] in = qm_mt_sunMaoInfo.getsImage();
            bitmap1 = BitmapFactory.decodeByteArray(in, 0, in.length);
        }

        /*************远程数据库***************/
        // Bitmap bitmap= sqlHandle.fsunmao_type_image(father);
        // zsm.setImageBitmap(bitmap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        Glide.with(this)
                .load(bytes)
                .placeholder(R.drawable.loading)
                .into(zsm);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ZsIfm.this, SunMaoData.class);
        intent.putExtra("fsunmaoname", bundle.getString("fathername"));//父榫卯名称
        intent.putExtra("csunmao_no", bundle.getString("childname_no"));//子榫卯的代号
        intent.putExtra("csunmao_name", bundle.getString("childname_name"));//子榫卯的名称
        startActivity(intent);
    }

    //
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

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}





