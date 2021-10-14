package fr.unice.Server.service;

import fr.unice.Server.business.game.PointsCalculator;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final Logger logger = LoggerFactory.getLogger(StatisticsService.class);

    @Autowired
    WebClient webClient;

    // TODO Stocker les stats ailleurs : Si 2 parties sont lancées en même temps il se passe quoi ?
    private final List<List<StatPDO>> statListOfAllGames = new ArrayList<>();

    public void addStat(List<? extends AbstractPlayer> players) {
        List<StatPDO> statList = new ArrayList<>();
        Map<AbstractPlayer, Integer> VP = PointsCalculator.calculVictoryPoints(players);
        for (AbstractPlayer player : players) {
            StatPDO stat = getStat(player);
            stat.setTotalVictoryPoints(VP.get(player));
            statList.add(stat);
        }
        this.statListOfAllGames.add(statList);
    }

    private StatPDO getStat(AbstractPlayer playerWithInventory) {
        StatPDO stat = new StatPDO();
        stat.setName(playerWithInventory.getName());

        Inventory inventory = playerWithInventory.getInventory();
        stat.setCardsPlayed(inventory.getCardsPlayed().stream().map(Card::getName).collect(Collectors.toList()));

        Map<String, Integer> resources = new HashMap<>();
        Set<Map.Entry<Resources, Integer>> entrySet = inventory.getResources().entrySet();
        for (Map.Entry<Resources, Integer> entry : entrySet) {
            resources.put(entry.getKey().name(), entry.getValue());
        }

        stat.setResources(resources);

        return stat;
    }

    public void sendStat(UUID gameId) {
        try {
            webClient.post().uri("/stats/add?gameId=" + gameId.toString()).body(Mono.just(this.statListOfAllGames), List.class).retrieve().bodyToMono(String.class).block();
        } catch (WebClientRequestException e) {
            logger.warn("Echec de connexion au serveur de statistiques. Les statistiques seront supprimées. Reason :" + e.getMessage());
        } finally {
            this.statListOfAllGames.clear();
        }
    }

    public String ping() {
        return "Le serveur de Stats envoie " + webClient.get().uri("/").retrieve().bodyToMono(String.class).block();
    }
}
