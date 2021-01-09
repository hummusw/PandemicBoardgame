public enum Role {
    CONTINGENCY_PLANNER("As an action, take any discarded Event card and store it on this card. \nWhen you play the stored Event card, remove it from the game. \n Limit: 1 Event card on this card at a time, which is  not part of your hand."),
    DISPATCHER("Move another player's pawn as if it were yours. \n As an action, move any pawn to a city with another pawn. \n Get permission before moving another player's pawn."),
    MEDIC("Remove all cubes of one color when doing Treat Disease. \n Automatically remove cubes of cured diseases from the city you are in (and prevent them from being placed there)."),
    OPERATIONS_EXPERT("As an action, build a research station in the city you are in (no City card needed). \nOnce per turn as an action, move from a research station station to any city by discarding any City card."),
    QUARANTINE_SPECIALIST("Prevent disease cube placements (and outbreaks) in the city you are in and all cities connected to it."),
    RESEARCHER("You may give any 1 of your City cards when you Share Knowledge. It need not match your city. A player who Shares Knowledge with you on their turn can take any 1 of your City cards."),
    SCIENTIST("You need only 4 cards of the same color to do the Discover a Cure action.");

    private String description;

    Role(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }
}
