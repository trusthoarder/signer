package main.java.com.trusthoarder.signer.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper
{
  private final static String DATABASE_NAME = "signer.db";
  private final static int DATABASE_VERSION = 1;

  public static final class Schema
  {
    public static String user_keys_table       = "user_keys";
    public static String user_keys_id          = "_id";
    public static String user_keys_fingerprint = "fingerprint";
    public static String user_keys_keyid       = "keyid";
    public static String user_keys_key         = "key";
  }

  public Database( Context context ) {
    super( context, DATABASE_NAME, null, DATABASE_VERSION );
  }

  @Override
  public void onCreate( SQLiteDatabase db ) {
    db.execSQL( "create table " + Schema.user_keys_table + "("
      + Schema.user_keys_id + " integer primary key autoincrement, "
      + Schema.user_keys_keyid + " text not null, "
      + Schema.user_keys_fingerprint + " text not null, "
      + Schema.user_keys_key + " blob);");
  }

  @Override
  public void onUpgrade( SQLiteDatabase db, int i, int i2 ) {
    // Do any upgrade for database here
  }

  public Cursor query(String table, String[] columns, String where, String[] parameters, Integer limit) {
    return getReadableDatabase().query( table, columns, where, parameters, null, null, null, limit.toString() );
  }

  public void insert( String tableName, ContentValues values ) {
    getWritableDatabase().insert( tableName, null, values );
  }

  public void delete(String table, String where, String[] parameters) {
    getWritableDatabase().delete( table, where, parameters );
  }
}
