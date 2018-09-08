package cn.qzd.machinetool;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;

import org.litepal.LitePal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import cn.qzd.machinetool.Fragment.ThreeFragment;
import cn.qzd.machinetool.Fragment.ToolFragment;
import cn.qzd.machinetool.LitepalBase.QM_MT_A_User;
import cn.qzd.machinetool.LitepalBase.QM_MT_A_User_Role_Auth;
import cn.qzd.machinetool.LitepalBase.QM_MT_KnifePoint;
import cn.qzd.machinetool.LitepalBase.QM_MT_MachineFile1;
import cn.qzd.machinetool.LitepalBase.QM_MT_MachineFileTopBotton;
import cn.qzd.machinetool.LitepalBase.QM_MT_SaveFtp;
import cn.qzd.machinetool.LitepalBase.QM_MT_UserLatheCode;
import cn.qzd.machinetool.SimpleFTP.FTP;
import cn.qzd.machinetool.SimpleFTP.Result;
import cn.qzd.machinetool.ZXing.ZXingActivity;
import cn.qzd.machinetool.util.PreferencesUtils;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DataActivity extends FragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{ //, DataFragment.FragmentInteraction{
    Bundle bundle;
    //private  SQLHandle sqlHandle=new SQLHandle();
    private DrawerLayout mDrawerLayout;
    private ViewPager main_viewpager;
    private NavigationView navigation_view;
    private TextView ImageView2;
    private ImageView ImageView3;
    private List<Fragment> fragments = new ArrayList<>();
    private MainAdapter mAdapter;
    private ImageView NavigationImageView;
    private ToolFragment twoFragment;
    private ThreeFragment threeFragment;
    private SharedPreferences pref,pref1,pref2;
    private SharedPreferences.Editor editor,editor1;
    private String name;
    private File file = null;
    static final String TAG = "FTP";
    private EditText txtHostName;

    private EditText txtUserName;

    private EditText txtPasswrod;
    private EditText txtport;

    /**
     * FTP.
     */
    private FTP ftp;
    /**
     * 服务器名.
     */
    private String hostName;
    /**
     * d端口号
     */
    private int port;

    /**
     * 用户名.
     */
    private String userName;

    /**
     * 密码.
     */
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        bundle = getIntent().getExtras();
        pref1 = getSharedPreferences("tool_name", Context.MODE_PRIVATE);
        name = pref1.getString("name","");
        initView();
        initData();
        ImageView2.setText(name+'\n'+"刀具参数");
    }


    public String getPath(){
        String  pathtype=bundle.getString("csunmao_no");//子榫卯代号
      /*  if (bundle.get("sunmao_type").equals("test001")) {
           pathtype="test001";
            // Toast.makeText(DataActivity.this, "我是直榫公头传过来的值", Toast.LENGTH_SHORT).show();
          /*  String inputText = "uewruwuerewurhei";
            save(inputText);
            Intent intent = new Intent(DataActivity.this, GenerateGCode.class);
            startActivity(intent);*/

       /* } else if (bundle.get("sunmao_type").equals("D-T-A-Z-02")) {
           // Toast.makeText(DataActivity.this, "我是直榫母头传过来的值", Toast.LENGTH_SHORT).show();
            pathtype="D-T-A-Z-02";
        }*/
        return pathtype;
    }


    private void initData() {
       /* shez.setOnClickListener(this);
        shez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DataActivity.this, "测试一下", Toast.LENGTH_SHORT).show();
            }
        });*/
        NavigationImageView.setOnClickListener(this);
        /**
         * 利用menu设置监听事件
         */

        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int item_id = item.getItemId();
                switch (item_id) {
                  /*  case R.id.To_tool_file:
                        DialogProperties properties = new DialogProperties();//filepicker
                        properties.selection_type = DialogConfigs.FILE_SELECT;
                        properties.selection_mode = DialogConfigs.DIR_SELECT;
                        properties.selection_mode = DialogConfigs.SINGLE_MODE;
                        properties.offset = new File("/mnt/sdcard/aqmzn/");//默认路径
                        FilePickerDialog dialog = new FilePickerDialog(DataActivity.this, properties);
                        dialog.setDialogSelectionListener(new DialogSelectionListener() {
                            @Override
                            public void onSelectedFilePaths(String[] files) {
                                for (String path : files) {
                                    File file = new File(path);
                                    SharedPreferences sharedPreferences = getSharedPreferences("jcwj", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("path", file.getPath());
                                    editor.commit();
                                    //Toast.makeText(Sunm.this,file.getPath(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        dialog.show();
                        break;
                    case R.id.New_tool_file:
                        /*SharedPreferences sharedPreferences = getSharedPreferences("jcwj", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("path", "");
                        Toast.makeText(DataActivity.this, "文件初始化完成",
                                Toast.LENGTH_SHORT).show();
                        Intent intent5 = new Intent(DataActivity.this, BiasActivity.class);
                        startActivity(intent5);
                        editor.commit();*/
                     /*   break;
                    case R.id.sxyl:
                /* 新建一个Intent对象 */
                      /*  Intent intent = new Intent(DataActivity.this, BiasActivity.class);
                        startActivity(intent);*/
                /* 启动一个新的Activity */
                /* 关闭当前的Activity */
                        // Sunm.this.finish();
                      /*  break;
                    case R.id.G_code:
                        /*Intent intent4 = new Intent(DataActivity.this, GMessage.class);
                        startActivity(intent4);*/
                      /*  break;
                    case R.id.file_h_t:
                       /* Intent intent3 = new Intent(DataActivity.this, TitleHPro.class);
                        startActivity(intent3);*/
                       // break;
                    case R.id.open_ftp2:
                       /* Intent intent2 = new Intent(DataActivity.this, MainFTP.class);//
                        startActivity(intent2);*/
                        View view = getLayoutInflater().inflate(R.layout.dialog_layout,null);
                        txtHostName = (EditText) view.findViewById(R.id.txt_host_name);
                        txtUserName = (EditText) view.findViewById(R.id.txt_user_name);
                        txtPasswrod = (EditText) view.findViewById(R.id.txt_password);
                        txtport= (EditText) view.findViewById(R.id.txt_port);
                        pref =getSharedPreferences("data", Context.MODE_PRIVATE);
                        String ftpdata=pref.getString("ftp", "");
                        if (ftpdata.equals("")){
                            Toast.makeText(DataActivity.this,"没有选择机床文件请手动配置参数",0).show();
                        }else {
                        ftpdata = ftpdata.replaceAll("([+*/^()\\]\\[])", "");
                        String[] ftp1 = ftpdata.split(",");
                        txtHostName.setText(ftp1[0]);
                        txtport.setText(ftp1[1]);
                        }
                        AlertDialog.Builder builder=new AlertDialog.Builder(DataActivity.this);
                        builder.setTitle("FTP");
                        builder.setIcon(R.drawable.login_icon);
                        builder.setView(view);
                        builder.setPositiveButton("连接", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //   Toast.makeText(ViewDialog.this,"0",0).show();
                                DialogProperties properties = new DialogProperties();//filepicker
                                properties.selection_type = DialogConfigs.FILE_SELECT;
                                properties.selection_mode = DialogConfigs.DIR_SELECT;
                                properties.selection_mode = DialogConfigs.SINGLE_MODE;
                                properties.offset = new File("/mnt/sdcard/aqmzn/");//默认路径
                                FilePickerDialog dialog1 = new FilePickerDialog(DataActivity.this, properties);
                                dialog1.setDialogSelectionListener(new DialogSelectionListener() {
                                    @Override
                                    public void onSelectedFilePaths(String[] files) {
                                        for (String path : files) {
                                            file = new File(path);
                                            SharedPreferences sharedPreferences = getSharedPreferences("jcwj", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("path", file.getPath());
                                            editor.commit();
                                            //Toast.makeText(Sunm.this,file.getPath(),Toast.LENGTH_SHORT).show();
                                            hostName = txtHostName.getText().toString();
                                            userName = txtUserName.getText().toString();
                                            password = txtPasswrod.getText().toString();
                                            port=Integer.valueOf(txtport.getText().toString().trim());

                                            rx.Observable.create(new rx.Observable.OnSubscribe<String>() {
                                                @Override
                                                public void call(Subscriber<? super String> subscriber) {
                                                    //TODO do something like download or ...
                                                    Result result = null;
                                                    try {
                                                        if (ftp != null) {
                                                            // 关闭FTP服务
                                                            //ftp.closeConnect();
                                                        }
                                                        // 初始化FTP
                                                        Log.e("hostName", hostName);
                                                        Log.e("userName", userName);
                                                        Log.e("passwrod", password);
                                                        ftp = new FTP(hostName, userName, password,port);
                                                        // 打开FTP服务
                                                        ftp.openConnect();
                                                        // 上传
                                                        Log.d(TAG, "Upload!");
                                                        Log.d(TAG, "File name:" + file.getName());
                                                        Log.d(TAG, "File path:" + file.getPath());
                                                        result = ftp
                                                                .uploading(file, FTP.REMOTE_PATH);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                    subscriber.onNext("Ftp uploading" + result.isSucceed());
                                                    subscriber.onCompleted();
                                                }
                                            })
                                                    .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                                                    .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                                                    .subscribe(new Observer<String>() {
                                                        @Override
                                                        public void onNext(String s) {
                                                            //TODO finish and return used bean
                                                            Log.d(TAG, "onNext!" + s);
                                            /*Toast.makeText(FTPActivity.this,
                                                    "上传文件到服务器:" + s, Toast.LENGTH_SHORT)
                                                    .show();*/
                                                        }

                                                        @Override
                                                        public void onCompleted() {
                                                            Log.d(TAG, "Completed!");
                                                            Toast.makeText(DataActivity.this, "发送成功", Toast.LENGTH_SHORT).show();

                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {
                                                            e.printStackTrace();
                                                            Log.d(TAG, "Error!");
                                                            Toast.makeText(DataActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                                            try {
                                                                ftp.closeConnect();
                                                            } catch (IOException e1) {
                                                                e1.printStackTrace();
                                                            }

                                                        }
                                                    });
                                        }
                                    }

                                });dialog1.show();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                             //   Toast.makeText(DataActivity.this,"1",0).show();
                            }
                        });
                        builder.create().show();


                        break;
                    case R.id.zxing:
                        Intent intent6 = new Intent(DataActivity.this, ZXingActivity.class);
                        intent6.putExtra("csunmao_code",bundle.getString("csunmao_no"));
                        startActivity(intent6);
                        break;
                    default:
//                        Double e= mon.tL[0];
                        Toast.makeText(DataActivity.this,"苏州铨木智能科技有限公司", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(DataActivity.this, "测试一下", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_data);
        main_viewpager = (ViewPager) findViewById(R.id.main_viewpager);
        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        //ImageView1 = (ImageView) findViewById(R.id.ImageView1);
        ImageView2 = (TextView) findViewById(R.id.ImageView5);
        //ImageView3 = (ImageView) findViewById(R.id.ImageView3);
      /*  Join = (Button) findViewById(R.id.joinour);
        shez = (Button) findViewById(R.id.shezhi);*/
     //   sure_nc = (Button) findViewById(R.id.btn_sure_nc);
        NavigationImageView = (ImageView) findViewById(R.id.NavigationImageView);
        //  oneFragment = new DataFragment();
        twoFragment = new ToolFragment();
        //threeFragment = new ThreeFragment();

        //fragments.add(oneFragment);
        fragments.add(twoFragment);
      //  fragments.add(threeFragment);

//        ImageView1.setSelected(true);
        ImageView2.setSelected(true);

        mAdapter = new MainAdapter(getSupportFragmentManager());
        main_viewpager.setAdapter(mAdapter);
        main_viewpager.setOffscreenPageLimit(2);//设置缓存的页数

        main_viewpager.addOnPageChangeListener(this);
    }

    /**
     * NavigationImageView的监听事件（Imageview）
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        mDrawerLayout.openDrawer(navigation_view);
    }

    /**
     * 滑动
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 滑动选中
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                // ImageView1.setSelected(true);
                ImageView2.setSelected(true);
                //ImageView3.setSelected(false);
                break;
            case 1:
                //  ImageView1.setSelected(false);
               /* ImageView2.setSelected(true);
                ImageView3.setSelected(false);-*/
               /* ImageView2.setSelected(false);
                ImageView3.setSelected(true);
                break;*/
            case 2:
              /*  ImageView1.setSelected(false);
                ImageView2.setSelected(false);
                ImageView3.setSelected(true);
                break;*/
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Data Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


    class MainAdapter extends FragmentPagerAdapter {

        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

   /* public void one_iv(View view) {

        main_viewpager.setCurrentItem(0);

        ImageView1.setSelected(true);
        ImageView2.setSelected(false);
        ImageView3.setSelected(false);
    }*/

    public void two_iv(View view) {
        main_viewpager.setCurrentItem(0);

//        ImageView1.setSelected(false);
        ImageView2.setSelected(true);
      //  ImageView3.setSelected(false);
    }

/*    public void three_iv(View view) {
        main_viewpager.setCurrentItem(1);

//        ImageView1.setSelected(false);
        ImageView2.setSelected(false);
        ImageView3.setSelected(true);
    }*/

    public void btn_join(View view) {
        Toast.makeText(DataActivity.this, "苏州铨木智能科技有限公司"+"\n"+"电话:0512-65805340", Toast.LENGTH_SHORT).show();
    }

    public void save(String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("data.ptp", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save_nc(View view) {//生成NC程序按钮
      /*  Intent intent = new Intent(this,Main3Activity.class);
        startActivity(intent);*/
       /* if (bundle.get("sunmao_type").equals("Z-H-1")) {
            // Toast.makeText(DataActivity.this, "我是直榫公头传过来的值", Toast.LENGTH_SHORT).show();
           /* String inputText = "uewruwuerewurhei";
            save(inputText);
            Intent intent = new Intent(DataActivity.this, GenerateGCode.class);
            startActivity(intent);
        } else if (bundle.get("sunmao_type").equals("type_zhisunwoman")) {
            Toast.makeText(DataActivity.this, "我是直榫母头传过来的值", Toast.LENGTH_SHORT).show();
        }*/
    }
    public void im_tool(View view){
        PreferencesUtils.PREFERENCE_NAME="MachineType";
        int machinetype = PreferencesUtils.getInt(DataActivity.this,"machinetype");
        pref=getSharedPreferences("user",Context.MODE_PRIVATE);
        final AlertDialog.Builder builder=new  AlertDialog.Builder(this);
        builder.setTitle("选择机床文件");
        builder.setIcon(R.drawable.login_icon);
        //判断机床文件所属公司
        String company="";
        List<QM_MT_A_User> list3= LitePal.select("company")
                .where("loginname = ?",pref.getString("account",""))
                .find(QM_MT_A_User.class);
        for (QM_MT_A_User user:list3){
            company=user.getCompany();
        }
        //查询用户id
        int userid=-1;
        List<QM_MT_A_User> list4= LitePal.select("userid")
                .where("loginname = ?",pref.getString("account",""))
                .find(QM_MT_A_User.class);
        for (QM_MT_A_User user:list4){
            userid=user.getUserID();
        }
        //查询是否是超级管理员
        int roleid=-1;
        List<QM_MT_A_User_Role_Auth> list5= LitePal.select("roleid")
                .where("userid = ?",String.valueOf(userid))
                .find(QM_MT_A_User_Role_Auth.class);
        for (QM_MT_A_User_Role_Auth user_role_auth:list5){
           roleid=user_role_auth.getRoleId();
        }

        //没有做任何判段直接将所有的机床文件显示出来7.19
        //判断是否为超管以及是苏州铨木智能科技有限公司的用户可以看到所有的机床文件，客户只能看到自家的机床文件7.20
        List<String> filename=new ArrayList();
        List<QM_MT_MachineFile1> list2 = null;
        if (roleid==22||company.equals("苏州铨木智能科技有限公司")) {
            list2 = LitePal.select("filename")
                    .where("shaftid = ?",String.valueOf(machinetype))
                    .find(QM_MT_MachineFile1.class);
        }else {
            list2 = LitePal.select("filename")
                    .where("(company = ? and shaftid = ?) or shafttype = 2",company,String.valueOf(machinetype))
                    .find(QM_MT_MachineFile1.class);
        }
        for (QM_MT_MachineFile1 mt_machineFile1:list2){
            filename.add(mt_machineFile1.getFileName());
        }
        final String[] arrayRegion=filename.toArray(new String[filename.size()]);
        //final String[] arrayRegion=sqlHandle.jcwj(sqlHandle.widther(sqlHandle.userid(pref.getString("account",""))),sqlHandle.company(pref.getString("account",""))).toArray(new String[sqlHandle.jcwj(sqlHandle.widther(sqlHandle.userid(pref.getString("account",""))),sqlHandle.company(pref.getString("account",""))).size()]);
        pref2 = getSharedPreferences("tool_code", Context.MODE_PRIVATE);
        builder.setSingleChoiceItems(arrayRegion,pref2.getInt("which", -1) ,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       /* pref2=getSharedPreferences("tool_code",Context.MODE_PRIVATE);
                        editor1=pref2.edit();
                        editor1.putInt("which",which);
                        editor1.commit();*/
                    }
                });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog alertDialog=(AlertDialog) dialog;
                int sp=alertDialog.getListView().getCheckedItemPosition();
                String str=(String) alertDialog.getListView().getItemAtPosition(sp);
                /**************点击的item名字*************/
                pref1 = getSharedPreferences("tool_name", Context.MODE_PRIVATE);
                editor=pref1.edit();
                editor.putString("name",str);
                editor.commit();

                /********点击的第几个item*******/
                pref2=getSharedPreferences("tool_code",Context.MODE_PRIVATE);
                editor1=pref2.edit();
                editor1.putInt("which",sp);
                editor1.commit();
                /***********************/
                /*****/
                //sqlite机床文件id
                int fileid = 0;
                List<QM_MT_MachineFile1> list1= LitePal.select("MachinefileId")
                        .where("FileName = ?",str)
                        .find(QM_MT_MachineFile1.class);
                for (QM_MT_MachineFile1 qm_mt_machineFile1:list1){
                    fileid = qm_mt_machineFile1.getMachinefileId();
                }
                //sqlite机床文件shafttype
                int shafttype = 0;
                List<QM_MT_MachineFile1> list8= LitePal.select("shafttype")
                        .where("FileName = ?",str)
                        .find(QM_MT_MachineFile1.class);
                for (QM_MT_MachineFile1 qm_mt_machineFile1:list8){
                    shafttype = qm_mt_machineFile1.getShaftType();
                }
                PreferencesUtils.putInt(DataActivity.this,"shafttype",shafttype);
                //sqlite文件头尾
                List topbottom=new ArrayList();
                List<QM_MT_MachineFileTopBotton> list2= LitePal.select("FileTop,FileBottom")
                        .where("FileId = ? and ischecked = 1",String.valueOf(fileid))
                        .find(QM_MT_MachineFileTopBotton.class);
                for (QM_MT_MachineFileTopBotton qmtopbotttom:list2){
                    topbottom.add(qmtopbotttom.getFileTop());
                    topbottom.add(qmtopbotttom.getFileBottom());
                }
                //sqliteG代码
                List GCode=new ArrayList();
                List<QM_MT_UserLatheCode> list3= LitePal.select("codetype,number")
                        .where("FileId = ?",String.valueOf(fileid))
                        .find(QM_MT_UserLatheCode.class);
                for (QM_MT_UserLatheCode gcode:list3){
                    GCode.add(gcode.getCodeType());
                    GCode.add(gcode.getNumber());
                }
                //sqliteSXLY
                List knifepoint=new ArrayList();
                List<QM_MT_KnifePoint> list4= LitePal.select("L,S,X,Y,safey,safez")
                        .where("FileId = ?",String.valueOf(fileid))
                        .find(QM_MT_KnifePoint.class);
                for (QM_MT_KnifePoint scly:list4){
                    knifepoint.add(scly.getL());
                    knifepoint.add(scly.getS());
                    knifepoint.add(scly.getX());
                    knifepoint.add(scly.getY());
                    knifepoint.add(scly.getSafey());
                    knifepoint.add(scly.getSafez());
                }
                //SQLITE FTP
                List ftpsave=new ArrayList();
                List<QM_MT_SaveFtp> list5= LitePal.select("ip,port")
                        .where("FileId = ?",String.valueOf(fileid))
                        .find(QM_MT_SaveFtp.class);
                for (QM_MT_SaveFtp saveftp:list5){
                    ftpsave.add(saveftp.getIP());
                    ftpsave.add(saveftp.getPort());
                }
                ////////////阿里云
                //int fileid=  sqlHandle.select_filenameid(str);
                /******/
                pref=getSharedPreferences("data",Context.MODE_PRIVATE);
                editor=pref.edit();
                editor.putString("top",topbottom.get(0).toString());//文件头
                editor.putString("bottom",topbottom.get(1).toString());//文件尾
                editor.putString("直线快速定位",GCode.get(0).toString() + GCode.get(1).toString());
                editor.putString("直线插补，切削进给", GCode.get(2).toString() + GCode.get(3).toString());
                editor.putString("顺时针圆弧插补", GCode.get(4).toString() + GCode.get(5).toString());
                editor.putString("逆时针圆弧插补", GCode.get(6).toString() + GCode.get(7).toString());
                editor.putString("程序暂停", GCode.get(8).toString() + GCode.get(9).toString());
                editor.putString("XY平面", GCode.get(10).toString() + GCode.get(11).toString());
                editor.putString("XZ平面", GCode.get(12).toString() + GCode.get(13).toString());
                editor.putString("YZ平面", GCode.get(14).toString() + GCode.get(15).toString());
                editor.putString("会机床参考点", GCode.get(16).toString() + GCode.get(17).toString());
                editor.putString("加工坐标系设定1", GCode.get(18).toString() + GCode.get(19).toString());
                editor.putString("加工坐标系设定2", GCode.get(20).toString() + GCode.get(21).toString());
                editor.putString("加工坐标系设定3", GCode.get(22).toString() + GCode.get(23).toString());
                editor.putString("加工坐标系设定4", GCode.get(24).toString() + GCode.get(25).toString());
                editor.putString("加工坐标系设定5", GCode.get(26).toString() + GCode.get(27).toString());
                editor.putString("加工坐标系设定6", GCode.get(28).toString() + GCode.get(29).toString());
                editor.putString("程序暂停", GCode.get(30).toString() + GCode.get(31).toString());
                editor.putString("英制", GCode.get(32).toString() + GCode.get(33).toString());
                editor.putString("公制", GCode.get(34).toString() + GCode.get(35).toString());
                editor.putString("绝对位置", GCode.get(36).toString() + GCode.get(37).toString());
                editor.putString("相对(增量)位置", GCode.get(38).toString() + GCode.get(39).toString());
                editor.putString("程序暂停", GCode.get(40).toString() + GCode.get(41).toString());
                editor.putString("程序选择性暂停", GCode.get(42).toString() + GCode.get(43).toString());
                editor.putString("程序结束", GCode.get(44).toString() + GCode.get(45).toString());
                editor.putString("主轴正转", GCode.get(46).toString() + GCode.get(47).toString());
                editor.putString("主轴停止", GCode.get(48).toString() + GCode.get(49).toString());
                editor.putString("夹具夹紧", GCode.get(50).toString() + GCode.get(51).toString());
                editor.putString("夹具松开", GCode.get(52).toString() + GCode.get(53).toString());
                editor.putString("等待机械手信号", GCode.get(54).toString() + GCode.get(55).toString());
                editor.putString("执行换刀动作", GCode.get(56).toString());
                editor.putString("定义插补切削速度", GCode.get(58).toString());
                editor.putString("定义主轴转速", GCode.get(60).toString());
                editor.putString("每分钟进给", GCode.get(62).toString() + GCode.get(63).toString());
                editor.putString("Biased_information", knifepoint.toString());
                editor.putString("ftp", ftpsave.toString());
                editor.putString("fileid",String.valueOf(fileid));
               /* editor.putString("top",sqlHandle.top(fileid).toString());//文件头
                editor.putString("bottom", sqlHandle.bottom(fileid).toString());//文件尾
                editor.putString("直线快速定位", sqlHandle.Gcode().toArray()[0].toString() + sqlHandle.Gcode().toArray()[1].toString());
                editor.putString("直线插补，切削进给", sqlHandle.Gcode().toArray()[2].toString() + sqlHandle.Gcode().toArray()[3].toString());
                editor.putString("顺时针圆弧插补", sqlHandle.Gcode().toArray()[4].toString() + sqlHandle.Gcode().toArray()[5].toString());
                editor.putString("逆时针圆弧插补", sqlHandle.Gcode().toArray()[6].toString() + sqlHandle.Gcode().toArray()[7].toString());
                editor.putString("程序暂停", sqlHandle.Gcode().toArray()[8].toString() + sqlHandle.Gcode().toArray()[9].toString());
                editor.putString("XY平面", sqlHandle.Gcode().toArray()[10].toString() + sqlHandle.Gcode().toArray()[11].toString());
                editor.putString("XZ平面", sqlHandle.Gcode().toArray()[12].toString() + sqlHandle.Gcode().toArray()[13].toString());
                editor.putString("YZ平面", sqlHandle.Gcode().toArray()[14].toString() + sqlHandle.Gcode().toArray()[15].toString());
                editor.putString("会机床参考点", sqlHandle.Gcode().toArray()[16].toString() + sqlHandle.Gcode().toArray()[17].toString());
                editor.putString("加工坐标系设定1", sqlHandle.Gcode().toArray()[18].toString() + sqlHandle.Gcode().toArray()[19].toString());
                editor.putString("加工坐标系设定2", sqlHandle.Gcode().toArray()[20].toString() + sqlHandle.Gcode().toArray()[21].toString());
                editor.putString("加工坐标系设定3", sqlHandle.Gcode().toArray()[22].toString() + sqlHandle.Gcode().toArray()[23].toString());
                editor.putString("加工坐标系设定4", sqlHandle.Gcode().toArray()[24].toString() + sqlHandle.Gcode().toArray()[25].toString());
                editor.putString("加工坐标系设定5", sqlHandle.Gcode().toArray()[26].toString() + sqlHandle.Gcode().toArray()[27].toString());
                editor.putString("加工坐标系设定6", sqlHandle.Gcode().toArray()[28].toString() + sqlHandle.Gcode().toArray()[29].toString());
                editor.putString("程序暂停", sqlHandle.Gcode().toArray()[30].toString() + sqlHandle.Gcode().toArray()[31].toString());
                editor.putString("英制", sqlHandle.Gcode().toArray()[32].toString() + sqlHandle.Gcode().toArray()[33].toString());
                editor.putString("公制", sqlHandle.Gcode().toArray()[34].toString() + sqlHandle.Gcode().toArray()[35].toString());
                editor.putString("绝对位置", sqlHandle.Gcode().toArray()[36].toString() + sqlHandle.Gcode().toArray()[37].toString());
                editor.putString("相对(增量)位置", sqlHandle.Gcode().toArray()[38].toString() + sqlHandle.Gcode().toArray()[39].toString());
                editor.putString("程序暂停", sqlHandle.Gcode().toArray()[40].toString() + sqlHandle.Gcode().toArray()[41].toString());
                editor.putString("程序选择性暂停", sqlHandle.Gcode().toArray()[42].toString() + sqlHandle.Gcode().toArray()[43].toString());
                editor.putString("程序结束", sqlHandle.Gcode().toArray()[44].toString() + sqlHandle.Gcode().toArray()[45].toString());
                editor.putString("主轴正转", sqlHandle.Gcode().toArray()[46].toString() + sqlHandle.Gcode().toArray()[47].toString());
                editor.putString("主轴停止", sqlHandle.Gcode().toArray()[48].toString() + sqlHandle.Gcode().toArray()[49].toString());
                editor.putString("夹具夹紧", sqlHandle.Gcode().toArray()[50].toString() + sqlHandle.Gcode().toArray()[51].toString());
                editor.putString("夹具松开", sqlHandle.Gcode().toArray()[52].toString() + sqlHandle.Gcode().toArray()[53].toString());
                editor.putString("等待机械手信号", sqlHandle.Gcode().toArray()[54].toString() + sqlHandle.Gcode().toArray()[55].toString());
                editor.putString("执行换刀动作", sqlHandle.Gcode().toArray()[56].toString());
                editor.putString("定义插补切削速度", sqlHandle.Gcode().toArray()[58].toString());
                editor.putString("定义主轴转速", sqlHandle.Gcode().toArray()[60].toString());
                editor.putString("每分钟进给", sqlHandle.Gcode().toArray()[62].toString() + sqlHandle.Gcode().toArray()[63].toString());
                editor.putString("Biased_information", sqlHandle.lsxy(fileid).toString());
                editor.putString("ftp", sqlHandle.Ftp(fileid).toString());*/
                editor.commit();
                ImageView2.setText(str+'\n'+"刀具参数");
            }
        });
        builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              //  Toast.makeText(DataActivity.this,String.valueOf(which),0).show();
            }
        });
        builder.create().show();
    }


}

