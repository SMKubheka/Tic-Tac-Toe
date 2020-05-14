package com.example.tictactoe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class username extends AppCompatActivity {

    private MyDBHandler myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        myDB = new MyDBHandler(this,"app");
    }

    public void pressEnter(View view){
        EditText player1name = (EditText) findViewById(R.id.username1);
        EditText player2name = (EditText) findViewById(R.id.username2);

        String playerOne = player1name.getText().toString();
        String playerTwo = player2name.getText().toString();

        String [] vals1 = {playerOne,"0"};
        String [] vals2 = {playerTwo,"0"};

        SQLiteDatabase db = myDB.getWritableDatabase();

        insertName(db,playerOne,vals1);
        insertName(db,playerTwo,vals2);

        db.close();

        // Transfer name input to MainActivity

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("NAME1",playerOne);
        intent.putExtra("NAME2",playerTwo);
        startActivity(intent);

    }

    // Insert name into db

    public boolean insertName(SQLiteDatabase db,String name, String[] vals){
        // Check if name exists
        Cursor res = db.rawQuery("SELECT * FROM players WHERE Name = ?", new String[]{ name });

        // If name doesn't exist -> add
        if (res.getCount() == 0){
            myDB.doUpdate("Insert into players(Name, Games_Won) values (?,?);", vals);
            return true;
        }
        // else -> return false
        return false;
    }
}