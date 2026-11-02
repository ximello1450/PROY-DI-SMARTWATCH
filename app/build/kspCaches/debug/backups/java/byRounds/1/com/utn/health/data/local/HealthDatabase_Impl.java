package com.utn.health.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.utn.health.data.local.dao.DailySummaryDao;
import com.utn.health.data.local.dao.DailySummaryDao_Impl;
import com.utn.health.data.local.dao.StreakDao;
import com.utn.health.data.local.dao.StreakDao_Impl;
import com.utn.health.data.local.dao.SyncStatusDao;
import com.utn.health.data.local.dao.SyncStatusDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HealthDatabase_Impl extends HealthDatabase {
  private volatile DailySummaryDao _dailySummaryDao;

  private volatile StreakDao _streakDao;

  private volatile SyncStatusDao _syncStatusDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `resumen_diario` (`fecha` TEXT NOT NULL, `pasos` INTEGER, `distanciaMetros` REAL, `caloriasKcal` REAL, `fcPromedio` INTEGER, `fcMin` INTEGER, `fcMax` INTEGER, `minutosSueno` INTEGER, `ultimaSincronizacion` INTEGER, PRIMARY KEY(`fecha`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `racha_distancia` (`id` INTEGER NOT NULL, `rachaActual` INTEGER NOT NULL, `mejorRacha` INTEGER NOT NULL, `metaKm` REAL NOT NULL, `congeladoresDisponibles` INTEGER NOT NULL, `estaCongeladaHoy` INTEGER NOT NULL, `rachaRecuperable` INTEGER NOT NULL, `conteoRecuperable` INTEGER NOT NULL, `ultimaFechaCompletada` TEXT, `fechasCompletadasCsv` TEXT NOT NULL, `pesoKg` REAL, `estaturaCm` REAL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `estado_sincronizacion` (`id` INTEGER NOT NULL, `permisosConcedidos` INTEGER NOT NULL, `healthConnectDisponible` INTEGER NOT NULL, `ultimaSincronizacion` INTEGER, `fuentesDetectadas` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '9108ff6ec1e2807c457c9eb44b6296bb')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `resumen_diario`");
        db.execSQL("DROP TABLE IF EXISTS `racha_distancia`");
        db.execSQL("DROP TABLE IF EXISTS `estado_sincronizacion`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsResumenDiario = new HashMap<String, TableInfo.Column>(9);
        _columnsResumenDiario.put("fecha", new TableInfo.Column("fecha", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResumenDiario.put("pasos", new TableInfo.Column("pasos", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResumenDiario.put("distanciaMetros", new TableInfo.Column("distanciaMetros", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResumenDiario.put("caloriasKcal", new TableInfo.Column("caloriasKcal", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResumenDiario.put("fcPromedio", new TableInfo.Column("fcPromedio", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResumenDiario.put("fcMin", new TableInfo.Column("fcMin", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResumenDiario.put("fcMax", new TableInfo.Column("fcMax", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResumenDiario.put("minutosSueno", new TableInfo.Column("minutosSueno", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsResumenDiario.put("ultimaSincronizacion", new TableInfo.Column("ultimaSincronizacion", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysResumenDiario = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesResumenDiario = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoResumenDiario = new TableInfo("resumen_diario", _columnsResumenDiario, _foreignKeysResumenDiario, _indicesResumenDiario);
        final TableInfo _existingResumenDiario = TableInfo.read(db, "resumen_diario");
        if (!_infoResumenDiario.equals(_existingResumenDiario)) {
          return new RoomOpenHelper.ValidationResult(false, "resumen_diario(com.utn.health.data.local.entity.DailySummaryEntity).\n"
                  + " Expected:\n" + _infoResumenDiario + "\n"
                  + " Found:\n" + _existingResumenDiario);
        }
        final HashMap<String, TableInfo.Column> _columnsRachaDistancia = new HashMap<String, TableInfo.Column>(12);
        _columnsRachaDistancia.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRachaDistancia.put("rachaActual", new TableInfo.Column("rachaActual", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRachaDistancia.put("mejorRacha", new TableInfo.Column("mejorRacha", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRachaDistancia.put("metaKm", new TableInfo.Column("metaKm", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRachaDistancia.put("congeladoresDisponibles", new TableInfo.Column("congeladoresDisponibles", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRachaDistancia.put("estaCongeladaHoy", new TableInfo.Column("estaCongeladaHoy", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRachaDistancia.put("rachaRecuperable", new TableInfo.Column("rachaRecuperable", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRachaDistancia.put("conteoRecuperable", new TableInfo.Column("conteoRecuperable", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRachaDistancia.put("ultimaFechaCompletada", new TableInfo.Column("ultimaFechaCompletada", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRachaDistancia.put("fechasCompletadasCsv", new TableInfo.Column("fechasCompletadasCsv", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRachaDistancia.put("pesoKg", new TableInfo.Column("pesoKg", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRachaDistancia.put("estaturaCm", new TableInfo.Column("estaturaCm", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRachaDistancia = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRachaDistancia = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRachaDistancia = new TableInfo("racha_distancia", _columnsRachaDistancia, _foreignKeysRachaDistancia, _indicesRachaDistancia);
        final TableInfo _existingRachaDistancia = TableInfo.read(db, "racha_distancia");
        if (!_infoRachaDistancia.equals(_existingRachaDistancia)) {
          return new RoomOpenHelper.ValidationResult(false, "racha_distancia(com.utn.health.data.local.entity.StreakEntity).\n"
                  + " Expected:\n" + _infoRachaDistancia + "\n"
                  + " Found:\n" + _existingRachaDistancia);
        }
        final HashMap<String, TableInfo.Column> _columnsEstadoSincronizacion = new HashMap<String, TableInfo.Column>(5);
        _columnsEstadoSincronizacion.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstadoSincronizacion.put("permisosConcedidos", new TableInfo.Column("permisosConcedidos", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstadoSincronizacion.put("healthConnectDisponible", new TableInfo.Column("healthConnectDisponible", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstadoSincronizacion.put("ultimaSincronizacion", new TableInfo.Column("ultimaSincronizacion", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEstadoSincronizacion.put("fuentesDetectadas", new TableInfo.Column("fuentesDetectadas", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEstadoSincronizacion = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEstadoSincronizacion = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEstadoSincronizacion = new TableInfo("estado_sincronizacion", _columnsEstadoSincronizacion, _foreignKeysEstadoSincronizacion, _indicesEstadoSincronizacion);
        final TableInfo _existingEstadoSincronizacion = TableInfo.read(db, "estado_sincronizacion");
        if (!_infoEstadoSincronizacion.equals(_existingEstadoSincronizacion)) {
          return new RoomOpenHelper.ValidationResult(false, "estado_sincronizacion(com.utn.health.data.local.entity.SyncStatusEntity).\n"
                  + " Expected:\n" + _infoEstadoSincronizacion + "\n"
                  + " Found:\n" + _existingEstadoSincronizacion);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "9108ff6ec1e2807c457c9eb44b6296bb", "e26ac415168d71ec367cadfb6249c1f6");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "resumen_diario","racha_distancia","estado_sincronizacion");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `resumen_diario`");
      _db.execSQL("DELETE FROM `racha_distancia`");
      _db.execSQL("DELETE FROM `estado_sincronizacion`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DailySummaryDao.class, DailySummaryDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StreakDao.class, StreakDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SyncStatusDao.class, SyncStatusDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public DailySummaryDao dailySummaryDao() {
    if (_dailySummaryDao != null) {
      return _dailySummaryDao;
    } else {
      synchronized(this) {
        if(_dailySummaryDao == null) {
          _dailySummaryDao = new DailySummaryDao_Impl(this);
        }
        return _dailySummaryDao;
      }
    }
  }

  @Override
  public StreakDao streakDao() {
    if (_streakDao != null) {
      return _streakDao;
    } else {
      synchronized(this) {
        if(_streakDao == null) {
          _streakDao = new StreakDao_Impl(this);
        }
        return _streakDao;
      }
    }
  }

  @Override
  public SyncStatusDao syncStatusDao() {
    if (_syncStatusDao != null) {
      return _syncStatusDao;
    } else {
      synchronized(this) {
        if(_syncStatusDao == null) {
          _syncStatusDao = new SyncStatusDao_Impl(this);
        }
        return _syncStatusDao;
      }
    }
  }
}
