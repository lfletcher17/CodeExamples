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

public class EventEntityDao_Impl implements EventEntityDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfEventEntity;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfEventEntity;

  public EventEntityDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEventEntity = new EntityInsertionAdapter<EventEntity>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `attended_event`(`id`,`event_id`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, EventEntity value) {
        stmt.bindLong(1, value.getId());
        stmt.bindLong(2, value.getEventId());
      }
    };
    this.__deletionAdapterOfEventEntity = new EntityDeletionOrUpdateAdapter<EventEntity>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `attended_event` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, EventEntity value) {
        stmt.bindLong(1, value.getId());
      }
    };
  }

  @Override
  public void insertEvent(EventEntity eventEntity) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfEventEntity.insert(eventEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteEvent(EventEntity eventEntity) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfEventEntity.handle(eventEntity);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<EventEntity> loadAllEvents() {
    final String _sql = "SELECT * FROM attended_event";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfEventId = _cursor.getColumnIndexOrThrow("event_id");
      final List<EventEntity> _result = new ArrayList<EventEntity>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final EventEntity _item;
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final int _tmpEventId;
        _tmpEventId = _cursor.getInt(_cursorIndexOfEventId);
        _item = new EventEntity(_tmpId,_tmpEventId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public EventEntity loadEvent(String eventID) {
    final String _sql = "SELECT * FROM attended_event WHERE event_id == ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (eventID == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, eventID);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfEventId = _cursor.getColumnIndexOrThrow("event_id");
      final EventEntity _result;
      if(_cursor.moveToFirst()) {
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final int _tmpEventId;
        _tmpEventId = _cursor.getInt(_cursorIndexOfEventId);
        _result = new EventEntity(_tmpId,_tmpEventId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
