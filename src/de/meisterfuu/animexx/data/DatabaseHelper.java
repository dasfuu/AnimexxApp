package de.meisterfuu.animexx.data;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import de.meisterfuu.animexx.objects.ENSDraftObject;
import de.meisterfuu.animexx.objects.ENSObject;
import de.meisterfuu.animexx.objects.ENSQueueObject;
import de.meisterfuu.animexx.objects.XMPPMessageObject;
import de.meisterfuu.animexx.objects.XMPPRoosterObject;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "db.db";
	private static final int DATABASE_VERSION = 5123;

	private RuntimeExceptionDao<ENSObject, Long> ENSRuntimeDao = null;
	private RuntimeExceptionDao<ENSDraftObject, Long> ENSSendRuntimeDao = null;
	private RuntimeExceptionDao<ENSQueueObject, Long> ENSQueueRuntimeDao = null;
	private RuntimeExceptionDao<XMPPMessageObject, Long> XMPPHistoryRuntimeDao = null;
	private RuntimeExceptionDao<XMPPRoosterObject, String> XMPPRoosterRuntimeDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void deleteDatabase(Context pContext) {
		try {
			TableUtils.clearTable(getConnectionSource(), ENSObject.class);
			TableUtils.clearTable(getConnectionSource(), ENSDraftObject.class);
			TableUtils.clearTable(getConnectionSource(), ENSQueueObject.class);
			TableUtils.clearTable(getConnectionSource(), XMPPMessageObject.class);
			TableUtils.clearTable(getConnectionSource(), XMPPRoosterObject.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, ENSObject.class);
			TableUtils.createTable(connectionSource, ENSDraftObject.class);
			TableUtils.createTable(connectionSource, ENSQueueObject.class);
			TableUtils.createTable(connectionSource, XMPPMessageObject.class);
			TableUtils.createTable(connectionSource, XMPPRoosterObject.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, ENSObject.class, true);
			TableUtils.dropTable(connectionSource, ENSDraftObject.class, true);
			TableUtils.dropTable(connectionSource, ENSQueueObject.class, true);
			TableUtils.dropTable(connectionSource, XMPPMessageObject.class, true);
			TableUtils.dropTable(connectionSource, XMPPRoosterObject.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		} 
	}
	
	@Override
	public void close() {
		super.close();
		ENSRuntimeDao = null;
		ENSSendRuntimeDao = null;
		ENSQueueRuntimeDao = null;
		XMPPHistoryRuntimeDao = null;
		XMPPRoosterRuntimeDao = null;
	}
	
	
	public RuntimeExceptionDao<ENSObject, Long> getENSDataDao() {
		if (ENSRuntimeDao == null) {
			ENSRuntimeDao = getRuntimeExceptionDao(ENSObject.class);
		}
		return ENSRuntimeDao;
	}
	public RuntimeExceptionDao<ENSDraftObject, Long> getENSDraftDataDao() {
		if (ENSSendRuntimeDao == null) {
			ENSSendRuntimeDao = getRuntimeExceptionDao(ENSDraftObject.class);
		}
		return ENSSendRuntimeDao;
	}
	public RuntimeExceptionDao<ENSQueueObject, Long> getENSQueueDataDao() {
		if (ENSQueueRuntimeDao == null) {
			ENSQueueRuntimeDao = getRuntimeExceptionDao(ENSQueueObject.class);
		}
		return ENSQueueRuntimeDao;
	}
	public RuntimeExceptionDao<XMPPMessageObject, Long> getXMPPMessageDataDao() {
		if (XMPPHistoryRuntimeDao == null) {
			XMPPHistoryRuntimeDao = getRuntimeExceptionDao(XMPPMessageObject.class);
		}
		return XMPPHistoryRuntimeDao;
	}
	public RuntimeExceptionDao<XMPPRoosterObject, String> getXMPPRoosterDataDao() {
		if (XMPPRoosterRuntimeDao == null) {
			XMPPRoosterRuntimeDao = getRuntimeExceptionDao(XMPPRoosterObject.class);
		}
		return XMPPRoosterRuntimeDao;
	}

}
