package org.openflow.protocol.factory;

import java.nio.ByteBuffer;
import java.util.List;

import org.openflow.protocol.OFType;
import org.openflow.protocol.multipart.OFMultipartData;
import org.openflow.protocol.multipart.OFMultipartDataType;


/**
 * The interface to factories used for retrieving OFMultipart instances. All
 * methods are expected to be thread-safe.
 * @author David Erickson (daviderickson@cs.stanford.edu)
 */
public interface OFMultipartFactory {
    /**
     * Retrieves an OFMultipart instance corresponding to the specified
     * OFMultipartDataType
     * @param t the type of the containing OFMessage, only accepts multipart
     *           request or reply
     * @param st the type of the OFMultipart to be retrieved
     * @return an OFMultipart instance
     */
    public OFMultipartData getMultipartData(OFType t, OFMultipartDataType st);

    /**
     * Attempts to parse and return all OFMultipart contained in the given
     * ByteBuffer, beginning at the ByteBuffer's position, and ending at
     * position+length.
     * @param t the type of the containing OFMessage, only accepts multipart
     *           request or reply
     * @param st the type of the OFMultipart to be retrieved
     * @param data the ByteBuffer to parse for OpenFlow Multipart
     * @param length the number of Bytes to examine for OpenFlow Multipart
     * @return a list of OFMultipart instances
     */
    public List<OFMultipartData> parseMultipartData(OFType t,
            OFMultipartDataType st, ByteBuffer data, int length);

    /**
     * Attempts to parse and return all OFMultipart contained in the given
     * ByteBuffer, beginning at the ByteBuffer's position, and ending at
     * position+length.
     * @param t the type of the containing OFMessage, only accepts multipart
     *           request or reply
     * @param st the type of the OFMultipart to be retrieved
     * @param data the ByteBuffer to parse for OpenFlow Multipart
     * @param length the number of Bytes to examine for OpenFlow Multipart
     * @param limit the maximum number of messages to return, 0 means no limit
     * @return a list of OFMultipart instances
     */
    public List<OFMultipartData> parseMultipartData(OFType t,
            OFMultipartDataType st, ByteBuffer data, int length, int limit);
}
