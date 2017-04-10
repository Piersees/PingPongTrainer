package haikal.android.fr.pingpong.model;

/**
 * Created by Megou on 09/04/2017.
 */

public class Players {
    private long id;
    private String name;

    //constructors
    public Players(){}

    public Players(String name){
        this.name = name;
    }

    //getters
    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    //setters
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }
}

