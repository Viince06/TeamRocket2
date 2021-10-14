package fr.unice.Player.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import dto.ActionObject;
import dto.EventId;
import fr.unice.Player.business.strategy.EasyStrategy;
import fr.unice.Player.business.strategy.IStrategy;
import model.PlayerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class ClientService {

    private static final int MAX_RETRY = 3;
    private static final String NAME = new Faker().name().firstName();
    private UUID playerId;
    private UUID gameId;

    Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private WebClient webClient;

    private IStrategy playerStrategy = new EasyStrategy();

    public void register() {
        String body = webClient.post().uri("/player/register").body(BodyInserters.fromValue(NAME)).exchangeToMono(clientResponse -> {
            if (clientResponse.statusCode() == HttpStatus.OK) {
                logger.info("Registred to the server.");
                return clientResponse.bodyToMono(String.class);
            } else {
                logger.error(String.format("Failed to register to the server : status  %s", clientResponse.statusCode()));
                return null;
            }
        }).block();

        if (body == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to register to the server");
        }

        this.playerId = UUID.fromString(body);
    }

    public void join() {
        if (playerId == null) {
            throw new IllegalStateException("Player must be registered before join a game.");
        }
        ParameterizedTypeReference<ServerSentEvent<String>> type = new ParameterizedTypeReference<>() {
        };

        Flux<ServerSentEvent<String>> eventStream = webClient.get().uri("/game/join").header("x-player-id", this.playerId.toString()).retrieve().bodyToFlux(type);

        eventStream.subscribe(content -> {
                    logger.info("Time: {} - event: name[{}], id [{}], content[{}] ", LocalTime.now(), content.event(), content.id(), content.data());

                    switch (Objects.requireNonNull(EventId.toEventId(content.event()))) {
                        case JOIN:
                            this.gameId = UUID.fromString(Objects.requireNonNull(content.data()));
                            logger.info("Player has joined game {}", content.data());
                            break;

                        case PLAY:
                            try {
                                ObjectMapper mapper = new ObjectMapper();
                                ActionObject actionObject = mapper.readValue(content.data(), ActionObject.class);
                                play(actionObject, 1);

                            } catch (JsonProcessingException e) {
                                logger.error("join() - PLAY event - Failed to read ActionObject");
                            }
                            break;

                        case WINNER:
                            logger.info("Game finished ! The winner is {}", content.data());
                            break;
                    }
                },
                error -> {
                    logger.error("Connection error: ", error);
                },
                () -> {
                    logger.info("Connection completed.");
                }
        );
    }

    private void play(ActionObject actionObject, int retry) {
        PlayerAction playerAction = playerStrategy.chooseAction(actionObject.getPlayerInventory(), actionObject.getInventoryLeft(), actionObject.getInventoryRight());
        webClient.post()
                .uri(String.format("/game/%s/play", gameId.toString()))
                .body(BodyInserters.fromValue(playerAction))
                .header("x-player-id", this.playerId.toString())
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.OK) {
                        logger.info("Action accepted : {}", playerAction);
                        return clientResponse.bodyToMono(String.class);
                    } else if (clientResponse.statusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                        logger.error("Action refused : {} (try :{})", clientResponse.statusCode(), retry);
                        if (retry < MAX_RETRY) {
                            this.play(actionObject, retry + 1);
                        }
                    } else {
                        logger.error("play() - An error has occurred (status: {})", clientResponse.statusCode());
                        if (retry < MAX_RETRY) {
                            CompletableFuture.delayedExecutor(10L * retry, TimeUnit.SECONDS).execute(() -> this.play(actionObject, retry + 1));
                        }
                    }
                    return Mono.just("KO");
                }).subscribe();
    }
}
