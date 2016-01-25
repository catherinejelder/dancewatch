package com.example.celder.data;

public class StatRange {
    SingleAxisStatRange zRange;
    SingleAxisStatRange xRange;
    SingleAxisStatRange yRange;

    public StatRange(SingleAxisStatRange zRange, SingleAxisStatRange xRange, SingleAxisStatRange yRange) {
        this.zRange = zRange;
        this.xRange = xRange;
        this.yRange = yRange;
    }

    public boolean matches(Stats s) {
        return (zRange.matches(s.zStats) && xRange.matches(s.xStats) && yRange.matches(s.yStats));
    }
}

