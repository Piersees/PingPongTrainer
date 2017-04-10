package haikal.android.fr.pingpong.model;

/**
 * Created by Megou on 09/04/2017.
 */

public class Sets {
    private long id;
    private long match;
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
    public Sets(long match){
        this.match=match;
        this.score1=0;
        this.score2=0;
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
