package cn.qzd.machinetool.ZXing;

import android.Manifest;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import cn.qzd.machinetool.LitepalBase.QM_MT_SunMaoInfo;
import cn.qzd.machinetool.R;

import com.tbruyelle.rxpermissions2.Permission;

import org.litepal.LitePal;

import cn.qzd.machinetool.helper.TitleBar;
import cn.qzd.machinetool.helper.SQLHandle;
import io.reactivex.functions.Consumer;


/**
 * Created by Administrator on 2018/1/26 0026.
 */
public class ZXingActivity extends AppCompatActivity {
    Bitmap bitmap2, bitmap5;
    SQLHandle sqlHandle;
    Bundle bundle;
    private ImageView im;
    private EditText content;
    String pathtype;
    private static final String TAG = "RxPermissionTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.qrcord_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        title();
        requestEachRxPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        bundle = getIntent().getExtras();
        String allchilds = bundle.getString("csunmao_code");
        String[] onechild = allchilds.split(",");
        int index = onechild.length;
        sqlHandle = new SQLHandle();

        content = (EditText) findViewById(R.id.et_content);
        final RadioButton but_qrcode = (RadioButton) findViewById(R.id.qrcode);//二维码
        final RadioButton but_barcode = (RadioButton) findViewById(R.id.barcode);//条形码
        final RadioButton sunmao1 = (RadioButton) findViewById(R.id.sunmao1);//1
        final RadioButton sunmao2 = (RadioButton) findViewById(R.id.sunmao2);//2
        final RadioButton sunmao3 = (RadioButton) findViewById(R.id.sunmao3);//3
        final Button save = (Button) findViewById(R.id.sava);
        im = (ImageView) findViewById(R.id.bitmap_image);
        im.setImageResource(R.drawable.write);
        save.setEnabled(false);
        if (index == 1) {
            sunmao2.setVisibility(View.GONE);
            sunmao3.setVisibility(View.GONE);
        } else if (index == 2) {
            sunmao3.setVisibility(View.GONE);
        } else if (index == 3) {
        }
        //动态获取edittext内容
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String Scontent = content.getText().toString();
                if (sunmao1.isChecked()) {
                    pathtype = onechild[0].trim();
                    List<QM_MT_SunMaoInfo> list1 = LitePal.select("sImage")
                            .where("SunMaoNo = ? and parentid !=0", pathtype)
                            .limit(1)
                            .find(QM_MT_SunMaoInfo.class);
                    for (QM_MT_SunMaoInfo qm_mt_sunMaoInfo : list1) {
                        byte[] in = qm_mt_sunMaoInfo.getsImage();
                        bitmap2 = BitmapFactory.decodeByteArray(in, 0, in.length);
                    }
                    // bitmap2=sqlHandle.sunmao_type_image(pathtype.trim());
                }
                if (sunmao2.isChecked()) {
                    pathtype = onechild[1].trim();
                    List<QM_MT_SunMaoInfo> list1 = LitePal.select("sImage")
                            .where("SunMaoNo = ?", pathtype)
                            .find(QM_MT_SunMaoInfo.class);
                    for (QM_MT_SunMaoInfo qm_mt_sunMaoInfo : list1) {
                        byte[] in = qm_mt_sunMaoInfo.getsImage();
                        bitmap2 = BitmapFactory.decodeByteArray(in, 0, in.length);
                    }
                    // bitmap2=sqlHandle.sunmao_type_image(pathtype.trim());

                }
                if (sunmao3.isChecked()) {
                    pathtype = onechild[2].trim();
                    List<QM_MT_SunMaoInfo> list1 = LitePal.select("sImage")
                            .where("SunMaoNo = ?", pathtype)
                            .find(QM_MT_SunMaoInfo.class);
                    for (QM_MT_SunMaoInfo qm_mt_sunMaoInfo : list1) {
                        byte[] in = qm_mt_sunMaoInfo.getsImage();
                        bitmap2 = BitmapFactory.decodeByteArray(in, 0, in.length);
                    }
                    //   bitmap2=sqlHandle.sunmao_type_image(pathtype.trim());
                }
                if (but_qrcode.isChecked() == true) {
                    if (Scontent != null && Scontent.length() > 0 && Scontent.length() <= 16) {
                        save.setEnabled(true);
                        Bitmap bitmap1 = drawTextToBitmap(getBaseContext(), "    " + pathtype + "识别内容:" + Scontent);//文字
                        Matrix matrix = new Matrix();
                        matrix.setScale(1f, 1f);
                        Bitmap bmap = resizeBitmap(bitmap2, 222, 222);
                        Bitmap bm = Bitmap.createBitmap(bmap, 0, 0, bmap.getWidth(),
                                bmap.getHeight(), matrix, true);
                        Bitmap bitmap3 = newbitmap(bitmap1, bm, 295, 160);
                        Bitmap bitmap4 = ZXingUtils.createQRImage(Scontent, 340, 340);//设置二维码大小
                        bitmap5 = newbitmap(bitmap3, bitmap4, 235, 380);//宽，高
                        im.setImageBitmap(bitmap5);
                    } else {
                        im.setImageResource(R.drawable.write);
                        save.setEnabled(false);
                    }
                } else {
                    if (Scontent != null && Scontent.length() > 0 && Scontent.length() <= 16 && isChinese1(Scontent) == false) {
                        save.setEnabled(true);
                        Bitmap bitmap1 = drawTextToBitmap(getBaseContext(), "     " + pathtype + "识别内容:" + Scontent);//文字
                        Matrix matrix = new Matrix();
                        matrix.setScale(1f, 1f);
                        Bitmap bmap = resizeBitmap(bitmap2, 222, 222);
                        Bitmap bm = Bitmap.createBitmap(bmap, 0, 0, bmap.getWidth(),
                                bmap.getHeight(), matrix, true);
                        Bitmap bitmap3 = newbitmap(bitmap1, bm, 295, 160);//榫卯图片位置
                        Bitmap bitmap4 = ZXingUtils.creatBarcode(getBaseContext(), Scontent, 400, 200, true);//设置条形码大小
                        bitmap5 = newbitmap(bitmap3, bitmap4, 200, 400);//宽，高
                        im.setImageBitmap(bitmap5);
                    } else if (isChinese1(Scontent)) {
                        im.setImageResource(R.drawable.write);
                        save.setEnabled(false);
                        Toast.makeText(ZXingActivity.this, "条形码不支持中文", 0).show();
                    } else if (Scontent.length() > 16) {
                        im.setImageResource(R.drawable.write);
                        save.setEnabled(false);
                        Toast.makeText(ZXingActivity.this, "最多输入16个字符", 0).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZXingUtils.saveImageToGallery(getBaseContext(), bitmap5);
                Toast.makeText(ZXingActivity.this, "二维码保存成功", 0).show();
            }
        });
    }

    public static Bitmap drawTextToBitmap(Context gContext, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap =
                BitmapFactory.decodeResource(resources, R.drawable.write);

        Bitmap.Config bitmapConfig =
                bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize((int) (12 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
//      paint.setTextAlign(Align.CENTER);

        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 4;
        int y = (bitmap.getHeight() + bounds.height()) / 6;

//      canvas.drawText(gText, x * scale, y * scale, paint);
        canvas.drawText(gText, x, y, paint);

        return bitmap;
    }

    public static Bitmap newbitmap(Bitmap bitmap1, Bitmap bitmap2, int x, int y) {
        Bitmap bitmap3 = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());
        Canvas canvas = new Canvas(bitmap3);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(bitmap2, x, y, null);
        return bitmap3;
    }

    private void requestEachRxPermission(String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(permissions).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(@NonNull Permission permission) throws Exception {
                if (permission.granted) {
                    // Toast.makeText(ZXingActivity.this, "已获取权限"+ permission.name , Toast.LENGTH_SHORT).show();
                } else if (permission.shouldShowRequestPermissionRationale) {
                    //拒绝权限请求
                    Toast.makeText(ZXingActivity.this, "已拒绝权限" + permission.name, Toast.LENGTH_SHORT).show();
                } else {
                    // 拒绝权限请求,并不再询问
                    // 可以提醒用户进入设置界面去设置权限
                    Toast.makeText(ZXingActivity.this, "已拒绝权限" + permission.name + "并不再询问", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 使用Matrix将Bitmap压缩到指定大小
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }

    /**
     * 给Bitmap设置圆角
     *
     * @param bitmap
     * @param roundPx 圆角值
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
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
        titleBar.setTitle("生成二维码");
        titleBar.setTitleColor(Color.WHITE);
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    // 判断一个字符是否是中文
    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    // 判断一个字符串是否含有中文
    public static boolean isChinese1(String str) {
        if (str == null) return false;
        for (char c : str.toCharArray()) {
            if (isChinese(c)) return true;// 有一个中文字符就返回
        }
        return false;
    }
}

