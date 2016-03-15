package com.figli.shopingassistance.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.figli.shopingassistance.model.ModelTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Figli on 22.02.2016.
 */
public class DBQueryManager {

    private SQLiteDatabase database;

    public DBQueryManager(SQLiteDatabase database) {
        this.database = database;
    }

    public ModelTask getTask(long timeStamp) {
        ModelTask modelTask = null;
        Cursor cursor = database.query(DBHelper.PRODUCT_TABLE_VIEW, null, DBHelper.SELECTION_TIME_STAMP_VIEW,
                new String[]{Long.toString(timeStamp)}, null, null, null);

        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(DBHelper.PRODUCT_TITLE_COLUMN_VIEW));
            String quantity = cursor.getString(cursor.getColumnIndex(DBHelper.PRODUCT_QUANTITY_VIEW));
            long date = cursor.getLong(cursor.getColumnIndex(DBHelper.PRODUCT_DATE_COLUMN_VIEW));
            int priority = cursor.getInt(cursor.getColumnIndex(DBHelper.PRODUCT_PRIORITY_COLUMN_VIEW));
            int status = cursor.getInt(cursor.getColumnIndex(DBHelper.PRODUCT_STATUS_COLUMN_VIEW));
            String description = cursor.getString(cursor.getColumnIndex(DBHelper.PRODUCT_DESCRIPTION_VIEW));

            modelTask = new ModelTask(title, date, priority, status, timeStamp, quantity, description);
        }
        cursor.close();
        return modelTask;
    }

    public List<ModelTask> getTasksView(String selection, String[] selectionArgs, String orderBy) {
        List<ModelTask> tasks = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.PRODUCT_TABLE_VIEW, null, selection, selectionArgs, null, null, orderBy);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(DBHelper.PRODUCT_TITLE_COLUMN_VIEW));
                String quantity = cursor.getString(cursor.getColumnIndex(DBHelper.PRODUCT_QUANTITY_VIEW));
                long date = cursor.getLong(cursor.getColumnIndex(DBHelper.PRODUCT_DATE_COLUMN_VIEW));
                int priority = cursor.getInt(cursor.getColumnIndex(DBHelper.PRODUCT_PRIORITY_COLUMN_VIEW));
                int status = cursor.getInt(cursor.getColumnIndex(DBHelper.PRODUCT_STATUS_COLUMN_VIEW));
                long timeStamp = cursor.getLong(cursor.getColumnIndex(DBHelper.PRODUCT_TIME_STAMP_COLUMN_VIEW));
                String description = cursor.getString(cursor.getColumnIndex(DBHelper.PRODUCT_DESCRIPTION_VIEW));

                ModelTask modelTask = new ModelTask(title, date, priority, status, timeStamp, quantity, description);
                tasks.add(modelTask);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return tasks;
    }
}
