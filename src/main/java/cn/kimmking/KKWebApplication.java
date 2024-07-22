package cn.kimmking;

import cn.kimmking.web.KKWebServer;
import cn.kimmking.web.Response;
import cn.kimmking.web.mapping.RouterMapping;

import java.util.Properties;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/1 上午3:51
 */
public class KKWebApplication {

    public final static String WEB_NAME = "KKWeb";
    public final static String WEB_VERSION = "1.0.0";
    public final static String WEB_PORT_NAME = "kk.web.port";
    public final static int WEB_PORT = 8080;

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.setProperty(WEB_PORT_NAME, ""+WEB_PORT);

        addRouterFunction();

        System.out.println(WEB_NAME + " " + WEB_VERSION +" starting...");
        KKWebServer server = new KKWebServer(properties);
        System.out.println(WEB_NAME + " " + WEB_VERSION +" started at http://localhost:" + WEB_PORT + " for server:" + server.toString());
        try {
            server.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void addRouterFunction() {

        RouterMapping.register("/info", (request) -> {
            Response response = new Response();
            response.setStatusCode(200);
            response.setBody("{\"web\":\"KK Web\",\"version\":\"v1.0.0\"}".getBytes());
            response.getHeaders().put("version", "v1.0.0");
            return response;
        });

    }

}
