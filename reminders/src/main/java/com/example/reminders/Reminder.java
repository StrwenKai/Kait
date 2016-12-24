package com.example.reminders;

/**
 * Created by Administrator on 2016/12/24.
 */

public class Reminder {
    private int mId;
    private String mContent;
    private int mImportant;

    public Reminder( int mId,String content, int important) {
        mContent = content;
        this.mId = mId;
        mImportant = important;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getImportant() {
        return mImportant;
    }

    public void setImportent(int important) {
        mImportant = important;
    }
}
