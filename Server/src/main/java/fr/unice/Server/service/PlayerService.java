package fr.unice.Server.service;

import dto.PlayerStatus;
import fr.unice.Server.business.player.Player;
import fr.unice.Server.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Player getPlayerOrThrow(UUID playerId) {
        return this.playerRepository.getPlayer(playerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "This player was not found in our player list.") // 404 ? 500 ? 401 ?
        );
    }

    public UUID createPlayer(String name) {
        return this.playerRepository.createPlayer(name);
    }

    public PlayerStatus getStatus(UUID playerId) {
        Player player = this.getPlayerOrThrow(playerId);
        Optional<UUID> gameId = this.playerRepository.getGameId(playerId);
        return new PlayerStatus(player.getName(), playerId, gameId.isPresent(), gameId.orElse(null));
    }
}
