package zquirrel.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VarUtil {

	public static boolean isEmpty(String string) {
		return string == null || string.isEmpty(); 
	}
	
	public static String formatDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
}
