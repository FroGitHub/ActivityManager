package back.activitymanager.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;

class LocationApi {

    private static final int EARTH_RADIUS_KM = 6371;
    private static final String CITY = "Київ";
    private static final String COUNTRY = "Україна";
    private static final String SUBURB = "Центр";
    private static final double DEFAULT_RADIUS_KM = 5;
    private static final String USER_AGENT = "YourApp";
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/";

    private LocationApi() {
    }

    public static boolean isWithinRadius(double lat1, double lon1,
                                         double lat2, double lon2,
                                         double radiusKm) {
        double lat = Math.toRadians(lat2 - lat1);
        double lon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(lat / 2) * Math.sin(lat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                        * Math.sin(lon / 2) * Math.sin(lon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;
        return distance <= radiusKm;
    }

    private static String getLocationName(String json) {
        JSONObject obj = new JSONObject(json);
        String displayName = obj.getString("display_name");
        String[] parts = displayName.split(",\\s*");
        return Arrays.stream(parts)
                .filter(s -> !CITY.equals(s))
                .filter(s -> !COUNTRY.equals(s))
                .filter(s -> !SUBURB.equals(s))
                .collect(Collectors.joining(", "));
    }

    private static double[] getLatLon(String districtName) throws IOException {
        String query = NOMINATIM_URL + "search?q="
                + URLEncoder.encode(districtName + " район, "
                + CITY, StandardCharsets.UTF_8)
                + "&format=json&limit=1";

        JSONObject obj = getFirstResultFromApi(query);
        double lat = obj.getDouble("lat");
        double lon = obj.getDouble("lon");

        return new double[]{lat, lon};
    }

    public static String getLocalNameByLatLon(double lat, double lon) {
        String query = NOMINATIM_URL + "reverse?lat=" + lat + "&lon=" + lon + "&format=json";
        try {
            JSONObject obj = getJsonObjectFromUrl(query);
            return getLocationName(obj.toString());
        } catch (Exception e) {
            return "Not found";
        }
    }

    public static boolean isEtLocal(double myLat,
                                    double myLon,
                                    String districtName) throws IOException {
        double[] latLon = getLatLon(districtName);
        return isWithinRadius(myLat, myLon, latLon[0], latLon[1], DEFAULT_RADIUS_KM);
    }

    private static JSONObject getFirstResultFromApi(String urlString) throws IOException {
        JSONArray arr = new JSONArray(getResponseFromUrl(urlString));
        if (arr.isEmpty()) {
            throw new IllegalArgumentException("No location found for: " + urlString);
        }
        return arr.getJSONObject(0);
    }

    private static JSONObject getJsonObjectFromUrl(String urlString) throws IOException {
        return new JSONObject(getResponseFromUrl(urlString));
    }

    private static String getResponseFromUrl(String urlString) throws IOException {
        URI uri = URI.create(urlString);
        URL url = uri.toURL();

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", USER_AGENT);

        if (conn.getResponseCode() != 200) {
            throw new IOException("HTTP error: " + conn.getResponseCode());
        }

        try (InputStream in = conn.getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
