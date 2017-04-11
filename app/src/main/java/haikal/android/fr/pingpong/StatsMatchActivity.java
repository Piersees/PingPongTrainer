package haikal.android.fr.pingpong;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import com.jjoe64.graphview.Viewport;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.List;

import haikal.android.fr.pingpong.model.Matches;

public class StatsMatchActivity extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private DatabaseHelper dbh;
    final int THUMBSIZE = 400;
    long match_id;

    long latitude, longitude;

    TabHost tabHost;

    TextView startTime, endTime, locLat, locLong, totalSets;

    TextView p1name, p2name;

    TextView p1ScorePt, p2ScorePt, p1SetWon, p2SetWon, p1OOR, p2OOR, p1Aces, p2Aces, p1AtkPt, p2AtkPt, p1DefPt, p2DefPt,
            p1DMS, p1Let, p2DMS, p2Let, p1Beh, p2Beh;

    LinearLayout picContainer;

    private int nbAcesp1, nbAcesp2, nbFoulsp1, nbFoulsp2,nbTot1,nbTotp2, nbAtkp1,nbAtkp2,nbDefp1,nbDefp2;
    private String namep1, namep2;

    GraphView graphAces;
    GraphView graphFouls;
    GraphView graphPercentageAtk;
    GraphView graphPercentageDef;

    private GoogleMap mMap;
    Marker marker;
    private CameraPosition mCameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_match);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Stats");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Stats");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Graphs");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Graphs");
        host.addTab(spec);

        nbFoulsp1=0;
        nbFoulsp2=0;
        nbAcesp1=0;
        nbAcesp2=0;
        nbAtkp1=0;
        nbAtkp2=0;
        nbDefp1=0;
        nbDefp2=0;
        nbTot1=0;
        nbTotp2=0;

        dbh = new DatabaseHelper(getApplicationContext());

        Intent intent = getIntent();
        match_id = intent.getLongExtra("match",0);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //// GET VIEW ELEMENTS
        picContainer = (LinearLayout) findViewById(R.id.picContainer);

        startTime = (TextView) findViewById(R.id.startTime);
        endTime = (TextView) findViewById(R.id.endTime);
        locLat  = (TextView) findViewById(R.id.locLat);
        locLong  = (TextView) findViewById(R.id.locLong);
        totalSets = (TextView) findViewById(R.id.totalSets);

        p1name = (TextView) findViewById(R.id.p1name);
        p2name = (TextView) findViewById(R.id.p2name);

        p1ScorePt = (TextView) findViewById(R.id.p1ScorePt);
        p2ScorePt = (TextView) findViewById(R.id.p2ScorePt);
        p1SetWon = (TextView) findViewById(R.id.p1SetWon);
        p2SetWon = (TextView) findViewById(R.id.p2SetWon);
        p1OOR = (TextView) findViewById(R.id.p1OOR);
        p2OOR = (TextView) findViewById(R.id.p2OOR);
        p1Aces = (TextView) findViewById(R.id.p1Aces);
        p2Aces = (TextView) findViewById(R.id.p2Aces);
        p1AtkPt = (TextView) findViewById(R.id.p1AtkPt);
        p2AtkPt = (TextView) findViewById(R.id.p2AtkPt);
        p1DefPt = (TextView) findViewById(R.id.p1DefPt);
        p2DefPt = (TextView) findViewById(R.id.p2DefPt);
        p1DMS = (TextView) findViewById(R.id.p1DMS);
        p1Let = (TextView) findViewById(R.id.p1Let);
        p2DMS = (TextView) findViewById(R.id.p2DMS);
        p2Let = (TextView) findViewById(R.id.p2Let);
        p1Beh = (TextView) findViewById(R.id.p1Beh);
        p2Beh = (TextView) findViewById(R.id.p2Beh);

        graphAces = (GraphView) findViewById(R.id.graphAces);
        graphFouls = (GraphView) findViewById(R.id.graphFouls);
        graphPercentageAtk = (GraphView) findViewById(R.id.graphPercentageAtk);
        graphPercentageDef = (GraphView) findViewById(R.id.graphPercentageDef);

        this.displayPics();
        this.displayMatchStats();
        //display map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.displayPlayerStats();

        this.displayGraphs();


       //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        //set the marker where the match was
        LatLng position = new LatLng(latitude,longitude);
        marker = mMap.addMarker(new MarkerOptions().position(position));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }

    public void displayPics(){
        List<String> pics = new ArrayList<String>();
        pics = dbh.getPics(match_id);

        // if there aren't any pictures
        if(pics.isEmpty()){
            TextView nopic = new TextView(getApplicationContext());
            nopic.setText(getString(R.string.no_pic_msg));
            picContainer.addView(nopic);
        }else {
            // for each picture
            for (String path : pics) {
                ImageView picture = new ImageView(getApplicationContext());
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path),
                        THUMBSIZE, THUMBSIZE);
                Bitmap fullpic = BitmapFactory.decodeFile(path);
                //fullpic = Bitmap.createScaledBitmap()
                //picture.setImageBitmap(ThumbImage);
                //picture.setMaxWidth(THUMBSIZE);
                picture.setImageBitmap(fullpic);
                picture.setPadding(0,10,0,0);
                picture.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                picture.setVisibility(View.VISIBLE);

                picContainer.addView(picture);
            }
        }
    }

    public void displayMatchStats(){

        String[] matchInfo = dbh.getMatchInfo(match_id);

        startTime.setText(matchInfo[0]);
        endTime.setText(matchInfo[1]);
        locLat.setText(matchInfo[2]);
        locLong.setText(matchInfo[3]);
        totalSets.setText(matchInfo[4]);

        latitude = Long.parseLong(matchInfo[2]);
        longitude = Long.parseLong(matchInfo[3]);
    }

    public void displayPlayerStats(){
        long player1_id;
        long player2_id;
        String[] playerNames = dbh.getPlayerNamesID(match_id);

        // get IDs
        player1_id = Long.parseLong(playerNames[1]);
        player2_id = Long.parseLong(playerNames[3]);

        // display names
        p1name.setText(playerNames[0]);
        namep1 = playerNames[0];
        p2name.setText(playerNames[2]);
        namep2 = playerNames[2];

        int[] player1Stats = dbh.getPlayerStats(match_id, player1_id);
        int[] player2Stats = dbh.getPlayerStats(match_id, player2_id);


        // display stats
        p1ScorePt.setText(String.valueOf(player1Stats[0]));
        nbTot1=player1Stats[0];
        p2ScorePt.setText(String.valueOf(player2Stats[0]));
        nbTotp2=player2Stats[0];

        p1SetWon.setText(String.valueOf(player1Stats[1]));
        p2SetWon.setText(String.valueOf(player2Stats[1]));

        p1Aces.setText(String.valueOf(player1Stats[2]));
        nbAcesp1 = player1Stats[2];
        p2Aces.setText(String.valueOf(player2Stats[2]));
        nbAcesp2 = player2Stats[2];

        p1AtkPt.setText(String.valueOf(player1Stats[3]));
        nbAtkp1=player1Stats[3];
        p2AtkPt.setText(String.valueOf(player2Stats[3]));
        nbAtkp2=player2Stats[3];

        p1Beh.setText(String.valueOf(player1Stats[4]));
        nbFoulsp1=player1Stats[4];
        p2Beh.setText(String.valueOf(player2Stats[4]));
        nbFoulsp2=player2Stats[4];

        p1DefPt.setText(String.valueOf(player1Stats[5]));
        nbDefp1 = player1Stats[5];
        p2DefPt.setText(String.valueOf(player2Stats[5]));
        nbDefp2=player2Stats[5];

        p1DMS.setText(String.valueOf(player1Stats[6]));
        nbFoulsp1+=player1Stats[6];
        p2DMS.setText(String.valueOf(player2Stats[6]));
        nbFoulsp2+=player2Stats[6];

        p1Let.setText(String.valueOf(player1Stats[7]));
        nbFoulsp1+=player1Stats[7];
        p2Let.setText(String.valueOf(player2Stats[7]));
        nbFoulsp2+=player2Stats[7];

        p1OOR.setText(String.valueOf(player1Stats[8]));
        nbFoulsp1+=player1Stats[8];
        p2OOR.setText(String.valueOf(player2Stats[8]));
        nbFoulsp2+=player2Stats[8];

    }

    public void displayGraphs() {
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, nbAcesp1)
        });
        series.setTitle(namep1);
        series.setColor(Color.RED);
        graphAces.addSeries(series);


        BarGraphSeries<DataPoint> series2 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, nbAcesp2)
        });
        series2.setTitle(namep2);
        series2.setColor(Color.BLUE);
        graphAces.addSeries(series2);


        graphAces.getViewport().setXAxisBoundsManual(true);
        graphAces.getViewport().setMinX(0);
        graphAces.getViewport().setMaxX(2);
        graphAces.getViewport().setYAxisBoundsManual(true);
        graphAces.getViewport().setMinY(0);


        BarGraphSeries<DataPoint> series3 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, nbFoulsp1)

        });
        series3.setTitle(namep1);
        series3.setColor(Color.RED);
        graphFouls.addSeries(series3);

        BarGraphSeries<DataPoint> series4 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, nbFoulsp2)
        });
        series4.setTitle(namep2);
        series4.setColor(Color.BLUE);
        graphFouls.addSeries(series4);


        graphFouls.getViewport().setXAxisBoundsManual(true);
        graphFouls.getViewport().setMinX(0);
        graphFouls.getViewport().setMaxX(2);
        graphFouls.getViewport().setYAxisBoundsManual(true);
        graphFouls.getViewport().setMinY(0);

        float percentage;
        if(nbTot1==0 || nbAtkp1 ==0){
            percentage=0;
        }else{
            percentage = (float)nbAtkp1/(float)nbTot1;
            percentage = percentage*100;}



        BarGraphSeries<DataPoint> series5 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, percentage)

        });
        series5.setTitle(namep1);
        series5.setColor(Color.RED);
        graphPercentageAtk.addSeries(series5);

        if(nbTotp2==0 || nbAtkp2 == 0){
            percentage=0;
        }else{
            percentage = (float)nbAtkp2/(float)nbTotp2;
            percentage = percentage*100;
        }

        BarGraphSeries<DataPoint> series6 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, percentage)
        });
        series6.setTitle(namep2);
        series6.setColor(Color.BLUE);
        graphPercentageAtk.addSeries(series6);


        graphPercentageAtk.getViewport().setXAxisBoundsManual(true);
        graphPercentageAtk.getViewport().setMinX(0);
        graphPercentageAtk.getViewport().setMaxX(2);

        graphPercentageAtk.getViewport().setYAxisBoundsManual(true);
        graphPercentageAtk.getViewport().setMinY(0);
        graphPercentageAtk.getViewport().setMaxY(100);

        if(nbDefp1==0 || nbTot1 ==0){
            percentage =0;
        }else {
            percentage = (float)nbDefp1/(float)nbTot1;
            percentage = percentage*100;
        }



        BarGraphSeries<DataPoint> series7 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, percentage)

        });
        series7.setTitle(namep1);
        series7.setColor(Color.RED);
        graphPercentageDef.addSeries(series7);

        if(nbDefp2==0 || nbTotp2 ==0){
            percentage =0;
        }else {
            percentage = (float)nbDefp2 / (float)nbTotp2;
            percentage = percentage * 100;
        }
        BarGraphSeries<DataPoint> series8 = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(1, percentage)
        });
        series8.setTitle(namep2);
        series8.setColor(Color.BLUE);
        graphPercentageDef.addSeries(series8);



        graphPercentageDef.getViewport().setXAxisBoundsManual(true);
        graphPercentageDef.getViewport().setMinX(0);
        graphPercentageDef.getViewport().setMaxX(2);

        graphPercentageDef.getViewport().setYAxisBoundsManual(true);
        graphPercentageDef.getViewport().setMinY(0);
        graphPercentageDef.getViewport().setMaxY(100);



        graphAces.setTitle("Aces");
        graphAces.getLegendRenderer().setVisible(true);
        graphAces.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graphFouls.setTitle(getString(R.string.foul));
        graphFouls.getLegendRenderer().setVisible(true);
        graphFouls.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graphPercentageAtk.setTitle(getString(R.string.percAtk));
        graphPercentageAtk.getLegendRenderer().setVisible(true);
        graphPercentageAtk.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graphPercentageDef.setTitle(getString(R.string.percDef));
        graphPercentageDef.getLegendRenderer().setVisible(true);
        graphPercentageDef.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

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
}
