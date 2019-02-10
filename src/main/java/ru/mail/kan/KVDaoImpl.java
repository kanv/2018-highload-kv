/*
 * Copyright 2019 (c) Vitaly Kan <vitalikkan@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package ru.mail.kan;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import ru.mail.polis.KVDao;

/**
 * Key-value DAO API implementation
 *
 * @author Vitaly Kan <vitalikkan@gmail.com>
 */
public class KVDaoImpl implements KVDao {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String FILENAME = "KVS";

    private DB mDb;
    private HTreeMap<byte[], byte[]> mMap;

    public KVDaoImpl(final File file) {
        mDb = DBMaker.fileDB(file.getAbsolutePath() + File.pathSeparator + FILENAME)
                .fileMmapEnable()
                .cleanerHackEnable()
                .make();
        mMap = mDb.hashMap(FILENAME, Serializer.BYTE_ARRAY, Serializer.BYTE_ARRAY)
                .create();
    }

    @Override
    public @NotNull
    byte[] get(@NotNull byte[] key) throws NoSuchElementException, IOException {
        if (!mMap.containsKey(key)) {
            LOGGER.warn("No such element: key = {}", key);
            throw new NoSuchElementException();
        }
        return mMap.get(key);
    }

    @Override
    public void upsert(@NotNull byte[] key, @NotNull byte[] value) throws IOException {
        mMap.put(key, value);
    }

    @Override
    public void remove(@NotNull byte[] key) throws IOException {
        mMap.remove(key);
    }

    @Override
    public void close() {
        if (!mDb.isClosed()) mDb.close();
    }
}
