package com.trusthoarder.signer.domain;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KeyServerTest
{

  private final HttpClient http = mock( HttpClient.class );
  private final HttpResponse response = mock( HttpResponse.class );
  private final HttpEntity responseContent = mock( HttpEntity.class );

  @Test
  public void shouldGetKey() throws Exception {
    // Given
    when( http.execute( any( HttpGet.class ) ) ).thenReturn( response );
    when( response.getEntity() ).thenReturn( responseContent );
    when( responseContent.getContent() ).thenReturn( getClass().getResource( "/twokeys.pgp" ).openStream() );

    KeyServer repo = new KeyServer( "doesntmatter", http );

    // When
    PublicKey result = repo.get( "something" ).get();

    // Then
    assertThat( result.fingerprint(), equalTo( "C039183BDF5F3B0F479F6FE386C3657C10A2B20C" ) );
  }

  @Test
  public void shouldFindKeys() throws Exception
  {
    // Given
    when(http.execute( any(HttpGet.class) )).thenReturn( response );
    when(response.getEntity()).thenReturn( responseContent );
    when(responseContent.getContent()).thenReturn( new ByteArrayInputStream( MOCK_DATA.getBytes() ) );

    KeyServer repo = new KeyServer("doesntmatter", http );

    // When
    List<PublicKeyMeta> result = repo.find( "something" );

    // Then
    assertThat(result, equalTo( asList(
      new PublicKeyMeta(675721236, "Michael John Burns <mburns@thoughtbot.com>"),
      new PublicKeyMeta(558429095, "Michael A. Burns"))));
  }

  private final static String MOCK_DATA =
    "info:1:15\n" +
      "pub:2846B014:1:4096:1385546874::\n" +
      "uid:Michael John Burns <mike@mike-burns.com>:1385912280::\n" +
      "uid:Michael John Burns <mburns@thoughtbot.com>:1385547207::\n" +
      "uat::::\n" +
      "pub:2148F3A7:17:1024:1073927288::\n" +
      "uid:Michael A. Burns:1073927288::\n";
}
