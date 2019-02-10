package ru.mail.kan;

import one.nio.http.HttpServerConfig;
import one.nio.http.Request;
import one.nio.http.Response;
import one.nio.server.AcceptorConfig;

import java.nio.ByteBuffer;

public class Utils {

    public static class Const {
        public static class Responses {
            public static final Response RESPONSE_CREATED = new Response(Response.CREATED, Response.EMPTY);
            public static final Response RESPONSE_ACCEPTED = new Response(Response.ACCEPTED, Response.EMPTY);
            public static final Response RESPONSE_NOT_FOUND = new Response(Response.NOT_FOUND, Response.EMPTY);
            public static final Response RESPONSE_BAD_REUQEST = new Response(Response.BAD_REQUEST, Response.EMPTY);
            public static final Response RESPONSE_INTERNAL_ERROR = new Response(Response.INTERNAL_ERROR, Response.EMPTY);
            public static final Response RESPONSE_GATEWAY_TIMEOUT = new Response(Response.GATEWAY_TIMEOUT, Response.EMPTY);
            public static final Response RESPONSE_METHOD_NOT_ALLOWED = new Response(Response.METHOD_NOT_ALLOWED, Response.EMPTY);
            public static final Response RESPONSE_SERVICE_UNAVAILABLE = new Response(Response.SERVICE_UNAVAILABLE, Response.EMPTY);
        }

        public class Http {
            public static final String QUERY_PARAM_ID = "id=";
            public static final String QUERY_PARAM_REPLICAS = "replicas=";
            public static final String HEADER_RETRANSMIT = "Retransmit: ";
        }

        public static final int SIZE_OF_LONG = 8;
    }

    public static class ByteUtil {
        private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

        public static byte[] fromLong(long x) {
            buffer.putLong(0, x);
            return buffer.array();
        }

        public static long toLong(byte[] bytes) {
            buffer.put(bytes, 0, bytes.length);
            buffer.flip();
            return buffer.getLong();
        }
    }

    public static HttpServerConfig createHttpServerConfig(int port) {
        HttpServerConfig cfg = new HttpServerConfig();
        cfg.acceptors = new AcceptorConfig[1];
        cfg.acceptors[0] = new AcceptorConfig();
        cfg.acceptors[0].port = port;
        return cfg;
    }

    public static boolean requestHasParamId(Request request) {
        return request != null && request.getParameter(Const.Http.QUERY_PARAM_ID) != null;
    }
}
