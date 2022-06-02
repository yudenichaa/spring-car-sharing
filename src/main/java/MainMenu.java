import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MainMenu {
    private final ManagerMenu managerMenu;
    private final CustomerMenu customerMenu;
    private final CustomerDao customerDao;

    public MainMenu(Connection connection) {
        managerMenu = new ManagerMenu(connection);
        customerMenu = new CustomerMenu(connection);
        customerDao = new CustomerDao(connection);
    }

    public void show() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMainMenu();
            int option = scanner.nextInt();

            if (option == 0) {
                break;
            } else if (option == 1) {
                managerMenu.show();
            } else if (option == 2) {
                customerMenu.show();
            } else if (option == 3) {
                createCustomer();
            }
        }
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("1. Log in as a manager");
        System.out.println("2. Log in as a customer");
        System.out.println("3. Create a customer");
        System.out.println("0. Exit");

    }

    void createCustomer() throws SQLException {
        System.out.println();
        System.out.println("Enter the customer name:");
        Scanner scanner = new Scanner(System.in);
        String customerName = scanner.nextLine();
        customerDao.createCustomer(customerName);
        System.out.println("The customer was added!");
    }
}
