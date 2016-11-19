####zookeeper配置####
 storm.zookeeper.servers:
     - "psb-la-vm01"
     - "psb-la-vm02"
     - "psb-la-vm03"
     - "psb-la-vm06"
 storm.zookeeper.port: 2181
 storm.zookeeper.root: "/mdrill"
 
####蓝鲸配置####
 storm.local.dir: "/data/storm"
 nimbus.host: "psb-la-vm06"
 
####hadoop配置####
 hadoop.conf.dir: "/opt/modules/hadoop/hadoop-1.2.1/conf"
 hadoop.java.opts: "-Xmx1536m"
 
 
 
 ####mdrill存储目录配置####
 higo.workdir.list: "/data/mdrill"

 #----mdrill的表格列表在hdfs下的路径-----
 higo.table.path: "/mdrill/tablelist"
 #----mdrill中启动的solr使用的初始端口号-----
 higo.solr.ports.begin: 51110
 #----mdrill分区方式，目前支持default,day,month,single，default是将一个月分成3个区,single意味着没有分区-----
 higo.partion.type: "month"
 #----创建索引生成的每个shard的并行----
 higo.index.parallel: 5
 #----启动的shard的数，每个shard为一个solr实例，结合cpu个数和内存进行配置，10台48G内存配置60----
 higo.shards.count: 20
 
 #----基于冗余的ha,设置为1表示没有冗余，如果设置为2，则冗余号位0,1----
 higo.shards.replication: 1
 #----启动的merger server的worker数量，建议根据机器数量设定----
 higo.mergeServer.count: 4
 #----mdrill同时最多加载的分区个数，取决于内存与数据量----
 higo.cache.partions: 8
 #----通用的worker的jvm参数配置----
 worker.childopts: "-Xms4g -Xmx4g -Xmn2g -XX:SurvivorRatio=3 -XX:PermSize=96m -XX:MaxPermSize=256m -XX:+UseParallelGC -XX:ParallelGCThreads=16 -XX:+UseAdaptiveSizePolicy -XX:+PrintGCDetails -XX:+PrintGCTimeStamps  -Xloggc:%storm.home%/logs/gc-%port%.log "
 #----个别的worker的端口的jvm参数配置，通常用于给merger server单独配置----

 #----任务分配，注意，原先分布在同一个位置的任务，迁移后还必须分布在一起，比如说acker:0与merger:10分布在一起了，那么迁移后他们还要在一起，为了保险起见，建议同一个端口跑多个任务的，可以像下面acker，heartbeat那样----
 mdrill.task.ports.adhoc: "6901~6902"
 mdrill.task.assignment.adhoc:
     - "####看到这里估计大家都会晕，但是这个是任务调度很重要的地方，出去透透气，回来搞定这里吧####"     
     - "####注解：merge表示是merger server;shard@0表示是shard的第0个冗余，__acker与heartbeat是内部线程，占用资源很小####"     
     - "####下面为初始分布，用于同一台机器之间没有宕机的调度####"
     - "####切记，一个端口只能分配一个shard，千万不要将merge与shard或shard与shard分配到同一个端口里，这会引起计算数据的不准确的####"
     - "merge:0&shard@0:0~4;psb-la-vm06:6601,6701~6705;6602,6711~6715;6603,6721~6725"
     - "merge:1&shard@0:5~9;psb-la-vm01:6601,6701~6705;6602,6711~6715;6603,6721~6725"
     - "merge:2&shard@0:10~14;psb-la-vm02:6601,6701~6706;6602,6711~6716;6603,6721~6726"
     - "merge:3&shard@0:15~19;psb-la-vm03:6601,6701~6705;6602,6711~6715;6603,6721~6725"
     - "__acker:0;psb-la-vm06:6601;6602;6603"
     - "heartbeat:0;psb-la-vm06:6601;6602;6603"
     - "merge:0;psb-la-vm06:6601;6602;6603" 
 
 #----mdrill个别表的分区设定，这里为rpt_seller_all_m表----
 
 higo.mode.fact_wirelesspv_clickcosteffect1_lbs_adhoc_d: "@nothedate@" 
 higo.partion.type.fact_p4ppvclick_qrycatpid_d: "day"
 higo.partion.type.rpt_seller_all_m: "month"
 higo.partion.type.r_rpt_cps_luna_item: "default"
 higo.partion.type.fact_wirelesspv_clickcosteffect1_lbs_adhoc_d: "day"
 higo.partion.type.r_rpt_cps_adhoc_seller: "default"
 higo.partion.type.rpt_seller_all_m_single: "single"
 higo.partion.type.rpt_seller_all_m_dir: "dir@thedate"
 higo.partion.type.ods_allpv_ad_d: "day"
 higo.partion.type.fact_wirelesspv_clickcosteffect1_app_adhoc_d_1: "day"
 higo.mode.fact_wirelesspv_clickcosteffect1_app_adhoc_d_1: "@nonzipout@nothedate@" 
 higo.partion.type.fact_wirelesspv_clickcosteffect1_app_adhoc_d_2: "day"
 higo.mode.fact_wirelesspv_clickcosteffect1_app_adhoc_d_2: "@nonzipout@nothedate@" 
 higo.partion.type.fact_wirelesspv_clickcosteffect1_app_adhoc_d_3: "day"
 higo.mode.fact_wirelesspv_clickcosteffect1_app_adhoc_d_3: "@nonzipout@nothedate@" 
 higo.partion.type.fact_wirelesspv_clickcosteffect1_app_adhoc_d_4: "day"
 higo.mode.fact_wirelesspv_clickcosteffect1_app_adhoc_d_4: "@nonzipout@nothedate@"
 
 #---注意只有分区类型为day的时候才能配置nothedate参数----
 higo.partion.type.fact_wirelesspv_clickcosteffect1_adhoc_d: "day"
 higo.mode.fact_wirelesspv_clickcosteffect1_adhoc_d: "@nonzipout@nothedate@"
 higo.partion.type.fact_wireless_pvcost_clickeffect_zz_adhoc_d: "day"
 higo.mode.fact_wireless_pvcost_clickeffect_zz_adhoc_d: "@nonzipout@nothedate@"  
  
 #-------------hdfs mode-----------------
 higo.partion.type.s_ods_log_p4pclick: "day"
 higo.mode.s_ods_log_p4pclick: "@fdt@nonzipout@nothedate@" 
 
 higo.mode.ods_allpv_ad_d: "@hdfs@fdt@sigment:15@blocksize:1073741824@iosortmb:256@igDataChange@" 
 higo.index.parallel.ods_allpv_ad_d: 40
 
 #-------------为个别表单独指定并行-----------------
 higo.index.parallel.r_rpt_cps_luna_item: 10


