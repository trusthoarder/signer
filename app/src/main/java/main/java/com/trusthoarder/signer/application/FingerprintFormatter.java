package main.java.com.trusthoarder.signer.application;

import main.java.com.trusthoarder.signer.domain.Key;

public class FingerprintFormatter {
  public static String humanReadableFingerprint(Key key) {
    String unformattedFpr = key.fingerprint();
    StringBuilder formattedFpr = new StringBuilder();

    for (int i = 0; i < unformattedFpr.length(); i++) {
      formattedFpr.append(unformattedFpr.charAt(i));

      if (3 == i % 4) {
        formattedFpr.append(" ");
      }
    }

    return formattedFpr.toString();
  }
}
