package org.openflow.protocol;

import java.util.List;

import org.openflow.protocol.multipart.OFMultipartData;
import org.openflow.util.U16;

/**
 * Represents an ofp_stats_reply message
 * @author David Erickson (daviderickson@cs.stanford.edu)
 */
public class OFMultipartReply extends OFMultipartMessageBase {
    public enum OFMultipartReplyFlags {
        REPLY_MORE      (1 << 0);

        protected short type;

        OFMultipartReplyFlags(int type) {
            this.type = (short) type;
        }

        public short getTypeValue() {
            return type;
        }
    }

    public OFMultipartReply() {
        super();
        this.type = OFType.MULTIPART_REPLY;
        this.length = U16.t(OFMultipartMessageBase.MINIMUM_LENGTH);
    }

    /**
     * @return the multipart data
     */
    public List<? extends OFMultipartData> getMultipartData() {
        if (this.multipartData == null)
            return null;
        else if (this.multipartData.size() == 0)
            return null;
        else 
            return this.multipartData;
    }
}
