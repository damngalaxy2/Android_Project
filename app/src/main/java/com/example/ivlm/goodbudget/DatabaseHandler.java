package com.example.ivlm.goodbudget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by _IVLM on 3/10/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Envelope.db";
    public static final String TABLE_NAME = "envelope_table";
    public static final String TABLE_NAME_2 = "spinner_envelope_item_table";
    public static final String TABLE_NAME_3 = "income_table";
    public static final String TABLE_NAME_4 = "transaction_table";
    public static final String TABLE_NAME_5 = "unallocated";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "Envelope";
    public static final String COL_3 = "Envelope_Amount";
    public static final String COL_4 = "Envelope_Type";
    public static final String COL_10 = "Now_Amount";

    public static final String COL_5 = "Envelope_Collections";

    public static final String COL_6 = "ID_Income";
    public static final String COL_7 = "Income";
    public static final String COL_8 = "Income_Type";
    public static final String COL_9 = "Total_Income_per_Row";

    public static final String COL_11 = "ID_Transactions";
    public static final String COL_12 = "Title_Transactions";
    public static final String COL_13 = "Date_Transactions";
    public static final String COL_14 = "Amount_Transactions";
    public static final String COL_15 = "Envelope_Transactions";
    public static final String COL_16 = "Account_Transactions";
    public static final String COL_17 = "Type_Transactions";
    public static final String COL_18 = "Note_Transactions";
    public static final String COL_19 = "Date_Transactions_String";

    public static final String COL_20 = "Id_Unallocated";
    public static final String COL_21 = "Amount_Unallocated";
    public static final String COL_22 = "Type";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE Table " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " TEXT," + COL_3 + " DOUBLE," + COL_4 + " TEXT," + COL_10 + " DOUBLE)");
        db.execSQL("CREATE Table " + TABLE_NAME_2 + " (" + COL_5 + " TEXT PRIMARY KEY)");
        db.execSQL("CREATE Table " + TABLE_NAME_3 + " (" + COL_6 + " INTEGER PRIMARY KEY," + COL_7 + " DOUBLE," + COL_8 + " INTEGER," + COL_9 + " INTEGER)");
        db.execSQL("CREATE Table " + TABLE_NAME_4 + " (" + COL_11 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_12 + " TEXT," + COL_13 + " LONG," + COL_14 + " DOUBLE," + COL_15 + " TEXT," + COL_16 + " TEXT," +
                COL_17 + " TEXT," + COL_18 + " TEXT," + COL_19 + " TEXT)");
        db.execSQL("CREATE Table " + TABLE_NAME_5 + " (" + COL_20 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_21 + " DOUBLE," + COL_22 + " TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_4);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_5);
        onCreate(db);
    }
    public boolean InsertData(String EnvelopeName, double EnvelopeAmount, String EnvelopeType){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, EnvelopeName);
        contentValues.put(COL_3, EnvelopeAmount);
        contentValues.put(COL_4, EnvelopeType);
        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        if(result == -1){
            sqLiteDatabase.close();
            return false;
        }else
            sqLiteDatabase.close();
            return true;
    }

    public boolean InsertDataUnallocated(double Amount, String type){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_21, Amount);
        contentValues.put(COL_22, type);

        sqLiteDatabase.insert(TABLE_NAME_5, null, contentValues);
        return true;
    };
    public Cursor getCountData(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        return cursor;
    }

    public Cursor getUnallocatedPlust(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +TABLE_NAME_5 + " WHERE Type='Plus'",null);

        return cursor;
    }
    public Cursor getCompareEnvelope(String envelope){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME +" WHERE Envelope ='"+envelope+"'",null);

        return cursor;
    }
    public boolean InsertNowAmount(double NowAmount, int Id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_10, NowAmount);
        sqLiteDatabase.update(TABLE_NAME, contentValues, "ID="+Id, null);
        return true;
    }
    public Cursor getSpecificNowAmount(String envelope){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Envelope='"+envelope+"'",null);
        return cursor;
    }
    public boolean UpdateSpecificNowAmount(String envelope, double NowAmount){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_10, NowAmount);
        sqLiteDatabase.update(TABLE_NAME, contentValues, "Envelope='"+envelope+"'",null);
        return true;
    }

    public Cursor getMaxData(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT MAX(ID) FROM " + TABLE_NAME, null);

        return cursor;
    }

    public Cursor getMinData(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT MIN(ID) FROM "+TABLE_NAME,null);

        return cursor;
    }

    public Cursor getSumAll(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(Envelope_Amount) AS Total FROM " + TABLE_NAME, null);

        return cursor;
    }

    public Cursor getSumUnallocated(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM("+COL_21+") AS Total FROM " + TABLE_NAME_5,null);

        return cursor;
    }

    public Cursor getSumMonthly(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(Envelope_Amount) AS TotalMonthly FROM " + TABLE_NAME + " WHERE Envelope_Type = 'Monthly'", null);

        return cursor;
    }

    public Cursor getSUMNowAmountMonthly(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(Now_Amount) AS TotalAMount FROM " + TABLE_NAME + " WHERE Envelope_Type='Monthly'", null);

        return cursor;
    }

    public Cursor getSUMNowAmountYear(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(Now_Amount) AS TotalAMount FROM " + TABLE_NAME + " WHERE Envelope_Type='Year'", null);

        return cursor;
    }
    public Cursor getSumYear(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(Envelope_Amount) AS TotalYear FROM " + TABLE_NAME + " WHERE Envelope_Type = 'Year'", null);

        return cursor;
    }

    public Cursor getCountDataMonthly(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+COL_4+ " = 'Monthly'",null);

        return cursor;
    }

    public Cursor getCountDataYear(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+COL_4+ " = 'Year'",null);

        return cursor;
    }

    public boolean UpdateEnvelope(int id, String envelope, double amount, String type){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,envelope);
        contentValues.put(COL_3,amount);
        contentValues.put(COL_4, type);
        sqLiteDatabase.update(TABLE_NAME, contentValues, "ID="+id, null);
        return true;
    }

    public boolean DeleteEnvelope(int Id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.delete(TABLE_NAME, COL_1 + " = " + Id, null) > 0;
    }

    public boolean InsertDataSpinnerEnvelope(String EnvelopeCollectionsName){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_5, EnvelopeCollectionsName);
        long result = sqLiteDatabase.insert(TABLE_NAME_2, null, contentValues);
        if(result == -1){
            sqLiteDatabase.close();
            return  false;
        }else
            sqLiteDatabase.close();
            return true;
    }

    public Cursor getDataEnvelopeCollections(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_2, null);

        return cursor;
    }

    public void DeleteEnvelopeCollections(String rowID){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        try {
            sqLiteDatabase.delete(TABLE_NAME_2, COL_5 + " =?", new String[]{rowID});
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            sqLiteDatabase.close();
        }
    }

    public boolean InsertIncome(int id, double Income, int Position, int Total){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_6, id);
        contentValues.put(COL_7, Income);
        contentValues.put(COL_8, Position);
        contentValues.put(COL_9, Total);
        long result = sqLiteDatabase.insert(TABLE_NAME_3, null, contentValues);
        if(result == -1){
            sqLiteDatabase.close();
            return false;
        }else
            sqLiteDatabase.close();
            return true;
    }

    public Cursor getCountIncomeData(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_3, null);

        return cursor;
    }

    public Cursor getSumIncome(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(Total_Income_per_Row)FROM " + TABLE_NAME_3 ,null);

        return cursor;
    }
    public void DeleteDataIncome(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM "+ TABLE_NAME_3);
        sqLiteDatabase.execSQL("VACUUM");
    }
    public boolean InsertTransactions(String Title, long Date, double Amount, String EnvelopeName, String Account, String Type, String Note, String DateString){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_12, Title);
        contentValues.put(COL_13, Date);
        contentValues.put(COL_14, Amount);
        contentValues.put(COL_15, EnvelopeName);
        contentValues.put(COL_16, Account);
        contentValues.put(COL_17, Type);
        contentValues.put(COL_18, Note);
        contentValues.put(COL_19, DateString);
        sqLiteDatabase.insert(TABLE_NAME_4, null, contentValues);
        return  true;
    }
    public Cursor getAllDataTransactions(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_4 + " ORDER BY " + COL_11+ " DESC", null);
        return cursor;
    }

    public Cursor getSumTransactionsExpense (){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(Amount_Transactions) FROM " + TABLE_NAME_4 + " WHERE Type_Transactions = 'Expense'", null);

        return cursor;
    }
    public Cursor getSumTransactionsCredit (){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM(Amount_Transactions) FROM " + TABLE_NAME_4 + " WHERE Type_Transactions = 'Credit'", null);

        return cursor;
    }
}
