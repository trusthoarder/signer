package main.java.com.trusthoarder.signer.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.trusthoarder.signer.R;
import main.java.com.trusthoarder.signer.domain.Database;
import main.java.com.trusthoarder.signer.domain.PublicKey;
import main.java.com.trusthoarder.signer.domain.UserKeys;
import main.java.com.trusthoarder.signer.infrastructure.Optional;
import main.java.com.trusthoarder.signer.infrastructure.SafeAsyncTask;

import static main.java.com.trusthoarder.signer.application.FingerprintFormatter.humanReadableFingerprint;
import static main.java.com.trusthoarder.signer.application.SearchResultsActivity.KEYID;
import static main.java.com.trusthoarder.signer.application.SearchResultsActivity.SEARCH_STRING;
import static main.java.com.trusthoarder.signer.infrastructure.ui.QRCode.buildQRCode;

public class DashboardActivity extends Activity {

  private static final int QR_SCAN_REQUEST = 1;
  private static final int MANUAL_SEARCH_REQUEST = 2;
  private static final int VERIFICATION_TUTORIAL_REQUEST = 3;

  private Database db;
  private TextView keyIdText;
  private ImageView qrCodeIV;
  private EditText searchText;

  @Override
  protected void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );

    db = new Database( this );
    setContentView( R.layout.dashboard );
    keyIdText = (TextView) findViewById( R.id.keyid );
    qrCodeIV = (ImageView) findViewById( R.id.qrCode );
    searchText = (EditText) findViewById( R.id.searchString );

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
          new IllegalStateException( "There is no user key set up." );
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

  public void onScanClicked(View view) {
    try {
      Intent intent = new Intent( "com.google.zxing.client.android.SCAN" );
      intent.putExtra( "SCAN_MODE", "QR_CODE_MODE" );
      startActivityForResult( intent, QR_SCAN_REQUEST );
    } catch(Exception e)
    {
      Log.e( "signer", "Unable to launch QR scanned.", e );
    }
  }

  public void onSearchClicked(View view) {
    Intent intent = new Intent(this, SearchResultsActivity.class);
    intent.putExtra( SEARCH_STRING, searchText.getText().toString() );
    startActivityForResult( intent, MANUAL_SEARCH_REQUEST );
  }

  @Override
  protected void onActivityResult( int requestCode, int resultCode, Intent intent ) {
    if (resultCode == RESULT_OK) {
      switch ( requestCode ) {
        case QR_SCAN_REQUEST:
          runKeyVerificationTutorial( intent.getStringExtra( "SCAN_RESULT" ) );
          return;
        case MANUAL_SEARCH_REQUEST:
          runKeyVerificationTutorial( intent.getStringExtra( KEYID ) );
          return;
        case VERIFICATION_TUTORIAL_REQUEST:
          // Nothing to do, just show the dashboard
          return;
      }
    } else if (resultCode == RESULT_CANCELED) {
      Log.w( "signer", requestCode + ", " + intent );
      Toast.makeText( this, "Scan was Cancelled!", Toast.LENGTH_LONG).show();
      return;
    }

    Log.e( "signer", "Don't know how to handle result: " + requestCode );
  }

  private void runKeyVerificationTutorial( String keyid ) {
    try {
      Intent intent = new Intent( this, VerificationProcessActivity.class );
      intent.putExtra( VerificationProcessActivity.KEYID, keyid );
      startActivityForResult( intent, VERIFICATION_TUTORIAL_REQUEST );
    } catch(Exception e)
    {
      Log.e( "signer", "A", e );
    }
  }

  public void onSignOutClicked( View view ) {
    new SafeAsyncTask<Void>(  ){
      @Override
      public Void call() throws Exception {
        new UserKeys( db ).deleteUserKey();
        return null;
      }

      @Override
      protected void onSuccess( Void ignore ) throws Exception {
        startActivity(new Intent(DashboardActivity.this, SetupActivity.class));
        finish();
      }

      @Override
      protected void onException( Exception e ) throws RuntimeException {
        Log.e("signer", "Failed to sign out.", e);
        Toast.makeText( DashboardActivity.this,
          "Failed to sign out: " + e.getMessage(), Toast.LENGTH_LONG ).show();
      }
    }.execute();
  }
}
