package back.activitymanager.api;

import java.io.IOException;

public class LocalAdaptorApi {

    public static String getLocalNameByLatLon(double lat, double lng) {
        return LocationApi.getLocalNameByLatLon(lat, lng);
    }

    public static boolean isEtLocal(double myLat, double myLng, String districtName)
            throws IOException {
        return LocationApi.isEtLocal(myLat, myLng, districtName);
    }
}
