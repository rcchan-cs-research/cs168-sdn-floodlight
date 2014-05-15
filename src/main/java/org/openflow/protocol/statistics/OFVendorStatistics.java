package org.openflow.protocol.statistics;

import java.nio.ByteBuffer;

/**
 * The base class for vendor implemented statistics message
 * @author David Erickson (daviderickson@cs.stanford.edu)
 * @author Srini Seetharaman (srini.seetharaman@gmail.com)
 */
public class OFVendorStatistics implements OFStatistics {
    protected static int MINIMUM_LENGTH = 8;

    protected int vendor;
    protected int expType;
    protected byte[] body;

    // non-message fields
    protected int length = 0;

    @Override
    public void readFrom(ByteBuffer data) {
        this.vendor = data.getInt();
        this.expType = data.getInt();
        if (body == null)
            body = new byte[length - MINIMUM_LENGTH];
        data.get(body);
    }

    @Override
    public void writeTo(ByteBuffer data) {
        data.putInt(this.vendor);
        data.putInt(this.expType);
        if (body != null)
            data.put(body);
    }

    @Override
    public int hashCode() {
        final int prime = 457;
        int result = 1;
        result = prime * result + vendor;
        result = prime * result + expType;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof OFVendorStatistics)) {
            return false;
        }
        OFVendorStatistics other = (OFVendorStatistics) obj;
        if (vendor != other.vendor) {
            return false;
        }
        if (expType != other.expType) {
            return false;
        }
        return true;
    }

    @Override
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public int computeLength() {
        return getLength();
    }
}
