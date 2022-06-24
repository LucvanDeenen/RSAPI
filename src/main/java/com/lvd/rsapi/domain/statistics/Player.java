package com.lvd.rsapi.domain.statistics;

import lombok.Data;

import java.util.Map;

@Data
public class Player {

    private Map<String, Score> scores;

    private Map<String, Stats> stats;

}
