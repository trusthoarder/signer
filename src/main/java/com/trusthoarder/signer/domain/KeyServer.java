package com.trusthoarder.signer.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Security;
import java.util.List;

import android.util.Log;
import com.trusthoarder.signer.infrastructure.Optional;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import static com.trusthoarder.signer.infrastructure.Optional.none;
import static com.trusthoarder.signer.infrastructure.Optional.some;
import static java.net.URLEncoder.encode;

public class KeyServer
{
  static {
    Security.insertProviderAt( new org.spongycastle.jce.provider.BouncyCastleProvider(), 1 );
  }

  private final HttpClient client;
  private final String getURI;
  private final String listURI;

  public KeyServer( String keyServer, HttpClient client ) {
    this.getURI = keyServer + "/pks/lookup?search=%s&op=get&options=mr&fingerprint=on";
    this.listURI = keyServer + "/pks/lookup?search=%s&op=vindex&options=mr";
    this.client = client;
  }

  public Optional<PublicKey> get( String keyIdOrFingerprint ) throws IOException, URISyntaxException {
    HttpGet req = new HttpGet();
    req.setURI( new URI( String.format( getURI, encode( keyIdOrFingerprint ) ) ) );

    HttpResponse response = client.execute( req );

    try {
      return some( PublicKey.deserialize( response.getEntity().getContent() ) );
    } catch( IllegalArgumentException e) {
      Log.w("signer", "Invalid key received from keyserver.");
      return none();
    }
  }

  public List<PublicKeyMeta> find( String searchString ) throws IOException, URISyntaxException {
    HttpGet req = new HttpGet();
    req.setURI( new URI( String.format( listURI, encode( searchString ))) );
    HttpResponse response = client.execute( req );
    BufferedReader content = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
    return new PGPMachineFormatReader().read( content );
  }
}
