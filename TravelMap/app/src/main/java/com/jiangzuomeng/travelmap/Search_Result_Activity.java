package com.jiangzuomeng.travelmap;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Search_Result_Activity extends AppCompatActivity {
    public static String KEYWORDS = "keywords";
    TextView textView;
    String searchwords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__result_);
        textView = (TextView)findViewById(R.id.search_textView);
        Bundle bundle =getIntent().getExtras();
        searchwords = bundle.getString(KEYWORDS);
        handleIntent(getIntent());
    }
    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
            textView.setText(searchwords);
    }
}
