package model;

import java.util.Date;

public class Description {
    private String description;
    private Date estimatedDate; // ca sa primesti mesaje, legate de ce goaluri ai neindeplinite si cand li se aproprie perioada

    public Description(String description, Date estimatedDate) {
        this.description = description;
        this.estimatedDate = estimatedDate;
    }

    public String getDescription() {
        return description;
    }

    public Date getEstimatedDate() {
        return estimatedDate;
    }
}
