public class Car {
    private final Integer id;
    private final String name;

    private final boolean rented;

    public Car(Integer id, String name, boolean rented) {
        this.id = id;
        this.name = name;
        this.rented = rented;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isRented() {
        return rented;
    }
}
