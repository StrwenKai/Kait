package com.example.reminders;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
                            int nId = getIdFromPostion(masterListpoistion);
                            Reminder reminder = mDbAdapter.fetchReminderById(nId);
                            fireCustomDialog(reminder);
                            //  Toast.makeText(MainActivity.this, "编辑" + masterListpoistion, Toast.LENGTH_SHORT).show();
                        }//删除备忘录
                        else {
                            //Toast.makeText(MainActivity.this, "删除" + masterListpoistion, Toast.LENGTH_SHORT).show();
                            mDbAdapter.deleteReminderById(getIdFromPostion(masterListpoistion));
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                        }
                        dialog.dismiss();//将对话框彻底删除
                    }
                });
            }

        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
              //  @TargetApi(Build.VERSION_CODES.KITKAT)
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.cam_menu, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_reminder:
                            for (int nC = mCursorAdapter.getCount() - 1; nC >= 0; nC--) {
                                if (mListView.isItemChecked(nC)) {
                                    mDbAdapter.deleteReminderById(getIdFromPostion(nC));

                                }
                            }
                            mode.finish();
                            mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                            return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                }
            });
        }



    }

    private int getIdFromPostion(int nC) {
        return (int) mCursorAdapter.getItemId(nC);

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


    private void fireCustomDialog(final Reminder reminder) {//用于插入和编辑
        //创建自定义对话框
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);

        TextView titleView = (TextView) dialog.findViewById(R.id.custom_title);
        final EditText editCustom = (EditText) findViewById(R.id.custom_edit_reminder);
        Button commitButton = (Button) dialog.findViewById(R.id.custom_button_commit);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.custom_check_box);
        LinearLayout rootLayout = (LinearLayout) dialog.findViewById(R.id.custom_root_layout);
        final boolean isEditOperation = (reminder != null);//?

        //这里是编辑
        if (isEditOperation) {
            //如果有备忘传入，那么该方法认为这是编辑操作并将该变量设置为true，否则false
            //如果fireCustomDialog是编辑操作，那么标题被设置为“编辑便签”操作  同时根据便签参数的值设置CheckBox
            //和EditText中的内容    还会把外层容器的布局背景设置为蓝色，目的是可视化地区分编辑对话框和插入对话框
            titleView.setText("编辑便签");
            checkBox.setChecked(reminder.getImportant() == 1);
            editCustom.setText(reminder.getContent());
            rootLayout.setBackgroundColor(getResources().getColor(R.color.blue));
        }

        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderText = editCustom.getText().toString();
                if (isEditOperation) {
                    Reminder reminderEdited = new Reminder(reminder.getmId(), reminderText, checkBox.isChecked() ? 1 : 0);
                    mDbAdapter.updateReminder(reminderEdited);//更新数据库
                    //这是为新建便签

                } else {
                    mDbAdapter.createReminder(reminderText, checkBox.isChecked());

                }
                mCursorAdapter.changeCursor(mDbAdapter.fetchAllReminders());
                dialog.dismiss();
            }
        });
        Button buttonCancel = (Button)dialog.findViewById(R.id.custom_button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //System.out.println("创建菜单");
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                fireCustomDialog(null);
                return true;
            case R.id.action_exit:
                finish();
                return true;
            default:
                return false;
        }

    }


}
