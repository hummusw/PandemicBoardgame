public class EventCard extends PlayerCard {

    private EventType eventType;

    public EventCard(EventType type) {
        this.eventType = type;
    }

    @Override
    public CardType getCardType() {
        return CardType.EVENT;
    }

    @Override
    public int getPopulation() {
        return 0;
    }

    public EventType getEventType() {
        return eventType;
    }

}
