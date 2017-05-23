package be.miras.programs.frederik.util;

import java.util.Date;

/**
 * @author Frederik Vanden Bussche
 * 
 * util class betreffende datums
 *
 */
public class Datum {
	
	
	public Datum(){
	}

	/**
	 * @param maandString String
	 * @return int
	 * 
	 * converteert de naam van een maand in een int (0 - 11)
	 */
	public static int converteerMaand(String maandString) {
		int maand = 0;
		switch (maandString) {
		case "Jan":
			maand = 0;
			break;
		case "Feb":
			maand = 1;
			break;
		case "Mar":
			maand = 2;
			break;
		case "Apr":
			maand = 3;
			break;
		case "May":
			maand = 4;
			break;
		case "Jun":
			maand = 5;
			break;
		case "Jul":
			maand = 6;
			break;
		case "Aug":
			maand = 7;
			break;
		case "Sep":
			maand = 8;
			break;
		case "okt":
			maand = 9;
			break;
		case "Nov":
			maand = 10;
			break;
		case "Dec":
			maand = 11;
			break;
		default:

			break;
		}
		return maand;
	}

	/**
	 * @param datumString String
	 * @return Date
	 * 
	 * creëert een Date vanuit een string
	 */
	public static Date creeerDatum(String datumString) {
		if (datumString != null && !datumString.isEmpty()) {
			int jaar = 0;
			int maand = 0;
			int dag = 0;
			datumString = datumString.trim();
			if (datumString.length() == 10){
				// type dd/mm/yyyy of type yyyy/mm/dd
				if (datumString.substring(2, 3).equals("/") && 
						datumString.substring(5, 6).equals("/") && 
						datumString.length() == 10) {
					// Er is reeds gevalideerd dat de datum van het type dd/mm/yyyy is
					
					dag = Datatype.stringNaarInt(datumString.substring(0, 2));
					maand = Datatype.stringNaarInt(datumString.substring(3, 5)) - 1;
					jaar = Datatype.stringNaarInt(datumString.substring(6, 10));
					jaar = jaar - 1900;
				//einde dd/mm/yyyy	
				} else if ((datumString.subSequence(4, 5).equals("/") && 
						datumString.substring(7, 8).equals("/")
						&& datumString.length() == 10) ||
						(datumString.subSequence(4, 5).equals("-") &&
								datumString.substring(7, 8).equals("-")
								&& datumString.length() == 10)) {
					// Er is reeds gevalideerd dat de datum van het type yyyy/mm/dd is
					dag = Datatype.stringNaarInt(datumString.substring(8, 10));
					maand = Datatype.stringNaarInt(datumString.substring(5, 7)) - 1;
					jaar = Datatype.stringNaarInt(datumString.substring(0, 4));
					jaar = jaar - 1900;
				}
						
			} else if (datumString.length() == 29) {
				jaar = Datatype.stringNaarInt(datumString.substring(24, 28)) - 1900; // YYYY
				String maandString = datumString.substring(4, 7);
				maand = Datum.converteerMaand(maandString);
				dag = Datatype.stringNaarInt(datumString.substring(8, 10));// tussen 1 en 31
			}
			Date datum = new Date(jaar, maand, dag);
			return datum;
		} else {
			return null;
		}
	}

	/**
	 * @param datum Date
	 * @return String
	 * 
	 * genereer een String van het type dd/mm/YYYY
	 */
	public static String datumToString(Date datum) {
		int dag = datum.getDate();
		int maand = datum.getMonth() + 1;
		int jaar = datum.getYear() + 1900;
		String dagString = String.format("%02d", dag);
		String maandstring = String.format("%02d", maand);
		String jaarString = Integer.toString(jaar);
		String datumString = dagString.concat("/")
				.concat(maandstring).concat("/").concat(jaarString);
		
		return datumString;
	}
	
	/**
	 * @param datum Date
	 * @return String
	 * 
	 * genereer een string met het tijdstip (00:00;00)
	 */
	public static String tijdstipToString(Date datum) {
		int uur = datum.getHours();
		int minuten = datum.getMinutes();
		int seconden = datum.getSeconds();

		String tijdstip = uur + ":" + minuten + ":" + seconden;
	
		return tijdstip;
	}
	

}
