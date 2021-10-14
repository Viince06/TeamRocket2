package fr.unice.Stats;

import model.StatPDO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    private StatService statService;

    private static final Log log = LogFactory.getLog(Controller.class);

    @GetMapping("/")
    public String ping() {
        return "Hello World !";
    }

    @PostMapping(value = "/stats/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addStatistics(@RequestParam String gameId, @RequestBody List<List<StatPDO>> stats) {
        log.info("Réception de statistiques.");
        try {
            this.statService.createStatCsv(gameId, stats);
            log.info("Statistiques enregistrées.");
            return new ResponseEntity<>("OK", HttpStatus.valueOf(200));
        } catch (IOException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("KO", HttpStatus.valueOf(500));
        }
    }
}
