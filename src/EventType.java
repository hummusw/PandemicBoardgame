public enum EventType {

    AIRLIFT("Move any 1 pawn to any city. Get permission before moving another player's pawn."),
    FORECAST("Draw, look at, and rearrange the top 6 cards of the Infection Deck. Put them back on top."),
    GOVERNMENT_GRANT("Add 1 research station to any city (no City card needed."),
    ONE_QUIET_NIGHT("Skip the next Infect Cities step (do not flip over any Infection cards)."),
    RESILIENT_POPULATION("Remove any 1 card in the Infection Discard Pile from the game. You may play this between the Infect and Intensify steps of an epidemic.");

    private String description;

    EventType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
