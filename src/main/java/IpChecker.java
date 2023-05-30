import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author haseochen
 */
public class IpChecker {
    public static void main(String[] args) throws Exception {
        getIp();
    }

    public static String getIp() throws Exception {
        URL whatismyip = new URL("https://ip.tool.lu");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine().split(": ")[1];
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}