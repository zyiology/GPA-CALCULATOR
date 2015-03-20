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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class AddSubjActivity extends ActionBarActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subj);

        final ArrayList<Subject> subjArrayList = new ArrayList<Subject>();
        Subject[] oldSubjArray = (Subject[]) getIntent().getExtras().get(SubjectList.SUBJECT_ARRAY);

        if (oldSubjArray != null) {
            for (int i=0;i<oldSubjArray.length;i++) {
                subjArrayList.add(oldSubjArray[i]);
            }
        }

        final Button subjSubmission = (Button) findViewById(R.id.subjSubmission);
        final EditText subjName = (EditText) findViewById(R.id.SubjName);

        subjSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subject newSubject = new Subject(subjName.getText().toString(),"0", new ArrayList<Result>());
                subjArrayList.add(newSubject);
                double totalGpa = 0;
                for (int i=0;i<subjArrayList.size();i++) {
                    totalGpa += Double.parseDouble(subjArrayList.get(i).getGpa());
                }

                Subject[] subjArray = new Subject[subjArrayList.size()];
                subjArray = subjArrayList.toArray(subjArray);//preparing subjArray

                Intent intent = new Intent(AddSubjActivity.this, SubjectList.class);
                intent.putExtra(SubjectList.FINAL_GPA, totalGpa/(subjArrayList.size()));
                intent.putExtra(SubjectList.SUBJECT_ARRAY, subjArray);
                startActivity(intent);
            }
        });

        subjName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    subjSubmission.performClick();
                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm != null){
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    }
                }
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_subj, menu);
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
