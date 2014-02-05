package de.meisterfuu.animexx.data.ens;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import de.meisterfuu.animexx.objects.ENSObject;
import de.meisterfuu.animexx.objects.ENSDraftObject;

public class ENSDatabase extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "ens.db";
	private static final int DATABASE_VERSION = 2;

	private RuntimeExceptionDao<ENSObject, Long> ENSRuntimeDao = null;
	private RuntimeExceptionDao<ENSDraftObject, Long> ENSSendRuntimeDao = null;

	public ENSDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void deleteDatabase(Context pContext) {
		try {
			TableUtils.clearTable(getConnectionSource(), ENSObject.class);
			TableUtils.clearTable(getConnectionSource(), ENSDraftObject.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(ENSDatabase.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, ENSObject.class);
			TableUtils.createTable(connectionSource, ENSDraftObject.class);
		} catch (SQLException e) {
			Log.e(ENSDatabase.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		
		try {
			Log.i(ENSDatabase.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, ENSObject.class, true);
			TableUtils.dropTable(connectionSource, ENSDraftObject.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(ENSDatabase.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		} 
	}
	
	@Override
	public void close() {
		super.close();
		ENSRuntimeDao = null;
		ENSSendRuntimeDao = null;
	}
	
	
	public RuntimeExceptionDao<ENSObject, Long> getENSDataDao() {
		if (ENSRuntimeDao == null) {
			ENSRuntimeDao = getRuntimeExceptionDao(ENSObject.class);
		}
		return ENSRuntimeDao;
	}
	public RuntimeExceptionDao<ENSDraftObject, Long> getSendENSDataDao() {
		if (ENSSendRuntimeDao == null) {
			ENSSendRuntimeDao = getRuntimeExceptionDao(ENSDraftObject.class);
		}
		return ENSSendRuntimeDao;
	}


}
