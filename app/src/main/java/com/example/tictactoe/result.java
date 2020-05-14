package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class result extends AppCompatActivity {

    int player1Points;
    int player2Points;

    private String player1name;
    private String player2name;

    private MyDBHandler DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        TextView winner = (TextView) findViewById(R.id.winner);
        TextView scoreboard = (TextView) findViewById(R.id.scoreboard);
        DB = new MyDBHandler(this,"app");


        SQLiteDatabase db = DB.getReadableDatabase();

        // Receiving and Displaying the score

        player1Points = getIntent().getIntExtra("WINS1", 0);
        player2Points = getIntent().getIntExtra("WINS2", 0);

        player1name = getIntent().getStringExtra("NAMES1"); //come back to this
        player2name = getIntent().getStringExtra("NAMES2");

        // Creating Shared Preferences to store the names

        if(player1Points > player2Points) {
            winner.setText(player1name);
            scoreboard.setText(player1Points + "-" + player2Points);
            updateScore(db,player1name);
        }
        else {
            winner.setText(player2name);
            scoreboard.setText(player2Points + "-" + player1Points);
            updateScore(db,player2name);
        }
        db.close();
    }

    public void NEWGAME(View view) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    public void showLEADERBOARD(View view) {
        startActivity(new Intent(getApplicationContext(), Leaderboard.class));
    }

    // DISABLE RETURN BUTTON
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public int getScore (SQLiteDatabase db, String name){
        // make sure SQLiteDatabase is readable
        int output = 0;
        Cursor cursor = db.query(MyDBHandler.TABLE_PLAYERS, new String[] {MyDBHandler.GAMES_WON}, MyDBHandler.PLAYER_NAME + " ='" + name + "'",null,null,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                output = cursor.getInt(0);
            }
        }
        return output;
    }

    public void updateScore(SQLiteDatabase db, String name){
        int playerScore = getScore(db,name);
        int playerScoreInc = playerScore + 1;

        ContentValues values = new ContentValues();
        values.put("Games_Won",Integer.toString(playerScoreInc));

        db.update(MyDBHandler.TABLE_PLAYERS,values,MyDBHandler.PLAYER_NAME + " =?", new String[] {name});
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("player1name", player1name);
        outState.putString("player2name", player2name);
        outState.putInt("player2Points", player2Points);
        outState.putInt("player1Points", player1Points);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        player1name = savedInstanceState.getString("player1name");
        player2name = savedInstanceState.getString("player2name");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Points = savedInstanceState.getInt("player1Points");
    }
}
