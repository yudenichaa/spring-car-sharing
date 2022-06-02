import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class CarMenu {
    private final CarDao carDao;

    public CarMenu(Connection connection) {
        carDao = new CarDao(connection);;
    }

    public void show(Integer companyId, String companyName) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printCarMenu(companyName);
            int option = scanner.nextInt();

            if (option == 0) {
                break;
            } else if (option == 1) {
                printCarsList(companyId, companyName);
            } else if (option == 2) {
                createCar(companyId);
            }
        }
    }

    private void printCarMenu(String companyName) {
        System.out.println();
        System.out.println("'" + companyName + "' company:");
        System.out.println("1. Car list");
        System.out.println("2. Create a car");
        System.out.println("0. Back");
    }

    private void printCarsList(int companyId, String companyName) throws SQLException {
        List<Car> cars = carDao.getCars(companyId);
        System.out.println();

        if (cars.size() == 0) {
            System.out.println("The car list is empty!");
            return;
        }

        System.out.println("'" + companyName + "' cars:");
        IntStream
                .range(0, cars.size())
                .forEach(idx -> System.out.println((idx + 1) + ". " + cars.get(idx).getName()));
    }

    private void createCar(int companyId) throws SQLException {
        System.out.println("\nEnter the car name:");
        Scanner scanner = new Scanner(System.in);
        String carName = scanner.nextLine();
        carDao.createCar(carName, companyId);
        System.out.println("The car was added!");
    }
}
