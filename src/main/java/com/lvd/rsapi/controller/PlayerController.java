package com.lvd.rsapi.controller;

import com.lvd.rsapi.domain.statistics.Player;
import com.lvd.rsapi.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(PlayerController.url)
public class PlayerController {

    public static final String url = "api/stats";

    private final PlayerService playerService;

    private final RestTemplate restTemplate;

    @Autowired
    public PlayerController(PlayerService playerService, RestTemplate restTemplate) {
        this.playerService = playerService;
        this.restTemplate = restTemplate;
    }

    @Value("${uri}")
    private String uri;

    @GetMapping("{name}")
    public ResponseEntity<Player> getPlayer(@PathVariable String name) {

        // Error handling here due to the fact
        // that the returned value can give a 404 error
        try {
            final var result = restTemplate.getForObject(uri + name, String.class);

            if (result != null) {
                final var player = playerService.formatResult(result);
                return new ResponseEntity<>(player, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
