package com.mike_burns.signer.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import static java.net.URLEncoder.encode;

public class KeyRepository
{
  private final String keyServer;
  private final HttpClient client;

  public KeyRepository(String keyServer, HttpClient client)
  {
      this.keyServer = keyServer;
      this.client = client;
  }

  public List<PGPKey> find( String searchString ) throws IOException, URISyntaxException
  {
      HttpGet req = new HttpGet();
      req.setURI( new URI( String.format( keyServer + "/pks/lookup?search=%s&op=vindex&options=mr", encode( searchString ))) );
      HttpResponse response = client.execute( req );
      BufferedReader content = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
      return new PGPMachineFormatReader().read( content );
  }
}
