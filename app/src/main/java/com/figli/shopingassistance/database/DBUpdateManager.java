package com.figli.shopingassistance.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.figli.shopingassistance.model.ModelTask;

/**
 * Created by Figli on 22.02.2016.
 */
public class DBUpdateManager {

    SQLiteDatabase database;

    public DBUpdateManager(SQLiteDatabase database) {
        this.database = database;
    }

    public void title(long timeStamp, String title) {
        update(DBHelper.PRODUCT_TITLE_COLUMN_VIEW, timeStamp, title);
    }

    public void quantity(long timeStamp, String quantity) {
        update(DBHelper.PRODUCT_QUANTITY_VIEW, timeStamp, quantity);
    }

    public void date(long timeStamp, long date) {
        update(DBHelper.PRODUCT_DATE_COLUMN_VIEW, timeStamp, date);
    }

    public void priority(long timeStamp, int priority) {
        update(DBHelper.PRODUCT_PRIORITY_COLUMN_VIEW, timeStamp, priority);
    }

    public void status(long timeStamp, int status) {
        update(DBHelper.PRODUCT_STATUS_COLUMN_VIEW, timeStamp, status);
    }

    public void task(ModelTask task) {
        title(task.getTimeStamp(), task.getTitle());
        quantity(task.getTimeStamp(), task.getQuantity());
        date(task.getTimeStamp(), task.getDate());
        priority(task.getTimeStamp(), task.getPriority());
        status(task.getTimeStamp(), task.getStatus());
    }



    private void update(String column, long key, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column, value);
        database.update(DBHelper.PRODUCT_TABLE_VIEW, contentValues, DBHelper.PRODUCT_TIME_STAMP_COLUMN_VIEW + " = " + key, null);
    }

    private void update(String column, long key, long value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(column, value);
        database.update(DBHelper.PRODUCT_TABLE_VIEW, contentValues, DBHelper.PRODUCT_TIME_STAMP_COLUMN_VIEW + " = " + key, null);

    }

}
