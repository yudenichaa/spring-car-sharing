import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDao {
    static String SELECT_COMPANIES_QUERY = "" +
            "SELECT ID, NAME\n" +
            "FROM COMPANY;";

    static String CREATE_COMPANY_QUERY = "" +
            "INSERT INTO COMPANY (NAME)\n" +
            "VALUES (?);";

    private final Connection connection;

    public CompanyDao(Connection connection) {
        this.connection = connection;
    }

    public List<Company> getCompanies() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet companiesResultSet = statement.executeQuery(SELECT_COMPANIES_QUERY);
        List<Company> companies = new ArrayList<>();

        while (companiesResultSet.next()) {
            Integer id = companiesResultSet.getInt("ID");
            String name = companiesResultSet.getString("NAME");
            Company company = new Company(id, name);
            companies.add(company);
        }

        return companies;
    }

    public void createCompany(String companyName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(CREATE_COMPANY_QUERY);
        statement.setString(1, companyName);
        statement.executeUpdate();
    }
}