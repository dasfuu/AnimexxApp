package de.meisterfuu.animexx.api;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;

import de.meisterfuu.animexx.objects.ENSDraftObject;
import de.meisterfuu.animexx.objects.ENSNotifyObject;
import de.meisterfuu.animexx.objects.ENSObject;
import de.meisterfuu.animexx.objects.ENSQueueObject;
import de.meisterfuu.animexx.objects.XMPPMessageObject;
import de.meisterfuu.animexx.objects.XMPPRoosterObject;

public class DatabaseHelperTest extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "db.db";
	private static final int DATABASE_VERSION = 5126;

	private HashMap<Class, RuntimeExceptionDao> daos = new HashMap<Class,RuntimeExceptionDao>();
	private Classes[] objList = new Classes[]{
			new Classes(ENSObject.class, Long.class),
			new Classes(ENSDraftObject.class, Long.class),
			new Classes(ENSNotifyObject.class, Long.class),
			new Classes(ENSQueueObject.class, Long.class),
			new Classes(XMPPMessageObject.class, Long.class),
			new Classes(XMPPRoosterObject.class, String.class)
	};

	public DatabaseHelperTest(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void deleteDatabase(Context pContext) {
		try {
			for(Classes obj: objList){
				TableUtils.clearTable(getConnectionSource(), obj.type);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelperTest.class.getName(), "onCreate");

			for(Classes obj: objList){
				TableUtils.createTable(getConnectionSource(), obj.type);
			}
		} catch (SQLException e) {
			Log.e(DatabaseHelperTest.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		
		try {
			Log.i(DatabaseHelperTest.class.getName(), "onUpgrade");

			for(Classes obj: objList){
				TableUtils.dropTable(getConnectionSource(), obj.type, true);
			}

			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelperTest.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		} 
	}
	
	@Override
	public void close() {
		super.close();
		daos.clear();
		daos = null;
	}
	
	
	public RuntimeExceptionDao<ENSObject, Long> getENSDataDao() {
		if (daos.containsKey(ENSObject.class)) {
			daos.put( ENSObject.class, getRuntimeExceptionDao(ENSObject.class));
		}
		return daos.get(ENSObject.class);
	}

	public RuntimeExceptionDao<ENSDraftObject, Long> getENSDraftDataDao() {
		if (daos.containsKey(ENSObject.class)) {
			daos.put( ENSObject.class, getRuntimeExceptionDao(ENSDraftObject.class));
		}
		return daos.get(ENSObject.class);
	}
	public RuntimeExceptionDao<ENSQueueObject, Long> getENSQueueDataDao() {
		if (daos.containsKey(ENSObject.class)) {
			daos.put( ENSObject.class, getRuntimeExceptionDao(ENSQueueObject.class));
		}
		return daos.get(ENSObject.class);
	}
	public RuntimeExceptionDao<ENSNotifyObject, Long> getENSNotifyDataDao() {
		if (daos.containsKey(ENSObject.class)) {
			daos.put( ENSObject.class, getRuntimeExceptionDao(ENSNotifyObject.class));
		}
		return daos.get(ENSObject.class);
	}
	public RuntimeExceptionDao<XMPPMessageObject, Long> getXMPPMessageDataDao() {
		if (daos.containsKey(ENSObject.class)) {
			daos.put( ENSObject.class, getRuntimeExceptionDao(XMPPMessageObject.class));
		}
		return daos.get(ENSObject.class);
	}
	public RuntimeExceptionDao<XMPPRoosterObject, String> getXMPPRoosterDataDao() {
		if (daos.containsKey(ENSObject.class)) {
			daos.put( ENSObject.class, getRuntimeExceptionDao(XMPPRoosterObject.class));
		}
		return daos.get(ENSObject.class);
	}

	private class Classes {

		public Class type;
		public Class id;

		private Classes(final Class pId, final Class pType) {
			id = pId;
			type = pType;
		}
	}
}
