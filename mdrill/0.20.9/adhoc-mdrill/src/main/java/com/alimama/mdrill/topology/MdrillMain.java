package com.alimama.mdrill.topology;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

import com.alimama.mdrill.index.JobIndexerPartion;
import com.alimama.mdrill.utils.HadoopUtil;
import com.alipay.bluewhale.core.cluster.Cluster;
import com.alipay.bluewhale.core.cluster.ClusterState;
import com.alipay.bluewhale.core.cluster.StormClusterState;
import com.alipay.bluewhale.core.command.KillTopology;
import com.alipay.bluewhale.core.custom.CustomAssignment;
import com.alipay.bluewhale.core.utils.StormUtils;

public class MdrillMain {
	public static void main(String[] args) throws Exception {
		String type = args[0];
		if (type.equals("table")) {
			runTable(args);
			return;
		}
		if (type.equals("create")) {
			createtable(args);
			return;
		}
		if (type.equals("drop")) {
			cleartable(args);
			return;
		}
		if (type.equals("index")) {
			makeIndex(args);
			return;
		}
		
		

	}
	
	
	private static void createtable(String[] args) throws Exception {
		String tableConfig = args[1];
		String store="false";
		if(args.length>2)
		{
			store= args[2];
		}
		String stormhome = System.getProperty("storm.home");
		if (stormhome == null) {
			stormhome=".";
		}
				
		
		Map stormconf = Utils.readStormConfig();
		String hdfsSolrDir = (String) stormconf.get("higo.table.path");
		Configuration conf = getConf(stormconf);

		FileSystem fs=FileSystem.get(conf);
		FileSystem lfs=FileSystem.getLocal(conf);
		String topschema=getFileContent(lfs,new Path(stormhome,"solr/conf/schema.top.txt"));
		String downschema=getFileContent(lfs,new Path(stormhome,"solr/conf/schema.down.txt"));
		String fieldschema=getFileContent(lfs,new Path(tableConfig));
		CCJSqlParserManager parserManager = new CCJSqlParserManager();
		CreateTable createTable = (CreateTable) parserManager.parse(new StringReader(fieldschema));
		StringBuffer buffer=new StringBuffer();
		String tableName=createTable.getTable().getName();
		buffer.append("\r\n");

		for(int i=0;i<createTable.getColumnDefinitions().size();i++)
		{
			ColumnDefinition col=(ColumnDefinition)createTable.getColumnDefinitions().get(i);
			String colname=col.getColumnName();
			String[] coltype=col.getColDataType().getDataType().split("_mdrill_");
			String type=coltype[0];
			String fieldstore=store;
			if(coltype.length>1)
			{
				fieldstore=coltype[1];
			}
			if(type.equals("long")||type.equals("int")||type.equals("tint")||type.equals("bigint"))
			{
				type="tlong";
			}
			
			
			if(type.equals("float")||type.equals("double")||type.equals("tfloat"))
			{
				type="tdouble";
			}
			
			if(type.indexOf("date")>0||type.indexOf("tdate")>0||type.indexOf("time")>0)
			{
				type="tdate";
			}
			if(type.indexOf("char")>0||type.indexOf("string")>0)
			{
				type="string";
			}
			
			if(type.toLowerCase().equals("text"))
			{
				buffer.append("<field name=\""+colname+"\" type=\""+type+"\" indexed=\"true\" stored=\""+fieldstore+"\"  omitTermFreqAndPositions=\"true\" multiValued=\"true\" />");

			}else{
				buffer.append("<field name=\""+colname+"\" type=\""+type+"\" indexed=\"true\" stored=\""+fieldstore+"\"  omitTermFreqAndPositions=\"true\" />");

			}
			buffer.append("\r\n");
		}
		
		Path tablepath=new Path(hdfsSolrDir,tableName);
		Path solrpath=new Path(tablepath,"solr");
		if(!fs.exists(tablepath))
		{
			fs.mkdirs(tablepath);
		}
		if(fs.exists(solrpath))
		{
			fs.delete(solrpath,true);
		}
		
		fs.copyFromLocalFile(new Path(stormhome,"solr"), solrpath);
		
		if(fs.exists(new Path(solrpath,"conf/schema.xml")))
		{
			fs.delete(new Path(solrpath,"conf/schema.xml"),true);
		}
		
		
		
		FSDataOutputStream out=fs.create(new Path(solrpath,"conf/schema.xml"));
		java.io.BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(out));
		writer.append(topschema);
		writer.append(buffer.toString());
		writer.append(downschema);
		writer.close();
		out.close();
		
