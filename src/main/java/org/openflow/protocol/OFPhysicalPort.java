package org.openflow.protocol;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Map;
import org.openflow.util.LRULinkedHashMap;

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
        OFPPS_LINK_DOWN   (1 << 0, "DOWN"),
        OFPPS_BLOCKED     (1 << 1, "BLOCKED"),
        OFPPS_LIVE        (1 << 2, "UP");

        protected int value;
        protected String name;
        protected static Map<Integer, OFPortState> mapping;

        private OFPortState(int value, String name) {
        	this.value = value;
            this.name = name;

            OFPortState.addMapping(this.value, this);
        }

        /**
         * Adds a mapping from type value to OFPortFeatures enum
         *
         * @param i state values
         * @param t type
         */
        static public void addMapping(int i, OFPortState t) {
            if (mapping == null)
                mapping = new LRULinkedHashMap<Integer, OFPortState>(10);

            mapping.put(i, t);
        }

        /**
         * Given a wire protocol OpenFlow type number, return the OFType associated
         * with it
         *
         * @param i state values
         * @return OFPortFeatures enum type
         */

        public static OFPortState valueOf(int i) {
            return mapping.get(i);
        }
        
        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Represents the speed of a port
     */
    public enum OFPortSpeed {
        SPEED_NONE(0),
        SPEED_10MB(10),
        SPEED_100MB(100),
        SPEED_1GB(1000),
        SPEED_10GB(10000),
        SPEED_40GB(40000),
        SPEED_100GB(100000),
        SPEED_1TB(1000000);

        private int speedInMps;

        private OFPortSpeed(int speedInMbps) {
            this.speedInMps = speedInMbps;
        }

        public long getSpeedInMbps() {
            return this.speedInMps;
        }

        public static OFPortSpeed max(OFPortSpeed s1, OFPortSpeed s2) {
            return (s1.getSpeedInMbps() > s2.getSpeedInMbps()) ? s1 : s2;
        }

        public static OFPortSpeed min(OFPortSpeed s1, OFPortSpeed s2) {
            return (s1.getSpeedInMbps() < s2.getSpeedInMbps()) ? s1 : s2;
        }
    }

    public enum OFPortFeatures {
        OFPPF_10MB_HD    (1 << 0, OFPortSpeed.SPEED_10MB, false, "10MB half-duplex"),
        OFPPF_10MB_FD    (1 << 1, OFPortSpeed.SPEED_10MB, true, "10MB full-duplex"),
        OFPPF_100MB_HD   (1 << 2, OFPortSpeed.SPEED_100MB, false, "100MB half-duplex"),
        OFPPF_100MB_FD   (1 << 3, OFPortSpeed.SPEED_100MB, true, "100MB full-duplex"),
        OFPPF_1GB_HD     (1 << 4, OFPortSpeed.SPEED_1GB, false, "1GB half-duplex"),
        OFPPF_1GB_FD     (1 << 5, OFPortSpeed.SPEED_1GB, true, "1GB full-duplex"),
        OFPPF_10GB_FD    (1 << 6, OFPortSpeed.SPEED_10GB, true, "10GB full-duplex"),
        OFPPF_40GB_FD    (1 << 7, OFPortSpeed.SPEED_40GB, true, "40GB full-duplex"),
        OFPPF_100GB_FD   (1 << 8, OFPortSpeed.SPEED_100GB, true, "100GB full-duplex"),
        OFPPF_1TB_FD     (1 << 9, OFPortSpeed.SPEED_1TB, true, "1TB full-duplex"),
        OFPPF_OTHER      (1 << 10, OFPortSpeed.SPEED_NONE, false, "Other"),
        OFPPF_COPPER     (1 << 11, OFPortSpeed.SPEED_NONE, false, "Copper"),
        OFPPF_FIBER      (1 << 12, OFPortSpeed.SPEED_NONE, false, "Fiber"),
        OFPPF_AUTONEG    (1 << 13, OFPortSpeed.SPEED_NONE, false, "Autoneg"),
        OFPPF_PAUSE      (1 << 14, OFPortSpeed.SPEED_NONE, false, "Pause"),
        OFPPF_PAUSE_ASYM (1 << 15, OFPortSpeed.SPEED_NONE, false, "Pause Asymm");

        protected int value;
        protected OFPortSpeed speed;
        protected boolean isFullDuplex;
        protected String name;

        private OFPortFeatures(int value, OFPortSpeed speed, boolean isFullDuplex, String name) {
            this.value = value;
            this.speed = speed;
            this.isFullDuplex = isFullDuplex;
            this.name = name;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        /**
         * @return the port speed
         */
        public OFPortSpeed getPortSpeed() {
            return speed;
        }

        /**
         * @return boolean about full duplex
         */
        public boolean isFullDuplex() {
            return isFullDuplex;
        }

        @Override
        public String toString() {
            return name;
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
