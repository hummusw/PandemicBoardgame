public enum Location {
    ALGIERS("Algiers", "Algeria", DiseaseType.BLACK, 2946000, 6500),
    ATLANTA("Atlanta", "United States", DiseaseType.BLUE, 4715000, 700),
    BAGHDAD("Baghdad", "Iraq", DiseaseType.BLACK, 6204000, 10400),
    BANGKOK("Bangkok", "Thailand", DiseaseType.RED, 7151000, 3200),
    BEIJING("Beijing", "People's Republic of China", DiseaseType.RED, 17311000, 5000),
    BOGOTA("Bogotá", "Colombia", DiseaseType.YELLOW, 8702000, 21000),
    BUENOS_AIRES("Buenos Aires", "Argentina", DiseaseType.YELLOW, 13639000, 5200),
    CAIRO("Cairo", "Egypt", DiseaseType.BLACK, 14718000, 8900),
    CHENNAI("Chennai", "India", DiseaseType.BLACK, 8865000, 14600),
    CHICAGO("Chicago", "United States", DiseaseType.BLUE, 9121000, 1300),
    DELHI("Delhi", "India", DiseaseType.BLACK, 22242000, 11500),
    ESSEN("Essen", "Germany", DiseaseType.BLUE, 575000, 2800),
    HO_CHI_MINH_CITY("Ho Chi Minh City", "Vietnam", DiseaseType.RED, 8314000, 9900),
    HONG_KONG("Hong Kong", "People's Republic of China", DiseaseType.RED, 7106000, 25900),
    ISTANBUL("Istanbul", "Turkey", DiseaseType.BLACK, 13576000, 9700),
    JAKARTA("Jakarta", "Indonesia", DiseaseType.RED, 26063000, 9400),
    JOHANNESBURG("Johannesburg", "South Africa", DiseaseType.YELLOW, 3888000, 2400),
    KARACHI("Karachi", "Pakistan", DiseaseType.BLACK, 20711000, 25800),
    KHARTOUM("Khartoum", "Sudan", DiseaseType.YELLOW, 4887000, 4500),
    KINSHASA("Kinshasa", "Dem. Republic of the Congo", DiseaseType.YELLOW, 9046000, 15500),
    KOLKATA("Kolkata", "India", DiseaseType.BLACK, 14374000, 11900),
    LAGOS("Lagos", "Nigeria", DiseaseType.YELLOW, 11547000, 12700),
    LIMA("Lima", "Peru", DiseaseType.YELLOW, 9121000, 14100),
    LONDON("London", "United Kingdom", DiseaseType.BLUE, 8586000, 5300),
    LOS_ANGELES("Los Angeles", "United States", DiseaseType.YELLOW, 14900000, 2400),
    MADRID("Madrid", "Spain", DiseaseType.BLUE, 542700, 5700),
    MANILA("Manila", "Philippines", DiseaseType.RED, 20767000, 14400),
    MEXICO_CITY("Mexico City", "Mexico", DiseaseType.YELLOW, 19463000, 9500),
    MIAMI("Miami", "United States", DiseaseType.YELLOW, 5582000, 1700),
    MILAN("Milan", "Italy", DiseaseType.BLUE, 5232000, 2800),
    MONTREAL("Montréal", "Canada", DiseaseType.BLUE, 3429000, 2200),
    MOSCOW("Moscow", "Russia", DiseaseType.BLACK, 15512000, 3500),
    MUMBAI("Mumbai", "India", DiseaseType.BLACK, 16910000, 30900),
    NEW_YORK("New York", "United States", DiseaseType.BLUE, 20464000, 1800),
    OSAKA("Osaka", "Japan", DiseaseType.RED, 2871000, 13000),
    PARIS("Paris", "France", DiseaseType.BLUE, 10755000, 3800),
    RIYADH("Riyadh", "Saudi Arabia", DiseaseType.BLACK, 5037000, 3400),
    SAN_FRANCISCO("San Francisco", "United States", DiseaseType.BLUE, 5864000, 2100),
    SANTIAGO("Santiago", "Chile", DiseaseType.YELLOW, 6015000, 6500),
    SAO_PAULO("São Paulo", "Brazil", DiseaseType.YELLOW, 20186000, 6400),
    SEOUL("Seoul", "South Korea", DiseaseType.RED, 22547000, 10400),
    SHANGHAI("Shanghai", "People's Republic of China", DiseaseType.RED, 13482000, 2200),
    ST_PETERSBURG("St. Petersburg", "Russia", DiseaseType.BLUE, 4879000, 4100),
    SYDNEY("Sydney", "Australia", DiseaseType.RED, 3785000, 2100),
    TAIPEI("Taipei", "Taiwan", DiseaseType.RED, 8338000, 7300),
    TEHRAN("Tehran", "Iran", DiseaseType.BLACK, 7419000, 9500),
    TOKYO("Tokyo", "Japan", DiseaseType.RED, 13189000, 6030),
    WASHINGTON("Washington", "United States", DiseaseType.BLUE, 4679000, 1400);

    private String nameOfCity;
    private String nameOfCountry;
    private DiseaseType diseaseType;
    private int population;

    Location(String city, String country, DiseaseType type, int pop, int density) {
        nameOfCity = city;
        nameOfCountry = country;
        diseaseType = type;
        population = pop;
    }

    public String getNameOfCity() {
        return nameOfCity;
    }

    public String getNameOfCountry() {
        return nameOfCountry;
    }

    public DiseaseType getDiseaseType() {
        return diseaseType;
    }

    public int getPopulation() {
        return population;
    }
}
