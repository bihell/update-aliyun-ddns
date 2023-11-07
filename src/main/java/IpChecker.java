import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * @author haseochen
 */
public class IpChecker {
    public static void main(String[] args) throws Exception {
        getIp();
    }

    //    public static String getIp() throws Exception {
//        URL whatismyip = new URL("https://ip.tool.lu");
//        BufferedReader in = null;
//        try {
//            in = new BufferedReader(new InputStreamReader(
//                    whatismyip.openStream()));
//            String ip = in.readLine().split(": ")[1];
//            return ip;
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    public static String getIp() {
        String result = HttpUtil.get("https://www.ipplus360.com/getIP");
        JSONObject jsonObject = JSONUtil.parseObj(result);
        Boolean success = (Boolean) jsonObject.get("success");
        if (!success) {
            System.out.println((String) jsonObject.get("msg"));
            System.exit(1);
        }
        return (String) jsonObject.get("data");
    }
}