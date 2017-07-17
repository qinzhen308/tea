package com.becdoor.teanotes.until;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.becdoor.teanotes.DJApplication;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


/**
 * Created with IntelliJ IDEA. User: kait Date: 4/2/13 Time: 11:39 AM To change
 * this template use File | Settings | File Templates.
 */
public class DateUtil {

	public static final int TIME_UNIT_SECOND = 1;
	public static final int TIME_UNIT_MINUTE = 2;
	public static final int TIME_UNIT_HOUR = 3;
	public static final int TIME_UNIT_DAY = 4;

	// date
	public static final long MINUTE = 1000 * 60;
	public static final long HALF_HOUR = 30 * MINUTE;
	public static final long HOUR = 2 * HALF_HOUR;
	public static final long DAY = 24 * HOUR;
	public static final long WEEK = 7 * DAY;

	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	static {
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
	}

	public static SimpleDateFormat simpleDayDateFormat = new SimpleDateFormat(
			"yyyyMMdd");
	public static SimpleDateFormat simpleMonthDayFormat = new SimpleDateFormat(
			"MM-dd");
	public static SimpleDateFormat simpleMDHSFormat = new SimpleDateFormat(
			"MM-dd HH:mm");
	public static SimpleDateFormat simpleHSFormat = new SimpleDateFormat(
			"HH:mm");
	public static SimpleDateFormat simpleYHMDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static SimpleDateFormat utcDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ");

