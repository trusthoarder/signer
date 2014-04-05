package com.trusthoarder.signer.application;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.trusthoarder.signer.R;
import com.trusthoarder.signer.domain.Database;
import com.trusthoarder.signer.domain.PublicKey;
import com.trusthoarder.signer.domain.UserKeys;
import com.trusthoarder.signer.infrastructure.Optional;
import com.trusthoarder.signer.infrastructure.SafeAsyncTask;

import static com.trusthoarder.signer.application.FingerprintFormatter.humanReadableFingerprint;
import static com.trusthoarder.signer.infrastructure.ui.QRCode.buildQRCode;

public class DashboardActivity extends Activity {
  private Database db;
  private TextView keyIdText;
  private ImageView qrCodeIV;

  @Override
  protected void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );

    db = new Database( this );
    setContentView( R.layout.dashboard );
    keyIdText = (TextView) findViewById( R.id.keyid );
    qrCodeIV = (ImageView) findViewById( R.id.qrCode );

    new SafeAsyncTask<Optional<PublicKey>>(  ){
      @Override
      public Optional<PublicKey> call() throws Exception {
        return new UserKeys( db ).userKey();
      }

      @Override
      protected void onSuccess( Optional<PublicKey> userKey ) throws Exception {
        if(userKey.isPresent()) {
          PublicKey key = userKey.get();
          keyIdText.setText( key.keyIdString() );
          qrCodeIV.setImageBitmap( buildQRCode( DashboardActivity.this, humanReadableFingerprint( key ) ) );
        } else {
          onException(new IllegalStateException( "There is no user key set up." ));
        }
      }

      @Override
      protected void onException( Exception e ) throws RuntimeException {
        Log.e("signer", "Failed to load dashboard.", e);
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
