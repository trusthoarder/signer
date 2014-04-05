package com.trusthoarder.signer.application;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.trusthoarder.signer.R;
import com.trusthoarder.signer.domain.KeyServer;
import com.trusthoarder.signer.domain.PublicKeyMeta;
import com.trusthoarder.signer.infrastructure.SafeAsyncTask;
import com.trusthoarder.signer.infrastructure.ui.BasicAdapter;

public class SearchResultsActivity extends ListActivity {
  public final static String SEARCH_STRING = "com.trusthoarder.signer.SEARCH_STRING";

  public final static String KEYID = "com.trusthoarder.signer.KEYID";
  private final KeyServer keys = new KeyServer();

  @Override
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );

    final String searchString = getIntent().getStringExtra( SEARCH_STRING );

    setContentView( R.layout.search_results );
    loadSearchResults( searchString );

  }

  private void loadSearchResults( final String searchString ) {
    new SafeAsyncTask<List<PublicKeyMeta>>() {
      @Override
      public List<PublicKeyMeta> call() throws Exception {
        return keys.find( searchString );
      }

      @Override
      protected void onSuccess( List<PublicKeyMeta> pgpKeys ) throws Exception {
        ListAdapter adapter = new BasicAdapter<PublicKeyMeta>( R.layout.search_result, pgpKeys ) {
          @Override
          protected void render( PublicKeyMeta item, View view ) {
            ((TextView) view.findViewById( R.id.keyid )).setText( item.keyIdString() );
            ((TextView) view.findViewById( R.id.uid )).setText( item.friendlyName() );
          }
        };

        setListAdapter( adapter );
      }

      @Override
      protected void onException( Exception e ) throws RuntimeException {
        Toast.makeText( SearchResultsActivity.this,
          "Failed to load keys from server: " + e.getMessage(), Toast.LENGTH_LONG ).show();
      }
    }.execute();
  }

  @Override
  protected void onListItemClick( ListView l, View v, int position, long id ) {
    PublicKeyMeta item = (PublicKeyMeta) getListView().getItemAtPosition( position );

    Intent intent = new Intent();
    intent.putExtra( KEYID, item.keyIdString() );
    setResult( RESULT_OK, intent );
    finish();
  }
}
