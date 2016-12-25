package com.example.reminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2016/12/24.
 */
class ReminderDbAdapter {


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);//执行一个SQL语句
            //显示创建数据库的时候错误
            /*原来是DATABASE_CREATE创建数据库的语句错误了写的时候粗心*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from  version" + oldVersion + "to" +
                    newVersion + ",which will destroy all old  data");
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);

            onCreate(db);
        }
    }
       //列名
        public static final String COL_ID = "_id";
        public static final String COL_CONTENT = "content";
        public static final String COL_IMPORTANT = "important";
       //索引
        public static final int INDEX_ID = 0;
        public static final int INDEX_CONTENT = INDEX_ID + 1;
        public static final int INDEX_IMPORTANT = INDEX_ID + 2;
        //用于日志
        private static final String TAG = "RemindersDbAdapter（log)";

        private DatabaseHelper mDbHelper;
        private SQLiteDatabase mDb;

        private static final String DATABASE_NAME = "dba_remder";//数据库名
        private static final String TABLE_NAME = "tbl_remdrs";//主表名
        private static final int DATABASE_VERSION = 1;//版本号

        private final Context mContext;

        private static final String DATABASE_CREATE =
                "CREATE TABLE if not exists " + TABLE_NAME +
                " ( " + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_CONTENT + " TEXT, " +
                        COL_IMPORTANT + " INTEGER );";

        /*
        * 如何使用DatabaseHelper  来打开关闭数据库
        * 构造函数保存了一个Context实例
        * close()方法使用助手类来关闭数据库
        * */
        public ReminderDbAdapter(Context context) {
            this.mContext = context;

        }

        //open 初始化助手类并用来获取数据库的实例
        public void open() throws SQLException {
            mDbHelper = new DatabaseHelper(mContext);
            mDb = mDbHelper.getWritableDatabase();
        }

        //close用来关闭数据库
        public void close() {
            if (mDbHelper != null) {
                mDbHelper.close();
            }
        }

        /*包含在tbl_remdrs表中处理Reminder对象创建、读取、更新、和删除操作的所有逻辑
        这些叫做GRUD（创建、读取、更新、和删除）操作
        *   ContentValues是一个数据梭，用于将数据值传递给数据库的insert方法
        *   cursor-》光标
        * */
        public void createReminder(String name, boolean important) {
            ContentValues values = new ContentValues();
            values.put(COL_CONTENT, name);
            values.put(COL_IMPORTANT, important ? 1 : 0);
            mDb.insert(TABLE_NAME, null, values);
        }

        public long createReminder(Reminder reminder) {
            ContentValues values = new ContentValues();
            values.put(COL_CONTENT, reminder.getContent());//连接名字
            values.put(COL_IMPORTANT, reminder.getImportant());

            return mDb.insert(TABLE_NAME, null, values);//插入行
        }

        //读取
        public Reminder fetchReminderById(int id) {
            Cursor cursor = mDb.query(TABLE_NAME, new String[]{
                    COL_ID, COL_CONTENT, COL_IMPORTANT},
                    COL_ID + "=?",
                    new String[]{String.valueOf(id)},
                    null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

            }else {
                cursor.close();
            }
            return new Reminder(
                    cursor.getInt(INDEX_ID),
                    cursor.getString(INDEX_CONTENT),
                    cursor.getInt(INDEX_IMPORTANT)
            );
        }

        public Cursor fetchAllReminders() {
            Cursor mCursor = mDb.query(TABLE_NAME,new String[]{COL_ID,
                    COL_CONTENT, COL_IMPORTANT},
                    null, null, null, null,null
            );
            if (mCursor != null) {
                mCursor.moveToFirst();
            }
            return mCursor;
        }

        //更新
        public void updateReminder(Reminder reminder) {
            ContentValues values = new ContentValues();
            values.put(COL_CONTENT, reminder.getContent());
            values.put(COL_IMPORTANT, reminder.getImportant());
            mDb.update(TABLE_NAME, values,
                    COL_ID + "=?", new String[]{String.valueOf(reminder.getmId())});
        }
        //删除

        public void deleteReminderById(int nId) {
            mDb.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(nId)});
        }

        public void deleteAllReminders() {
            mDb.delete(TABLE_NAME, null, null);

        }

}