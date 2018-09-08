package cn.qzd.machinetool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.qzd.machinetool.LitepalBase.QM_MT_SunMaoInfo;
import cn.qzd.machinetool.helper.SortAdapter;
import cn.qzd.machinetool.util.PreferencesUtils;
import cn.qzd.machinetool.view.ContactSortModel;
import cn.qzd.machinetool.view.EditTextWithDel;
import cn.qzd.machinetool.view.PinyinComparator;
import cn.qzd.machinetool.view.PinyinUtils;
import cn.qzd.machinetool.view.SideBar;

/**
 * Created by mb on 2018/7/21.
 */

public class JointList extends AppCompatActivity {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;//, mTvTitle;
    private SortAdapter adapter;
    private EditTextWithDel mEtSearchName;
    private List<ContactSortModel> SourceDateList;
    private List<String> list2;
    private String[] alljointname;
    private int machinetype;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joint_activity);
        list2=new ArrayList<String>();
        bundle = getIntent().getExtras();
        machinetype = bundle.getInt("Machinetype");
        PreferencesUtils.PREFERENCE_NAME="MachineType";
        PreferencesUtils.putInt(JointList.this,"machinetype",machinetype);
        //从本地数据库获取数据
        List<QM_MT_SunMaoInfo> list1 = LitePal.select("SunMaoName")
                .where("(ParentID = 0 or ParentID = infoID) and IsUse= 1 and machinetype = ?",String.valueOf(machinetype)).find(QM_MT_SunMaoInfo.class);
        for (QM_MT_SunMaoInfo qm_mt_sunMaoInfo : list1) {
            list2.add(qm_mt_sunMaoInfo.getSunMaoName());
        }
        alljointname=new String[list2.size()];
        list2.toArray(alljointname);
        /**************************************************/
        initViews();
    }

    private void initViews() {
        mEtSearchName = (EditTextWithDel) findViewById(R.id.et_search);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        //mTvTitle = (TextView) findViewById(R.id.tv_title);
        sortListView = (ListView) findViewById(R.id.lv_contact);
        initDatas();
        initEvents();
        setAdapter();
    }

    private void setAdapter() {
        //SourceDateList = filledData(getResources().getStringArray(R.array.contacts));
        SourceDateList = filledData(alljointname);
        Collections.sort(SourceDateList, new PinyinComparator());
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);
    }

    private void initEvents() {
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position );//
                }
            }
        });

        //ListView的点击事件
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /*mTvTitle.setText(((ContactSortModel) adapter.getItem(position )).getName());//
                Toast.makeText(getApplication(), ((ContactSortModel) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();*/
                //以及传递值往下一个页面显示不同的图片以及名称即ZsIfm
               /* String dddd = list2.get(position).toString();
                Toast.makeText(JointList.this, dddd, 0).show();*/
                String dddd=((ContactSortModel) adapter.getItem(position )).getName();

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
                        .where("ParentID=?", String.valueOf(infoid))
                        .find(QM_MT_SunMaoInfo.class);
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

                Intent intent = new Intent(JointList.this, ZsIfm.class);
                intent.putExtra("childname_name", list4.toString());//子榫卯名称
                intent.putExtra("childname_no", list5.toString());//子榫卯的代号
                intent.putExtra("fathername", dddd);//父榫卯的名称
                startActivity(intent);
            }
        });


        //根据输入框输入值的改变来过滤搜索
        mEtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initDatas() {
        sideBar.setTextView(dialog);
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<ContactSortModel> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = SourceDateList;
        } else {
            mSortList.clear();
            for (ContactSortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
    }

    private List<ContactSortModel> filledData(String[] date) {
        List<ContactSortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();

        for (int i = 0; i < date.length; i++) {
            ContactSortModel sortModel = new ContactSortModel();
            sortModel.setName(date[i]);
            String pinyin = PinyinUtils.getPingYin(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;
    }
}
