import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ManagerMenu {
    private final CompanyDao companyDao;
    private final CarMenu carMenu;

    public ManagerMenu(Connection connection) {
        companyDao = new CompanyDao(connection);
        carMenu = new CarMenu(connection);
    }

    public void show() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printCompanyMenu();
            int option = scanner.nextInt();

            if (option == 0) {
                break;
            } else if (option == 1) {
                chooseCompany();
            } else if (option == 2) {
                createCompany();
            }
        }
    }

    private void printCompanyMenu() {
        System.out.println();
        System.out.println("1. Company list");
        System.out.println("2. Create a company");
        System.out.println("0. Back");
    }

    private void chooseCompany() throws SQLException {
        List<Company> companies = companyDao.getCompanies();

        if (companies.size() == 0) {
            System.out.println("\nThe company list is empty!");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        int companyId;

        do {
            printCompaniesList(companies);
            companyId = scanner.nextInt();
        } while (companyId < 0 || companyId > companies.size());

        if (companyId == 0) {
            return;
        }

        String companyName = null;

        for (Company c : companies) {
            if (c.getId() == companyId) {
                companyName = c.getName();
            }
        }

        carMenu.show(companyId, companyName);
    }

    private void printCompaniesList(List<Company> companies) {
        System.out.println("\nChoose a company:");
        companies.stream()
                .sorted(Comparator.comparing(Company::getId))
                .forEach(c -> System.out.println(c.getId() + ". " + c.getName()));
        System.out.println("0. Back");
    }

    private void createCompany() throws SQLException {
        System.out.println();
        System.out.println("Enter the company name:");
        Scanner scanner = new Scanner(System.in);
        String companyName = scanner.nextLine();
        companyDao.createCompany(companyName);
        System.out.println("The company was created!");
    }
}
