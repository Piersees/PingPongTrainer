package haikal.android.fr.pingpong.model;

/**
 * Created by Megou on 09/04/2017.
 */

public class Matches {


    private long id;
    private double longitude, latitude;
    private long winner_id;
    private long player1;
    private long player2;
    private String begin_time;
    private String end_time;

    public Matches(){}

    public Matches(double latitude, double longitude, long player1, long player2, String begin_time){
        this.latitude=latitude;
        this.longitude=longitude;
        this.player1=player1;
        this.player2=player2;
        this.begin_time=begin_time;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public long getPlayer1() {
        return player1;
    }

    public String getEnd_time() {
        return end_time;
    }

    public long getWinner_id() {
        return winner_id;
    }

    public long getPlayer2() {
        return player2;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }



    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setPlayer1(long player1) {
        this.player1 = player1;
    }

    public void setPlayer2(long player2) {
        this.player2 = player2;
    }

    public void setWinner_id(long winner_id) {
        this.winner_id = winner_id;
    }
}