####liuhk2####
 higo.mode.feedback: "@realtime@"
 higo.partion.type.feedback: "day"
 feedback-log: "feedback"
 feedback-subid: "1226132513ZWOWEH3B"
 feedback-accesskey: "2ac04eb8-b3a4-4fb3-ab89-e8a689501b37"
 feedback-parse: "com.lenovo.lps.push.marketing.drill.feedback.FeedbackDataParser"
#### feedback-reader: "com.lenovo.lps.push.marketing.drill.test.reader.FeedbackDataReader"
 feedback-reader: "com.lenovo.lps.push.marketing.drill.reader.KafkaDataReader"
 feedback-commitbatch: 500
 feedback-commitbuffer: 1000
 feedback-spoutbuffer: 1000
 feedback-boltbuffer: 1000
 feedback-mode: "merger"
 feedback-timeoutSpout: 60
 feedback-timeoutBolt: 60
 feedback-timeoutCommit: 30
 feedback-boltIntervel: 10
 feedback-spoutIntervel: 10
 feedback-threads: 12
 feedback-threads_reduce: 12
 feedback-group_id: "feedback"
 feedback-topic_name: "feedback-topic"
 feedback-zookeeper_connect: "172.17.61.115:2181"

 mdrill.task.ports.feedbacktopology: "6902"
 mdrill.task.assignment.feedbacktopology:
     - "map_feedback:0~2;psb-la-vm06:6801~6803;6804~6806"
     - "map_feedback:3~5;psb-la-vm01:6801~6803;6804~6806"                                                                                                                                                                       
     - "map_feedback:6~8;psb-la-vm02:6801~6803;6804~6806"
     - "map_feedback:9~11;psb-la-vm03:6801~6803;6804~6806"
     - "reduce_feedback:0~2;psb-la-vm06:6801~6803;6804~6806" 
     - "reduce_feedback:3~5;psb-la-vm01:6801~6803;6804~6806"
     - "reduce_feedback:6~8;psb-la-vm02:6801~6803;6804~6806"
     - "reduce_feedback:9~11;psb-la-vm03:6801~6803;6804~6806" 
     - "map_feedbackred:0~2;psb-la-vm06:6801~6803;6804~6806"
     - "map_feedbackred:3~5;psb-la-vm01:6801~6803;6804~6806"
     - "map_feedbackred:6~8;psb-la-vm02:6801~6803;6804~6806"
     - "map_feedbackred:9~11;psb-la-vm03:6801~6803;6804~6806"
     - "reduce_feedbackred:0~2;psb-la-vm06:6801~6803;6804~6806"
     - "reduce_feedbackred:3~5;psb-la-vm01:6801~6803;6804~6806"
     - "reduce_feedbackred:6~8;psb-la-vm02:6801~6803;6804~6806"
     - "reduce_feedbackred:9~11;psb-la-vm03:6801~6803;6804~6806"
     - "map_hit:0~2;psb-la-vm06:6801~6803;6804~6806"
     - "map_hit:3~5;psb-la-vm01:6801~6803;6804~6806"                                                                                                                                                                       
     - "map_hit:6~8;psb-la-vm02:6801~6803;6804~6806"
     - "map_hit:9~11;psb-la-vm03:6801~6803;6804~6806"
     - "reduce_hit:0~2;psb-la-vm06:6801~6803;6804~6806" 
     - "reduce_hit:3~5;psb-la-vm01:6801~6803;6804~6806"
     - "reduce_hit:6~8;psb-la-vm02:6801~6803;6804~6806"
     - "reduce_hit:9~11;psb-la-vm03:6801~6803;6804~6806" 
     - "map_hitred:0~2;psb-la-vm06:6801~6803;6804~6806"
     - "map_hitred:3~5;psb-la-vm01:6801~6803;6804~6806"                                                                                                                                                                       
     - "map_hitred:6~8;psb-la-vm02:6801~6803;6804~6806"
     - "map_hitred:9~11;psb-la-vm03:6801~6803;6804~6806"
     - "reduce_hitred:0~2;psb-la-vm06:6801~6803;6804~6806" 
     - "reduce_hitred:3~5;psb-la-vm01:6801~6803;6804~6806"
     - "reduce_hitred:6~8;psb-la-vm02:6801~6803;6804~6806"
     - "reduce_hitred:9~11;psb-la-vm03:6801~6803;6804~6806" 
     - "__acker:0~2;psb-la-vm06:6801~6803;6804~6806"
     - "__acker:3~5;psb-la-vm01:6801~6803;6804~6806"  
     - "__acker:6~8;psb-la-vm02:6801~6803;6804~6806"
     - "__acker:9~11;psb-la-vm03:6801~6803;6804~6806" 

