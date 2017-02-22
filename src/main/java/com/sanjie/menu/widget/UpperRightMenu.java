package com.sanjie.menu.widget;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.sanjie.menu.R;
import com.sanjie.menu.adapter.MenuAdapter;
import com.sanjie.menu.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * The drop-down menu appears in the upper right corner of the screen
 * Follow-up will continue to update
 *
 * Welcome to make valuable comments
 * Email:sanjie_lang@163.com
 * Created by LangSanJie on 2017/2/22.
 */

public class UpperRightMenu {

    private Activity mContext;
    private PopupWindow mPopupWindow;
    private RecyclerView mRecyclerView;
    private View content;

    private MenuAdapter menuAdapter;
    private List<MenuItem> menuItemList;

    private static final int DEFAULT_HEIGHT = RecyclerView.LayoutParams.WRAP_CONTENT;
    private int mHeight = DEFAULT_HEIGHT;
    private int mWidth = DEFAULT_HEIGHT;
    private boolean showIcon = true;
    private boolean darkBackground = true;
    private boolean needAnimation = true;

    private static final int DEFAULT_ANIM_STYLE = R.style.AnimStyle;
    private int animationStyle = DEFAULT_ANIM_STYLE;

    private float alpha = 0.75f;

    public UpperRightMenu(Activity activity) {
        this.mContext = activity;
        init();
    }

    /**
     * Initialize the control
     */
    private void init() {
        content = LayoutInflater.from(mContext).inflate(R.layout.popup_menu, null);
        mRecyclerView = (RecyclerView) content.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        menuItemList = new ArrayList<>();
        menuAdapter = new MenuAdapter(mContext, this, menuItemList, showIcon);
    }

    /**
     * Based on PopupWindow
     * @return
     */
    private PopupWindow getPopupWindow() {
        mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setContentView(content);
        mPopupWindow.setHeight(mHeight);
        mPopupWindow.setWidth(mWidth);
        if (needAnimation) {
            mPopupWindow.setAnimationStyle(animationStyle <= 0 ? DEFAULT_ANIM_STYLE : animationStyle);
        }
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (darkBackground) {
                    setBackgroundAlpha(alpha, 1f, 300L);
                }
            }
        });

        menuAdapter.setData(menuItemList);
        menuAdapter.setShowIcon(showIcon);
        mRecyclerView.setAdapter(menuAdapter);

        return mPopupWindow;
    }

    /**
     * customize height
     * @param height
     * @return
     */
    public UpperRightMenu setHeight(int height) {
        if (height <= 0 && height != RecyclerView.LayoutParams.MATCH_PARENT && height != RecyclerView.LayoutParams.WRAP_CONTENT) {
            this.mHeight = DEFAULT_HEIGHT;
        } else {
            this.mHeight = height;
        }
        return this;
    }

    /**
     * customize width
     * @param width
     * @return
     */
    public UpperRightMenu setWidth(int width) {
        if (width <= 0 && width != RecyclerView.LayoutParams.MATCH_PARENT && width != RecyclerView.LayoutParams.WRAP_CONTENT) {
            this.mWidth = DEFAULT_HEIGHT;
        } else {
            this.mWidth = width;
        }
        return this;
    }

    /**
     * Whether to display the icon
     * @param show
     * @return
     */
    public UpperRightMenu setShowIcon(boolean show) {
        this.showIcon = show;
        return this;
    }

    /**
     * Add an option
     * @param menuItem
     * @return
     */
    public UpperRightMenu addMenuItem(MenuItem menuItem) {
        menuItemList.add(menuItem);
        return this;
    }

    /**
     * Add an option and add position
     * @param menuItem
     * @param position
     * @return
     */
    public UpperRightMenu addMenuItem(MenuItem menuItem, int position) {
        menuItemList.add(position, menuItem);
        return this;
    }

    /**
     * Add an option collection
     * @param list
     * @return
     */
    public UpperRightMenu addMenuList(List<MenuItem> list) {
        menuItemList.addAll(list);
        return this;
    }

    /**
     * Add an option collection and add position
     * @param list
     * @param position
     * @return
     */
    public UpperRightMenu addMenuList(List<MenuItem> list, int position) {
        menuItemList.addAll(position, list);
        return this;
    }

    /**
     * Set the mask background
     * @param darkBackground
     * @return
     */
    public UpperRightMenu darkBackground(boolean darkBackground) {
        this.darkBackground = darkBackground;
        return this;
    }

    /**
     * Do you need the Animation Style
     * @param needAnimation
     * @return
     */
    public UpperRightMenu needAnimationStyle(boolean needAnimation) {
        this.needAnimation = needAnimation;
        return this;
    }

    /**
     * Set the Animation Style
     * @param styleResId
     * @return
     */
    public UpperRightMenu setAnimationStyle(int styleResId) {
        this.needAnimation = true;
        this.animationStyle = styleResId;
        return this;
    }

    /**
     * Set up listening events
     * @param listener
     * @return
     */
    public UpperRightMenu setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        menuAdapter.setOnMenuItemClickListener(listener);
        return this;
    }

    /**
     * show
     * @param anchor
     * @return
     */
    public UpperRightMenu showAsDropDown(View anchor) {
        showAsDropDown(anchor, 0, 0);
        return this;
    }

    public UpperRightMenu showAsDropDown(View anchor, int xOff, int yOff) {
        if (mPopupWindow == null) {
            getPopupWindow();
        }
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAsDropDown(anchor, xOff, yOff);
            if(darkBackground){
                setBackgroundAlpha(1f, alpha, 240);
            }
        }
        return this;
    }

    private void setBackgroundAlpha(float from, float to, long duration) {
        final WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = (float) animation.getAnimatedValue();
                mContext.getWindow().setAttributes(lp);
            }
        });
        animator.start();
    }

    /**
     * hide
     */
    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClick(int position);
    }
}
