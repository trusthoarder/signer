package com.trusthoarder.signer.domain;

import android.content.ContentValues;
import android.database.Cursor;

import static com.trusthoarder.signer.domain.Database.Schema.user_keys_fingerprint;
import static com.trusthoarder.signer.domain.Database.Schema.user_keys_keyid;
import static com.trusthoarder.signer.domain.Database.Schema.user_keys_table;

public class UserKeys
{
  private final Database db;

  public UserKeys( Database db ) {
    this.db = db;
  }

  public void setUserKeyTo( Key key ) {
    Cursor result = db.query( user_keys_table, new String[]{"1"}, "fingerprint=?", new String[]{key.fingerprint()}, 1 );
    if(result.getCount() == 0) {
      db.insert( user_keys_table, contentValuesFor( key ) );
    }
    else if(result.getCount() == 1) {
      throw new UnsupportedOperationException("Sorry.");
    }
    else {
      throw new IllegalStateException( "Should never have more than one user key at a time." );
    }
  }

  private ContentValues contentValuesFor( Key key ) {
    ContentValues toInsert = new ContentValues();
    toInsert.put( user_keys_fingerprint, key.fingerprint() );
    toInsert.put( user_keys_keyid, key.keyIdString() );
    return toInsert;
  }
}
