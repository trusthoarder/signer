package com.trusthoarder.signer.application;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.trusthoarder.signer.R;
import com.trusthoarder.signer.domain.KeyServer;
import com.trusthoarder.signer.domain.PublicKey;
import com.trusthoarder.signer.infrastructure.Optional;
import com.trusthoarder.signer.infrastructure.SafeAsyncTask;

import static com.trusthoarder.signer.application.FingerprintFormatter.humanReadableFingerprint;

public class VerificationProcessActivity extends Activity {

  public static String KEYID = "com.trusthoarder.signer.KEYID";

  private final KeyServer keyServer = new KeyServer();
  private TextView fingerprintText;
  private TextView friendlyName;

  @Override
  protected void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );
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
      protected void onSuccess( Optional<PublicKey> keyToVerify ) throws Exception {
        if(keyToVerify.isPresent()) {
          PublicKey key = keyToVerify.get();
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
    // Put key in list of signed keys here.
    setResult( RESULT_OK );
    finish();
  }

  public void onCancelClicked( View view ) {
    setResult( RESULT_CANCELED );
    finish();
  }
}
