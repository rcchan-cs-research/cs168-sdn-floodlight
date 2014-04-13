package org.openflow.protocol.factory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFType;
import org.openflow.protocol.action.OFAction;
import org.openflow.protocol.action.OFActionType;
import org.openflow.protocol.instruction.OFInstruction;
import org.openflow.protocol.instruction.OFInstructionType;
import org.openflow.protocol.queue.OFQueueProperty;
import org.openflow.protocol.queue.OFQueuePropertyType;
import org.openflow.protocol.multipart.OFMultipartData;
import org.openflow.protocol.multipart.OFMultipartDataType;
import org.openflow.protocol.hello.OFHelloElement;
import org.openflow.protocol.hello.OFHelloElementType;
import org.openflow.protocol.multipart.OFVendorStatistics;
import org.openflow.protocol.meter.OFMeterBand;
import org.openflow.protocol.meter.OFMeterBandType;


/**
 * A basic OpenFlow factory that supports naive creation of both Messages and
 * Actions.
 *
 * @author David Erickson (daviderickson@cs.stanford.edu)
 * @author Rob Sherwood (rob.sherwood@stanford.edu)
 * @author Srini Seetharaman (srini.seetharaman@gmail.com)
 *
 */
public class BasicFactory implements OFMessageFactory, OFActionFactory,
        OFQueuePropertyFactory, OFMultipartFactory,
        OFInstructionFactory, OFHelloElementFactory,
        OFMeterBandFactory {
    @Override
    public OFMessage getMessage(OFType t) {
        return t.newInstance();
    }

    @Override
    public List<OFMessage> parseMessages(ByteBuffer data) {
        return parseMessages(data, 0);
    }

    @Override
    public List<OFMessage> parseMessages(ByteBuffer data, int limit) {
        List<OFMessage> results = new ArrayList<OFMessage>();
        OFMessage demux = new OFMessage();
        OFMessage ofm;

        while (limit == 0 || results.size() <= limit) {
            if (data.remaining() < OFMessage.MINIMUM_LENGTH)
                return results;

            data.mark();
            demux.readFrom(data);
            data.reset();

            if (demux.getLengthU() > data.remaining())
                return results;

            ofm = getMessage(demux.getType());
            if (ofm instanceof OFActionFactoryAware) {
                ((OFActionFactoryAware)ofm).setActionFactory(this);
            }
            if (ofm instanceof OFMessageFactoryAware) {
                ((OFMessageFactoryAware)ofm).setMessageFactory(this);
            }
            if (ofm instanceof OFQueuePropertyFactoryAware) {
                ((OFQueuePropertyFactoryAware)ofm).setQueuePropertyFactory(this);
            }
            if (ofm instanceof OFMultipartFactoryAware) {
                ((OFMultipartFactoryAware)ofm).setMultipartFactory(this);
            }
            if (ofm instanceof OFHelloElementFactoryAware) {
                ((OFHelloElementFactoryAware)ofm).setHelloElementFactory(this);
            }
            if (ofm instanceof OFMeterBandFactoryAware) {
                ((OFMeterBandFactoryAware)ofm).setMeterBandFactory(this);
            }
            ofm.readFrom(data);
            if (OFMessage.class.equals(ofm.getClass())) {
                // advance the position for un-implemented messages
                data.position(data.position()+(ofm.getLengthU() -
                        OFMessage.MINIMUM_LENGTH));
            }
            results.add(ofm);
        }

        return results;
    }

    @Override
    public OFAction getAction(OFActionType t) {
        return t.newInstance();
    }

    @Override
    public List<OFAction> parseActions(ByteBuffer data, int length) {
        return parseActions(data, length, 0);
    }

    @Override
    public List<OFAction> parseActions(ByteBuffer data, int length, int limit) {
        List<OFAction> results = new ArrayList<OFAction>();
        OFAction demux = new OFAction();
        OFAction ofa;
        int end = data.position() + length;

        while (limit == 0 || results.size() <= limit) {
            if (data.remaining() < OFAction.MINIMUM_LENGTH ||
                    (data.position() + OFAction.MINIMUM_LENGTH) > end)
                return results;

            data.mark();
            demux.readFrom(data);
            data.reset();

            if (demux.getLengthU() > data.remaining() ||
                    (data.position() + demux.getLengthU()) > end)
                return results;

            ofa = getAction(demux.getType());
            ofa.readFrom(data);
            if (OFAction.class.equals(ofa.getClass())) {
                // advance the position for un-implemented messages
                data.position(data.position()+(ofa.getLengthU() -
                        OFAction.MINIMUM_LENGTH));
            }
            results.add(ofa);
        }

        return results;
    }

    @Override
    public OFActionFactory getActionFactory() {
        return this;
    }

    @Override
    public OFInstruction getInstruction(OFInstructionType t) {
        return t.newInstance();
    }

    @Override
    public List<OFInstruction> parseInstructions(ByteBuffer data, int length) {
        return parseInstructions(data, length, 0);
    }

    @Override
    public List<OFInstruction> parseInstructions(ByteBuffer data, int length, int limit) {
        List<OFInstruction> results = new ArrayList<OFInstruction>();
        OFInstruction demux = new OFInstruction();
        OFInstruction ofi;
        int end = data.position() + length;

        while (limit == 0 || results.size() <= limit) {
            if (data.remaining() < OFInstruction.MINIMUM_LENGTH ||
                    (data.position() + OFInstruction.MINIMUM_LENGTH) > end)
                return results;

            data.mark();
            demux.readFrom(data);
            data.reset();

            if (demux.getLengthU() > data.remaining() ||
                    (data.position() + demux.getLengthU()) > end)
                return results;

            ofi = getInstruction(demux.getType());
            ofi.readFrom(data);
            if (OFInstruction.class.equals(ofi.getClass())) {
                // advance the position for un-implemented messages
                data.position(data.position()+(ofi.getLengthU() -
                        OFInstruction.MINIMUM_LENGTH));
            }
            results.add(ofi);
        }

        return results;
    }

    @Override
    public OFInstructionFactory getInstructionFactory() {
        return this;
    }

    @Override
    public OFMultipartData getMultipartData(OFType t, OFMultipartDataType st) {
        return st.newInstance(t);
    }

    @Override
    public List<OFMultipartData> parseMultipartData(OFType t, OFMultipartDataType st,
            ByteBuffer data, int length) {
        return parseMultipartData(t, st, data, length, 0);
    }

    /**
     * @param t
     *            OFMessage type: should be one of stats_request or stats_reply
     * @param st
     *            type of this multipart message, e.g., DESC, TABLE
     * @param data
     *            buffer to read from
     * @param length
     *            length of records
     * @param limit
     *            number of records to grab; 0 == all
     * 
     * @return list of multipart records
     */

    @Override
    public List<OFMultipartData> parseMultipartData(OFType t, OFMultipartDataType st,
            ByteBuffer data, int length, int limit) {
        List<OFMultipartData> results = new ArrayList<OFMultipartData>();
        OFMultipartData multipartData = getMultipartData(t, st);

        int start = data.position();
        int count = 0;

        while (limit == 0 || results.size() <= limit) {
            // TODO Create a separate MUX/DEMUX path for vendor stats
            if (multipartData instanceof OFVendorStatistics)
                ((OFVendorStatistics)multipartData).setLength(length);

            /**
             * can't use data.remaining() here, b/c there could be other data
             * buffered past this message
             */
            if ((length - count) >= multipartData.getLength()) {
                if (multipartData instanceof OFActionFactoryAware)
                    ((OFActionFactoryAware)multipartData).setActionFactory(this);
                multipartData.readFrom(data);
                results.add(multipartData);
                count += multipartData.getLength();
                multipartData = getMultipartData(t, st);
            } else {
                if (count < length) {
                    /**
                     * Nasty case: partial/incomplete statistic found even
                     * though we have a full message. Found when NOX sent
                     * agg_stats request with wrong agg multipartData length (52
                     * instead of 56)
                     * 
                     * just throw the rest away, or we will break framing
                     */
                    data.position(start + length);
                }
                return results;
            }
        }
        return results; // empty; no multipartData at all
    }

    @Override
    public OFQueueProperty getQueueProperty(OFQueuePropertyType t) {
        return t.newInstance();
    }

    @Override
    public List<OFQueueProperty> parseQueueProperties(ByteBuffer data,
            int length) {
        return parseQueueProperties(data, length, 0);
    }

    @Override
    public List<OFQueueProperty> parseQueueProperties(ByteBuffer data,
            int length, int limit) {
        List<OFQueueProperty> results = new ArrayList<OFQueueProperty>();
        OFQueueProperty demux = new OFQueueProperty();
        OFQueueProperty ofqp;
        int end = data.position() + length;

        while (limit == 0 || results.size() <= limit) {
            if (data.remaining() < OFQueueProperty.MINIMUM_LENGTH ||
                    (data.position() + OFQueueProperty.MINIMUM_LENGTH) > end)
                return results;

            data.mark();
            demux.readFrom(data);
            data.reset();

            if (demux.getLengthU() > data.remaining() ||
                    (data.position() + demux.getLengthU()) > end)
                return results;

            ofqp = getQueueProperty(demux.getType());
            ofqp.readFrom(data);
            if (OFQueueProperty.class.equals(ofqp.getClass())) {
                // advance the position for un-implemented messages
                data.position(data.position()+(ofqp.getLengthU() -
                        OFQueueProperty.MINIMUM_LENGTH));
            }
            results.add(ofqp);
        }

        return results;
    }
    @Override
    public OFHelloElement getHelloElement(OFHelloElementType t) {
        return t.newInstance();
    }

    @Override
    public List<OFHelloElement> parseHelloElements(ByteBuffer data,
            int length) {
        return parseHelloElements(data, length, 0);
    }

    @Override
    public List<OFHelloElement> parseHelloElements(ByteBuffer data,
            int length, int limit) {
        List<OFHelloElement> results = new ArrayList<OFHelloElement>();
        OFHelloElement demux = new OFHelloElement();
        OFHelloElement ofqp;
        int end = data.position() + length;

        while (limit == 0 || results.size() <= limit) {
            if (data.remaining() < OFHelloElement.MINIMUM_LENGTH ||
                    (data.position() + OFHelloElement.MINIMUM_LENGTH) > end)
                return results;

            data.mark();
            demux.readFrom(data);
            data.reset();

            if (demux.getLengthU() > data.remaining() ||
                    (data.position() + demux.getLengthU()) > end)
                return results;

            ofqp = getHelloElement(demux.getType());
            ofqp.readFrom(data);
            if (OFHelloElement.class.equals(ofqp.getClass())) {
                // advance the position for un-implemented messages
                data.position(data.position()+(ofqp.getLengthU() -
                        OFHelloElement.MINIMUM_LENGTH));
            }
            results.add(ofqp);
        }

        return results;
    }

    @Override
    public OFMeterBand getMeterBand(OFMeterBandType t) {
        return t.newInstance();
    }

    @Override
    public List<OFMeterBand> parseMeterBands(ByteBuffer data,
            int length) {
        return parseMeterBands(data, length, 0);
    }

    @Override
    public List<OFMeterBand> parseMeterBands(ByteBuffer data,
            int length, int limit) {
        List<OFMeterBand> results = new ArrayList<OFMeterBand>();
        OFMeterBand demux = new OFMeterBand();
        OFMeterBand ofqp;
        int end = data.position() + length;

        while (limit == 0 || results.size() <= limit) {
            if (data.remaining() < OFMeterBand.MINIMUM_LENGTH ||
                    (data.position() + OFMeterBand.MINIMUM_LENGTH) > end)
                return results;

            data.mark();
            demux.readFrom(data);
            data.reset();

            if (demux.getLengthU() > data.remaining() ||
                    (data.position() + demux.getLengthU()) > end)
                return results;

            ofqp = getMeterBand(demux.getType());
            ofqp.readFrom(data);
            if (OFMeterBand.class.equals(ofqp.getClass())) {
                // advance the position for un-implemented messages
                data.position(data.position()+(ofqp.getLengthU() -
                        OFMeterBand.MINIMUM_LENGTH));
            }
            results.add(ofqp);
        }

        return results;
    }
}
