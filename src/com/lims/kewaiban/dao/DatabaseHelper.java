package com.lims.kewaiban.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

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
	// 用户数据库文件的版本
	private static final int DB_VERSION = 1;
	// 数据库文件目标存放路径为系统默认位置，cn.arthur.examples 是你的包名
	private final String DB_PATH = "/com.droid/databases/";
	/*
	 * //如果你想把数据库文件存放在SD卡的话 private static String DB_PATH =
	 * android.os.Environment.getExternalStorageDirectory().getAbsolutePath() +
	 * "/arthurcn/drivertest/packfiles/";
	 */
	private static String ASSETS_NAME = "meituan_cities.db";
	/**
	 * 如果数据库文件较大，使用FileSplit分割为小于1M的小文件 此例中分割为 hello.db.101 hello.db.102
	 * hello.db.103
	 */
	// 第一个文件名后缀
	private static final int ASSETS_SUFFIX_BEGIN = 101;
	// 最后一个文件名后缀
	private static final int ASSETS_SUFFIX_END = 103;
	private final Context myContext;
	@SuppressWarnings("rawtypes")
	private final Map<String, Dao> daos = new HashMap<String, Dao>();

	private DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		myContext = context;
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

	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			// 数据库已存在，do nothing.
		} else {
			// 创建数据库
			try {
				File dir = new File(DB_PATH);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File dbf = new File(DB_PATH + DB_NAME);
				if (dbf.exists()) {
					dbf.delete();
				}
				SQLiteDatabase.openOrCreateDatabase(dbf, null);
				// 复制asseets中的db文件到DB_PATH下
				copyDataBase();
			} catch (IOException e) {
				throw new Error("数据库创建失败");
			}
		}
	}

	// 检查数据库是否有效
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		String myPath = DB_PATH + DB_NAME;
		try {
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {
		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(ASSETS_NAME);
		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	// 复制assets下的大数据库文件时用这个
	private void copyBigDataBase() throws IOException {
		InputStream myInput;
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		for (int i = ASSETS_SUFFIX_BEGIN; i < ASSETS_SUFFIX_END + 1; i++) {
			myInput = myContext.getAssets().open(ASSETS_NAME + "." + i);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				myOutput.write(buffer, 0, length);
			}
			myOutput.flush();
			myInput.close();
		}
		myOutput.close();
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
