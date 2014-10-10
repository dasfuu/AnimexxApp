
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

import de.meisterfuu.animexx.objects.ens.ENSDraftObject;
import de.meisterfuu.animexx.objects.ens.ENSNotifyObject;
import de.meisterfuu.animexx.objects.ens.ENSObject;
import de.meisterfuu.animexx.objects.ens.ENSQueueObject;
import de.meisterfuu.animexx.objects.xmpp.XMPPMessageObject;
import de.meisterfuu.animexx.objects.xmpp.XMPPRoosterObject;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "db.db";
	private static final int DATABASE_VERSION = 5127;

	private HashMap<Class, RuntimeExceptionDao> daos = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		daos = new HashMap<Class,RuntimeExceptionDao>();
	}

	public void deleteDatabase(Context pContext) {
		try {
			for(DatabaseTables obj: DatabaseTables.values()){
				TableUtils.clearTable(getConnectionSource(), obj.getClasses().type);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");

			for(DatabaseTables obj: DatabaseTables.values()){
				TableUtils.createTable(getConnectionSource(), obj.getClasses().type);
			}
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {

		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");

			for(DatabaseTables obj: DatabaseTables.values()){
				TableUtils.dropTable(getConnectionSource(), obj.getClasses().type, true);
			}
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
		daos.clear();
		daos = null;
	}


	public RuntimeExceptionDao getDao(DatabaseTables table) {
		if (!daos.containsKey(table.getClasses().type)) {
			daos.put( table.getClasses().type, getRuntimeExceptionDao(table.getClasses().type));
		}
		return daos.get(table.getClasses().type);
	}

	public RuntimeExceptionDao<ENSObject, Long> getENSDataDao() {
		return getDao(DatabaseTables.ENSObject);
	}
	public RuntimeExceptionDao<ENSDraftObject, Long> getENSDraftDataDao() {
		return getDao(DatabaseTables.ENSDraftObject);
	}
	public RuntimeExceptionDao<ENSQueueObject, Long> getENSQueueDataDao() {
		return getDao(DatabaseTables.ENSQueueObject);
	}
	public RuntimeExceptionDao<ENSNotifyObject, Long> getENSNotifyDataDao() {
		return getDao(DatabaseTables.ENSNotifyObject);
	}
	public RuntimeExceptionDao<XMPPMessageObject, Long> getXMPPMessageDataDao() {
		return getDao(DatabaseTables.XMPPMessageObject);
	}
	public RuntimeExceptionDao<XMPPRoosterObject, String> getXMPPRoosterDataDao() {
		return getDao(DatabaseTables.XMPPRoosterObject);
	}

	public enum DatabaseTables {

		ENSObject (new DatabaseHelper.Classes(ENSObject.class, Long.class)),
		ENSDraftObject (new DatabaseHelper.Classes(ENSDraftObject.class, Long.class)),
		ENSNotifyObject (new DatabaseHelper.Classes(ENSNotifyObject.class, Long.class)),
		ENSQueueObject (new DatabaseHelper.Classes(ENSQueueObject.class, Long.class)),
		XMPPMessageObject (new DatabaseHelper.Classes(XMPPMessageObject.class, Long.class)),
		XMPPRoosterObject (new DatabaseHelper.Classes(XMPPRoosterObject.class, String.class));

		private DatabaseHelper.Classes cl;

		private DatabaseTables(DatabaseHelper.Classes classes){
			this.cl = classes;
		}

		public DatabaseHelper.Classes getClasses(){
			return cl;
		}
	}


	public static class Classes {

		public Class type;
		public Class id;

		public Classes(final Class pType, final Class pId) {
			id = pId;
			type = pType;
		}
	}
}
