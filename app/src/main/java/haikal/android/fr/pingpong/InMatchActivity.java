package haikal.android.fr.pingpong;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

import haikal.android.fr.pingpong.model.Matches;
import haikal.android.fr.pingpong.DatabaseHelper;
import haikal.android.fr.pingpong.model.Players;
import haikal.android.fr.pingpong.model.Sets;

public class InMatchActivity extends AppCompatActivity {

    private DatabaseHelper dbh;
    private String currentServer;
    private String setServer;
    private int setnbJ1, setnbJ2, indice;
    Players player1, player2;
    Matches match;
    Sets currentSet;

    TextView nameJ1, nameJ2, p1name, p2name, scoreJ1, scoreJ2, setJ1, setJ2, server;

    private int currentFoul;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbh = new DatabaseHelper(getApplicationContext());

        Intent intent = getIntent();

        // creating players instances
        player1 = new Players(intent.getStringExtra("player1"));
        player2 = new Players(intent.getStringExtra("player2"));
        // adding players to the database if they don't already exist, fetching their ids
        player1.setId(dbh.createPlayer(player1.getName()));
        player2.setId(dbh.createPlayer(player2.getName()));

        // creating new match instance
        match = new Matches(intent.getDoubleExtra("latitude",0), intent.getDoubleExtra("longitude",0), player1.getId(), player2.getId(), intent.getStringExtra("time"));
        // adding match to the database, fetching its id
        match.setId(dbh.createMatch(match));

        // creating new set instance
        currentSet = new Sets(match.getId());
        // adding set to the database, fetching its id
        currentSet.setId(dbh.createSet(currentSet));

        setnbJ1 = 0;
        setnbJ2 = 0;
        indice = 0;

        nameJ1 = (TextView) findViewById(R.id.J1name);
        nameJ2 = (TextView) findViewById(R.id.J2name);
        p1name = (TextView) findViewById(R.id.p1name);
        p2name = (TextView) findViewById(R.id.p2name);
        scoreJ1 = (TextView) findViewById(R.id.scoreJ1);
        scoreJ2 = (TextView) findViewById(R.id.scoreJ2);
        setJ1 = (TextView) findViewById(R.id.setJ1);
        setJ2 = (TextView) findViewById(R.id.setJ2);
        server = (TextView) findViewById(R.id.service);

        // displaying player names
        nameJ1.setText(player1.getName());
        nameJ2.setText(player2.getName());
        p1name.setText(player1.getName());
        p2name.setText(player2.getName());

        //displaying player score
        scoreJ1.setText(String.valueOf(currentSet.getScore1()));
        scoreJ2.setText(String.valueOf(currentSet.getScore2()));
        setJ1.setText(String.valueOf(setnbJ1));
        setJ2.setText(String.valueOf(setnbJ2));

        setServer = intent.getStringExtra("server");
        currentServer = intent.getStringExtra("server");
        // displaying servicing player name
        switch(currentServer){
            case "1":
                server.setText(player1.getName());
                break;
            case "2":
                server.setText(player2.getName());
                break;
        }

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void myClickHandler(View view) {

        final Intent intentSM = new Intent(this, StatsMatchActivity.class);
        switch (view.getId()) {
            case R.id.matchEnd:
                if(setnbJ1 > setnbJ2){
                    match.setWinner_id(player1.getId());
                }else{
                    if(setnbJ2 > setnbJ1){
                        match.setWinner_id(player2.getId());

                    }else{
                            this.showMatchNotOverDialog();
                            break;
                    }
                }
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String formattedDate = df.format(c.getTime());
                match.setEnd_time(formattedDate);
                dbh.updateMatch(match);
                startActivity(intentSM);
                break;

            case R.id.ptAtkJ1:
                dbh.createAttack(match.getId(),player1.getId(),currentSet.getId());
                this.player1WinsPoint();
                indice++;
                this.serviceRoutine();
                break;

            case R.id.ptAtkJ2:
                dbh.createAttack(match.getId(),player2.getId(),currentSet.getId());
                this.player2WinsPoint();
                indice++;
                this.serviceRoutine();
                break;

            case R.id.ace1balle:
                switch (currentServer){
                    case "1":
                        dbh.createAces(match.getId(),player1.getId(),currentSet.getId(),1);
                        this.player1WinsPoint();
                        indice++;
                        this.serviceRoutine();
                        break;
                    case "2":
                        dbh.createAces(match.getId(),player2.getId(),currentSet.getId(),1);
                        this.player2WinsPoint();
                        indice++;
                        this.serviceRoutine();

                        break;
                }
                break;

            case R.id.ace2balle:
                switch (currentServer){
                    case "1":
                        dbh.createAces(match.getId(),player1.getId(),currentSet.getId(),2);
                        this.player1WinsPoint();
                        indice++;
                        this.serviceRoutine();
                        break;
                    case "2":
                        dbh.createAces(match.getId(),player2.getId(),currentSet.getId(),2);
                        this.player2WinsPoint();
                        indice++;
                        this.serviceRoutine();

                        break;
                }
                break;

            case R.id.fautebutton:
                this.showFoulDialog();
                switch (currentServer){
                    case "1":
                        createFoul(currentFoul, player1, currentSet, match);
                        this.player2WinsPoint();
                        indice++;
                        this.serviceRoutine();
                        break;
                    case "2":
                        createFoul(currentFoul, player2, currentSet, match);
                        this.player1WinsPoint();
                        indice++;
                        this.serviceRoutine();
                        break;
                }
                break;

            case R.id.fautej1:
                this.showFoulDialog();
                createFoul(currentFoul,player1,currentSet,match);
                this.player2WinsPoint();
                indice++;
                this.serviceRoutine();
                break;

            case R.id.fautej2:
                this.showFoulDialog();
                createFoul(currentFoul,player2,currentSet,match);
                this.player1WinsPoint();
                indice++;
                this.serviceRoutine();
                break;

            case R.id.ptDefJ1:
                dbh.createDefense(match.getId(),player1.getId(),currentSet.getId());
                this.player1WinsPoint();
                indice++;
                this.serviceRoutine();
                break;

            case R.id.ptDefJ2:
                dbh.createDefense(match.getId(),player2.getId(),currentSet.getId());
                this.player2WinsPoint();
                indice++;
                this.serviceRoutine();
                break;
        }
    }

