package org.openflow.protocol.multipart;

import java.lang.reflect.Constructor;

import org.openflow.protocol.Instantiable;
import org.openflow.protocol.OFType;

public enum OFMultipartDataType {
    DESC        (0, OFDescriptionStatistics.class, OFDescriptionStatistics.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFDescriptionStatistics();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFDescriptionStatistics();
                        }
                    }),
    FLOW       (1, OFFlowStatisticsRequest.class, OFFlowStatisticsReply.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFFlowStatisticsRequest();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFFlowStatisticsReply();
                        }
                    }),
    AGGREGATE  (2, OFAggregateStatisticsRequest.class, OFAggregateStatisticsReply.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFAggregateStatisticsRequest();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFAggregateStatisticsReply();
                        }
                    }),
    TABLE      (3, OFTableStatistics.class, OFTableStatistics.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFTableStatistics();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFTableStatistics();
                        }
                    }),
    PORT       (4, OFPortStatisticsRequest.class, OFPortStatisticsReply.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFPortStatisticsRequest();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFPortStatisticsReply();
                        }
                    }),
    QUEUE      (5, OFQueueStatisticsRequest.class, OFQueueStatisticsReply.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFQueueStatisticsRequest();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFQueueStatisticsReply();
                        }
                    }),
/* TODO
    GROUP       (6, OFGroupStatisticsRequest.class, OFGroupStatisticsReply.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFGroupStatisticsRequest();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFGroupStatisticsReply();
                        }
                    }),

    GROUP_DESC   (6, OFGroupDescriptionStatisticsRequest.class, OFGroupDescriptionStatisticsReply.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFGroupDescriptionStatisticsRequest();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFGroupDescriptionStatisticsReply();
                        }
                    }),
    GROUP_FEATURES (7, OFGroupFeaturesStatisticsRequest.class, OFGroupFeaturesStatisticsReply.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFGroupFeaturesStatisticsRequest();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFGroupFeaturesStatisticsReply();
                        }
                    }),
    METER         (8, OFMeterStatisticsRequest.class, OFMeterStatisticsReply.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFMeterStatisticsRequest();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFMeterStatisticsReply();
                        }
                    }),

    METER_CONFIG   (9, OFMeterConfigStatisticsRequest.class, OFMeterConfigStatisticsReply.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFMeterConfigStatisticsRequest();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFMeterConfigStatisticsReply();
                        }
                    }),
    METER_FEATURES (10, OFMeterFeaturesStatisticsRequest.class, OFMeterFeaturesStatisticsReply.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFMeterFeaturesStatisticsRequest();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFMeterFeaturesStatisticsReply();
                        }
                    }),
    TABLE_FEATURES (11, OFTableFeaturesStatisticsRequest.class, OFTableFeaturesStatisticsReply.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFTableFeaturesStatisticsRequest();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFTableFeaturesStatisticsReply();
                        }
                    }),
    PORT_DESC (12, OFPortDescriptionStatisticsRequest.class, OFPortDescriptionStatisticsReply.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFPortDescriptionStatisticsRequest();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFPortDescriptionStatisticsReply();
                        }
                    }),
 */
    VENDOR     (0xffff, OFVendorStatistics.class, OFVendorStatistics.class,
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFVendorStatistics();
                        }
                    },
                    new Instantiable<OFMultipartData>() {
                        @Override
                        public OFMultipartData instantiate() {
                            return new OFVendorStatistics();
                        }
                    });

    static OFMultipartDataType[] requestMapping;
    static OFMultipartDataType[] replyMapping;

    protected Class<? extends OFMultipartData> requestClass;
    protected Constructor<? extends OFMultipartData> requestConstructor;
    protected Instantiable<OFMultipartData> requestInstantiable;
    protected Class<? extends OFMultipartData> replyClass;
    protected Constructor<? extends OFMultipartData> replyConstructor;
    protected Instantiable<OFMultipartData> replyInstantiable;
    protected short type;

    /**
     * Store some information about the OpenFlow Statistic type, including wire
     * protocol type number, and derived class
     *
     * @param type Wire protocol number associated with this OFMultipartDataType
     * @param requestClass The Statistics Java class to return when the
     *                     containing OFType is MULTIPART_REQUEST
     * @param replyClass   The Statistics Java class to return when the
     *                     containing OFType is MULTIPART_REPLY
     */
    OFMultipartDataType(int type, Class<? extends OFMultipartData> requestClass,
            Class<? extends OFMultipartData> replyClass,
            Instantiable<OFMultipartData> requestInstantiable,
            Instantiable<OFMultipartData> replyInstantiable) {
        this.type = (short) type;
        this.requestClass = requestClass;
        try {
            this.requestConstructor = requestClass.getConstructor(new Class[]{});
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failure getting constructor for class: " + requestClass, e);
        }

        this.replyClass = replyClass;
        try {
            this.replyConstructor = replyClass.getConstructor(new Class[]{});
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failure getting constructor for class: " + replyClass, e);
        }
        this.requestInstantiable = requestInstantiable;
        this.replyInstantiable = replyInstantiable;
        OFMultipartDataType.addMapping(this.type, OFType.MULTIPART_REQUEST, this);
        OFMultipartDataType.addMapping(this.type, OFType.MULTIPART_REPLY, this);
    }

    /**
     * Adds a mapping from type value to OFMultipartDataType enum
     *
     * @param i OpenFlow wire protocol type
     * @param t type of containing OFMessage, only accepts MULTIPART_REQUEST or
     *          MULTIPART_REPLY
     * @param st type
     */
    static public void addMapping(short i, OFType t, OFMultipartDataType st) {
        if (i < 0)
            i = (short) (16+i);
        if (t == OFType.MULTIPART_REQUEST) {
            if (requestMapping == null)
                requestMapping = new OFMultipartDataType[16];
            OFMultipartDataType.requestMapping[i] = st;
        } else if (t == OFType.MULTIPART_REPLY){
            if (replyMapping == null)
                replyMapping = new OFMultipartDataType[16];
            OFMultipartDataType.replyMapping[i] = st;
        } else {
            throw new RuntimeException(t.toString() + " is an invalid OFType");
        }
    }

    /**
     * Remove a mapping from type value to OFMultipartDataType enum
     *
     * @param i OpenFlow wire protocol type
     * @param t type of containing OFMessage, only accepts MULTIPART_REQUEST or
     *          MULTIPART_REPLY
     */
    static public void removeMapping(short i, OFType t) {
        if (i < 0)
            i = (short) (16+i);
        if (t == OFType.MULTIPART_REQUEST) {
            requestMapping[i] = null;
        } else if (t == OFType.MULTIPART_REPLY){
            replyMapping[i] = null;
        } else {
            throw new RuntimeException(t.toString() + " is an invalid OFType");
        }
    }

    /**
     * Given a wire protocol OpenFlow type number, return the OFMultipartDataType
     * associated with it
     *
     * @param i wire protocol number
     * @param t type of containing OFMessage, only accepts MULTIPART_REQUEST or
     *          MULTIPART_REPLY
     * @return OFMultipartDataType enum type
     */
    static public OFMultipartDataType valueOf(short i, OFType t) {
        if (i < 0)
            i = (short) (16+i);
        if (t == OFType.MULTIPART_REQUEST) {
            return requestMapping[i];
        } else if (t == OFType.MULTIPART_REPLY){
            return replyMapping[i];
        } else {
            throw new RuntimeException(t.toString() + " is an invalid OFType");
        }
    }

    /**
     * @return Returns the wire protocol value corresponding to this
     * OFMultipartDataType
     */
    public short getTypeValue() {
        return this.type;
    }

    /**
     * @param t type of containing OFMessage, only accepts MULTIPART_REQUEST or
     *          MULTIPART_REPLY
     * @return return the OFMessage subclass corresponding to this
     *                OFMultipartDataType
     */
    public Class<? extends OFMultipartData> toClass(OFType t) {
        if (t == OFType.MULTIPART_REQUEST) {
            return requestClass;
        } else if (t == OFType.MULTIPART_REPLY){
            return replyClass;
        } else {
            throw new RuntimeException(t.toString() + " is an invalid OFType");
        }
    }

    /**
     * Returns the no-argument Constructor of the implementation class for
     * this OFMultipartDataType, either request or reply based on the supplied
     * OFType
     *
     * @param t
     * @return
     */
    public Constructor<? extends OFMultipartData> getConstructor(OFType t) {
        if (t == OFType.MULTIPART_REQUEST) {
            return requestConstructor;
        } else if (t == OFType.MULTIPART_REPLY) {
            return replyConstructor;
        } else {
            throw new RuntimeException(t.toString() + " is an invalid OFType");
        }
    }

    /**
     * @return the requestInstantiable
     */
    public Instantiable<OFMultipartData> getRequestInstantiable() {
        return requestInstantiable;
    }

    /**
     * @param requestInstantiable the requestInstantiable to set
     */
    public void setRequestInstantiable(
            Instantiable<OFMultipartData> requestInstantiable) {
        this.requestInstantiable = requestInstantiable;
    }

    /**
     * @return the replyInstantiable
     */
    public Instantiable<OFMultipartData> getReplyInstantiable() {
        return replyInstantiable;
    }

    /**
     * @param replyInstantiable the replyInstantiable to set
     */
    public void setReplyInstantiable(Instantiable<OFMultipartData> replyInstantiable) {
        this.replyInstantiable = replyInstantiable;
    }

    /**
     * Returns a new instance of the implementation class for
     * this OFMultipartDataType, either request or reply based on the supplied
     * OFType
     *
     * @param t
     * @return
     */
    public OFMultipartData newInstance(OFType t) {
        if (t == OFType.MULTIPART_REQUEST) {
            return requestInstantiable.instantiate();
        } else if (t == OFType.MULTIPART_REPLY) {
            return replyInstantiable.instantiate();
        } else {
            throw new RuntimeException(t.toString() + " is an invalid OFType");
        }
    }
}
