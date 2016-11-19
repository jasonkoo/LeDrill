package com.alipay.bluewhale.core.zilch;
import org.apache.log4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.alipay.bluewhale.core.utils.StormUtils;
/**
 * zeromq �Ļ�����װ
 * @author yannian
 *
 */
public class ZeroMq {
    private static Logger LOG = Logger.getLogger(ZeroMq.class);

    public static Context context(int threads) {
        try {
            return ZMQ.context(threads);
        } catch (UnsatisfiedLinkError e) {
            LOG.error("context", e);
        }
        return null;
    }

    public static int sndmore = ZMQ.SNDMORE;
    public static int req = ZMQ.REQ;
    public static int rep = ZMQ.REP;
    public static int xreq = ZMQ.XREQ;
    public static int xrep = ZMQ.XREP;
    public static int pub = ZMQ.PUB;
    public static int sub = ZMQ.SUB;
    public static int pair = ZMQ.PAIR;
    public static int push = ZMQ.PUSH;
    public static int pull = ZMQ.PULL;

    public static byte[] barr(Short v) {
        return StormUtils.barr(v);
    }

    public static byte[] barr(Integer v) {
        return StormUtils.barr(v);
    }

    public static Socket socket(Context context, int type) {
        return context.socket(type);
    }

    public static Socket set_linger(Socket socket, long linger_ms) {
        socket.setLinger(linger_ms);
        return socket;
    }

    public static Socket set_hwm(Socket socket, long hwm) {
        socket.setHWM(hwm);
        return socket;
    }

    public static Socket bind(Socket socket, String url) {
        socket.bind(url);
        return socket;
    }

    public static Socket connect(Socket socket, String url) {
        socket.connect(url);
        return socket;
    }

    public static Socket subscribe(Socket socket, byte[] topic) {
        socket.subscribe(topic);
        return socket;
    }

    public static Socket subscribe(Socket socket) {
        byte[] topic = {};
        return subscribe(socket, topic);
    }

    public static Socket unsubscribe(Socket socket, byte[] topic) {
        socket.unsubscribe(topic);
        return socket;
    }

    public static Socket unsubscribe(Socket socket) {
        byte[] topic = {};
        return unsubscribe(socket, topic);
    }

    public static Socket send(Socket socket, byte[] message, int flags) {
        socket.send(message, flags);
        return socket;
    }

    public static Socket send(Socket socket, byte[] message) {
        return send(socket, message, ZMQ.NOBLOCK);
    }

    public static byte[] recv(Socket socket, int flags) {
        return socket.recv(flags);
    }

    public static byte[] recv(Socket socket) {
        return recv(socket, 0);
    }

}
