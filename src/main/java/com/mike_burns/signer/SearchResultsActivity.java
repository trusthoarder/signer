package com.mike_burns.signer;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.mike_burns.signer.domain.KeyRepository;
import com.mike_burns.signer.domain.PGPKey;
import com.mike_burns.signer.infrastructure.SafeAsyncTask;
import com.mike_burns.signer.infrastructure.ui.BasicAdapter;
import org.apache.http.impl.client.DefaultHttpClient;

public class SearchResultsActivity extends ListActivity
{
    public final static String KEYID = "com.mike_burns.signer.KEYID";
    private final KeyRepository keys = new KeyRepository(
        "http://pgp.mit.edu", new DefaultHttpClient() );

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );

        Intent intent = getIntent();
        final String searchString = intent.getStringExtra( SigningActivity.SEARCH_STRING );

        setContentView( R.layout.search_results );
        loadSearchResults( searchString );

    }

    private void loadSearchResults( final String searchString )
    {
        new SafeAsyncTask<List<PGPKey>>()
        {
            @Override
            public List<PGPKey> call() throws Exception
            {
                return keys.find( searchString );
            }

            @Override
            protected void onSuccess( List<PGPKey> pgpKeys ) throws Exception
            {
                ListAdapter adapter = new BasicAdapter<PGPKey>( R.layout.search_result, pgpKeys )
                {
                    @Override
                    protected void render( PGPKey item, View view )
                    {
                        ((TextView) view.findViewById( R.id.keyid )).setText( item.keyId() );
                        ((TextView) view.findViewById( R.id.uid )).setText( item.uids().iterator().next().uidString() );
                    }
                };

                setListAdapter( adapter );
            }

            @Override
            protected void onException( Exception e ) throws RuntimeException
            {
                Toast.makeText( SearchResultsActivity.this,
                        "Failed to load keys from server: " + e.getMessage(), Toast.LENGTH_LONG ).show();
            }
        }.execute();
    }

    @Override
    protected void onListItemClick( ListView l, View v, int position, long id )
    {
        PGPKey item = (PGPKey) getListView().getItemAtPosition( position );

        Intent intent = new Intent( this, KeyActivity.class );
        intent.putExtra( KEYID, item.keyId() );

        startActivity( intent );
    }
}
