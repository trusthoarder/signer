package main.java.com.trusthoarder.signer.domain;

/** Lightweight meta data about a public key. */
public class PublicKeyMeta {

  private final long keyId;
  private final String friendlyName;

  public PublicKeyMeta( long keyId, String friendlyName ) {
    this.keyId = keyId;
    this.friendlyName = friendlyName;
  }

  public long keyId() {
    return keyId;
  }

  public String keyIdString() {
    return Long.toHexString(keyId);
  }

  /** A "friendly" name for this key, such as a UID if one is available. */
  public String friendlyName() {
    return friendlyName;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) {
      return true;
    }
    if ( o == null || getClass() != o.getClass() ) {
      return false;
    }

    PublicKeyMeta that = (PublicKeyMeta) o;

    if ( keyId != that.keyId ) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return (int) (keyId ^ (keyId >>> 32));
  }

  @Override
  public String toString() {
    return "PublicKeyMeta{" +
      "keyId=" + keyId +
      ", friendlyName='" + friendlyName + '\'' +
      '}';
  }
}
