package be.miras.programs.frederik.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import be.miras.programs.frederik.model.Adres;

/**
 * @author Frederik Vanden Bussche
 * 
 *         util class met alle methodes die gebruik maken van GoogleApis.
 *
 */
public class GoogleApis {

	// constanten voor adres-latlng conversie en omgekeerd
	final static private String basisUrl = "https://maps.googleapis.com/maps/api/";
	final static private String geocode = "geocode/";
	final static private String format = "json?";
	final static private String anteAdres = "address=";
	final static private String anteLatlng = "latlng=";
	final static private String GEOCODE_API_KEY = "&key=AIzaSyBPFvaC4_cZTBqOPB0HWnyP56P6iC1VFv8";

	// constanten voor het berekenen van een afstand tussen 2 locaties
	final static private String distancematrix = "distancematrix/";
	final static private String distance_units = "units=metric"; 
					// metric = km, imperial = miles
	final static private String distance_origins = "&origins=";
	final static private String distance_destinations = "&destinations=";
	final static private String DISTANCE_MATRIX_API_KEY = "&key=AIzaSyAUnPw4QSRquDQOT11dKrytCR_wnVnO6wk";

	// constanten voor het tonen van een static map
	final static private String staticmap = "staticmap?center=";
	final static private String staticmap_zoom = "&zoom=13";
	final static private String staticmap_size = "&size=300x150";
	final static private String staticmap_maptype = "&maptype=roadmap";
	final static private String staticmap_marker = "&markers=color:red%7C";
	final static private String STATICMAP_API_KEY = "&key=AIzaSyBnD0Uzz6p4FovJQX6jUdGgR3IxI4OMqVQ";

	final static private String googleMaps = "https://maps.google.com/?q=";

	private static final Logger LOGGER = Logger.getLogger(GoogleApis.class);
	private static final String TAG = "GoogleApis: ";
	
	
	public GoogleApis(){
	}

	/**
	 * @param adres Adres
	 * @return Double[]
	 * 
	 * return de latitude en longitude van een adres
	 */
	public static double[] zoeklatlng(Adres adres) {
		String url = urlBuilder(adres);
		String jsonString = executePost(url);
		double[] latlng = haalLatlng(jsonString);

		return latlng;
	}

	/**
	 * @param lat double
	 * @param lng double
	 * @return Adres
	 * 
	 * return een adres aan de hand van een latidute en een longitude
	 */
	public static Adres zoekAdres(double lat, double lng) {
		String url = urlBuilder(lat, lng);
		String jsonString = executePost(url);
		Adres adres = haalAdres(jsonString);

		return adres;
	}

