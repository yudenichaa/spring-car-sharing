import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    static String CREATE_TABLES_QUERY = "" +
            "CREATE TABLE IF NOT EXISTS COMPANY (\n" +
            "    ID INT PRIMARY KEY AUTO_INCREMENT,\n" +
            "    NAME VARCHAR UNIQUE NOT NULL\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS CAR (\n" +
            "   ID INT PRIMARY KEY AUTO_INCREMENT,\n" +
            "   NAME VARCHAR UNIQUE NOT NULL,\n" +
            "   RENTED BOOLEAN DEFAULT FALSE,\n" +
            "   COMPANY_ID INT NOT NULL,\n" +
            "   CONSTRAINT FK_COMPANY FOREIGN KEY (COMPANY_ID)\n" +
            "   REFERENCES COMPANY(ID)\n" +
            ");\n" +
            "CREATE TABLE IF NOT EXISTS CUSTOMER (\n" +
            "   ID INT PRIMARY KEY AUTO_INCREMENT,\n" +
            "   NAME VARCHAR UNIQUE NOT NULL,\n" +
            "   RENTED_CAR_ID INT,\n" +
            "   CONSTRAINT FK_CAR FOREIGN KEY (RENTED_CAR_ID)\n" +
            "   REFERENCES CAR(ID)\n" +
            ");\n" +
            "ALTER TABLE COMPANY\n" +
            "ALTER COLUMN ID RESTART WITH 1;\n" +
            "ALTER TABLE CAR\n" +
            "ALTER COLUMN ID RESTART WITH 1;\n" +
            "ALTER TABLE CUSTOMER\n" +
            "ALTER COLUMN ID RESTART WITH 1;\n";

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");

        String dbName = args.length > 0 ? args[1] : "carsharing";
        String dbPath = "jdbc:h2:./src/carsharing/db/" + dbName;
        Connection connection = DriverManager.getConnection(dbPath);
        connection.setAutoCommit(true);

        Statement statement = connection.createStatement();
        statement.executeUpdate(CREATE_TABLES_QUERY);

        MainMenu mainMenu = new MainMenu(connection);
        mainMenu.show();

        connection.close();
    }
}