package cn.qzd.machinetool;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import org.litepal.LitePal;

import cn.qzd.machinetool.LitepalBase.QM_MT_ABaxisVal;
import cn.qzd.machinetool.LitepalBase.QM_MT_A_User;
import cn.qzd.machinetool.LitepalBase.QM_MT_A_User_Role_Auth;
import cn.qzd.machinetool.LitepalBase.QM_MT_KnifePoint;
import cn.qzd.machinetool.LitepalBase.QM_MT_Knifeproperty_path_R;
import cn.qzd.machinetool.LitepalBase.QM_MT_MachineFile1;
import cn.qzd.machinetool.LitepalBase.QM_MT_MachineFileTopBotton;
import cn.qzd.machinetool.LitepalBase.QM_MT_SaveFtp;
import cn.qzd.machinetool.LitepalBase.QM_MT_SunMaoInfo;
import cn.qzd.machinetool.LitepalBase.QM_MT_SunMaoPathParameterVal;
import cn.qzd.machinetool.LitepalBase.QM_MT_SunMao_Path_R;
import cn.qzd.machinetool.LitepalBase.QM_MT_SunMao_Porperty_R;
import cn.qzd.machinetool.LitepalBase.QM_MT_UserLatheCode;
import cn.qzd.machinetool.helper.Loading_view;
import cn.qzd.machinetool.helper.SQLHandle;

import java.util.ArrayList;

import java.util.List;


/**
 * 登录界面
 * Created by admin on 2018/5/17.
 */

