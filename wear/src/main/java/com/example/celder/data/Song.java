package com.example.celder.data;

/**
 * Created by celder on 1/24/16.
 */
public enum Song {
    GANGNAM_STYLE("spotify:track:03UrZgTINDqvnUMbbIMhql", new StatRange(new SingleAxisStatRange(-2.50, 2.80, 0.39, 0.770, 5.9, 32.5), new SingleAxisStatRange(0.5, 0.92, 0.25, 0.71, 5.9, 13), new SingleAxisStatRange(-0.82, -0.368, 0.38, 0.71, 5, 14.1))),
    SINGLE_LADIES("spotify:track:5R9a4t5t5O0IsznsrKPVro", new StatRange(new SingleAxisStatRange(-2.0, 1.4, 1.3, 1.9, 7.0, 14.1), new SingleAxisStatRange(0.0, 0.34, 0.10, 0.44, 3.9, 32.5), new SingleAxisStatRange(-1.6, -1.4, 0.1, 0.62, 7.0, 14.1))),
    NONE(null, null);

    public final String uri;
    public final StatRange statRange;

    private Song(String uri, StatRange sRange) {
        this.uri = uri;
        this.statRange = sRange;
    }
}
