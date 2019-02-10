package ru.mail.kan;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.mail.polis.KVDao;
import ru.mail.polis.KVService;

import java.io.IOException;

public class KVServiceImpl implements KVService {
    private static final Logger LOGGER = LogManager.getLogger();

    private KVDao mDao;
    private int mPort;
    private KVServer mServer;


    public KVServiceImpl(KVDao dao, int port) {
        mDao = dao;
        mPort = port;
    }

    @Override
    public void start() {
        try {
            mServer = new KVServer(mDao, mPort);
        } catch (final IOException e) {
            LOGGER.error("Cannot start server: {}", e.getLocalizedMessage());
        }
    }

    @Override
    public void stop() {
        mServer.stop();
    }
}
