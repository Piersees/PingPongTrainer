package haikal.android.fr.pingpong;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StatsClubActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    private DatabaseHelper dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_club);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        dbh = new DatabaseHelper(getApplicationContext());

        this.displayMatches();

        dbh.close();

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final Intent intentNVM = new Intent(this, NouveauMatchActivity.class );
        final Intent intentSC = new Intent(this, StatsClubActivity.class);

        if (id == R.id.nav_new_match) {
            startActivity(intentNVM);
        } else if (id == R.id.nav_prev_matches) {
            startActivity(intentSC);
        } /*else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displayMatches(){
        List<String[]> matchInfo = dbh.getMatchListInfo();
        LinearLayout container = (LinearLayout)findViewById(R.id.matchListCont);

        if(matchInfo.isEmpty()){
            TextView nomatch = new TextView(getApplicationContext());
            nomatch.setText(getString(R.string.no_match_msg));
            container.addView(nomatch);
        }else {

            for (String[] match : matchInfo) {
                Button matchButton = (Button)getLayoutInflater().inflate(R.layout.matchbutton, null);;
                matchButton.setTag(R.id.buttonType,"matchButton");;
                matchButton.setTag(R.id.matchID, match[3]);
                matchButton.setText(match[1]+" vs "+match[2]+" - "+match[0]);
                matchButton.setOnClickListener(btnclick);
                matchButton.setVisibility(View.VISIBLE);

                container.addView(matchButton);
            }
        }

    }

    View.OnClickListener btnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Intent intentSM = new Intent(v.getContext(), StatsMatchActivity.class);
            if(((String) v.getTag(R.id.buttonType)) == "matchButton") {
                long id = Long.valueOf((String) v.getTag(R.id.matchID)).longValue();
                Log.d("ID", String.valueOf(id));
                intentSM.putExtra("match", id);
                startActivity(intentSM);
            }
        }
    };
}
