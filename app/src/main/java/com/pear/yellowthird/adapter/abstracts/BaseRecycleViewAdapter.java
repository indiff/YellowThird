package com.pear.yellowthird.adapter.abstracts;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的加强版list view的通用适配器
 */
public abstract class BaseRecycleViewAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected RecycleViewItemListener itemListener;
    protected List<T> datas = new ArrayList<T>();

    public List<T> getDatas() {
        if (datas==null)
            datas = new ArrayList<T>();
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public void setItemListener(RecycleViewItemListener listener){
        this.itemListener = listener;
    }

}
