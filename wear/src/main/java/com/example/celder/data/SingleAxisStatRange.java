package com.example.celder.data;


public class SingleAxisStatRange {
    // average value
    double minMiddle;
    double maxMiddle;
    // max - min
    double minAmplitude;
    double maxAmplitude;
    // number of samples in one cycle
    double minPeriod;
    double maxPeriod;

    public SingleAxisStatRange(double minMiddle, double maxMiddle,
                               double minAmplitude, double maxAmplitude,
                               double minPeriod, double maxPeriod) {
        this.minMiddle = minMiddle;
        this.maxMiddle = maxMiddle;
        this.minAmplitude = minAmplitude;
        this.maxAmplitude = maxAmplitude;
        this.minPeriod = minPeriod;
        this.maxPeriod = maxPeriod;
    }

    public boolean matches(SingleAxisStats s) {
        if (! (this.minMiddle < s.middle && this.maxMiddle > s.middle)) {
            return false;
        }
        if (! (this.minAmplitude < s.amplitude && this.maxAmplitude > s.amplitude)) {
            return false;
        }
        if (! (this.minPeriod < s.period && this.maxPeriod > s.period)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "minMiddle: " + minMiddle + "maxMiddle: " + maxMiddle
                + ",\n minAmplitude: " + minAmplitude + " maxAmplitude: " + maxAmplitude
                + ",\n minPeriod: " + minPeriod + " maxPeriod: " + maxPeriod;
    }
}
