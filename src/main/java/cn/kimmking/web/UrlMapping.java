package cn.kimmking.web;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/1 上午4:07
 */
public interface UrlMapping {

    WebHandler getHandler(String url);

}
