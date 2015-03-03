package com.goldenhand.bleakfalls.gpacalculator;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    public String[] tempStringArray = new String[4];
    public ArrayList<Result> allResults = new ArrayList<Result>();

    static Result[] allResultsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            Result[] oldResultArray = (Result[]) getIntent().getExtras().get(ResultSummary.RESULT_ARRAY);
            for (int i=0;i<oldResultArray.length;i++) {
                allResults.add(oldResultArray[i]);
            }
        }

        //setting up input stuff
        final EditText name = (EditText) findViewById(R.id.Name);
        final EditText marks = (EditText) findViewById(R.id.Marks);
        final EditText totalMarks = (EditText) findViewById(R.id.TotalMarks);
        final EditText weightage = (EditText) findViewById(R.id.Weightage);
        final Button submission = (Button) findViewById(R.id.Submit);
        final ListView resultSummary = (ListView) findViewById(R.id.resultSummary);

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
        name.requestFocus();

        name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    marks.requestFocus();
                }
                return true;
            }
        });

        marks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    totalMarks.requestFocus();
                }
                return true;
            }
        });

        totalMarks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    weightage.requestFocus();
                }
                return true;
            }
        });

        weightage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    submission.performClick();
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null){
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }
                return true;
            }
        });

        submission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().matches("") || marks.getText().toString().matches("") || totalMarks.getText().toString().matches("") || weightage.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "NOT ALL FIELDS ARE FILLED", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(marks.getText().toString())>Integer.parseInt(totalMarks.getText().toString())){
                    Toast.makeText(getApplicationContext(), "MARKS CANNOT EXCEED TOTAL MARKS", Toast.LENGTH_SHORT).show();
                } else {
                    //preparing resultarray
                    Result result = new Result(name.getText().toString(), marks.getText().toString(), totalMarks.getText().toString(), weightage.getText().toString());
                    allResults.add(result);
                    Result[] allResultsArray = new Result[allResults.size()];
                    allResultsArray = allResults.toArray(allResultsArray);

                    //preparing gpa calculations
                    float totalPercent = 0;
                    float totalMarks = 0;
                    for (int i = 0; i < allResultsArray.length; i++) {
                        totalMarks += ((Float.parseFloat(allResultsArray[i].getMarks()) / Float.parseFloat(allResultsArray[i].getTotalMarks())) * ((Float.parseFloat(allResultsArray[i].getWeightage())) / 100));
                        totalPercent += Float.parseFloat(allResultsArray[i].getWeightage())/100;
                        Log.d("Marks/Total", String.valueOf((Float.parseFloat(allResultsArray[i].getMarks()) / Float.parseFloat(allResultsArray[i].getTotalMarks()))));
                    }
                    float finalScore = (totalMarks / totalPercent) * 100;
                    Log.d("FinalScore",String.valueOf(finalScore));

                    String gpa;
                    if (finalScore >= 79.5) {
                        gpa = "4.0";
                    } else if (finalScore >= 69.5) {
                        gpa = "3.6";
                    } else if (finalScore >= 64.5) {
                        gpa = "3.2";
                    } else if (finalScore >= 59.5) {
                        gpa = "2.8";
                    } else if (finalScore >= 54.5) {
                        gpa = "2.4";
                    } else if (finalScore >= 49.5) {
                        gpa = "2.0";
                    } else if (finalScore >= 44.5) {
                        gpa = "1.6";
                    } else if (finalScore >= 39.5) {
                        gpa = "1.2";
                    } else {
                        gpa = "0.8";
                    }

                    Intent intent = new Intent(MainActivity.this, ResultSummary.class);
                    intent.putExtra(ResultSummary.RESULT_ARRAY, allResultsArray);
                    intent.putExtra(ResultSummary.GPA, gpa);
                    intent.putExtra(ResultSummary.FINAL_SCORE, finalScore);
                    startActivity(intent);
                }
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
