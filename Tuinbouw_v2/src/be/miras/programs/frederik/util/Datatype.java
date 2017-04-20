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

		// an exponent is 'e' or 'E' followed by an optionally
		// signed decimal integer.
		final String Exp = "[eE][+-]?" + Digits;
		final String fpRegex = ("[\\x00-\\x20]*" + // Optional leading
													// "whitespace"
				"[+-]?(" + // Optional sign character
				"NaN|" + // "NaN" string
				"Infinity|" + // "Infinity" string

				// A decimal floating-point string representing a finite
				// positive
				// number without a leading sign has at most five basic pieces:
				// Digits . Digits ExponentPart FloatTypeSuffix
				//
				// Since this method allows integer-only strings as input
				// in addition to strings of floating-point literals, the
				// two sub-patterns below are simplifications of the grammar
				// productions from the Java Language Specification, 2nd
				// edition, section 3.10.2.

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
			getal = Double.valueOf(tekst); // Will not throw
											// NumberFormatException
		else {
			// Perform suitable alternative action
		}
		return getal;
	}
}
