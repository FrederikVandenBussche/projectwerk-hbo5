/**
 * 
 */
package be.miras.programs.frederik.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Frederik Vanden Bussche
 * 
 *Util class voor inputvalidatie
 *         Er wordt een message gegenereert indien: 
 *         - een tekst leeg is of uit nummers bestaat 
 *         - een int niet enkel uit cijfers bestaat 
 *         - een double niet uit een geldig kommagetal bestaat 
 *         - een datum niet van het type dd/mm/yyyy of YYYY/mm/dd is .
 *
 */
public class InputValidatie {
	
	
	public InputValidatie(){
	}
	
 	/**
 	 * @param tekst String
 	 * @return String message indien de string niet ingevuld is
 	 */
 	public static String ingevuld(String tekst){
		String msg = null;
		if (tekst == null || tekst.trim().isEmpty() || tekst.trim().length() == 0){
			msg = " is niet ingevuld.";
		}
		return msg;
	}
	
	/**
	 * @param tekst String
	 * @return String indien de tekst niet is ingevuld of niet enkel bestaat uit
	 * letters of de tekens '-_. of spatie
	 */
	public static String enkelAlfabetisch(String tekst) {
		String msg = null;
		if (tekst == null || tekst.trim().isEmpty() || tekst.trim().length() == 0) {
			msg = " is niet ingevuld.";
		} else {
			char[] array = tekst.trim().toCharArray();
			for (int i = 0; i < array.length; i++) {
				if (!Character.isAlphabetic(array[i])) {
					if (String.valueOf(array[i]).equals("'") ||
							String.valueOf(array[i]).equals("-") ||
							String.valueOf(array[i]).equals("_") ||
							String.valueOf(array[i]).equals(".") ||
							String.valueOf(array[i]).equals(" ") ){
						
					} else {
						msg = " bestaat niet enkel uit letters.";
					}
				}
			}
		}
		return msg;
	}
	
	/** 
	 * @param getal String
	 * @return String indien het getal niet is ingevuld, of niet enkel uit cijfers bestaat.
	 */
	public static String geheelGetal(String getal) {
		String msg = null;
		if (getal == null || getal.trim().isEmpty() || getal.trim().length() == 0) {
			msg = " is niet ingevuld.";
		} else {
			char[] array = getal.toCharArray();
			for (int i = 0; i < array.length; i++) {
				if (!Character.isDigit(array[i])) {
					msg = " bestaat niet enkel uit cijfers.";
				}
			}
		}
		return msg;
	}

	/**
	 * @param getal String
	 * @return String indien het getal niet geparst kan worden naar een double.
	 */
	public static String kommagetal(String getal) {
		String msg = null;

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
		
		if (!Pattern.matches(fpRegex, getal)) {
			char[] array = getal.toCharArray();
			for (int i = 0; i < array.length; i++) {
				if (!Character.isDigit(array[i])) {
					msg = " is geen (komma-)getal.";
				}
			}
		}
		
		return msg;
	}

	/**
	 * @param datum String
	 * @return String indien de string niet van het type dd/mm/YYYY of YYY/mm/dd is
	 */
	public static String correcteDatum(String datum) {
		String msg = null;
		Date date = new Date();
		if (datum == null || datum.trim().isEmpty() || datum.trim().length() == 0) {
			msg = " is niet ingevuld.";
		} else {
			// datum kan van het type dd/mm/yyyy of van het type yyyy/mm/dd
			String errorMsg = " is niet correct ingevuld. Gelieve een datum van het type dd/mm/yyyy in te geven.";

			if (datum.length() == 10 && datum.substring(2, 3).equals("/") && datum.substring(5, 6).equals("/")) {
				// controle of de datum van het type dd/mm/yyyy is
				char[] array = datum.toCharArray();
				if (!Character.isDigit(array[0]) || !Character.isDigit(array[1]) || !Character.isDigit(array[3])
						|| !Character.isDigit(array[4]) || !Character.isDigit(array[6]) || !Character.isDigit(array[7])
						|| !Character.isDigit(array[8]) || !Character.isDigit(array[9])) {
					msg = errorMsg;
				}

				if (Datatype.stringNaarInt(datum.substring(0, 2)) > 31 
						// meer dan 31dagen
						|| Datatype.stringNaarInt(datum.substring(3, 5)) > 12 
						// meer dan 12 maanden
						|| Datatype.stringNaarInt(datum.substring(6, 10)) < 1900 
						// vroeger dan 1900
						|| Datatype.stringNaarInt(datum.substring(6, 10)) > (date.getYear() + 2000)) {
						// later dan nu binnen 100 jaar
					msg = errorMsg;
				}
			} else if ((datum.length() == 10 &&
					datum.subSequence(4, 5).equals("/") && 
					datum.substring(7, 8).equals("/") ) ||
					(datum.length() == 10 && 
							datum.subSequence(4, 5).equals("-") &&
							datum.substring(7, 8).equals("-"))) {
				// controle of de datum van het type yyyy/mm/dd is
				char[] array = datum.toCharArray();
				if (!Character.isDigit(array[0]) || !Character.isDigit(array[1]) || !Character.isDigit(array[2])
						|| !Character.isDigit(array[3]) || !Character.isDigit(array[5]) || !Character.isDigit(array[6])
						|| !Character.isDigit(array[8]) || !Character.isDigit(array[9])) {
					msg = errorMsg;
				}
				
				if (Datatype.stringNaarInt(datum.substring(8, 10)) > 31 
						// meer dan 31 dagen
						|| Datatype.stringNaarInt(datum.substring(5, 7)) > 12 
						// meer dan 12 maanden
						|| Datatype.stringNaarInt(datum.substring(0, 4)) < 1900 
						// vroeger dan 1900
						|| Datatype.stringNaarInt(datum.substring(0, 4)) > (date.getYear() + 2000) 
						// later dan nu binnen 100 jaar
				) {
					msg = errorMsg;
				}
			} else {
				msg = errorMsg;
			}
		}
		return msg;
	}

	/**
	 * @param email String
	 * @return String indien email niet geen geldig email adres is
	 */
	public static String correctEmailadres(String email) {
		String msg = null;

		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*" //voor de @
				+ "@" 																// de @
				+ "(?:[a-zA-Z0-9-]+\\.)"		//cijfer en letters gevolgd door een punt
				+ "+[a-zA-Z]{2,6}$";			// tussen de 2 en de 6 letters
		 
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		
		if(!matcher.matches()){
			msg = " is geen correct email-adres";
		}
		
		
		return msg;
	}

	/**
	 * @param btwNr String
	 * @return indien het btwNr geen geldig btw nummer is.
	 */
	public static String geldigBtwNummer(String btwNr) {
		String msg = null;
		
		if(btwNr.trim().length() != 12 
				|| enkelAlfabetisch(btwNr.trim().substring(0, 2)) != null
				|| geheelGetal(btwNr.trim().substring(2, 12)) != null){
			msg = " is geen belgisch BTW nummer (vb: BE0123456789";
		}
		return msg;
	}

	
}
