package fr.unice.Server.business.player;

import java.util.LinkedList;
import java.util.UUID;

public class PlayerList extends LinkedList<Player> {

    public Player leftPlayerOf(Player current) {
        int currentIndex = this.indexOf(current);
        if (currentIndex == -1) {
            throw new IllegalArgumentException("Player not in list");
        }
        if (currentIndex == 0) {
            return this.getLast();
        } else {
            return this.get(currentIndex - 1);
        }
    }

    public Player rightPlayerOf(Player current) {
        int currentIndex = this.indexOf(current);
        if (currentIndex == -1) {
            throw new IllegalArgumentException("Player not in list");
        }

        if (currentIndex == this.size() - 1) {
            return this.getFirst();
        } else {
            return this.get(currentIndex + 1);
        }
    }

    private int indexOf(Player target) {
        if (target == null) {
            return -1;
        }

        UUID targetUUID = target.getPlayerId();

        int index = 0;
        for (Player t : this) {
            if (targetUUID.equals(t.getPlayerId())) {
                return index;
            }
            ++index;
        }

        return -1;
    }

    public Player getByUUID(UUID uuid) {
        for (Player t : this) {
            if (uuid.equals(t.getPlayerId()))
                return t;
        }

        return null;
    }
}
