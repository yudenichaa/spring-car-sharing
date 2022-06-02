public class RentedCarDto {
    private final String carName;
    private final String companyName;

    public RentedCarDto(String carName, String companyName) {
        this.carName = carName;
        this.companyName = companyName;
    }

    public String getCarName() {
        return carName;
    }

    public String getCompanyName() {
        return companyName;
    }
}
