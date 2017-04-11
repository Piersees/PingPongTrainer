package haikal.android.fr.pingpong;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import haikal.android.fr.pingpong.model.Matches;

public class StatsMatchActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseHelper dbh;
    final int THUMBSIZE = 400;
    long match_id;

    long latitude, longitude;

    TextView startTime, endTime, locLat, locLong, totalSets;

    TextView p1name, p2name;

    TextView p1ScorePt, p2ScorePt, p1SetWon, p2SetWon, p1OOR, p2OOR, p1Aces, p2Aces, p1AtkPt, p2AtkPt, p1DefPt, p2DefPt,
            p1DMS, p1Let, p2DMS, p2Let, p1Beh, p2Beh;

    LinearLayout picContainer;

    private GoogleMap mMap;
    Marker marker;
    private CameraPosition mCameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_match);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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

        this.displayPics();
        this.displayMatchStats();
        //display map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.displayPlayerStats();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        p2name.setText(playerNames[2]);

        int[] player1Stats = dbh.getPlayerStats(match_id, player1_id);
        int[] player2Stats = dbh.getPlayerStats(match_id, player2_id);


        // display stats
        p1ScorePt.setText(String.valueOf(player1Stats[0]));
        p2ScorePt.setText(String.valueOf(player2Stats[0]));

        p1SetWon.setText(String.valueOf(player1Stats[1]));
        p2SetWon.setText(String.valueOf(player2Stats[1]));

        p1Aces.setText(String.valueOf(player1Stats[2]));
        p2Aces.setText(String.valueOf(player2Stats[2]));

        p1AtkPt.setText(String.valueOf(player1Stats[3]));
        p2AtkPt.setText(String.valueOf(player2Stats[3]));

        p1Beh.setText(String.valueOf(player1Stats[4]));
        p2Beh.setText(String.valueOf(player2Stats[4]));

        p1DefPt.setText(String.valueOf(player1Stats[5]));
        p2DefPt.setText(String.valueOf(player2Stats[5]));

        p1DMS.setText(String.valueOf(player1Stats[6]));
        p2DMS.setText(String.valueOf(player2Stats[6]));

        p1Let.setText(String.valueOf(player1Stats[7]));
        p2Let.setText(String.valueOf(player2Stats[7]));

        p1OOR.setText(String.valueOf(player1Stats[8]));
        p2OOR.setText(String.valueOf(player2Stats[8]));


    }

    public void myClickHandler(View view) {

        final Intent intentG = new Intent(this, GraphActivity.class);


        switch (view.getId()) {

            case R.id.graphs:

                startActivity(intentG);

                break;


        }
    }
}
