import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDao {

    static String SELECT_CUSTOMERS_QUERY = "" +
            "SELECT ID, NAME, RENTED_CAR_ID\n" +
            "FROM CUSTOMER;";
    static String CREATE_CUSTOMER_QUERY = "" +
            "INSERT INTO CUSTOMER (NAME)\n" +
            "VALUES (?);";

    static String RENT_CAR_QUERY = "" +
            "UPDATE CUSTOMER\n" +
            "SET RENTED_CAR_ID = ?\n" +
            "WHERE ID = ?;";

    static String SET_CAR_RENTED_QUERY = "" +
            "UPDATE CAR\n" +
            "SET RENTED = TRUE\n" +
            "WHERE ID = ?;";

    static String RETURN_CAR_QUERY = "" +
            "UPDATE CUSTOMER\n" +
            "SET RENTED_CAR_ID = NULL\n" +
            "WHERE ID = ?;";

    static String SET_CAR_NOT_RENTED_QUERY = "" +
            "UPDATE CAR\n" +
            "SET RENTED = FALSE\n" +
            "WHERE ID = ?;";

    static String GET_CUSTOMER_QUERY = "" +
            "SELECT RENTED_CAR_ID\n" +
            "FROM CUSTOMER\n" +
            "WHERE ID = ?;";

    static String GET_CUSTOMER_CAR_QUERY = "" +
            "SELECT NAME, COMPANY_ID\n" +
            "FROM CAR\n" +
            "WHERE ID = ?;";

    static String GET_CUSTOMER_CAR_COMPANY_QUERY = "" +
            "SELECT NAME\n" +
            "FROM COMPANY\n" +
            "WHERE ID = ?;";

    private final Connection connection;

    public CustomerDao(Connection connection) {
        this.connection = connection;
    }

    public List<Customer> getCustomers() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet customersResultSet = statement.executeQuery(SELECT_CUSTOMERS_QUERY);
        List<Customer> customers = new ArrayList<>();

        while (customersResultSet.next()) {
            Integer id = customersResultSet.getInt("ID");
            String name = customersResultSet.getString("NAME");
            Integer rentedCarId = customersResultSet.getInt("RENTED_CAR_ID");
            Customer customer = new Customer(id, name, rentedCarId);
            customers.add(customer);
        }

        return customers;
    }

    public void createCustomer(String customerName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_CUSTOMER_QUERY);
        statement.setString(1, customerName);
        statement.executeUpdate();
    }


    public boolean isCarRented(int customerId) throws SQLException {
        return getRentedCarId(customerId).isPresent();
    }

    public void rentCar(int customerId, int carId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(RENT_CAR_QUERY);
        statement.setInt(1, carId);
        statement.setInt(2, customerId);
        statement.executeUpdate();
        statement = connection.prepareStatement(SET_CAR_RENTED_QUERY);
        statement.setInt(1, carId);
        statement.executeUpdate();
    }

    public boolean returnCar(int customerId) throws SQLException {
        Optional<Integer> rentedCarId = getRentedCarId(customerId);

        if (rentedCarId.isEmpty()) {
            return false;
        }

        PreparedStatement statement = connection.prepareStatement(RETURN_CAR_QUERY);
        statement.setInt(1, rentedCarId.get());
        statement.executeUpdate();
        statement = connection.prepareStatement(SET_CAR_NOT_RENTED_QUERY);
        statement.setInt(1, rentedCarId.get());
        statement.executeUpdate();
        return true;
    }

    public Optional<RentedCarDto> getRentedCarInfo(int customerId) throws SQLException {
        Optional<Integer> rentedCarId = getRentedCarId(customerId);

        if (rentedCarId.isEmpty()) {
            return Optional.empty();
        }

        PreparedStatement statement = connection.prepareStatement(GET_CUSTOMER_CAR_QUERY);
        statement.setInt(1, rentedCarId.get());
        ResultSet carResultSet = statement.executeQuery();
        carResultSet.next();
        String carName = carResultSet.getString("NAME");
        int companyId = carResultSet.getInt("COMPANY_ID");
        statement = connection.prepareStatement(GET_CUSTOMER_CAR_COMPANY_QUERY);
        statement.setInt(1, companyId);
        ResultSet companyResultSet = statement.executeQuery();
        companyResultSet.next();
        String companyName = companyResultSet.getString("NAME");
        return Optional.of(new RentedCarDto(carName, companyName));
    }

    private Optional<Integer> getRentedCarId(int customerId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(GET_CUSTOMER_QUERY);
        statement.setInt(1, customerId);
        ResultSet customerResultSet = statement.executeQuery();
        customerResultSet.next();
        Integer rentedCarId = customerResultSet.getInt("RENTED_CAR_ID");

        if (customerResultSet.wasNull()) {
            return Optional.empty();
        }

        return Optional.of(rentedCarId);
    }
}
