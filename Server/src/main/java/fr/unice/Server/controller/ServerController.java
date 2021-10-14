package fr.unice.Server.controller;

import fr.unice.Server.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ServerController {
    @Autowired
    StatisticsService statisticsService;

    @GetMapping("/")
    public String hello(HttpServletRequest request) {
        return "Welcome to 7Wonders. Please join here a game here : " + request.getRequestURL().toString() + "join.";
    }

    // Temporaire : pour prouver la communication entre le serveur de jeu et le serveur de Stats.
    @GetMapping("/ping-stat")
    public String pingStats() {
        return statisticsService.ping();
    }

}