	/**
	 * @param adres1 Adres
	 * @param adres2 Adres
	 * @return double
	 * 
	 * return het aantal km tussen twee adressen
	 */
	public static double berekenAantalKilometers(Adres adres1, Adres adres2) {
		String url = urlBuilder(adres1, adres2);

		double aantalKilometer = 0;
		try {
			String jsonString = executePost(url);
			aantalKilometer = haalAantalKilometers(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("IOException : " + TAG + "berekenAantalKilometers(adres, adres) ", e);
		}

		return aantalKilometer;
	}

	/**
	 * @param adres Adres
	 * @return String
	 * 
	 * return de url om de latlng uit een adres te halen
	 */
	private static String urlBuilder(Adres adres) {

		String straat = adres.getStraat();
		/// een spatie in de straatnaam dient vervangen te worden door '%20'
		char[] straatCharArray = straat.toCharArray();
		straat = "";
		for (int i = 0; i < straatCharArray.length; i++) {
			if (Character.isWhitespace(straatCharArray[i])) {
				straat = straat.concat("%20");
			} else {
				straat = straat.concat(String.valueOf(straatCharArray[i]));
			}
		}

		int huisnummer = adres.getNummer();
		int postcode = adres.getPostcode();
		String gemeente = adres.getPlaats();
		String adresString = straat + "+" + huisnummer + "+" + postcode + "+" + gemeente;

		String adresUrl = basisUrl + geocode + format + anteAdres + adresString + GEOCODE_API_KEY;

		return adresUrl;
	}

	/**
	 * @param lat double
	 * @param lng double
	 * @return String
	 * 
	 * return de url om het adres uit een latlng te halen
	 */
	private static String urlBuilder(double lat, double lng) {

		String latlng = lat + "," + lng;
		String latlngUrl = basisUrl + geocode + format + anteLatlng + latlng + GEOCODE_API_KEY;

		return latlngUrl;
	}

	/**
	 * @param adres1 Adres
	 * @param adres2 Adres
	 * @return String
	 * 
	 * return de url om de afstand tussen twee adressen te berekenen
	 */
	private static String urlBuilder(Adres adres1, Adres adres2) {
		String straat1 = adres1.getStraat();
		int huisnummer1 = adres1.getNummer();
		int postcode1 = adres1.getPostcode();
		String gemeente1 = adres1.getPlaats();
		String adresString1 = straat1 + "+" + huisnummer1 + "+" + postcode1 + "+" + gemeente1;

		String straat2 = adres2.getStraat();
		int huisnummer2 = adres2.getNummer();
		int postcode2 = adres2.getPostcode();
		String gemeente2 = adres2.getPlaats();
		String adresString2 = straat2 + "+" + huisnummer2 + "+" + postcode2 + "+" + gemeente2;
		String afstandUrl = basisUrl + distancematrix + format + distance_units + distance_origins + adresString1
				+ distance_destinations + adresString2 + DISTANCE_MATRIX_API_KEY;
		return afstandUrl;
	}

	/**
	 * @param adres Adres
	 * @return String
	 * 
	 * return de url om een Static Map te creëeren
	 */
	public static String urlBuilderStaticMap(Adres adres) {
		String straat = adres.getStraat();
		int huisnummer = adres.getNummer();
		int postcode = adres.getPostcode();
		String gemeente = adres.getPlaats();

		String adresString = straat + "+" + huisnummer + "+" + postcode + "+" + gemeente;
		String url = basisUrl + staticmap + adresString + staticmap_zoom + staticmap_size + staticmap_maptype
				+ staticmap_marker + adresString + STATICMAP_API_KEY;
		return url;
	}

	/**
	 * @param adres Adres
	 * @return String
	 * 
	 * return de url om het adres te tonen in google maps
	 */
	public static String urlBuilderGoogleMaps(Adres adres) {
		String straat = adres.getStraat();
		int huisnummer = adres.getNummer();
		int postcode = adres.getPostcode();
		String gemeente = adres.getPlaats();

		String url = googleMaps + straat + " " + huisnummer + " " + postcode + " " + gemeente;
		return url;
	}

	/**
	 * @param targetUrl String
	 * @return String
	 * 
	 * return de response indien de googleApis targetUrl gepost wordt
	 */
	private static String executePost(String targetUrl) {
		HttpURLConnection connection = null;

		try {
			// Creëer connectie
			URL url = new URL(targetUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Zend request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();

			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("IOException: " + TAG + "targetUrl = " + targetUrl);
			LOGGER.error("IOException: " + TAG + "ExecutePost(targetUrl) ", e);
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

	}

	/**
	 * @param jsonString String
	 * @return Double[]
	 * 
	 * return de latidute en de longitude uit een jsonString
	 */
	private static double[] haalLatlng(String jsonString) {
		double[] latlng = new double[2];
		try {
			JSONParser parser = new JSONParser();
			Object resultObject = parser.parse(jsonString);

			if (resultObject instanceof JSONObject) {
				JSONObject obj = (JSONObject) resultObject;
				if (obj.containsKey("results")) {
					JSONArray array = (JSONArray) obj.get("results");
					if (array.size() > 0) {
						JSONObject obj2 = (JSONObject) array.get(0);
						if (obj2.containsKey("geometry")) {
							JSONObject obj3 = (JSONObject) obj2.get("geometry");
							if (obj3.containsKey("location")) {
								JSONObject obj4 = (JSONObject) obj3.get("location");
								if (obj4.containsKey("lng") && obj4.containsKey("lat")) {

									Double lng = (Double) obj4.get("lng");
									Double lat = (Double) obj4.get("lat");

									latlng[0] = lat;
									latlng[1] = lng;
								}
							}
						}
					}
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
			LOGGER.error("ParseException" + TAG + "haallatlng(jsonString): " + jsonString);
			LOGGER.error("ParseException" + TAG + "haalLatling(String jsonString)", e);
		}
		return latlng;
	}

	/**
	 * @param jsonString String
	 * @return Adres
	 * 
	 * return het adres uit een jsonString
	 */
	private static Adres haalAdres(String jsonString) {
		Adres adres = new Adres();
		try {
			JSONParser parser = new JSONParser();
			Object resultObject = parser.parse(jsonString);

			if (resultObject instanceof JSONObject) {
				JSONObject obj = (JSONObject) resultObject;
				if (obj.containsKey("results")) {
					JSONArray array = (JSONArray) obj.get("results");
					if (array.size() > 0) {
						JSONObject obj2 = (JSONObject) array.get(0);
						if (obj2.containsKey("address_components")) {
							JSONArray array2 = (JSONArray) obj2.get("address_components");
							if (array2.size() > 5) {
								JSONObject street_number = (JSONObject) array2.get(0);
								JSONObject route = (JSONObject) array2.get(1);
								JSONObject locality = (JSONObject) array2.get(2);
								JSONObject postal_code = (JSONObject) array2.get(6);
								String key = "long_name";
								String straat = (String) route.get(key);
								String huisnummer = (String) street_number.get(key);
								String postcode = (String) postal_code.get(key);
								String plaats = (String) locality.get(key);
								adres.setStraat(straat);
								adres.setNummer(Datatype.stringNaarInt(huisnummer));
								adres.setPostcode(Datatype.stringNaarInt(postcode));
								adres.setPlaats(plaats);
							}
						}
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			LOGGER.error("ParseException: " + TAG + "haalAdres(jsonString): " + jsonString);
			LOGGER.error("ParseException: " + TAG + "haalAdres(jsonString): ", e);
		}
		return adres;
	}

	/**
	 * @param jsonString String
	 * @return double
	 * 
	 * return het aantal kilometers uit een jsonString
	 */
	private static double haalAantalKilometers(String jsonString) {
		double aantalKilometer = Double.MIN_VALUE;
		try {
			JSONParser parser = new JSONParser();
			Object resultObject = parser.parse(jsonString);

			if (resultObject instanceof JSONObject) {
				JSONObject obj = (JSONObject) resultObject;
				if (obj.containsKey("rows")){
					JSONArray array = (JSONArray) obj.get("rows");
					if (array.size() > 0){
						JSONObject obj2 = (JSONObject) array.get(0);
						if (obj2.containsKey("elements")){
							JSONArray array2 = (JSONArray) obj2.get("elements");
							if (array2.size() > 0){
								JSONObject obj3 = (JSONObject) array2.get(0);
								if (obj3.containsKey("distance")){
									JSONObject obj4 = (JSONObject) obj3.get("distance");
									if (obj4.containsKey("text")){
										String afstand = (String) obj4.get("text");

										String[] afstandSplit = afstand.trim().split("\\s+");
										aantalKilometer = Datatype.stringNaarDouble(afstandSplit[0]);
									}
								}
							}
						}
					}
				}	
			}

		} catch (ParseException e) {
			e.printStackTrace();
			LOGGER.error("ParseException" + TAG + "haalAantalKilometers(jsonString): "+ jsonString);
			LOGGER.error("ParseException" + TAG + "haalAantalKilometers(jsonString): ", e);
		}
		return aantalKilometer;
	}

	
}
