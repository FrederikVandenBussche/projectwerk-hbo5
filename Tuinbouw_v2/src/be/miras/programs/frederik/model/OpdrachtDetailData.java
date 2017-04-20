package be.miras.programs.frederik.model;

import java.util.List;
import java.util.Map;

/*
 * De data die meegegeven wordt aan de session als
 * men de details van een opdracht ophaalt
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
		super();
	}

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

	public String getAanspreeknaam() {
		return aanspreeknaam;
	}

	public void setAanspreeknaam(String aanspreeknaam) {
		this.aanspreeknaam = aanspreeknaam;
	}

	public String getVariabelveld1() {
		return variabelveld1;
	}

	public void setVariabelveld1(String variabelveld1) {
		this.variabelveld1 = variabelveld1;
	}

	public String getVariabelveld2() {
		return variabelveld2;
	}

	public void setVariabelveld2(String variabelveld2) {
		this.variabelveld2 = variabelveld2;
	}

	public String getButtonNaam() {
		return buttonNaam;
	}

	public void setButtonNaam(String buttonNaam) {
		this.buttonNaam = buttonNaam;
	}

	public Opdracht getOpdracht() {
		return opdracht;
	}

	public void setOpdracht(Opdracht opdracht) {
		this.opdracht = opdracht;
	}

	public Map<Integer, String> getKlantNaamMap() {
		return klantNaamMap;
	}

	public void setKlantNaamMap(Map<Integer, String> klantNaamMap) {
		this.klantNaamMap = klantNaamMap;
	}

	public String getAdresString() {
		return adresString;
	}

	public void setAdresString(String adresString) {
		this.adresString = adresString;
	}

	public Map<Integer, String> getAdresMap() {
		return adresMap;
	}

	public void setAdresMap(Map<Integer, String> adresMap) {
		this.adresMap = adresMap;
	}

	public List<Materiaal> getMateriaalLijst() {
		return materiaalLijst;
	}

	public void setMateriaalLijst(List<Materiaal> materiaalLijst) {
		this.materiaalLijst = materiaalLijst;
	}

	public String getStaticmap() {
		return staticmap;
	}

	public void setStaticmap(String staticmap) {
		this.staticmap = staticmap;
	}

	public String getGooglemap() {
		return googlemap;
	}

	public void setGooglemap(String googlemap) {
		this.googlemap = googlemap;
	}
	
	

}
