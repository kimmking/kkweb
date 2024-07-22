package cn.kimmking.web.mapping;

import cn.kimmking.web.Request;
import cn.kimmking.web.Response;
import cn.kimmking.web.UrlMapping;
import cn.kimmking.web.WebHandler;
import cn.kimmking.web.handler.FunctionAdapterWebHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/2 上午2:09
 */
public class RouterMapping implements UrlMapping {

    public static final RouterMapping DEFAULT = new RouterMapping();

    Map<String, WebHandler> handlers = new HashMap<>();

    @Override
    public WebHandler getHandler(String url) {
        return handlers.entrySet().stream()
                .filter(e -> url.matches(e.getKey()))
                .findFirst().map(Map.Entry::getValue)
                .orElse(null);
    }

    public void registerHandler(String urlPattern, WebHandler handler) {
        handlers.put(urlPattern, handler);
    }

    public void registerFunction(String urlPattern, Function<Request, Response> function) {
        registerHandler(urlPattern, new FunctionAdapterWebHandler(function));
    }

    public static void register(String urlPattern, Function<Request, Response> function) {
        DEFAULT.registerFunction(urlPattern, function);
    }

}
