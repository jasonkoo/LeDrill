package com.alipay.bluewhale.core.zk;

import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public interface WatcherCallBack  {
	public void execute(KeeperState state,EventType type,String path);
}
