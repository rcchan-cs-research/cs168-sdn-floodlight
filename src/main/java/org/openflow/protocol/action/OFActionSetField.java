package org.openflow.protocol.action;

import java.nio.ByteBuffer;

import org.openflow.protocol.OFOXMField;
import org.openflow.protocol.OFOXMFieldType;
import org.openflow.util.U8;

/**
 * Represents an ofp_action_set_action
 * @author Srini Seetharaman (srini.seetharaman@gmail.com)
 */
public class OFActionSetField extends OFAction {
    public static int MINIMUM_LENGTH = 8;

    // Should only be used when the bottom of stack bit is set,
    // else it should be 0x8847 or 0x8848
    protected OFOXMField oxmField;

    public OFActionSetField() {
        super.setType(OFActionType.SET_FIELD);
        super.setLength((short) MINIMUM_LENGTH);
    }

    public OFActionSetField(OFOXMFieldType oxmFieldType, Object oxmFieldValue) {
        super.setType(OFActionType.SET_FIELD);
        super.setLength((short) MINIMUM_LENGTH);
        
        // Disallow some pipeline fields
        if (oxmFieldType == OFOXMFieldType.IN_PORT ||
            oxmFieldType == OFOXMFieldType.IN_PHY_PORT ||
            oxmFieldType == OFOXMFieldType.METADATA ||
            oxmFieldType == OFOXMFieldType.IPV6_EXTHDR) { 
            throw new RuntimeException("Set field action disallowed for " + oxmFieldType);
        }
        this.oxmField = new OFOXMField(oxmFieldType, oxmFieldValue);
        this.length += oxmField.getOXMFieldLength();    
    }

    public OFActionSetField(OFOXMField oxmField) {
        super.setType(OFActionType.SET_FIELD);
        super.setLength((short) MINIMUM_LENGTH);
        
        // Disallow some pipeline fields
        OFOXMFieldType oxmFieldType = oxmField.getOXMFieldType();
        if (oxmFieldType == OFOXMFieldType.IN_PORT ||
                oxmFieldType == OFOXMFieldType.IN_PHY_PORT ||
                oxmFieldType == OFOXMFieldType.METADATA ||
                oxmFieldType == OFOXMFieldType.IPV6_EXTHDR) { 
                throw new RuntimeException("Set field action disallowed for " + oxmFieldType);
            }
        this.oxmField = oxmField;
        this.length += oxmField.getOXMFieldLength();
    }

    /**
     * @return the field info
     */
    public OFOXMField getOXMField() {
        return oxmField;
    }

    /**
     * @param oxmField the oxmField to set
     */
    public OFActionSetField setOXMField(OFOXMField oxmField) {
        this.oxmField = oxmField;
        return this;
    }

    @Override
    public void readFrom(ByteBuffer data) {
        super.readFrom(data);
        this.oxmField.readFrom(data);
        int padLength = length - this.oxmField.getOXMFieldLength();
        data.position(data.position() + padLength); // pad
    }

    @Override
    public void writeTo(ByteBuffer data) {
        super.writeTo(data);
        oxmField.writeTo(data);
        int padLength = length - this.oxmField.getOXMFieldLength();
        for (;padLength>0;padLength--)
            data.get(); //pad
    }

    @Override
    public int hashCode() {
        final int prime = 383;
        int result = super.hashCode();
        result = prime * result + oxmField.hashCode();
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
        if (!(obj instanceof OFActionSetField)) {
            return false;
        }
        OFActionSetField other = (OFActionSetField) obj;
        if (!oxmField.equals(other.oxmField)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OFActionSetField [oxmField=" + oxmField + "]";
    }
}