		System.out.println(buffer.toString());
		System.out.println("create succ at "+tablepath.toString());

		System.exit(0);

	}
	
	private static String getFileContent(FileSystem fs,Path f) throws IOException
	{
		StringBuffer buffer=new StringBuffer();
		FSDataInputStream in=fs.open(f);
		BufferedReader bf  = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = bf.readLine()) != null) {
			buffer.append(line);
			buffer.append("\r\n");
		}
		bf.close();
		in.close();
		
		return buffer.toString();
	}

	private static void makeIndex(String[] args) throws Exception {
		
		
		Map stormconf = Utils.readStormConfig();
		Integer shards = StormUtils.parseInt(stormconf.get("higo.shards.count"));
		String hdfsSolrDir = (String) stormconf.get("higo.table.path");
		Configuration conf = getConf(stormconf);
		
		String tablename=args[1];
		String inputdir = args[2];
		Integer maxday = Integer.parseInt(args[3]);
		String startday = args[4];
		
		String format = "seq";
		if (args.length > 5) {
			format = args[5];
		}
		String split = "";
		if (args.length > 6) {
			split = args[6];
		}
		String submatch = "*";
		if (args.length > 7) {
			submatch = args[7];
		}
		
		String rep = conf.get("dfs.replication");
		if (args.length > 8) {
			rep = args[8];
		}
		
		int dayplus=3;
		if (args.length > 9) {
			dayplus = Integer.parseInt(args[9]);;
		}
	
		Integer parallel = StormUtils.parseInt(stormconf.get("higo.index.parallel"));
		if(stormconf.containsKey("higo.index.parallel."+tablename))
		{
			parallel=StormUtils.parseInt(String.valueOf(stormconf.get("higo.index.parallel."+tablename)));
		}
		String tablemode="";

		if(stormconf.containsKey("higo.mode."+tablename))
		{
			tablemode=String.valueOf(stormconf.get("higo.mode."+tablename));
		}
		String[] jobValues = new String[]{split,submatch,String.valueOf(parallel),tablemode,rep};
		String type = (String) stormconf.get("higo.partion.type");
		String tabletype = (String) stormconf.get("higo.partion.type."+tablename);
		if(tabletype!=null&&!tabletype.isEmpty())
		{
			type=tabletype;
		}


		JobIndexerPartion index = new JobIndexerPartion(tablename,conf, shards,
				new Path(hdfsSolrDir,tablename).toString(), inputdir, dayplus, maxday, startday, format, type);
		ToolRunner.run(conf, index, jobValues);

		System.exit(0);

	}

	private static Configuration getConf(Map stormconf) {
		String hadoopConfDir = (String) stormconf.get("hadoop.conf.dir");
		String opts = (String) stormconf.get("hadoop.java.opts");
		Configuration conf = new Configuration();
		conf.set("mapred.child.java.opts", opts);
		HadoopUtil.grabConfiguration(hadoopConfDir, conf);
		return conf;
	}

	private static void runTable(String[] args) throws Exception {
		String tableName = args[1];
		Map stormconf = Utils.readStormConfig();
		ClusterState zkClusterstate = Cluster
				.mk_distributed_cluster_state(stormconf);
		StormClusterState zkCluster = Cluster
				.mk_storm_cluster_state(zkClusterstate);
		zkCluster.higo_remove(tableName);
		zkCluster.disconnect();

		Integer shards = StormUtils
				.parseInt(stormconf.get("higo.shards.count"));
		
		Integer replication = StormUtils.parseInt(stormconf.containsKey("higo.shards.replication")?stormconf.get("higo.shards.replication"):1);
		
		Integer partions = StormUtils.parseInt(stormconf
				.get("higo.cache.partions"));
		Integer portbase = StormUtils.parseInt(stormconf
				.get("higo.solr.ports.begin"));

		String hdfsSolrDir = (String) stormconf.get("higo.table.path");
		
		String realtimebinlog = String.valueOf(stormconf.get("higo.realtime.binlog"));
		

		String topologyName = "adhoc";

		String hadoopConfDir = (String) stormconf.get("hadoop.conf.dir");
		String localWorkDirList = (String) stormconf.get("higo.workdir.list");
		Integer msCount = StormUtils.parseInt(stormconf
				.get("higo.mergeServer.count"));
		Config conf = new Config();
		String[] tablelist=tableName.split(",");
		for(String tbl:tablelist)
		{
			String key="higo.mode."+tbl;
			Object val=stormconf.get(key);
			if(val!=null)
			{
				conf.put(key, val);
			}
		}

		conf.setNumWorkers(shards + msCount);
		conf.setNumAckers(1);
		conf.setMaxSpoutPending(100);
		
		List<String> assignment=(List<String>) stormconf.get(MdrillDefaultTaskAssignment.MDRILL_ASSIGNMENT_DEFAULT+"."+topologyName);
		String assignmentports=String.valueOf(stormconf.get(MdrillDefaultTaskAssignment.MDRILL_ASSIGNMENT_PORTS+"."+topologyName));
		if((assignment==null||assignment.size()==0)&&stormconf.containsKey("higo.fixed.shards"))
		{
			//兼容旧的调度
			Integer fixassign = StormUtils.parseInt(stormconf.containsKey("higo.fixed.shards")?stormconf.get("higo.fixed.shards"):0);
			conf.put(CustomAssignment.TOPOLOGY_CUSTOM_ASSIGNMENT,MdrillTaskAssignment.class.getName());
			conf.put(MdrillTaskAssignment.SHARD_REPLICATION, replication);
			conf.put(MdrillTaskAssignment.HIGO_FIX_SHARDS,fixassign);
			for(int i=1;i<=fixassign;i++)
			{
				conf.put(MdrillTaskAssignment.HIGO_FIX_SHARDS+"."+i,(String) stormconf.get("higo.fixed.shards"+"."+i));
			}
			conf.put(MdrillTaskAssignment.MS_PORTS,	(String) stormconf.get("higo.merge.ports"));
			conf.put(MdrillTaskAssignment.MS_NAME, "merge");
			conf.put(MdrillTaskAssignment.SHARD_NAME, "shard");
			conf.put(MdrillTaskAssignment.REALTIME_NAME, "realtime");

		}else{
			conf.put(CustomAssignment.TOPOLOGY_CUSTOM_ASSIGNMENT, MdrillDefaultTaskAssignment.class.getName());
			conf.put(MdrillDefaultTaskAssignment.MDRILL_ASSIGNMENT_DEFAULT, assignment);
			conf.put(MdrillDefaultTaskAssignment.MDRILL_ASSIGNMENT_PORTS, assignmentports);
		}
		BoltParams paramsMs=new BoltParams();
		paramsMs.compname="merge_0";
		paramsMs.replication=1;
		paramsMs.replicationindex=0;
		paramsMs.binlog=realtimebinlog;
		
		ShardsBolt ms = new ShardsBolt(paramsMs, hadoopConfDir, hdfsSolrDir,
				tableName, localWorkDirList, portbase + shards*replication + 100, 0,
				partions, topologyName);

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("heartbeat", new HeartBeatSpout(), 1);
		for(int i=0;i<replication;i++)
		{
			BoltParams paramsShard=new BoltParams();
			paramsShard.replication=replication;
			paramsShard.replicationindex=i;
			paramsShard.compname="shard_"+i;
			paramsShard.binlog=realtimebinlog;


			ShardsBolt solr = new ShardsBolt(paramsShard, hadoopConfDir, hdfsSolrDir,
					tableName, localWorkDirList, portbase+shards*i, shards, partions,
					topologyName);
			builder.setBolt("shard@"+i, solr, shards).allGrouping("heartbeat");
		}
		builder.setBolt("merge", ms, msCount).allGrouping("heartbeat");
		StormSubmitter.submitTopology(topologyName, conf,builder.createTopology());
		System.out.println("start complete ");
		System.exit(0);
	}
	private static void cleartable(String[] args) throws Exception {
		String tableName = args[1];
		String topologyName = "adhoc";
		String[] killargs = {topologyName};
		try {
			KillTopology.main(killargs);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map stormconf = Utils.readStormConfig();
		ClusterState zkClusterstate = Cluster
				.mk_distributed_cluster_state(stormconf);
		StormClusterState zkCluster = Cluster
				.mk_storm_cluster_state(zkClusterstate);
		zkCluster.higo_remove(topologyName);
		for (String s : tableName.split(",")) {
			zkCluster.higo_remove(s);
		}
		zkCluster.disconnect();
		System.exit(0);
	}

}
