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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//could not make delete item, listener does not work

public class ResultSummary extends ActionBarActivity {

    static String RESULT_ARRAY = "com.goldenhand.bleakfalls.gpacalculator.AddAssgmActivity.allResultArray";
    static String GPA = "com.goldenhand.bleakfalls.gpacalculator.AddAssgmActivity.GPA";
    static String FINAL_SCORE = "com.goldenhand.bleakfalls.gpacalculator.AddAssgmActivity.FinalScore";
    static String TARGET_SUBJECT = "com.goldenhand.bleakfalls.gpacalculator.SubjectList.targetSubject";

    Result[] allResultArray;
    private static Result[] resultArray;
    public static int itemHeight;
    Subject subjectReload;//for reload page after deleting an assgm
    Subject[] subjectArrayReload;
    private static ObservableString toDelete = new ObservableString();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result_summary);
        Subject[] subjectArray = (Subject[]) getIntent().getExtras().get(SubjectList.SUBJECT_ARRAY);
        Subject subject = (Subject) getIntent().getExtras().get(ResultSummary.TARGET_SUBJECT);
        subjectReload = subject;
        subjectArrayReload = subjectArray;

        String gpa = subject.getGpa();
        final ArrayList<Result> resultsArrayList = subject.getResults();

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
        toDelete.setOnStringChangeListener(new OnStringChangeListener() {
            @Override
            public void onStringChanged(String resultName) {
                Intent intent = new Intent(ResultSummary.this, ResultSummary.class);
                ArrayList<Result> newResults = subjectReload.getResults();
                for (int i=0;i<subjectReload.getResults().size();i++) {
                    if (resultName == subjectReload.getResults().get(i).getResultName()) {//resetting subjectReload for reload
                        newResults.remove(i);
                        subjectReload.setResults(newResults);
                    }
                    for (int x=0;x<subjectArrayReload.length;i++) {//resetting subjectArrayReload
                        if (subjectArrayReload[x].getName() == subjectReload.getName()) {
                            subjectArrayReload[x].setResults(newResults);
                        }
                    }
                }
                intent.putExtra(SubjectList.SUBJECT_ARRAY, subjectArrayReload);
                intent.putExtra(TARGET_SUBJECT, subjectReload);
                if (getIntent().getExtras().containsKey(FINAL_SCORE)) {
                    intent.putExtra(FINAL_SCORE, (String) getIntent().getExtras().get(FINAL_SCORE));
                }
                startActivity(intent);
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
            ListAdapter listAdapter;
            if (resultArray!= null) {
                listAdapter = createListAdapter(resultArray);
                setListAdapter(listAdapter);
            }

            final ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Map<String, String> target = (Map<String, String>) parent.getItemAtPosition(position);
                    for (int i=0; i<resultArray.length;i++) {
                        if (resultArray[i].getResultName().equals(target.get("name"))) {
                            toDelete.set(target.get("name"));
                        }
                    }
                }
            });
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
