package com.bigggfish.slidelistview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private SlideDeleteListView mListView;

    private List<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mListView = (SlideDeleteListView) findViewById(R.id.list);

        for (int i = 0; i < 20; i++) {

            datas.add("Position" + i);
        }

        final ListAdapter listAdapter = new ListAdapter(this, R.layout.list_item, datas);
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(this);
        listAdapter.setOnDeleteClickListener(new SlideDeleteListView.SlideBaseAdapter.OnDeleteClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(MainActivity.this, "DeleteClick" + position, Toast.LENGTH_SHORT).show();
                datas.remove(position);
                listAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast.makeText(this, "ItemClick" + position, Toast.LENGTH_SHORT).show();
    }

    class ListAdapter extends SlideDeleteListView.SlideBaseAdapter<ViewHolder>{

        private List<String> mDatas;

        public ListAdapter(Context context, @LayoutRes int itemLayoutRes, List<String> datas){
            this(context, itemLayoutRes);
            this.mDatas = datas;
        }

        public ListAdapter(Context context, @LayoutRes int itemLayoutRes) {
            super(context, itemLayoutRes);
        }

        @Override
        public ViewHolder getViewHolder(View itemView) {
            return new ViewHolder(itemView);
        }

        @Override
        public void convert(ViewHolder viewHolder, int position) {
            viewHolder.tvMessage.setText(mDatas.get(position));
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas == null ? null : mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    class ViewHolder extends SlideDeleteListView.ViewHolder{

        private TextView tvMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMessage = (TextView) itemView.findViewById(R.id.title);
        }
    }

}
