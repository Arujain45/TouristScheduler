package Objects;

import java.util.Date;

public class DaySchedule {
    private Date openingTime;
    private Date closingTime;

    public DaySchedule(Date openingTime, Date closingTime) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public Date getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Date openingTime) {
        this.openingTime = openingTime;
    }

    public Date getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Date closingTime) {
        this.closingTime = closingTime;
    }
}

