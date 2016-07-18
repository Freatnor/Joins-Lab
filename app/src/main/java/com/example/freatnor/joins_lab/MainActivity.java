package com.example.freatnor.joins_lab;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EmployeeDatabaseHelper helper;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helper = EmployeeDatabaseHelper.getInstance(MainActivity.this);
        mListView = (ListView) findViewById(R.id.list_view);


        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Employee> employees = new ArrayList<>();
                employees.add(new Employee("123-04-5678", "John", "Smith", 1973, "New York"));
                employees.add(new Employee("123-04-5679", "David", "McWill", 1982, "Seattle"));
                employees.add(new Employee("123-04-5680", "Katerina", "Wise", 1973, "Boston"));
                employees.add(new Employee("123-04-5681", "Donald", "Lee", 1992, "London"));
                employees.add(new Employee("123-04-5682", "Gary", "Henwood", 1987, "Las Vegas"));
                employees.add(new Employee("123-04-5683", "Anthony", "Bright", 1963, "Seattle"));
                employees.add(new Employee("123-04-5684", "William", "Newey", 1995, "Boston"));
                employees.add(new Employee("123-04-5685", "Melony", "Smith", 1970, "Chicago"));

                ArrayList<Job> jobs = new ArrayList<>();
                jobs.add(new Job("123-04-5678", "Fuzz", 60, 1));
                jobs.add(new Job("123-04-5679", "GA", 70, 2));
                jobs.add(new Job("123-04-5680", "Little Place", 120, 5));
                jobs.add(new Job("123-04-5681", "Macy's", 78, 3));
                jobs.add(new Job("123-04-5682", "New Life", 65, 1));
                jobs.add(new Job("123-04-5683", "Believe", 158, 6));
                jobs.add(new Job("123-04-5684", "Macy's", 200, 8));
                jobs.add(new Job("123-04-5685", "Stop", 299, 12));

                helper.insertRows(employees);
                helper.insertRowJobs(jobs);
            }
        });

        Button bostonCompany = (Button) findViewById(R.id.boston_companies);
        bostonCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> bostonCompanyNames = helper.getBostonCompanies();
                mListView.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, bostonCompanyNames));
            }
        });

        Button sameCompany = (Button) findViewById(R.id.same_company);
        sameCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> sameCompanyNames = helper.getSameCompanyJoins();
                mListView.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, sameCompanyNames));
            }
        });

        Button highestSalary = (Button) findViewById(R.id.highest_salary_companies);
        highestSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> highestSalaryCompanyNames = helper.getHighestSalaries();
                mListView.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1, highestSalaryCompanyNames));
            }
        });




//        CursorAdapter adapter = new CursorAdapter(MainActivity.this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
//            @Override
//            public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
//                return null;
//            }
//
//            @Override
//            public void bindView(View view, Context context, Cursor cursor) {
//
//            }
//        };
//
//        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText firstNameBox = new EditText(MainActivity.this);
                firstNameBox.setHint("First Name");
                layout.addView(firstNameBox);

                final EditText lastNameBox = new EditText(MainActivity.this);
                lastNameBox.setHint("Last Name");
                layout.addView(lastNameBox);

                final EditText ssnBox = new EditText(MainActivity.this);
                ssnBox.setHint("SSN");
                layout.addView(ssnBox);

                final EditText yearOfBirthBox = new EditText(MainActivity.this);
                yearOfBirthBox.setHint("Year of Birth");
                layout.addView(yearOfBirthBox);

                final EditText cityBox = new EditText(MainActivity.this);
                cityBox.setHint("City");
                layout.addView(cityBox);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add new Employee")
                        .setMessage("Something")
                        .setView(layout)
                        .setPositiveButton("Add Employee", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Employee employee = new Employee(ssnBox.getText().toString(),
                                        firstNameBox.getText().toString(), lastNameBox.getText().toString(),
                                        Integer.parseInt(yearOfBirthBox.getText().toString()),
                                        cityBox.getText().toString());
                                helper.insertRow(employee, null);
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
