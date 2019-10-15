package com.cretin.www.expandabletextview;

import android.content.Context;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cretin.www.expandabletextview.model.ViewModel;
import com.cretin.www.expandabletextview.model.ViewModelWithFlag;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

public class ShowInRecyclerViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    private List list;

    private FloatingActionButton floatingActionButton;

    //是需要保存展开或收回状态
    private boolean flag = true;
    private String content;

    private String yourText = "我所认识的中国，强大、友好我所认识的中国，强大、友好。@奥特曼 “一带一路”经济带带动了沿线国家的经济发展，促进我国与他国的友好往来和贸易发展，可谓“双赢”。http://www.baidu.com 自古以来，中国以和平、友好的面孔示人。汉武帝派张骞出使西域，开辟丝绸之路，增进与西域各国的友好往来。http://www.baidu.com 胡麻、胡豆、香料等食材也随之传入中国，汇集于中华美食。@RNG 漠漠古道，驼铃阵阵，这条路奠定了“一带一路”的基础，让世界认识了中国。";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_in_recycler_view);

        recyclerView = findViewById(R.id.recyclerview);
        floatingActionButton = findViewById(R.id.float_btn);

        list = new ArrayList<>();
        changeStateAndSetData(null,false);

        floatingActionButton.setOnClickListener(v -> changeStateAndSetData(content,true));
    }

    /**
     * 切换状态
     */
    private void changeStateAndSetData(String content, boolean change) {
        if (change)
            flag = !flag;
        list.clear();
        if (flag) {
            Toast.makeText(this, "保留之前的展开或收回状态", Toast.LENGTH_SHORT).show();
            floatingActionButton.setImageResource(R.mipmap.green);
            for (int i = 0; i < 50; i++) {
                if (TextUtils.isEmpty(content))
                    list.add(new ViewModelWithFlag("第" + (i + 1) + "条数据：" + yourText));
                else
                    list.add(new ViewModelWithFlag("第" + (i + 1) + "条数据：" + content));
            }
        } else {
            Toast.makeText(this, "不保留之前的展开或收回状态", Toast.LENGTH_SHORT).show();
            floatingActionButton.setImageResource(R.mipmap.gray);
            for (int i = 0; i < 50; i++) {
                if (TextUtils.isEmpty(content))
                    list.add(new ViewModel("第" + (i + 1) + "条数据：" + yourText));
                else
                    list.add(new ViewModel("第" + (i + 1) + "条数据：" + content));
            }
        }
        adapter = new MyRecyclerViewAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.RecyclerHolder> {
        private Context mContext;
        private List dataList;

        public MyRecyclerViewAdapter(Context context, List list) {
            this.mContext = context;
            this.dataList = list;
        }

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_text, parent, false);
            return new RecyclerHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            if (flag) {
                //注意：保留状态需要在设置内容之前调用bind方法
                holder.textView.bind((ViewModelWithFlag) list.get(position));
                holder.textView.setContent(((ViewModelWithFlag) list.get(position)).getTitle());
            } else {
                holder.textView.setContent(((ViewModel) list.get(position)).getTitle());
            }
            holder.textView.setLinkClickListener((type, content, selfContent) -> {
                switch (type) {
                    case LINK_TYPE:
                        Toast.makeText(mContext, "点击链接：" + content, Toast.LENGTH_SHORT).show();
                        break;
                    case MENTION_TYPE:
                        Toast.makeText(mContext, "点击用户：" + content, Toast.LENGTH_SHORT).show();
                        break;
                    case SELF:
                        Toast.makeText(mContext, "你点击了自定义规则 内容是：" + content + " " + selfContent, Toast.LENGTH_SHORT).show();
                        break;
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder {
            private ExpandableTextView textView;

            private RecyclerHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tv_item);
                itemView.setOnClickListener(v -> Toast.makeText(
                        ShowInRecyclerViewActivity.this, "您点击了第" + getLayoutPosition() + "个Item", Toast.LENGTH_SHORT).show());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);
        new AlertDialog.Builder(this)
                .setMessage("请输入将要替换的内容:")
                .setTitle("提示")
                .setView(view)
                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("确定", (dialog, which) -> {
                    EditText editText = view.findViewById(R.id.ed_content);
                    String string = editText.getText().toString();
                    if (TextUtils.isEmpty(string)) {
                        Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    content = string;
                    changeStateAndSetData(string,false);
                    dialog.dismiss();
                })
                .show();
        return super.onOptionsItemSelected(item);
    }
}
