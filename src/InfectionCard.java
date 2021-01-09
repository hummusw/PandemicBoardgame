public class InfectionCard extends Card {

    private Location location;

    public InfectionCard(Location location) {
        this.location = location;
    }

    @Override
    public CardType getCardType() {
        return CardType.INFECTION;
    }

    public Location getLocation() {
        return location;
    }
}
