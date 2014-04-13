package org.openflow.protocol;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

import org.openflow.util.U8;
import org.openflow.util.U16;
import org.openflow.util.U32;

/**
 * Represents an ofp_match structure
 * 
 * @author Srini Seetharaman (srini.seetharaman@gmail.com)
 * 
 */

public class OFMatch implements Cloneable {
    /**
     * 
     */
    public static int MINIMUM_LENGTH = 8;

    public static final int ETH_TYPE_IPV4 = 0x800;
    public static final int ETH_TYPE_ARP = 0x806;
    public static final int ETH_TYPE_VLAN = 0x8100;

    public static final int IP_PROTO_TCP = 6;
    public static final int IP_PROTO_UDP = 17;
    public static final int IP_PROTO_SCTP = 132;
    
    enum OFMatchType {
        STANDARD, OXM
    }
        
    // Note: Only supporting OXM and OpenFlow_Basic matches
    public enum OFMatchClass {
        NXM_0                ((short)0x0000),
        NXM_1                ((short)0x0001),
        OPENFLOW_BASIC       ((short)0x8000),
        VENDOR         ((short)0xffff);

        protected short value;

        private OFMatchClass(short value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public short getValue() {
            return value;
        }
    }

    protected OFMatchType type;
    protected short length;
    protected short matchLength;
    protected List<OFMatchField> matchFields;

    /**
     * By default, create a OFMatch that matches everything
     * (mostly because it's the least amount of work to make a valid OFMatch)
     */
    public OFMatch() {
        this.type = OFMatchType.OXM;
        this.length = U16.t(MINIMUM_LENGTH);
        this.matchLength = 4; //No padding
    }

    public OFMatchType getType() {
        return type;
    }

    /**
     * Get the length of this message
     * @return length
     */
    public short getLength() {
        return length;
    }

    /**
     * Get the length of this message, unsigned
     * @return
     */
    public int getLengthU() {
        return U16.f(length);
    }

    public short getMatchLength() {
        return matchLength;
    }
    
    /** Sets match field. In case of existing field, checks for existing value
     * 
     * @param matchField Check for uniqueness of field and add matchField
     */
    public void setField(OFMatchField newMatchField) {
        if (this.matchFields == null)
            this.matchFields = new ArrayList<OFMatchField>();
        for (OFMatchField matchField: this.matchFields) {
            if (matchField.getOXMFieldType() == newMatchField.getOXMFieldType()) {
                matchField.setOXMFieldValue(newMatchField.getOXMFieldValue());
                matchField.setOXMFieldMask(newMatchField.getOXMFieldMask());
                return;
            }
        }
        this.matchFields.add(newMatchField);
        this.matchLength += newMatchField.getOXMFieldLength();
        this.length = U16.t(8*((this.matchLength + 4 + 7)/8)); //includes padding
    }

    public void setField(OFOXMFieldType matchFieldType, Object matchFieldValue) {
        OFMatchField matchField = new OFMatchField(matchFieldType, matchFieldValue);
        setField(matchField);
    }

    public void setField(OFOXMFieldType matchFieldType, Object matchFieldValue, Object matchFieldMask) {
        OFMatchField matchField = new OFMatchField(matchFieldType, matchFieldValue, matchFieldMask);
        setField(matchField);
    }

    /**
     * Returns read-only copies of the matchfields contained in this OFMatch
     * @return a list of ordered OFMatchField objects
     */
    public List<OFMatchField> getMatchFields() {
        return this.matchFields;
    }

    /**
     * Sets the list of matchfields this OFMatch contains
     * @param matchFields a list of ordered OFMatchField objects
     */
    public OFMatch setMatchFields(List<OFMatchField> matchFields) {
        this.matchFields = matchFields;
        return this;
    }

    /**
     * Get value of particular field
     * @return
     */
    public Object getMatchFieldValue(OFOXMFieldType matchType) {
        for (OFMatchField matchField: matchFields) {
            if (matchField.getOXMFieldType() == matchType)
                return matchField.getOXMFieldValue();
        }
        return null;
    }

