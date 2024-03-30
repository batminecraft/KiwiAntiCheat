package fr.batminecraft.kiwianticheat.report;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Date;

public class ReportPacket {
    private String reporter;
    private String reported;
    private String type;
    private String date;

    public ReportPacket(String reporter, String reported, String type, String date) {
        this.reporter = reporter;
        this.reported = reported;
        this.type = type;
        this.date = date;
    }

    public String getReporter() {
        return reporter;
    }

    public String getReported() {
        return reported;
    }

    public String getType() {
        return type;
    }


    public String getDate() {
        return date;
    }
}
