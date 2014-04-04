package com.trusthoarder.signer.domain;

import com.trusthoarder.signer.infrastructure.Strings;
import org.spongycastle.openpgp.PGPPublicKeyRing;

/** The domain layer mapping of a public PGP key. Backed by a regular
 * bouncy castle PGPPublicKeyRing. */
public class SpongyBackedKey extends PublicKeyMeta implements Key {

  private final PGPPublicKeyRing rawKeyRing;

  public SpongyBackedKey( PGPPublicKeyRing rawKeyRing ) {
    super(rawKeyRing.getPublicKey().getKeyID(), "N/A");
    this.rawKeyRing = rawKeyRing;
  }

  public String fingerprint() {
    return Strings.bytesToHex( rawKeyRing.getPublicKey().getFingerprint() );
  }
}
