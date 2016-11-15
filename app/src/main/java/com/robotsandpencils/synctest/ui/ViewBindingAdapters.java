package com.robotsandpencils.synctest.ui;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * A place to keep the binding adapters that are used
 * within this project.
 * <p>
 * Created by nealsanche on 2015-12-16.
 */
public class ViewBindingAdapters {

    @BindingAdapter("isGone")
    public static void setIsGone(View view, boolean hide) {
        view.setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter("isInvisible")
    public static void setIsInvisible(View view, boolean hide) {
        view.setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
    }

    @BindingAdapter("leftMargin")
    public static void setLeftMargin(View view, double value) {
        float density = view.getResources().getDisplayMetrics().density;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layoutParams;
            params.leftMargin = (int) (value * density);
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutParams;
            params.leftMargin = (int) (value * density);
        }
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("bottomMargin")
    public static void setTheBottomMargin(View view, double value) {
        float density = view.getResources().getDisplayMetrics().density;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) layoutParams;
            params.bottomMargin = (int) (value * density);
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) layoutParams;
            params.bottomMargin = (int) (value * density);
        } else if (layoutParams instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layoutParams;
            params.bottomMargin = (int) (value * density);
        }

        view.setLayoutParams(layoutParams);
    }
}
