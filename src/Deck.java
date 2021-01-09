import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.shuffle;

public class Deck<T extends Card> {
    static final int INFECTION_DECK_MAXSIZE = 48; // 48 Locations
    static final int PLAYER_DECK_MAXSIZE = 59; // 48 City cards, 6 Epidemic cards, 5 Event cards

    private ArrayList<T> deck;

    // Constructors
    public Deck() {
        deck = new ArrayList<>();
    }

    public Deck(int initialCapacity) {
        deck = new ArrayList<>(initialCapacity);
    }

    public Deck(ArrayList<T> arrayList) {
        deck = arrayList;
    }

    // Static "constructors"
    public static Deck<InfectionCard> newInfectionDeck() {
        ArrayList<InfectionCard> out = new ArrayList<>(INFECTION_DECK_MAXSIZE);

        for (Location location : Location.values()) {
            out.add(new InfectionCard(location));
        }

        shuffle(out);

        return new Deck<>(out);
    }

    public static Deck<PlayerCard> newPlayerDeck() {
        ArrayList<PlayerCard> out = new ArrayList<>(PLAYER_DECK_MAXSIZE);

        for (Location location : Location.values()) {
            out.add(new CityCard(location));
        }

        for (EventType event : EventType.values()) {
            out.add(new EventCard(event));
        }

        shuffle(out);

        return new Deck<>(out);
    }

    // Public methods
    public T draw() throws IllegalStateException {
        if (deck.isEmpty()) {
            throw new IllegalStateException("Deck is empty!");
        }

        return deck.remove(deck.size() - 1);
    }

    public T drawBottom() {
        return deck.remove(0);
    }

    public void add(T card) {
        deck.add(card);
    }

    public void addDiscard(Deck<T> other) {
        shuffle(other.deck);
        deck.addAll(other.deck);
        other.deck = new ArrayList<>();
    }

    public int size() {
        return deck.size();
    }

    public void initializeEpidemics(int count) throws IllegalStateException {
        // Create count number of subdecks of equal size
        List<PlayerCard>[] subdecks = (ArrayList<PlayerCard>[]) new ArrayList[count];
        int subdeckLength = deck.size()/count;
        for (int i = 0; i < count - 1; i++) {
            subdecks[i] = (List<PlayerCard>) deck.subList(i * subdeckLength, (i + 1) * subdeckLength);
        }
        subdecks[count - 1] = (List<PlayerCard>) deck.subList((count - 1) * subdeckLength, deck.size() - 1);

        // Add an epidemic card to each subdeck
        for (List<PlayerCard> subdeck : subdecks) {
            subdeck.add(new EpidemicCard());
            shuffle(subdeck);
        }

        // Combine subdecks into player deck
        ArrayList<T> newDeck = new ArrayList<>(PLAYER_DECK_MAXSIZE);
        for (int i = 0; i < count; i++) {
            newDeck.addAll((Collection<? extends T>) subdecks[i]);
        }
        deck = newDeck;
    }
}
