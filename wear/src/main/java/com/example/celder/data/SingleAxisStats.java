package com.example.celder.data;

public class SingleAxisStats {
    // average value
    double middle;
    // max - min
    double amplitude;
    // number of samples in one cycle
    double period;

    public SingleAxisStats(double middle, double amplitude, double period) {
        this.middle = middle;
        this.amplitude = amplitude;
        this.period = period;
    }

    @Override
    public String toString() {
        return "middle: " + middle + ",\n amplitude: " + amplitude + ",\n period: " + period;
    }
}