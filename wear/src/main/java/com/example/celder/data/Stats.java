package com.example.celder.data;


public class Stats {
    SingleAxisStats zStats;
    SingleAxisStats xStats;
    SingleAxisStats yStats;

    public Stats(SingleAxisStats zStats, SingleAxisStats xStats, SingleAxisStats yStats) {
        this.zStats = zStats;
        this.xStats = xStats;
        this.yStats = yStats;
    }

    @Override
    public String toString() {
        return "zStats: " + zStats + ",\n xStats: " + xStats + ",\n yStats: " + yStats;
    }
}
