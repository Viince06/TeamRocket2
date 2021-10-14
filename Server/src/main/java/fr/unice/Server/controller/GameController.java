package fr.unice.Server.controller;

import dto.ActionObject;
import fr.unice.Server.service.GameService;
import model.PlayerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/game")
public class GameController {

    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameService gameService;


    @GetMapping("/join")
    public SseEmitter joinGame(@RequestAttribute UUID playerId) {
        SseEmitter emitter = new SseEmitter(3600000L);
        UUID gameId = gameService.userJoin(playerId, emitter);

        try {
            SseEmitter.SseEventBuilder event = SseEmitter.event()
                    .id("join")
                    .data(gameId.toString())
                    .name("join");
            emitter.send(event);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return emitter;
    }

    @PostMapping("/{gameId}/play")
    public String play(@RequestAttribute UUID playerId, @PathVariable String gameId, @RequestBody PlayerAction playerAction) {
        this.gameService.userPlay(UUID.fromString(gameId), playerId, playerAction);
        return "OK";
    }

    @GetMapping("/{gameId}/actionObject")
    public ActionObject actionObject(@RequestAttribute UUID playerId, @PathVariable String gameId) {
        return this.gameService.recoverCurrentActionObject(UUID.fromString(gameId), playerId);
    }

    //TODO : /status
}
