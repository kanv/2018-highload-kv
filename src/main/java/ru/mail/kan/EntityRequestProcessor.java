package ru.mail.kan;

import one.nio.http.HttpSession;
import one.nio.http.Request;
import one.nio.http.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ru.mail.polis.KVDao;

import java.io.IOException;
import java.util.NoSuchElementException;

public class EntityRequestProcessor implements RequestProcessor {
    private static final Logger LOG = LogManager.getLogger();

    private KVDao mDao;

    public EntityRequestProcessor(KVDao dao) {
        mDao = dao;
    }

    @Override
    public @NotNull Response process(Request request) {
        if (!Utils.requestHasParamId(request)) return Utils.Const.Responses.RESPONSE_BAD_REUQEST;
        byte[] key = request.getParameter(Utils.Const.Http.QUERY_PARAM_ID).getBytes();
        byte[] body = request.getBody();

        switch (request.getMethod()) {
            case Request.METHOD_GET:
                return processGet(key);
            case Request.METHOD_PUT:
                return processPut(key, body);
            case Request.METHOD_DELETE:
                return processDelete(key);
            default:
                return Utils.Const.Responses.RESPONSE_METHOD_NOT_ALLOWED;
        }
    }

    private @NotNull
    Response processGet(byte[] key) {
        try {
            Data data = Data.make(mDao.get(key));
            return Response.ok(data.toByteArray());
        } catch (final NoSuchElementException e) {
            return Utils.Const.Responses.RESPONSE_NOT_FOUND;
        } catch (final IOException e) {
            return Utils.Const.Responses.RESPONSE_INTERNAL_ERROR;
        }
    }

    private @NotNull
    Response processPut(byte[] key, byte[] body) {
        if (body == null) return Utils.Const.Responses.RESPONSE_BAD_REUQEST;

        try {
            Data data = Data.make(Data.Status.UPSERTED, System.currentTimeMillis(), body);
            mDao.upsert(key, data.toByteArray());
            return new Response(Response.CREATED, data.toByteArray());
        } catch (final IOException e) {
            return Utils.Const.Responses.RESPONSE_INTERNAL_ERROR;
        }
    }

    private @NotNull
    Response processDelete(byte[] key) {
        try {
            Data data = Data.make(Data.Status.UPSERTED, System.currentTimeMillis());
            mDao.upsert(key, data.toByteArray());
            return Utils.Const.Responses.RESPONSE_ACCEPTED;
        } catch (final IOException e) {
            return Utils.Const.Responses.RESPONSE_INTERNAL_ERROR;
        }
    }
}
