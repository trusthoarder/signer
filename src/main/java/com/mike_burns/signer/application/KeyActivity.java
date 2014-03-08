package com.mike_burns.signer.application;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import com.mike_burns.signer.R;

public class KeyActivity extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView( R.layout.key);

    Intent intent = getIntent();
    String keyid = intent.getStringExtra(SearchResultsActivity.KEYID);

    TextView keyidText = (TextView)findViewById(R.id.keyid);
    keyidText.setText(keyid);
  }

  public void approve(View view) {
    TextView message = new TextView(this);
    message.setText("Approved");
    setContentView(message);
  }
}
