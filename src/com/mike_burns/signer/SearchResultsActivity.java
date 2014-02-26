package com.mike_burns.signer;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

public class SearchResultsActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();
    String message = intent.getStringExtra(SigningActivity.SEARCH_STRING);

    // Create the text view
    TextView textView = new TextView(this);
    textView.setTextSize(40);
    textView.setText(message);

    // Set the text view as the activity layout
    setContentView(textView);
    // setContentView(R.layout.search_results);
  }
}
