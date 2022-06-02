import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDao {
    static String SELECT_CARS_QUERY = "" +
            "SELECT ID, NAME, RENTED\n" +
            "FROM CAR\n" +
            "WHERE COMPANY_ID = (?);";

    static String CREATE_CAR_QUERY = "" +
            "INSERT INTO CAR (NAME, COMPANY_ID)\n" +
            "VALUES (?, ?);";

    private final Connection connection;

    public CarDao(Connection connection) {
        this.connection = connection;
    }

    public List<Car> getCars(int companyId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(SELECT_CARS_QUERY);
        statement.setInt(1, companyId);
        ResultSet carsResultSet = statement.executeQuery();
        List<Car> cars = new ArrayList<>();

        while (carsResultSet.next()) {
            Integer id = carsResultSet.getInt("ID");
            String name = carsResultSet.getString("NAME");
            boolean rented = carsResultSet.getBoolean("RENTED");
            Car car = new Car(id, name, rented);
            cars.add(car);
        }

        return cars;
    }

    public void createCar(String carName, int companyId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_CAR_QUERY);
        statement.setString(1, carName);
        statement.setInt(2, companyId);
        statement.executeUpdate();
    }
}
