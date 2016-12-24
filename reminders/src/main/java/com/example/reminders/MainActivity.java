package com.example.reminders;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.reminders_row, R.id.row_text, new String[]{
                "first record", "second record", "third record", "第四"});
        //第一个参数表示当前Activity的Context对象
    /*Adapter还需要知道使用哪个布局，以及使用布局中那些字段来显示行数据，要满足要求，
      则需要同时传入布局及其中的TextView的Id
     最后一个参数是对应列表中每个条目的字符串数组*/
        mListView.setAdapter(arrayAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       System.out.println("创建菜单");
        getMenuInflater().inflate(R.menu.menu,menu);
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
