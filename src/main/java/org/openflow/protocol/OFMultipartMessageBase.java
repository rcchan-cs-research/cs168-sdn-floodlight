package org.openflow.protocol;

import java.nio.ByteBuffer;
import java.util.List;

import org.openflow.protocol.factory.OFMultipartFactory;
import org.openflow.protocol.factory.OFMultipartFactoryAware;
import org.openflow.protocol.multipart.OFMultipartData;
import org.openflow.protocol.multipart.OFMultipartDataType;
import org.openflow.util.U16;


/**
 * Base class for multipart messages (including statistics requests/replies)
 *
 * @author David Erickson (daviderickson@cs.stanford.edu) - Mar 27, 2010
 */
public abstract class OFMultipartMessageBase extends OFMessage implements
        OFMultipartFactoryAware {
    public static int MINIMUM_LENGTH = 16;

    protected OFMultipartFactory multipartFactory;
    protected OFMultipartDataType multipartDataType;
    protected short flags;
    protected List<? extends OFMultipartData> multipartData;

    /**
     * Construct a ofp_multipart_* message
     */
    public OFMultipartMessageBase() {
        super();
        this.length = U16.t(MINIMUM_LENGTH);
    }

    /**
     * @return the multipartDataType
     */
    public OFMultipartDataType getMultipartDataType() {
        return multipartDataType;
    }

    /**
     * @param multipartDataType the multipartDataType to set
     */
    public OFMultipartMessageBase setMultipartDataType(OFMultipartDataType multipartDataType) {
        this.multipartDataType = multipartDataType;
        return this;
    }

    /**
     * @return the flags
     */
    public short getFlags() {
        return flags;
    }

    /**
     * @param flags the flags to set
     */
    public OFMultipartMessageBase setFlags(short flags) {
        this.flags = flags;
        return this;
    }

    public OFMultipartData getFirstMultipartData() {
        if (multipartData == null ) {
            throw new RuntimeException("No multipart statistics data available");
        }
        if (multipartData.size() == 0) {
            throw new RuntimeException("No multipart statistics data available");
        }
        return multipartData.get(0);
    }

    /**
     * @param multipartData the multipartData to set
     */
    public void setMultipartData(List<? extends OFMultipartData> multipartData) {
        this.multipartData = multipartData;
    }

    @Override
    public void setMultipartFactory(OFMultipartFactory multipartFactory) {
        this.multipartFactory = multipartFactory;
    }

    @Override
    public void readFrom(ByteBuffer data) {
        super.readFrom(data);
        this.multipartDataType = OFMultipartDataType.valueOf(data.getShort(), this
                .getType());
        this.flags = data.getShort();
        data.getInt(); //pad
        if (this.multipartFactory == null)
            throw new RuntimeException("OFMultipartFactory not set");
        this.multipartData = multipartFactory.parseMultipartData(this.getType(),
                this.multipartDataType, data, super.getLengthU() - MINIMUM_LENGTH);
    }

    @Override
    public void writeTo(ByteBuffer data) {
        super.writeTo(data);
        data.putShort(this.multipartDataType.getTypeValue());
        data.putShort(this.flags);
        data.putInt(0); //pad
        if (this.multipartData != null) {
            for (OFMultipartData statistic : this.multipartData) {
                statistic.writeTo(data);
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 317;
        int result = super.hashCode();
        result = prime * result + flags;
        result = prime * result
                + ((multipartDataType == null) ? 0 : multipartDataType.hashCode());
        result = prime * result
                + ((multipartData == null) ? 0 : multipartData.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof OFMultipartMessageBase)) {
            return false;
        }
        OFMultipartMessageBase other = (OFMultipartMessageBase) obj;
        if (flags != other.flags) {
            return false;
        }
        if (multipartDataType == null) {
            if (other.multipartDataType != null) {
                return false;
            }
        } else if (!multipartDataType.equals(other.multipartDataType)) {
            return false;
        }
        if (multipartData == null) {
            if (other.multipartData != null) {
                return false;
            }
        } else if (!multipartData.equals(other.multipartData)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OFMultipartMessage [type=" + type + ", flags=" + flags + 
                ", data=" + multipartData + "]";
    }

    /* (non-Javadoc)
     * @see org.openflow.protocol.OFMessage#computeLength()
     */
    @Override
    public void computeLength() {
        int l = MINIMUM_LENGTH;
        if (multipartData != null) {
            for (OFMultipartData stat : multipartData) {
                l += stat.computeLength();
            }
        }
        this.length = U16.t(l);
    }
}
