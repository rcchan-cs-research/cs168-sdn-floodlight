package org.openflow.protocol.statistics;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.openflow.util.U16;

/**
 * Represents an ofp_group_stats structure
 * @author Srini Seetharaman (srini.seetharaman@gmail.com)
 */
public class OFGroupStatisticsReply implements OFStatistics {
    public static int MINIMUM_LENGTH = 40;

    protected short length;
    protected int groupId;
    private int refCount;
    protected long packetCount;
    protected long byteCount;
    protected int durationSeconds;
    protected int durationNanoseconds;
    private List<OFGroupBucketCounter> bucketStatistics;

    public OFGroupStatisticsReply() {
        super();
        this.length = (short) MINIMUM_LENGTH;
        bucketStatistics = new ArrayList<OFGroupBucketCounter>();
    }

    /**
     * @return the groupId
     */
    public int getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public OFGroupStatisticsReply setGroupId(int groupId) {
        this.groupId = groupId;
        return this;
    }

    /**
     * @return the refCount
     */
    public int getRefCount() {
        return refCount;
    }

    /**
     * @param refCount the refCount to set
     */
    public void setRefCount(int refCount) {
        this.refCount = refCount;
    }

    /**
     * @return the packetCount
     */
    public long getPacketCount() {
        return packetCount;
    }

    /**
     * @param packetCount the packetCount to set
     */
    public OFGroupStatisticsReply setPacketCount(long packetCount) {
        this.packetCount = packetCount;
        return this;
    }

    /**
     * @return the byteCount
     */
    public long getByteCount() {
        return byteCount;
    }

    /**
     * @param byteCount the byteCount to set
     */
    public OFGroupStatisticsReply setByteCount(long byteCount) {
        this.byteCount = byteCount;
        return this;
    }

    /**
     * @param length the length to set
     */
    public void setLength(short length) {
        this.length = length;
    }

    @Override
    public int getLength() {
        return U16.f(length);
    }

    /**
     * @return the bucketStatistics
     */
    public List<OFGroupBucketCounter> getBandStatistics() {
        return bucketStatistics;
    }

    /**
     * @param bucketStatistics the bucketStatistics to set
     */
    public OFGroupStatisticsReply setBandStatistics(List<OFGroupBucketCounter> bucketStatistics) {
        this.bucketStatistics = bucketStatistics;
        updateLength();
        return this;
    }

    /**
     * @return the durationSeconds
     */
    public int getDurationSeconds() {
        return durationSeconds;
    }

    /**
     * @param durationSeconds the durationSeconds to set
     */
    public OFGroupStatisticsReply setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
        return this;
    }

    /**
     * @return the durationNanoseconds
     */
    public int getDurationNanoseconds() {
        return durationNanoseconds;
    }

    /**
     * @param durationNanoseconds the durationNanoseconds to set
     */
    public OFGroupStatisticsReply setDurationNanoseconds(int durationNanoseconds) {
        this.durationNanoseconds = durationNanoseconds;
        return this;
    }

    @Override
    public void readFrom(ByteBuffer data) {
        this.groupId = data.getInt();
        this.length = data.getShort();
        for (int i=0;i<6;i++)
            data.get(); // pad
        this.setRefCount(data.getInt());
        this.packetCount = data.getLong();
        this.byteCount = data.getLong();
        for (int i=0;i<this.length-MINIMUM_LENGTH;i+=OFGroupBucketCounter.MINIMUM_LENGTH) {
            OFGroupBucketCounter counter = new OFGroupBucketCounter();
            counter.readFrom(data);
            this.bucketStatistics.add(counter);
        }
    }

    @Override
    public void writeTo(ByteBuffer data) {
        data.putInt(this.groupId);
        data.putShort(this.length);
        for (int i=0;i<6;i++)
            data.put((byte) 0); //pad
        data.putInt(this.getRefCount());
        data.putLong(this.packetCount);
        data.putLong(this.byteCount);
        if (bucketStatistics != null) {
            for (OFGroupBucketCounter counter : bucketStatistics) {
                counter.writeTo(data);
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((bucketStatistics == null) ? 0 : bucketStatistics.hashCode());
        result = prime * result + (int) (byteCount ^ (byteCount >>> 32));
        result = prime * result + getRefCount();
        result = prime * result + length;
        result = prime * result + groupId;
        result = prime * result
                + (int) (packetCount ^ (packetCount >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OFGroupStatisticsReply other = (OFGroupStatisticsReply) obj;
        if (bucketStatistics == null) {
            if (other.bucketStatistics != null)
                return false;
        } else if (!bucketStatistics.equals(other.bucketStatistics))
            return false;
        if (byteCount != other.byteCount)
            return false;
        if (getRefCount() != other.getRefCount())
            return false;
        if (length != other.length)
            return false;
        if (groupId != other.groupId)
            return false;
        if (packetCount != other.packetCount)
            return false;
        return true;
    }

    public void updateLength() {
        int l = MINIMUM_LENGTH;
        if (bucketStatistics != null) {
            for (OFGroupBucketCounter counter : bucketStatistics) {
                l += counter.getLength();
            }
        }
        this.length = U16.t(l);
    }

    @Override
    public String toString() {
        return "OFGroupStatisticsReply [length=" + length + ", groupId="
                + groupId + ", refCount=" + getRefCount() + ", packetCount="
                + packetCount + ", byteCount=" + byteCount
                + ", bucketStatistics=" + bucketStatistics + "]";
    }

    @Override
    public int computeLength() {
        return getLength();
    }
}
