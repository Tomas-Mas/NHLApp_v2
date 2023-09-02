package com.tom.nhl.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class LogUtil {

	public static void writeLog(String message) {
		try {
			FileWriter errLog = new FileWriter("alert.txt", true);
			errLog.write(new Date().toString() + " - " + message + "\n\n");
			errLog.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