	public static int getDayCountOfYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}

	public static String getYearConcatDay() {
		Calendar calendar = Calendar.getInstance();
		DecimalFormat df = new DecimalFormat("000");
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		return String.valueOf(calendar.get(Calendar.YEAR)) + df.format(day);
	}

	// 1970.1.1 到现在的天数
	public static int getDayCount() {
		Calendar cal = Calendar.getInstance();
		cal.set(1970, 0, 1, 0, 0, 0);
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		long intervalMilli = now.getTimeInMillis() - cal.getTimeInMillis();
		return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	}

	public static String getYearConcatMonthDay() {
		Calendar calendar = Calendar.getInstance();
		DecimalFormat df = new DecimalFormat("00");
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return String.valueOf(calendar.get(Calendar.YEAR)) + df.format(month)
				+ df.format(day);
	}

	// 比当前时间晚多少小时
	public static boolean afterNowBetweenHours(String source, int hours) {
		Date now = new Date();
		Date sourceDate = null;
		try {
			sourceDate = simpleDateFormat.parse(source);
		} catch (ParseException e) {
		}
		if (sourceDate != null && sourceDate.getTime() > now.getTime()
				&& sourceDate.getTime() - HOUR * hours <= now.getTime()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean afterNow(String source) {
		Date now = new Date();
		Date sourceDate = null;
		try {
			sourceDate = simpleDateFormat.parse(source);
		} catch (ParseException e) {
		}

		if (sourceDate != null && sourceDate.getTime() < now.getTime()) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean beforeNow(String source) {
		try {
			Date date = simpleDateFormat.parse(source);
			return date.before(new Date());
		} catch (ParseException e) {
		}
		return false;
	}

	public static boolean afterOneDay(int dayTime) {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_YEAR);

		if (day != dayTime) {
			return true;
		} else {
			return false;
		}
	}

	public static int getPromotionHour(String source) {
		// Calendar calendar = Calendar.getInstance();
		// try {
		// calendar.setTime(simpleDateFormat.parse(source));
		// return calendar.get(Calendar.HOUR_OF_DAY);
		// } catch (Exception e) {
		// LogUtil.w(e);
		// }
		//
		// return calendar.get(Calendar.HOUR_OF_DAY);
		try {
			return Integer.valueOf(source.substring(11, 13));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean dateCompare(String newTime, String oldTime) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

		try {
			Date date = simpleDateFormat.parse(newTime);
			return date.getTime() > Long.parseLong(oldTime);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}

	// 判断是否是当天
	public static boolean isCurrentDay(String timeSource) {
		try {
			if (!TextUtils.isEmpty(timeSource) && !"-1".equals(timeSource)) {
				return simpleDayDateFormat.format(new Date().getTime()).equals(
						simpleDayDateFormat.format(new Date(Long
								.parseLong(timeSource))));
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	// 判断是否是同一天
	public static boolean isSameDay(String timeSource, String timeSource2) {
		try {
			if (!TextUtils.isEmpty(timeSource) && !"-1".equals(timeSource)
					&& !TextUtils.isEmpty(timeSource2)
					&& !"-1".equals(timeSource2)) {
				return simpleDayDateFormat.format(
						simpleDateFormat.parse(timeSource)).equals(
						simpleDayDateFormat.format(simpleDateFormat
								.parse(timeSource2)));
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	// 返回秒
	public static long getCommonTime(String source) {
		Long dis = 0L;
		Date souceDate;
		try {
			souceDate = simpleDateFormat.parse(source);
			dis = souceDate.getTime() / 1000;
		} catch (ParseException e) {
			// nothing
		}
		return dis;
	}

	// 返回秒
	public static long getCommonTime2(String source) {
		Long dis = 0L;
		Date souceDate;
		try {
			souceDate = simpleDateFormat.parse(source);
			dis = souceDate.getTime() / 1000;
		} catch (ParseException e) {
			// nothing
		}

		return dis;
	}

	public static boolean isNowDay(String longTime) {
		if (StringUtil.isNull(longTime)) {
			return false;
		}

		long timeSource = Long.valueOf(longTime);
		return isNowDay(timeSource);
	}

	public static boolean isNowDay(long longTime) {
		if (longTime <= 0)
			return false;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(longTime);

		return calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance()
				.get(Calendar.DAY_OF_YEAR);
	}

	public static long getTimeMillis(String source) {
		Date date = new Date();
		try {
			date = simpleDateFormat.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}

	public static long getTimeMillis(String pattern, String source) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = new Date();
		try {
			date = format.parse(source);
		} catch (ParseException e) {

		}
		return date.getTime();
	}

	public static String getTime(String pattern, Date date) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	
	public static String getTimeUnitSecond(String pattern, long secondTime) {
		Date date=new Date(secondTime*1000);
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}
	
	public static String getTimeUnitSecond(SimpleDateFormat simpleDateFormat, long secondTime) {
		Date date=new Date(secondTime*1000);
		return simpleDateFormat.format(date);
	}
	
	public static String getYMDTimeUnitSecond(long secondTime) {
		return getTimeUnitSecond(simpleYHMDateFormat, secondTime);
	}
	

	public static long getExpeiresTime(String source) {
		Long dis = 0L;
		Date now = new Date();
		Date souceDate;
		try {
			souceDate = simpleDateFormat.parse(source);
			dis = souceDate.getTime() / 1000;
		} catch (ParseException e) {
			// nothing
		}

		return dis;
	}

	// 与当前时间比较差
	public static long getDValueTime(String source) {
		Long dis = 0L;
		Date now = new Date();
		Date souceDate;
		try {
			souceDate = simpleDateFormat.parse(source);
			dis = souceDate.getTime() - now.getTime();
		} catch (ParseException e) {
		}

		return dis;
	}

	// 和服务器时间的时间比较差
	public static long getDValueTimeBetTwoTime(String sourceTime,
			long sourceServer) {
		Long dis = 0L;
		Date sourceDateTime;
		try {
			sourceDateTime = simpleDateFormat.parse(sourceTime);
			dis = sourceDateTime.getTime() - sourceServer;
		} catch (ParseException e) {
		}

		return dis;
	}

	public static String getMDHSTime(String source) {
		try {
			Date date = simpleDateFormat.parse(source);
			return simpleMDHSFormat.format(date);
		} catch (ParseException e) {
		}
		return source;
	}

	public static String getMDHSTime(long source) {

		return simpleMDHSFormat.format(new Date(source));

	}

	public static String getYMDTime(long source) {
		return simpleYHMDateFormat.format(new Date(source));

	}

	public static String getHSTime(long source) {

		return simpleHSFormat.format(new Date(source));

	}

	public static String getImYMDHMMTime(long source) {

		return simpleDateFormat.format(new Date(source));

	}

	// SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy年M月d日");
	public static long getRestTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		return calendar.getTimeInMillis() - System.currentTimeMillis();
	}

	public static String getCurrentTime() {
		return simpleDateFormat.format(new Date());
	}

	public static String getYMDDate(Date date) {
		return simpleYHMDateFormat.format(date);
	}

	public static String getYMDDate(String source) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy年M月d日");
		Date date = new Date();
		try {
			date = simpleYHMDateFormat.parse(source);
		} catch (ParseException e) {
		}
		return simpleFormat.format(date);
	}




	public static String getDayLeftTime(Context context, long time) {
		time /= 1000;
		long s = time % 60;
		String ss;

		if (s < 10) {
			ss = "0" + s;
		} else {
			ss = "" + s;
		}

		time /= 60;
		long m = time % 60;
		String sm;
		if (m < 10) {
			sm = "0" + m;
		} else {
			sm = "" + m;
		}

		time /= 60;
		long h = time % 24;
		String sh;
		if (h < 10) {
			sh = "0" + h;
		} else {
			sh = "" + h;
		}

		time /= 24;
		long d = time;
		String sd;
		if (d < 10) {
			sd = "0" + d;
		} else {
			sd = "" + d;
		}

		return sd + ":" + sh + ":" + sm + ":" + ss;
	}


	public static int getNowYear() {
		// 获取当前年份
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	public static int getNowMonth() {
		// 获取当前年份
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH);
	}


	private static DateResult getMyDayDistance(String timeSource) {
		Calendar today = Calendar.getInstance();
		Calendar old = Calendar.getInstance();
		int xcs = 0;
		try {
			Date sourceDate = simpleDateFormat.parse(timeSource);
			old.setTime(sourceDate);
			int hour = sourceDate.getHours();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			old.set(Calendar.HOUR_OF_DAY, 0);
			old.set(Calendar.MINUTE, 0);
			old.set(Calendar.SECOND, 0);
			old.set(Calendar.MILLISECOND, 0);

			long intervalMilli = old.getTimeInMillis()
					- today.getTimeInMillis();
			xcs = (int) (intervalMilli / (24 * 60 * 60 * 1000));

			if (xcs == 0) {
				return new DateResult(DateResult.DATE_RESULT_NO_SELL, "今日"
						+ hour + "点开卖");
			} else if (xcs == 1) {
				return new DateResult(DateResult.DATE_RESULT_NO_SELL, "明日"
						+ hour + "点开卖");
			} else {
				return new DateResult(DateResult.DATE_RESULT_NO_SELL, xcs
						+ "日后开卖");
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
	}

	public static DateResult getRestTimeCatgoryFengQiang() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long diff = cal.getTimeInMillis() - System.currentTimeMillis();
		long diffDay = (diff) / (24 * 60 * 60 * 1000);// 天
		long diffHours = (diff) / (60 * 60 * 1000);// 小时
		long diffMinus = (diff) / (60 * 1000);// 分
		long diffMiao = (diff) / (1000);// 秒
		if (diffMiao > 0) {
			String hours = "";
			if (diffHours % 24 >= 10) {
				hours = diffHours % 24 + "";
			} else {
				hours = "0" + diffHours % 24;
			}

			String min = "";
			if (diffMinus % 60 >= 10) {
				min = diffMinus % 60 + "";
			} else {
				min = "0" + diffMinus % 60;
			}

			String mao = "";
			if (diffMiao % 60 >= 10) {
				mao = diffMiao % 60 + "";
			} else {
				mao = "0" + diffMiao % 60;
			}
			return new DateResult(DateResult.DATE_RESULT_LAST_DAY, diffDay
					+ "," + hours + "," + min + "," + mao + "");
		} else {
			return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
		}

	}



	private static DateResult getDayDistance(String timeSource) {
		Calendar today = Calendar.getInstance();
		Calendar old = Calendar.getInstance();
		int xcs = 0;
		try {
			Date sourceDate = simpleDateFormat.parse(timeSource);
			old.setTime(sourceDate);
			int hour = sourceDate.getHours();

			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			today.set(Calendar.MILLISECOND, 0);
			old.set(Calendar.HOUR_OF_DAY, 0);
			old.set(Calendar.MINUTE, 0);
			old.set(Calendar.SECOND, 0);
			old.set(Calendar.MILLISECOND, 0);

			long intervalMilli = old.getTimeInMillis()
					- today.getTimeInMillis();
			xcs = (int) (intervalMilli / (24 * 60 * 60 * 1000));

			if (xcs == 0) {
				return new DateResult(DateResult.DATE_RESULT_NO_SELL, "今日"
						+ hour + ":00");
			} else if (xcs == 1) {
				return new DateResult(DateResult.DATE_RESULT_NO_SELL, "明日"
						+ hour + ":00");
			} else if (xcs == 2) {
				return new DateResult(DateResult.DATE_RESULT_NO_SELL, "后天"
						+ hour + ":00");
			} else {
				return new DateResult(DateResult.DATE_RESULT_NO_SELL, "即将开卖");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new DateResult(DateResult.DATE_RESULT_NO_NULL, "");
	}



	public static class DateResult {

		public static final int DATE_RESULT_NO_SELL = 0;
		public static final int DATE_RESULT_LAST_DAY = 1;
		public static final int DATE_RESULT_LAST_HOUR = 2;
		public static final int DATE_RESULT_NO_NULL = 3;
		public static final int DATE_RESULT_OVER_FIFTEEN = 15;
		public static final int DATE_RESULT_LAST_MIAO = 4;
		public static final int DATE_RESULT_LAST_MIN = 5;
		private int type;
		private String result;

		public DateResult(int type, String result) {
			this.type = type;
			this.result = result;
		}

		public int getType() {
			return type;
		}

		public String getResult() {
			return result;
		}

	}

	/**
	 * 是今天还是明天
	 * 
	 * @param timeSource
	 * @return 0今天 ,1 明天, -1异常
	 */
	public static int isTomorrowDay(String timeSource) {
		if (TextUtils.isEmpty(timeSource)) {
			return -1;
		}
		try {
			Date date = simpleDateFormat.parse(timeSource);
			if (simpleDayDateFormat.format(new Date().getTime()).equals(
					simpleDayDateFormat.format(date))) {
				return 0;
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
			date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
			if (simpleDayDateFormat.format(new Date().getTime()).equals(
					simpleDayDateFormat.format(date))) {
				return 1;
			}
		} catch (ParseException e) {
		}

		return -1;
	}

	public static boolean isYesterday(long longTime) {
		if (longTime <= 0)
			return false;

		// 消息时间+1天如果等于当前时间，那么消息时间就是昨天
		Calendar calendarMsg = Calendar.getInstance();
		calendarMsg.setTimeInMillis(longTime);
		calendarMsg.add(calendarMsg.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		Date dateMsgMsg = calendarMsg.getTime(); // 这个时间就是日期往后推一天的结果
		if (simpleDayDateFormat.format(new Date().getTime()).equals(
				simpleDayDateFormat.format(dateMsgMsg))) {
			return true;
		}
		return false;
	}

	public static String getSellTipListItemTime(String timeSource) {
		int timeStatue = isTomorrowDay(timeSource);
		if (timeStatue == 0) {
			return "今日"
					+ (timeSource.substring(timeSource.indexOf(" ") + 1,
							timeSource.lastIndexOf(":"))) + "开抢";
		} else if (timeStatue == 1) {
			return "明日"
					+ (timeSource.substring(timeSource.indexOf(" ") + 1,
							timeSource.lastIndexOf(":"))) + "开抢";
		} else {
			return (getMDHSTime(timeSource)) + "开抢";
		}
	}

	/**
	 * 计算剩余时间 显示倒计时：该品牌距离下架的时间。超过24小时的，显示X天；不足24小时的，显示X小时。举两个栗子：
	 * 1、剩余4天05小时15分35秒，显示为“剩4天” 2、剩余5小时15分35秒，显示为“剩5小时”
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static String getfakeDealTime(long endTime) {
		long residueTime = endTime - System.currentTimeMillis();
		long time = 0;
		String unit = "天";
		if (DJApplication.getInstance().getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.GINGERBREAD) {
			time = residueTime / (1000 * 3600 * 24);
			if (time <= 0) {
				unit = "小时";
				time = residueTime / (1000 * 3600);
				if (time <= 0) {
					unit = "分";
					time = residueTime / (1000 * 60);
					if (time <= 0) {
						unit = "秒";
						time = residueTime / 1000;
					}
				}
			}
		} else {
			time = TimeUnit.MILLISECONDS.toDays(residueTime);
			if (time <= 0) {
				unit = "小时";
				time = TimeUnit.MILLISECONDS.toHours(residueTime);
				if (time <= 0) {
					unit = "分";
					time = TimeUnit.MILLISECONDS.toMinutes(residueTime);
					if (time <= 0) {
						unit = "秒";
						time = TimeUnit.MILLISECONDS.toSeconds(residueTime);
					}
				}
			}
		}
		return "剩余" + time + unit;
	}

	/**
	 * 世界协调时间的钻换
	 * 
	 * @param UTCTime
	 *            格式为 "2014-11-11T14:00:00+08:00" 的时间
	 * @return 格式为 "yyyy-MM-dd HH:mm:ss" "2014-11-11 14:00:00"
	 */
	public static String parseUTCTime(String UTCTime) {
		if (TextUtils.isEmpty(UTCTime))
			return "";
		String utcTime = UTCTime.substring(0, UTCTime.length() - 3)
				+ UTCTime.substring(UTCTime.length() - 2, UTCTime.length());
		Date date;
		try {
			date = utcDateFormat.parse(utcTime);
			return simpleDateFormat.format(date);
		} catch (ParseException e) {
		}
		return "";
	}

	/**
	 * 世界协调时间的钻换
	 * 
	 * @param UTCTime
	 *            格式为 "2014-11-11T14:00:00+08:00" 的时间
	 * @return 返回毫秒
	 */
	public static long parseUTCTimeToMillis(String UTCTime) {
		if (TextUtils.isEmpty(UTCTime))
			return -1;
		String utcTime = UTCTime.substring(0, UTCTime.length() - 3)
				+ UTCTime.substring(UTCTime.length() - 2, UTCTime.length());
		Date date;
		try {
			date = utcDateFormat.parse(utcTime);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static String getCurrentUTCTime() {
		return utcDateFormat.format(new Date());
	}

	// true就是big>small
	public static boolean compareSimpleDate(String big, String small) {
		try {
			if (TextUtils.isEmpty(big))
				return false;
			if (TextUtils.isEmpty(small))
				return true;
			Date bigDate = simpleDateFormat.parse(big);
			Date smallDate = simpleDateFormat.parse(small);
			return bigDate.getTime() > smallDate.getTime();

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}

	// true就是big>small
	public static boolean compareSimpleDate(long big, String small) {
		try {
			if (TextUtils.isEmpty(small))
				return true;
			Date bigDate = simpleDateFormat.parse(small);
			return bigDate.getTime() < big;

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 以oneday为基础，向前或向后加days天
	 * 
	 * @param oneDay
	 *            以这天为基础 格式"yyyy-MM-dd"
	 * @param days
	 *            把日期往后增加一天.整数往后推,负数往前移动
	 * @return 输出日期，格式"yyyy-MM-dd"
	 */
	public static String addSomeDays(String oneDay, int days) {
		String result = "";
		try {
			Date date = simpleYHMDateFormat.parse(oneDay);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.DATE, days);
			result = simpleDateFormat.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 以oneday为基础，向前或向后加days天
	 * 
	 * @param oneDay
	 *            以这天为基础 格式"yyyy-MM-dd hh:mm:ss"
	 * @param minute
	 *            时间增加minute分钟.正数往后推,负数往前移动
	 * @return 输出日期，格式"yyyy-MM-dd hh:mm:ss"
	 */
	public static String addSomeMinute(String oneDay, int minute) {
		String result = "";
		try {
			Date date = simpleDateFormat.parse(oneDay);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(calendar.MINUTE, minute);
			result = simpleDateFormat.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 获取从今天算起前或者后几（days）天的日期,比如最近7天的，days=-6
	public static String addSomeDaysSinceToday(int days) {
		Date date = new Date();
		return addSomeDays(simpleYHMDateFormat.format(date), days);
	}

	/**
	 * 判断今天、昨天、某天
	 * 
	 * @param timeSource
	 * @return 0今天 ,1 昨天, -1 都不是
	 */
	public static int maybeYesterday(String timeSource) {
		if (TextUtils.isEmpty(timeSource)) {
			return -1;
		}
		try {
			Date date = simpleDateFormat.parse(timeSource);
			if (simpleDayDateFormat.format(new Date().getTime()).equals(
					simpleDayDateFormat.format(date))) {
				return 0;
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
			if (simpleDayDateFormat.format(new Date().getTime()).equals(
					simpleDayDateFormat.format(date))) {
				return 1;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * 消息中心的消息时间处理
	 * 
	 * @param date
	 *            格式 "2015-08-15 15:30:22" 或者 "2015-08-15 15:30"
	 * @return 格式 "今日 19:50"；"昨日 19:50"；"2015-08-15 15:20"
	 */
	public static String handleMsgCenterDate(String date) {
		if (TextUtils.isEmpty(date))
			return "";
		String nossDate = "";
		if (date.length() == 19) {// yyyy-MM-dd hh:mm:ss
			nossDate = date.substring(0, date.lastIndexOf(":"));
		} else if (date.length() == 16) {// yyyy-MM-dd hh:mm
			nossDate = date;
		} else {// 无法识别
			return date;
		}
		StringBuilder sb = new StringBuilder("");
		int what = maybeYesterday(date);
		if (what == 0) {// 今天
			sb.append("今天 ").append(nossDate.split(" ")[1]);
		} else if (what == 1) {// 昨天
			sb.append("昨天 ").append(nossDate.split(" ")[1]);
		} else if (what == -1) {// 某天
			return nossDate;
		}
		return sb.toString();
	}

	// 和现在比 时间差
	public static String deltaTimeNow(String someTime, int unit,
			SimpleDateFormat format) {
		try {
			Date someDate = format.parse(someTime);
			long delta = someDate.getTime() - System.currentTimeMillis();
			return cast(delta, unit);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	// 和现在比 时间差 "yyyy-MM-dd hh:mm:ss"
	public static String deltaTimeNowSimpleFormat(String someTime, int unit) {
		return deltaTimeNow(someTime, unit, simpleDateFormat);
	}

	public static String deltaTimeSimpleDateFormat(String someTime,
			String reference, int unit) {

		return deltaTime(someTime, reference, unit, simpleDateFormat);
	}

	public static String deltaTime(String someTime, String referenceTime,
			int unit, SimpleDateFormat format) {
		try {
			Date someDate = format.parse(someTime);
			Date referenceDate = format.parse(referenceTime);
			long delta = someDate.getTime() - referenceDate.getTime();
			return cast(delta, unit);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	// 时间转换成 天 小时 分 秒；unit是单位，如果单位是分，但只有59秒，那么就显示59秒，而不是0分
	// 递归，但最多不会超过4层,而且没有分叉。测试性能 平均 580毫秒/10000次
	public static String cast(long time, int unit) {
		if (unit < 1 || unit > 4) {
			return time + "秒";
		}
		long unitTime = 1;// 默认单位1毫秒
		long result = 0;
		String unitStr = "";
		switch (unit) {
		case TIME_UNIT_SECOND:
			unitTime = TimeUnit.SECONDS.toMillis(1);
			unitStr += "秒";
			break;
		case TIME_UNIT_MINUTE:
			// unitTime=TimeUnit.MINUTES.toMillis(1);
			unitTime = 1000 * 60;
			unitStr += "分钟";
			break;
		case TIME_UNIT_HOUR:
			// unitTime=TimeUnit.HOURS.toMillis(1);
			unitTime = 1000 * 60 * 60;
			unitStr += "小时";
			break;
		case TIME_UNIT_DAY:
			// unitTime=TimeUnit.DAYS.toMillis(1);
			unitTime = 1000 * 60 * 60 * 24;
			unitStr += "天";
			break;
		default:
			// unitTime=TimeUnit.DAYS.toMillis(1);
			unitTime = 0;
			unitStr += "秒";
			break;
		}
		result = time / unitTime;
		if (unit <= 1 || result != 0) {
			return result + unitStr;
		} else {
			return cast(time, --unit);
		}

	}

	/**
	 * 将毫秒转换成日期格式
	 * 
	 * @param strTime
	 * @return
	 */
	public static String stringToDate(String strTime) {

		Date date = new Date();
		try {
			date.setTime(Long.parseLong(strTime) * 1000);
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
			return " ";
		}
		return simpleDateFormat.format(date);
	}

}
