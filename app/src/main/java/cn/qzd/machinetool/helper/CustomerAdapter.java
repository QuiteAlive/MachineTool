package cn.qzd.machinetool.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import cn.qzd.machinetool.Fragment.DataFragment;
import cn.qzd.machinetool.R;


/**
 * Created by liuweiqiang on 2016/9/7.
 */
public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {
    private boolean flag=false;
    public Context mContext;
    public String[] mInfos;
    public String[] mHints;

    public interface SaveEditListener{

         void SaveEdit(int position, String string);
    }

    public CustomerAdapter(String[] infos, String[] hints, Context mContext) {
        this.mInfos=infos;
        this.mHints=hints;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_customer, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {

        holder.c_name.setText(mInfos[position]);
        holder.c_name_et.setText(mHints[position]);
        flag=true;
        //添加editText的监听事件
        holder.c_name_et.addTextChangedListener(new TextSwitcher(holder));
        //通过设置tag，防止position紊乱
        holder.c_name_et.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mInfos.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView c_name;//客户名称
        EditText c_name_et;//填写项

        public MyViewHolder(View view)
        {
            super(view);
            c_name = (TextView) view.findViewById(R.id.c_name);
            c_name_et= (EditText) view.findViewById(R.id.c_name_et);
        }
    }

    //自定义EditText的监听类
    class TextSwitcher implements TextWatcher {

        private MyViewHolder mHolder;

        public TextSwitcher(MyViewHolder mHolder) {
            this.mHolder = mHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (flag) {
                //用户输入完毕后，处理输入数据，回调给主界面处理
                SaveEditListener listener = (SaveEditListener) mContext;

                if (s != null) {
                    listener.SaveEdit(Integer.parseInt(mHolder.c_name_et.getTag().toString()), s.toString());
                }
            }
        }
    }

}
