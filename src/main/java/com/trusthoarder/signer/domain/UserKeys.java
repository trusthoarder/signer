package com.trusthoarder.signer.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import com.trusthoarder.signer.infrastructure.Optional;

import static com.trusthoarder.signer.domain.Database.Schema.user_keys_fingerprint;
import static com.trusthoarder.signer.domain.Database.Schema.user_keys_keyid;
import static com.trusthoarder.signer.domain.Database.Schema.user_keys_table;

public class UserKeys
{
  // Note: We may want to store the "real" key in the db and have spongy castle handle this
  public static class UserKey implements Key
  {
    private String fingerprint;
    private String keyId;

    public UserKey( String fingerprint, String keyId ) {
      this.fingerprint = fingerprint;
      this.keyId = keyId;
    }

    @Override
    public String fingerprint() {
      return fingerprint;
    }

    @Override
    public long keyId() {
      return 0;
    }

    @Override
    public String keyIdString() {
      return keyId;
    }

    @Override
    public String friendlyName() {
      return null;
    }
  }

  private final Database db;

  public UserKeys( Database db ) {
    this.db = db;
  }

  public void setUserKeyTo( Key key ) {
    if(userKey().isPresent()) {
      db.insert( user_keys_table, contentValuesFor( key ) );
    }
    else {
      throw new UnsupportedOperationException("Sorry.");
    }
  }

  public boolean isSetup() {
    return userKey().isPresent();
  }

  public Optional<Key> userKey() {
    Log.i("signer", "Querying..");
    Cursor result = db.query( user_keys_table, new String[]{"*"}, null, null, 2 );
    if(result.getCount() == 0) {
      return Optional.none();
    }
    else if(result.getCount() == 1) {
      result.moveToFirst();
      return Optional.some( (Key)new UserKey(
        result.getString( result.getColumnIndex( user_keys_fingerprint ) ),
        result.getString( result.getColumnIndex( user_keys_keyid ) )
      ));
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
