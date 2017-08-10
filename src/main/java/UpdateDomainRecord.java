import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.*;

/**
 * Created by haseochen on 2017/8/4.
 */
public class UpdateDomainRecord {


    public static void main(String[] args) throws Exception {


        if (args.length == 0) {
            System.out.println(
                    "You should take parameters:\n" +
                            "1.Get Domain Records: UpdateDomainRecord.jar DescribeDomainRecords AccessKeyId AccessKeySecret DomainName\n" +
                            "2.Update Domain Records: UpdateDomainRecord.jar UpdateDomainRecord AccessKeyId AccessKeySecret RecordId RR ");
            System.exit(0);
        }

        Map<String, String> parameters = new HashMap<String, String>();
        // add request parameters
        parameters.put("Action", args[0]);
        parameters.put("Version", "2015-01-09");
        parameters.put("AccessKeyId", args[1]);
        parameters.put("Timestamp", StringToSign.formatIso8601Date(new Date()));
        parameters.put("SignatureMethod", "HMAC-SHA1");
        parameters.put("SignatureVersion", "1.0");
        parameters.put("SignatureNonce", UUID.randomUUID().toString());
        parameters.put("Format", "json");

        if (args[0].equals("DescribeDomainRecords")) {
            parameters.put("DomainName", args[3]);
        }

        if (args[0].equals("UpdateDomainRecord")) {
            parameters.put("RecordId", args[3]);
            parameters.put("RR", args[4]);
            parameters.put("Type", "A");
            parameters.put("Value", IpChecker.getIp());
        }

        // calculate signature
        String get_stringToSign = StringToSign.getSign(parameters);
        String signature = HmacSha1Signature.calculateRFC2104HMAC(get_stringToSign.toString(), args[2] + "&");

        // final request URL
        parameters.put("Signature", signature);
        String[] sortedKeys = parameters.keySet().toArray(new String[]{});
        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (String key : sortedKeys) {
            // encode key and value
            canonicalizedQueryString.append("&")
                    .append(StringToSign.percentEncode(key)).append("=")
                    .append(StringToSign.percentEncode(parameters.get(key)));
        }

        // send request
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://alidns.aliyuncs.com/?" + canonicalizedQueryString.substring(1))
                .get()
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());

    }
}
