package com.mli.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.mli.constants.Constant;

/**
 * @author Nikhilesh.Tiwari
 * Date util use across the project.
 */
public class DateUtil {

	//public final static String DATE_PATTERN = "dd MMM yyyy HH:mm:ss";

	//public final static DateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

	/**
	 * convert from Current Date to UTC TimeStamp </br>
	 * 
	 * @return TimeStamp
	 */
	public static Long toCurrentUTCTimeStamp() {
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Calendar calendar = Calendar.getInstance(timeZone);
		return calendar.getTime().getTime() / 1000;
	}
	
	public static Long toCurrentGMTTimeStamp() {
		TimeZone timeZone = TimeZone.getTimeZone("GMT");
		Calendar calendar = Calendar.getInstance(timeZone);
		return calendar.getTime().getTime() / 1000;
	}

	/**
	 * convert from Current Date to UTC MilliSecond </br>
	 * 
	 * @return TimeStamp
	 */
	public static Long toMilliSecond() {
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Calendar calendar = Calendar.getInstance(timeZone);
		return calendar.getTime().getTime();
	}

	public static String extractDateAsString(Long timestamp) {
		Date date = new Date(timestamp * 1000);
		return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}

	public static String extractDateAsStringDashFormate(Long timestamp) {
		Date date = new Date(timestamp * 1000);
		return new SimpleDateFormat("dd-MM-yyyy").format(date);
	}
	
	public static String dateFormat(Long timestamp) {
		Date date = new Date(timestamp * 1000);
		return new SimpleDateFormat(Constant.DATE_FORMAT).format(date);
	}
	
	public static String extractDateAsStringSlashFormate(Long timestamp) {
		Date date = new Date(timestamp * 1000);
		return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}
	
