package cn.kimmking.web;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/2 上午3:14
 */
@Data
public class Request {
    private String method;
    private String path;
    private Map<String, String> params     = new HashMap<>();
    private Map<String, String> headers    = new HashMap<>();
    private Map<String, String> cookies    = new HashMap<>();
    private Map<String, String> attributes = new HashMap<>();
    private byte[] body;
}
