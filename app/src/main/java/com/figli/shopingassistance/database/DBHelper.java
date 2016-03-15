package com.figli.shopingassistance.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.figli.shopingassistance.model.ModelTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Figli on 22.02.2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final int DATABASE_VERSION_OLD = 1;
    private static String DATABASE_NAME = "shop_product";
    private static String DATABASE_NAME_OLD = "product";
    private static String DB_PATH = "/data/data/com.figli.shopingassistance/databases/";

    public SQLiteDatabase database;
    private Context myContext;


    public static final String PRODUCT_TABLE = "product";
    public static final String PRODUCT_TABLE_VIEW = "product_view";
    public static final String PRODUCT_TITLE_COLUMN = "title";
    public static final String PRODUCT_TITLE_COLUMN_VIEW = "title";
    public static final String PRODUCT_DATE_COLUMN = "date";
    public static final String PRODUCT_DATE_COLUMN_VIEW = "date";
    public static final String PRODUCT_PRIORITY_COLUMN = "priority";
    public static final String PRODUCT_PRIORITY_COLUMN_VIEW = "priority";
    public static final String PRODUCT_STATUS_COLUMN = "status";
    public static final String PRODUCT_STATUS_COLUMN_VIEW = "status";
    public static final String PRODUCT_TIME_STAMP_COLUMN = "time_stamp";
    public static final String PRODUCT_TIME_STAMP_COLUMN_VIEW = "time_stamp";
    public static final String PRODUCT_QUANTITY_VIEW = "quantity";
    public static final String PRODUCT_DESCRIPTION_VIEW = "description";
    public static final String SELECTION_STATUS = PRODUCT_STATUS_COLUMN + " = ?";
    public static final String SELECTION_STATUS_VIEW = PRODUCT_STATUS_COLUMN_VIEW + " = ?";
    public static final String SELECTION_TIME_STAMP = PRODUCT_TIME_STAMP_COLUMN + " = ?";
    public static final String SELECTION_TIME_STAMP_VIEW = PRODUCT_TIME_STAMP_COLUMN_VIEW + " = ?";
    public static final String SELECTION_LIKE_TITLE = PRODUCT_TITLE_COLUMN + " LIKE ?";
    public static final String SELECTION_LIKE_TITLE_VIEW = PRODUCT_TITLE_COLUMN_VIEW + " LIKE ?";

    private DBQueryManager dbQueryManager;
    private DBUpdateManager dbUpdateManager;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
        create_db();
        dbQueryManager = new DBQueryManager(getReadableDatabase());
        dbUpdateManager = new DBUpdateManager(getWritableDatabase());
        boolean dbexist = checkdatabase();
        if(dbexist) {
            merge_db(DATABASE_VERSION_OLD, DATABASE_VERSION);
        }

    }

    private boolean checkdatabase() {
        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DATABASE_NAME_OLD;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch(SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }


    public void create_db(){
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DB_PATH + DATABASE_NAME);
            if (!file.exists()) {
                this.getReadableDatabase();
                //получаем локальную бд как поток
                myInput = myContext.getAssets().open(DATABASE_NAME);
                // Путь к новой бд
                String outFileName = DB_PATH + DATABASE_NAME;

                // Открываем пустую бд
                myOutput = new FileOutputStream(outFileName);

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        }
        catch(IOException ex){

        }
    }

    public void merge_db(int oldVersion, int newVersion) {

        if(newVersion != oldVersion) {
            open();
            database.execSQL("ATTACH DATABASE '" + DB_PATH + File.separator + DATABASE_NAME_OLD + "' AS Old_DB");
            database.execSQL("INSERT OR IGNORE INTO product (title) SELECT task_title FROM Old_DB.product;");
        }
    }

    public void open() throws SQLException {
        String path = DB_PATH + DATABASE_NAME;
        database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveTask(ModelTask task) {

        ContentValues newValues = new ContentValues();

        newValues.put(PRODUCT_TITLE_COLUMN, task.getTitle());
        newValues.put(PRODUCT_DATE_COLUMN, task.getDate());
        newValues.put(PRODUCT_PRIORITY_COLUMN, task.getPriority());
        newValues.put(PRODUCT_STATUS_COLUMN, task.getStatus());
        newValues.put(PRODUCT_TIME_STAMP_COLUMN, task.getTimeStamp());

        getWritableDatabase().insert(PRODUCT_TABLE, null, newValues);

    }

    public void saveTaskView(ModelTask task) {

        ContentValues newValues = new ContentValues();

        newValues.put(PRODUCT_TITLE_COLUMN_VIEW, task.getTitle());
        newValues.put(PRODUCT_DATE_COLUMN_VIEW, task.getDate());
        newValues.put(PRODUCT_PRIORITY_COLUMN_VIEW, task.getPriority());
        newValues.put(PRODUCT_STATUS_COLUMN_VIEW, task.getStatus());
        newValues.put(PRODUCT_TIME_STAMP_COLUMN_VIEW, task.getTimeStamp());
        newValues.put(PRODUCT_QUANTITY_VIEW, task.getQuantity());
        newValues.put(PRODUCT_DESCRIPTION_VIEW, task.getDescription());

        getWritableDatabase().insert(PRODUCT_TABLE_VIEW, null, newValues);

    }

    public DBUpdateManager update() {
        return dbUpdateManager;
    }

    public DBQueryManager query() {
        return dbQueryManager;
    }

    public void removeTaskView(long timeStamp) {
        getWritableDatabase().delete(PRODUCT_TABLE_VIEW, SELECTION_TIME_STAMP_VIEW, new String[]{Long.toString(timeStamp)});
    }

    public String[] getAllTasks() {

        Cursor cursor = getWritableDatabase().query(PRODUCT_TABLE, new String[]{PRODUCT_TITLE_COLUMN}, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            String[] str = new String[cursor.getCount()];
            int i = 0;

            while (cursor.moveToNext()) {
                str[i] = cursor.getString(cursor.getColumnIndex(PRODUCT_TITLE_COLUMN));
                i++;
            }
            return str;
        } else {
            return new String[]{};
        }
    }
}
