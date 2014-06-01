package main.java.com.trusthoarder.signer.domain;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import main.java.com.trusthoarder.signer.infrastructure.Strings;
import org.spongycastle.openpgp.PGPObjectFactory;
import org.spongycastle.openpgp.PGPPublicKeyRing;
import org.spongycastle.openpgp.PGPUtil;

/** The domain layer mapping of a public PGP key. Backed by a regular
 * bouncy castle PGPPublicKeyRing. */
public class PublicKey extends PublicKeyMeta implements Key {

  private final PGPPublicKeyRing rawKeyRing;

  public PublicKey( PGPPublicKeyRing rawKeyRing ) {
    super(rawKeyRing.getPublicKey().getKeyID(), "N/A");
    this.rawKeyRing = rawKeyRing;
  }

  public String fingerprint() {
    return Strings.bytesToHex( rawKeyRing.getPublicKey().getFingerprint() );
  }

  public byte[] serialize() {
    try {
      return rawKeyRing.getEncoded();
    }
    catch ( IOException e ) {
      throw new RuntimeException( "Failed to serialize key.", e );
    }
  }

  public static PublicKey deserialize( byte[] serialized ) throws IOException {
    return deserialize( new ByteArrayInputStream( serialized ) );
  }

  public static PublicKey deserialize( InputStream serialized ) throws IOException {
    PGPObjectFactory factory = new PGPObjectFactory( PGPUtil.getDecoderStream( serialized ));
    for(Object obj = factory.nextObject(); obj != null; obj = factory.nextObject()) {
      if(obj instanceof PGPPublicKeyRing ) {
        return new PublicKey( (PGPPublicKeyRing) obj );
      }
    }

    throw new IllegalArgumentException( "Data provided is not a serialized public key." );
  }
}
