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
import com.utn.health.data.local.entity.DailySummaryEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DailySummaryDao_Impl implements DailySummaryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DailySummaryEntity> __insertionAdapterOfDailySummaryEntity;

  private final Converters __converters = new Converters();

  public DailySummaryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDailySummaryEntity = new EntityInsertionAdapter<DailySummaryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `resumen_diario` (`fecha`,`pasos`,`distanciaMetros`,`caloriasKcal`,`fcPromedio`,`fcMin`,`fcMax`,`minutosSueno`,`ultimaSincronizacion`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DailySummaryEntity entity) {
        statement.bindString(1, entity.getFecha());
        if (entity.getPasos() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getPasos());
        }
        if (entity.getDistanciaMetros() == null) {
          statement.bindNull(3);
        } else {
          statement.bindDouble(3, entity.getDistanciaMetros());
        }
        if (entity.getCaloriasKcal() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getCaloriasKcal());
        }
        if (entity.getFcPromedio() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getFcPromedio());
        }
        if (entity.getFcMin() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getFcMin());
        }
        if (entity.getFcMax() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getFcMax());
        }
        if (entity.getMinutosSueno() == null) {
          statement.bindNull(8);
        } else {
          statement.bindLong(8, entity.getMinutosSueno());
        }
        final Long _tmp = __converters.dateToTimestamp(entity.getUltimaSincronizacion());
        if (_tmp == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, _tmp);
        }
      }
    };
  }

  @Override
  public Object insertOrUpdate(final DailySummaryEntity summary,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDailySummaryEntity.insert(summary);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<DailySummaryEntity> getSummaryByDate(final String date) {
    final String _sql = "SELECT * FROM resumen_diario WHERE fecha = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"resumen_diario"}, new Callable<DailySummaryEntity>() {
      @Override
      @Nullable
      public DailySummaryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfPasos = CursorUtil.getColumnIndexOrThrow(_cursor, "pasos");
          final int _cursorIndexOfDistanciaMetros = CursorUtil.getColumnIndexOrThrow(_cursor, "distanciaMetros");
          final int _cursorIndexOfCaloriasKcal = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriasKcal");
          final int _cursorIndexOfFcPromedio = CursorUtil.getColumnIndexOrThrow(_cursor, "fcPromedio");
          final int _cursorIndexOfFcMin = CursorUtil.getColumnIndexOrThrow(_cursor, "fcMin");
          final int _cursorIndexOfFcMax = CursorUtil.getColumnIndexOrThrow(_cursor, "fcMax");
          final int _cursorIndexOfMinutosSueno = CursorUtil.getColumnIndexOrThrow(_cursor, "minutosSueno");
          final int _cursorIndexOfUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaSincronizacion");
          final DailySummaryEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpFecha;
            _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            final Long _tmpPasos;
            if (_cursor.isNull(_cursorIndexOfPasos)) {
              _tmpPasos = null;
            } else {
              _tmpPasos = _cursor.getLong(_cursorIndexOfPasos);
            }
            final Double _tmpDistanciaMetros;
            if (_cursor.isNull(_cursorIndexOfDistanciaMetros)) {
              _tmpDistanciaMetros = null;
            } else {
              _tmpDistanciaMetros = _cursor.getDouble(_cursorIndexOfDistanciaMetros);
            }
            final Double _tmpCaloriasKcal;
            if (_cursor.isNull(_cursorIndexOfCaloriasKcal)) {
              _tmpCaloriasKcal = null;
            } else {
              _tmpCaloriasKcal = _cursor.getDouble(_cursorIndexOfCaloriasKcal);
            }
            final Long _tmpFcPromedio;
            if (_cursor.isNull(_cursorIndexOfFcPromedio)) {
              _tmpFcPromedio = null;
            } else {
              _tmpFcPromedio = _cursor.getLong(_cursorIndexOfFcPromedio);
            }
            final Long _tmpFcMin;
            if (_cursor.isNull(_cursorIndexOfFcMin)) {
              _tmpFcMin = null;
            } else {
              _tmpFcMin = _cursor.getLong(_cursorIndexOfFcMin);
            }
            final Long _tmpFcMax;
            if (_cursor.isNull(_cursorIndexOfFcMax)) {
              _tmpFcMax = null;
            } else {
              _tmpFcMax = _cursor.getLong(_cursorIndexOfFcMax);
            }
            final Long _tmpMinutosSueno;
            if (_cursor.isNull(_cursorIndexOfMinutosSueno)) {
              _tmpMinutosSueno = null;
            } else {
              _tmpMinutosSueno = _cursor.getLong(_cursorIndexOfMinutosSueno);
            }
            final Instant _tmpUltimaSincronizacion;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfUltimaSincronizacion)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfUltimaSincronizacion);
            }
            _tmpUltimaSincronizacion = __converters.fromTimestamp(_tmp);
            _result = new DailySummaryEntity(_tmpFecha,_tmpPasos,_tmpDistanciaMetros,_tmpCaloriasKcal,_tmpFcPromedio,_tmpFcMin,_tmpFcMax,_tmpMinutosSueno,_tmpUltimaSincronizacion);
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
  public Object getSummaryByDateSync(final String date,
      final Continuation<? super DailySummaryEntity> $completion) {
    final String _sql = "SELECT * FROM resumen_diario WHERE fecha = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DailySummaryEntity>() {
      @Override
      @Nullable
      public DailySummaryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfPasos = CursorUtil.getColumnIndexOrThrow(_cursor, "pasos");
          final int _cursorIndexOfDistanciaMetros = CursorUtil.getColumnIndexOrThrow(_cursor, "distanciaMetros");
          final int _cursorIndexOfCaloriasKcal = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriasKcal");
          final int _cursorIndexOfFcPromedio = CursorUtil.getColumnIndexOrThrow(_cursor, "fcPromedio");
          final int _cursorIndexOfFcMin = CursorUtil.getColumnIndexOrThrow(_cursor, "fcMin");
          final int _cursorIndexOfFcMax = CursorUtil.getColumnIndexOrThrow(_cursor, "fcMax");
          final int _cursorIndexOfMinutosSueno = CursorUtil.getColumnIndexOrThrow(_cursor, "minutosSueno");
          final int _cursorIndexOfUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaSincronizacion");
          final DailySummaryEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpFecha;
            _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            final Long _tmpPasos;
            if (_cursor.isNull(_cursorIndexOfPasos)) {
              _tmpPasos = null;
            } else {
              _tmpPasos = _cursor.getLong(_cursorIndexOfPasos);
            }
            final Double _tmpDistanciaMetros;
            if (_cursor.isNull(_cursorIndexOfDistanciaMetros)) {
              _tmpDistanciaMetros = null;
            } else {
              _tmpDistanciaMetros = _cursor.getDouble(_cursorIndexOfDistanciaMetros);
            }
            final Double _tmpCaloriasKcal;
            if (_cursor.isNull(_cursorIndexOfCaloriasKcal)) {
              _tmpCaloriasKcal = null;
            } else {
              _tmpCaloriasKcal = _cursor.getDouble(_cursorIndexOfCaloriasKcal);
            }
            final Long _tmpFcPromedio;
            if (_cursor.isNull(_cursorIndexOfFcPromedio)) {
              _tmpFcPromedio = null;
            } else {
              _tmpFcPromedio = _cursor.getLong(_cursorIndexOfFcPromedio);
            }
            final Long _tmpFcMin;
            if (_cursor.isNull(_cursorIndexOfFcMin)) {
              _tmpFcMin = null;
            } else {
              _tmpFcMin = _cursor.getLong(_cursorIndexOfFcMin);
            }
            final Long _tmpFcMax;
            if (_cursor.isNull(_cursorIndexOfFcMax)) {
              _tmpFcMax = null;
            } else {
              _tmpFcMax = _cursor.getLong(_cursorIndexOfFcMax);
            }
            final Long _tmpMinutosSueno;
            if (_cursor.isNull(_cursorIndexOfMinutosSueno)) {
              _tmpMinutosSueno = null;
            } else {
              _tmpMinutosSueno = _cursor.getLong(_cursorIndexOfMinutosSueno);
            }
            final Instant _tmpUltimaSincronizacion;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfUltimaSincronizacion)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfUltimaSincronizacion);
            }
            _tmpUltimaSincronizacion = __converters.fromTimestamp(_tmp);
            _result = new DailySummaryEntity(_tmpFecha,_tmpPasos,_tmpDistanciaMetros,_tmpCaloriasKcal,_tmpFcPromedio,_tmpFcMin,_tmpFcMax,_tmpMinutosSueno,_tmpUltimaSincronizacion);
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

  @Override
  public Flow<List<DailySummaryEntity>> getRecentSummaries(final int limit) {
    final String _sql = "SELECT * FROM resumen_diario ORDER BY fecha DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"resumen_diario"}, new Callable<List<DailySummaryEntity>>() {
      @Override
      @NonNull
      public List<DailySummaryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfFecha = CursorUtil.getColumnIndexOrThrow(_cursor, "fecha");
          final int _cursorIndexOfPasos = CursorUtil.getColumnIndexOrThrow(_cursor, "pasos");
          final int _cursorIndexOfDistanciaMetros = CursorUtil.getColumnIndexOrThrow(_cursor, "distanciaMetros");
          final int _cursorIndexOfCaloriasKcal = CursorUtil.getColumnIndexOrThrow(_cursor, "caloriasKcal");
          final int _cursorIndexOfFcPromedio = CursorUtil.getColumnIndexOrThrow(_cursor, "fcPromedio");
          final int _cursorIndexOfFcMin = CursorUtil.getColumnIndexOrThrow(_cursor, "fcMin");
          final int _cursorIndexOfFcMax = CursorUtil.getColumnIndexOrThrow(_cursor, "fcMax");
          final int _cursorIndexOfMinutosSueno = CursorUtil.getColumnIndexOrThrow(_cursor, "minutosSueno");
          final int _cursorIndexOfUltimaSincronizacion = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaSincronizacion");
          final List<DailySummaryEntity> _result = new ArrayList<DailySummaryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DailySummaryEntity _item;
            final String _tmpFecha;
            _tmpFecha = _cursor.getString(_cursorIndexOfFecha);
            final Long _tmpPasos;
            if (_cursor.isNull(_cursorIndexOfPasos)) {
              _tmpPasos = null;
            } else {
              _tmpPasos = _cursor.getLong(_cursorIndexOfPasos);
            }
            final Double _tmpDistanciaMetros;
            if (_cursor.isNull(_cursorIndexOfDistanciaMetros)) {
              _tmpDistanciaMetros = null;
            } else {
              _tmpDistanciaMetros = _cursor.getDouble(_cursorIndexOfDistanciaMetros);
            }
            final Double _tmpCaloriasKcal;
            if (_cursor.isNull(_cursorIndexOfCaloriasKcal)) {
              _tmpCaloriasKcal = null;
            } else {
              _tmpCaloriasKcal = _cursor.getDouble(_cursorIndexOfCaloriasKcal);
            }
            final Long _tmpFcPromedio;
            if (_cursor.isNull(_cursorIndexOfFcPromedio)) {
              _tmpFcPromedio = null;
            } else {
              _tmpFcPromedio = _cursor.getLong(_cursorIndexOfFcPromedio);
            }
            final Long _tmpFcMin;
            if (_cursor.isNull(_cursorIndexOfFcMin)) {
              _tmpFcMin = null;
            } else {
              _tmpFcMin = _cursor.getLong(_cursorIndexOfFcMin);
            }
            final Long _tmpFcMax;
            if (_cursor.isNull(_cursorIndexOfFcMax)) {
              _tmpFcMax = null;
            } else {
              _tmpFcMax = _cursor.getLong(_cursorIndexOfFcMax);
            }
            final Long _tmpMinutosSueno;
            if (_cursor.isNull(_cursorIndexOfMinutosSueno)) {
              _tmpMinutosSueno = null;
            } else {
              _tmpMinutosSueno = _cursor.getLong(_cursorIndexOfMinutosSueno);
            }
            final Instant _tmpUltimaSincronizacion;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfUltimaSincronizacion)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfUltimaSincronizacion);
            }
            _tmpUltimaSincronizacion = __converters.fromTimestamp(_tmp);
            _item = new DailySummaryEntity(_tmpFecha,_tmpPasos,_tmpDistanciaMetros,_tmpCaloriasKcal,_tmpFcPromedio,_tmpFcMin,_tmpFcMax,_tmpMinutosSueno,_tmpUltimaSincronizacion);
            _result.add(_item);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
