package com.rest.yun.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @project: yun
 * @Title: CommonUtiles.java
 * @Package com.rest.yun.util
 * @Description: 通用公共方法封装类
 * @author 杨贵松
 * @date 2014年1月22日 下午11:56:34
 * @version V1.0
 */
public class CommonUtiles {

	public final static String ISO_8859_1 = "ISO-8859-1";
	public final static String UTF_8 = "UTF-8";

	/**
	 * @Title: getSystemDateTime
	 * @author 杨贵松
	 * @date 2014年1月22日 下午11:53:47
	 * @Description: 获得系统时间
	 * @return Date 返回
	 * @throws ParseException
	 */
	public static Date getSystemDateTime() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = sdf.parse(sdf.format(new Date()));
		return time;
	}

	/**
	 * @Title: getLastDate
	 * @author 杨贵松
	 * @date 2014年11月01日 下午11:17:35
	 * @Description: 获取比当前时间早几秒的时间
	 * @return
	 * @throws ParseException
	 *             Date 返回
	 */
	public static Date getLastDate(int num) throws ParseException {
		long t = System.currentTimeMillis();
		Date date = new Date(t + num * 1000);
		return date;
	}
	/**
	 * @Title:       caculate
	 * @author:      杨贵松
	 * @time         2014年12月15日 下午10:41:01
	 * @Description: 高斯消元法求四元一次方程
	 * @return       double[]
	 * @throws
	 */
	public static float[] caculate(double[][] b) throws NumberFormatException
	{
		double[][] a = new double[4][5];
		for(int i=0;i<4;i++){
			a[i][0] = b[i][0]*b[i][0]*b[i][0]; 
			a[i][1] = b[i][0]*b[i][0]; 
			a[i][2] = b[i][0]; 
			a[i][3] = 1; 
			a[i][4] = b[i][1]; 
		}
		int _rows = a.length;
		int _cols = a[0].length;
		int L = _rows - 1;
		int i, j, l, n, m, k = 0;
		double[] temp1 = new double[_rows];
		/* 第一个do-while是将增广矩阵消成上三角形式 */
		do {
			n = 0;
			for (l = k; l < L; l++)
				temp1[n++] = a[l + 1][k] / a[k][k];
			for (m = 0, i = k + 1; i < _rows; i++, m++) {
				for (j = k; j < _cols; j++)
					a[i][j] -= temp1[m] * a[k][j];
			}
			k++;
		} while (k < _rows);
		// 第二个do-while是将矩阵消成对角形式，并且重新给k赋值,最后只剩下对角线和最后一列的数，其它都为0
		k = L - 1;
		do {
			n = 0;
			for (l = k; l >= 0; l--)
				temp1[n++] = a[k - l][k + 1] / a[k + 1][k + 1];
			for (m = 0, i = k; i >= 0; i--, m++) {
				for (j = k; j < _cols; j++)
					a[k - i][j] -= temp1[m] * a[k + 1][j];
			}
			k--;
		} while (k >= 0);
		//下一个for是解方程组 
		float[] newresult = new float[_rows];
		NumberFormat nf = NumberFormat.getNumberInstance();//保留两位小数
        nf.setMaximumFractionDigits(2);
		for (i = 0; i < _rows; i++) {
			newresult[i] = Float.parseFloat(nf.format((float)(a[i][_rows] / a[i][i])));
		}
		return newresult;
	}
	
	/**
	 * @Title:       printDay
	 * @author:      杨贵松
	 * @time         2015年1月14日 上午5:41:04
	 * @Description: 求两个日期之间的所有日期
	 * @return       List<Date>
	 * @throws
	 */
	public static List<Date> betweenDayList(Date start, Date end) {
		List<Date> list = new ArrayList<Date>();
		Calendar startDay = Calendar.getInstance();
		Calendar endDay = Calendar.getInstance();

		startDay.setTime(start);
		endDay.setTime(end);
		// 给出的日期开始日比终了日大则不执行打印
		if (startDay.compareTo(endDay) > 0) {
			return list;
		}
		// 给出的日期开始日与终了日相同则打印开始日期后终止
		if (startDay.compareTo(endDay) == 0) {
			list.add(start);
			return list;
		}
		// 现在打印中的日期
		Calendar currentPrintDay = startDay;
		while (true) {
			// 打印日期
			list.add(currentPrintDay.getTime());
			// 日期加一
			currentPrintDay.add(Calendar.DATE, 1);
			// 日期加一后判断是否达到终了日，达到则终止打印
			if (currentPrintDay.compareTo(endDay) == 0) {
				list.add(currentPrintDay.getTime());
				break;
			}
		}
		return list;
	}
	/**
	 * @Title:       delAllFile
	 * @author:      杨贵松
	 * @time         2015年1月25日 下午9:09:51
	 * @Description: 删除指定文件夹下所有文件
	 * @return       void
	 * @throws
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
			}
		}
	}
	/**
	 * Decode URL by UTF-8
	 * 
	 * @param value
	 * @return
	 */
	public static String decodeUrl(String value) {
		try {
			value = URLDecoder.decode(value, UTF_8);
		} catch (Exception e) {
		}
		return value;
	}

	public static String fixedChinaCode(String value) {
		try {
			value = new String(value.getBytes(ISO_8859_1), UTF_8);
		} catch (UnsupportedEncodingException e) {
		}
		return value;
	}
}
