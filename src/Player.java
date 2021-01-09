import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

public class Player {

    private static final int HAND_LIMIT = 7;
    private static final int ACTION_LIMIT = 4;

    private String name;
    private Role role;
    private ArrayList<PlayerCard> hand;
    private PlayerCard contingencyCard;
    private int actionsLeft;

    public Player(@NotNull String name, @NotNull Role role) {
        this.name = name;
        this.role = role;
        this.hand = new ArrayList<>(HAND_LIMIT);
        this.contingencyCard = null;
        this.actionsLeft = ACTION_LIMIT;
    }

    public void newTurn() {
        actionsLeft = ACTION_LIMIT;
    }

    public void addToHand(@NotNull PlayerCard card) {
        this.hand.add(card);
    }

    public void discard(@NotNull PlayerCard card) throws IllegalArgumentException {
        if (!this.hand.contains(card)) {
            throw new IllegalArgumentException("Card not in hand");
        }

        this.hand.remove(card);
    }

    public boolean overHandLimit() {
        return this.hand.size() > HAND_LIMIT;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public int getActionsLeft() {
        return actionsLeft;
    }

    public void decrementActionsLeft() {
        actionsLeft--;
    }

    public void clearActionsLeft() {
        actionsLeft = 0;
    }

    public ArrayList<PlayerCard> getHand() {
        return hand;
    }
}
