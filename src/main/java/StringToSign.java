import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by haseochen on 2017/8/4.
 */
public class StringToSign {
    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static String formatIso8601Date(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    private static final String ENCODING = "UTF-8";

    public static String percentEncode(String value)
            throws UnsupportedEncodingException {
        return value != null ?
                URLEncoder.encode(value, ENCODING).replace("+", "%20")
                        .replace("*", "%2A").replace("%7E", "~")
                : null;
    }

    public static String getSign(Map<String, String> parameters) throws UnsupportedEncodingException {

        final String HTTP_METHOD = "GET";

        // Sort
        String[] sortedKeys = parameters.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);

        final String SEPARATOR = "&";

        // generate stringToSign
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(HTTP_METHOD).append(SEPARATOR);
        stringToSign.append(percentEncode("/")).append(SEPARATOR);

        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (String key : sortedKeys) {
            // encode key and value
            canonicalizedQueryString.append("&")
                    .append(percentEncode(key)).append("=")
                    .append(percentEncode(parameters.get(key)));
        }

        // encode canonicalizedQueryString
        stringToSign.append(percentEncode(canonicalizedQueryString.toString().substring(1)));

        return stringToSign.toString();
    }

}
