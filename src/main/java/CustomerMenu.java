import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CustomerMenu {
    private final CustomerDao customerDao;
    private final CompanyDao companyDao;
    private final CarDao carDao;

    public CustomerMenu(Connection connection) {
        customerDao = new CustomerDao(connection);
        companyDao = new CompanyDao(connection);
        carDao = new CarDao(connection);
    }

    public void show() throws SQLException {
        int customerId = chooseCustomer();

        if (customerId == 0) {
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printCustomerMenu();
            int option = scanner.nextInt();

            if (option == 0) {
                break;
            } else if (option == 1) {
                rentCar(customerId);
            } else if (option == 2) {
                returnCar(customerId);
            } else if (option == 3) {
                printRentedCarInfo(customerId);
            }
        }
    }

    private void printCustomerMenu() {
        System.out.println();
        System.out.println("1. Rent a car");
        System.out.println("2. Return a rented car");
        System.out.println("3. My rented car");
        System.out.println("0. Back");
    }

    private void printCustomersList(List<Customer> customers) {
        System.out.println("\nChoose a customer:");
        customers.stream()
                .sorted(Comparator.comparing(Customer::getId))
                .forEach(c -> System.out.println(c.getId() + ". " + c.getName()));
        System.out.println("0. Back");
    }

    private int chooseCustomer() throws SQLException {
        List<Customer> customers = customerDao.getCustomers();

        if (customers.size() == 0) {
            System.out.println("\nThe customer list is empty!");
            return 0;
        }

        Scanner scanner = new Scanner(System.in);
        int customerId;

        do {
            printCustomersList(customers);
            customerId = scanner.nextInt();
        } while (customerId < 0 || customerId > customers.size());

        return customerId;
    }

    private void printCompaniesList(List<Company> companies) {
        System.out.println("\nChoose a company:");
        companies.stream()
                .sorted(Comparator.comparing(Company::getId))
                .forEach(c -> System.out.println(c.getId() + ". " + c.getName()));
        System.out.println("0. Back");
    }

    private void printCarsList(List<Car> cars) {
        System.out.println("\nChoose a car:");
        IntStream
                .range(0, cars.size())
                .forEach(idx -> System.out.println((idx + 1) + ". " + cars.get(idx).getName()));
    }

    private void rentCar(int customerId) throws SQLException {
        if (customerDao.isCarRented(customerId)) {
            System.out.println("\nYou've already rented a car!");
            return;
        }

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

        List<Car> cars = carDao.getCars(companyId);
        List<Car> availableCars = cars.stream()
                .filter(c -> !c.isRented())
                .collect(Collectors.toList());


        if (availableCars.size() == 0) {
            System.out.println("\nNo available cars in the '" + companyName + "'");
            return;
        }

        int carIndex;

        do {
            printCarsList(availableCars);
            carIndex = scanner.nextInt();
        } while (carIndex < 0 || carIndex > companies.size());

        if (carIndex == 0) {
            return;
        }

        Car car = cars.get(carIndex - 1);
        customerDao.rentCar(customerId, car.getId());
        System.out.println("You rented '" + car.getName() + "'");
    }

    private void returnCar(int customerId) throws SQLException {
        if (customerDao.returnCar(customerId)) {
            System.out.println("\nYou've returned a rented car!");
        } else {
            System.out.println("\nYou didn't rent a car!");
        }
    }

    private void printRentedCarInfo(int customerId) throws SQLException {
        Optional<RentedCarDto> rentedCar = customerDao.getRentedCarInfo(customerId);
        if (rentedCar.isPresent()) {
            RentedCarDto carInfo = rentedCar.get();
            System.out.println("\nYour rented car:");
            System.out.println(carInfo.getCarName());
            System.out.println("Company:");
            System.out.println(carInfo.getCompanyName());
        } else {
            System.out.println("\nYou didn't rent a car!");
        }
    }
}
