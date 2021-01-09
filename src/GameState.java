import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GameState {
    private static final int OUTBREAK_LIMIT = 8;
    private static final int OUTBREAK_CUBES = 3; // highest number of cubes without triggering outbreak
    private static final int RESEARCH_STATION_LIMIT = 6;
    private static final int DISEASE_CUBE_LIMIT = 24;
    private static final int[] INFECTION_RATE_TRACK = {2, 2, 2, 3, 3, 4, 4};
    private static final int[] INITIAL_HAND_SIZE = {0, 0, 4, 3, 2};
    private static final int HAND_MAXSIZE = 7;

    private GamePhase gamePhase; //fixme updated but not used
    private ArrayList<Player> players;
    private Player activePlayer;
    private HashMap<DiseaseType, Integer> diseaseCubesLeft;
    private HashMap<DiseaseType, CureState> diseaseCureStates;
    private HashMap<Location, HashMap<DiseaseType, Integer>> cityDiseaseCubeCountMap;
    private HashMap<Location, Boolean> researchStationMap;
    private HashMap<Player, Location> pawnLocation;
    private int outbreakCount;
    private int epidemicCount;
    private int infectionRate;
    private int researchStationsLeft;
    private Deck<InfectionCard> infectionDeck;
    private Deck<InfectionCard> infectionDiscard;
    private Deck<PlayerCard> playerDeck;
    private Deck<PlayerCard> playerDiscard; //fixme actually move cards to discard pile
    private int difficulty; // Number of Epidemic cards in player deck (4: Introductory, 5: Standard, 6: Heroic)

    // todo add constructor

    private void gameSetup() {
        gamePhase = GamePhase.SETUP;

        // 1: Set out the board and pieces
        diseaseCubesLeft = new HashMap<>();
        for (DiseaseType diseaseType : DiseaseType.values()) {
            diseaseCubesLeft.put(diseaseType, DISEASE_CUBE_LIMIT);
        }
        researchStationsLeft = RESEARCH_STATION_LIMIT;
        researchStationMap = new HashMap<>();
        researchStationMap.put(Location.ATLANTA, true);
        researchStationsLeft--;

        // 2: Place outbreaks and cure markers
        outbreakCount = 0;
        diseaseCureStates = new HashMap<>();
        for (DiseaseType diseaseType : DiseaseType.values()) {
            diseaseCureStates.put(diseaseType, CureState.NOT_DISCOVERED);
        }

        // 3: Place infection rate marker and infect 9 cities
        epidemicCount = 0;
        cityDiseaseCubeCountMap = new HashMap<>();
        for (Location city : Location.values()) {
            cityDiseaseCubeCountMap.put(city, new HashMap<>());
        }
        infectionRate = INFECTION_RATE_TRACK[epidemicCount];
        infectionDeck = Deck.newInfectionDeck();
        infectionDiscard = new Deck<>(Deck.INFECTION_DECK_MAXSIZE);
        for (int initialCubes = 3; initialCubes > 0; initialCubes--) {
            for (int cityNum = 0; cityNum < 3; cityNum++) {
                infectCity(initialCubes);
            }
        }

        // 4: Give each player cards and a pawn
        playerDeck = Deck.newPlayerDeck();
        playerDiscard = new Deck<>(Deck.PLAYER_DECK_MAXSIZE);
        pawnLocation = new HashMap<>();
        for (Player player : players) {
            pawnLocation.put(player, Location.ATLANTA);
            for (int i = 0; i < INITIAL_HAND_SIZE[players.size()]; i++) {
                player.getHand().add(playerDeck.draw());
            }
        }

        // 5: Prepare the Player Deck
        playerDeck.initializeEpidemics(difficulty);

        // 6: Begin play
        Player startingPlayer = players.get(0);
        int highestPopulation = startingPlayer.getHand().get(0).getPopulation();
        for (Player player : players) {
            for (PlayerCard card : player.getHand()) {
                if (card.getPopulation() > highestPopulation) {
                    highestPopulation = card.getPopulation();
                    startingPlayer = player;
                }
            }
        }
        activePlayer = startingPlayer;
    }

    // todo implement phase and player switching

    // todo implement event cards

    /**
     * Action: Drive / Ferry [Movement]
     * Game description: Move to a city connected by a white line to the one you are in.
     * Role modifier: Medic automatically treats cured diseases in cities he is in (does not take an action).
     *
     * @param player Player to move
     * @param location Adjacent location to move to
     */
    public void driveFerry(Player player, Location location) {
        // Gather conditions
        boolean hasActions = player.getActionsLeft() > 0;
        Location playerLocation = pawnLocation.get(player);
        boolean neighboring = WorldMap.getNeighbors(playerLocation).contains(location);

        // Do actions if conditions are met
        if (hasActions && neighboring) {
            pawnLocation.put(player, location);
            player.decrementActionsLeft();

            // todo special case: medic + cured disease + disease cubes in city
        }
    }

    /**
     * Action: Direct Flight [Movement]
     * Game description: Discard a City card to move to the city named on the card.
     * Role modifier: Medic automatically treats cured diseases in cities he is in (does not take an action).
     *
     * @param player Player to move
     * @param location Location to move to
     */
    public void directFlight(@NotNull Player player, Location location) {
        // Variable setup
        boolean hasActions = player.getActionsLeft() > 0;
        ArrayList<PlayerCard> playerHand = player.getHand();

        // Search for matching card
        boolean hasMatchingCard = false;
        PlayerCard matchedCard = null;
        for (PlayerCard card : playerHand) {
            if (card instanceof CityCard) {
                if (((CityCard) card).getLocation() == location) {
                    hasMatchingCard = true;
                    matchedCard = card;
                }
            }
        }

        // Do action if conditions are met
        if (hasActions && hasMatchingCard) {
            player.discard(matchedCard);
            pawnLocation.put(player, location);
            player.decrementActionsLeft();

            checkMedicAutoTreat(player);
        }
    }

    /**
     * Auto-treats cured diseases in the city that the medic is in
     *
     * @param player Player moved
     */
    private void checkMedicAutoTreat(@NotNull Player player) {
        boolean isMedic = player.getRole() == Role.MEDIC;
        Location playerLocation = pawnLocation.get(player);

        if (isMedic) {
            for (DiseaseType diseaseType : DiseaseType.values()) {
                // Auto treat cured diseases
                if (diseaseCureStates.get(diseaseType) == CureState.CURE_DISCOVERED) {
                    int cubesToRemove = cityDiseaseCubeCountMap.get(playerLocation).get(diseaseType);
                    cityDiseaseCubeCountMap.get(playerLocation).put(diseaseType, 0);
                    diseaseCubesLeft.put(diseaseType, diseaseCubesLeft.get(diseaseType) + cubesToRemove);

                    // Eradication check
                    if (diseaseCubesLeft.get(diseaseType) == DISEASE_CUBE_LIMIT) {
                        diseaseCureStates.put(diseaseType, CureState.ERADICATED);
                    }
                }
            }
        }
    }

    /**
     * Action: Charter Flight [Movement]
     * Game description: Discard the City card that matches the city you are in and move to any city.
     * Role modifier: Medic automatically treats cured diseases in cities he is in (does not take an action).
     *
     * @param player Player to move
     * @param location Location to move to
     */
    public void charterFlight(@NotNull Player player, @NotNull Location location) {
        // Variable setup
        boolean hasActions = player.getActionsLeft() > 0;
        Location playerLocation = pawnLocation.get(player);
        ArrayList<PlayerCard> playerHand = player.getHand();

        // Search for matching card
        boolean hasMatchingCard = false;
        PlayerCard matchedCard = null;
        for (PlayerCard card : playerHand) {
            if (card instanceof CityCard) {
                if (((CityCard) card).getLocation() == playerLocation) {
                    hasMatchingCard = true;
                    matchedCard = card;
                }
            }
        }

        // Do action if conditions are met
        if (hasActions && hasMatchingCard) {
            player.discard(matchedCard);
            pawnLocation.put(player, location);
            player.decrementActionsLeft();

            checkMedicAutoTreat(player);
        }
    }

    /**
     * Action: Shuttle Flight [Movement]
     * Game description: Move from a city with a research station to any other city that has a research station
     * Role modifier: Medic automatically treats cured diseases in cities he is in (does not take an action).
     *
     * @param player Player to move
     * @param location Location with a research station to move to
     */
    public void shuttleFlight(@NotNull Player player, @NotNull Location location) {
        // Variable setup
        boolean hasActions = player.getActionsLeft() > 0;
        Location playerLocation = pawnLocation.get(player);
        boolean fromResearchStation = researchStationMap.get(playerLocation);
        boolean toResearchStation = researchStationMap.get(location);

        // Do action if conditions are met
        if (hasActions && fromResearchStation && toResearchStation) {
            pawnLocation.put(player, location);
            player.decrementActionsLeft();

            checkMedicAutoTreat(player);
        }
    }

    /**
     * Action: Build a Research Station [Other]
     * Game description: Discard the City card that matches the city you are in to place a research station there. Take the research station from the pile next to the board. If all 6 reserach stations have been built, take a research station from anywhere on the board.
     * Role modifier: Operations Expert can build a research station in his current city without discarding a City card.
     *
     * @param player Player to build a research station
     */
    public void buildResearchStation(@NotNull Player player) {
        // Variable setup
        boolean hasActions = player.getActionsLeft() > 0;
        Location playerLocation = pawnLocation.get(player);
        boolean isOperationsExpert = player.getRole() == Role.OPERATIONS_EXPERT;
        ArrayList<PlayerCard> playerHand = player.getHand();

        // Search for matching card
        boolean hasMatchingCard = false;
        PlayerCard matchedCard = null;
        for (PlayerCard card : playerHand) {
            if (card instanceof CityCard) {
                if (((CityCard) card).getLocation() == playerLocation) {
                    hasMatchingCard = true;
                    matchedCard = card;
                }
            }
        }

        // Do action if conditions are met
        if (hasActions && (isOperationsExpert || hasMatchingCard)) {
            if (!isOperationsExpert) {
                player.discard(matchedCard);
            }
            player.decrementActionsLeft();

            if (researchStationsLeft == 0) {
                askPlayerForResearchStationToTake();
            }

            researchStationMap.put(playerLocation, true);
            researchStationsLeft--;
        }
    }

    //todo implement
    private void askPlayerForResearchStationToTake() {
        //note increment researchStationsLeft
    }

    /**
     * Action: Treat Disease [Other]
     * Game description: Remove 1 disease cube from the city you are in, placing it in the cube supply next to the board. If this disease has been cured, remove all cubes of that color from the city you are in. If the last cube of a cured disease is removed from the board, this disease is eradicated. Flip its cured marker from its "vial" side to its "∅" side.
     * Role modifier: Medic removes all cubes of the same color
     *
     * @param player Player to treat disease
     * @param diseaseType Disease to treat
     */
    public void treatDisease(Player player, DiseaseType diseaseType) {
        // Variable setup
        boolean hasActions = player.getActionsLeft() > 0;
        Location playerLocation = pawnLocation.get(player);
        boolean isMedic = player.getRole() == Role.MEDIC;
        boolean isCured = diseaseCureStates.get(diseaseType) == CureState.CURE_DISCOVERED;

        // Do action if conditions are met
        if (hasActions) {
            // Calculations
            int removeUpTo = (isMedic || isCured) ? 3 : 1;
            int currentCubes = cityDiseaseCubeCountMap.get(playerLocation).get(diseaseType);
            int cubesToRemove = Math.min(removeUpTo, currentCubes);

            // Move cubes from map to supply, decrement player actions
            cityDiseaseCubeCountMap.get(playerLocation).put(diseaseType, currentCubes - cubesToRemove);
            diseaseCubesLeft.put(diseaseType, diseaseCubesLeft.get(diseaseType) + cubesToRemove);
            player.decrementActionsLeft();

            // Eradication check
            boolean noCubesLeft = diseaseCubesLeft.get(diseaseType) == DISEASE_CUBE_LIMIT;
            if (isCured && noCubesLeft) {
                diseaseCureStates.put(diseaseType, CureState.ERADICATED);
            }
        }
    }

    /**
     * Action: Share Knowledge [Other]
     * Game description: You can do this action in two ways: give the City card that matches the city you are in to another player, or take the City card that matches the city you are in from another player. The other player must also be int he city with you. Both of you need to agree to do this. If the player who gets the card now has more than 7 cards, that player must immediately discard a card or play an Event card.
     * Role modifier: Researcher can give any City card from her hand to another player in the same city as her, without this card having to match her city.
     *
     * @param fromPlayer Player to give the card
     * @param toPlayer Player to receive the card
     * @param card City card to transfer
     */
    public void shareKnowledge(Player fromPlayer, Player toPlayer, CityCard card) {
        // Variable setup
        boolean hasActions = activePlayer.getActionsLeft() > 0; // hmmmmmmm
        boolean activePlayerSharing = activePlayer == fromPlayer || activePlayer == toPlayer;
        boolean playersInSameCity = pawnLocation.get(fromPlayer) == pawnLocation.get(toPlayer);
        boolean cardMatchesCity = card.getLocation() == pawnLocation.get(activePlayer);
        boolean fromResearcher = fromPlayer.getRole() == Role.RESEARCHER;

        // Do action if conditions are met
        if (hasActions && activePlayerSharing && playersInSameCity && (fromResearcher || cardMatchesCity)) {
            fromPlayer.discard(card);
            toPlayer.addToHand(card);
            activePlayer.decrementActionsLeft();

            // todo if the player who gets the card now has more than 7 cards, that player must immediately discard a card or play event
        }
    }

    /**
     * Action: Discover a Cure [Other]
     * Game description: At any research station, discard 5 City cards of the same color from your hand to cure the disease of that color. Move the disease's cure marker to its Cure Indicator. If no cubes of this color are on the board, this disease is now eradicated. Flip its cure marker from its "vial" side to its "∅" side.
     * Role modifier: Scientist only needs 4 City cards of the same disease color to discover a cure for that disease.
     *
     * @param player Player to discover a cure
     * @param diseaseType Disease to cure
     * @param cards Set of cards to discard
     */
    public void discoverCure(Player player, DiseaseType diseaseType, Set<CityCard> cards) {
        // Variable setup
        boolean hasActions = player.getActionsLeft() > 0;
        Location playerLocation = pawnLocation.get(player);
        boolean atResearchStation = researchStationMap.get(playerLocation);
        boolean isScientist = player.getRole() == Role.SCIENTIST;
        int cardsNeeded = isScientist ? 4 : 5; //fixme add a constant
        boolean quantityReached = cards.size() == cardsNeeded;
        boolean cardTypesCorrect = true;
        boolean playerHasCards = true;

        // Verify card types
        for (CityCard card : cards) {
            if (card.getLocation().getDiseaseType() != diseaseType) {
                cardTypesCorrect = false;
            }
            if (!player.getHand().contains(card)) {
                playerHasCards = false;
            }
        }

        // Do action if conditions are met
        if (hasActions && atResearchStation && quantityReached && cardTypesCorrect && playerHasCards) {
            for (CityCard card : cards) {
                player.discard(card);
            }

            boolean noCubesLeft = diseaseCubesLeft.get(diseaseType) == DISEASE_CUBE_LIMIT;
            CureState cureState = noCubesLeft ? CureState.ERADICATED : CureState.CURE_DISCOVERED;
            diseaseCureStates.put(diseaseType, cureState);

            player.decrementActionsLeft();
        }
    }

    /**
     * Ends action phase
     */
    public void endActionPhase() {
        activePlayer.clearActionsLeft();
        gamePhase = GamePhase.DRAW_CARDS;
    }

    /**
     * Adds cards to player's hand, asks to discard if necessary, and advances game phase.
     */
    public void drawCards() {
        for (int i = 0; i < 2; i++) { //fixme add 2 as a constant
            try {
                PlayerCard card = playerDeck.draw();
                if (card.getCardType() == CardType.EPIDEMIC) {
                    epidemicTriggered();
                } else {
                    activePlayer.getHand().add(card);
                }
            } catch (IllegalStateException noCardsLeft) {
                lossTriggered(LossCondition.OUT_OF_TIME);
                return;
            }
        }

        while (activePlayer.getHand().size() > HAND_MAXSIZE) {
            askPlayerToDiscard();
        }

        gamePhase = GamePhase.INFECT;
    }

    //todo implement
    private void askPlayerToDiscard() {
    }

    /**
     * Resolves an epidemic event
     */
    private void epidemicTriggered() {
        // 1: Increase
        epidemicCount++;
        infectionRate = INFECTION_RATE_TRACK[epidemicCount];

        // 2: Infect
        Location epidemicCity = infectionDeck.drawBottom().getLocation();
        infectionDiscard.add(new InfectionCard(epidemicCity));
        DiseaseType diseaseType = epidemicCity.getDiseaseType();
        if (diseaseCureStates.get(diseaseType) != CureState.ERADICATED) {
            addCubesAttempt(epidemicCity, OUTBREAK_CUBES);
        }

        // 3: Intensify
        infectionDeck.addDiscard(infectionDiscard);
    }

    /**
     * Infects cities according to infection rate
     *
     * Game phase: 3 (Infect)
     *
     */
    public void infectCities() {
        // Infect the same number of cities as the current infection rate, adding one cube to each city
        for (int i = 0; i < infectionRate; i++) {
            infectCity(1);
        }
    }

    /**
     * Ends infection phase, advances active player
     */
    public void endInfectPhase() {
        gamePhase = GamePhase.ACTION;

        int index = players.indexOf(activePlayer);
        if (index == players.size() - 1) {
            index = 0;
        } else {
            index++;
        }
        activePlayer = players.get(index);
    }

    private void infectCity(int cubes) {
        // Get city to infect
        Location city = infectionDeck.draw().getLocation();
        infectionDiscard.add(new InfectionCard(city));
        addCubesAttempt(city, cubes);
    }

    private void addCubesAttempt(Location city, int cubes) {
        // See if this disease has already been eradicated
        DiseaseType diseaseType = city.getDiseaseType();
        if (diseaseCureStates.get(diseaseType) == CureState.ERADICATED) {
            return;
        }

        // Count cubes already on city and left in game
        int existingCubes = cityDiseaseCubeCountMap.get(city).get(diseaseType);
        int cubesLeft = diseaseCubesLeft.get(diseaseType);

        // Add cube to city if it doesn't cause an outbreak and there are enough cubes left
        if (existingCubes + cubes > OUTBREAK_CUBES) {
            int remainingCubes = OUTBREAK_CUBES - existingCubes; //fixme find a better name
            if (cubesLeft < remainingCubes) {
                lossTriggered(LossCondition.SPREAD_TOO_MUCH);
            } else {
                cityDiseaseCubeCountMap.get(city).put(diseaseType, existingCubes + remainingCubes);
                diseaseCubesLeft.put(city.getDiseaseType(), cubesLeft - remainingCubes);
                outbreak(city);
            }
        } else if (cubesLeft < cubes) {
            lossTriggered(LossCondition.SPREAD_TOO_MUCH);
        } else {
            cityDiseaseCubeCountMap.get(city).put(diseaseType, existingCubes + cubes);
            diseaseCubesLeft.put(city.getDiseaseType(), cubesLeft - cubes);
        }
    }

    private void outbreak(Location origin) {
        // Set up pending queue and finished set
        Queue<Location> pendingOutbreaks = new LinkedList<>();
        Set<Location> finishedOutbreaks = new HashSet<>();
        pendingOutbreaks.add(origin);

        DiseaseType outbreakType = origin.getDiseaseType();

        while (!pendingOutbreaks.isEmpty()) {
            // Increase outbreak counter
            outbreakCount++;
            if (outbreakCount >= OUTBREAK_LIMIT) {
                lossTriggered(LossCondition.WORLDWIDE_PANIC);
                return;
            }

            // Get outbreak location and neighbors
            Location outbreakLocation = pendingOutbreaks.remove();
            List<Location> neighbors = WorldMap.getNeighbors(outbreakLocation);

            // Spread to neighbors
            for (Location neighbor : neighbors) {
                // Do not spread to a city more than once during a chain outbreak
                if (!finishedOutbreaks.contains(neighbor)) {
                    int neighborCubes = cityDiseaseCubeCountMap.get(neighbor).get(outbreakType);

                    // If this would cause a neighbor to go over 3, add to the list of pending outbreaks
                    if (neighborCubes == OUTBREAK_CUBES) {
                        pendingOutbreaks.add(neighbor);
                    }

                    // Add a cube to a neighbor with less than 3 of this type
                    else {
                        // If there are no cubes of this type left, players have lost
                        if (diseaseCubesLeft.get(outbreakType) <= 0) {
                            lossTriggered(LossCondition.SPREAD_TOO_MUCH);
                            return;
                        }

                        // Otherwise, add a disease cube
                        else {
                            cityDiseaseCubeCountMap.get(neighbor).put(outbreakType, neighborCubes + 1);
                            diseaseCubesLeft.put(outbreakType, diseaseCubesLeft.get(outbreakType) - 1);
                        }
                    }
                }
            }

            finishedOutbreaks.add(outbreakLocation);
        }
    }

    private void lossTriggered(LossCondition lossCondition) {
        //todo
    }

}
