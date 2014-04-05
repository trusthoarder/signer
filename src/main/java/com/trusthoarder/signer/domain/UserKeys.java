package com.trusthoarder.signer.domain;

import java.io.IOException;

import android.content.ContentValues;
import android.database.Cursor;
import com.trusthoarder.signer.infrastructure.Optional;

import static com.trusthoarder.signer.domain.Database.Schema.user_keys_fingerprint;
import static com.trusthoarder.signer.domain.Database.Schema.user_keys_key;
import static com.trusthoarder.signer.domain.Database.Schema.user_keys_keyid;
import static com.trusthoarder.signer.domain.Database.Schema.user_keys_table;

public class UserKeys {

  private final Database db;

  public UserKeys( Database db ) {
    this.db = db;
  }

  public void setUserKeyTo( PublicKey key ) {
    if(userKey().isPresent()) {
      db.insert( user_keys_table, contentValuesFor( key ) );
    }
    else {
      db.delete( user_keys_table, "1=1", new String[]{} );
      db.insert( user_keys_table, contentValuesFor( key ) );
    }
  }

  public boolean isSetup() {
    return userKey().isPresent();
  }

  public Optional<PublicKey> userKey() {
    Cursor result = db.query( user_keys_table, new String[]{user_keys_key}, null, null, 2 );
    if(result.getCount() == 0) {
      return Optional.none();
    }
    else if(result.getCount() == 1) {
      result.moveToFirst();
      try {
        return Optional.some( PublicKey.deserialize( result.getBlob( result.getColumnIndex( user_keys_key ) ) ) );
      }
      catch ( IOException e ) {
        throw new IllegalStateException( "Unable to load user key.", e );
      }
    }
    else {
      throw new IllegalStateException( "Should never have more than one user key at a time." );
    }
  }

  private ContentValues contentValuesFor( PublicKey key ) {
    ContentValues toInsert = new ContentValues();
    toInsert.put( user_keys_fingerprint, key.fingerprint() );
    toInsert.put( user_keys_keyid, key.keyIdString() );
    toInsert.put( user_keys_key, key.serialize() );
    return toInsert;
  }
}
