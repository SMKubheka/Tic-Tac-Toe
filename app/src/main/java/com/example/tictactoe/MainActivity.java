package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int GAME_FIELD_SIZE = 3;
    private static final String EMPTY_FIELD = "";

    private Button[][] gameCells = new Button[GAME_FIELD_SIZE][GAME_FIELD_SIZE];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    private String TEMP_NAME1;
    private String TEMP_NAME2;

    private String PERM_NAME1;
    private String PERM_NAME2;

    private int holder = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.P1);
        textViewPlayer2 = findViewById(R.id.P2);

        for (int i = 0; i < GAME_FIELD_SIZE; i++) {
            for (int j = 0; j < GAME_FIELD_SIZE; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                gameCells[i][j] = findViewById(resID);
                gameCells[i][j].setOnClickListener(this);
            }
        }

        // Creating the rest button on Game Screen

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        // Receiving names from the username activity

        TEMP_NAME1 = getIntent().getStringExtra("NAME1");
        TEMP_NAME2 = getIntent().getStringExtra("NAME2");

        textViewPlayer1.setText(TEMP_NAME1 + ": " + 0);
        textViewPlayer2.setText(TEMP_NAME2 + ": " + 0);

        // Creating Shared Preference to store the names

        SharedPreferences settings = getSharedPreferences("players",MODE_PRIVATE);
        PERM_NAME1 = settings.getString("ACTUAL1","player 1");
        PERM_NAME2 = settings.getString("ACTUAL2","player 2");

        if (TEMP_NAME1 != null && !TEMP_NAME1.equals(PERM_NAME1)) {
            holder = 1;
            textViewPlayer1.setText(TEMP_NAME1 + ": " + 0);

            // SAVE
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("ACTUAL1", TEMP_NAME1);
            editor.commit();

        }
        if (TEMP_NAME2 != null && !TEMP_NAME2.equals(PERM_NAME2)){
            holder = 1;
            textViewPlayer2.setText(TEMP_NAME2 + ": " + 0);

            // SAVE
            SharedPreferences.Editor editor1 = settings.edit();
            editor1.putString("ACTUAL2", TEMP_NAME2);
            editor1.commit();
        }
        else{
            holder = -1;
            textViewPlayer1.setText(PERM_NAME1 + ": " + 0);
            textViewPlayer2.setText(PERM_NAME2 + ": " + 0);
        }

    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals(EMPTY_FIELD)) {
            return;
        }

        if (player1Turn) {
            ((Button) v).setText("O");
        } else {
            ((Button) v).setText("X");
        }

        roundCount++;

        if (checkForWinner()) {
            if (player1Turn) {
                ReportP1Wins();
            } else {
                ReportP2Wins();
            }
        } else if (roundCount == 9) {
            ReportDraw();
        } else {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWinner() {
        String[][] field = new String[GAME_FIELD_SIZE][GAME_FIELD_SIZE];

        for (int i = 0; i < GAME_FIELD_SIZE; i++) {
            for (int j = 0; j < GAME_FIELD_SIZE; j++) {
                field[i][j] = gameCells[i][j].getText().toString();
            }
        }

        for (int i = 0; i < GAME_FIELD_SIZE; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals(EMPTY_FIELD)) {
                return true;
            }
        }

        for (int i = 0; i < GAME_FIELD_SIZE; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals(EMPTY_FIELD)) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals(EMPTY_FIELD)) {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals(EMPTY_FIELD)) {
            return true;
        }

        return false;
    }

    private void ReportP1Wins() {
        player1Points++;

        if (player1Points == 5){

            // SHOW RESULT PAGE
            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("WINS1",player1Points); // this passes a variable from one activity to another
            intent.putExtra("WINS2",player2Points);

            if (holder == 1){
                intent.putExtra("NAMES1",TEMP_NAME1);
                intent.putExtra("NAMES2",TEMP_NAME2);
            }
            else{
                intent.putExtra("NAMES1",PERM_NAME1);
                intent.putExtra("NAMES2",PERM_NAME2);
            }
            startActivity(intent);
        }

        else{
            Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
            updatePointsText();
            resetBoard();
        }
    }

    private void ReportP2Wins() {
        player2Points++;

        if (player2Points == 5){

            // SHOW RESULT PAGE
            Intent intent = new Intent(getApplicationContext(), result.class);
            intent.putExtra("WINS1",player1Points);
            intent.putExtra("WINS2",player2Points);

            if (holder == 1){
                intent.putExtra("NAMES1",TEMP_NAME1);
                intent.putExtra("NAMES2",TEMP_NAME2);
            }
            else {
                intent.putExtra("NAMES1", PERM_NAME1);
                intent.putExtra("NAMES2", PERM_NAME2);

                startActivity(intent);
            }
        }
        else{
            Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
            updatePointsText();
            resetBoard();
        }
    }

    private void ReportDraw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void updatePointsText() {

        if (holder == 1){
            textViewPlayer1.setText(TEMP_NAME1 + ": " + player1Points);
            textViewPlayer2.setText(TEMP_NAME2 + ": " + player2Points);
        }
        else{
            textViewPlayer1.setText(PERM_NAME1 + ": " + player1Points);
            textViewPlayer2.setText(PERM_NAME2 + ": " + player2Points);
        }
    }

    private void resetBoard() {
        for (int i = 0; i < GAME_FIELD_SIZE; i++) {
            for (int j = 0; j < GAME_FIELD_SIZE; j++) {
                gameCells[i][j].setText(EMPTY_FIELD);
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }

    // DISABLE THE RETURN BUTTON

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch(event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

}

