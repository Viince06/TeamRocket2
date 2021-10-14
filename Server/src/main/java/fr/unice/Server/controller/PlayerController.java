package fr.unice.Server.controller;

import dto.PlayerStatus;
import fr.unice.Server.service.PlayerService;
import model.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    @Autowired
    private PlayerService playerService;

    @PostMapping("/register")
    public String register(@RequestBody String name) {
        logger.debug("/register - {}", name);
        return playerService.createPlayer(name).toString();
    }

    @GetMapping("/inventory")
    public Inventory getInventory(@RequestAttribute UUID playerId) {
        logger.debug("/inventory - {}", playerId.toString());
        return this.playerService.getPlayerOrThrow(playerId).getInventory();
    }

    @GetMapping("/status")
    public PlayerStatus getStatus(@RequestAttribute UUID playerId) {
        logger.debug("/status - {}", playerId.toString());
        return this.playerService.getStatus(playerId);
    }
}
