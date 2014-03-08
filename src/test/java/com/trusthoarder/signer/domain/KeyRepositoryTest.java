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

public class KeyRepositoryTest
{

    @Test
    public void shouldFindKeys() throws Exception
    {
        // Given
        HttpClient http = mock( HttpClient.class );
        HttpResponse response = mock(HttpResponse.class);
        HttpEntity responseContent = mock( HttpEntity.class );

        when(http.execute( any(HttpGet.class) )).thenReturn( response );
        when(response.getEntity()).thenReturn( responseContent );
        when(responseContent.getContent()).thenReturn( new ByteArrayInputStream( MOCK_DATA.getBytes() ) );

        KeyRepository repo = new KeyRepository("doesntmatter", http );

        // When
        List<PGPKey> result = repo.find( "something" );

        // Then
        assertThat(result, equalTo( asList(
                new PGPKey("2846B014", PGPFlag.VALID, 1, 4096, 1385546874l, -1l, asList(
                        new PGPUID( "Michael John Burns <mike@mike-burns.com>", 1385912280, -1, PGPFlag.VALID ),
                        new PGPUID( "Michael John Burns <mburns@thoughtbot.com>", 1385547207, -1, PGPFlag.VALID )
                ) ),
                new PGPKey("2148F3A7", PGPFlag.VALID, 17, 1024, 1073927288, -1l, asList(
                        new PGPUID( "Michael A. Burns", 1073927288, -1, PGPFlag.VALID )
                )))));
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
