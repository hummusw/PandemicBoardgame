public class CityCard extends PlayerCard {
    private Location location;

    public CityCard(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public CardType getCardType() {
        return CardType.CITY;
    }

    @Override
    public int getPopulation() {
        return location.getPopulation();
    }
}
