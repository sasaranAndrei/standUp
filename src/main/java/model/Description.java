package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    public String getStringDate() {
        String result = "";
        if (estimatedDate != null){
            DateFormat dateFormat = new SimpleDateFormat(Excel.DATE_FORMAT);
            result = dateFormat.format(estimatedDate);
        }
        return result;
    }
}
