package com.example.tictactoe;

public class Player {
    private String _player_Name;
    private int _games_won;

    public Player(){
    }

    public Player(String _player_Name,  int _games_won){
        this._player_Name = _player_Name;
        this._games_won = _games_won;
    }

    public String get_player_Name() {
        return _player_Name;
    }

    public void set_player_Name(String _player_Name) {
        this._player_Name = _player_Name;
    }

    public int get_games() {
        return _games_won;
    }

    public void set_games(int _games_won) {
        this._games_won = _games_won;
    }
}
