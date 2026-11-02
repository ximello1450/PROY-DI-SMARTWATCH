package com.utn.health.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.utn.health.data.local.Converters;
import com.utn.health.data.local.entity.SyncStatusEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SyncStatusDao_Impl implements SyncStatusDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SyncStatusEntity> __insertionAdapterOfSyncStatusEntity;

  private final Converters __converters = new Converters();

  public SyncStatusDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSyncStatusEntity = new EntityInsertionAdapter<SyncStatusEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `estado_sincronizacion` (`id`,`permisosConcedidos`,`healthConnectDisponible`,`ultimaSincronizacion`,`fuentesDetectadas`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SyncStatusEntity entity) {
        statement.bindLong(1, entity.getId());
        final int _tmp = entity.getPermisosConcedidos() ? 1 : 0;
        statement.bindLong(2, _tmp);
        final int _tmp_1 = entity.getHealthConnectDisponible() ? 1 : 0;
        statement.bindLong(3, _tmp_1);
        final Long _tmp_2 = __converters.dateToTimestamp(entity.getUltimaSincronizacion());
        if (_tmp_2 == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp_2);
        }
        statement.bindString(5, entity.getFuentesDetectadas());
      }
    };
  }

  @Override
  public Object insertOrUpdate(final SyncStatusEntity status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSyncStatusEntity.insert(status);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<SyncStatusEntity> getStatus() {
    final String _sql = "SELECT * FROM estado_sincronizacion WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"estado_sincronizacion"}, new Callable<SyncStatusEntity>() {
      @Override
      @Nullable
      public SyncStatusEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPermisosConcedidos = CursorUtil.getColumnIndexOrThrow(_cursor, "permisosConcedidos");
          final int _cursorIndexOfHealthConnectDisponible = CursorUtil.getColumnIndexOrThrow(_cursor, "healthConnectDisponible");
          final int _cursorIndexOfUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaSincronizacion");
          final int _cursorIndexOfFuentesDetectadas = CursorUtil.getColumnIndexOrThrow(_cursor, "fuentesDetectadas");
          final SyncStatusEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final boolean _tmpPermisosConcedidos;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfPermisosConcedidos);
            _tmpPermisosConcedidos = _tmp != 0;
            final boolean _tmpHealthConnectDisponible;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHealthConnectDisponible);
            _tmpHealthConnectDisponible = _tmp_1 != 0;
            final Instant _tmpUltimaSincronizacion;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfUltimaSincronizacion)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfUltimaSincronizacion);
            }
            _tmpUltimaSincronizacion = __converters.fromTimestamp(_tmp_2);
            final String _tmpFuentesDetectadas;
            _tmpFuentesDetectadas = _cursor.getString(_cursorIndexOfFuentesDetectadas);
            _result = new SyncStatusEntity(_tmpId,_tmpPermisosConcedidos,_tmpHealthConnectDisponible,_tmpUltimaSincronizacion,_tmpFuentesDetectadas);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getStatusSync(final Continuation<? super SyncStatusEntity> $completion) {
    final String _sql = "SELECT * FROM estado_sincronizacion WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SyncStatusEntity>() {
      @Override
      @Nullable
      public SyncStatusEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPermisosConcedidos = CursorUtil.getColumnIndexOrThrow(_cursor, "permisosConcedidos");
          final int _cursorIndexOfHealthConnectDisponible = CursorUtil.getColumnIndexOrThrow(_cursor, "healthConnectDisponible");
          final int _cursorIndexOfUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaSincronizacion");
          final int _cursorIndexOfFuentesDetectadas = CursorUtil.getColumnIndexOrThrow(_cursor, "fuentesDetectadas");
          final SyncStatusEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final boolean _tmpPermisosConcedidos;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfPermisosConcedidos);
            _tmpPermisosConcedidos = _tmp != 0;
            final boolean _tmpHealthConnectDisponible;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfHealthConnectDisponible);
            _tmpHealthConnectDisponible = _tmp_1 != 0;
            final Instant _tmpUltimaSincronizacion;
            final Long _tmp_2;
            if (_cursor.isNull(_cursorIndexOfUltimaSincronizacion)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getLong(_cursorIndexOfUltimaSincronizacion);
            }
            _tmpUltimaSincronizacion = __converters.fromTimestamp(_tmp_2);
            final String _tmpFuentesDetectadas;
            _tmpFuentesDetectadas = _cursor.getString(_cursorIndexOfFuentesDetectadas);
            _result = new SyncStatusEntity(_tmpId,_tmpPermisosConcedidos,_tmpHealthConnectDisponible,_tmpUltimaSincronizacion,_tmpFuentesDetectadas);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
