package com.trusthoarder.signer.domain;

public interface Key {
  public String fingerprint();
  public long keyId();
  public String keyIdString();
  public String friendlyName();
}
