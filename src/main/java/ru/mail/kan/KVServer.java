package ru.mail.kan;

import one.nio.http.*;
import ru.mail.polis.KVDao;

import java.io.IOException;

public class KVServer extends HttpServer {

    private EntityRequestProcessor mEntityRequestProcessor;

    public KVServer(KVDao dao, int port) throws IOException {
        super(Utils.createHttpServerConfig(port));
        mEntityRequestProcessor = new EntityRequestProcessor(dao);
    }

    @Path("/v0/entity")
    public void entity(Request request, HttpSession session) throws IOException {
        Response response = mEntityRequestProcessor.process(request);
        session.sendResponse(response);
    }
}
