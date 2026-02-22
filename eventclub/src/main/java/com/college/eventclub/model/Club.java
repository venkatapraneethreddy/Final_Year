package com.college.eventclub.model;

public class Club {
    private Long clubId;
    private String clubName;
    private String description;
    private String status;

    public Club(Long clubId, String clubName, String description, String status) {
        this.clubId = clubId;
        this.clubName = clubName;
        this.description = description;
        this.status = status;
    }

    public Long getClubId() { return clubId; }
    public String getClubName() { return clubName; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
