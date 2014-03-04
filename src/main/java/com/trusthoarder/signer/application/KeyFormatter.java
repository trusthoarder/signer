package com.trusthoarder.signer.application;

import java.lang.StringBuilder;
import com.trusthoarder.signer.domain.Key;

public class KeyFormatter implements Key {
  private final Key key;

  public KeyFormatter(Key key) {
    this.key = key;
  }
    
  public String fingerprint() {
    String unformattedFpr = this.key.fingerprint();
    StringBuilder formattedFpr = new StringBuilder();

    for (int i = 0; i < unformattedFpr.length(); i++) {
      formattedFpr.append(unformattedFpr.charAt(i));

      if (3 == i % 4) {
        formattedFpr.append(" ");
      }
    }

    return formattedFpr.toString();
  }

  public long keyId() {
    return this.key.keyId();
  }

  public String keyIdString() {
    return ("0x" + this.key.keyIdString());
  }

  public String friendlyName() {
    return this.key.friendlyName();
  }
}
