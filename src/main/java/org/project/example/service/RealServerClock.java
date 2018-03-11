package org.project.example.service;

import java.util.Calendar;

public class RealServerClock implements ServerClock {

	@Override
	public Calendar now() {
		return Calendar.getInstance();
	}

}
