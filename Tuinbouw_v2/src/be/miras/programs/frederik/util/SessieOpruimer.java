/**
 * 
 */
package be.miras.programs.frederik.util;

import javax.servlet.http.HttpSession;

/**
 * @author Frederik Vanden Bussche
 * 
 *         Een class die session attributen verwijderd
 *
 */
public class SessieOpruimer {

	/**
	 * @param session HttpSession
	 * 
	 * Verwijdert alle gebruikte attributen uit de Session
	 */
	public static void AttributenVerwijderaar(HttpSession session) {

		String[] sessienamen = { "taak", "werknemerMap", "personeelLijst", "personeelsnaam", "takenLijst", "id",
				"aanspreeknaam", "buttonNaam", "personeelslid", "opdrachtDetailData", "opdrachtLijst", "materiaal",
				"materiaalLijst", "variabelVeldnaam1", "variabelVeldnaam2", "variabelVeld1", "variabelVeld2", "klant",
				"opdrachtLijstVoorKlantDetail", "opdrachtMap", "particulierLijst", "bedrijfLijst", "adresMap",
				"factuur", "klantMap", "klantlijst", "werkgever", };

		for (int i = 0; i < sessienamen.length; i++) {
			session.removeAttribute(sessienamen[i]);
		}

	}

}
