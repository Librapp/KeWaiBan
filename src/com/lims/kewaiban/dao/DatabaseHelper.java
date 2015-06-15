package com.lims.kewaiban.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lims.kewaiban.bean.Lesson;
import com.lims.kewaiban.bean.Parent;
import com.lims.kewaiban.bean.School;
import com.lims.kewaiban.bean.Shop;
import com.lims.kewaiban.bean.Student;
import com.lims.kewaiban.bean.Subject;
import com.lims.kewaiban.bean.Teacher;
import com.lims.kewaiban.bean.User;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	private static final String DB_NAME = "kwb.db";
	@SuppressWarnings("rawtypes")
	private final Map<String, Dao> daos = new HashMap<String, Dao>();

	private DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Lesson.class);
			TableUtils.createTable(connectionSource, Parent.class);
			TableUtils.createTable(connectionSource, School.class);
			TableUtils.createTable(connectionSource, Shop.class);
			TableUtils.createTable(connectionSource, Student.class);
			TableUtils.createTable(connectionSource, Teacher.class);
			TableUtils.createTable(connectionSource, Subject.class);
			TableUtils.createTable(connectionSource, User.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, Lesson.class, true);
			TableUtils.dropTable(connectionSource, Parent.class, true);
			TableUtils.dropTable(connectionSource, School.class, true);
			TableUtils.dropTable(connectionSource, Shop.class, true);
			TableUtils.dropTable(connectionSource, Student.class, true);
			TableUtils.dropTable(connectionSource, Teacher.class, true);
			TableUtils.dropTable(connectionSource, Subject.class, true);
			TableUtils.dropTable(connectionSource, User.class, true);
			onCreate(database, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static DatabaseHelper instance;

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static synchronized DatabaseHelper getHelper(Context context) {
		if (instance == null) {
			synchronized (DatabaseHelper.class) {
				if (instance == null)
					instance = new DatabaseHelper(context);
			}
		}
		return instance;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public synchronized Dao getDao(Class clazz) throws SQLException {
		Dao dao = null;
		String className = clazz.getSimpleName();

		if (daos.containsKey(className)) {
			dao = daos.get(className);
		}
		if (dao == null) {
			dao = super.getDao(clazz);
			daos.put(className, dao);
		}
		return dao;
	}

	@Override
	public void close() {
		super.close();
		for (String key : daos.keySet()) {
			@SuppressWarnings({ "rawtypes", "unused" })
			Dao dao = daos.get(key);
			dao = null;
		}
	}
}
