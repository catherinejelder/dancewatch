package com.example.celder.data;


import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * assume 40 ms samples
 * @author celder
 *
 */
public class SimpleDanceRecord {

    private int window = 2 * 1000 / 40; // number of recent records we're interested in (2 seconds)

    // TODO: make private
    public Deque<Float> zs = new ArrayDeque<>();
    public Deque<Float> xs = new ArrayDeque<>();
    public Deque<Float> ys = new ArrayDeque<>();

    public Song isSong() {
        for(Song s: Song.values()) {
            if (s.statRange != null) {
                if (s.statRange.matches(this.getStats())) {
                    return s;
                }
            }
        }
        return Song.NONE;
    }

    public Stats getStats() {
        double[] zStats = getStatsForAxis(0);
        double[] xStats = getStatsForAxis(1);
        double[] yStats = getStatsForAxis(2);
        SingleAxisStats zS = new SingleAxisStats(zStats[0], zStats[1], zStats[2]);
        SingleAxisStats xS = new SingleAxisStats(xStats[0], xStats[1], xStats[2]);
        SingleAxisStats yS = new SingleAxisStats(yStats[0], yStats[1], yStats[2]);
        Stats stats = new Stats(zS, xS, yS);
        return stats;
    }

    // TODO: refactor this!
    private double[] getStatsForAxis(int axis) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        Deque<Float> targetArr;
        if (axis == 0){
            targetArr = zs;
        } else if (axis == 1) {
            targetArr = xs;
        } else {
            targetArr = ys;
        }
        for (int i=0; i<targetArr.size(); i++) {
            stats.addValue((new Float((float) targetArr.toArray()[i])).doubleValue());
        }
        double[] statsArr = new double[3];
        double amp = stats.getMax() - stats.getMin();
        double middle = stats.getMin() + (amp / 2);
//		double freq = getTopFrequency(axis); // TODO: use fourier transform
        double numMiddleCrossings = getNumMiddleCrossings(targetArr, middle);
        double pd = 0;
        if (numMiddleCrossings != 0) {
            pd = 2 * (targetArr.size() / numMiddleCrossings);
        }

        statsArr[0] = middle;
        statsArr[1] = amp;
        statsArr[2] = pd;

        return statsArr;
    }

    private int getNumMiddleCrossings(Deque<Float> queue, double middle) {
        Object[] arr = queue.toArray();
        int numCrossings = 0;
        for (int i=0; i<arr.length-1; i++) {
            if (Math.signum((float)arr[i] - middle) != Math.signum((float)arr[i+1] - middle)) {
                numCrossings++;
            }
        }
        return numCrossings;
    }
    // for testing
    public void setQueue(float[] z, float[] x, float[] y) {
        for (int i=0; i<z.length; i++) {
            zs.add(z[i]);
            xs.add(x[i]);
            ys.add(y[i]);
        }
    }

    // add point to queue. if needed, translate point to compensate for "leaps"
    public void translateAndAddPoint(Deque<Float> queue, float pt) {
        // TODO: this could translate points upwards or downwards forever
        if ((!queue.isEmpty()) && (Math.abs(queue.peekLast() - pt) >  0.7 * 2 * Math.PI)) {
            queue.add((float) (Math.signum(queue.peekLast()) * 2 * Math.PI) + pt);
        } else {
            queue.add(pt);
        }
    }

    public void addPoint(float[] pos) {
        // add point to queue
//        zs.add(pos[0]);
//        xs.add(pos[1]);
//        ys.add(pos[2]);
        translateAndAddPoint(zs, pos[0]);
        translateAndAddPoint(xs, pos[1]);
        translateAndAddPoint(ys, pos[2]);

        // trim old points from queue
        trimQueueIfNeeded(zs);
        trimQueueIfNeeded(xs);
        trimQueueIfNeeded(ys);
    }

    public void trimQueueIfNeeded(Deque<Float> q) {
        if (q.size() > window) q.removeFirst();
    }
}
