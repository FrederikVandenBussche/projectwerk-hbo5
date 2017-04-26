package be.miras.programs.frederik.util;

import java.util.regex.Pattern;

public class Datatype {

	private static boolean isNumeriek(String tekst) {
		boolean isNumeriek = true;
		char[] array = tekst.toCharArray();
		for (int i = 0; i < array.length; i++) {
			if (!Character.isDigit(array[i])) {
				isNumeriek = false;
			}
		}

		return isNumeriek;
	}

	public static int stringNaarInt(String tekst) {
		int getal = Integer.MIN_VALUE;

		if (tekst == null || tekst.isEmpty()) {
			getal = 0;
		} else if (tekst.length() == 0) {
			getal = 0;
		} else if (isNumeriek(tekst)) {
			getal = Integer.parseInt(tekst);
		}
		return getal;
	}

	public static double stringNaarDouble(String tekst) {
		double getal = Double.MIN_VALUE;
		
		final String Digits = "(\\p{Digit}+)";
		final String HexDigits = "(\\p{XDigit}+)";

		// een exponent is 'e' of 'E' optioneel gevolgd door een + of - 
		// met daarna een integer.
		final String Exp = "[eE][+-]?" + Digits;
		final String fpRegex = ("[\\x00-\\x20]*" + // optioneel beginnent met
													// "whitespace"
				"[+-]?(" + // optioneel + of - karakter
				"NaN|" + // "NaN" string
				"Infinity|" + // "Infinity" string

				// Een decimaal floating-point string is een eindig aantal
				// positieve nummers die niet voorafgaan aan een teken.
				// ze hebben 5 basis stukken: 
				// Digits . Digits ExponentPart FloatTypeSuffix
				//
				
				// Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
				"(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +

				// . Digits ExponentPart_opt FloatTypeSuffix_opt
				"(\\.(" + Digits + ")(" + Exp + ")?)|" +

				// Hexadecimal strings
				"((" +
				// 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
				"(0[xX]" + HexDigits + "(\\.)?)|" +

				// 0[xX] HexDigits_opt . HexDigits BinaryExponent
				// FloatTypeSuffix_opt
				"(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

				")[pP][+-]?" + Digits + "))" + "[fFdD]?))" + "[\\x00-\\x20]*");// Optional
																				// trailing
																				// "whitespace"

		if (Pattern.matches(fpRegex, tekst))
			getal = Double.valueOf(tekst); 
		return getal;
	}
}
