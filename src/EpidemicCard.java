public class EpidemicCard extends PlayerCard {

    @Override
    public CardType getCardType() {
        return CardType.EPIDEMIC;
    }

    @Override
    public int getPopulation() {
        return 0;
    }
}
