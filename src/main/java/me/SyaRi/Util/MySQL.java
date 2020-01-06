package me.SyaRi.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {

	private Connection connection;
	private static Statement statement;
	final private String host = "localhost";
	final private static String database = "LunaRPG";
	final private String username = "LunaRPG";
	final private String password = "Cv_bn&5";
	final private int port = 3306;

	public void setup() {
		try {
			synchronized (this) {
				if(connection != null && connection.isClosed()) {
					return;
				}
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + getDatabase(),
																							username, password);
				statement = connection.createStatement();
				getStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " + getDatabase() + ".Town " +
										"(Name VARCHAR(255), World VARCHAR(255), X FLOAT(10, 1), Y FLOAT(10, 1), Z FLOAT(10, 1));");
				getStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " + getDatabase() + ".Click " +
										"(FromWorld VARCHAR(255), FromX FLOAT(10, 1), FromY FLOAT(10, 1), FromZ FLOAT(10, 1), " +
										"ToWorld VARCHAR(255), ToX FLOAT(10, 1), ToY FLOAT(10, 1), ToZ FLOAT(10, 1));");
				getStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " + getDatabase() + ".Item " +
										"(Type VARCHAR(255), Name VARCHAR(255), ID VARCHAR(255), Sell SMALLINT UNSIGNED);");
				getStatement().executeUpdate("CREATE TABLE IF NOT EXISTS " + getDatabase() + ".Fish " +
						"(Biome VARCHAR(255), Value SMALLINT UNSIGNED, Item VARCHAR(255));");
			}
		} catch(SQLException | ClassNotFoundException e){
			e.printStackTrace();
		}
	}

	public static Statement getStatement() {
		return statement;
	}

	public static String getDatabase() {
		return database;
	}
}