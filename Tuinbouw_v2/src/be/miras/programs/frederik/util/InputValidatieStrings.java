package be.miras.programs.frederik.util;

/**
 * @author Frederik Vanden Bussche
 * 
 * Util class met: 
 *         - String's die meegegeven worden in errormessages tijdens de
 *         inputvalidatie.
 *         - String's die meegegeven worden tijdens mislukte inlogpogingen
 *
 */

public class InputValidatieStrings {
	
	public static final String GeplandeDatum = " Geplande datum";
	public static final String GeplandeDatumToekomst = " De geplande datum moet in de toekomst liggen.";
	public static final String GeplandeDatumNaStartDatumOpdracht = " De geplande datum moet na de startdatum van de opdracht liggen: ";
	public static final String GeplandeDatumVoorEinddatumOpdracht = " De geplande datum moet voor de einddatum van de opdracht liggen: ";
	public static final String Taaknaam = " Taaknaam";
	public static final String Voornaam = " Voornaam";
	public static final String Naam = " Naam";
	public static final String Loon = " Loon";
	public static final String Email = " Email";
	public static final String GeboorteDatum = " Geboortedatum";

	public static final String GeboortedatumInVerleden = " Geboortedatum moet in het verleden liggen;";
	public static final String Aanwervingsdatum = " Aanwervingsdatum";
	public static final String AanwervingsdatumNietInToekomst = " Aanwervingsdatum mag niet in de toekomst liggen.";

	public static final String Straat = " Straat";
	public static final String Huisnummer = " Huisnummer";
	public static final String Postcode = " Postcode";
	public static final String Plaats = " Plaats";

	public static final String NaamOpdrachtgever = " Naam opdrachtgever";
	public static final String OpdrachtNaam = "  Opdracht naam";
	public static final String StartDatum = " StartDatum";
	public static final String StartDatumToekomst = " De startdatum dient in de toekomst te liggen.";
	public static final String EindDatum = " Einddatum";
	public static final String EindDatumNaStartDatum = " De einddatum dient na de startdatum te liggen.";
	public static final String StartDatumNietNaEindDatum = " De startdatum mag niet na de einddatum zijn.";
	public static final String EinddatumNietVoorStartDatum = " De einddatum mag niet voor de startdatum zijn. ";

	public static final String HoeveelheidGeheelGetal = " De hoeveelheid moet een geheel getal zijn. ";

	public static final String Soort = " Soort";
	public static final String Eenheidsmaat = " Eeinheidsmaat";
	public static final String Eenheidsprijs = " Eenheidsprijs";
	public static final String EenheidsprijsNietCorrect = " De Eenheidsprijs werd niet correct ingevuld.";

	public static final String BtwNummer = " BTW nummer";

	public static final String FactuurMessage = "Er kunnen geen facturen voor deze "
			+ "klant worden gemaakt omdat er nog geen afgewerkte opdrachten zijn.";

	public static final String OudWachtwoord = " Oud Wachtwoord";
	public static final String NieuwWachtwoord = " Gelieve het nieuw wachtwoord 2 maal in te geven. ";

	public static final String GeboortedatumBedrijfsleider = " Geboortedatum: Een bedrijfsleider is ten minsten 18 jaar.";
	public static final String GebruikersNaamInGeven = " Gelieve een gebruikersnaam in te geven";
	public static final String GebruikersNaamInGebruik = " Deze gebruikersnaam is reeds in gebruik. Gelieve een andere gebruikersnaam te kiezen. ";

	public static final String InlogGebruikersnaamNietGekend = " Deze gebruikersnaam is niet gekend. ";
	public static final String InlogWachtwoordNietCorrect = " Het ingevoerde wachtwoord is onjuist. ";
	public static final String InlogBevoegdheid = " Gebruiker is bekend, maar je hebt de rechten niet om hier in te loggen. ";


}
