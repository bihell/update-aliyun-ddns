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

        String RR = args[3];
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

        JSONObject jsonObject = JSONUtil.parseObj(getResult(parameters,AccessKeySecret));
        JSONObject domainRecords = JSONUtil.parseObj(jsonObject.getStr("DomainRecords"));
        JSONArray record = JSONUtil.parseArray(domainRecords.getStr("Record"));

        String recordId = null;
        String ip = null;
        for (Object var : record) {
            JSONObject entries = JSONUtil.parseObj(var);
            if (RR.equals(entries.getStr("RR")) && "A".equals(entries.getStr("Type"))) {
                recordId = entries.getStr("RecordId");
                ip = entries.getStr("Value");
                System.out.println("匹配记录："+entries.toString());
                break;
            }
        }

        if (recordId ==null)
        {
            System.out.println("未找到该记录！");
        }

        if (ip.equals(IpChecker.getIp()))
        {
            System.out.println("IP地址未改变，无需更新！");
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
            parameters.put("RR", RR);
            parameters.put("Type", "A");
            parameters.put("Value", IpChecker.getIp());

            System.out.println("IP已更新："+getResult(parameters,AccessKeySecret));
        }
    }

    @NotNull
    private static String getResult(Map<String, String> parameters,String AccessKeySecret) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {
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
