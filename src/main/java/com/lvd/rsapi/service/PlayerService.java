package com.lvd.rsapi.service;

import com.lvd.rsapi.domain.statistics.*;
import com.lvd.rsapi.domain.statistics.ScoreName;
import com.lvd.rsapi.domain.statistics.StatsName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    @Transactional
    public Player formatResult(String res) {
        if (res == null) {
            return null;
        }

        final var lines = List.of(res.split("\\r?\\n"));

        return setPlayer(setStatistics(lines), setScores(lines));
    }

    private Player setPlayer(Map<String, Stats> statsMap, Map<String, Score> scoreMap) {
        Player player = new Player();

        player.setStats(statsMap);
        player.setScores(scoreMap);

        return player;
    }

    private HashMap<String, Stats> setStatistics(List<String> data) {

        final var statistics = data.stream()
                .map(line -> line.split(","))
                .filter(line -> line.length > 2)
                .collect(Collectors.toList());

        HashMap<String, Stats> statsHashMap = new HashMap<>();

        for (int index = 0; index < statistics.size(); index++) {
            final var value = statistics.get(index);

            Stats stats = new Stats();

            stats.setRank(Integer.parseInt(value[0]));
            stats.setLevel(Integer.parseInt(value[1]));
            stats.setExperience(Integer.parseInt(value[2]));

            statsHashMap.put(StatsName.values()[index].toString(), stats);
        }

        return statsHashMap;
    }

    private HashMap<String, Score> setScores(List<String> data) {

        final var scores = data.stream()
                .map(line -> line.split(","))
                .filter(line -> line.length == 2)
                .collect(Collectors.toList());

        HashMap<String, Score> scoresHashMap = new HashMap<>();

        for (int index = 0; index < scores.size(); index++) {
            final var value = scores.get(index);
            Score score = new Score();

            score.setRank(Integer.parseInt(value[0]));
            score.setScore(Integer.parseInt(value[1]));

            scoresHashMap.put(ScoreName.values()[index].toString(), score);
        }

        return scoresHashMap;
    }

}
