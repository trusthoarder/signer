package com.trusthoarder.signer.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.trusthoarder.signer.R;
import com.trusthoarder.signer.domain.Database;
import com.trusthoarder.signer.domain.UserKeys;
import com.trusthoarder.signer.infrastructure.SafeAsyncTask;

public class SetupActivity extends Activity {
  public final static String SEARCH_STRING = "com.trusthoarder.signer.SEARCH_STRING";
  private Database db;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    db = new Database( this );

    new SafeAsyncTask<Boolean>(){
      @Override
      public Boolean call() throws Exception {
        return new UserKeys( db ).isSetup();
      }

      @Override
      protected void onSuccess( Boolean isSetup ) throws Exception {
        if(isSetup) {
          startActivity( new Intent(SetupActivity.this, DashboardActivity.class) );
        }
        else {
          setContentView( R.layout.search );
        }
      }

      @Override
      protected void onException( Exception e ) throws RuntimeException {
        Log.e( "signer", "Failed to load user key.", e );
        Toast.makeText( SetupActivity.this,
          "Failed because of reasons: " + e.getMessage(), Toast.LENGTH_LONG ).show();
      }
    }.execute();

  }

  public void search(View view) {
    Intent intent = new Intent(this, SearchResultsActivity.class);

    EditText editText = (EditText) findViewById(R.id.search_string);
    String message = editText.getText().toString();
    intent.putExtra(SEARCH_STRING, message);

    startActivity(intent);
  }

  @Override
  protected void onPause() {
    db.close();
    super.onPause();
  }
}
