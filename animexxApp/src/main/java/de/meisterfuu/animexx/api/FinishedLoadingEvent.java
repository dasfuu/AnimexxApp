package de.meisterfuu.animexx.api;

/**
 * Created by Furuha on 06.12.2014.
 */
public class FinishedLoadingEvent {

    private int parentId;

    public FinishedLoadingEvent(int parentId) {
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
