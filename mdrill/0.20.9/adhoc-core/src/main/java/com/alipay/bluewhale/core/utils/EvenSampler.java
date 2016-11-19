package com.alipay.bluewhale.core.utils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * ��������ʵ�֣��ص�����freqƵ���ڿ϶��ᱻִ��һ�Σ������ıȽϾ���
 * Ŀǰ����task���tupleͳ��
 * @author yannian
 *
 */
public class EvenSampler {
    private volatile int freq;
    private AtomicInteger i=new AtomicInteger(-1);
    private volatile int target;
    private Random r = new Random();

    public EvenSampler(int freq) {
        this.freq = freq;
        this.target = r.nextInt(freq);
    }

    public boolean getResult() {
    	i.incrementAndGet();
        if (i.get() >= freq) {
            target = r.nextInt(freq);
            i.set(0);
        }
        if (i.get()==target) {
            return true;
        }
        return false;
    }
}
