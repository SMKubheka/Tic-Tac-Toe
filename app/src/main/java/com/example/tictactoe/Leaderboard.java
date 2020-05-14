package com.example.tictactoe;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Leaderboard extends AppCompatActivity {

    private MyDBHandler myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Button BackButton = (Button) findViewById(R.id.button);
        BackButton.setOnClickListener( new View.OnClickListener(){
            public void onClick(View v){
                OpenResultActivity();
            }
        });

        myDB = new MyDBHandler(this,"app");

        String Solution = "";
        TextView l = (TextView) findViewById(R.id.leaderboard);
        Cursor c = myDB.doQuery("SELECT Name, Games_Won from players ORDER BY Games_Won DESC");
        while (c.moveToNext()) {
            Solution += (c.getString(c.getColumnIndex("Name")) + " " + c.getLong(c.getColumnIndex("Games_Won")) + "\n");
        }
        c.close();
        l.setText(Solution);
    }

    public void OpenResultActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }




}