package de.meisterfuu.animexx.objects.event;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventRoomProgramObject {

    @SerializedName("raum_id")
    private long roomId;

    @SerializedName("raum_name")
    private String roomName;

    @SerializedName("termine")
    private List<EventProgramEntry> entries;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<EventProgramEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<EventProgramEntry> entries) {
        this.entries = entries;
    }

    public static class EventProgramEntry {

        @SerializedName("id")
        private long id;
        @SerializedName("name")
        private String name;
        @SerializedName("beschreibung")
        private String description;
        @SerializedName("ganztags")
        private String allday;
        @SerializedName("start_utc")
        private String startUtc;
        @SerializedName("end_utc")
        private String endUtc;
        @SerializedName("start_server")
        private String startServer;
        @SerializedName("end_server")
        private String endServer;

        public long getId() {
            return id;
        }

        public void setId(final long pId) {
            id = pId;
        }

        public String getName() {
            return name;
        }

        public void setName(final String pName) {
            name = pName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(final String pDescription) {
            description = pDescription;
        }

        public String getAllday() {
            return allday;
        }

        public void setAllday(final String pAllday) {
            allday = pAllday;
        }

        public String getStartUtc() {
            return startUtc;
        }

        public void setStartUtc(final String pStartUtc) {
            startUtc = pStartUtc;
        }

        public String getEndUtc() {
            return endUtc;
        }

        public void setEndUtc(final String pEndUtc) {
            endUtc = pEndUtc;
        }

        public String getStartServer() {
            return startServer;
        }

        public void setStartServer(final String pStartServer) {
            startServer = pStartServer;
        }

        public String getEndServer() {
            return endServer;
        }

        public void setEndServer(final String pEndServer) {
            endServer = pEndServer;
        }
    }

}
