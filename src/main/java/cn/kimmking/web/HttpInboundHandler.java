package cn.kimmking.web;

import cn.kimmking.web.mapping.RouterMapping;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cn.kimmking.web.mapping.RouterMapping.DEFAULT;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/1 上午4:02
 */
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private List<UrlMapping> mappings = new ArrayList<>();

    public HttpInboundHandler() {
        this.mappings.add(DEFAULT);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            //logger.info("channelRead流量接口请求开始，时间为{}", startTime);
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            String uri = fullRequest.uri();
            System.out.println("  uri ==>> " + uri);
            WebHandler webHandler = mappings.stream()
                    .map(m -> m.getHandler(uri))
                    .filter(Objects::nonNull).findFirst().orElse(null);

            if(webHandler == null) {
                writeResponseWith(ctx, fullRequest, "no web handler in mappings for uri:" + uri);
            } else {
                webHandler.handle(ctx, fullRequest);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void writeResponseWith(ChannelHandlerContext ctx, FullHttpRequest fullRequest, String message) {
        byte[] body = message.getBytes(StandardCharsets.UTF_8);
        System.out.println("length=" + body.length);
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND, Unpooled.wrappedBuffer(body));
        response.headers().set("Content-Type", "application/json");
        response.headers().setInt("Content-Length", body.length);
        response.headers().set("kk.web.handler", "NULL");

        if (fullRequest != null) {
            if (!HttpUtil.isKeepAlive(fullRequest)) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                ctx.write(response);
            }
        }
        ctx.flush();
    }

}
