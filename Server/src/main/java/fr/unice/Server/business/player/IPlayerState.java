package fr.unice.Server.business.player;

import model.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface IPlayerState extends IPlayer {
    PlayerAction chooseAction(Age age, int turn, Inventory inventoryLeft, Inventory inventoryRight);
    void announceWinner(String winnerName);
    void connected(SseEmitter emitter);
    void disconnected();
}
