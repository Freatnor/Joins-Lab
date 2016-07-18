package com.example.freatnor.joins_lab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonathan Taylor on 7/18/16.
 */
public class EmployeeDatabaseHelper  extends SQLiteOpenHelper {
    public static final String EMPLOYEE_TABLE_NAME = "employee";
    public static final String COL_SSN = "ssn";
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_YEAR_OF_BIRTH = "year_of_birth";
    public static final String COL_CITY = "city";

    public static final String JOB_TABLE_NAME = "job";
    public static final String COL_COMPANY = "company";
    public static final String COL_SALARY = "salary";
    public static final String COL_EXPERIENCE = "experience";



    private EmployeeDatabaseHelper(Context context) {
        super(context, "EmployeeRecords.db", null, 1);
    }

    private static EmployeeDatabaseHelper INSTANCE;

    public static synchronized EmployeeDatabaseHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new EmployeeDatabaseHelper(context.getApplicationContext());
        }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_EMPLOYEE);
        db.execSQL(SQL_CREATE_ENTRIES_JOB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES_EMPLOYEE);
        db.execSQL(SQL_DELETE_ENTRIES_JOB);
        onCreate(db);
    }

    private static final String SQL_CREATE_ENTRIES_EMPLOYEE = "CREATE TABLE " +
            EMPLOYEE_TABLE_NAME + " (" +
            COL_SSN + " TEXT PRIMARY KEY," +
            COL_FIRST_NAME + " TEXT," +
            COL_LAST_NAME + " TEXT," +
            COL_YEAR_OF_BIRTH + " INTEGER," +
            COL_CITY + " TEXT" + ")";

    private static final String SQL_CREATE_ENTRIES_JOB = "CREATE TABLE " +
            JOB_TABLE_NAME + " (" +
            COL_SSN + " TEXT PRIMARY KEY," +
            COL_COMPANY + " TEXT," +
            COL_SALARY + " INTEGER" + "," +
            COL_EXPERIENCE + " INTEGER" + "," +
            "FOREIGN KEY("+ COL_SSN +") REFERENCES "+ EMPLOYEE_TABLE_NAME+"("+ COL_SSN +") )";

    private static final String SQL_DELETE_ENTRIES_EMPLOYEE = "DROP TABLE IF EXISTS " +
            EMPLOYEE_TABLE_NAME;
    private static final String SQL_DELETE_ENTRIES_JOB = "DROP TABLE IF EXISTS " +
            JOB_TABLE_NAME;

    public void insertRow(Employee employee) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SSN, employee.getSSN()); 
        values.put(COL_FIRST_NAME, employee.getFirstName());
        values.put(COL_LAST_NAME, employee.getLastName());
        values.put(COL_YEAR_OF_BIRTH, employee.getYearOfBirth());
        values.put(COL_CITY, employee.getCity());
        try{db.insertOrThrow(EMPLOYEE_TABLE_NAME, null, values);}
        catch(Exception e){
            Log.e("Insert", "insertRow: unable to insert because of unique issue", e);
        }
    }

    public void insertRowJob(Job job) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SSN, job.getSSN());
        values.put(COL_COMPANY, job.getCompany());
        values.put(COL_SALARY, job.getSalary());
        values.put(COL_EXPERIENCE, job.getExperience());
        try{db.insertOrThrow(JOB_TABLE_NAME, null, values);}
        catch(Exception e){
            Log.e("Insert", "insertRow: unable to insert because of unique issue", e);
        }
    }

    public void insertRows(List<Employee> employees){
        for (Employee employee : employees) {
            insertRow(employee);
        }
    }

    public void insertRowJobs(List<Job> jobs){
        for (Job job : jobs) {
            insertRowJob(job);
        }
    }

    public ArrayList<String> getSameCompanyJoins() {
        ArrayList<String> names = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(EMPLOYEE_TABLE_NAME);

        String query = "SELECT " + EMPLOYEE_TABLE_NAME + "." + COL_FIRST_NAME + ", " + EMPLOYEE_TABLE_NAME
                + "." + COL_LAST_NAME + ", COUNT(" + JOB_TABLE_NAME
                + "." + COL_COMPANY + ") FROM " + EMPLOYEE_TABLE_NAME
                + " JOIN " + JOB_TABLE_NAME + " ON " + JOB_TABLE_NAME + "." + COL_SSN
                + " = " + EMPLOYEE_TABLE_NAME + "." + COL_SSN + " GROUP BY " + JOB_TABLE_NAME
                + "." + COL_COMPANY + " HAVING COUNT(" + JOB_TABLE_NAME
                + "." + COL_COMPANY + ") > 1";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                String name = cursor.getString(cursor.getColumnIndex(COL_FIRST_NAME));
                name += " " + cursor.getString(cursor.getColumnIndex(COL_LAST_NAME));
                names.add(name);
                cursor.moveToNext();
            }
        }
        return names;
    }

    public ArrayList<String> getBostonCompanies(){
        ArrayList<String> names = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT " + JOB_TABLE_NAME + "." + COL_COMPANY + " FROM " + JOB_TABLE_NAME
                + " JOIN " + EMPLOYEE_TABLE_NAME + " ON " + JOB_TABLE_NAME + "." + COL_SSN
                + " = " + EMPLOYEE_TABLE_NAME + "." + COL_SSN + " WHERE " + EMPLOYEE_TABLE_NAME
                + "." + COL_CITY + " = 'Boston'";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                names.add(cursor.getString(cursor.getColumnIndex(COL_COMPANY)));
                cursor.moveToNext();
            }
        }
        return names;
    }

    //It's a list since I thought it was originally going to return all the companies if a tie...
    //Leaving it as is since even with the LIMIT it works correctly with a list
    public ArrayList<String> getHighestSalaries(){
        ArrayList<String> names = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {COL_COMPANY};

        Cursor cursor = db.query(JOB_TABLE_NAME, columns, null, null, null, null, COL_SALARY, 1 + "");

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                names.add(cursor.getString(cursor.getColumnIndex(COL_COMPANY)));
                cursor.moveToNext();
            }
        }
        return names;
    }

}
