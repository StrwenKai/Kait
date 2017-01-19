package com.example.reminders;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //实现备忘录的增删改除
//向ListView添加条目
    private ListView mListView;
    private ReminderDbAdapter mDbAdapter;
    private ReminderSimpleCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.reminders_list_view);
        mListView.setDivider(null);
        mDbAdapter = new ReminderDbAdapter(this);
        mDbAdapter.open();

        if (savedInstanceState == null) {
            insertSomeReminders();
        }

        Cursor cursor = mDbAdapter.fetchAllReminders();
        String[] from = new String[]{ReminderDbAdapter.COL_CONTENT};
        int[] to = new int[]{R.id.row_text};
        mCursorAdapter = new ReminderSimpleCursorAdapter(MainActivity.this, R.layout.reminders_row
                , cursor, from, to, 0);
        //现在游标适配器(controller)更新了db数据库(model)的数据到ListView的view上的数据即试图变了
        mListView.setAdapter(mCursorAdapter);
        //执行ＳＱＬ语句创建文本
        //添加示例
        insertSomeReminders();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, final int masterListpoistion, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);//修改日志容器
                ListView modeListView = new ListView(MainActivity.this);//用于填充列表项的ArrayAdapter
                String[] modes = new String[]{"编辑", "删除"};//执行的两个操作
                ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, android.R.id.text1, modes);
                //适配器的核心 ----编辑备忘和删除备忘
                //两个功能参数传给ListView之前，需要传给AlertDialog.Builder
                modeListView.setAdapter(modeAdapter);
                builder.setView(modeListView);//创建并显示带有选项表的对话框
                final Dialog dialog = builder.create();
                dialog.show();
                modeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //编辑备忘
                        if (position == 0) {
                            Toast.makeText(MainActivity.this, "编辑" + masterListpoistion, Toast.LENGTH_SHORT).show();
                        }//删除备忘录
                        else {
                            Toast.makeText(MainActivity.this, "删除" + masterListpoistion, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();//将对话框彻底删除
                    }
                });
            }

        });
    }

    private void insertSomeReminders() {
        mDbAdapter.deleteAllReminders();
        mDbAdapter.createReminder("示例1", true);
        mDbAdapter.createReminder("示例2", false);
        mDbAdapter.createReminder("示例3", false);
        mDbAdapter.createReminder("示例4", true);
        mDbAdapter.createReminder("示例5", false);
        mDbAdapter.createReminder("示例6", false);
        mDbAdapter.createReminder("示例7", true);
        mDbAdapter.createReminder("示例8", false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("创建菜单");
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                Log.d(getLocalClassName(), "creat new Reminders");
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return false;
        }

    }
}
