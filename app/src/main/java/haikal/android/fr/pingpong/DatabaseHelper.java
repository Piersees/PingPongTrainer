package haikal.android.fr.pingpong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import haikal.android.fr.pingpong.model.Matches;
import haikal.android.fr.pingpong.model.Sets;


/**
 * Created by Megou on 09/04/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private String msg;

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "pingpong";

    // Tables names
    public static final String TABLE_PLAYERS = "Players";
    public static final String TABLE_MATCHES = "Matches";
    public static final String TABLE_SETS = "Sets";
    public static final String TABLE_PICTURES = "Pictures";
        // Special hits
    public static final String TABLE_ATTACK = "Attack_Points";
    public static final String TABLE_DEFENSE = "Defense_Points";
    public static final String TABLE_ACES = "Aces";
        // Fouls
    public static final String TABLE_LETS = "Lets";
    public static final String TABLE_DOUBLE_MISSED_SERVICE = "DoubleMissedServices";
    public static final String TABLE_OUT_OF_RANGE = "OutOfRange";
    public static final String TABLE_BEHAVIOR = "Behavior";




    /// COMMON COLUMNS
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLAYER = "player_id";
    public static final String COLUMN_PLAYERONE = "playerOne_id";
    public static final String COLUMN_PLAYERTWO = "playerTwo_id";
    public static final String COLUMN_WINNER = "winner_id";
    public static final String COLUMN_MATCH = "match_id";
    public static final String COLUMN_SET = "set_id";

    /// PLAYERS - Columns
    public static final String COLUMN_PLAYERS_NAME = "name";

    /// MATCHES - Columns
    public static final String COLUMN_MATCHES_LATITUDE = "latitude";
    public static final String COLUMN_MATCHES_LONGITUDE = "longitude";
    public static final String COLUMN_MATCHES_BEGIN_TIME = "begin_time";
    public static final String COLUMN_MATCHES_END_TIME = "end_time";

    /// SETS - Columns
    public static final String COLUMN_SETS_SCOREONE = "score_one";
    public static final String COLUMN_SETS_SCORETWO = "score_two";

    /// ATTACK - Columns --> ID, PLAYER, MATCH
    /// DEFENSE - Columns --> ID, PLAYER, MATCH
    /// ACES - Columns --> ID, PLAYER, MATCH
    public static final String COLUMN_ACES_BALL_NUMBER = "ball_number";
    /// LETS - Columns --> ID, PLAYER, MATCH
    /// DOUBLE MISSED SERVICE - Columns --> ID, PLAYER, MATCH
    /// OUT OF RANGE - Columns --> ID, PLAYER, MATCH
    /// BEHAVIOR - Columns --> ID, PLAYER, MATCH

    /// PICTURES - Columns
    public static final String COLUMN_PICTURES_PATH = "pic_path";

    /// TABLE CREATE STATEMENTS

    // PLAYERS
        private static final String CREATE_TABLE_PLAYERS = "CREATE TABLE "
                + TABLE_PLAYERS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PLAYERS_NAME + " TEXT" + ")";

    // MATCHES
        private static final String CREATE_TABLE_MATCHES = "CREATE TABLE "
                + TABLE_MATCHES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PLAYERONE + " INTEGER," + COLUMN_PLAYERTWO + " INTEGER," + COLUMN_WINNER + " INTEGER,"
                + COLUMN_MATCHES_BEGIN_TIME + " TEXT," + COLUMN_MATCHES_END_TIME + " TEXT,"
                + COLUMN_MATCHES_LATITUDE + " REAL," + COLUMN_MATCHES_LONGITUDE + " REAL" + ")";

        // SETS

        private static final String CREATE_TABLE_SETS = "CREATE TABLE "
                + TABLE_SETS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PLAYERONE + " INTEGER," + COLUMN_PLAYERTWO + " INTEGER,"
                + COLUMN_SETS_SCOREONE + " INTEGER," + COLUMN_SETS_SCORETWO + " INTEGER,"
                + COLUMN_WINNER + " INTEGER," + COLUMN_MATCH + " INTEGER" + ")";


        // PICTURES
        private static final String CREATE_TABLE_PICTURES = "CREATE TABLE "
                + TABLE_PICTURES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_MATCH + " INTEGER," + COLUMN_PICTURES_PATH + " TEXT" + ")";

        // ATTAQUE

        private static final String CREATE_TABLE_ATTACK = "CREATE TABLE "
                + TABLE_ATTACK + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_SET + " INTEGER,"
                + COLUMN_MATCH + " INTEGER," + COLUMN_PLAYER + " INTEGER" + ")";

        // DEFENSE
        private static final String CREATE_TABLE_DEFENSE = "CREATE TABLE "
                + TABLE_DEFENSE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_SET + " INTEGER,"
                + COLUMN_MATCH + " INTEGER," + COLUMN_PLAYER + " INTEGER" + ")";

    // ACES
    private static final String CREATE_TABLE_ACES = "CREATE TABLE "
            + TABLE_ACES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_SET + " INTEGER," + COLUMN_ACES_BALL_NUMBER + " INTEGER,"
            + COLUMN_MATCH + " INTEGER," + COLUMN_PLAYER + " INTEGER" + ")";

    //LETS
    private static final String CREATE_TABLE_LETS = "CREATE TABLE "
            + TABLE_LETS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_SET + " INTEGER,"
            + COLUMN_MATCH + " INTEGER," + COLUMN_PLAYER + " INTEGER" + ")";

    // DOUBLE MISSED SERVICES
    private static final String CREATE_TABLE_DOUBLE_MISSED_SERVICE = "CREATE TABLE "
            + TABLE_DOUBLE_MISSED_SERVICE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_SET + " INTEGER,"
            + COLUMN_MATCH + " INTEGER," + COLUMN_PLAYER + " INTEGER" + ")";

    // OUT OF RANGE
    private static final String CREATE_TABLE_OUT_OF_RANGE = "CREATE TABLE "
            + TABLE_OUT_OF_RANGE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_SET + " INTEGER,"
            + COLUMN_MATCH + " INTEGER," + COLUMN_PLAYER + " INTEGER" + ")";

    // BEHAVIOR
    private static final String CREATE_TABLE_BEHAVIOR = "CREATE TABLE "
            + TABLE_BEHAVIOR + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
            + COLUMN_SET + " INTEGER,"
            + COLUMN_MATCH + " INTEGER," + COLUMN_PLAYER + " INTEGER" + ")";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        msg = (String) context.getText(R.string.user_exists);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACES);
        db.execSQL(CREATE_TABLE_BEHAVIOR);
        db.execSQL(CREATE_TABLE_ATTACK);
        db.execSQL(CREATE_TABLE_DOUBLE_MISSED_SERVICE);
        db.execSQL(CREATE_TABLE_LETS);
        db.execSQL(CREATE_TABLE_DEFENSE);
        db.execSQL(CREATE_TABLE_MATCHES);
        db.execSQL(CREATE_TABLE_OUT_OF_RANGE);
        db.execSQL(CREATE_TABLE_PLAYERS);
        db.execSQL(CREATE_TABLE_SETS);
        db.execSQL(CREATE_TABLE_PICTURES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop previous tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEHAVIOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTACK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOUBLE_MISSED_SERVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEFENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OUT_OF_RANGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURES);
        //create new tables
        onCreate(db);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public long createMatch(Matches match){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYERONE, match.getPlayer1());
        values.put(COLUMN_PLAYERTWO, match.getPlayer2());
        values.put(COLUMN_MATCHES_BEGIN_TIME, match.getBegin_time());
        values.put(COLUMN_MATCHES_LATITUDE, match.getLatitude());
        values.put(COLUMN_MATCHES_LONGITUDE, match.getLongitude());

        // insert row
        long match_id = db.insert(TABLE_MATCHES, null, values);

        return match_id;
    }
    public long createPlayer(String name){

        long player_id;
        SQLiteDatabase dbr = this.getReadableDatabase();

       // String selectQuery = "SELECT  * FROM " + TABLE_PLAYERS + " WHERE "
               // + COLUMN_PLAYERS_NAME + " = " + name;

       // Log.e(LOG, selectQuery);

        Cursor c = dbr.rawQuery("SELECT  * FROM " + TABLE_PLAYERS + " WHERE "
                + COLUMN_PLAYERS_NAME + " =?", new String[]{name});

        c.moveToFirst();
        // if the player already exists
        if(c!=null && c.getCount()>0){
            player_id = c.getInt(c.getColumnIndex(COLUMN_ID));
            Log.d(TAG, msg);
            return player_id;
        }
        // if the player doesn't exist yet
        else{
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_PLAYERS_NAME, name);

            // insert row
            player_id = db.insert(TABLE_PLAYERS, null, values);
        }
        return player_id;
    }
    public long createSet(Sets set){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MATCH, set.getMatch());
        values.put(COLUMN_PLAYERONE, set.getPlayer1());
        values.put(COLUMN_PLAYERTWO, set.getPlayer2());
        values.put(COLUMN_SETS_SCOREONE, set.getScore1());
        values.put(COLUMN_SETS_SCORETWO, set.getScore2());

        // insert row
        long set_id = db.insert(TABLE_SETS, null, values);

        return set_id;
    }
    public long createAces(long match, long player, long set, int ballnumber){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER, player);
        values.put(COLUMN_MATCH, match);
        values.put(COLUMN_SET, set);
        values.put(COLUMN_ACES_BALL_NUMBER, ballnumber);
        // insert row
        long ace_id = db.insert(TABLE_ACES, null, values);

        return ace_id;
    }
    public long createAttack(long match, long player, long set){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER, player);
        values.put(COLUMN_MATCH, match);
        values.put(COLUMN_SET, set);
        // insert row
        long attack_id = db.insert(TABLE_ATTACK, null, values);

        return attack_id;
    }
    public long createDefense(long match, long player, long set){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER, player);
        values.put(COLUMN_MATCH, match);
        values.put(COLUMN_SET, set);
        // insert row
        long defense_id = db.insert(TABLE_DEFENSE, null, values);

        return defense_id;
    }
    public long createBehavior(long match, long player, long set){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER, player);
        values.put(COLUMN_MATCH, match);
        values.put(COLUMN_SET, set);
        // insert row
        long behavior_id = db.insert(TABLE_BEHAVIOR, null, values);

        return behavior_id;
    }
    public long createLets(long match, long player, long set){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER, player);
        values.put(COLUMN_MATCH, match);
        values.put(COLUMN_SET, set);
        // insert row
        long lets_id = db.insert(TABLE_LETS, null, values);

        return lets_id;
    }
    public long createOutOfRange(long match, long player, long set){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER, player);
        values.put(COLUMN_MATCH, match);
        values.put(COLUMN_SET, set);

        // insert row
        long oor_id = db.insert(TABLE_OUT_OF_RANGE, null, values);

        return oor_id;
    }
    public long createDoubleMissedService(long match, long player, long set){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PLAYER, player);
        values.put(COLUMN_MATCH, match);
        values.put(COLUMN_SET, set);
        // insert row
        long dms_id = db.insert(TABLE_DOUBLE_MISSED_SERVICE, null, values);

        return dms_id;
    }
    public long createPicture(long match, String path){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MATCH, match);
        values.put(COLUMN_PICTURES_PATH, path);
        // insert row
        long pic_id = db.insert(TABLE_PICTURES, null, values);

        return pic_id;
    }

    public void updateSet(Sets set){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SETS_SCOREONE, set.getScore1());
        values.put(COLUMN_SETS_SCORETWO, set.getScore2());
        values.put(COLUMN_WINNER, set.getWinner());

        // updating row
        db.update(TABLE_SETS, values, COLUMN_ID + " = ?",
                new String[]{ String.valueOf(set.getId())});
    }
    public void updateMatch(Matches match){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MATCHES_END_TIME, match.getEnd_time());
        values.put(COLUMN_WINNER, match.getWinner_id());

        // updating row
        db.update(TABLE_MATCHES, values, COLUMN_ID + " = ?",
                new String[]{ String.valueOf(match.getId())});
    }

    public List<String> getPics(long match_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_PICTURES_PATH};

        Cursor result = db.query(TABLE_PICTURES, columns, COLUMN_MATCH+"=?",
                new String[]{String.valueOf(match_id)}, null, null, null);
        //Cursor result = db.rawQuery("SELECT  * FROM " + TABLE_PICTURES + " WHERE "
               // + COLUMN_MATCH + " =?", new String[]{match_id});

        List<String> pics = new ArrayList<String>();

        result.moveToFirst();
        // while there are results
        while (!result.isAfterLast()) {
            // add image path to list
            pics.add(result.getString(0));
            result.moveToNext();
        }
        result.close();

        return pics;
    }

    public String[] getMatchInfo(long match_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_MATCHES_BEGIN_TIME,COLUMN_MATCHES_END_TIME,COLUMN_MATCHES_LATITUDE,COLUMN_MATCHES_LONGITUDE};

        Cursor result = db.query(TABLE_MATCHES, columns, COLUMN_ID+"=?",
                new String[]{String.valueOf(match_id)}, null, null, null);
        //Cursor result = db.rawQuery("SELECT  * FROM " + TABLE_PICTURES + " WHERE "
        // + COLUMN_MATCH + " =?", new String[]{match_id});

        String nbSets = getNbSets(match_id);

        String[] matchInfo = new String[5];
        result.moveToFirst();

        // while there are results
       if(result!=null && result.getCount()>0){
            // add image path to list
           matchInfo[0]=result.getString(0);
           matchInfo[1]=result.getString(1);
           matchInfo[2]=String.valueOf(result.getLong(2));
           matchInfo[3]=String.valueOf(result.getLong(3));
           matchInfo[4]=nbSets;
        }
        result.close();

        return matchInfo;
    }

    public String getNbSets(long match_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String nbSets;
        Cursor result = db.query(TABLE_SETS, null, COLUMN_MATCH+"=?",
                new String[]{String.valueOf(match_id)}, null, null, null);
        nbSets = String.valueOf(result.getCount());
        return nbSets;
    }

    public String[] getPlayerNamesID(long match_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String playerNamesID[] = new String[4];

        long id1, id2;

        // get players ID
        String[] columns = {COLUMN_PLAYERONE, COLUMN_PLAYERTWO};

        Cursor result = db.query(TABLE_MATCHES, columns, COLUMN_ID+"=?",
                new String[]{String.valueOf(match_id)}, null, null, null);

        result.moveToFirst();
        // while there are results
        if(result!=null && result.getCount()>0){
            id1 = result.getLong(0);
            id2 = result.getLong(1);
            playerNamesID[1]=String.valueOf(id1);
            playerNamesID[3]=String.valueOf(id2);

            // get player names

            String[] columnName = {COLUMN_PLAYERS_NAME};
            Cursor name1 = db.query(TABLE_PLAYERS, columnName, COLUMN_ID+"=?",
                    new String[]{String.valueOf(id1)}, null, null, null);
            Cursor name2 = db.query(TABLE_PLAYERS, columnName, COLUMN_ID+"=?",
                    new String[]{String.valueOf(id2)}, null, null, null);

            name1.moveToFirst();
            name2.moveToFirst();

            if(name1!=null && name1.getCount()>0){
                playerNamesID[0]=name1.getString(0);
            }
            if(name2!=null && name2.getCount()>0){
                playerNamesID[2]=name2.getString(0);
            }
            name1.close();
            name2.close();
        }
        result.close();


        return playerNamesID;
    }

    public int[] getPlayerStats(long match_id, long player_id){
        SQLiteDatabase db = this.getReadableDatabase();
        int playerStats[] = new int[9];

        String[] Stats = {TABLE_ACES, TABLE_ATTACK, TABLE_BEHAVIOR, TABLE_DEFENSE, TABLE_DOUBLE_MISSED_SERVICE, TABLE_LETS, TABLE_OUT_OF_RANGE};

        playerStats[0]=getTotalScore(match_id, player_id);
        playerStats[1]=getSetsWon(match_id, player_id);
        int i = 2;
        for (String statTable : Stats) {
            playerStats[i]=getStat(match_id, player_id, statTable);
            i++;
        }

        return playerStats;
    }

    public int getTotalScore(long match_id, long player_id){
        int totalScore=0;
        SQLiteDatabase db = this.getReadableDatabase();

        // player as player1

        String[] col1 = {COLUMN_SETS_SCOREONE};
        Cursor result1 = db.query(TABLE_SETS,
                null,
                COLUMN_MATCH+"=? AND "+COLUMN_PLAYERONE+"=?",
                new String[]{String.valueOf(match_id), String.valueOf(player_id)}, null, null, null);

        if(result1.moveToFirst()){
            // while there are results
            while (!result1.isAfterLast()) {
                totalScore= totalScore + (result1.getInt(3));
                result1.moveToNext();
            }
            result1.close();
        } else{

        }


        //player as player2

        String[] col2 = {COLUMN_SETS_SCORETWO};
        Cursor result2 = db.query(TABLE_SETS,
                null,
                COLUMN_MATCH+"=? AND "+COLUMN_PLAYERTWO+"=?",
                new String[]{String.valueOf(match_id), String.valueOf(player_id)}, null, null, null);
        result2.moveToFirst();
        // while there are results
        while (!result2.isAfterLast()) {
            totalScore = totalScore+(result2.getInt(4));
            result2.moveToNext();
        }
        result2.close();

        return totalScore;
    }
    public int getSetsWon(long match_id, long player_id){
        int setsWon;
        SQLiteDatabase db = this.getReadableDatabase();

        // get set instances
        Cursor result = db.query(TABLE_SETS, null, COLUMN_MATCH+"=? AND "+COLUMN_WINNER+"=?",
                new String[]{String.valueOf(match_id), String.valueOf(player_id)}, null, null, null);

        result.moveToFirst();

        if(result!=null && result.getCount()>0){
            setsWon = result.getCount();
        }else{
            setsWon = 0;
        }
        return setsWon;
    }
    public int getStat(long match_id, long player_id, String statTable){
        int stat;
        SQLiteDatabase db = this.getReadableDatabase();

        // get stat instances
        Cursor result = db.query(statTable, null, COLUMN_MATCH+"=? AND "+COLUMN_PLAYER+"=?",
                new String[]{String.valueOf(match_id), String.valueOf(player_id)}, null, null, null);

        result.moveToFirst();
        if(result!=null && result.getCount()>0){
            stat = result.getCount();
        }else{
            stat = 0;
        }
        return stat;
    }

}