	public static String extractDateWithTSAsStringSlashFormate(Long timestamp) {
		Date date = new Date(timestamp * 1000);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("IST"));
		return format.format(date);
	}
	
	public static Long dateFormater(String inputeDate) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		try {
			date = df.parse(inputeDate);
		} catch (ParseException e) {
			return null;
		}
		return date.getTime() / 1000;
	}

	public static Long dateSeconds(Long inputeDate) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dateString = df.format(inputeDate);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date;
		try {
			date = df.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
		return date.getTime() / 1000;
	}

	public static String dateInFormatddmmyyyy(Long inputeDate) {
		Date date = new Date(inputeDate * 1000);
		SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
		String dateText = df2.format(date);
		return dateText;
	}

	public static String todayDateddmmyyyy(Long timestamp,String formate) {
		Date date = new Date(timestamp * 1000);
		return new SimpleDateFormat(formate).format(date);
	}

	public static int getDiffInYear(Long date) {
		return (int) Math.ceil(DateUtil.monthsBetween(date, DateUtil.toCurrentUTCTimeStamp()) / 12.0);
	}

	public static int monthsBetween(final Long s1, final Long s2) {
		final Calendar d1 = Calendar.getInstance();
		d1.setTimeInMillis(s1 * 1000);
		final Calendar d2 = Calendar.getInstance();
		d2.setTimeInMillis(s2 * 1000);
		int diff = (d2.get(Calendar.YEAR) - d1.get(Calendar.YEAR)) * 12 + d2.get(Calendar.MONTH)
				- d1.get(Calendar.MONTH);
		return diff;
	}
	
	public static int ageValidation18Year(String dob) throws ParseException{
		 return Period.between(LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd/MM/yyyy").parse(dob))), LocalDate.now()).getYears();
	}
	
	public static String todayDateDDMMYYYY() {
		return new SimpleDateFormat("ddMMyyyy").format( new Date());
	}

	public static String getYesturdayDateAsStringDashFormate() {
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Calendar calendar = Calendar.getInstance(timeZone);
		calendar.add(Calendar.DATE, -1);
		Date date = new Date(calendar.getTime().getTime());
		return new SimpleDateFormat("dd-MM-yyyy").format(date);
	}
	
	public static Long dateWithStartTime(Date date) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		int month = date.getMonth()+1;
		int day = date.getDate();
		int year = date.getYear()+1900;
		Date today = formatter.parse("" + year + "-" + month + "-" + day + "T00:00:00.000+0000");
		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		Calendar calendar = Calendar.getInstance(timeZone);
		calendar.setTime(today);
		return calendar.getTime().getTime() / 1000;
	}
	
	public static boolean validateDateFormate(String date, String format) {
		boolean isValid = false;
		try {
			// validate format
			SimpleDateFormat sdfrmt = new SimpleDateFormat(format);
			sdfrmt.parse(date);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDateStringToDate(date, format)); // Now use today date.
			cal.add(Calendar.DATE, 8);
			Date up = cal.getTime();
			
			cal = Calendar.getInstance();
			cal.setTime(getStartTSDate(cal.getTime()));
			Date to = cal.getTime();
			
			if (to.equals(up)) {
				isValid = true;
			} else if (up.after(to)) {
				isValid = true;
			} else {
				isValid = false;
			}
		} catch (Exception exception) {
			isValid = false;
			exception.printStackTrace();
		}
		return isValid;
	}
	
	public static Long getDateStringToLong(String date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		long milliseconds = 0l;
		try {
			Date d = f.parse(date);
			milliseconds = d.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return milliseconds/1000;
	}
	
	public static Date getDateStringToDate(String date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		try {
			Date d = f.parse(date);
			d.setHours(0);
			d.setMinutes(0);
			d.setSeconds(0);
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Long addDaysToCurrentTS(int day) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, day);
		return now.getTimeInMillis() / 1000;
	}
	
	public static Long addDaysWithUTCLastTS(int day) {
		Calendar now = Calendar.getInstance();
		now.setTimeZone(TimeZone.getTimeZone("UTC"));
		now.add(Calendar.DATE, day);
		now.set(Calendar.HOUR_OF_DAY,23);
		now.set(Calendar.MINUTE,59);
		now.set(Calendar.SECOND,59);
		return  now.getTimeInMillis()/1000;
	}
	
	public static Date getStartTSDate(Date date) {
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date;
	}
	
	public static Long dateFormaterInIST(String inputeDate,String format) {
		DateFormat df = new SimpleDateFormat(format);
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = new Date();
		try {
			date = df.parse(inputeDate);
		} catch (ParseException e) {
			return null;
		}
		return date.getTime() / 1000;
	}
	
	public static List<Long> getAllDatesInLong(Long fromDate, Long toDate) {
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(fromDate * 1000);
		Date from = now.getTime();
		now.setTimeInMillis(toDate * 1000);
		Date to = now.getTime();
		List<Long> dates = new ArrayList<>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(from);
		while (calendar.getTime().before(to)) {
			dates.add(calendar.getTimeInMillis() / 1000);
			calendar.add(Calendar.DATE, 1);
		}
		return dates;
	}
	
	public static String extractDateWithTSAsAM_PMStringSlashFormate(Long timestamp) {
		Date date = new Date(timestamp * 1000);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		format.setTimeZone(TimeZone.getTimeZone("IST"));
		return format.format(date);
	}
	
	/*public static void main(String[] args) {
		Long from = DateUtil.dateFormaterInIST("30/10/2018" + " " + Constant.START_TS,
				Constant.DATE_WITH_TS_FORMAT);
		Long to = DateUtil.dateFormaterInIST("31/10/2018" + " " + Constant.END_TS, Constant.DATE_WITH_TS_FORMAT);

		List<Long> dates = DateUtil.getAllDatesInLong(from, to);
		dates.stream().forEach(System.out::println);
		System.out.println(DateUtil.extractDateAsStringDashFormate(dates.get(0)));
		
		System.out.println(MliCryptoUtil.generateSecureToken(String.valueOf(new Long(11)),"+v+oL+iHrLbB4J9kmp2PZw"));
	}*/
	
	/**
	 * 
	 * @param date
	 * @return
	 * @author rajkumar
	 */
	public static Date getEndTSDate(Date date) {
		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
		return date;
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 * @author rajkumar
	 */
	public static HashMap<String, Long> getDiffHourMinSecond(final Long date) {
		HashMap<String, Long> map = new HashMap<String, Long>();
		final Calendar prev = Calendar.getInstance();
		prev.setTimeInMillis(date * 1000);
		final Calendar now = Calendar.getInstance();
		long duration = now.getTime().getTime() - prev.getTime().getTime();
		long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
		long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
		long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
		map.put("hour",diffInHours);
		map.put("minute",diffInMinutes);
		map.put("second",diffInSeconds%60);
		return map;
	}

}
