package db.mysql;

public class MySQLDBUtil {
	private static final String HOSTNAME = "localhost";
	//private static final String PORT_NUM = "8888"; 
	private static final String PORT_NUM = "8889"; 
	//project name update
	public static final String DB_NAME = "myproject";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";
	public static final String URL = "jdbc:mysql://"
			+ HOSTNAME + ":" + PORT_NUM + "/" + DB_NAME
			+ "?user=" + USERNAME + "&password=" + PASSWORD
			+ "&autoReconnect=true&serverTimezone=UTC";
}
