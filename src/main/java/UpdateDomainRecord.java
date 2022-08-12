import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

/**
 * Created by haseochen on 2017/8/4.
 */
public class UpdateDomainRecord {


    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("You should take parameters:\n" + "1.Get Domain Records: UpdateDomainRecord.jar DescribeDomainRecords AccessKeyId AccessKeySecret DomainName\n" + "2.Update Domain Records: UpdateDomainRecord.jar UpdateDomainRecord AccessKeyId AccessKeySecret RecordId RR ");
            System.exit(0);
        }

        String[] RRs = args[3].split(",");
        String AccessKeyId = args[0];
        String AccessKeySecret = args[1];
        String DomainName = args[2];

        Map<String, String> parameters = new HashMap<String, String>();

        parameters.put("Action", "DescribeDomainRecords");
        parameters.put("Version", "2015-01-09");
        parameters.put("AccessKeyId", AccessKeyId);
        parameters.put("Timestamp", StringToSign.formatIso8601Date(new Date()));
        parameters.put("SignatureMethod", "HMAC-SHA1");
        parameters.put("SignatureVersion", "1.0");
        parameters.put("SignatureNonce", UUID.randomUUID().toString());
        parameters.put("Format", "json");
        parameters.put("DomainName", DomainName);

        JSONObject jsonObject = JSONUtil.parseObj(getResult(parameters, AccessKeySecret));
//        System.out.println(jsonObject.toStringPretty());
        JSONArray records = JSONUtil.parseArray(jsonObject.getByPath("DomainRecords.Record"));

        for (String rr : RRs) {
            String recordId = null;
            String ip = null;
            for (Object record : records) {
                JSONObject entries = JSONUtil.parseObj(record);
                if (rr.equals(entries.getStr("RR")) && "A".equals(entries.getStr("Type"))) {
                    recordId = entries.getStr("RecordId");
                    ip = entries.getStr("Value");
//                    System.out.println("matched record" + entries.toString());
                    break;
                }
            }

            if (recordId == null) {
                System.out.println("The record " + rr + " was not found");
                continue;
            }

            if (ip.equals(IpChecker.getIp())) {
                System.out.println("The IP address of " +rr+  " is unchanged and does not need to be updated");
            } else {
                parameters.clear();
                parameters.put("Action", "UpdateDomainRecord");
                parameters.put("Version", "2015-01-09");
                parameters.put("AccessKeyId", AccessKeyId);
                parameters.put("Timestamp", StringToSign.formatIso8601Date(new Date()));
                parameters.put("SignatureMethod", "HMAC-SHA1");
                parameters.put("SignatureVersion", "1.0");
                parameters.put("SignatureNonce", UUID.randomUUID().toString());
                parameters.put("Format", "json");

                parameters.put("RecordId", recordId);
                parameters.put("RR", rr);
                parameters.put("Type", "A");
                parameters.put("Value", IpChecker.getIp());

                System.out.println(rr+"has been updated");
            }
        }

    }

    @NotNull
    private static String getResult(Map<String, String> parameters, String AccessKeySecret) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        // calculate signature
        String getStringToSign = StringToSign.getSign(parameters);
        String signature = HmacSha1Signature.calculateRFC2104HMAC(getStringToSign.toString(), AccessKeySecret + "&");

        // final request URL
        parameters.put("Signature", signature);
        String[] sortedKeys = parameters.keySet().toArray(new String[]{});
        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (String key : sortedKeys) {
            // encode key and value
            canonicalizedQueryString.append("&").append(StringToSign.percentEncode(key)).append("=").append(StringToSign.percentEncode(parameters.get(key)));
        }

        // send request
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("https://alidns.aliyuncs.com/?" + canonicalizedQueryString.substring(1)).get().build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();
        return result;
    }
}
