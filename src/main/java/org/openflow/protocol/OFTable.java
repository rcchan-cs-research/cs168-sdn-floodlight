package org.openflow.protocol;

public enum OFTable {
    OFTT_MAX                ((byte)0xfe),
    OFTT_ALL                ((byte)0xff);

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
