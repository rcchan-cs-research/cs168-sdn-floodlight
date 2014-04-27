package org.openflow.protocol;

import java.nio.ByteBuffer;

import org.openflow.protocol.OFOXMFieldType;

/**
 * @author Srini Seetharaman (srini.seetharaman@gmail.com)
 */
public class OFOXMField implements Cloneable {
    public static int MINIMUM_LENGTH = 4;

    protected OFOXMFieldType oxmFieldType;
    protected Object oxmFieldValue;
    protected byte oxmFieldHasMask;
    protected int oxmFieldLength;

    public OFOXMField() {
        this.oxmFieldLength = 0;
        this.oxmFieldHasMask = 0;
    }
    
    public OFOXMField(OFOXMFieldType oxmFieldType, Object oxmFieldValue) {
        this.oxmFieldType = oxmFieldType;
        this.oxmFieldValue = oxmFieldValue;
        this.oxmFieldLength = 4 + oxmFieldType.getFieldPayloadLength();
        this.oxmFieldHasMask = 0;
    }

    public OFOXMField(int oxmHeader, Object oxmFieldValue) {
        byte typeVal = (byte)((oxmHeader >> 9) & 0x7F);
        this.oxmFieldType = OFOXMFieldType.valueOf(typeVal);
        this.oxmFieldHasMask = (byte) ((oxmHeader >> 8) & 0x1);
        this.oxmFieldLength = (oxmHeader & 0xFF);
        this.oxmFieldValue = oxmFieldValue;
    }

    public OFOXMFieldType getOXMFieldType() {
        return oxmFieldType;
    }

    public int getOXMFieldHasMask() {
        return oxmFieldHasMask;
    }
    public int getOXMFieldLength() {
        return oxmFieldLength;
    }

    public Object getOXMFieldValue() {
        return oxmFieldValue;
    }

    public void setOXMFieldValue(Object oxmFieldValue) {
        this.oxmFieldValue = oxmFieldValue;
    }

    public int getOXMFieldHeader() {
        return (oxmFieldType.getFieldClass() << 16)
            | (oxmFieldType.getFieldType() << 9)
            | (oxmFieldHasMask << 8)
            | ((byte)oxmFieldType.getFieldPayloadLength());
    }

    public boolean isAllZero(Object val) {
        if (val instanceof Byte) {
        	return ((Byte)val == 0);
        }
        if (val instanceof Short) {
        	return ((Short)val== 0);
        }
        if (val instanceof Integer) {
        	return ((Integer)val == 0);
        }
        if (val instanceof Long) {
        	return ((Long)val == 0);
        }
        if (val instanceof byte[]) {
	    	for (byte b: (byte[])val)
	    		if (b != 0)
	    			return false;
	    	return true;
        }
        //TODO: error check
        return false;
    }
    

    public Object readObject(ByteBuffer data, int length)
    {
        switch (length) {
            case 1: 
                return new Byte(data.get());
            case 2: 
                return new Short(data.getShort());
            case 4: 
                return new Integer(data.getInt());
            case 8:
                return new Long(data.getLong());
            default:
                byte val[] = new byte[length];
                data.get(val, 0, length);
                return val;
        }
    }       
    
    public void readFrom(ByteBuffer data) {
        int header = data.getInt();
        short fieldClass =  (short) (header >> 16);
        byte fieldType = (byte) ((header >> 9) & 0x7f);
        
        //TODO: Sanity check the field payload length reported

        this.oxmFieldHasMask = (byte) ((header >> 8) & 1);
        this.oxmFieldLength = (header & 0xff);
        this.oxmFieldType = OFOXMFieldType.valueOf(fieldType);
        this.oxmFieldValue = readObject(data, this.oxmFieldType.getFieldPayloadLength());
    }

    public void writeObject(ByteBuffer data, Object value, int length)
    {
        switch (length) {
            case 1: 
                data.put((Byte)value);
                break;
            case 2: 
                data.putShort((Short)value);
                break;
            case 4: 
                data.putInt((Integer)value);
                break;
            case 8:
                data.putLong((Long)value);
                break;
            default:
                data.put((byte[])value, 0, length);
                break;
        }
    }       
            
    public void writeTo(ByteBuffer data) {
        data.putInt(getOXMFieldHeader());
        writeObject(data, oxmFieldValue, oxmFieldType.getFieldPayloadLength());
    }

    public int hashCode() {
        final int prime = 367;
        int result = 1;
        result = prime * result + ((oxmFieldValue == null) ? 0 : oxmFieldValue.hashCode());
        result = prime * result + getOXMFieldHeader();
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
        if (!(obj instanceof OFOXMField)) {
            return false;
        }
        OFOXMField other = (OFOXMField) obj;
        if (oxmFieldType != other.oxmFieldType) {
            return false;
        }
        if (oxmFieldLength != other.oxmFieldLength) {
            return false;
        }
        if (oxmFieldHasMask != other.oxmFieldHasMask) {
            return false;
        }
        if (!oxmFieldValue.equals(other.oxmFieldValue)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public OFOXMField clone() throws CloneNotSupportedException {
        OFOXMField oxmField = new OFOXMField();
        oxmField.setOXMFieldValue(oxmFieldValue);
        return oxmField;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OFOXMField [oxmFieldType=" + oxmFieldType + ",oxmFieldHasMask=" +
            oxmFieldHasMask + ", oxmFieldLength=" + oxmFieldLength +
            ", oxmFieldValue=" + oxmFieldValue + "]";
    }
}
