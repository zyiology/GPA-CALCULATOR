package com.goldenhand.bleakfalls.gpacalculator;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//intent should always contain allResultArray, allSubjArray

public class SubjectList extends ActionBarActivity {

    static String FINAL_GPA = "com.goldenhand.bleakfalls.gpacalculator.AddSubjActivity.finalGPA";
    static String SUBJECT_ARRAY = "com.goldenhand.bleakfalls.gpacalculator.AddSubjActivity.GPA";

    Subject[] allSubjArray;
    private static Subject[] subjArray;
    private static String overallGpa;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_subject_list);
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(SUBJECT_ARRAY)) {
                allSubjArray = (Subject[]) getIntent().getExtras().get(SUBJECT_ARRAY);
                subjArray = allSubjArray;
                context=SubjectList.this;
                if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
                    subjectList list = new subjectList();
                    //.app.Fragment fragment = getFragmentManager().findFragmentById(R.id.subjectList);
                    FragmentManager fm = getFragmentManager();
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.included, list);
                    ft.commit();
                }
                double totalGpa = 0;
                for (int i=0;i<allSubjArray.length;i++) {
                    totalGpa += Double.parseDouble(allSubjArray[i].getGpa());
                }
                overallGpa = Double.toString(totalGpa/allSubjArray.length);
                TextView overallGpaItemTextView = (TextView) findViewById(R.id.overallGpaItem);
                overallGpaItemTextView.setText("Overall GPA: " + overallGpa);
            }
        }
        Button add = (Button) findViewById(R.id.addSubjectButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubjectList.this, AddSubjActivity.class);
                i.putExtra(SUBJECT_ARRAY, allSubjArray);
                startActivity(i);
            }
        });
        Button sendToResults = (Button) findViewById(R.id.fakeButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubjectList.this, AddSubjActivity.class);
                i.putExtra(SUBJECT_ARRAY, allSubjArray);
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

    public static class subjectList extends ListFragment {

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Subject[] subject = subjArray;
            if (subject!= null) {
                final ListAdapter listAdapter = createListAdapter(subject);
                setListAdapter(listAdapter);
            }

            ListView lv = getListView();
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Map<String, String> target = (Map<String, String>) parent.getItemAtPosition(position);
                    Subject targetSubject = new Subject("FAKE","0", new ArrayList<Result>());
                    for (int i=0;i<subjArray.length;i++) {
                        if (target.get("name") == subjArray[i].getName()) {
                            targetSubject = subjArray[i];
                        }
                    }
                    Intent intent = new Intent(context, ResultSummary.class);
                    intent.putExtra(ResultSummary.TARGET_SUBJECT, targetSubject);
                    intent.putExtra(SUBJECT_ARRAY, subjArray);
                    startActivity(intent);
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
        static final String TEXT2 = "gpa";

        private List<Map<String, String>> convertToListItems(final Subject[] subjectsOutput) {
            final List<Map<String, String>> listItem = new ArrayList<Map<String, String>>(subjectsOutput.length);

            for (final Subject subject: subjectsOutput) {
                final Map<String, String> listItemMap = new HashMap<String, String>();
                listItemMap.put(TEXT1, subject.getName());
                String subItem = "GPA: " + subject.getGpa();
                listItemMap.put(TEXT2, subItem);
                listItem.add(Collections.unmodifiableMap(listItemMap));
            }

            return Collections.unmodifiableList(listItem);
        }

        public ListAdapter createListAdapter(final Subject[] subjectsOutput) {
            final String[] fromMapKey = new String[] {TEXT1, TEXT2};
            final int[] toLayoutId = new int[] {R.id.Name, R.id.Marks};
            final List<Map<String, String>> list = convertToListItems(subjectsOutput);

            return new SimpleAdapter(this.getActivity(), list,
                    R.layout.list_layout,
                    fromMapKey, toLayoutId);
        }
    }
}

