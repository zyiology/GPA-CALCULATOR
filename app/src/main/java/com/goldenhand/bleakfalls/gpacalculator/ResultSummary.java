package com.goldenhand.bleakfalls.gpacalculator;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultSummary extends ActionBarActivity {

    static String RESULT_ARRAY = "com.goldenhand.bleakfalls.gpacalculator.MainActivity.allResultArray";
    static String GPA = "com.goldenhand.bleakfalls.gpacalculator.MainActivity.GPA";
    static String FINAL_SCORE = "com.goldenhand.bleakfalls.gpacalculator.MainActivity.FinalScore";

    Result[] allResultArray;
    private static Result[] resultArray;
    public static int itemHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_result_summary);
        allResultArray = (Result[]) getIntent().getExtras().get(RESULT_ARRAY);
        resultArray = allResultArray;
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            resultSummary list = new resultSummary();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            //ft.add(android.R.id.content, list);
            ft.add(R.id.included, list);
            ft.commit();
        }
        String gpa = (String) getIntent().getExtras().get(GPA);
        TextView gpatext = (TextView) findViewById(R.id.GPA);
        gpatext.setText(gpa);

        String finalScore = (String) getIntent().getExtras().get(FINAL_SCORE).toString();
        TextView finalScoreText = (TextView) findViewById(R.id.FinalScore);
        finalScoreText.setText(finalScore);

        Button add = (Button) findViewById(R.id.Add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ResultSummary.this, MainActivity.class);
                i.putExtra(RESULT_ARRAY, allResultArray);
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
