package be.miras.programs.frederik.model;

import java.util.List;
import java.util.Map;

/**
 * @author Frederik Vanden Bussche
 * 
 *         view model van de benodigde date om het opdrachtDetail op te bouwen
 *         
 *         De data wordt meegegeven aan de session als
 *         men de details van een opdracht ophaaldt.
 *
 */
public class OpdrachtDetailData {

	private String aanspreeknaam;
	private String variabelveld1;
	private String variabelveld2;
	private String buttonNaam;
	private Opdracht opdracht;
	private Map<Integer, String> klantNaamMap;
	private String adresString;
	private Map<Integer, String> adresMap;
	private List<Materiaal> materiaalLijst;
	private String staticmap;
	private String googlemap;

	public OpdrachtDetailData() {
	}

	/**
	 * @param aanspreeknaam String
	 * @param variabelveld1 String
	 * @param variabelveld2 String
	 * @param buttonNaam String
	 * @param opdracht Opdracht
	 * @param klantNaamMap Map<Integer, String>
	 * @param adresString String
	 * @param adresMap Map<Integer, String>
	 * @param materiaalLijst List<Materiaal>
	 * @param staticmap String
	 * @param googlemap String
	 */
	public OpdrachtDetailData(String aanspreeknaam, String variabelveld1, String variabelveld2, String buttonNaam,
			Opdracht opdracht, Map<Integer, String> klantNaamMap, String adresString, Map<Integer, String> adresMap,
			List<Materiaal> materiaalLijst, String staticmap, String googlemap) {
		super();
		this.aanspreeknaam = aanspreeknaam;
		this.variabelveld1 = variabelveld1;
		this.variabelveld2 = variabelveld2;
		this.buttonNaam = buttonNaam;
		this.opdracht = opdracht;
		this.klantNaamMap = klantNaamMap;
		this.adresString = adresString;
		this.adresMap = adresMap;
		this.materiaalLijst = materiaalLijst;
		this.staticmap = staticmap;
		this.googlemap = googlemap;
	}

	/**
	 * @return the aanspreeknaam String
	 */ 
	public String getAanspreeknaam() {
		return aanspreeknaam;
	}

	/**
	 * @param aanspreeknaam
	 *            the aanspreeknaam to set String
	 */
	public void setAanspreeknaam(String aanspreeknaam) {
		this.aanspreeknaam = aanspreeknaam;
	}

	/**
	 * @return the variabelveld1 String
	 */
	public String getVariabelveld1() {
		return variabelveld1;
	}

	/**
	 * @param variabelveld1
	 *            the variabelveld1 to set String
	 */
	public void setVariabelveld1(String variabelveld1) {
		this.variabelveld1 = variabelveld1;
	}

	/**
	 * @return the variabelveld2 String
	 */
	public String getVariabelveld2() {
		return variabelveld2;
	}

	/**
	 * @param variabelveld2
	 *            the variabelveld2 to set String
	 */
	public void setVariabelveld2(String variabelveld2) {
		this.variabelveld2 = variabelveld2;
	}

	/**
	 * @return the buttonNaam String
	 */
	public String getButtonNaam() {
		return buttonNaam;
	}

	/**
	 * @param buttonNaam
	 *            the buttonNaam to set String
	 */
	public void setButtonNaam(String buttonNaam) {
		this.buttonNaam = buttonNaam;
	}

	/**
	 * @return the opdracht Opdracht
	 */
	public Opdracht getOpdracht() {
		return opdracht;
	}

	/**
	 * @param opdracht
	 *            the opdracht to set Opdracht
	 */
	public void setOpdracht(Opdracht opdracht) {
		this.opdracht = opdracht;
	}

	/**
	 * @return the klantNaamMap Map<Integer, String>
	 */
	public Map<Integer, String> getKlantNaamMap() {
		return klantNaamMap;
	}

	/**
	 * @param klantNaamMap
	 *            the klantNaamMap to set Map<Integer, String
	 */
	public void setKlantNaamMap(Map<Integer, String> klantNaamMap) {
		this.klantNaamMap = klantNaamMap;
	}

	/**
	 * @return the adresString String
	 */
	public String getAdresString() {
		return adresString;
	}

	/**
	 * @param adresString
	 *            the adresString to set String
	 */
	public void setAdresString(String adresString) {
		this.adresString = adresString;
	}

	/**
	 * @return the adresMap Map<Integer, String>
	 */
	public Map<Integer, String> getAdresMap() {
		return adresMap;
	}

	/**
	 * @param adresMap
	 *            the adresMap to set Map<Integer, String>
	 */
	public void setAdresMap(Map<Integer, String> adresMap) {
		this.adresMap = adresMap;
	}

	/**
	 * @return the materiaalLijst List<Materiaal>
	 */
	public List<Materiaal> getMateriaalLijst() {
		return materiaalLijst;
	}

	/**
	 * @param materiaalLijst
	 *            the materiaalLijst to set List<Materiaal>
	 */
	public void setMateriaalLijst(List<Materiaal> materiaalLijst) {
		this.materiaalLijst = materiaalLijst;
	}

	/**
	 * @return the staticmap String
	 */
	public String getStaticmap() {
		return staticmap;
	}

	/**
	 * @param staticmap
	 *            the staticmap to set String
	 */
	public void setStaticmap(String staticmap) {
		this.staticmap = staticmap;
	}

	/**
	 * @return the googlemap String
	 */
	public String getGooglemap() {
		return googlemap;
	}

	/**
	 * @param googlemap
	 *            the googlemap to set String
	 */
	public void setGooglemap(String googlemap) {
		this.googlemap = googlemap;
	}

	
}
