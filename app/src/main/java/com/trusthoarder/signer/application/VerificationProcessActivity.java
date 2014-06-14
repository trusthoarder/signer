package com.trusthoarder.signer.application;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.trusthoarder.signer.R;
import com.trusthoarder.signer.domain.Database;
import com.trusthoarder.signer.domain.KeyServer;
import com.trusthoarder.signer.domain.PublicKey;
import com.trusthoarder.signer.domain.UserKeys;
import com.trusthoarder.signer.infrastructure.Optional;
import com.trusthoarder.signer.infrastructure.SafeAsyncTask;

import static com.trusthoarder.signer.application.FingerprintFormatter.humanReadableFingerprint;

public class VerificationProcessActivity extends Activity {

  public static String KEYID = "com.trusthoarder.signer.KEYID";

  private final KeyServer keyServer = new KeyServer();
  private TextView fingerprintText;
  private TextView friendlyName;

  private Database db;
  private UserKeys keys;

  private Optional<PublicKey> keyToVerify = Optional.none();

  @Override
  protected void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
    db = new Database(this);
    keys = new UserKeys(db);
    setContentView( R.layout.verify_key );

    fingerprintText = (TextView) findViewById( R.id.fingerprint );
    friendlyName = (TextView) findViewById( R.id.friendly_name );

    final String keyIdToVerify = getIntent().getStringExtra( KEYID );

    new SafeAsyncTask<Optional<PublicKey>>(  ){
      @Override
      public Optional<PublicKey> call() throws Exception {
        return keyServer.get( keyIdToVerify );
      }

      @Override
      protected void onSuccess( Optional<PublicKey> loadedKey ) throws Exception {
        if(loadedKey.isPresent()) {
          keyToVerify = loadedKey;
          PublicKey key = loadedKey.get();
          fingerprintText.setText( humanReadableFingerprint( key ) );
          friendlyName.setText( key.friendlyName() );
        }
        else {
          Toast.makeText( VerificationProcessActivity.this,
            "Could not find key: " + keyIdToVerify, Toast.LENGTH_LONG ).show();
          setResult( RESULT_CANCELED );
          finish();
        }
      }

      @Override
      protected void onException( Exception e ) throws RuntimeException {
        Log.e( "signer", "Failed to run verification.", e );
        Toast.makeText( VerificationProcessActivity.this,
          "Failed load: " + e.getMessage(), Toast.LENGTH_LONG ).show();
      }
    }.execute();
  }

  public void onSignClicked( View view ) {
    // Put key in list of signed keys
    if(keyToVerify.isPresent()) {
      new SafeAsyncTask<Void>(  ){
        @Override
        public Void call() throws Exception {
          keys.addVerifiedKey(keyToVerify.get());
          return null;
        }

        @Override
        protected void onSuccess( Void ignore ) throws Exception {
          setResult( RESULT_OK );
          finish();
        }

        @Override
        protected void onException( Exception e ) throws RuntimeException {
          Log.e( "signer", "Failed to save verified key.", e );
          Toast.makeText( VerificationProcessActivity.this,
              "Failed to save verified key: " + e.getMessage(), Toast.LENGTH_LONG ).show();
        }
      }.execute();
    }
  }

  public void onCancelClicked( View view ) {
    setResult( RESULT_CANCELED );
    finish();
  }

  @Override
  protected void onPause() {
    db.close();
    super.onPause();
  }
}
