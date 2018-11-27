package com.example.android.eventsuggester.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.HashSet;

public class AppDatabase_Impl extends AppDatabase {
  private volatile EventEntityDao _eventEntityDao;

  private volatile ArtistEntityDao _artistEntityDao;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate() {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `attended_event` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `event_id` INTEGER)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `blacklisted_artist` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `artistId` INTEGER, `artistName` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"b9adef705dc72c50cf7376cfd9bc603c\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `attended_event`");
        _db.execSQL("DROP TABLE IF EXISTS `blacklisted_artist`");
      }

      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
      }

      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsAttendedEvent = new HashMap<String, TableInfo.Column>(2);
        _columnsAttendedEvent.put("id", new TableInfo.Column("id", "INTEGER", 1));
        _columnsAttendedEvent.put("event_id", new TableInfo.Column("event_id", "INTEGER", 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAttendedEvent = new HashSet<TableInfo.ForeignKey>(0);
        final TableInfo _infoAttendedEvent = new TableInfo("attended_event", _columnsAttendedEvent, _foreignKeysAttendedEvent);
        final TableInfo _existingAttendedEvent = TableInfo.read(_db, "attended_event");
        if (! _infoAttendedEvent.equals(_existingAttendedEvent)) {
          throw new IllegalStateException("Migration didn't properly handle attended_event(com.example.android.eventsuggester.Database.EventEntity).\n"
                  + " Expected:\n" + _infoAttendedEvent + "\n"
                  + " Found:\n" + _existingAttendedEvent);
        }
        final HashMap<String, TableInfo.Column> _columnsBlacklistedArtist = new HashMap<String, TableInfo.Column>(3);
        _columnsBlacklistedArtist.put("id", new TableInfo.Column("id", "INTEGER", 1));
        _columnsBlacklistedArtist.put("artistId", new TableInfo.Column("artistId", "INTEGER", 0));
        _columnsBlacklistedArtist.put("artistName", new TableInfo.Column("artistName", "TEXT", 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBlacklistedArtist = new HashSet<TableInfo.ForeignKey>(0);
        final TableInfo _infoBlacklistedArtist = new TableInfo("blacklisted_artist", _columnsBlacklistedArtist, _foreignKeysBlacklistedArtist);
        final TableInfo _existingBlacklistedArtist = TableInfo.read(_db, "blacklisted_artist");
        if (! _infoBlacklistedArtist.equals(_existingBlacklistedArtist)) {
          throw new IllegalStateException("Migration didn't properly handle blacklisted_artist(com.example.android.eventsuggester.Database.ArtistEntity).\n"
                  + " Expected:\n" + _infoBlacklistedArtist + "\n"
                  + " Found:\n" + _existingBlacklistedArtist);
        }
      }
    }, "b9adef705dc72c50cf7376cfd9bc603c");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .version(1)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "attended_event","blacklisted_artist");
  }

  @Override
  public EventEntityDao eventEntityDao() {
    if (_eventEntityDao != null) {
      return _eventEntityDao;
    } else {
      synchronized(this) {
        if(_eventEntityDao == null) {
          _eventEntityDao = new EventEntityDao_Impl(this);
        }
        return _eventEntityDao;
      }
    }
  }

  @Override
  public ArtistEntityDao artistEntityDao() {
    if (_artistEntityDao != null) {
      return _artistEntityDao;
    } else {
      synchronized(this) {
        if(_artistEntityDao == null) {
          _artistEntityDao = new ArtistEntityDao_Impl(this);
        }
        return _artistEntityDao;
      }
    }
  }
}