public class Loginactivity extends AppCompatActivity {
    private Button login,button2;
    private EditText username, password1;
    private CheckBox cbsave;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SQLHandle sqlHandle;
    private Loading_view loading;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.login_layout);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("提示信息");
        progressDialog.setMessage("正在下载中，请稍后....");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        sqlHandle = new SQLHandle();

        pref = PreferenceManager.getDefaultSharedPreferences(this);//利用SharedPreferences存储实现记住密码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        login = (Button) findViewById(R.id.bt_login);
        username = (EditText) findViewById(R.id.ed_name);
        password1 = (EditText) findViewById(R.id.ed_password);
        cbsave = (CheckBox) findViewById(R.id.cb_save);
        final String us = username.getText().toString();
        final String pw = password1.getText().toString();
        pref = getSharedPreferences("user", Context.MODE_PRIVATE);
        boolean isRemenber = pref.getBoolean("remember_password", false);
        if (isRemenber) {
            //将账号和密码都设置到文本中
            String account = pref.getString("account", "");//将正确输入的存储信息取出
            String password = pref.getString("password", "");
            username.setText(account);
            password1.setText(password);
            cbsave.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean verify = false;
                final String us = username.getText().toString();
                final String pw = password1.getText().toString();
                List<QM_MT_A_User> list1 = LitePal.select("*")
                        .where("PassWord = ? and LoginName =?", pw, us).find(QM_MT_A_User.class);
                for (QM_MT_A_User qm_mt_a_user : list1) {
                    verify = true;
                }
                //  if (sqlHandle.login(us, pw) ==1) {
                if (verify) {
                    editor = pref.edit();
                    if (cbsave.isChecked()) {
                        pref = getSharedPreferences("user", Context.MODE_PRIVATE);
                        editor = pref.edit();
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", us);//点击按钮将存储
                        editor.putString("password", pw);
                        editor.commit();
                    } else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(Loginactivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } /*else if(sqlHandle.login(us, pw) ==2){
                    Toast.makeText(Loginactivity.this, "连接数据库失败,请查看网络是否连通", Toast.LENGTH_SHORT).show();
                    }*/ else {
                    Toast.makeText(Loginactivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                }

            }
        });
        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new  MyAsyncTask().execute();
            }
        });
    }

    public void register(View view) {
        Intent intent = new Intent(Loginactivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * 定义一个类，让其继承AsyncTask这个类
     * Params: String类型，表示传递给异步任务的参数类型是String，通常指定的是URL路径
     * Progress: Integer类型，进度条的单位通常都是Integer类型
     * Result：byte[]类型，表示我们下载好的图片以字节数组返回
     * @author xiaoluo
     *
     */
    public class MyAsyncTask extends AsyncTask<String, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //    在onPreExecute()中我们让ProgressDialog显示出来
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                int flag = 0;
                if (sqlHandle.judge() == 1) {
                    LitePal.deleteAll(QM_MT_SunMao_Path_R.class, "id>?", "0");
                    LitePal.deleteAll(QM_MT_ABaxisVal.class, "id>?", "0");
                    LitePal.deleteAll(QM_MT_SunMaoInfo.class, "id>?", "0");
                    LitePal.deleteAll(QM_MT_A_User.class, "id>?", "0");
                    LitePal.deleteAll(QM_MT_SunMao_Porperty_R.class, "id>?", "0");
                    LitePal.deleteAll(QM_MT_SunMaoPathParameterVal.class, "id>?", "0");
                    LitePal.deleteAll(QM_MT_MachineFile1.class, "id>?", "0");
                    LitePal.deleteAll(QM_MT_MachineFileTopBotton.class, "id>?", "0");
                    LitePal.deleteAll(QM_MT_UserLatheCode.class, "id>?", "0");
                    LitePal.deleteAll(QM_MT_SaveFtp.class, "id>?", "0");
                    LitePal.deleteAll(QM_MT_KnifePoint.class, "id>?", "0");
                    LitePal.deleteAll(QM_MT_Knifeproperty_path_R.class, "id>?", "0");
                    LitePal.deleteAll(QM_MT_A_User_Role_Auth.class, "id>?", "0");
                    QM_MT_SunMaoInfo info = new QM_MT_SunMaoInfo();
                    ArrayList allinfo = new ArrayList();
                    allinfo = sqlHandle.sunmaoinfo();
                    for (int i = 0; i < allinfo.size() - 6; i++) {
                        info = new QM_MT_SunMaoInfo();
                        if (i % 7 == 0) {
                            info.setInfoID((Integer) allinfo.get(i));
                            info.setSunMaoNo((String) allinfo.get(i + 1));
                            info.setSunMaoName((String) allinfo.get(i + 2));
                            info.setParentID((Integer) allinfo.get(i + 3));
                            info.setsImage((byte[]) allinfo.get(i + 4));
                            info.setMachineType((Integer) allinfo.get(i + 5));
                            info.setIsUse((Integer) allinfo.get(i + 6));
                            info.save();
                        }
                    }
                    if (info.save()) {
                        flag++;
                        //Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                    }

                    QM_MT_SunMao_Path_R info1 = new QM_MT_SunMao_Path_R();
                    ArrayList pathall = new ArrayList();
                    pathall = sqlHandle.sunmaopath();
                    for (int i = 0; i < pathall.size() - 2; i++) {
                        info1 = new QM_MT_SunMao_Path_R();
                        if (i % 3 == 0) {
                            info1.setSunMaoNameNo((String) pathall.get(i));
                            info1.setPathName((String) pathall.get(i + 1));
                            info1.setPath_r_id((Integer) pathall.get(i + 2));
                            //info1.saveOrUpdate("id = ?",String.valueOf(i+1));
                            info1.save();
                        }
                    }
                    if (info1.save()) {
                        flag++;
                        // Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                    }
                    QM_MT_ABaxisVal info2 = new QM_MT_ABaxisVal();
                    ArrayList ABaxis = new ArrayList();
                    ABaxis = sqlHandle.ABaxisval();
                    for (int i = 0; i < ABaxis.size() - 14; i++) {
                        info2 = new QM_MT_ABaxisVal();
                        if (i % 15 == 0) {
                            info2.setSunMao_Path_ID((Integer) ABaxis.get(i));
                            info2.setISRad1Check((Integer) ABaxis.get(i + 1));
                            info2.setProfileFormula((String) ABaxis.get(i + 2));
                            info2.setProfileScope1((Double) ABaxis.get(i + 3));
                            info2.setProfileScope2((Double) ABaxis.get(i + 4));
                            info2.setISRad2Check((Integer) ABaxis.get(i + 5));
                            info2.setAaxleFormula((String) ABaxis.get(i + 6));
                            info2.setAaxleScope1((Double) ABaxis.get(i + 7));
                            info2.setAaxleScope2((Double) ABaxis.get(i + 8));
                            info2.setISRad3Check((Integer) ABaxis.get(i + 9));
                            info2.setBaxleFormula((String) ABaxis.get(i + 10));
                            info2.setBaxleScope1((Double) ABaxis.get(i + 11));
                            info2.setBaxleScope2((Double) ABaxis.get(i + 12));
                            info2.setStartDot((String) ABaxis.get(i + 13));
                            info2.setISClockWise((Integer) ABaxis.get(i + 14));
                            //info2.saveOrUpdate("id = ?",String.valueOf(i+1));
                            info2.save();
                        }
                    }
                    if (info2.save()) {
                        flag++;
                        //Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                    }
                    QM_MT_A_User qm_user = new QM_MT_A_User();
                    ArrayList qm_userall = new ArrayList();
                    qm_userall = sqlHandle.qm_user();
                    for (int i = 0; i < qm_userall.size() - 3; i++) {
                        qm_user = new QM_MT_A_User();
                        if (i % 4 == 0) {
                            qm_user.setPassWord((String) qm_userall.get(i));
                            qm_user.setLoginName((String) qm_userall.get(i + 1));
                            qm_user.setCompany((String) qm_userall.get(i + 2));
                            qm_user.setUserID((Integer) qm_userall.get(i + 3));
                            qm_user.save();
                        }
                    }
                    if (qm_user.save()) {
                        flag++;
                        // Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                    }
                    QM_MT_SunMaoPathParameterVal qm_paramet = new QM_MT_SunMaoPathParameterVal();
                    ArrayList qm_parametall = new ArrayList();
                    qm_parametall = sqlHandle.qm_path_property();
                    for (int i = 0; i < qm_parametall.size() - 2; i++) {
                        qm_paramet = new QM_MT_SunMaoPathParameterVal();
                        if (i % 3 == 0) {
                            qm_paramet.setProperty((String) qm_parametall.get(i));
                            qm_paramet.setSize((String) qm_parametall.get(i + 1));
                            qm_paramet.setSunMao_Path_ID((Integer) qm_parametall.get(i + 2));
                            qm_paramet.save();
                        }
                    }
                    if (qm_paramet.save()) {
                        flag++;
                        //Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                    }

                    QM_MT_SunMao_Porperty_R qmdata_paramet = new QM_MT_SunMao_Porperty_R();
                    ArrayList qmdata_parametall = new ArrayList();
                    qmdata_parametall = sqlHandle.qm_data_property();
                    for (int i = 0; i < qmdata_parametall.size() - 4; i++) {
                        qmdata_paramet = new QM_MT_SunMao_Porperty_R();
                        if (i % 5 == 0) {
                            qmdata_paramet.setSunMaoNo((String) qmdata_parametall.get(i));
                            qmdata_paramet.setSunMaoPorperty((String) qmdata_parametall.get(i + 1));
                            qmdata_paramet.setPorpertyNo((String) qmdata_parametall.get(i + 2));
                            qmdata_paramet.setPorpertyVal((String) qmdata_parametall.get(i + 3));
                            qmdata_paramet.setImage((byte[]) qmdata_parametall.get(i + 4));
                            qmdata_paramet.save();
                        }
                    }
                    if (qmdata_paramet.save()) {
                        flag++;
                        //Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                    }

                    QM_MT_MachineFile1 qm_mt_machineFile1 = new QM_MT_MachineFile1();
                    ArrayList qmdfile_name = new ArrayList();
                    qmdfile_name = sqlHandle.MachineFile();
                    for (int i = 0; i < qmdfile_name.size() - 4; i++) {
                        qm_mt_machineFile1 = new QM_MT_MachineFile1();
                        if (i % 5 == 0) {
                            qm_mt_machineFile1.setShaftId((Integer) qmdfile_name.get(i));
                            qm_mt_machineFile1.setFileName((String) qmdfile_name.get(i + 1));
                            qm_mt_machineFile1.setCompany((String) qmdfile_name.get(i + 2));
                            qm_mt_machineFile1.setMachinefileId((Integer) qmdfile_name.get(i + 3));
                            qm_mt_machineFile1.setShaftType((Integer) qmdfile_name.get(i + 4));
                            qm_mt_machineFile1.save();
                        }
                    }
                    if (qm_mt_machineFile1.save()) {
                        flag++;
                        //Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                    }

                    QM_MT_MachineFileTopBotton qm_mt_machineFileTopBotton = new QM_MT_MachineFileTopBotton();
                    ArrayList qmdtop_bottom = new ArrayList();
                    qmdtop_bottom = sqlHandle.TopandBottom();
                    for (int i = 0; i < qmdtop_bottom.size() - 3; i++) {
                        qm_mt_machineFileTopBotton = new QM_MT_MachineFileTopBotton();
                        if (i % 4 == 0) {
                            qm_mt_machineFileTopBotton.setFileTop((String) qmdtop_bottom.get(i));
                            qm_mt_machineFileTopBotton.setFileBottom((String) qmdtop_bottom.get(i + 1));
                            qm_mt_machineFileTopBotton.setFileId((Integer) qmdtop_bottom.get(i + 2));
                            qm_mt_machineFileTopBotton.setIsChecked((Integer) qmdtop_bottom.get(i + 3));
                            qm_mt_machineFileTopBotton.save();
                        }
                    }
                    if (qm_mt_machineFileTopBotton.save()) {
                        flag++;
                        //Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                    }
                    QM_MT_UserLatheCode qm_mt_latheCode = new QM_MT_UserLatheCode();
                    ArrayList qmlathecode = new ArrayList();
                    qmlathecode = sqlHandle.LatheCode();
                    for (int i = 0; i < qmlathecode.size() - 3; i++) {
                        qm_mt_latheCode = new QM_MT_UserLatheCode();
                        if (i % 4 == 0) {
                            qm_mt_latheCode.setFileId((String) qmlathecode.get(i));
                            qm_mt_latheCode.setCodeType((String) qmlathecode.get(i + 1));
                            qm_mt_latheCode.setNumber((String) qmlathecode.get(i + 2));
                            qm_mt_latheCode.setInstruction((String) qmlathecode.get(i + 3));
                            qm_mt_latheCode.save();
                        }
                    }
                    if (qm_mt_latheCode.save()) {
                        flag++;
                        //Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                    }
                    QM_MT_SaveFtp qm_mt_saveFtp = new QM_MT_SaveFtp();
                    ArrayList qmsaveftp = new ArrayList();
                    qmsaveftp = sqlHandle.SaveFtp();
                    for (int i = 0; i < qmsaveftp.size() - 2; i++) {
                        qm_mt_saveFtp = new QM_MT_SaveFtp();
                        if (i % 3 == 0) {
                            qm_mt_saveFtp.setFileId((Integer) qmsaveftp.get(i));
                            qm_mt_saveFtp.setIP((String) qmsaveftp.get(i + 1));
                            qm_mt_saveFtp.setPort((String) qmsaveftp.get(i + 2));
                            qm_mt_saveFtp.save();
                        }
                    }
                    if (qm_mt_saveFtp.save()) {
                        flag++;
                        //Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                    }
                    QM_MT_KnifePoint qm_mt_knifePoint = new QM_MT_KnifePoint();
                    ArrayList qmknifepoint = new ArrayList();
                    qmknifepoint = sqlHandle.KnifePoint();
                    for (int i = 0; i < qmknifepoint.size() - 7; i++) {
                        qm_mt_knifePoint = new QM_MT_KnifePoint();
                        if (i % 8 == 0) {
                            qm_mt_knifePoint.setFileId((Integer) qmknifepoint.get(i));
                            qm_mt_knifePoint.setKnifeName((String) qmknifepoint.get(i + 1));
                            qm_mt_knifePoint.setL((Double) qmknifepoint.get(i + 2));
                            qm_mt_knifePoint.setS((Double) qmknifepoint.get(i + 3));
                            qm_mt_knifePoint.setX((Double) qmknifepoint.get(i + 4));
                            qm_mt_knifePoint.setY((Double) qmknifepoint.get(i + 5));
                            qm_mt_knifePoint.setSafey((String) qmknifepoint.get(i + 6));
                            qm_mt_knifePoint.setSafez((String) qmknifepoint.get(i + 7));
                            qm_mt_knifePoint.save();
                        }
                    }
                    if (qm_mt_knifePoint.save()) {
                        flag++;
                        //Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                    }
                    QM_MT_Knifeproperty_path_R knifeproperty_path_r = new QM_MT_Knifeproperty_path_R();
                    ArrayList qmknifepath = new ArrayList();
                    qmknifepath = sqlHandle.KnifePath_r();
                    for (int i = 0; i < qmknifepath.size() - 3; i++) {
                        knifeproperty_path_r = new QM_MT_Knifeproperty_path_R();
                        if (i % 4 == 0) {
                            knifeproperty_path_r.setSunMao_Path_ID((Integer) qmknifepath.get(i));
                            knifeproperty_path_r.setKnifeName((String) qmknifepath.get(i + 1));
                            knifeproperty_path_r.setProperty((String) qmknifepath.get(i + 2));
                            knifeproperty_path_r.setSize((Double) qmknifepath.get(i + 3));
                            knifeproperty_path_r.save();
                        }
                    }
                    if (knifeproperty_path_r.save()) {
                        flag++;
                        //Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                    }

                    QM_MT_A_User_Role_Auth user_role_auth = new QM_MT_A_User_Role_Auth();
                    ArrayList qmrole_auth = new ArrayList();
                    qmrole_auth = sqlHandle.user_role();
                    for (int i = 0; i < qmrole_auth.size() - 1; i++) {
                        user_role_auth = new QM_MT_A_User_Role_Auth();
                        if (i % 2 == 0) {
                            user_role_auth.setUserID((Integer) qmrole_auth.get(i));
                            user_role_auth.setRoleId((Integer) qmrole_auth.get(i + 1));
                            user_role_auth.save();
                        }
                    }
                    if (user_role_auth.save()) {
                        flag++;
                        //Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败", Toast.LENGTH_SHORT).show();
                    }
                    if (flag == 13) {
                        Toast.makeText(Loginactivity.this, "存储成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Loginactivity.this, "存储失败，丢失" + (6 - flag) + "个数据表", Toast.LENGTH_SHORT).show();
                    }
                } else if (sqlHandle.judge() == 2) {
                    Toast.makeText(Loginactivity.this, "连接数据库失败,请查看网络是否连通", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(Void v)
        {
            super.onPostExecute(v);
            //    将doInBackground方法返回的byte[]解码成要给Bitmap
           // Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);
            //    更新我们的ImageView控件
           // image.setImageBitmap(bitmap);
            //    使ProgressDialog框消失
            progressDialog.dismiss();
        }
    }
}
