package com.sanjie.menu.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanjie.menu.R;
import com.sanjie.menu.model.MenuItem;
import com.sanjie.menu.widget.UpperRightMenu;

import java.util.List;

/**
 * Created by LangSanJie on 2017/2/22.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Context mContext;
    private List<MenuItem> menuItems;

    private UpperRightMenu rightMenu;
    private UpperRightMenu.OnMenuItemClickListener onMenuItemClickListener;

    private boolean showIcon;

    public MenuAdapter(Context mContext, UpperRightMenu rightMenu, List<MenuItem> menuItems, boolean showIcon) {
        this.mContext = mContext;
        this.menuItems = menuItems;
        this.rightMenu = rightMenu;
        this.showIcon = showIcon;
    }

    public void setData(List<MenuItem> data) {
        menuItems = data;
        notifyDataSetChanged();
    }

    public void setShowIcon(boolean showIcon) {
        this.showIcon = showIcon;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View container = LayoutInflater.from(mContext).inflate(R.layout.item_menu_list, parent, false);
        return new ViewHolder(container);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MenuItem menuItem = menuItems.get(position);
        if (showIcon){
            holder.icon.setVisibility(View.VISIBLE);
            int resId = menuItem.getIcon();
            holder.icon.setImageResource(resId < 0 ? 0 : resId);
        }else{
            holder.icon.setVisibility(View.GONE);
        }
        holder.text.setText(TextUtils.isEmpty(menuItem.getText()) ? "what ?" : menuItem.getText());

        if(position == 0){
            holder.container.setBackground(addStateDrawanle(-1,R.drawable.popup_top_pressed));
        }else if(position == menuItems.size() -1){
            holder.container.setBackground(addStateDrawanle(-1,R.drawable.popup_bottom_pressed));
        }else{
            holder.container.setBackground(addStateDrawanle(-1,R.drawable.popup_middle_pressed));
        }

        final int pos = holder.getAdapterPosition();
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onMenuItemClickListener != null){
                    rightMenu.dismiss();
                    onMenuItemClickListener.onMenuItemClick(pos);
                }
            }
        });
    }

    private StateListDrawable addStateDrawanle(int normalId, int pressedId){
        StateListDrawable sd = new StateListDrawable();
        Drawable normal = normalId == -1 ? null : mContext.getResources().getDrawable(normalId);
        Drawable pressed = pressedId == -1 ? null : mContext.getResources().getDrawable(pressedId);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{}, normal);
        return sd;
    }

    @Override
    public int getItemCount() {
        return menuItems == null ? 0 : menuItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewGroup container;
        ImageView icon;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            container = (ViewGroup) itemView;
            icon = (ImageView) itemView.findViewById(R.id.iv_menu_item_icon);
            text = (TextView) itemView.findViewById(R.id.tv_menu_item_text);
        }
    }

    public void setOnMenuItemClickListener(UpperRightMenu.OnMenuItemClickListener listener){
        this.onMenuItemClickListener = listener;
    }
}