####liuhk2####
 higo.mode.feedbackred: "@realtime@cleanPartionDays:15@"
 higo.partion.type.feedbackred: "day"
 feedbackred-log: "feedbackred"
 feedbackred-subid: "1226132513ZWOWEH3B"
 feedbackred-accesskey: "2ac04eb8-b3a4-4fb3-ab89-e8a689501b37"
 feedbackred-parse: "com.lenovo.lps.push.marketing.drill.feedback.FeedbackRedDataParser"
#### feedbackred-reader: "com.lenovo.lps.push.marketing.drill.test.reader.FeedbackDataReader"
 feedbackred-reader: "com.lenovo.lps.push.marketing.drill.reader.KafkaDataReader"
 feedbackred-commitbatch: 500
 feedbackred-commitbuffer: 1000
 feedbackred-spoutbuffer: 1000
 feedbackred-boltbuffer: 1000
 feedbackred-mode: "merger"
 feedbackred-timeoutSpout: 60
 feedbackred-timeoutBolt: 60
 feedbackred-timeoutCommit: 30
 feedbackred-boltIntervel: 10
 feedbackred-spoutIntervel: 10
 feedbackred-threads: 12
 feedbackred-threads_reduce: 12
 feedbackred-group_id: "feedbackred"
 feedbackred-topic_name: "feedback-topic"
 feedbackred-zookeeper_connect: "172.17.61.115:2181"

