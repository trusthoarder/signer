package com.trusthoarder.signer.application;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.trusthoarder.signer.R;
import com.trusthoarder.signer.domain.Database;
import com.trusthoarder.signer.domain.PublicKey;
import com.trusthoarder.signer.domain.UserKeys;
import com.trusthoarder.signer.infrastructure.Optional;
import com.trusthoarder.signer.infrastructure.SafeAsyncTask;

public class DashboardActivity extends Activity {
  private Database db;
  private TextView keyIdText;

  @Override
  protected void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );

    db = new Database( this );
    setContentView( R.layout.dashboard );
    keyIdText = (TextView) findViewById( R.id.keyid );

    new SafeAsyncTask<Object>(  ){
      @Override
      public Object call() throws Exception {
        UserKeys userKeys = new UserKeys( db );

        Optional<PublicKey> userKey = userKeys.userKey();

        if(userKey.isPresent()) {
          keyIdText.setText( userKey.get().keyIdString() );
        } else {
          throw new IllegalStateException( "There is no user key set up." );
        }

        return null;
      }

      @Override
      protected void onException( Exception e ) throws RuntimeException {
        Toast.makeText( DashboardActivity.this,
          "Failed to load user key: " + e.getMessage(), Toast.LENGTH_LONG ).show();
      }
    }.execute();
  }

  @Override
  protected void onPause() {
    db.close();
    super.onPause();
  }
}
