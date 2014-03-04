package com.trusthoarder.signer.application;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.trusthoarder.signer.R;
import com.trusthoarder.signer.domain.KeyRepository;
import com.trusthoarder.signer.domain.PublicKey;
import com.trusthoarder.signer.domain.Key;
import com.trusthoarder.signer.infrastructure.Optional;
import com.trusthoarder.signer.infrastructure.SafeAsyncTask;

import org.apache.http.impl.client.DefaultHttpClient;

import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.WriterException;

public class KeyActivity extends Activity {
  private TextView keyIdTextView;
  private TextView fingerprintTextView;
  private ImageView qrCodeIV;
  private final KeyRepository keys = new KeyRepository(
      "http://pgp.mit.edu", new DefaultHttpClient() );

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView( R.layout.key);
    keyIdTextView = (TextView)findViewById(R.id.keyScreenKeyId);
    fingerprintTextView = (TextView)findViewById(R.id.keyScreenFingerprint);
    qrCodeIV = (ImageView)findViewById(R.id.qrCode);

    Intent intent = getIntent();
    String keyId = intent.getStringExtra(SearchResultsActivity.KEYID);

    fillInKeyDetails(keyId);
  }

  public void approve(View view) {
    TextView message = new TextView(this);
    message.setText("Approved");
    setContentView(message);
  }

  private void fillInKeyDetails(final String keyId) {
    new SafeAsyncTask<Optional<PublicKey>>() {
      @Override
      public Optional<PublicKey> call() throws Exception {
        return keys.get( "0x" + keyId );
      }

      @Override
      protected void onSuccess( Optional<PublicKey> potentialKey ) throws Exception {
        if (potentialKey.isPresent()) {
          Key pgpKey = potentialKey.get();
          Key formattedKey = new KeyFormatter(pgpKey);

          keyIdTextView.setText(formattedKey.keyIdString());
          qrCodeIV.setImageBitmap(buildQRCode(pgpKey.fingerprint()));
          fingerprintTextView.setText(formattedKey.fingerprint());
        } else {
          Toast.makeText( KeyActivity.this, "Could not find the key",
              Toast.LENGTH_LONG ).show();
        }
      }

      @Override
      protected void onException( Exception e ) throws RuntimeException {
        Toast.makeText( KeyActivity.this,
            "Failed to load keys from server: " + e.getMessage(),
            Toast.LENGTH_LONG ).show();
      }

    private Bitmap buildQRCode(String message) {
      int width = 200, height = 200;
      QRCodeWriter writer = new QRCodeWriter();
      Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

      try {
        BitMatrix output = writer.encode(
            message, BarcodeFormat.valueOf("QR_CODE"), width, height);

        for (int x = 0; x < width; x++)
          for (int y = 0; y < height; y++)
            bmp.setPixel(x, y, output.get(x,y) ? Color.BLACK : Color.WHITE);

      } catch (WriterException e) {
        Toast.makeText(KeyActivity.this, e.getMessage(), Toast.LENGTH_LONG);
      }

      return bmp;
    }

    }.execute();
  }
}
