package cn.kimmking.web.handler;

import cn.kimmking.web.Request;
import cn.kimmking.web.Response;
import cn.kimmking.web.WebHandler;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.function.Function;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/6/2 上午4:05
 */
public class FunctionAdapterWebHandler implements WebHandler {
    private final Function<Request, Response> function;

    public FunctionAdapterWebHandler(Function<Request, Response> function) {
        this.function = function;
    }

    @Override
    public void handle(ChannelHandlerContext ctx, FullHttpRequest fullRequest) {
        Request request = buildRequest(ctx, fullRequest);
        FullHttpResponse response = buildResponse(ctx, fullRequest, handle(request));
        if (!HttpUtil.isKeepAlive(fullRequest)) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            ctx.write(response);
        }
        ctx.flush();
    }

    public Response handle(Request request) {
        return function.apply(request);
    }

    private FullHttpResponse buildResponse(ChannelHandlerContext ctx,
                                           FullHttpRequest fullRequest, Response response) {
        FullHttpResponse fullResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(response.getBody()));
        if(response.getStatusCode() != 200) {
            fullResponse.setStatus(HttpResponseStatus.valueOf(response.getStatusCode()));
        }
        fullResponse.headers().set("Content-Type", "application/json");
        fullResponse.headers().setInt("Content-Length", response.getBody().length);
        fullResponse.headers().set("kk.web.handler", "function.adapter");
        response.getHeaders().forEach((k,v) -> fullResponse.headers().set(k, v));
        return fullResponse;
    }

    private Request buildRequest(ChannelHandlerContext ctx, FullHttpRequest fullRequest) {
        Request request = new Request();
        request.setMethod(fullRequest.method().name());
        fullRequest.headers().forEach(e -> request.getHeaders().put(e.getKey(), e.getValue()));
        if("POST".equalsIgnoreCase(request.getMethod())) {
            byte[] bytes = ByteBufUtil.getBytes(fullRequest.content());
            request.setBody(bytes);
        }
        return request;
    }

}
