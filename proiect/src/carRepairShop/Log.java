package carRepairShop;

import java.time.LocalTime;

public class Log {
    private int personId;
    private LocalTime logTimeDate;

    public Log(int personId) {
        this.personId = personId;
        this.logTimeDate = LocalTime.now();
    }

    public int getPersonId() {
        return personId;
    }

    public LocalTime getLogTimeDate() {
        return logTimeDate;
    }
}
