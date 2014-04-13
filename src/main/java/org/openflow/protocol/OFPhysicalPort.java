package org.openflow.protocol;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Represents ofp_phy_port
 * @author David Erickson (daviderickson@cs.stanford.edu) - Mar 25, 2010
 */
public class OFPhysicalPort {
    public static int MINIMUM_LENGTH = 64;
    public static int OFP_ETH_ALEN = 6;

    public enum OFPortConfig {
        OFPPC_PORT_DOWN    (1 << 0),
        OFPPC_NO_RECV      (1 << 2),
        OFPPC_NO_FWD       (1 << 5),
        OFPPC_NO_PACKET_IN (1 << 6);

        protected int value;

        private OFPortConfig(int value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    public enum OFPortState {
        OFPPS_LINK_DOWN   (1 << 0),
        OFPPS_BLOCKED     (1 << 1),
        OFPPS_LIVE        (1 << 2);

        protected int value;

        private OFPortState(int value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    public enum OFPortFeatures {
        OFPPF_10MB_HD    (1 << 0),
        OFPPF_10MB_FD    (1 << 1),
        OFPPF_100MB_HD   (1 << 2),
        OFPPF_100MB_FD   (1 << 3),
        OFPPF_1GB_HD     (1 << 4),
        OFPPF_1GB_FD     (1 << 5),
        OFPPF_10GB_FD    (1 << 6),
        OFPPF_40GB_FD    (1 << 7),
        OFPPF_100GB_FD   (1 << 8),
        OFPPF_1TB_FD     (1 << 9),
        OFPPF_OTHER      (1 << 10),
        OFPPF_COPPER     (1 << 11),
        OFPPF_FIBER      (1 << 12),
        OFPPF_AUTONEG    (1 << 13),
        OFPPF_PAUSE      (1 << 14),
        OFPPF_PAUSE_ASYM (1 << 15);

        protected int value;

        private OFPortFeatures(int value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    protected int portNumber;
    protected byte[] hardwareAddress;
    protected String name;
    protected int config;
    protected int state;
    protected int currentFeatures;
    protected int advertisedFeatures;
    protected int supportedFeatures;
    protected int peerFeatures;

    protected int currSpeed;
    protected int maxSpeed;
    /**
     * @return the portNumber
     */
    public int getPortNumber() {
        return portNumber;
    }

    /**
     * @param portNumber the portNumber to set
     */
    public OFPhysicalPort setPortNumber(int portNumber) {
        this.portNumber = portNumber;
        return this;
    }

    /**
     * @return the hardwareAddress
     */
    public byte[] getHardwareAddress() {
        return hardwareAddress;
    }

    /**
     * @param hardwareAddress the hardwareAddress to set
     */
    public OFPhysicalPort setHardwareAddress(byte[] hardwareAddress) {
        if (hardwareAddress.length != OFP_ETH_ALEN)
            throw new RuntimeException("Hardware address must have length "
                    + OFP_ETH_ALEN);
        this.hardwareAddress = hardwareAddress;
        return this;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public OFPhysicalPort setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return the config
     */
    public int getConfig() {
        return config;
    }

    /**
     * @param config the config to set
     */
    public OFPhysicalPort setConfig(int config) {
        this.config = config;
        return this;
    }

    /**
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public OFPhysicalPort setState(int state) {
        this.state = state;
        return this;
    }

    /**
     * @return the currentFeatures
     */
    public int getCurrentFeatures() {
        return currentFeatures;
    }

    /**
     * @param currentFeatures the currentFeatures to set
     */
    public OFPhysicalPort setCurrentFeatures(int currentFeatures) {
        this.currentFeatures = currentFeatures;
        return this;
    }

    /**
     * @return the advertisedFeatures
     */
    public int getAdvertisedFeatures() {
        return advertisedFeatures;
    }

    /**
     * @param advertisedFeatures the advertisedFeatures to set
     */
    public OFPhysicalPort setAdvertisedFeatures(int advertisedFeatures) {
        this.advertisedFeatures = advertisedFeatures;
        return this;
    }

    /**
     * @return the supportedFeatures
     */
    public int getSupportedFeatures() {
        return supportedFeatures;
    }

    /**
     * @param supportedFeatures the supportedFeatures to set
     */
    public OFPhysicalPort setSupportedFeatures(int supportedFeatures) {
        this.supportedFeatures = supportedFeatures;
        return this;
    }

    /**
     * @return the peerFeatures
     */
    public int getPeerFeatures() {
        return peerFeatures;
    }

    /**
     * @param peerFeatures the peerFeatures to set
     */
    public OFPhysicalPort setPeerFeatures(int peerFeatures) {
        this.peerFeatures = peerFeatures;
        return this;
    }

    /**
     * @return the currSpeed
     */
    public int getCurrSpeed() {
        return currSpeed;
    }

    /**
     * @param currSpeed the curr_speed to set
     */
    public OFPhysicalPort setCurrSpeed(int currSpeed) {
        this.currSpeed = currSpeed;
        return this;
    }

    /**
     * @return the maxSpeed
     */
    public int getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * @param maxSpeed the max_speed to set
     */
    public OFPhysicalPort setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    /**
     * Read this message off the wire from the specified ByteBuffer
     * @param data
     */
    public void readFrom(ByteBuffer data) {
        this.portNumber = data.getInt();
        data.position(data.position() + 4); // pad
        if (this.hardwareAddress == null)
            this.hardwareAddress = new byte[OFP_ETH_ALEN];
        data.get(this.hardwareAddress);
        data.position(data.position() + 2); // pad
        byte[] name = new byte[16];
        data.get(name);
        // find the first index of 0
        int index = 0;
        for (byte b : name) {
            if (0 == b)
                break;
            ++index;
        }
        this.name = new String(Arrays.copyOf(name, index),
                Charset.forName("ascii"));
        this.config = data.getInt();
        this.state = data.getInt();
        this.currentFeatures = data.getInt();
        this.advertisedFeatures = data.getInt();
        this.supportedFeatures = data.getInt();
        this.peerFeatures = data.getInt();
        this.currSpeed = data.getInt();
        this.maxSpeed = data.getInt();
    }

    /**
     * Write this message's binary format to the specified ByteBuffer
     * @param data
     */
    public void writeTo(ByteBuffer data) {
        data.putInt(this.portNumber);
        data.putInt(0); // pad
        data.put(hardwareAddress);
        data.putShort((short)0); // pad
        try {
            byte[] name = this.name.getBytes("ASCII");
            if (name.length < 16) {
                data.put(name);
                for (int i = name.length; i < 16; ++i) {
                    data.put((byte) 0);
                }
            } else {
                data.put(name, 0, 15);
                data.put((byte) 0);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        data.putInt(this.config);
        data.putInt(this.state);
        data.putInt(this.currentFeatures);
        data.putInt(this.advertisedFeatures);
        data.putInt(this.supportedFeatures);
        data.putInt(this.peerFeatures);
        data.putInt(this.currSpeed);
        data.putInt(this.maxSpeed);
    }

    @Override
    public int hashCode() {
        final int prime = 307;
        int result = 1;
        result = prime * result + portNumber;
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
        if (!(obj instanceof OFPhysicalPort)) {
            return false;
        }
        OFPhysicalPort other = (OFPhysicalPort) obj;
        if (portNumber != other.portNumber) {
            return false;
        }
        return true;
    }
}
