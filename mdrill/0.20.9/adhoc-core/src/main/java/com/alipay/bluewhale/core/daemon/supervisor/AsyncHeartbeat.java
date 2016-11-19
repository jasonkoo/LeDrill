package com.alipay.bluewhale.core.daemon.supervisor;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import backtype.storm.Config;

import com.alipay.bluewhale.core.callback.RunnableCallback;
/**
 * ÿ��SUPERVISOR_HEARTBEAT_FREQUENCY_SECSʱ�䣬д��supervisor������
 */
class AsyncHeartbeat extends RunnableCallback {
	
    private static Logger LOG = Logger.getLogger(AsyncHeartbeat.class);

    private Map                  conf;

    private Heartbeat heartbeat;

    private AtomicBoolean     active;

    private Integer              result;

    /**
     * @param conf
     * @param heartbeat
     * @param active
     * @param activeReadLock
     */
    public AsyncHeartbeat(Map conf, Heartbeat heartbeat,
    		AtomicBoolean active) {
        this.conf = conf;
        this.heartbeat = heartbeat;
        this.active = active;
        this.result = null;
    }
    
    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public void run() {
        heartbeat.run();
        if (active.get()) {
            result = (Integer) conf
                    .get(Config.SUPERVISOR_HEARTBEAT_FREQUENCY_SECS);
        }else {
            result = -1;
        }
    }

}
