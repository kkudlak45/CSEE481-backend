package com.backend.server.utils;

import java.sql.Date;
import java.time.LocalDate;

public class GeneralUtils {

	public static LocalDate toLocalDate(Date date) {
		if (date == null) {
			return null;
		}
		
		return date.toLocalDate();
	}
	
}
