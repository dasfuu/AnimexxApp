package de.meisterfuu.animexx.api;

/**
 * Created by Furuha on 08.09.2015.
 */
public class Events {

    public static class MangaSeriesFetched {

        long seriesId;

        public long getSeriesId() {
            return seriesId;
        }

        public void setSeriesId(long seriesId) {
            this.seriesId = seriesId;
        }
    }

}
