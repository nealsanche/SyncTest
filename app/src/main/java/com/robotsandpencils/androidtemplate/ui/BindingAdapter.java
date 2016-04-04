package com.robotsandpencils.androidtemplate.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Provides a base adapter for a recyclerview that handles some of the databinding updates.
 * <p>
 * Created by nealsanche on 15-09-28.
 */
public abstract class BindingAdapter<T extends ViewDataBinding> extends RecyclerView.Adapter<BindingViewHolder<T>> {

    private final int[] mLayoutResourceIds;

    public BindingAdapter(int layoutResourceId) {
        mLayoutResourceIds = new int[1];
        mLayoutResourceIds[0] = layoutResourceId;
    }

    public BindingAdapter(int[] resourceIds) {
        mLayoutResourceIds = resourceIds;
    }

    @Override
    public BindingViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        T binding = createBindingForViewType(parent, viewType);
        return new BindingViewHolder<>(binding);
    }

    protected T createBindingForViewType(ViewGroup parent, int viewType) {
        return DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), mLayoutResourceIds[viewType], parent, false);
    }

    @Override
    public void onBindViewHolder(BindingViewHolder<T> holder, int position) {
        T binding = holder.getBinding();
        updateBinding(binding, position);
    }

    protected abstract void updateBinding(T binding, int position);
}