    public void readFrom(ByteBuffer data) {
        byte[] dataLayerAddress = new byte[6];
        byte[] dataLayerAddressMask = new byte[6];
        int networkAddress;
        int networkAddressMask;
        int wildcards;
        short dataLayerType = 0;
        byte networkProtocol = 0;
        short transportNumber;

        this.type = OFMatchType.values()[data.getShort()];
        this.length = data.getShort();
        int remaining = this.getLengthU() - 4; //length - sizeof(type and length)
        int end = data.position() + remaining; //includes padding in case of STANDARD match

        if (type == OFMatchType.OXM) {
            int padLength = 8*((length + 7)/8) - length;
            end += padLength; // including pad
            
            if (data.remaining() < remaining)
                remaining = data.remaining();
            this.matchFields = new ArrayList<OFMatchField>();
            while (remaining >= OFMatchField.MINIMUM_LENGTH) {
                OFMatchField matchField = new OFMatchField();
                matchField.readFrom(data);
                this.matchFields.add(matchField);
                remaining -= U32.f(matchField.getOXMFieldLength()+4); //value length + header length
            }
        } else {
            this.setField(OFOXMFieldType.IN_PORT, data.getInt());
            wildcards = data.getInt(); 

            if ((wildcards & OFMatchWildcardMask.ALL.getValue()) == 0) {
                data.position(end);
                return;
            }

            data.get(dataLayerAddress);
            data.get(dataLayerAddressMask);
            this.setField(OFOXMFieldType.ETH_SRC, dataLayerAddress.clone(), dataLayerAddressMask.clone());
        
            data.get(dataLayerAddress);
            data.get(dataLayerAddressMask);
            this.setField(OFOXMFieldType.ETH_DST, dataLayerAddress.clone(), dataLayerAddressMask.clone());

            if ((wildcards & OFMatchWildcardMask.DL_VLAN.getValue()) == 0)
                this.setField(OFOXMFieldType.VLAN_VID, data.getShort());
            else
                data.getShort(); //skip
                
            if ((wildcards & OFMatchWildcardMask.DL_VLAN_PCP.getValue()) == 0)
                this.setField(OFOXMFieldType.VLAN_PCP, data.get());
            else
                data.get(); //skip

            data.get(); //pad
            
            if ((wildcards & OFMatchWildcardMask.DL_TYPE.getValue()) == 0) {
                dataLayerType = data.getShort();
                this.setField(OFOXMFieldType.ETH_TYPE, data.getShort());
            } else
                data.getShort(); //skip

            if ((dataLayerType != ETH_TYPE_IPV4) && (dataLayerType != ETH_TYPE_ARP) && (dataLayerType != ETH_TYPE_VLAN)) {
                data.position(end);
                return;
            }
            
            if ((wildcards & OFMatchWildcardMask.NW_TOS.getValue()) == 0)
                this.setField(OFOXMFieldType.IP_DSCP, data.get());
            else
                data.get(); //skip

            if ((wildcards & OFMatchWildcardMask.NW_PROTO.getValue()) == 0) {
                networkProtocol = data.get();
                this.setField(OFOXMFieldType.IP_PROTO, networkProtocol);
            } else
                data.get(); //skip

            networkAddress = data.getInt();
            networkAddressMask = data.getInt();
            if (networkAddress != 0)
                this.setField(OFOXMFieldType.IPV4_SRC, networkAddress, networkAddressMask);

            networkAddress = data.getInt();
            networkAddressMask = data.getInt();
            if (networkAddress != 0)
                this.setField(OFOXMFieldType.IPV4_DST, networkAddress, networkAddressMask);

            if ((networkProtocol != IP_PROTO_TCP) && (networkProtocol != IP_PROTO_UDP) && (networkProtocol != IP_PROTO_SCTP)) {
                data.position(end);
                return;
            }

            transportNumber = data.getShort();
            if ((wildcards & OFMatchWildcardMask.TP_SRC.getValue()) == 0) {
                switch (transportNumber) {
                    case 6:
                        this.setField(OFOXMFieldType.TCP_SRC, data.getShort());
                        break;
                    case 17:
                        this.setField(OFOXMFieldType.UDP_SRC, data.getShort());
                        break;
                    case 132:
                        this.setField(OFOXMFieldType.SCTP_SRC, data.getShort());
                        break;
                }
            }

            transportNumber = data.getShort();
            if ((wildcards & OFMatchWildcardMask.TP_DST.getValue()) == 0) {
                switch (transportNumber) {
                    case 6:
                        this.setField(OFOXMFieldType.TCP_DST, data.getShort());
                        break;
                    case 17:
                        this.setField(OFOXMFieldType.UDP_DST, data.getShort());
                        break;
                    case 132:
                        this.setField(OFOXMFieldType.SCTP_DST, data.getShort());
                        break;
                }
            }

            if ((wildcards & OFMatchWildcardMask.MPLS_LABEL.getValue()) == 0) {
                this.setField(OFOXMFieldType.MPLS_LABEL, data.getInt());
            } else
                data.get(); //skip

            if ((wildcards & OFMatchWildcardMask.MPLS_TC.getValue()) == 0) {
                this.setField(OFOXMFieldType.MPLS_TC, data.get());
            } else
                data.get(); //skip
            
            data.get(); //pad
            data.get(); //pad
            data.get(); //pad
            
            this.setField(OFOXMFieldType.METADATA, data.getLong(), data.getLong());            
        }
        
        data.position(end); 
    }