####liuhk2####
 higo.mode.hit: "@realtime@"
 higo.partion.type.hit: "day"
 hit-log: "hit"
 hit-subid: "1226132513ZWOWEH3B"
 hit-accesskey: "2ac04eb8-b3a4-4fb3-ab89-e8a689501b37"
 hit-parse: "com.lenovo.lps.push.marketing.drill.hit.HitDataParser"
#### hit-reader: "com.lenovo.lps.push.marketing.drill.test.reader.HitDataReader"
 hit-reader: "com.lenovo.lps.push.marketing.drill.reader.KafkaDataReader"
 hit-commitbatch: 500
 hit-commitbuffer: 1000
 hit-spoutbuffer: 1000
 hit-boltbuffer: 1000
 hit-mode: "merger"
 hit-timeoutSpout: 60
 hit-timeoutBolt: 60
 hit-timeoutCommit: 30
 hit-boltIntervel: 10
 hit-spoutIntervel: 10
 hit-threads: 12
 hit-threads_reduce: 12
 hit-mode: "merger"
 hit-group_id: "hit"
 hit-topic_name: "hit-topic"
 hit-zookeeper_connect: "172.17.61.115:2181"

####liuhk2####
 higo.mode.hitred: "@realtime@cleanPartionDays:15@"
 higo.partion.type.hitred: "day"
 hitred-log: "hitred"
 hitred-subid: "1226132513ZWOWEH3B"
 hitred-accesskey: "2ac04eb8-b3a4-4fb3-ab89-e8a689501b37"
 hitred-parse: "com.lenovo.lps.push.marketing.drill.hit.HitRedDataParser"
##### hitred-reader: "com.lenovo.lps.push.marketing.drill.test.reader.HitDataReader"
 hitred-reader: "com.lenovo.lps.push.marketing.drill.reader.KafkaDataReader"
 hitred-commitbatch: 500
 hitred-commitbuffer: 1000
 hitred-spoutbuffer: 1000
 hitred-boltbuffer: 1000
 hitred-mode: "merger"
 hitred-timeoutSpout: 60
 hitred-timeoutBolt: 60
 hitred-timeoutCommit: 30
 hitred-boltIntervel: 10
 hitred-spoutIntervel: 10
 hitred-threads: 12
 hitred-threads_reduce: 12
 hitred-group_id: "hitred"
 hitred-topic_name: "hit-topic"
 hitred-zookeeper_connect: "172.17.61.115:2181"

######################################
######################################
######################################
####liuhk2####
 higo.mode.lei_test: "@realtime@"
 higo.partion.type.lei_test: "day"
 lei_test-log: "lei_test"
 lei_test-subid: "1226132513ZWOWEH3B"
 lei_test-accesskey: "2ac04eb8-b3a4-4fb3-ab89-e8a689501b37"
 lei_test-parse: "com.lenovo.push.drill.test.MyParser"
 lei_test-reader: "com.lenovo.push.drill.test.MyReader"
 lei_test-commitbatch: 5
 lei_test-commitbuffer: 100
 lei_test-spoutbuffer: 100
 lei_test-boltbuffer: 100
 lei_test-mode: "merger"
 lei_test-timeoutSpout: 60
 lei_test-timeoutBolt: 60
 lei_test-timeoutCommit: 30
 lei_test-boltIntervel: 10
 lei_test-spoutIntervel: 10
 lei_test-threads: 1
 lei_test-threads_reduce: 1
 lei_test-mode: "merger"
 
 mdrill.task.ports.leitopology: "6901"
 mdrill.task.assignment.leitopology:
     - "map_lei_test:0~2;psb-la-vm01:6801~6803;6804~6806"
     - "reduce_lei_test:0~2;psb-la-vm01:6801~6803;6804~6806"
     - "__acker:0~2;psb-la-vm01:6801~6803;6804~6806"
     - "__acker:3~5;psb-la-vm01:6801~6803;6804~6806"
