package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Databasebackup;

public class DatabasebackupDao {
	public List<Databasebackup> getAll(DatabaseDao databaseDao) throws SQLException {
		List<Databasebackup> databasebackups = new ArrayList<Databasebackup>();

		databaseDao.query("select * from databasebackup");
		while (databaseDao.next()) {
			Databasebackup databasebackup = new Databasebackup();
			databasebackup.setDatabasebackupId(databaseDao.getInt("databasebackupId"));
			databasebackup.setName(databaseDao.getString("name"));
			databasebackup.setTime(databaseDao.getTimestamp("time"));
			databasebackup.setDirectory(databaseDao.getString("directory"));
			databasebackups.add(databasebackup);
		}

		return databasebackups;
	}

	public Integer add(Databasebackup databasebackup, DatabaseDao databaseDao) throws SQLException {
		databasebackup.setDirectory(databasebackup.getDirectory().replace("\\", "\\\\"));

		String sql = "insert into databasebackup(name,directory) values('" + databasebackup.getName() + "','"
				+ databasebackup.getDirectory() + "');";
		return databaseDao.update(sql);
	}

	public Databasebackup getById(DatabaseDao databaseDao, Integer databasebackupId) throws SQLException {
		Databasebackup databasebackup = new Databasebackup();

		databaseDao.getById("databasebackup", databasebackupId);
		while (databaseDao.next()) {
			databasebackup.setDatabasebackupId(databaseDao.getInt("databasebackupId"));
			databasebackup.setName(databaseDao.getString("name"));
			databasebackup.setTime(databaseDao.getTimestamp("time"));
			databasebackup.setDirectory(databaseDao.getString("directory"));
		}

		return databasebackup;
	}
}
