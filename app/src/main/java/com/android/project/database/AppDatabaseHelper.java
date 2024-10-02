package com.android.project.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.project.model.Attendance;
import com.android.project.model.Job;
import com.android.project.model.Labourer;
import com.android.project.model.RozgarSewak;
import com.android.project.utility.Constants;
import com.android.project.utility.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class AppDatabaseHelper extends SQLiteOpenHelper {


    public AppDatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_SEWAK_TABLE = "CREATE TABLE " + Constants.ROZGARSEWAK_TABLE_NAME + "(" +
                Constants.ID_KEY + " INTEGER PRIMARY KEY," +
                Constants.ROZGARSEWAK_NAME_KEY + " TEXT," +
                Constants.ROZGARSEWAK_CONTACT_KEY + " TEXT," +
                Constants.ROZGARSEWAK_USERNAME_KEY + " TEXT," +
                Constants.ROZGARSEWAK_PASSWORD_KEY + " TEXT," +
                Constants.ROZGARSEWAK_PANCHAYATH_KEY + " TEXT," +
                Constants.ROZGARSEWAK_PROFILEPATH_KEY + " TEXT " +
                ")";

        String create_labourer_table_query = "CREATE TABLE " + Constants.LABOURER_TABLE_NAME + "(" +
                Constants.ID_KEY + " INTEGER PRIMARY KEY," +
                Constants.LABOURER_NAME_COLUMN + " TEXT," +
                Constants.LABOURER_MOBILE_COLUMN + " TEXT," +
                Constants.LABOURER_USERNAME_COLUMN + " TEXT," +
                Constants.LABOURER_PASSWORD_COLUMN + " TEXT," +
                Constants.LABOURER_PROFILEPATH_COLUMN + " TEXT," +
                Constants.LABOURER_AGE_COLUMN + " INTEGER," +
                Constants.LABOURER_ISAPPROVED_COLUMN + " INTEGER," +
                Constants.LABOURER_IS_JOBCARD_ISSUED_COLUMN + " INTEGER," +
                Constants.LABOURER_VOTER_ID_COLUMN + " TEXT," +
                Constants.LABOURER_AADHAR_COLUMN + " TEXT," +
                Constants.LABOURER_BPL_CARD_COLUMN + " TEXT," +
                Constants.LABOURER_PANCHAYATH_COLUMN + " TEXT," +
                Constants.LABOURER_ALLOTTED_FOR_JOB_COLUMN + " INTEGER," +
                Constants.LABOURER_BANK_COLUMN + " TEXT," +
                Constants.LABOURER_IFSC_COLUMN + " TEXT," +
                Constants.LABOURER_ACCOUNT_COLUMN + " TEXT," +

                Constants.LABOURER_IS_REGISTERED_FOR_SCHEME_COLUMN + " INTEGER," +
                Constants.LABOURER_GENDER_COLUMN + " TEXT " +
                ")";


        String create_job_table_query = "CREATE TABLE " + Constants.JOB_TABLE_NAME + "(" +
                Constants.ID_KEY + " INTEGER PRIMARY KEY," +
                Constants.JOB_TITLE_KEY + " TEXT," +
                Constants.JOB_DESCRIPTION_KEY + " TEXT," +
                Constants.JOB_DURATION_KEY + " INTEGER," +
                Constants.JOB_FUNDS_ALLOTTED + " TEXT," +
                Constants.JOB_NUMBER_OF_LABOURERS + " INTEGER" +
                ")";

        String create_attendance_table_query = "CREATE TABLE " + Constants.ATTENDANCE_TABLE_NAME + "(" +
                Constants.ID_KEY + " INTEGER PRIMARY KEY," +
                Constants.ATTENDANCE_DATE_KEY + " TEXT," +
                Constants.LABOURER_ID_KEY + " INTEGER," +
                Constants.JOB_ID_KEY + " INTEGER " +
                ")";

        String CREATE_JOB_LABOURER_TABLE = "CREATE TABLE " + Constants.JOB_LABOURER_TABLE_NAME + "("
                + Constants.ID_KEY + " INTEGER PRIMARY KEY," +
                Constants.LABOURER_ID_KEY + " INTEGER," +
        Constants.BEFORE_PHOTO_KEY + " TEXT," +
        Constants.AFTER_PHOTO_KEY + " TEXT," +
        Constants.DATE_KEY + " TEXT,"

                + Constants.JOB_ID_KEY + " INTEGER" +  ")";

        sqLiteDatabase.execSQL(create_labourer_table_query);
        sqLiteDatabase.execSQL(create_job_table_query);
        sqLiteDatabase.execSQL(create_attendance_table_query);
        sqLiteDatabase.execSQL(CREATE_JOB_LABOURER_TABLE);

        sqLiteDatabase.execSQL(CREATE_SEWAK_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //adding labourer
    public int addLabourer(Labourer labourer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Inserting Row to labourer table
        values.put(Constants.LABOURER_NAME_COLUMN, labourer.getName());

        values.put(Constants.LABOURER_PASSWORD_COLUMN, labourer.getPassword());
        values.put(Constants.LABOURER_PROFILEPATH_COLUMN, labourer.getProfilePath());

        values.put(Constants.LABOURER_USERNAME_COLUMN, labourer.getUsername());
        values.put(Constants.LABOURER_MOBILE_COLUMN, labourer.getMobile());
        values.put(Constants.LABOURER_AGE_COLUMN, labourer.getAge());
        values.put(Constants.LABOURER_GENDER_COLUMN, labourer.getGender());
        values.put(Constants.LABOURER_ISAPPROVED_COLUMN, labourer.isApproved());
        values.put(Constants.LABOURER_IS_JOBCARD_ISSUED_COLUMN, labourer.isJobCardIssued());
        values.put(Constants.LABOURER_PANCHAYATH_COLUMN, labourer.getGramPanchayath());
        values.put(Constants.LABOURER_ALLOTTED_FOR_JOB_COLUMN, labourer.isAllottedForJob());
        values.put(Constants.LABOURER_BANK_COLUMN, labourer.getBank());
        values.put(Constants.LABOURER_IFSC_COLUMN, labourer.getIfsc());
        values.put(Constants.LABOURER_ACCOUNT_COLUMN, labourer.getAccount());

        int labourID = (int) db.insert(Constants.LABOURER_TABLE_NAME, null, values);
        db.close();
        return labourID;
    }

    public int addJob(Job job) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Inserting Row to labourer table
        values.put(Constants.JOB_TITLE_KEY, job.getTitle());

        values.put(Constants.JOB_DESCRIPTION_KEY, job.getDescription());
        values.put(Constants.JOB_DURATION_KEY, job.getDurationInDays());

        values.put(Constants.JOB_FUNDS_ALLOTTED, job.getFundsAllotted());
        values.put(Constants.JOB_NUMBER_OF_LABOURERS, job.getNumberOfLabourers());

        int jobID = (int) db.insert(Constants.JOB_TABLE_NAME, null, values);
        db.close();
        return jobID;
    }
    public Labourer getLabourerWithUsernameAndPassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[]{
                Constants.ID_KEY,
                Constants.LABOURER_NAME_COLUMN,
                Constants.LABOURER_MOBILE_COLUMN,
                Constants.LABOURER_AGE_COLUMN,
                Constants.LABOURER_GENDER_COLUMN,
                Constants.LABOURER_PANCHAYATH_COLUMN,
                Constants.LABOURER_USERNAME_COLUMN,
                Constants.LABOURER_PASSWORD_COLUMN,
                Constants.LABOURER_ISAPPROVED_COLUMN,
                Constants.LABOURER_IS_REGISTERED_FOR_SCHEME_COLUMN,
                Constants.LABOURER_IS_JOBCARD_ISSUED_COLUMN,
                Constants.LABOURER_ALLOTTED_FOR_JOB_COLUMN,
                Constants.LABOURER_VOTER_ID_COLUMN,
                Constants.LABOURER_BPL_CARD_COLUMN,
                Constants.LABOURER_AADHAR_COLUMN,
                Constants.LABOURER_BANK_COLUMN,
                Constants.LABOURER_IFSC_COLUMN,
                Constants.LABOURER_ACCOUNT_COLUMN,
                Constants.LABOURER_PROFILEPATH_COLUMN};


        String where = Constants.LABOURER_USERNAME_COLUMN + " =?" + " AND " + Constants.LABOURER_PASSWORD_COLUMN + " =?";

        Cursor cursor = db.query(Constants.LABOURER_TABLE_NAME, columns, where,
                new String[]{username, password}, null, null, null, null);
        Labourer labourer = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_NAME_COLUMN));
            long labourerID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));

            String contact = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_MOBILE_COLUMN));
            String profilePath = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PROFILEPATH_COLUMN));
            String gender = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_GENDER_COLUMN));
            int age = cursor.getInt(cursor.getColumnIndex(Constants.LABOURER_AGE_COLUMN));
            String panchayath = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PANCHAYATH_COLUMN));
            String aadhar = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_AADHAR_COLUMN));
            String voterID = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_VOTER_ID_COLUMN));
            String bplcard = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_BPL_CARD_COLUMN));
            String bank = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_BANK_COLUMN));
            String ifsc = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_IFSC_COLUMN));
            String account = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_ACCOUNT_COLUMN));
            boolean isApproved = (cursor.getInt(cursor.getColumnIndex(Constants.LABOURER_ISAPPROVED_COLUMN)) ==1);
            boolean isRegisteredForScheme = (cursor.getInt(cursor.getColumnIndex(Constants.LABOURER_IS_REGISTERED_FOR_SCHEME_COLUMN)) ==1);
            boolean isAllottedForJob = (cursor.getInt(cursor.getColumnIndex(Constants.LABOURER_ALLOTTED_FOR_JOB_COLUMN)) ==1);
            boolean isJobCardIssued = (cursor.getInt(cursor.getColumnIndex(Constants.LABOURER_IS_JOBCARD_ISSUED_COLUMN)) ==1);


            labourer = new Labourer();
            labourer.setLabourerID(labourerID);
            labourer.setName(name);
            labourer.setMobile(contact);
            labourer.setUsername(username);
            labourer.setPassword(password);
            labourer.setGender(gender);
            labourer.setAge(age);
            labourer.setGramPanchayath(panchayath);
            labourer.setAadharID(aadhar);
            labourer.setVoterID(voterID);
            labourer.setBplCardNumber(bplcard);
            labourer.setAllottedForJob(isAllottedForJob);
            labourer.setBank(bank);
            labourer.setIfsc(ifsc);
            labourer.setAccount(account);
            labourer.setRegisteredForScheme(isRegisteredForScheme);
            labourer.setProfilePath(profilePath);
            labourer.setApproved(isApproved);
            labourer.setJobCardIssued(isJobCardIssued);
        }

        db.close();
        return labourer;

    }

    public Labourer getLabourerWithUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[]{
                Constants.LABOURER_NAME_COLUMN,
                Constants.LABOURER_MOBILE_COLUMN,
                Constants.LABOURER_PASSWORD_COLUMN,
                Constants.ID_KEY
        };

        String where = Constants.LABOURER_USERNAME_COLUMN + " =?";

        Cursor cursor = db.query(Constants.LABOURER_TABLE_NAME, columns, where,
                new String[]{username}, null, null, null, null);
        Labourer labourer = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_NAME_COLUMN));
            String mobile = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_MOBILE_COLUMN));
            String password = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PASSWORD_COLUMN));

            int labourID = cursor.getInt(cursor.getColumnIndex(Constants.ID_KEY));

            labourer = new Labourer();
            labourer.setLabourerID(labourID);
            labourer.setName(name);
            labourer.setMobile(mobile);
            labourer.setUsername(username);
            labourer.setPassword(password);

        }

        db.close();
        return labourer;

    }


    public RozgarSewak getRozgarSewakWithUsernameAndPassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[]{
                Constants.ROZGARSEWAK_NAME_KEY,
                Constants.ROZGARSEWAK_CONTACT_KEY,
                Constants.ID_KEY
        };

        String where = Constants.ROZGARSEWAK_USERNAME_KEY + " =?" + " AND " + Constants.ROZGARSEWAK_PASSWORD_KEY + " =?";

        Cursor cursor = db.query(Constants.ROZGARSEWAK_TABLE_NAME, columns, where,
                new String[]{username, password}, null, null, null, null);
        RozgarSewak rozgarSewak = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(Constants.ROZGARSEWAK_NAME_KEY));
            String mobile = cursor.getString(cursor.getColumnIndex(Constants.ROZGARSEWAK_CONTACT_KEY));
            int sewakID = cursor.getInt(cursor.getColumnIndex(Constants.ID_KEY));

            rozgarSewak = new RozgarSewak();
            rozgarSewak.setSewakID(sewakID);
            rozgarSewak.setName(name);
            rozgarSewak.setMobile(mobile);
            rozgarSewak.setUsername(username);
            rozgarSewak.setPassword(password);

        }

        db.close();
        return rozgarSewak;

    }

    public RozgarSewak getRozgarSewakWithUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[]{
                Constants.ROZGARSEWAK_NAME_KEY,
                Constants.ROZGARSEWAK_PASSWORD_KEY,
                Constants.ROZGARSEWAK_CONTACT_KEY,
                Constants.ID_KEY
        };

        String where = Constants.ROZGARSEWAK_USERNAME_KEY + " =?";

        Cursor cursor = db.query(Constants.ROZGARSEWAK_TABLE_NAME, columns, where,
                new String[]{username}, null, null, null, null);
        RozgarSewak rozgarSewak = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndex(Constants.ROZGARSEWAK_NAME_KEY));
            String mobile = cursor.getString(cursor.getColumnIndex(Constants.ROZGARSEWAK_CONTACT_KEY));
            String password = cursor.getString(cursor.getColumnIndex(Constants.ROZGARSEWAK_PASSWORD_KEY));
            int sewakID = cursor.getInt(cursor.getColumnIndex(Constants.ID_KEY));

            rozgarSewak = new RozgarSewak();
            rozgarSewak.setSewakID(sewakID);
            rozgarSewak.setName(name);
            rozgarSewak.setMobile(mobile);
            rozgarSewak.setUsername(username);
            rozgarSewak.setPassword(password);

        }

        db.close();
        return rozgarSewak;

    }

    public long addRozgarSewak(RozgarSewak rozgarSewak)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.ROZGARSEWAK_NAME_KEY, rozgarSewak.getName());
        values.put(Constants.ROZGARSEWAK_USERNAME_KEY, rozgarSewak.getUsername());
        values.put(Constants.ROZGARSEWAK_PASSWORD_KEY, rozgarSewak.getPassword());
        values.put(Constants.ROZGARSEWAK_PROFILEPATH_KEY, rozgarSewak.getProfilePath());
        values.put(Constants.ROZGARSEWAK_CONTACT_KEY, rozgarSewak.getMobile());
        values.put(Constants.ROZGARSEWAK_PANCHAYATH_KEY, rozgarSewak.getGramPanchayath());

        long sewakID = db.insert(Constants.ROZGARSEWAK_TABLE_NAME, null, values);

        db.close();
        return sewakID;
    }

    public ArrayList<String> getAllRozgarSewakUserNames()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.ROZGARSEWAK_TABLE_NAME;
        String[] columns = new String[]{Constants.ROZGARSEWAK_USERNAME_KEY};

        Cursor cursor = db.query(table, columns, null,
                null, null, null, null, null);

        ArrayList<String> sewakUserNames = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndex(Constants.ROZGARSEWAK_USERNAME_KEY));
                sewakUserNames.add(username);
            } while (cursor.moveToNext());
        }

        db.close();
        return sewakUserNames;
    }

    public ArrayList<String> getAllLabourerUserNames()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.LABOURER_TABLE_NAME;
        String[] columns = new String[]{Constants.LABOURER_USERNAME_COLUMN};

        Cursor cursor = db.query(table, columns, null,
                null, null, null, null, null);

        ArrayList<String> labourerUserNames = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_USERNAME_COLUMN));
                labourerUserNames.add(username);
            } while (cursor.moveToNext());
        }

        db.close();
        return labourerUserNames;
    }

    public Labourer getLabourerWithID(long labourerID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.LABOURER_TABLE_NAME;

        String[] columns = new String[]{
                Constants.LABOURER_NAME_COLUMN,
                Constants.LABOURER_MOBILE_COLUMN,
                Constants.LABOURER_AGE_COLUMN,
                Constants.LABOURER_GENDER_COLUMN,
                Constants.LABOURER_PANCHAYATH_COLUMN,
                Constants.LABOURER_USERNAME_COLUMN,
                Constants.LABOURER_PASSWORD_COLUMN,
                Constants.LABOURER_ISAPPROVED_COLUMN,
                Constants.LABOURER_BANK_COLUMN,
                Constants.LABOURER_IFSC_COLUMN,
                Constants.LABOURER_AADHAR_COLUMN,
                Constants.LABOURER_BPL_CARD_COLUMN,
                Constants.LABOURER_VOTER_ID_COLUMN,
                Constants.LABOURER_ACCOUNT_COLUMN,
                Constants.LABOURER_PROFILEPATH_COLUMN};
        String where = Constants.ID_KEY + " =?";

        Cursor cursor = db.query(table, columns, where,
                new String[]{String.valueOf(labourerID)}, null, null, null, null);
        Labourer labourer = null;
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();

            String name = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_NAME_COLUMN));
            String password = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PASSWORD_COLUMN));
            String username = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_USERNAME_COLUMN));

            String contact = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_MOBILE_COLUMN));
            String profilePath = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PROFILEPATH_COLUMN));
            String gender = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_GENDER_COLUMN));
            int age = cursor.getInt(cursor.getColumnIndex(Constants.LABOURER_AGE_COLUMN));
            String panchayath = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PANCHAYATH_COLUMN));
            String bank = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_BANK_COLUMN));
            String ifsc = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_IFSC_COLUMN));
            String account = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_ACCOUNT_COLUMN));
            String aadhar = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_AADHAR_COLUMN));
            String bpl = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_BPL_CARD_COLUMN));
            String voterid = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_VOTER_ID_COLUMN));

            boolean isApproved = (cursor.getInt(cursor.getColumnIndex(Constants.LABOURER_ISAPPROVED_COLUMN)) ==1);


            labourer = new Labourer();
            labourer.setLabourerID(labourerID);
            labourer.setName(name);
            labourer.setMobile(contact);
            labourer.setUsername(username);
            labourer.setPassword(password);
            labourer.setGender(gender);
            labourer.setAge(age);
            labourer.setGramPanchayath(panchayath);
            labourer.setBank(bank);
            labourer.setIfsc(ifsc);
            labourer.setAccount(account);
            labourer.setAadharID(aadhar);
            labourer.setVoterID(voterid);
            labourer.setBplCardNumber(bpl);

            labourer.setProfilePath(profilePath);
            labourer.setApproved(isApproved);
        }
        db.close();
        return labourer;
    }

    public int updateLabourerStatus(Labourer labourer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.LABOURER_ISAPPROVED_COLUMN , labourer.isApproved());

        // Updating row
        return db.update(Constants.LABOURER_TABLE_NAME, values, Constants.ID_KEY + "=?",
                new String[]{String.valueOf(labourer.getLabourerID())});
    }

    public int updatedJobAllottedStatusForLabourer(Labourer labourer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.LABOURER_ALLOTTED_FOR_JOB_COLUMN , labourer.isAllottedForJob());

        // Updating row
        return db.update(Constants.LABOURER_TABLE_NAME, values, Constants.ID_KEY + "=?",
                new String[]{String.valueOf(labourer.getLabourerID())});
    }





    public int registerLabourerForScheme(Labourer labourer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.LABOURER_AADHAR_COLUMN , labourer.getAadharID());
        values.put(Constants.LABOURER_BPL_CARD_COLUMN , labourer.getBplCardNumber());
        values.put(Constants.LABOURER_VOTER_ID_COLUMN , labourer.getVoterID());
        values.put(Constants.LABOURER_IS_REGISTERED_FOR_SCHEME_COLUMN , labourer.isRegisteredForScheme());

        // Updating row
        return db.update(Constants.LABOURER_TABLE_NAME, values, Constants.ID_KEY + "=?",
                new String[]{String.valueOf(labourer.getLabourerID())});
    }
    public int updateLabourerJobCardStatus(Labourer labourer){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.LABOURER_IS_JOBCARD_ISSUED_COLUMN , labourer.isJobCardIssued());

        // Updating row
        return db.update(Constants.LABOURER_TABLE_NAME, values, Constants.ID_KEY + "=?",
                new String[]{String.valueOf(labourer.getLabourerID())});
    }


    public ArrayList<Labourer> getRegisteredLabourerList()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.LABOURER_TABLE_NAME;

        String[] columns = new String[]{Constants.ID_KEY,
                Constants.LABOURER_NAME_COLUMN,
                Constants.LABOURER_USERNAME_COLUMN,
                Constants.LABOURER_PASSWORD_COLUMN,
                Constants.LABOURER_ISAPPROVED_COLUMN,
                Constants.LABOURER_MOBILE_COLUMN,
                Constants.LABOURER_PROFILEPATH_COLUMN};

        String where_clause = Constants.LABOURER_ISAPPROVED_COLUMN + " =0 AND " + Constants.LABOURER_IS_REGISTERED_FOR_SCHEME_COLUMN + " =1";


        Cursor cursor = db.query(table, columns, where_clause,
                null, null, null, null, null);
        ArrayList<Labourer> labourerList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {

                long beneficiaryID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));

                String name = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_NAME_COLUMN));
                String contact = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_MOBILE_COLUMN));
                String profilePath = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PROFILEPATH_COLUMN));
                String userName = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_USERNAME_COLUMN));
                String password = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PASSWORD_COLUMN));

                boolean isApproved = (cursor.getInt(cursor.getColumnIndex(Constants.LABOURER_ISAPPROVED_COLUMN)) ==1);


                Labourer labourer = new Labourer();
                labourer.setLabourerID(beneficiaryID);
                labourer.setName(name);
                labourer.setMobile(contact);
                labourer.setUsername(userName);
                labourer.setPassword(password);
                labourer.setProfilePath(profilePath);
                labourer.setApproved(isApproved);
                labourerList.add(labourer);
            }while (cursor.moveToNext());

        }

        db.close();
        return labourerList;
    }


    public ArrayList<Labourer> getJobAllottedLabourerList()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.LABOURER_TABLE_NAME;

        String[] columns = new String[]{Constants.ID_KEY,
                Constants.LABOURER_NAME_COLUMN,
                Constants.LABOURER_USERNAME_COLUMN,
                Constants.LABOURER_PASSWORD_COLUMN,
                Constants.LABOURER_ISAPPROVED_COLUMN,
                Constants.LABOURER_MOBILE_COLUMN,
                Constants.LABOURER_PROFILEPATH_COLUMN};
        String where_clause = Constants.LABOURER_ALLOTTED_FOR_JOB_COLUMN + " =1";

        Cursor cursor = db.query(table, columns, where_clause,
                null, null, null, null, null);
        ArrayList<Labourer> labourerList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {

                long beneficiaryID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));

                String name = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_NAME_COLUMN));
                String contact = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_MOBILE_COLUMN));
                String profilePath = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PROFILEPATH_COLUMN));
                String userName = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_USERNAME_COLUMN));
                String password = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PASSWORD_COLUMN));

                boolean isApproved = (cursor.getInt(cursor.getColumnIndex(Constants.LABOURER_ISAPPROVED_COLUMN)) ==1);


                Labourer labourer = new Labourer();
                labourer.setLabourerID(beneficiaryID);
                labourer.setName(name);
                labourer.setMobile(contact);
                labourer.setUsername(userName);
                labourer.setPassword(password);
                labourer.setProfilePath(profilePath);
                labourer.setApproved(isApproved);
                labourerList.add(labourer);
            }while (cursor.moveToNext());

        }

        db.close();
        return labourerList;
    }

    public ArrayList<Labourer> getApprovedLabourerList()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.LABOURER_TABLE_NAME;

        String[] columns = new String[]{Constants.ID_KEY,
                Constants.LABOURER_NAME_COLUMN,
                Constants.LABOURER_USERNAME_COLUMN,
                Constants.LABOURER_PASSWORD_COLUMN,
                Constants.LABOURER_ISAPPROVED_COLUMN,
                Constants.LABOURER_MOBILE_COLUMN,
                Constants.LABOURER_PROFILEPATH_COLUMN};
        String where_clause = Constants.LABOURER_ISAPPROVED_COLUMN + " =1";

        Cursor cursor = db.query(table, columns, where_clause,
                null, null, null, null, null);
        ArrayList<Labourer> labourerList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {

                long beneficiaryID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));

                String name = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_NAME_COLUMN));
                String contact = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_MOBILE_COLUMN));
                String profilePath = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PROFILEPATH_COLUMN));
                String userName = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_USERNAME_COLUMN));
                String password = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PASSWORD_COLUMN));

                boolean isApproved = (cursor.getInt(cursor.getColumnIndex(Constants.LABOURER_ISAPPROVED_COLUMN)) ==1);


                Labourer labourer = new Labourer();
                labourer.setLabourerID(beneficiaryID);
                labourer.setName(name);
                labourer.setMobile(contact);
                labourer.setUsername(userName);
                labourer.setPassword(password);
                labourer.setProfilePath(profilePath);
                labourer.setApproved(isApproved);
                labourerList.add(labourer);
            }while (cursor.moveToNext());

        }

        db.close();
        return labourerList;
    }


    public ArrayList<Labourer> getLabourersIssuedWithJobCard()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.LABOURER_TABLE_NAME;

        String[] columns = new String[]{Constants.ID_KEY,
                Constants.LABOURER_NAME_COLUMN,
                Constants.LABOURER_USERNAME_COLUMN,
                Constants.LABOURER_PASSWORD_COLUMN,
                Constants.LABOURER_ISAPPROVED_COLUMN,
                Constants.LABOURER_MOBILE_COLUMN,
                Constants.LABOURER_PROFILEPATH_COLUMN};
        String where_clause = Constants.LABOURER_ISAPPROVED_COLUMN + " =1 AND " + Constants.LABOURER_IS_REGISTERED_FOR_SCHEME_COLUMN + " =1 AND " + Constants.LABOURER_IS_JOBCARD_ISSUED_COLUMN + " =1 AND " + Constants.LABOURER_ALLOTTED_FOR_JOB_COLUMN + " =0";

        Cursor cursor = db.query(table, columns, where_clause,
                null, null, null, null, null);
        ArrayList<Labourer> labourerList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {

                long beneficiaryID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));

                String name = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_NAME_COLUMN));
                String contact = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_MOBILE_COLUMN));
                String profilePath = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PROFILEPATH_COLUMN));
                String userName = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_USERNAME_COLUMN));
                String password = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PASSWORD_COLUMN));

                boolean isApproved = (cursor.getInt(cursor.getColumnIndex(Constants.LABOURER_ISAPPROVED_COLUMN)) ==1);


                Labourer labourer = new Labourer();
                labourer.setLabourerID(beneficiaryID);
                labourer.setName(name);
                labourer.setMobile(contact);
                labourer.setUsername(userName);
                labourer.setPassword(password);
                labourer.setProfilePath(profilePath);
                labourer.setApproved(isApproved);
                labourerList.add(labourer);
            }while (cursor.moveToNext());

        }

        db.close();
        return labourerList;
    }
    public ArrayList<Labourer> getLabourersRegisteredForTheScheme()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.LABOURER_TABLE_NAME;

        String[] columns = new String[]{Constants.ID_KEY,
                Constants.LABOURER_NAME_COLUMN,
                Constants.LABOURER_USERNAME_COLUMN,
                Constants.LABOURER_PASSWORD_COLUMN,
                Constants.LABOURER_ISAPPROVED_COLUMN,
                Constants.LABOURER_MOBILE_COLUMN,
                Constants.LABOURER_PROFILEPATH_COLUMN};
        String where_clause = Constants.LABOURER_ISAPPROVED_COLUMN + " =1 AND " + Constants.LABOURER_IS_REGISTERED_FOR_SCHEME_COLUMN + " =1 AND " + Constants.LABOURER_IS_JOBCARD_ISSUED_COLUMN + " =0";

        Cursor cursor = db.query(table, columns, where_clause,
                null, null, null, null, null);
        ArrayList<Labourer> labourerList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {

                long beneficiaryID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));

                String name = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_NAME_COLUMN));
                String contact = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_MOBILE_COLUMN));
                String profilePath = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PROFILEPATH_COLUMN));
                String userName = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_USERNAME_COLUMN));
                String password = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PASSWORD_COLUMN));

                boolean isApproved = (cursor.getInt(cursor.getColumnIndex(Constants.LABOURER_ISAPPROVED_COLUMN)) ==1);


                Labourer labourer = new Labourer();
                labourer.setLabourerID(beneficiaryID);
                labourer.setName(name);
                labourer.setMobile(contact);
                labourer.setUsername(userName);
                labourer.setPassword(password);
                labourer.setProfilePath(profilePath);
                labourer.setApproved(isApproved);
                labourerList.add(labourer);
            }while (cursor.moveToNext());

        }

        db.close();
        return labourerList;
    }
    public Job getJobWithID(long jobID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = Constants.JOB_TABLE_NAME;
        String[] columns = new String[]{Constants.ID_KEY, Constants.JOB_TITLE_KEY, Constants.JOB_DESCRIPTION_KEY,
                Constants.JOB_NUMBER_OF_LABOURERS, Constants.JOB_FUNDS_ALLOTTED, Constants.JOB_DURATION_KEY};
        String where = Constants.ID_KEY + " =?";
        Cursor cursor = db.query(table, columns, where,
                new String[]{String.valueOf(jobID)}, null, null, null, null);
        ArrayList<Job> jobList = new ArrayList();
        Job job = null;
        if (cursor.moveToFirst()) {
                String title = cursor.getString(cursor.getColumnIndex(Constants.JOB_TITLE_KEY));
                String description = cursor.getString(cursor.getColumnIndex(Constants.JOB_DESCRIPTION_KEY));
                String funds = cursor.getString(cursor.getColumnIndex(Constants.JOB_FUNDS_ALLOTTED));
                int duration = cursor.getInt(cursor.getColumnIndex(Constants.JOB_DURATION_KEY));
                int numberOfLabourers = cursor.getInt(cursor.getColumnIndex(Constants.JOB_NUMBER_OF_LABOURERS));
                job = new Job();
                job.setJobID(jobID);
                job.setNumberOfLabourers(numberOfLabourers);
                job.setDurationInDays(duration);
                job.setFundsAllotted(funds);
                job.setTitle(title);
                job.setDescription(description);
                jobList.add(job);
        }
        db.close();
        return job;
    }


    public ArrayList<Job> getJobList()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.JOB_TABLE_NAME;

        String[] columns = new String[]{Constants.ID_KEY,
                Constants.JOB_TITLE_KEY,
                Constants.JOB_DESCRIPTION_KEY,
                Constants.JOB_NUMBER_OF_LABOURERS,
                Constants.JOB_FUNDS_ALLOTTED,
                Constants.JOB_DURATION_KEY};

        Cursor cursor = db.query(table, columns, null,
                null, null, null, null, null);
        ArrayList<Job> jobList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {

                long jobID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));

                String title = cursor.getString(cursor.getColumnIndex(Constants.JOB_TITLE_KEY));
                String description = cursor.getString(cursor.getColumnIndex(Constants.JOB_DESCRIPTION_KEY));
                String funds = cursor.getString(cursor.getColumnIndex(Constants.JOB_FUNDS_ALLOTTED));
                int duration = cursor.getInt(cursor.getColumnIndex(Constants.JOB_DURATION_KEY));
                int numberOfLabourers = cursor.getInt(cursor.getColumnIndex(Constants.JOB_NUMBER_OF_LABOURERS));



                Job job = new Job();
                job.setJobID(jobID);
                job.setNumberOfLabourers(numberOfLabourers);
                job.setDurationInDays(duration);
                job.setFundsAllotted(funds);
                job.setTitle(title);
                job.setDescription(description);
                jobList.add(job);
            }while (cursor.moveToNext());

        }

        db.close();
        return jobList;
    }

    public int updatePasswordForRozgarSewak(String newPassword, long sewakID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.ROZGARSEWAK_PASSWORD_KEY, newPassword);

        // Updating row
        return db.update(Constants.ROZGARSEWAK_TABLE_NAME, values, Constants.ID_KEY + "=?",
                new String[]{String.valueOf(sewakID)});
    }

    public int updatePasswordForLabourer(String newPassword, long labourerID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.LABOURER_PASSWORD_COLUMN, newPassword);

        // Updating row
        return db.update(Constants.LABOURER_TABLE_NAME, values, Constants.ID_KEY + "=?",
                new String[]{String.valueOf(labourerID)});
    }

    public long addAttendance(Attendance attendance)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // Inserting Row to Attendance table
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String date = dateFormatter.format(attendance.getDate().getTime());

        values.put(Constants.ATTENDANCE_DATE_KEY, date);
        values.put(Constants.LABOURER_ID_KEY, attendance.getLabourerID());
        values.put(Constants.JOB_ID_KEY, attendance.getJobID());

        long attendanceID = db.insert(Constants.ATTENDANCE_TABLE_NAME, null, values);

        db.close();
        return attendanceID;

    }

    public void addLabourerToJob(long labourerID, long jobID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();


        values.put(Constants.JOB_ID_KEY, jobID);
        values.put(Constants.LABOURER_ID_KEY, labourerID);
        db.insert(Constants.JOB_LABOURER_TABLE_NAME, null, values);

        db.close();
    }

    public long getJobIDForLabourer(long labourerID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.JOB_LABOURER_TABLE_NAME;
        String[] columns = new String[]{Constants.JOB_ID_KEY};
        String where = Constants.LABOURER_ID_KEY + " =?";

        Cursor cursor = db.query(table, columns, where,
                new String[]{String.valueOf(labourerID)}, null, null, null, null);

        int jobID = 0;
        if (cursor.moveToFirst()) {

                jobID = cursor.getInt(cursor.getColumnIndex(Constants.JOB_ID_KEY));
        }
        db.close();
        return jobID;
    }


    public void removeLabourerFromJob(Labourer labourer) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.JOB_LABOURER_TABLE_NAME, Constants.LABOURER_ID_KEY + "=?",
                new String[]{String.valueOf(labourer.getLabourerID())});
        db.close();
    }

    public ArrayList<Labourer> getLabourerListForJob(long jobID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.JOB_LABOURER_TABLE_NAME;
        String[] columns = new String[]{Constants.LABOURER_ID_KEY};
        String where = Constants.JOB_ID_KEY + " =?";

        Cursor cursor = db.query(table, columns, where,
                new String[]{String.valueOf(jobID)}, null, null, null, null);

        ArrayList<Labourer> labourers = new ArrayList<Labourer>();
        if (cursor.moveToFirst()) {
            do {
                long labourerID = cursor.getLong(cursor.getColumnIndex(Constants.LABOURER_ID_KEY));
                labourers.add(getLabourerWithID(labourerID));

            } while (cursor.moveToNext());
        }
        db.close();
        return labourers;
    }



    public ArrayList<Attendance> getAttendanceListForLabourer(long labourerID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.ATTENDANCE_TABLE_NAME;
        String[] columns = new String[]{Constants.ID_KEY,
        Constants.ATTENDANCE_DATE_KEY,
        Constants.LABOURER_ID_KEY,
        Constants.JOB_ID_KEY};
        String where = Constants.LABOURER_ID_KEY + " =?";

        Cursor cursor = db.query(table, columns, where,
                new String[]{String.valueOf(labourerID)}, null, null, null, null);

        ArrayList<Attendance> attendances = new ArrayList<Attendance>();
        if (cursor.moveToFirst()) {
            do {
                long id  = cursor.getInt(cursor.getColumnIndex(Constants.ID_KEY));
                long jobID  = cursor.getInt(cursor.getColumnIndex(Constants.JOB_ID_KEY));

                String stringDate  = cursor.getString(cursor.getColumnIndex(Constants.ATTENDANCE_DATE_KEY));

                Calendar date = Utility.stringToDate(stringDate);

                Attendance attendance = new Attendance();
                attendance.setAttendanceID(id);
                attendance.setLabourerID(labourerID);
                attendance.setJobID(jobID);
                attendance.setDate(date);
                attendances.add(attendance);

            } while (cursor.moveToNext());
        }
        db.close();
        return attendances;
    }

    public ArrayList<Labourer> getLabourersAllottedWithJob()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.LABOURER_TABLE_NAME;

        String[] columns = new String[]{Constants.ID_KEY,
                Constants.LABOURER_NAME_COLUMN,
                Constants.LABOURER_USERNAME_COLUMN,
                Constants.LABOURER_PASSWORD_COLUMN,
                Constants.LABOURER_ISAPPROVED_COLUMN,
                Constants.LABOURER_MOBILE_COLUMN,
                Constants.LABOURER_PROFILEPATH_COLUMN};
        String where_clause = Constants.LABOURER_ISAPPROVED_COLUMN + " =1 AND " + Constants.LABOURER_IS_REGISTERED_FOR_SCHEME_COLUMN + " =1 AND " + Constants.LABOURER_IS_JOBCARD_ISSUED_COLUMN + " =1 AND " + Constants.LABOURER_ALLOTTED_FOR_JOB_COLUMN + " =1";

        Cursor cursor = db.query(table, columns, where_clause,
                null, null, null, null, null);
        ArrayList<Labourer> labourerList = new ArrayList();
        if (cursor.moveToFirst()) {
            do {

                long beneficiaryID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));

                String name = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_NAME_COLUMN));
                String contact = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_MOBILE_COLUMN));
                String profilePath = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PROFILEPATH_COLUMN));
                String userName = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_USERNAME_COLUMN));
                String password = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_PASSWORD_COLUMN));

                boolean isApproved = (cursor.getInt(cursor.getColumnIndex(Constants.LABOURER_ISAPPROVED_COLUMN)) ==1);


                Labourer labourer = new Labourer();
                labourer.setLabourerID(beneficiaryID);
                labourer.setName(name);
                labourer.setMobile(contact);
                labourer.setUsername(userName);
                labourer.setPassword(password);
                labourer.setProfilePath(profilePath);
                labourer.setApproved(isApproved);
                labourerList.add(labourer);
            }while (cursor.moveToNext());

        }

        db.close();
        return labourerList;
    }

    public int updateProofPhotosForLabourer(Labourer labourer, String beforePath, String afterPath, Calendar date)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String stringDate = dateFormatter.format(date.getTime());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.BEFORE_PHOTO_KEY, beforePath);
        values.put(Constants.AFTER_PHOTO_KEY, afterPath);
        values.put(Constants.DATE_KEY, stringDate);

        // Updating row
        return db.update(Constants.JOB_LABOURER_TABLE_NAME, values, Constants.LABOURER_ID_KEY + "=?",
                new String[]{String.valueOf(labourer.getLabourerID())});

    }

    public ArrayList<String> getProofPhotosForLabourerForDate(long labourerID, Calendar date)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.JOB_LABOURER_TABLE_NAME;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String stringDate = dateFormatter.format(date.getTime());

        String[] columns = new String[]{Constants.BEFORE_PHOTO_KEY,
                Constants.AFTER_PHOTO_KEY};
        String where_clause = Constants.LABOURER_ID_KEY + " =?"  + " AND " + Constants.DATE_KEY + " =?";

        Cursor cursor = db.query(table, columns, where_clause,
                new String[]{String.valueOf(labourerID), stringDate}, null, null, null, null);
        ArrayList<String> proofPhotos = null;
        if (cursor.moveToFirst()) {


            proofPhotos = new ArrayList<>();
                String beforePhoto = cursor.getString(cursor.getColumnIndex(Constants.BEFORE_PHOTO_KEY));
                String afterPhoto = cursor.getString(cursor.getColumnIndex(Constants.AFTER_PHOTO_KEY));
                proofPhotos.add(beforePhoto);
            proofPhotos.add(afterPhoto);

        }

        db.close();
        return proofPhotos;

    }

    public ArrayList<String> getAllLabourerAadharIDs()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.LABOURER_TABLE_NAME;
        String[] columns = new String[]{Constants.LABOURER_AADHAR_COLUMN};

        Cursor cursor = db.query(table, columns, null,
                null, null, null, null, null);

        ArrayList<String> aadharIDs = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String aadharID = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_AADHAR_COLUMN));
                aadharIDs.add(aadharID);
            } while (cursor.moveToNext());
        }

        db.close();
        return aadharIDs;
    }

    public ArrayList<String> getAllLabourerBPLCardNumbers()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.LABOURER_TABLE_NAME;
        String[] columns = new String[]{Constants.LABOURER_BPL_CARD_COLUMN};

        Cursor cursor = db.query(table, columns, null,
                null, null, null, null, null);

        ArrayList<String> bplcardNumbers = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String bplcardNumber = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_BPL_CARD_COLUMN));
                bplcardNumbers.add(bplcardNumber);
            } while (cursor.moveToNext());
        }

        db.close();
        return bplcardNumbers;
    }

    public ArrayList<String> getAllLabourerVoterIDs()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.LABOURER_TABLE_NAME;
        String[] columns = new String[]{Constants.LABOURER_VOTER_ID_COLUMN};

        Cursor cursor = db.query(table, columns, null,
                null, null, null, null, null);

        ArrayList<String> voterIDs = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String voterID = cursor.getString(cursor.getColumnIndex(Constants.LABOURER_VOTER_ID_COLUMN));
                voterIDs.add(voterID);
            } while (cursor.moveToNext());
        }

        db.close();
        return voterIDs;
    }



}
