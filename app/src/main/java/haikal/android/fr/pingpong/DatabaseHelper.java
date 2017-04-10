package haikal.android.fr.pingpong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import haikal.android.fr.pingpong.model.Matches;
import haikal.android.fr.pingpong.model.Sets;


/**
 * Created by Megou on 09/04/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "pingpong";

    // Tables names
    public static final String TABLE_PLAYERS = "Players";
    public static final String TABLE_MATCHES = "Matches";
    public static final String TABLE_SETS = "Sets";
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
    public static final String COLUMN_MATCHES_LOCATION = "location";
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

    /// TABLE CREATE STATEMENTS

        // PLAYERS
        private static final String CREATE_TABLE_PLAYERS = "CREATE TABLE "
                + TABLE_PLAYERS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PLAYERS_NAME + " TEXT" + ")";
        // MATCHES
        private static final String CREATE_TABLE_MATCHES = "CREATE TABLE "
                + TABLE_MATCHES + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_PLAYERONE + " INTEGER,"
                + COLUMN_PLAYERTWO + " INTEGER," + COLUMN_WINNER + " INTEGER,"
                + COLUMN_MATCHES_BEGIN_TIME + " DATETIME,"
                + COLUMN_MATCHES_END_TIME + " DATETIME,"+ COLUMN_MATCHES_LOCATION + " TEXT" + ")";
        // SETS

        private static final String CREATE_TABLE_SETS = "CREATE TABLE "
                + TABLE_SETS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_PLAYERONE + " INTEGER," + COLUMN_PLAYERTWO + " INTEGER,"
                + COLUMN_SETS_SCOREONE + " INTEGER," + COLUMN_SETS_SCORETWO + " INTEGER,"
                + COLUMN_WINNER + " INTEGER," + COLUMN_MATCH + " INTEGER" + ")";


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
        values.put(COLUMN_MATCHES_LOCATION, match.getLocation());

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



}


