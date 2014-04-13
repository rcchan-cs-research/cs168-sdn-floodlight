package org.openflow.protocol;

import java.util.Collections;

import org.openflow.protocol.multipart.OFMultipartData;
import org.openflow.util.U16;

/**
 * Represents an ofp_stats_request message
 * @author David Erickson (daviderickson@cs.stanford.edu)
 */
public class OFMultipartRequest extends OFMultipartMessageBase {
    public enum OFMultipartRequestFlags {
        REQ_MORE      (1 << 0);

        protected short type;

        OFMultipartRequestFlags(int type) {
            this.type = (short) type;
        }

        public short getTypeValue() {
            return type;
        }
    }

    public OFMultipartRequest() {
        super();
        this.type = OFType.MULTIPART_REQUEST;
        this.length = U16.t(OFMultipartMessageBase.MINIMUM_LENGTH);
    }

    public OFMultipartData getMultipartData() {
        if (this.multipartData == null)
            return null;
        else if (this.multipartData.size() == 0)
            return null;
        else
            return this.multipartData.get(0);
    }

    /**
     * @param multipartData the multipart data to set
     */
    public OFMultipartRequest setMultipartData(OFMultipartData multipartData) {
        this.multipartData = Collections.singletonList(multipartData);
        return this;
    }
}
