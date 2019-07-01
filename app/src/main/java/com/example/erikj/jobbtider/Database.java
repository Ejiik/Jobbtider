package com.example.erikj.jobbtider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.round;

/**
 * Created by erikj on 3/23/2018.
 */

public class Database extends SQLiteOpenHelper {

    private static final String NAME = "TidsDatabas";
    private static final String TABLE_TIME = "Time";
    private static final String TABLE_MONTH = "Month";
    private static final String TABLE_YEAR = "Year";
    private static final String TABLE_TIMESTART = "TimeStart";
    private static final String KEY_MONTH = "month";
    private static final String KEY_YEAR = "year";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_WEEK = "week";

    public Database(Context context) {
        super(context, NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TIMESTART_TABLE = "CREATE TABLE " + TABLE_TIMESTART + "(" + KEY_TIME + " TEXT," + KEY_DATE + " TEXT)";
        String CREATE_TIME_TABLE = "CREATE TABLE " + TABLE_TIME + "("
                + KEY_DATE + " TEXT," + KEY_WEEK + " INT(53)," + KEY_TIME + " TEXT)";
        String CREATE_MONTH_TABLE = "CREATE TABLE " + TABLE_MONTH + "("
                + KEY_MONTH + " TEXT," + KEY_TIME + " TEXT)";
        String CREATE_YEAR_TABLE = "CREATE TABLE " + TABLE_YEAR + "("
                + KEY_YEAR + " TEXT," + KEY_TIME + " TEXT)";

        db.execSQL(CREATE_TIMESTART_TABLE);
        db.execSQL(CREATE_TIME_TABLE);
        db.execSQL(CREATE_MONTH_TABLE);
        db.execSQL(CREATE_YEAR_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIME);

        onCreate(db);
    }

    public void startTime(long time, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TIME, time);
        values.put(KEY_DATE, date);
        db.insert(TABLE_TIMESTART, null, values);
    }

    public long getStartTime(){
        SQLiteDatabase db = this.getReadableDatabase();
        long time = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIMESTART, null);
        if(cursor.moveToFirst()){
            do{
                time = Long.parseLong(cursor.getString(0));
            }while(cursor.moveToNext());
        }

        return time;
    }

    public String getStartDate(){
        SQLiteDatabase db = this.getReadableDatabase();
        String date = "";

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIMESTART, null);
        if(cursor.moveToFirst()){
            do{
                date = cursor.getString(1);
            }while(cursor.moveToNext());
        }

        return date;
    }

    public void addTime(String date,int week, String hours){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ContentValues monthValues = new ContentValues();

        values.put(KEY_DATE, date);
        values.put(KEY_WEEK, week);
        values.put(KEY_TIME, hours);

        db.execSQL("delete from " + TABLE_TIMESTART);

        if(getLatestTime().equals("") || date.substring(3, 5).equals(getLatestTime().substring(3, 5))){
            db.insert(TABLE_TIME, null, values);
        } else{
            Cursor cursor= db.rawQuery("SELECT * FROM " + TABLE_TIME, null);
            int sumMinutes = 0;

            if(cursor.moveToFirst()){
                do{
                    sumMinutes += (Integer.parseInt(cursor.getString(2)));
                }while(cursor.moveToNext());
            }
            monthValues.put(KEY_TIME, sumMinutes/60 + ":" + sumMinutes%60);
            db.insert(TABLE_MONTH, null, monthValues);
            db.insert(TABLE_TIME, null, values);
        }

        db.close();
    }

    public String getLatestTime(){
        String time = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIME, null);
        if(cursor.moveToFirst()){
            do{
                time = cursor.getString(0);
            }while(cursor.moveToNext());
        }
        return time;
    }

    public List<String> getAllTimes(){
        List<String> timesList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIME, null);

        if(cursor.moveToFirst()){
            do{
                timesList.add("Datum: " + cursor.getString(0) + "\nVecka: " + cursor.getInt(1) + "    Timmar: " + Integer.parseInt(cursor.getString(2))/60 + "    Minuter: " + Integer.parseInt(cursor.getString(2))%60);
            } while(cursor.moveToNext());
        }
        Collections.reverse(timesList);
        return timesList;
    }

    public String getTotalTime(String table){
        SQLiteDatabase db = this.getReadableDatabase();

        switch(table){
            case TABLE_TIME:

                break;
            case TABLE_MONTH:

                break;
            case TABLE_YEAR:

                break;
        }

        return "";
    }

    public void clearTable(String table){
        SQLiteDatabase db = this.getWritableDatabase();
        switch(table){
            case TABLE_TIME:

                db.execSQL("delete from " + TABLE_TIME);
                Log.e("DB", getLatestTime());
                break;
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
