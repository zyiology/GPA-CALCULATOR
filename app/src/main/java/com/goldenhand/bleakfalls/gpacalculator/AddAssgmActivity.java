package com.goldenhand.bleakfalls.gpacalculator;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddAssgmActivity extends ActionBarActivity {

    public String[] tempStringArray = new String[4];
    public ArrayList<Result> allResults = new ArrayList<Result>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assgm);

        //setting up input stuff
        final EditText assignmentEditText = (EditText) findViewById(R.id.Name);
        final EditText scoreEditText = (EditText) findViewById(R.id.Marks);
        final EditText maxScoreEditText = (EditText) findViewById(R.id.TotalMarks);
        final EditText weightageEditText = (EditText) findViewById(R.id.Weightage);
        final Button submission = (Button) findViewById(R.id.Submit);
        final ListView resultSummary = (ListView) findViewById(R.id.resultSummary);

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
        assignmentEditText.requestFocus();

        assignmentEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    scoreEditText.requestFocus();
                }
                return true;
            }
        });

        scoreEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    maxScoreEditText.requestFocus();
                }
                return true;
            }
        });

        maxScoreEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    weightageEditText.requestFocus();
                }
                return true;
            }
        });

        weightageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
                if (assignmentEditText.getText().toString().matches("") || scoreEditText.getText().toString().matches("") || maxScoreEditText.getText().toString().matches("") || weightageEditText.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "NOT ALL FIELDS ARE FILLED", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(scoreEditText.getText().toString())>Integer.parseInt(maxScoreEditText.getText().toString())){
                    Toast.makeText(getApplicationContext(), "MARKS CANNOT EXCEED TOTAL MARKS", Toast.LENGTH_SHORT).show();
                } else {
                    //preparing resultarray
                    Result result = new Result(assignmentEditText.getText().toString(), scoreEditText.getText().toString(), maxScoreEditText.getText().toString(), weightageEditText.getText().toString());
                    Subject testSubject = (Subject) getIntent().getExtras().get(ResultSummary.TARGET_SUBJECT);
                    ArrayList<Result> allResults = testSubject.getResults();
                    allResults.add(result);

                    for (int y=0;y<allResults.size();y++) {
                        Log.d("Results",allResults.get(y).getResultName());
                    }

                    //preparing gpa calculations
                    double totalScore = 0;
                    double maxScore = 0;
                    for (int i = 0; i < allResults.size(); i++) {
                        maxScore += ((Double.parseDouble(allResults.get(i).getMarks()) / Double.parseDouble(allResults.get(i).getTotalMarks())) * ((Double.parseDouble(allResults.get(i).getWeightage())) / 100));
                        totalScore += Double.parseDouble(allResults.get(i).getWeightage())/100;
                        Log.d("Marks/Total", String.valueOf((Double.parseDouble(allResults.get(i).getMarks()) / Double.parseDouble(allResults.get(i).getTotalMarks()))));
                    }
                    double finalScore = (totalScore / maxScore) * 100;
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

                    Subject subject = (Subject) getIntent().getExtras().get(ResultSummary.TARGET_SUBJECT);
                    Subject[] subjectArray = (Subject[]) getIntent().getExtras().get(SubjectList.SUBJECT_ARRAY);

                    for (int i=0;i<subjectArray.length;i++){
                        Log.d("setResults",subjectArray[i].getName());
                        Log.d("setResults",subject.getName());
                        if (subjectArray[i].getName().equals(subject.getName())) {
                            subjectArray[i].setResults(allResults);
                            subjectArray[i].setGpa(gpa);
                            subject.setResults(allResults);
                            subject.setGpa(gpa);
                            Log.d("setResults", "Results set");
                        }
                    }
                    Intent intent = new Intent(AddAssgmActivity.this, ResultSummary.class);
                    intent.putExtra(ResultSummary.FINAL_SCORE, finalScore);
                    intent.putExtra(ResultSummary.TARGET_SUBJECT, subject);
                    intent.putExtra(SubjectList.SUBJECT_ARRAY, subjectArray);
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