    public void writeTo(ByteBuffer data) {
        short matchLength = getMatchLength();
        data.putShort((short)this.type.ordinal());
        data.putShort(matchLength); //length does not include padding
        for (OFMatchField matchField : matchFields) {
            matchField.writeTo(data);
        }
        int padLength = 8*((matchLength + 7)/8) - matchLength;
        for (;padLength>0;padLength--)
            data.put((byte)0); //pad
    }

    public int hashCode() {
        final int prime = 227;
        int result = 1;
        result = prime * result + ((matchFields == null) ? 0 : matchFields.hashCode());
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
        OFMatch other = (OFMatch) obj;
        if (matchFields == null) {
            if (other.matchFields != null)
                return false;
        } else if (!matchFields.equals(other.matchFields)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public OFMatch clone() {
        OFMatch match = new OFMatch();
        try {
            List<OFMatchField> neoMatchFields = new LinkedList<OFMatchField>();
            for(OFMatchField matchField: this.matchFields)
                neoMatchFields.add((OFMatchField) matchField.clone());
            match.setMatchFields(neoMatchFields);
            return match;
        } catch (CloneNotSupportedException e) {
            // Won't happen
            throw new RuntimeException(e);
        }
    }

    /**
     * Load and return a new OFMatch based on supplied packetData, see
     * {@link #loadFromPacket(byte[], short)} for details.
     * 
     * @param packetData
     * @param inputPort
     * @return
     */
    public static OFMatch load(byte[] packetData, int inPort) {
        OFMatch ofm = new OFMatch();
        return ofm.loadFromPacket(packetData, inPort);
    }

    /**
     * Initializes this OFMatch structure with the corresponding data from the
     * specified packet.
     * 
     * Must specify the input port, to ensure that this.in_port is set
     * correctly.
     * 
     * Specify OFPort.NONE or OFPort.ANY if input port not applicable or
     * available
     * 
     * @param packetData
     *            The packet's data
     * @param inputPort
     *            the port the packet arrived on
     */
    public OFMatch loadFromPacket(byte[] packetData, int inPort) {
        short scratch;
        byte[] dataLayerAddress = new byte[6];
        short dataLayerType = 0;
        byte networkProtocol = 0;

        int transportOffset = 34;
        ByteBuffer packetDataBB = ByteBuffer.wrap(packetData);
        int limit = packetDataBB.limit();

        this.setField(OFOXMFieldType.IN_PORT, inPort);

        assert (limit >= 14);
        // dl dst
        packetDataBB.get(dataLayerAddress);
        this.setField(OFOXMFieldType.ETH_DST, dataLayerAddress.clone());
        // dl src
        packetDataBB.get(dataLayerAddress);
        this.setField(OFOXMFieldType.ETH_SRC, dataLayerAddress.clone());
        // dl type
        dataLayerType = packetDataBB.getShort();
        this.setField(OFOXMFieldType.ETH_TYPE, dataLayerType);
        
        if (dataLayerType == (short) ETH_TYPE_VLAN) { // need cast to avoid signed
            // has vlan tag
            scratch = packetDataBB.getShort();
            this.setField(OFOXMFieldType.VLAN_VID, ((short) (0xfff & scratch)));
            this.setField(OFOXMFieldType.VLAN_PCP, ((byte) ((0xe000 & scratch) >> 13)));
            dataLayerType = packetDataBB.getShort();
        }

        switch (dataLayerType) {
        case ETH_TYPE_IPV4: // ipv4
            // check packet length
            scratch = packetDataBB.get();
            scratch = (short) (0xf & scratch);
            transportOffset = (packetDataBB.position() - 1) + (scratch * 4);
            // nw tos (dscp)
            scratch = packetDataBB.get();
            this.setField(OFOXMFieldType.IP_DSCP, ((byte) ((0xfc & scratch) >> 2)));
            // nw protocol
            packetDataBB.position(packetDataBB.position() + 7);
            networkProtocol = packetDataBB.get();
            this.setField(OFOXMFieldType.IP_PROTO, networkProtocol);
            // nw src
            packetDataBB.position(packetDataBB.position() + 2);
            this.setField(OFOXMFieldType.IPV4_SRC, packetDataBB.getInt());
            // nw dst
            this.setField(OFOXMFieldType.IPV4_DST, packetDataBB.getInt());
            packetDataBB.position(transportOffset);
            break;
            
        case ETH_TYPE_ARP: // arp
            int arpPos = packetDataBB.position();
            // opcode
            scratch = packetDataBB.getShort(arpPos + 6);
            this.setField(OFOXMFieldType.ARP_OP, ((short) (0xff & scratch)));

            scratch = packetDataBB.getShort(arpPos + 2);
            // if ipv4 and addr len is 4
            if (scratch == 0x800 && packetDataBB.get(arpPos + 5) == 4) {
                // nw src
                this.setField(OFOXMFieldType.ARP_SPA, packetDataBB.getInt(arpPos + 14));
                // nw dst
                this.setField(OFOXMFieldType.ARP_TPA, packetDataBB.getInt(arpPos + 24));
            } 
            return this;
            
        default: //No OXM field added
            return this;
        }

        switch (networkProtocol) {
        case 0x01: // icmp
            // type
            this.setField(OFOXMFieldType.ICMPV4_TYPE, packetDataBB.get());
            // code
            this.setField(OFOXMFieldType.ICMPV4_CODE, packetDataBB.get());
            break;
        case 0x06: // tcp
            // tcp src
            this.setField(OFOXMFieldType.TCP_SRC, packetDataBB.getShort());
            // tcp dest
            this.setField(OFOXMFieldType.TCP_DST, packetDataBB.getShort());
            break;
        case 0x11: // udp
            // udp src
            this.setField(OFOXMFieldType.UDP_SRC, packetDataBB.getShort());
            // udp dest
            this.setField(OFOXMFieldType.UDP_DST, packetDataBB.getShort());
            break;
        case (byte)0x84: // sctp
            // sctp src
            this.setField(OFOXMFieldType.SCTP_SRC, packetDataBB.getShort());
            // sctp dest
            this.setField(OFOXMFieldType.SCTP_DST, packetDataBB.getShort());
            break;
        default:
            break;
        }
        return this;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "OFMatch [type=" + type + "length=" + length + "matchFields=" + matchFields + "]";
    }
}
