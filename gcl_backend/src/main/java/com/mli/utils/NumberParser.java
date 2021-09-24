package com.mli.utils;

import org.springframework.stereotype.Component;

@Component
public class NumberParser {

	/**
	 * This method will convert the number into the format given in format fld and
	 * replace from fld with To. getFormatedNumber(20,"xxxxx",'x','0') will return
	 * 00020
	 * 
	 * @param number
	 *            : number
	 * @param format
	 *            :
	 * @param from
	 * @param to
	 * @return
	 */
	public static String getFormatedNumber(int number, String format, char from, char to) {
		StringBuilder formattedNo = new StringBuilder();
		int noOfCharsToAppend = format.length() - (number + "").length();
		while (noOfCharsToAppend > 0) {
			formattedNo = formattedNo.append(from);
			noOfCharsToAppend--;
		}
		formattedNo = formattedNo.append(number);
		return formattedNo.toString().replace(from, to);
	}

}
