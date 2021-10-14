package fr.unice.Player.controller;

import fr.unice.Player.service.ClientService;
import fr.unice.Player.service.PlayerService;
import dto.ActionObject;
import model.PlayerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import model.Resources;
import model.Trade;

import java.util.List;

@RestController
public class PlayerController {

    Logger logger = LoggerFactory.getLogger(PlayerController.class);

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ClientService clientService;

    @PostMapping(value = "/play", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PlayerAction actionChosen(@RequestBody ActionObject body) throws Exception {
        logger.info("/play - turn " + body.getTurn());
        PlayerAction action = this.playerService.chooseAction(body.getPlayerInventory(), body.getInventoryLeft(), body.getInventoryRight());
        logger.info("Le joueur joue " + action.toString());
        return action;
    }

    @PostMapping(value = "/showWinner", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String showWinner(@RequestBody String winnerName) {
        logger.info("Game finished, winner is : " + winnerName);
        this.playerService.showWinner(winnerName);
        return "OK";
    }

    @PostMapping(value = "/chooseReward", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resources RewardChosen(@RequestBody List<Trade> Trades) {
        logger.info("/chooseReward");
        return this.playerService.chooseReward(Trades);
    }


    @GetMapping("/register")
    public void register() {
        this.clientService.register();
    }

    @GetMapping("/join")
    public void join() {
        this.clientService.join();
    }
}
