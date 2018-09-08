package cn.qzd.machinetool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import cn.qzd.machinetool.helper.ConnectionHelper;
import cn.qzd.machinetool.helper.SQLHandle;

/**
 * 注册界面
 * Created by admin
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText activityRegisterUsername,activityRegisterPassword,activityRegisterPhone,activityRegisterName,activityRegisterAdress;
    private Button activityRegisterRegister;
    private TextView activityRegisterBack;
    private SQLHandle sqlHandle;
    private int flag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        init();

    }

    public void init() {
        activityRegisterAdress = (EditText) findViewById(R.id.activity_register_address);
        activityRegisterBack = (TextView) findViewById(R.id.activity_register_back);
        activityRegisterName = (EditText) findViewById(R.id.activity_register_name);
        activityRegisterPassword = (EditText) findViewById(R.id.activity_register_password);
        activityRegisterPhone = (EditText) findViewById(R.id.activity_register_phone);
        activityRegisterUsername = (EditText) findViewById(R.id.activity_register_username);
        activityRegisterRegister = (Button) findViewById(R.id.activity_register_register);
        activityRegisterRegister.setOnClickListener(this);
        activityRegisterBack.setOnClickListener(this);
        sqlHandle=new SQLHandle();
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_register_back:
                Intent intent = new Intent(RegisterActivity.this, Loginactivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.activity_register_register:
                flag = 0;
                String name = activityRegisterUsername.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(this, "ID can not be empty", Toast.LENGTH_SHORT).show();
                } else if (name.length() < 6 || name.length() > 16) {
                    Toast.makeText(this, "ID length should in 6~16", Toast.LENGTH_SHORT).show();
                } else if (sqlHandle.loginname(name)){
                    Toast.makeText(this, "ID already existed", Toast.LENGTH_SHORT).show();
                }
                else {
                    flag = flag + 1;
                }
                String password = activityRegisterPassword.getText().toString();
                if (TextUtils.isEmpty(password) && flag >= 1) {
                    Toast.makeText(this, "password can not be empty", Toast.LENGTH_SHORT).show();
                } else if (flag >= 1 && password.length() < 6 || password.length() > 16) {
                    Toast.makeText(this, "password length should in 6~16", Toast.LENGTH_SHORT).show();
                } else {
                    flag = flag + 1;
                }
                if (flag >= 2) {
                    String phone = activityRegisterPhone.getText().toString();
                    String adress=activityRegisterAdress.getText().toString();
                    String Rname=activityRegisterName.getText().toString();
                    if ( phone.length() < 11) {
                        Toast.makeText(this, "Inlegal phone number", Toast.LENGTH_SHORT).show();
                    } else if (Rname.isEmpty()){
                        Toast.makeText(this, "Inlegal Your Name", Toast.LENGTH_SHORT).show();
                    }else if (adress.isEmpty()){
                        Toast.makeText(this, "Inlegal Your Adress", Toast.LENGTH_SHORT).show();
                    } else {
                            sqlHandle.register(name,password,adress,Rname,phone);
                            Toast.makeText(this, "Welcome to join us", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(RegisterActivity.this, Loginactivity.class);
                            startActivity(intent1);
                            finish();
                        }/*else {
                            Toast.makeText(this, "连接数据库失败,请查看网络是否连通", Toast.LENGTH_SHORT).show();
                        }
                    }*/
                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(RegisterActivity.this, Loginactivity.class);
        startActivity(intent);
        finish();
    }
}
