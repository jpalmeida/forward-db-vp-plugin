package br.ufes.inf.nemo.ontoumltodb.transformation.obda;

import java.util.Date;

import br.ufes.inf.nemo.ontoumltodb.util.DbmsSupported;

public class GenerateObdaConnection {

	public static String getConnection(DbmsSupported dbms, String hostName, String databaseName, String userConnection,
			String password) {

		String stringConnection = "";
		Date today = new Date();

		String url, driver;
		switch (dbms) {
		case H2: {
			url = getH2Url();
			driver = getH2Drive();
			break;
		}
		case MYSQL: {
			url = getMysqlUrl();
			driver = getMysqlDrive();
			break;
		}
		case ORACLE: {
			url = getOracleUrl();
			driver = getOracleDrive();
			break;
		}
		case POSTGRE: {
			url = getPostgreUrl();
			driver = getPostgreDrive();
			break;
		}
		case SQLSERVER: {
			url = getSqlServerUrl();
			driver = getSqlServerDrive();
			break;
		}
		default:
			url = getGenericUrl();
			driver = getGenericDrive();
			break;
		}

		stringConnection += "#Ontouml2DB " + today.toString() + "\n";
		stringConnection += url + "//" + hostName + "/" + databaseName + "\n";
		stringConnection += driver + "\n";
		stringConnection += "jdbc.user=" + userConnection + "\n";
		stringConnection += "jdbc.name=ontouml2-db00-ufes-nemo-000000000003" + "\n";
		stringConnection += "jdbc.password=" + password + "\n";

		return stringConnection;
	}

	private static String getH2Url() {
		return "jdbc.url=jdbc:h2:tcp:";
	}

	private static String getH2Drive() {
		return "jdbc.driver=org.h2.Driver";
	}

	private static String getMysqlUrl() {
		return "jdbc.url=jdbc:mysql:tcp:";
	}

	private static String getMysqlDrive() {
		return "jdbc.driver=org.mysql.Driver";
	}

	private static String getOracleUrl() {
		return "jdbc.url=jdbc:oracle:tcp:";
	}

	private static String getOracleDrive() {
		return "jdbc.driver=org.oracle.Driver";
	}

	private static String getPostgreUrl() {
		return "jdbc.url=jdbc:postgre:tcp:";
	}

	private static String getPostgreDrive() {
		return "jdbc.driver=org.postgre.Driver";
	}

	private static String getSqlServerUrl() {
		return "jdbc.url=jdbc:sqlserver:tcp:";
	}

	private static String getSqlServerDrive() {
		return "jdbc.driver=org.sqlserver.Driver";
	}

	private static String getGenericUrl() {
		return "jdbc.url==[PUT_URL_HERE]";
	}

	private static String getGenericDrive() {
		return "jdbc.driver=[PUT_DRIVE_HERE]";
	}
}
