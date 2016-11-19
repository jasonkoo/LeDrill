package com.alimama.quanjingmonitor.mdrillImport.parse;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.alimama.mdrillImport.InvalidEntryException;

public class munion_r_taobao_com_click_data extends com.alimama.mdrillImport.DataParser{
	private static final long serialVersionUID = 1L;
	public volatile long groupCreateerror=0;

	private static Logger LOG = Logger.getLogger(munion_r_taobao_com_click_data.class);

	private volatile long lines=0;
	private volatile long lines_sb=0;

	private static long TS_MAX=3600l*24*31;

	private volatile long laststartts=System.currentTimeMillis()/1000-TS_MAX;
	private volatile long lastendts=System.currentTimeMillis()/1000+TS_MAX;
	private volatile long timediff=System.currentTimeMillis();
	@Override
	public DataIter parseLine(String line) throws InvalidEntryException {
		
		try {
			if(line==null)
			{
				return null;
			}
			
			this.lines++;
			if(this.lines>100000)
			{
				this.laststartts=(System.currentTimeMillis()/1000)-TS_MAX;
				this.lastendts=(System.currentTimeMillis()/1000)+TS_MAX;
				this.lines=0;
			}
			
			String[] clicklog=line.split("\001",-1);
			if(clicklog.length<4)
			{
				return null;
			}
			
			String[] sessionb02=clicklog[1].split("\002",-1);
			String[] sessionb03=clicklog[2].split("\002",-1);
			String[] sessionb04=clicklog[3].split("\002",-1);
			
			if(sessionb02.length<12||sessionb03.length<2||sessionb04.length<2)
			{
				return null;
			}
			
			
			if(sessionb02[0].isEmpty()||sessionb02[1].isEmpty()||sessionb02[0].length()<=5||sessionb02[1].length()>50)
			{
				return null;
			}
			
		
			
			long ts = Long.parseLong(sessionb02[0]);
			
			
			this.lines_sb++;
			if(this.lines_sb>5000)
			{
				this.lines_sb=0;
				
				long nowts=System.currentTimeMillis();
				if(nowts-timediff>30000)
				{
					timediff=nowts;
					StringBuilder b = new StringBuilder();
					for (int i = 0; i < clicklog.length; i++) {
						b.append(i);
						b.append("=");
						b.append(String.valueOf(clicklog[i]));
						b.append(",");
					}
					LOG.info("parseLine_sb_"+formatDayMin.format(new Date(ts*1000))+" "+b.toString());
				
				}
			
			}

			if(ts<laststartts||ts>lastendts)
			{
				return null;
			}
			
			
			DataIterParse rtn= new DataIterParse(ts,sessionb02);
			
			return rtn;
		} catch (Throwable nfe) {
			if(groupCreateerror<100)
			{
				LOG.error("InvalidEntryException:"+line,nfe);
				groupCreateerror++;
			}
			
			throw new InvalidEntryException("Invalid log `" + line + "'\n" , nfe);
		}
	}
	
	public static class DataIterParse implements DataIter{
		private String[] pvlog=null;
		long ts;
		public DataIterParse(long ts,String[] pvlog) {
			this.pvlog = pvlog;
			this.ts=ts;
		}

		@Override
		public boolean next() {
			return false;
		}


		@Override
		public Number[] getSum() {
			return  new Number[]{
				0
				,1
				,0
				,0
				,0
				,0
				,0
				,0
		};
		}
		
		@Override
		public long getTs() {
			 return (ts/10)*10000;
		}
		

	    //wdm_v3_user_track
		@Override
		public Object[] getGroup() {
			long ts300 = (this.ts / 300) * 300000;
			Date d = new Date(ts300);
			String channel = String.valueOf(pvlog[7]).toLowerCase();
			if (channel.indexOf("android") >= 0) {
				channel = "android";
			} else if (channel.indexOf("iphone") >= 0||channel.indexOf("ios") >= 0) {
				channel = "ios";
			} else {
				channel = "other";
			}
			return new String[] { 
					String.valueOf(formatDay.format(d)),
					String.valueOf(formatMin.format(d)),
					"wireless",
					"munion_r_taobao_com_click_data",
					"wireless",
					String.valueOf(pvlog[1]),
					channel,
					DebugVersion.version
					};

		}
	}
	


	private static String[] colname={
		"thedate"
		,"miniute_5"
		,"source"
		,"sub_source"
		,"media_name"
		,"media_pid"
		,"channel"
		,"o2o"
};

	private static String[] colSumName={
		"pv_2"
		,"click_1"
		,"click_2"
		,"promise_click"
		,"pc_2_wap"
		,"weakup"
		,"backup_1"
		,"backup_2"
};
	
	@Override
	public String[] getSumName() {
		return colSumName;

	}

	@Override
	public String getTableName() {
		return "rpt_adpmp_3_8_online";
	}
	

    private static SimpleDateFormat formatDay = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat formatMin = new SimpleDateFormat("HHmm");

    private static SimpleDateFormat formatDayMin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public String[] getGroupName() {
		return colname;
	}
}

	
