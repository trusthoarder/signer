package com.trusthoarder.signer.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.trusthoarder.signer.R;
import com.trusthoarder.signer.domain.Database;
import com.trusthoarder.signer.domain.KeyServer;
import com.trusthoarder.signer.domain.PublicKey;
import com.trusthoarder.signer.domain.UserKeys;
import com.trusthoarder.signer.infrastructure.Optional;
import com.trusthoarder.signer.infrastructure.SafeAsyncTask;
import org.apache.http.impl.client.DefaultHttpClient;

import static com.trusthoarder.signer.application.FingerprintFormatter.humanReadableFingerprint;
import static com.trusthoarder.signer.infrastructure.ui.QRCode.buildQRCode;

public class AcceptKeyActivity extends Activity {

  private final KeyServer keyServer = new KeyServer( "http://pgp.mit.edu", new DefaultHttpClient() );

  private TextView keyIdTextView;
  private TextView fingerprintTextView;
  private ImageView qrCodeIV;

  private PublicKey currentKey;
  private Database db;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    db = new Database( this );

    setContentView( R.layout.key );
    keyIdTextView = (TextView)findViewById(R.id.keyScreenKeyId);
    fingerprintTextView = (TextView)findViewById(R.id.keyScreenFingerprint);
    qrCodeIV = (ImageView)findViewById(R.id.qrCode);

    Intent intent = getIntent();
    String keyId = intent.getStringExtra(SearchResultsActivity.KEYID);

    fillInKeyDetails(keyId);
  }

  public void approve(final View view) {
    new SafeAsyncTask<Object>(  ){
      @Override
      public Object call() throws Exception {
        UserKeys userKeys = new UserKeys(db);
        Log.e("signer", "Updating key: " + currentKey);
        if(currentKey != null) {
          Log.e("signer", "Updating key");
          userKeys.setUserKeyTo( currentKey );
        }

        return null;
      }

      @Override
      protected void onSuccess( Object o ) throws Exception {
        TextView message = new TextView(AcceptKeyActivity.this);
        message.setText( "Approved" );
        setContentView( message );
      }

      @Override
      protected void onException( Exception e ) throws RuntimeException {
        Toast.makeText( AcceptKeyActivity.this,
          "Failed to set your key: " + e.getMessage(),
          Toast.LENGTH_LONG ).show();
      }
    }.execute();
  }

  private void fillInKeyDetails(final String keyId) {
    new SafeAsyncTask<Optional<PublicKey>>() {
      @Override
      public Optional<PublicKey> call() throws Exception {
        return keyServer.get( "0x" + keyId );
      }

      @Override
      protected void onSuccess( Optional<PublicKey> potentialKey ) throws Exception {
        if (potentialKey.isPresent()) {
          PublicKey key = potentialKey.get();

          keyIdTextView.setText( key.keyIdString() );
          qrCodeIV.setImageBitmap( buildQRCode( AcceptKeyActivity.this, humanReadableFingerprint( key ) ) );
          fingerprintTextView.setText( key.fingerprint() );

          currentKey = key;
        } else {
          Toast.makeText( AcceptKeyActivity.this, "Could not find the key",
              Toast.LENGTH_LONG ).show();
        }
      }

      @Override
      protected void onException( Exception e ) throws RuntimeException {
        Toast.makeText( AcceptKeyActivity.this,
            "Failed to load keys from server: " + e.getMessage(),
            Toast.LENGTH_LONG ).show();
      }

    }.execute();
  }

  @Override
  protected void onPause() {
    db.close();
    super.onPause();
  }
}