    public void showFoulDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pick_foul)
                .setItems(R.array.fouls_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        currentFoul = which;
                    }
                });
        builder.show();
    }

    public void showMatchNotOverDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.match_not_over).setMessage(R.string.message_not_over);
        builder.show();
    }

    public void createFoul(int foul, Players player, Sets set, Matches match){
        switch (foul){
            case 0:
                dbh.createBehavior(match.getId(), player.getId(),set.getId());
                break;
            case 1:
                dbh.createLets(match.getId(), player.getId(),set.getId());
                break;
            case 2:
                dbh.createDoubleMissedService(match.getId(), player.getId(),set.getId());
                break;
            case 3:
                dbh.createOutOfRange(match.getId(), player.getId(),set.getId());
                break;
        }
    }

    public void player1WinsPoint(){
        currentSet.setScore1((currentSet.getScore1()+1));
        scoreJ1.setText(String.valueOf(currentSet.getScore1()));

        // if player1's score is 11 or above and has 2+ points more than player2, player1 won the set
        if((currentSet.getScore1() >= 11) && ((currentSet.getScore1()-currentSet.getScore2())>=2)){
            currentSet.setWinner(player1.getId());
            dbh.updateSet(currentSet);
            setnbJ1++;
            setJ1.setText(String.valueOf(setnbJ1));
            this.newSet();

        }
    }

    public void player2WinsPoint(){
        currentSet.setScore2((currentSet.getScore2()+1));
        scoreJ2.setText(String.valueOf(currentSet.getScore2()));

        // if player2's score is 11 or above and has 2+ points more than player1, player2 won the set
        if((currentSet.getScore2() >= 11) && ((currentSet.getScore2()-currentSet.getScore1())>=2)){
            currentSet.setWinner(player2.getId());
            dbh.updateSet(currentSet);
            setnbJ2++;
            setJ2.setText(String.valueOf(setnbJ2));
            this.newSet();
        }
    }

    public void serverChange(){
        switch(currentServer){
            case "1":
                currentServer="2";
                break;
            case "2":
                currentServer="1";
                break;
        }
        this.displayServerName();
    }

    public void displayServerName(){
        switch(currentServer){
            case "1":
                server.setText(player1.getName());
                break;
            case "2":
                server.setText(player2.getName());
                break;
        }
    }
    public void newSet(){
        indice = 0;
        switch (setServer){
            case "1":
                setServer ="2";
                currentServer = "2";
                break;
            case "2":
                setServer = "1";
                currentServer="1";
                break;
        }
        currentSet = new Sets(match.getId());
        // adding set to the database, fetching its id
        currentSet.setId(dbh.createSet(currentSet));
        // display score
        scoreJ1.setText(String.valueOf(currentSet.getScore1()));
        scoreJ2.setText(String.valueOf(currentSet.getScore2()));
        this.displayServerName();
    }

    public void serviceRoutine(){
        if(indice == 2){
            indice = 0;
            this.serverChange();
        }
    }
}
