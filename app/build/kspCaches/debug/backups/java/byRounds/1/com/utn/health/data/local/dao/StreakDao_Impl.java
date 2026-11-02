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
import com.utn.health.data.local.entity.StreakEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StreakDao_Impl implements StreakDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StreakEntity> __insertionAdapterOfStreakEntity;

  public StreakDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStreakEntity = new EntityInsertionAdapter<StreakEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `racha_distancia` (`id`,`rachaActual`,`mejorRacha`,`metaKm`,`congeladoresDisponibles`,`estaCongeladaHoy`,`rachaRecuperable`,`conteoRecuperable`,`ultimaFechaCompletada`,`fechasCompletadasCsv`,`pesoKg`,`estaturaCm`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StreakEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getRachaActual());
        statement.bindLong(3, entity.getMejorRacha());
        statement.bindDouble(4, entity.getMetaKm());
        statement.bindLong(5, entity.getCongeladoresDisponibles());
        final int _tmp = entity.getEstaCongeladaHoy() ? 1 : 0;
        statement.bindLong(6, _tmp);
        final int _tmp_1 = entity.getRachaRecuperable() ? 1 : 0;
        statement.bindLong(7, _tmp_1);
        statement.bindLong(8, entity.getConteoRecuperable());
        if (entity.getUltimaFechaCompletada() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getUltimaFechaCompletada());
        }
        statement.bindString(10, entity.getFechasCompletadasCsv());
        if (entity.getPesoKg() == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.getPesoKg());
        }
        if (entity.getEstaturaCm() == null) {
          statement.bindNull(12);
        } else {
          statement.bindDouble(12, entity.getEstaturaCm());
        }
      }
    };
  }

  @Override
  public Object insertOrUpdate(final StreakEntity streak,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStreakEntity.insert(streak);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<StreakEntity> getStreak() {
    final String _sql = "SELECT * FROM racha_distancia WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"racha_distancia"}, new Callable<StreakEntity>() {
      @Override
      @Nullable
      public StreakEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRachaActual = CursorUtil.getColumnIndexOrThrow(_cursor, "rachaActual");
          final int _cursorIndexOfMejorRacha = CursorUtil.getColumnIndexOrThrow(_cursor, "mejorRacha");
          final int _cursorIndexOfMetaKm = CursorUtil.getColumnIndexOrThrow(_cursor, "metaKm");
          final int _cursorIndexOfCongeladoresDisponibles = CursorUtil.getColumnIndexOrThrow(_cursor, "congeladoresDisponibles");
          final int _cursorIndexOfEstaCongeladaHoy = CursorUtil.getColumnIndexOrThrow(_cursor, "estaCongeladaHoy");
          final int _cursorIndexOfRachaRecuperable = CursorUtil.getColumnIndexOrThrow(_cursor, "rachaRecuperable");
          final int _cursorIndexOfConteoRecuperable = CursorUtil.getColumnIndexOrThrow(_cursor, "conteoRecuperable");
          final int _cursorIndexOfUltimaFechaCompletada = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaFechaCompletada");
          final int _cursorIndexOfFechasCompletadasCsv = CursorUtil.getColumnIndexOrThrow(_cursor, "fechasCompletadasCsv");
          final int _cursorIndexOfPesoKg = CursorUtil.getColumnIndexOrThrow(_cursor, "pesoKg");
          final int _cursorIndexOfEstaturaCm = CursorUtil.getColumnIndexOrThrow(_cursor, "estaturaCm");
          final StreakEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpRachaActual;
            _tmpRachaActual = _cursor.getInt(_cursorIndexOfRachaActual);
            final int _tmpMejorRacha;
            _tmpMejorRacha = _cursor.getInt(_cursorIndexOfMejorRacha);
            final double _tmpMetaKm;
            _tmpMetaKm = _cursor.getDouble(_cursorIndexOfMetaKm);
            final int _tmpCongeladoresDisponibles;
            _tmpCongeladoresDisponibles = _cursor.getInt(_cursorIndexOfCongeladoresDisponibles);
            final boolean _tmpEstaCongeladaHoy;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEstaCongeladaHoy);
            _tmpEstaCongeladaHoy = _tmp != 0;
            final boolean _tmpRachaRecuperable;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfRachaRecuperable);
            _tmpRachaRecuperable = _tmp_1 != 0;
            final int _tmpConteoRecuperable;
            _tmpConteoRecuperable = _cursor.getInt(_cursorIndexOfConteoRecuperable);
            final String _tmpUltimaFechaCompletada;
            if (_cursor.isNull(_cursorIndexOfUltimaFechaCompletada)) {
              _tmpUltimaFechaCompletada = null;
            } else {
              _tmpUltimaFechaCompletada = _cursor.getString(_cursorIndexOfUltimaFechaCompletada);
            }
            final String _tmpFechasCompletadasCsv;
            _tmpFechasCompletadasCsv = _cursor.getString(_cursorIndexOfFechasCompletadasCsv);
            final Double _tmpPesoKg;
            if (_cursor.isNull(_cursorIndexOfPesoKg)) {
              _tmpPesoKg = null;
            } else {
              _tmpPesoKg = _cursor.getDouble(_cursorIndexOfPesoKg);
            }
            final Double _tmpEstaturaCm;
            if (_cursor.isNull(_cursorIndexOfEstaturaCm)) {
              _tmpEstaturaCm = null;
            } else {
              _tmpEstaturaCm = _cursor.getDouble(_cursorIndexOfEstaturaCm);
            }
            _result = new StreakEntity(_tmpId,_tmpRachaActual,_tmpMejorRacha,_tmpMetaKm,_tmpCongeladoresDisponibles,_tmpEstaCongeladaHoy,_tmpRachaRecuperable,_tmpConteoRecuperable,_tmpUltimaFechaCompletada,_tmpFechasCompletadasCsv,_tmpPesoKg,_tmpEstaturaCm);
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
  public Object getStreakSync(final Continuation<? super StreakEntity> $completion) {
    final String _sql = "SELECT * FROM racha_distancia WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<StreakEntity>() {
      @Override
      @Nullable
      public StreakEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfRachaActual = CursorUtil.getColumnIndexOrThrow(_cursor, "rachaActual");
          final int _cursorIndexOfMejorRacha = CursorUtil.getColumnIndexOrThrow(_cursor, "mejorRacha");
          final int _cursorIndexOfMetaKm = CursorUtil.getColumnIndexOrThrow(_cursor, "metaKm");
          final int _cursorIndexOfCongeladoresDisponibles = CursorUtil.getColumnIndexOrThrow(_cursor, "congeladoresDisponibles");
          final int _cursorIndexOfEstaCongeladaHoy = CursorUtil.getColumnIndexOrThrow(_cursor, "estaCongeladaHoy");
          final int _cursorIndexOfRachaRecuperable = CursorUtil.getColumnIndexOrThrow(_cursor, "rachaRecuperable");
          final int _cursorIndexOfConteoRecuperable = CursorUtil.getColumnIndexOrThrow(_cursor, "conteoRecuperable");
          final int _cursorIndexOfUltimaFechaCompletada = CursorUtil.getColumnIndexOrThrow(_cursor, "ultimaFechaCompletada");
          final int _cursorIndexOfFechasCompletadasCsv = CursorUtil.getColumnIndexOrThrow(_cursor, "fechasCompletadasCsv");
          final int _cursorIndexOfPesoKg = CursorUtil.getColumnIndexOrThrow(_cursor, "pesoKg");
          final int _cursorIndexOfEstaturaCm = CursorUtil.getColumnIndexOrThrow(_cursor, "estaturaCm");
          final StreakEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpRachaActual;
            _tmpRachaActual = _cursor.getInt(_cursorIndexOfRachaActual);
            final int _tmpMejorRacha;
            _tmpMejorRacha = _cursor.getInt(_cursorIndexOfMejorRacha);
            final double _tmpMetaKm;
            _tmpMetaKm = _cursor.getDouble(_cursorIndexOfMetaKm);
            final int _tmpCongeladoresDisponibles;
            _tmpCongeladoresDisponibles = _cursor.getInt(_cursorIndexOfCongeladoresDisponibles);
            final boolean _tmpEstaCongeladaHoy;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEstaCongeladaHoy);
            _tmpEstaCongeladaHoy = _tmp != 0;
            final boolean _tmpRachaRecuperable;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfRachaRecuperable);
            _tmpRachaRecuperable = _tmp_1 != 0;
            final int _tmpConteoRecuperable;
            _tmpConteoRecuperable = _cursor.getInt(_cursorIndexOfConteoRecuperable);
            final String _tmpUltimaFechaCompletada;
            if (_cursor.isNull(_cursorIndexOfUltimaFechaCompletada)) {
              _tmpUltimaFechaCompletada = null;
            } else {
              _tmpUltimaFechaCompletada = _cursor.getString(_cursorIndexOfUltimaFechaCompletada);
            }
            final String _tmpFechasCompletadasCsv;
            _tmpFechasCompletadasCsv = _cursor.getString(_cursorIndexOfFechasCompletadasCsv);
            final Double _tmpPesoKg;
            if (_cursor.isNull(_cursorIndexOfPesoKg)) {
              _tmpPesoKg = null;
            } else {
              _tmpPesoKg = _cursor.getDouble(_cursorIndexOfPesoKg);
            }
            final Double _tmpEstaturaCm;
            if (_cursor.isNull(_cursorIndexOfEstaturaCm)) {
              _tmpEstaturaCm = null;
            } else {
              _tmpEstaturaCm = _cursor.getDouble(_cursorIndexOfEstaturaCm);
            }
            _result = new StreakEntity(_tmpId,_tmpRachaActual,_tmpMejorRacha,_tmpMetaKm,_tmpCongeladoresDisponibles,_tmpEstaCongeladaHoy,_tmpRachaRecuperable,_tmpConteoRecuperable,_tmpUltimaFechaCompletada,_tmpFechasCompletadasCsv,_tmpPesoKg,_tmpEstaturaCm);
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
