package main.java.com.trusthoarder.signer.infrastructure.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCode {
  public static Bitmap buildQRCode(Context ctx, String message) {
    int width = 200, height = 200;
    QRCodeWriter writer = new QRCodeWriter();
    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

    try {
      BitMatrix output = writer.encode(
        message, BarcodeFormat.valueOf( "QR_CODE" ), width, height);

      for (int x = 0; x < width; x++)
        for (int y = 0; y < height; y++)
          bmp.setPixel(x, y, output.get(x,y) ? Color.BLACK : Color.WHITE);

    } catch (WriterException e) {
      Toast.makeText( ctx, e.getMessage(), Toast.LENGTH_LONG ).show();
    }

    return bmp;
  }
}
