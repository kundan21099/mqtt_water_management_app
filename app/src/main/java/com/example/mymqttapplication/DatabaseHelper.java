 package com.example.mymqttapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

 public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String WATERMANAGEMENT_TABLE = "WATERMANAGEMENT_TABLE";
    public static final String COLUMN_VOLUME_A = "VOLUME";
    public static final String COLUMN_VOLUME_B = "VOLUMEB";
    public static final String COLUMN_FLOW_A = "FLOWA";
    public static final String COLUMN_FLOW_B = "FLOWB";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TIME = "TIME";
    public static final String COLUMN_DATE = "DATE";

     //public static String VOLUME_DB = "volume.db";


     public DatabaseHelper(@Nullable Context context) {
        super(context, "watermanagementsystem.db", null, 1);
    }



     //VOLUME_DB = "ALTER TABLE " + WATERMANAGEMENT_TABLE + " ADD COLUMN " + COLUMN_VOLUME_B + " TEXT;";


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement= "CREATE TABLE " + WATERMANAGEMENT_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TIME + " TEXT, " + COLUMN_DATE + " TEXT, " + COLUMN_VOLUME_A + " TEXT, " + COLUMN_VOLUME_B + " TEXT, " + COLUMN_FLOW_A + " TEXT, " + COLUMN_FLOW_B + " TEXT )";
        db.execSQL(createTableStatement);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*if (oldVersion < 2) {
            VOLUME_DB = "ALTER TABLE " + WATERMANAGEMENT_TABLE + " ADD COLUMN " + COLUMN_VOLUME_B + " TEXT;";
            //db.execSQL(VOLUME_DB);
        }*/
        /*if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE foo ADD COLUMN COLUMN_VOLUME_B TEXT");
        }*/
    }

    public boolean addOne(WaterManagementModel waterManagementModel) {
         String NA= String.valueOf(0);
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues cv = new ContentValues();
         cv.put(COLUMN_DATE, waterManagementModel.getCurrentDate());
         cv.put(COLUMN_VOLUME_A, waterManagementModel.getVal());
         if( waterManagementModel.getVal() != NA) {
             long insert = db.insert(WATERMANAGEMENT_TABLE, null, cv);
             if (insert == -1) {
                 return false;
             } else {
                 return true;
             }
         }else {
             return false;
         }
    }

     public boolean addTwo(WaterManagementModel waterManagementModel) {
         String NA= String.valueOf(0);
         SQLiteDatabase db= this.getWritableDatabase();
         ContentValues cv1 = new ContentValues();
         cv1.put(COLUMN_DATE, waterManagementModel.getCurrentDate());
         cv1.put(COLUMN_VOLUME_B, waterManagementModel.getVal());
         if( waterManagementModel.getVal() != NA) {
             long insert = db.insert(WATERMANAGEMENT_TABLE, null, cv1);
             if (insert == -1) {
                 return false;
             } else {
                 return true;
             }
         }else {
             return false;
         }
     }

     public boolean addThree(WaterManagementModel waterManagementModel) {
         String NA= String.valueOf(0);
         SQLiteDatabase db= this.getWritableDatabase();
         ContentValues cv1 = new ContentValues();
         cv1.put(COLUMN_TIME, waterManagementModel.getCurrentDate());
         cv1.put(COLUMN_FLOW_A, waterManagementModel.getVal());
         if( waterManagementModel.getVal() != NA) {
             long insert = db.insert(WATERMANAGEMENT_TABLE, null, cv1);
             if (insert == -1) {
                 return false;
             } else {
                 return true;
             }
         }else {
             return false;
         }
     }

     public boolean addFour(WaterManagementModel waterManagementModel) {
         String NA= String.valueOf(0);
         SQLiteDatabase db= this.getWritableDatabase();
         ContentValues cv1 = new ContentValues();
         cv1.put(COLUMN_TIME, waterManagementModel.getCurrentDate());
         cv1.put(COLUMN_FLOW_B, waterManagementModel.getVal());
         if( waterManagementModel.getVal() != NA) {
             long insert = db.insert(WATERMANAGEMENT_TABLE, null, cv1);
             if (insert == -1) {
                 return false;
             } else {
                 return true;
             }
         }else {
             return false;
         }
     }


     public boolean deleteOne(WaterManagementModel waterManagementModel){
         SQLiteDatabase db=this.getWritableDatabase();
         String queryString= " DELETE FROM " +WATERMANAGEMENT_TABLE;
         Cursor cursor= db.rawQuery(queryString,null);
         if(cursor.moveToFirst()){
             return true;
         }else {
             return false;
         }

     }


     public ArrayList<String> queryXData(){
         SQLiteDatabase db=this.getWritableDatabase();
         ArrayList<String> xData=new ArrayList<String>();

         String query= " SELECT " + COLUMN_TIME + " FROM " + WATERMANAGEMENT_TABLE + " WHERE " + COLUMN_TIME + " IS NOT NULL " ;

         @SuppressLint("Recycle") Cursor cursor=db.rawQuery(query,null);

         for(cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){

             xData.add(cursor.getString(0));
         }
         cursor.close();
         db.close();
         return xData;

     }

     public ArrayList<String> queryXDataB(){
         SQLiteDatabase db=this.getWritableDatabase();
         ArrayList<String> xData=new ArrayList<String>();

         String query= " SELECT " + COLUMN_DATE + " FROM " + WATERMANAGEMENT_TABLE + " WHERE " + COLUMN_DATE + " IS NOT NULL " + " GROUP BY " + COLUMN_DATE;;

         @SuppressLint("Recycle") Cursor cursor=db.rawQuery(query,null);

         for(cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){

             xData.add(cursor.getString(0));
         }
         cursor.close();
         db.close();
         return xData;

     }

     public ArrayList<String> queryYData(){
         SQLiteDatabase db=this.getWritableDatabase();
         ArrayList<String> yData=new ArrayList<String>();
         String query= " SELECT SUM( " + COLUMN_VOLUME_A + ") FROM " + WATERMANAGEMENT_TABLE + " WHERE " + COLUMN_VOLUME_A + " IS NOT NULL " + " GROUP BY " + COLUMN_DATE;


        // String query= "SELECT SUM(" + COLUMN_VOLUME + ") FROM " + WATERMANAGEMENT_TABLE + " WHERE " + COLUMN_VOLUME + " IS NOT NULL " + " GROUP BY " + COLUMN_DATE;

         @SuppressLint("Recycle") Cursor cursor=db.rawQuery(query,null);

         for(cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){

             yData.add(cursor.getString(0));
         }
         cursor.close();
         db.close();
         return yData;

     }

     public ArrayList<String> queryYDataB(){
         SQLiteDatabase db=this.getWritableDatabase();
         ArrayList<String> yData=new ArrayList<String>();
         String query= " SELECT SUM( " + COLUMN_VOLUME_B + ") FROM " + WATERMANAGEMENT_TABLE + " WHERE " + COLUMN_VOLUME_B + " IS NOT NULL " + " GROUP BY " + COLUMN_DATE; ;


         // String query= "SELECT SUM(" + COLUMN_VOLUME + ") FROM " + WATERMANAGEMENT_TABLE + " WHERE " + COLUMN_VOLUME + " IS NOT NULL " + " GROUP BY " + COLUMN_DATE;

         @SuppressLint("Recycle") Cursor cursor=db.rawQuery(query,null);

         for(cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){

             yData.add(cursor.getString(0));
         }
         cursor.close();
         db.close();
         return yData;

     }

     public ArrayList<String> queryYDataFlowA(){
         SQLiteDatabase db=this.getWritableDatabase();
         ArrayList<String> yData=new ArrayList<String>();
         String query= " SELECT " + COLUMN_FLOW_A + " FROM " + WATERMANAGEMENT_TABLE + " WHERE " + COLUMN_FLOW_A + " IS NOT NULL " ;


         // String query= "SELECT SUM(" + COLUMN_VOLUME + ") FROM " + WATERMANAGEMENT_TABLE + " WHERE " + COLUMN_VOLUME + " IS NOT NULL " + " GROUP BY " + COLUMN_DATE;

         @SuppressLint("Recycle") Cursor cursor=db.rawQuery(query,null);

         for(cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){

             yData.add(cursor.getString(0));
         }
         cursor.close();
         db.close();
         return yData;

     }

     public ArrayList<String> queryYDataFlowB(){
         SQLiteDatabase db=this.getWritableDatabase();
         ArrayList<String> yData=new ArrayList<String>();
         String query= " SELECT " + COLUMN_FLOW_B + " FROM " + WATERMANAGEMENT_TABLE + " WHERE " + COLUMN_FLOW_B + " IS NOT NULL " ;


         // String query= "SELECT SUM(" + COLUMN_VOLUME + ") FROM " + WATERMANAGEMENT_TABLE + " WHERE " + COLUMN_VOLUME + " IS NOT NULL " + " GROUP BY " + COLUMN_DATE;

         @SuppressLint("Recycle") Cursor cursor=db.rawQuery(query,null);

         for(cursor.moveToFirst(); !cursor.isAfterLast();cursor.moveToNext()){

             yData.add(cursor.getString(0));
         }
         cursor.close();
         db.close();
         return yData;

     }
}
