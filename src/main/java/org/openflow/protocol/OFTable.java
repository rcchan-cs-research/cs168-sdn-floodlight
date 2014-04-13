package org.openflow.protocol;

public enum OFTable {
    OFPTT_MAX                ((byte)0xfe),
    OFPTT_ALL                ((byte)0xff);

    protected byte value;

    private OFTable(byte value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public byte getValue() {
        return value;
    }
}
