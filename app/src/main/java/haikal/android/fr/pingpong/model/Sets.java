package haikal.android.fr.pingpong.model;

/**
 * Created by Megou on 09/04/2017.
 */

public class Sets {
    private long id;
    private long match;
    private long player1, player2;
    private int score1;
    private int score2;
    private long winner;

    public Sets(){};

    public Sets(long match, int score1, int score2, long winner){
        this.match=match;
        this.score1=score1;
        this.winner=winner;
        this.score2=score2;
    }
    public Sets(long match, long player1, long player2){
        this.match=match;
        this.score1=0;
        this.score2=0;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void setPlayer1(long player1) {
        this.player1 = player1;
    }

    public void setPlayer2(long player2) {
        this.player2 = player2;
    }

    public long getPlayer1() {
        return player1;
    }

    public long getPlayer2() {
        return player2;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public long getId() {
        return id;
    }

    public long getMatch() {
        return match;
    }


    public long getWinner() {
        return winner;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMatch(long match) {
        this.match = match;
    }


    public void setWinner(long winner) {
        this.winner = winner;
    }

}
