package com.example.reminders;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        mListView.setDivider(null);
        mDbAdapter=new ReminderDbAdapter(this);
        mDbAdapter.open();


        Cursor cursor=mDbAdapter.fetchAllReminders();

        String[] from=new String[]{ReminderDbAdapter.COL_CONTENT};

        int[] to=new int[]{R.id.row_text};

        mCursorAdapter=new ReminderSimpleCursorAdapter(MainActivity.this,R.layout.reminders_row
        ,cursor,from,to,0);
      //现在游标适配器(controller)更新了db数据库(model)的数据到ListView的view上的数据即试图变了

        mListView.setAdapter(mCursorAdapter);
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
