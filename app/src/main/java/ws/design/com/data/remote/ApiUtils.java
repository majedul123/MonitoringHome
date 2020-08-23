package ws.design.com.data.remote;

/**
 * Created by Chike on 12/4/2016.
 */

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://159.65.144.200:8001/v1/";

    public static SOService getSOService() {

        return RetrofitClient.getClient(BASE_URL).create(SOService.class);
    }
}
