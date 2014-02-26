package com.mike_burns.signer;

import android.app.ListActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchResultsActivity extends ListActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();
    String search_string = intent.getStringExtra(SigningActivity.SEARCH_STRING);

    ListAdapter adapter = new SimpleAdapter(
        this,
        dummyData(search_string),
        R.layout.search_result,
        new String[] { "KEYID", "PRIMARY_UID_NAME", "PRIMARY_UID_EMAIL" },
        new int[] { R.id.keyid, R.id.name, R.id.email }
    );

    setListAdapter(adapter);

    setContentView(R.layout.search_results);
  }

  private List<Map<String, String>> dummyData(String search_string) {
    List<Map<String, String>> data = new ArrayList<Map<String, String>>();

    Map<String, String> row1 = new HashMap<String, String>();
    row1.put("KEYID", "0xA1723F75AFEB61EC");
    row1.put("PRIMARY_UID_NAME", "Michael John Burns");
    row1.put("PRIMARY_UID_EMAIL", "mike@mike-burns.com");
    data.add(row1);

    Map<String, String> row2 = new HashMap<String, String>();
    row2.put("KEYID", "0x3E6761F72846B014");
    row2.put("PRIMARY_UID_NAME", "Michael John Burns");
    row2.put("PRIMARY_UID_EMAIL", "mike@mike-burns.com");
    data.add(row2);

    return data;
  }
}
