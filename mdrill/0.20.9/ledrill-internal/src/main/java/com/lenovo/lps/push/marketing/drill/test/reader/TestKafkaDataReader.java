package com.lenovo.lps.push.marketing.drill.test.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import org.apache.log4j.Logger;

import com.alimama.mdrillImport.ImportReader;



public class TestKafkaDataReader extends ImportReader.RawDataReader {

	private long ttCnt = 0;
	private long readCnt = 0;
	private long lastLogPrintingTime = System.currentTimeMillis();

	//private static final int KAFKA_PARTITION_TOTAL = 24;
	private static Logger LOG = Logger.getLogger(TestKafkaDataReader.class); 
    private ConsumerConfig config;
    private ConsumerConnector connector;  
     

    private BlockingQueue<String> messages = new LinkedBlockingQueue<String>(10000);
    //private BlockingQueue<String> messages = new LinkedBlockingQueue<String>();
    private ExecutorService threadPool;
    //private int partitonNum = 1;
    
    private Map mdrillConfig;
    private String confPrefix;
    private int readerIndex;
	private int readerCount;
	
	private Boolean initFlag = true;
 
    
	@Override
	public void init(Map mdrillConfig, String confPrefix, int readerIndex,
			int readerCount) throws IOException {
		LOG.info("kafka reader readerIndex = " + readerIndex);
		LOG.info("kafka reader readerCount = " + readerCount);
		this.mdrillConfig = mdrillConfig;
		this.confPrefix = confPrefix;
		this.readerIndex = readerIndex;
		this.readerCount = readerCount;
		myInit();
	}

	private synchronized void myInit() throws IOException {
		if (initFlag) {
			try {
				LOG.info("kafka reader myInit");
				Properties properties = new Properties();
				properties.load(ClassLoader
						.getSystemResourceAsStream("consumer.properties"));
				String groupId = (String) mdrillConfig.get(confPrefix
						+ "-group_id");
				LOG.info("groupId=" + groupId);
				properties.setProperty("group.id", groupId);
				String zookeeperConnect = (String) mdrillConfig.get(confPrefix
						+ "-zookeeper_connect");
				properties.setProperty("zookeeper.connect", zookeeperConnect);
				LOG.info("zookeeperConnect=" + zookeeperConnect);

				this.config = new ConsumerConfig(properties);
				String topic = (String) mdrillConfig.get(confPrefix
						+ "-topic_name");
				LOG.info("topic=" + topic);

				this.connector = Consumer.createJavaConsumerConnector(config);
				Map<String, Integer> topics = new HashMap<String, Integer>();
				topics.put(topic, 2);
				Map<String, List<KafkaStream<byte[], byte[]>>> streamMap = connector
						.createMessageStreams(topics);
				List<KafkaStream<byte[], byte[]>> streamList = streamMap
						.get(topic);
				int size = streamList.size();
				LOG.info("kafka streamList size = " + size);

				threadPool = Executors.newFixedThreadPool(size);
				for (KafkaStream<byte[], byte[]> consumerStream : streamList) {
					// System.out.println("partiton:"+partition.hashCode());
					// KafkaStream<byte[], byte[]> partition =
					// partitions.get(readerIndex % size);
					threadPool.execute(new MessageRunner(consumerStream));
					// KafkaStream<byte[], byte[]> partition =
					// partitions.get(readerIndex % size);
					// threadPool.execute(new MessageRunner(partition));
					// KafkaStream<byte[], byte[]> partition2 =
					// partitions.get((readerIndex+readerCount) % size);
					// threadPool.execute(new MessageRunner(partition2));
				}
				initFlag = false;
			} catch (Exception e) {
				initFlag = true;
				LOG.error(
						"001 kafka reader cannot be initialized: "
								+ e.getMessage(), e);
				throw new IOException("kafka reader cannot be initialized");
			}
		}
	}

	@Override
	public List<Object> read() throws IOException {
		myInit();
		readCnt++;
		printlog();
		
		try {
		List<Object> list = new ArrayList<Object>();
		int count = 0;
		while(count<10000){
			String message = messages.poll();
			if(message != null){
				//LOG.info("poll message=" + message);
				//list.add(message);
				count++;
				// for log only
				ttCnt++;
			}else{
				break;
			}			
		}
		//System.out.println("return list");
		return list;
		//return null;
		} catch (Exception e) {
				initFlag = true;
				LOG.error(
						"002 kafka reader does not run any more: "
								+ e.getMessage(), e);
				throw new IOException("kafka reader does not run any more");
		}
	}

	private void printlog() {
//		if (readCnt == Long.MAX_VALUE) {
//			readCnt = 0;
//			LOG.info("kafka reader: readCnt reset");
//		}
//		if (ttCnt == Long.MAX_VALUE) {
//			ttCnt = 0;
//			LOG.info("kafka reader: ttCnt reset");
//		}
		long now = System.currentTimeMillis();
		if (now - this.lastLogPrintingTime > 5000) {
			this.lastLogPrintingTime = now;
			LOG.info("kafka reader info: [confPrefix=" + confPrefix + ", readerIndex=" + readerIndex + ", readerCount=" +readerCount+", readCnt=" + readCnt + ", ttCnt=" +ttCnt + "]");
		}
		
	}

	@Override
	public void close() throws IOException {
		connector.shutdown();  
	}

	
    public interface MessageExecutor {  
        
        public void execute(MessageAndMetadata<byte[],byte[]> item);  

    } 
    
    class MessageRunner implements Runnable{  
        private KafkaStream<byte[], byte[]> partition;  
          
        MessageRunner(KafkaStream<byte[], byte[]> partition) {  
            this.partition = partition;  
        }  
          
        public void run(){  
        	try {
            ConsumerIterator<byte[], byte[]> it = this.partition.iterator();  
            while(it.hasNext()){  
                                //connector.commitOffsets();手动提交offset,当autocommit.enable=false时使用  
                MessageAndMetadata<byte[],byte[]> item = it.next();  
                String message = new String(item.message());
                //LOG.info("add message=" + message);
                //messages.add(message);
                messages.put(message);
            }  
        	} catch (Exception e) {
					initFlag = true;
					LOG.error(
							"003 kafka reader does not run any more: "
									+ e.getMessage(), e);
        	}
        }  
    } 
}


