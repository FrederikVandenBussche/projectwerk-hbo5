package be.miras.programs.frederik.util;

import java.util.Date;

public class Datum {

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

	public static Date creeerDatum(String datumString) {
		if (datumString != null && !datumString.isEmpty()) {
			String eersteLetter = datumString.substring(0, 1);
			int geboortejaar = 0;
			int geboortemaand = 0;
			int geboortedag = 0;
			if (eersteLetter.equals("1") || eersteLetter.equals("2")) {
				geboortejaar = Datatype.stringNaarInt(datumString.substring(0, 4)) - 1900; // YYYY
																							// -
																							// 1900
				geboortemaand = Datatype.stringNaarInt(datumString.substring(5, 7)) - 1; // tussen
																							// 0
																							// en
																							// 11
				geboortedag = Datatype.stringNaarInt(datumString.substring(8, 10));// tussen
																					// 1
																					// en
																					// 31

			} else {
				geboortejaar = Datatype.stringNaarInt(datumString.substring(24, 28)) - 1900; // YYYY
																								// -
																								// 1900
				String maandString = datumString.substring(4, 7);
				geboortemaand = Datum.converteerMaand(maandString);
				geboortedag = Datatype.stringNaarInt(datumString.substring(8, 10));// tussen
																					// 1
																					// en
																					// 31
			}
			Date datum = new Date(geboortejaar, geboortemaand, geboortedag);
			return datum;
		} else {
			return null;
		}
	}

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
	
	public static String tijdstipToString(Date datum) {
		int uur = datum.getHours();
		int minuten = datum.getMinutes();
		int seconden = datum.getSeconds();

		String tijdstip = uur + ":" + minuten + ":" + seconden;
	
		return tijdstip;
	}
	


}
