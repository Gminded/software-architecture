package nl.uva.sa.ft1;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogEntry {
	private Date date;
	private Integer level;
	private String type;
	private Integer value;
	
	public LogEntry(String entry) {
		String[] items = entry.split(";");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
		try {
			date = dateFormat.parse(items[0]);
		} catch (ParseException e) {
			System.out.println("Error parsing date");
			date = new Date();
		}
		String [] levels = {"error", "warning", "info", "verbose"};
		for (int i = 0; i < levels.length; i++) {
			if (levels[i].equals(items[1])) {
				level = i;
			}
		}
		type = items[2];
		value = Integer.parseInt(items[3]);
	}

	public Date getDate() {
		return date;
	}
	
	public Integer getLevel() {
		return level;
	}
	
	public String getType() {
		return type;
	}
	
	public Integer getValue() {
		return value;
	}

}
