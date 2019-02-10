package ru.mail.kan;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class Data {
    public static enum Status {
        UPSERTED(0),
        REMOVED(1);

        private final int index;

        Status(int index) {
            this.index = index;
        }

        public int value() {
            return index;
        }
    }

    private static final int INDEX_STATUS = 0;
    private static final int INDEX_BEGIN_TIMESTAMP = 1;
    private static final int INDEX_END_TIMESTAMP = 8;
    private static final int INDEX_BEGIN_DATA = 9;

    public static Data make(byte[] raw) throws IllegalStateException {
        if (raw.length <= INDEX_BEGIN_DATA) throw new IllegalStateException("Invalid data: invalid length");
        if (raw[0] < 0 || raw[0] > 1) throw new IllegalStateException("Invalid data: invalid status");

        Status status = Status.values()[raw[INDEX_STATUS]];
        long timestamp = Utils.ByteUtil.toLong(Arrays.copyOfRange(raw, INDEX_BEGIN_TIMESTAMP, INDEX_END_TIMESTAMP));
        byte[] data = Arrays.copyOfRange(raw, INDEX_BEGIN_DATA, raw.length - 1);

        return new Data(status, timestamp, data);
    }

    public static Data make(Status status, long timestamp, byte[] data) {
        return new Data(status, timestamp, data);
    }

    public static Data make(Status status, long timestamp) {
        return new Data(status, timestamp, null);
    }

    private Status mStatus;
    private long mTimestamp;
    private byte[] mData;

    private Data(Status status, long timestamp, byte[] data) {
        this.mStatus = status;
        this.mTimestamp = timestamp;
        this.mData = data;
    }

    public Status getStatus() {
        return mStatus;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    public byte[] getData() {
        return mData;
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] status = new byte[0];
        status[0] = (byte) mStatus.value();
        byte[] timestamp = Utils.ByteUtil.fromLong(mTimestamp);

        out.write(status, INDEX_STATUS, 1);
        out.write(timestamp, INDEX_BEGIN_TIMESTAMP, INDEX_END_TIMESTAMP);
        if (mData != null) out.write(mData, INDEX_BEGIN_DATA, mData.length - 1);

        return out.toByteArray();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other.getClass() != Data.class) return false;

        Data data = (Data) other;

        if (mStatus != data.getStatus()) return false;
        if (mTimestamp != data.getTimestamp()) return false;

        if (mData != null) {
            if (data.getData() == null) return false;
            return Arrays.equals(mData, data.getData());
        } else return data.getData() == null;

    }

    @Override
    public int hashCode() {
        int result = mStatus.hashCode();
        result = 31 * result + Long.hashCode(mTimestamp);
        result = 31 * result + Arrays.hashCode(mData);
        return result;
    }
}



