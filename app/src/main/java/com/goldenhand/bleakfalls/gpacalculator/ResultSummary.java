package com.goldenhand.bleakfalls.gpacalculator;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultSummary extends ActionBarActivity {

    static String RESULT_ARRAY = "com.goldenhand.bleakfalls.gpacalculator.AddAssgmActivity.allResultArray";
    static String GPA = "com.goldenhand.bleakfalls.gpacalculator.AddAssgmActivity.GPA";
    static String FINAL_SCORE = "com.goldenhand.bleakfalls.gpacalculator.AddAssgmActivity.FinalScore";
    static String TARGET_SUBJECT = "com.goldenhand.bleakfalls.gpacalculator.SubjectList.targetSubject";

    Result[] allResultArray;
    private static Result[] resultArray;
    public static int itemHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result_summary);
        Subject[] subjectArray = (Subject[]) getIntent().getExtras().get(SubjectList.SUBJECT_ARRAY);
        Subject subject = (Subject) getIntent().getExtras().get(ResultSummary.TARGET_SUBJECT);

        String gpa = subject.getGpa();
        ArrayList<Result> resultsArrayList = subject.getResults();

        allResultArray = new Result[resultsArrayList.size()];
        for (int i=0;i<resultsArrayList.size();i++) {
            allResultArray[i]=resultsArrayList.get(i);
        }
        resultArray = allResultArray;
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            resultSummary list = new resultSummary();
            android.app.Fragment fragment = getFragmentManager().findFragmentById(R.id.resultSummary);
            FragmentManager fm = getFragmentManager();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.included, list);
            ft.commit();
        }

        TextView subjectItemTextView = (TextView) findViewById(R.id.subjectItem);
        subjectItemTextView.setText("Subject: " + subject.getName());

        TextView gpaItemTextView = (TextView) findViewById(R.id.gpaItem);
        gpaItemTextView.setText("GPA: " + gpa);

        if (getIntent().getExtras().containsKey(FINAL_SCORE)) {
            TextView percentageItemTextView = (TextView) findViewById(R.id.percentageItem);
            percentageItemTextView.setText("Final Score: " + getIntent().getExtras().get(FINAL_SCORE));
        }

        Button add = (Button) findViewById(R.id.addAssgm);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultSummary.this, AddAssgmActivity.class);
                i.putExtra(SubjectList.SUBJECT_ARRAY, (Subject[]) getIntent().getExtras().get(SubjectList.SUBJECT_ARRAY));
                i.putExtra(TARGET_SUBJECT, (Subject) getIntent().getExtras().get(TARGET_SUBJECT));
                startActivity(i);
            }
        });

        Button returnSubjectList = (Button) findViewById(R.id.returnToSubjectList);
        returnSubjectList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultSummary.this, SubjectList.class);
                i.putExtra(SubjectList.SUBJECT_ARRAY, (Subject[]) getIntent().getExtras().get(SubjectList.SUBJECT_ARRAY));
                startActivity(i);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_summary, menu);
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

    public static class resultSummary extends ListFragment {

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Result[] result = resultArray;
            if (resultArray!= null) {
                final ListAdapter listAdapter = createListAdapter(result);
                setListAdapter(listAdapter);
            }
        }

        /*public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_result_summary, menu);
            return true;
        }*/

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

        static final String TEXT1 = "name";
        static final String TEXT2 = "marks";

        private List<Map<String, String>> convertToListItems(final Result[] resultsOutput) {
            final List<Map<String, String>> listItem = new ArrayList<Map<String, String>>(resultsOutput.length);

            for (final Result result: resultsOutput) {
                final Map<String, String> listItemMap = new HashMap<String, String>();
                listItemMap.put(TEXT1, result.getResultName());
                String subItem = "Marks: " + result.getMarks() + "/" + result.getTotalMarks() + " Weightage: " + result.getWeightage() + "%";
                listItemMap.put(TEXT2, subItem);
                listItem.add(Collections.unmodifiableMap(listItemMap));
            }

            return Collections.unmodifiableList(listItem);
        }

        public ListAdapter createListAdapter(final Result[] resultsOutput) {
            final String[] fromMapKey = new String[] {TEXT1, TEXT2};
            final int[] toLayoutId = new int[] {R.id.Name, R.id.Marks};
            final List<Map<String, String>> list = convertToListItems(resultsOutput);

            return new SimpleAdapter(this.getActivity(), list,
                    R.layout.list_layout,
                    fromMapKey, toLayoutId);
        }
    }
}