######################################
######################################
######################################

 ####--实时模式相关配置--####
 #----binlog记录的位置，默认为local如果hdfs稳定可以写hdfs----
 higo.realtime.binlog: "local"
 
 #----从tt实时导入到mdrill----
 higo.partion.type.rpt_quanjing_tanxclick: "default"
 higo.partion.type.rpt_quanjing_tanxpv: "default"
 higo.partion.type.s_ods_mm_adzones: "default"
 higo.mode.s_ods_mm_adzones: "@fieldcontains:status=A@" 

 higo.mode.tanx_pv: "@realtime@cleanPartions:99@"
 higo.partion.type.tanx_pv: "day"
 tanx_pv-log: "tanx_pv"
 tanx_pv-subid: "01061423317TVV8KSN"
 tanx_pv-accesskey: "604a3330-5c1f-4488-9466-de63f08fe297"
 tanx_pv-parse: "com.alimama.quanjingmonitor.mdrillImport.parse.tanx_pv"
 tanx_pv-reader: "com.alimama.quanjingmonitor.mdrillImport.reader.TT4Reader"
 tanx_pv-commitbatch: 200
 tanx_pv-commitbuffer: 2000
 tanx_pv-spoutbuffer: 3000
 tanx_pv-boltbuffer: 500
 tanx_pv-boltIntervel: 1000
 tanx_pv-spoutIntervel: 1000
 tanx_pv-threads: 32
 tanx_pv-threads_reduce: 12
 tanx_pv-mode: "merger"
 tanx_pv-timeoutSpout: 60
 tanx_pv-timeoutBolt: 30
 tanx_pv-timeoutCommit: 100
 
 
 higo.mode.tanx_click: "@realtime@cleanPartions:99@"
 higo.partion.type.tanx_click: "day"
 
 tanx_click_zhitou-log: "tanx_click_zhitou"
 tanx_click_zhitou-subid: "0106143856X2HG2CRT"
 tanx_click_zhitou-accesskey: "9f126ea7-e728-464f-92db-8fe03aed561a"
 tanx_click_zhitou-parse: "com.alimama.quanjingmonitor.mdrillImport.parse.tanx_click_zhitou"
 tanx_click_zhitou-reader: "com.alimama.quanjingmonitor.mdrillImport.reader.TT4Reader"
 tanx_click_zhitou-commitbatch: 200
 tanx_click_zhitou-commitbuffer: 2000
 tanx_click_zhitou-spoutbuffer: 500
 tanx_click_zhitou-boltbuffer: 500
 tanx_click_zhitou-boltIntervel: 10
 tanx_click_zhitou-spoutIntervel: 100
 tanx_click_zhitou-threads: 2
 tanx_click_zhitou-threads_reduce: 12
 tanx_click_zhitou-mode: "merger"
 tanx_click_zhitou-timeoutSpout: 30
 tanx_click_zhitou-timeoutBolt: 20
 tanx_click_zhitou-timeoutCommit: 60
 
 tanx_click_rt-log: "tanx_rd_log"
 tanx_click_rt-subid: "0106144243FH80E8QP"
 tanx_click_rt-accesskey: "a53f9bb3-26c0-4ba8-9389-13057b9e5d34"
 tanx_click_rt-parse: "com.alimama.quanjingmonitor.mdrillImport.parse.tanx_click_rt"
 tanx_click_rt-reader: "com.alimama.quanjingmonitor.mdrillImport.reader.TT4Reader"
 tanx_click_rt-commitbatch: 200
 tanx_click_rt-commitbuffer: 2000
 tanx_click_rt-spoutbuffer: 500
 tanx_click_rt-boltbuffer: 500
 tanx_click_rt-boltIntervel: 10
 tanx_click_rt-spoutIntervel: 100
 tanx_click_rt-threads: 2
 tanx_click_rt-threads_reduce: 12
 tanx_click_rt-mode: "merger"
 tanx_click_rt-timeoutSpout: 30
 tanx_click_rt-timeoutBolt: 20
 tanx_click_rt-timeoutCommit: 60
 
 tanx_user_rt-log: "user_rd"
 tanx_user_rt-subid: "0106144909UBGMF3IB"
 tanx_user_rt-accesskey: "91bb5ad9-dc37-49b1-be75-8e1de52e4407"
 tanx_user_rt-parse: "com.alimama.quanjingmonitor.mdrillImport.parse.tanx_user_rt"
 tanx_user_rt-reader: "com.alimama.quanjingmonitor.mdrillImport.reader.TT4Reader"
 tanx_user_rt-commitbatch: 200
 tanx_user_rt-commitbuffer: 2000
 tanx_user_rt-spoutbuffer: 500
 tanx_user_rt-boltbuffer: 500
 tanx_user_rt-boltIntervel: 10
 tanx_user_rt-spoutIntervel: 100
 tanx_user_rt-mode: "merger"
 tanx_user_rt-threads: 2
 tanx_user_rt-threads_reduce: 12
 tanx_user_rt-timeoutSpout: 30
 tanx_user_rt-timeoutBolt: 20
 tanx_user_rt-timeoutCommit: 60
 
 
 
 mdrill.task.ports.p4p_pv2topology: "6801~6806"
 mdrill.task.assignment.p4p_pv2topology:
     - "random"
  
 ####蓝鲸各种心跳间隔配置-一般不需要改动####
 nimbus.task.timeout.secs: 30
 task.heartbeat.frequency.secs: 6
 nimbus.supervisor.timeout.secs: 300
 supervisor.heartbeat.frequency.secs: 20
 nimbus.monitor.freq.secs: 60
 supervisor.worker.timeout.secs: 600
 supervisor.monitor.frequency.secs: 90
 worker.heartbeat.frequency.secs: 30
 
 #----每台机器启动的worker进程端口列表，一般请不要改动----
 supervisor.slots.ports:
    - 6601
    - 6602
    - 6603
    - 6604
    - 6605
    - 6606
    - 6701
    - 6702
    - 6703
    - 6704
    - 6705
    - 6706
    - 6707
    - 6708
    - 6709
    - 6710
    - 6711
    - 6712
    - 6713
    - 6714
    - 6715
    - 6716
    - 6717
    - 6718
    - 6719
    - 6720
    - 6721
    - 6722
    - 6723
    - 6724
    - 6725
    - 6726
    - 6727
    - 6728
    - 6729
    - 6730
    - 6731
    - 6732
    - 6733
    - 6734
    - 6735
    - 6736
    - 6737
    - 6738
    - 6739
    - 6801
    - 6802
    - 6803
    - 6804
    - 6805
    - 6806
    - 6901
    - 6902
    


####以下为adhoc项目专用，跟mdrill无关，不启动UI的话可忽略####
 higo.ui.conf.dir: "/home/taobao/bluewhale/ui16/alimama/adhoc-core/webapp"
 higo.download.offline.conn: "jdbc:mysql://tiansuan1.kgb.cm4:3306/adhoc_download?useUnicode=true&characterEncoding=utf8"
 higo.download.offline.username: "adhoc"
 higo.download.offline.passwd: "adhoc"
 higo.download.offline.store: "/group/tbdp-etao-adhoc/p4padhoc/download/offline"
 mysql.url: "jdbc:mysql://tiansuan1.kgb.cm4:3306/query_analyser?useUnicode=true&characterEncoding=UTF-8"
 mysql.username: "adhoc"
 mysql.password: "adhoc"

 dev.name.list:
   - "木晗"
   - "子落"
   - "张壮"
   - "行列"
