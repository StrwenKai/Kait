package com.example.reminders;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Administrator on 2016/12/24.
 * 需要一种从数据库获取备忘录加入到ListView中的方法
 */

public class ReminderSimpleCursorAdapter extends SimpleCursorAdapter {

    public ReminderSimpleCursorAdapter(Context context, int layout,
                                       Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.colImp=cursor.getColumnIndexOrThrow(toString());

            holder.colImp=cursor.getColumnIndexOrThrow(ReminderDbAdapter.COL_IMPORTANT);
            holder.listTab = view.findViewById(R.id.row_tab);
            view.setTag(holder);
        }
        if (cursor.getInt(holder.colImp) > 0) {
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.orange));

        } else {
            holder.listTab.setBackgroundColor(context.getResources().getColor(R.color.green));
        }

    }

    static class ViewHolder {
        int colImp;
        View listTab;
    }

}
