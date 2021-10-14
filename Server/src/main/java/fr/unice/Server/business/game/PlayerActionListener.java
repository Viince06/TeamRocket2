package fr.unice.Server.business.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerActionListener {

    private static final Logger logger = LoggerFactory.getLogger(PlayerActionListener.class);

    private final static long DEFAULT_TIME_VALUE = 5000;

    private boolean everyonePlayed = false;

    public PlayerActionListener() {
    }

    public void reset() {
        this.everyonePlayed = false;
    }

    public synchronized void waiting() {
        if (this.everyonePlayed)
            return;
        try {
            this.wait(DEFAULT_TIME_VALUE);
        } catch (InterruptedException e) {
            logger.error("PlayerActionListener was interrupted :", e);
        }
    }

    public synchronized void notifying() {
        this.everyonePlayed = true;
        this.notifyAll();
    }
}
