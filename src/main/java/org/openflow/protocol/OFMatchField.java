package org.openflow.protocol;

import java.nio.ByteBuffer;

/**
 * @author Srini Seetharaman (srini.seetharaman@gmail.com)
 */

public class OFMatchField extends OFOXMField implements Cloneable {
    protected Object mask;

    public OFMatchField() {
        super();
    }
    
    public OFMatchField(OFOXMFieldType type, Object value) {
        super(type, value);
        this.mask = null;
    }

    public OFMatchField(OFOXMFieldType type, Object value, Object mask) {
        super(type, value);
        this.length = 4 + 2 * type.getPayloadLength();
        if (mask != null)
            if (isAllZero(mask)) {
    	        this.hasMask = 1;
    	        this.mask = mask;
            }
    }

    public Object getMask() {
        return mask;
    }
    
    public void setMask(Object mask) {
        this.mask = mask;
    }

    public void readFrom(ByteBuffer data) {
        super.readFrom(data);
        if (this.hasMask == 1)
            this.mask = readObject(data, this.type.getPayloadLength());
    }

    public void writeTo(ByteBuffer data) {
        super.writeTo(data);
        if (this.hasMask == 1)
            writeObject(data, mask, type.getPayloadLength());
    }

    public int hashCode() {
        final int prime = 367;
        int result = super.hashCode();
        result = result * prime + ((mask==null)? 0: mask.hashCode());
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
        if (!(obj instanceof OFMatchField)) {
            return false;
        }
        OFMatchField other = (OFMatchField) obj;
        if (mask == null) {
            if (other.mask != null) {
                return false;
            }
        } else if (!mask.equals(other.mask)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public OFMatchField clone() throws CloneNotSupportedException {
        OFMatchField matchField = (OFMatchField)super.clone();
        matchField.setMask(mask);
        return matchField;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OFMatchField [type=" + type + ",hasMask=" +
            hasMask + ", length=" + length + ", value=" + value +
            ((hasMask == 1) ? ", mask=" + mask : "") + "]";
    }
}
