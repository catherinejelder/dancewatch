package com.example.celder.data;

/**
 * Created by celder on 1/24/16.
 */
public enum Song {
    // short term TODO: tweak params
    // long term TODO: machine learning
//    SINGLE_LADIES("spotify:track:5R9a4t5t5O0IsznsrKPVro", new StatRange(new SingleAxisStatRange(-2 * Math.PI, 2 * Math.PI, 1.3, 2.5, 7.0, 16.7), new SingleAxisStatRange(-0.05, 0.5, 0.10, 0.44, 5.0, 16.7), new SingleAxisStatRange(-1.8, -1.2, 0.1, 0.9, 5.0, 16.7))),
    SINGLE_LADIES("spotify:track:5R9a4t5t5O0IsznsrKPVro", new StatRange(new SingleAxisStatRange(-5 * Math.PI, 5 * Math.PI, 1.3, 2.5, 7.0, 16.7), new SingleAxisStatRange(-5 * Math.PI, 5 * Math.PI, 0.10, 0.44, 5.0, 16.7), new SingleAxisStatRange(-5 * Math.PI, 5 * Math.PI, 0.1, 0.9, 5.0, 16.7))),
//    CALL_YOUR_GIRLFRIEND("spotify:track:2sCoROOlNQyFpRQEe6A5lv", new StatRange(new SingleAxisStatRange(- Math.PI, Math.PI, 0.5, 1.7, 19.9, 28.0), new SingleAxisStatRange(-0.05, 0.5, 0.35, 0.9, 11.0, 22.0), new SingleAxisStatRange(-0.8, -0.2, 0.5, 1.2, 19.9, 28.0))),
//    CALL_YOUR_GIRLFRIEND("spotify:track:2sCoROOlNQyFpRQEe6A5lv", new StatRange(new SingleAxisStatRange(-2 * Math.PI, 2 * Math.PI, 0.5, 2.1, 19.9, 28.0), new SingleAxisStatRange(-0.05, 0.5, 0.35, 1.0, 19.9, 28.0), new SingleAxisStatRange(-1.0, -0.2, 0.5, 1.3, 19.9, 28.0))),
    CALL_YOUR_GIRLFRIEND("spotify:track:2sCoROOlNQyFpRQEe6A5lv", new StatRange(new SingleAxisStatRange(-5 * Math.PI, 5 * Math.PI, 0.5, 2.1, 19.9, 28.0), new SingleAxisStatRange(-5 * Math.PI, 5 * Math.PI, 0.35, 1.0, 19.9, 28.0), new SingleAxisStatRange(-5 * Math.PI, 5 * Math.PI, 0.5, 1.3, 19.9, 28.0))),
    //    GANGNAM_STYLE("spotify:track:03UrZgTINDqvnUMbbIMhql", new StatRange(new SingleAxisStatRange(-2.50, 2.80, 0.39, 0.770, 5.9, 32.5), new SingleAxisStatRange(0.5, 0.92, 0.25, 0.71, 5.9, 13), new SingleAxisStatRange(-0.82, -0.368, 0.38, 0.71, 5, 14.1))),
    GANGNAM_STYLE("spotify:track:03UrZgTINDqvnUMbbIMhql", new StatRange(new SingleAxisStatRange(-5 * Math.PI, 5 * Math.PI, 0.39, 0.770, 22.0, 26.0), new SingleAxisStatRange(-5 * Math.PI, 5 * Math.PI, 0.25, 0.71, 9.0, 26.0), new SingleAxisStatRange(-5 * Math.PI, 5 * Math.PI, 0.38, 0.71, 9.0, 26.0))),
    NONE(null, null);

    public final String uri;
    public final StatRange statRange;

    private Song(String uri, StatRange sRange) {
        this.uri = uri;
        this.statRange = sRange;
    }
}
