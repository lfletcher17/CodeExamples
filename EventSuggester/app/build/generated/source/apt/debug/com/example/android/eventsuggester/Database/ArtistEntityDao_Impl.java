package com.example.android.eventsuggester.Database;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class ArtistEntityDao_Impl implements ArtistEntityDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfArtistEntity;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfArtistEntity;

  public ArtistEntityDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfArtistEntity = new EntityInsertionAdapter<ArtistEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `blacklisted_artist`(`id`,`artistId`,`artistName`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ArtistEntity value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getArtistId());
        if (value.getArtistName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getArtistName());
        }
      }
    };
    this.__deletionAdapterOfArtistEntity = new EntityDeletionOrUpdateAdapter<ArtistEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `blacklisted_artist` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, ArtistEntity value) {
        stmt.bindLong(1, value.getId());
      }
    };
  }

  @Override
  public void insertEvent(ArtistEntity artistEntity) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfArtistEntity.insert(artistEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteEvent(ArtistEntity artistEntity) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfArtistEntity.handle(artistEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<ArtistEntity> loadAllEvents() {
    final String _sql = "SELECT * FROM blacklisted_artist";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfArtistId = _cursor.getColumnIndexOrThrow("artistId");
      final int _cursorIndexOfArtistName = _cursor.getColumnIndexOrThrow("artistName");
      final List<ArtistEntity> _result = new ArrayList<ArtistEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final ArtistEntity _item;
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final int _tmpArtistId;
        _tmpArtistId = _cursor.getInt(_cursorIndexOfArtistId);
        final String _tmpArtistName;
        _tmpArtistName = _cursor.getString(_cursorIndexOfArtistName);
        _item = new ArtistEntity(_tmpId,_tmpArtistId,_tmpArtistName);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
