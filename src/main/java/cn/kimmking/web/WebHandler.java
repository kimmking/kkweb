package cn.kimmking.web;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/1 上午4:08
 */
public interface WebHandler {
    void handle(ChannelHandlerContext ctx, FullHttpRequest fullRequest);
}
