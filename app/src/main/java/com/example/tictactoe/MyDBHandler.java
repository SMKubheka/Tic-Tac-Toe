package com.example.tictactoe;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;


public class MyDBHandler extends SQLiteOpenHelper {

    // VARIABLES

    private static final int DATABASE_VERSION = 12;
    private static final String DATABASE_NAME = "leaderboard.db";
    public static final String TABLE_PLAYERS = "players";
    public static final String PLAYER_NAME = "Name"; // column players names
    public static final String GAMES_WON = "Games_Won"; // column games won

    // CONSTRUCTOR
    public MyDBHandler(Context context, String name) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE players(Name TEXT NOT NULL, Games_Won INT NOT NULL)");
        //db.execSQL("INSERT INTO players(Name,Games_Won) values ('Sibongakonke','20')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        onCreate(db);
    }

    // Update values in table
    public void doUpdate(String sql, String[] params){
        try{
            getWritableDatabase().execSQL(sql,params);
        }catch(SQLException mSQLException){
            System.err.println("-- doUpdate -- \n"+sql);
            mSQLException.printStackTrace(System.err);
        }
    }

    public Cursor doQuery(String sql) {
        try {
            Cursor mCur = getReadableDatabase().rawQuery(sql,null);
            return mCur;
        } catch (SQLException mSQLException) {
            System.err.println("-- doQuery --\n"+sql);
            mSQLException.printStackTrace();
            return null;
        }
    }
}
