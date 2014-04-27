package org.openflow.protocol;

import java.nio.ByteBuffer;

/**
 * @author Srini Seetharaman (srini.seetharaman@gmail.com)
 */

public class OFMatchField extends OFOXMField implements Cloneable {
    protected Object oxmFieldMask;

    public OFMatchField() {
        super();
    }
    
    public OFMatchField(OFOXMFieldType oxmFieldType, Object oxmFieldValue) {
        super(oxmFieldType, oxmFieldValue);
        this.oxmFieldMask = null;
    }

    public OFMatchField(OFOXMFieldType oxmFieldType, Object oxmFieldValue, Object oxmFieldMask) {
        super(oxmFieldType, oxmFieldValue);
        this.oxmFieldLength = 4 + 2 * oxmFieldType.getFieldPayloadLength();
        if (isAllZero(oxmFieldMask)) {
	        this.oxmFieldHasMask = 1;
	        this.oxmFieldMask = oxmFieldMask;
        }
    }

    public Object getOXMFieldMask() {
        return oxmFieldMask;
    }
    
    public void setOXMFieldMask(Object oxmFieldMask) {
        this.oxmFieldMask = oxmFieldMask;
    }

    public void readFrom(ByteBuffer data) {
        super.readFrom(data);
        if (this.oxmFieldHasMask == 1)
            this.oxmFieldMask = readObject(data, this.oxmFieldType.getFieldPayloadLength());
    }

    public void writeTo(ByteBuffer data) {
        super.writeTo(data);
        if (this.oxmFieldHasMask == 1)
            writeObject(data, oxmFieldMask, oxmFieldType.getFieldPayloadLength());
    }

    public int hashCode() {
        final int prime = 367;
        int result = super.hashCode();
        result = result * prime + ((oxmFieldMask==null)? 0: oxmFieldMask.hashCode());
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
        if (oxmFieldMask == null) {
            if (other.oxmFieldMask != null) {
                return false;
            }
        } else if (!oxmFieldMask.equals(other.oxmFieldMask)) {
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
        matchField.setOXMFieldMask(oxmFieldMask);
        return matchField;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OFMatchField [oxmFieldType=" + oxmFieldType + ",oxmFieldHasMask=" +
            oxmFieldHasMask + ", oxmFieldLength=" + oxmFieldLength + ", oxmFieldValue=" + oxmFieldValue +
            ((oxmFieldHasMask == 1) ? ", oxmFieldMask=" + oxmFieldMask : "") + "]";
    }
}
