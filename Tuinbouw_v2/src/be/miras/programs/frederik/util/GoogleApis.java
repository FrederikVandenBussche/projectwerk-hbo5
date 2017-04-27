package be.miras.programs.frederik.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import be.miras.programs.frederik.model.Adres;

public class GoogleApis {
	
	final static private String basisUrl = "https://maps.googleapis.com/maps/api/";
	final static private String geocode = "geocode/";
	final static private String format = "json?";
	final static private String anteAdres = "address=";
	final static private String anteLatlng = "latlng=";
	final static private String API_KEY = "&key=AIzaSyBPFvaC4_cZTBqOPB0HWnyP56P6iC1VFv8";
	
	// constanten voor het berekenen van een afstand tussen 2 locaties
	//basisUrl
	final static private String distancematrix = "distancematrix/" ;
	//format
	final static private String distance_units = "units=metric"; //metric = km, imperial = miles
	final static private String distance_origins = "&origins=";
	//eerste adres
	final static private String distance_destinations = "&destinations=";
	// tweede adres
	final static private String DISTANCE_MATRIX_API_KEY ="&key=AIzaSyAUnPw4QSRquDQOT11dKrytCR_wnVnO6wk";

	//constanten voor het tonen van een static map
	//besisurl
	final static private String staticmap = "staticmap?center=";
	// het adres die in het center van de map moet staan
	final static private String staticmap_zoom = "&zoom=13";
	final static private String staticmap_size = "&size=300x150";
	final static private String staticmap_maptype = "&maptype=roadmap";
	final static private String staticmap_marker = "&markers=color:red%7C";
	//lat,lng van de marker
	final static private String STATICMAP_API_KEY = "&key=AIzaSyBnD0Uzz6p4FovJQX6jUdGgR3IxI4OMqVQ";
	
	final static private String googleMaps = "https://maps.google.com/?q=";
	
	
	public static double[] zoeklatlng(Adres adres) {
		String url = urlBuilder(adres);
		String jsonString = executePost(url);
		double[] latlng = haalLatlng(jsonString);

		return latlng;
	}

	public static Adres zoekAdres (double lat, double lng){
		String url = urlBuilder(lat, lng);
		String jsonString = executePost(url);
		Adres adres = haalAdres(jsonString);
		
		return adres;
	}
	
	public static double berekenAantalKilometers(Adres adres1, Adres adres2){
		String url = urlBuilder(adres1, adres2);
		String jsonString = executePost(url);
		double aantalKilometer = haalAantalKilometers(jsonString);
		
		return aantalKilometer;
	}

	private static String urlBuilder(Adres adres) {
		
		String straat = adres.getStraat();
		///een spatie in de straatnaam dient vervangen te worden door '%20'
		char[] straatCharArray = straat.toCharArray();
		straat = "";
		for (int i = 0; i < straatCharArray.length; i++){
			if(Character.isWhitespace(straatCharArray[i])){
				straat = straat.concat("%20");
			} else {
				straat = straat.concat(String.valueOf(straatCharArray[i]));
			}
		}
		
		int huisnummer = adres.getNummer();
		int postcode = adres.getPostcode();
		String gemeente = adres.getPlaats();
		String adresString = straat + "+" + huisnummer + "+" + postcode + "+" + gemeente;

		String adresUrl = basisUrl + geocode + format + anteAdres + adresString + API_KEY;

		return adresUrl;
	}

	private static String urlBuilder(double lat, double lng) {

		String latlng = lat + "," + lng;
		String latlngUrl = basisUrl + geocode + format + anteLatlng + latlng + API_KEY;

		return latlngUrl;
	}

	private static String urlBuilder(Adres adres1, Adres adres2){
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
		String afstandUrl = basisUrl + distancematrix + format + distance_units + distance_origins 
				+ adresString1 + distance_destinations + adresString2 + DISTANCE_MATRIX_API_KEY;
		return afstandUrl;
	}
	
	public static String urlBuilderStaticMap(Adres adres){
		String straat = adres.getStraat();
		int huisnummer = adres.getNummer();
		int postcode = adres.getPostcode();
		String gemeente = adres.getPlaats();
				
		String adresString = straat + "+" + huisnummer + "+" + postcode + "+" + gemeente;
		String url = basisUrl + staticmap + adresString + staticmap_zoom 
				+ staticmap_size + staticmap_maptype + 
				staticmap_marker + adresString + STATICMAP_API_KEY; 
 		return url;
	}
	
	public static String urlBuilderGoogleMaps(Adres adres){
		String straat = adres.getStraat();
		int huisnummer = adres.getNummer();
		int postcode = adres.getPostcode();
		String gemeente = adres.getPlaats();
		
		String url = googleMaps + straat + " " + huisnummer + " " + postcode + " " + gemeente;
		return url;
	}

	private static String executePost(String targetUrl) {
		HttpURLConnection connection = null;

		try {
			// Creëer connectie
			URL url = new URL(targetUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			// connection.setRequestProperty("Content-Length",
			// Integer.toString(urlParameters.getBytes().length));
			// connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoOutput(true);

			// Zend request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			;
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
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

	}

	private static double[] haalLatlng(String jsonString) {
		double[] latlng = new double[2];
		try {
			JSONParser parser = new JSONParser();
			Object resultObject = parser.parse(jsonString);

			if (resultObject instanceof JSONObject) {
				JSONObject obj = (JSONObject) resultObject;
				JSONArray array = (JSONArray) obj.get("results");
				JSONObject obj2 = (JSONObject) array.get(0);
				JSONObject obj3 = (JSONObject) obj2.get("geometry");
				JSONObject obj4 = (JSONObject) obj3.get("location");

				Double lng = (Double) obj4.get("lng");
				Double lat = (Double) obj4.get("lat");
				latlng[0] = lat;
				latlng[1] = lng;
			}

		} catch (ParseException e) {

			e.printStackTrace();
		}
		return latlng;
	}

	private static Adres haalAdres(String jsonString) {
		Adres adres = new Adres();
		try {
			JSONParser parser = new JSONParser();
			Object resultObject = parser.parse(jsonString);
			
			if (resultObject instanceof JSONObject) {
				JSONObject obj = (JSONObject) resultObject;
				JSONArray array = (JSONArray) obj.get("results");
				JSONObject obj2 = (JSONObject) array.get(0);
				JSONArray array2 = (JSONArray) obj2.get("address_components");
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

		} catch (ParseException e) {

			e.printStackTrace();
		}
		return adres;
	}

	private static double haalAantalKilometers(String jsonString){
		double aantalKilometer = Double.MIN_VALUE;
		try {
			JSONParser parser = new JSONParser();
			Object resultObject = parser.parse(jsonString);

			if (resultObject instanceof JSONObject) {
				JSONObject obj = (JSONObject) resultObject;
				JSONArray array = (JSONArray) obj.get("rows");
				JSONObject obj2 = (JSONObject) array.get(0);
				JSONArray array2 = (JSONArray) obj2.get("elements");
				JSONObject obj3 = (JSONObject) array2.get(0);
				JSONObject obj4 = (JSONObject) obj3.get("distance");
				String afstand = (String) obj4.get("text");
				
				String[] afstandSplit = afstand.trim().split("\\s+");
				aantalKilometer = Datatype.stringNaarDouble(afstandSplit[0]);	
			}

		} catch (ParseException e) {

			e.printStackTrace();
		}
		return aantalKilometer;
	}

	
}
