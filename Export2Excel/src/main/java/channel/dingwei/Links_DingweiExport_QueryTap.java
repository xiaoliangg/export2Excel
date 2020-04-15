package channel.dingwei;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bean.C9LfeuTapData;
import com.bean.DingWeiExport;
import com.bean.DingWeiMonth;
import com.dao.IC9LfeuTapDataDao;
import com.dao.IM2mCardInfoDao;
import com.util.TimeUtil;

public class Links_DingweiExport_QueryTap {

	public static void main(String[] args) throws IOException {
		String path = "C:/Users/ll/Desktop/"; 
//		String path = "/home/iotota/"; 
        String fileType = "xlsx";
        String fileName = "鼎为1811-1901数据分析和TAP详单_C9正式账户3";
		Map<String,String> imsiMap = ImsiMap.getImsimap();

        ApplicationContext context = new ClassPathXmlApplicationContext(
                "spring-applicationContext.xml");
        String [] arr  = context.getBeanDefinitionNames();
        
        IC9LfeuTapDataDao c9LfeuTapDataDao = (IC9LfeuTapDataDao)context.getBean("IC9LfeuTapDataDao");
        Date start = new Date();
        export(path,fileName,fileType,c9LfeuTapDataDao,imsiMap);
        Date end = new Date();
        System.out.println("导出结束，耗时:" + (end.getTime()-start.getTime())/1000);

	}

	private static void export(String path, String fileName, String fileType,IC9LfeuTapDataDao c9LfeuTapDataDao,Map<String,String> imsiMap) throws IOException {
        Workbook wb = null;  
        if (fileType.equals("xls")) {  
            wb = new HSSFWorkbook();  
        }  
        else if(fileType.equals("xlsx"))  
        {  
            wb = new SXSSFWorkbook(1000);  //SXSSFWorkbook适合大数据的导出，不会内存溢出
        }  
        else  
        {  
            System.out.println("您的文档格式不正确！");  
        } 
        XSSFCellStyle borderStyle = (XSSFCellStyle) wb.createCellStyle();
        borderStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框  
        borderStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框  
        borderStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框  
        borderStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
        
        XSSFCellStyle alignCenterStyle = (XSSFCellStyle) wb.createCellStyle();
        alignCenterStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
        	
		
        Sheet sheet = wb.createSheet("Raw data");
        //创建标题行
		Row row = (Row)sheet.createRow(0);
		createHeadRow(row,borderStyle);
		
		List<C9LfeuTapData> tapList = c9LfeuTapDataDao.queryAll();
		//填充数据
		for(int i =0;i<tapList.size();i++){
			C9LfeuTapData tap = tapList.get(i);
			row = (Row)sheet.createRow(i+1);

			
			Cell cell = row.createCell(0);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(getCardImsi(tap.getImsi(),imsiMap));
	        cell = row.createCell(1);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(tap.getCallTime());
	        cell = row.createCell(2);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(tap.getOperator());
	        cell = row.createCell(3);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(tap.getProfileTadig());
	        cell = row.createCell(4);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(Long.parseLong(tap.getChargableUnits()));
	        cell = row.createCell(5);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(Long.parseLong(tap.getChargedUnits()));
	        cell = row.createCell(6);
	        cell.setCellStyle(borderStyle);
	        if(tap.getCharge() != null){
	        	cell.setCellValue(Double.parseDouble(tap.getCharge()));
	        }else{
	        	cell.setCellValue(Double.parseDouble("0"));
	        	
	        }
	        cell = row.createCell(7);
	        cell.setCellStyle(borderStyle);
	        if(tap.getPerMbCost() != null){
	        	cell.setCellValue(Double.parseDouble(tap.getPerMbCost()));
	        }else{
	        	cell.setCellValue(Double.parseDouble("0"));
	        }
		}
		
		
		sheet.setColumnWidth(0, 256*17);
		sheet.setColumnWidth(1, 256*19);
		sheet.setColumnWidth(6, 256*15);
		sheet.setColumnWidth(7, 256*15);
        //创建文件流  
        OutputStream stream2 = new FileOutputStream(path+fileName+"."+fileType);  
        //写入数据  
        wb.write(stream2);  
        //关闭文件流  
        stream2.close();
		
	}

	private static String getCardImsi(String imsi1,Map<String,String> imsiMap) {
		Set<String> imsi2Set = imsiMap.keySet();
		for(String imsi2:imsi2Set){
			if(imsiMap.get(imsi2).equals(imsi1)){
				return imsi2;
			}
		}
		return imsi1;
	}

	private static void createHeadRow(Row row, XSSFCellStyle borderStyle) {
		Cell cell = row.createCell(0);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("imsi");
        cell = row.createCell(1);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("CallTime");
        cell = row.createCell(2);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("operator");
        cell = row.createCell(3);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("ProfileTADIG");
        cell = row.createCell(4);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("ChargableUnits");
        cell = row.createCell(5);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("ChargedUnits");
        cell = row.createCell(6);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("charge");
        cell = row.createCell(7);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("per MB cost");
		
	}

	private static DingWeiMonth getMonthUsage(List<DingWeiMonth> monthUsages, String month) {
		for(DingWeiMonth monthUsage:monthUsages){
			if(monthUsage.getMonth().equals(month)){
				return monthUsage;
			}
		}
		return null;
	}

	private static void createHeadRow1(Row row, XSSFCellStyle borderStyle, int monthSize) {
		Cell cell = row.createCell(0);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("imsi");
        cell = row.createCell(1);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("first CDR");
        cell = row.createCell(2);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("last CDR");
        
        for(int i=0;i<monthSize;i++){
            cell = row.createCell(2+i*2+1);
            cell.setCellStyle(borderStyle);
            cell.setCellValue("usage (MB)");
            cell = row.createCell(2+i*2+2);
            cell.setCellStyle(borderStyle);
            cell.setCellValue("tap cost");
        }
        
        cell = row.createCell(2+monthSize*2+1);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("Total usage (MB)");
        cell = row.createCell(2+monthSize*2+2);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("Total tap cost");
	}

	private static void createHeadRow0(Row row, XSSFCellStyle borderStyle,XSSFCellStyle alignCenterStyle, String[] months, Sheet sheet, Workbook wb) {
		for(int i = 0;i<months.length;i++){
			
			Cell cell = row.createCell(3+2*i);
	        cell.setCellStyle(alignCenterStyle);
	        CellRangeAddress brodercell = new CellRangeAddress(0,0,3+2*i,3+2*i+1);

	        setBorderForMergeCell(XSSFCellStyle.BORDER_THIN, brodercell, sheet, wb);
	        cell.setCellValue(months[i]);

		}

		
	}

	private static List<DingWeiExport> getDingWeiExports(IC9LfeuTapDataDao c9LfeuTapDataDao, IM2mCardInfoDao m2mCardInfoDao, String[] months) {
		//查询鼎为渠道所有的imsi:1、获得imsiMap(两个imsi的对应关系) 2、获得查询tap所需的imsis
		Map<String,String> imsiMap = getImsimap();

		List<String> allDingWeiImsis = m2mCardInfoDao.queryAllDingWeiImsi(); //查询全部鼎为imsi，其中g的码号是222打头的
		String imsis = getAllImsis(allDingWeiImsis,imsiMap);
		//根据imsi映射文件映射得到两个imsi
		Map<String,String> allImsiMap = getAllImsimap(allDingWeiImsis,imsiMap);

		
		String startTime = getEarliestTime(months);
		String endTime = getLatestTime(months);
		List<DingWeiExport> allTotalUsage = c9LfeuTapDataDao.query1(imsis, startTime, endTime);
		//使用imsiMap 为 allTotalUsage 填充两个imsi
		
		for(String month:months){
			startTime = getStartTime(month);
			endTime = getEndTime(month);
			//每月查询
			List<DingWeiMonth> monthUsage = c9LfeuTapDataDao.queryMonth(imsis, startTime, endTime);
			//为allTotalUsage填充
			fill(allTotalUsage,monthUsage); 
		}
		
		//填充imsi2
		for(DingWeiExport export:allTotalUsage){
			if(imsiMap.containsValue(export.getImsi1())){
				String imsi2 = getImsi2(export.getImsi1(),imsiMap);
				export.setImsi2(imsi2);
			}else{
				export.setImsi2(export.getImsi1());
			}
			System.out.println(export.toString());
			List<DingWeiMonth> monthUsages = export.getMonthsUsage();
			for(DingWeiMonth monthUsage:monthUsages){
				System.out.println("--------" + monthUsage);
			}
		}
		allTotalUsage = fillNullUsageImsi(allDingWeiImsis,allImsiMap,allTotalUsage);
		System.out.println(allTotalUsage.size());
		return allTotalUsage;
		//导出到excel
		
	}

	


	private static List<DingWeiExport> fillNullUsageImsi(List<String> allDingWeiImsis, Map<String, String> allImsiMap, List<DingWeiExport> allTotalUsage) {
		List<DingWeiExport> allTotalUsage2 = new ArrayList<DingWeiExport>();
		for(String imsi2:allDingWeiImsis){ //有序的，且是allImsiMap的key
			if(getExport(allTotalUsage,imsi2) == null){
				DingWeiExport export = new DingWeiExport();
				export.setImsi1(allImsiMap.get(imsi2));
				export.setImsi2(imsi2);
				export.setTotalCost("0");
				export.setTotalUsage("0");
				allTotalUsage2.add(export);
			}else{
				allTotalUsage2.add(getExport(allTotalUsage, imsi2));
			}
		}
		return allTotalUsage2;
	}

	private static DingWeiExport getExport(List<DingWeiExport> allTotalUsage, String imsi2) {
		for(DingWeiExport export:allTotalUsage){
			if(export.getImsi2().equals(imsi2)){
				return export;
			}
		}
		return null;
	}

	/**
     * 合并单元格设置边框
     * @param i
     * @param cellRangeTitle
     * @param sheet
     * @param wb
     */
    private static void setBorderForMergeCell(short i, CellRangeAddress cellRangeTitle, Sheet sheet, Workbook wb){
        sheet.addMergedRegion(cellRangeTitle);
        RegionUtil.setBorderBottom(i, cellRangeTitle, sheet, wb);
        RegionUtil.setBorderLeft(i, cellRangeTitle, sheet, wb);
        RegionUtil.setBorderRight(i, cellRangeTitle, sheet, wb);
        RegionUtil.setBorderTop(i, cellRangeTitle, sheet, wb);
    }
	
	
	
	
	private static String getImsi2(String imsi1, Map<String, String> imsiMap) {
		for (String key : imsiMap.keySet()) { 
			  if(imsiMap.get(key).equals(imsi1)){
				  return key;
			  }
		}
		System.out.println("imsimap中不包含:" + imsi1 + " !!!!");
		return null;
	}

	private static void fill(List<DingWeiExport> allTotalUsage, List<DingWeiMonth> monthUsage) {
		for(DingWeiExport export:allTotalUsage){
			for(DingWeiMonth month:monthUsage){
				if(export.getImsi1().equals(month.getImsi1())){
					export.getMonthsUsage().add(month);
				}
			}
		}
		
	}

	private static String getEndTime(String month) {
        Date d = TimeUtil.format("yyyy-MM",month);
		String nextMonth = TimeUtil.getFormateNextMonth("yyyy-MM",d);
		String endTime = nextMonth + "-01 00:00:00";
		return endTime;
	}

	private static String getStartTime(String month) {
		// TODO Auto-generated method stub
		return month + "-01 00:00:00";
	}

	private static String getLatestTime(String[] months) {
        Date d = TimeUtil.format("yyyy-MM", months[months.length -1]);
		String nextMonth = TimeUtil.getFormateNextMonth("yyyy-MM",d);
		String endTime = nextMonth + "-01 00:00:00";
		return endTime;
	}

	private static String getEarliestTime(String[] months) {
		// TODO Auto-generated method stub
		return months[0] + "-01 00:00:00";
	}
	private static Map<String, String> getAllImsimap(List<String> allDingWeiImsis, Map<String, String> imsiMap) {
		Map<String, String> allImsiMap = new HashMap<String, String>();
		for(String imsi:allDingWeiImsis){
			if(imsiMap.containsKey(imsi)){
				allImsiMap.put(imsi, imsiMap.get(imsi));
			}else{
				allImsiMap.put(imsi, imsi);
			}
		}
		return allImsiMap;
	}
	private static String getAllImsis(List<String> allDingWeiImsis, Map<String, String> imsiMap) {
		StringBuilder allImsi = new StringBuilder();
		for(String imsi:allDingWeiImsis){
			if(imsiMap.containsKey(imsi)){
				allImsi.append(imsiMap.get(imsi) + ",");
			}else{
				allImsi.append(imsi + ",");
			}
		}
		return allImsi.toString();
	}

	private static Map<String, String> getImsimap() {
		Map<String, String> imsiMap = new HashMap<String, String>();
		imsiMap.put("222013090256860", "234180001256860");
		imsiMap.put("222013090256861", "234180001256861");
		imsiMap.put("222013090258016", "234180001258016");
		imsiMap.put("222013090258017", "234180001258017");
		imsiMap.put("222013090258683", "234180001258683");
		imsiMap.put("222013091267587", "234180000267587");
		imsiMap.put("222013091267591", "234180000267591");
		imsiMap.put("222013091267668", "234180000267668");
		imsiMap.put("222013091267671", "234180000267671");
		imsiMap.put("222013091267713", "234180000267713");
		imsiMap.put("222013091267731", "234180000267731");
		imsiMap.put("222013091267875", "234180000267875");
		imsiMap.put("222013091269834", "234180000269834");
		imsiMap.put("222013091269835", "234180000269835");
		imsiMap.put("222013091269838", "234180000269838");
		imsiMap.put("222013091281894", "234180000281894");
		imsiMap.put("222013091281924", "234180000281924");
		imsiMap.put("222013091281968", "234180000281968");
		imsiMap.put("222013091281998", "234180000281998");
		imsiMap.put("222013091282001", "234180000282001");
		imsiMap.put("222013091282009", "234180000282009");
		imsiMap.put("222013091282021", "234180000282021");
		imsiMap.put("222013091282023", "234180000282023");
		imsiMap.put("222013091282035", "234180000282035");
		imsiMap.put("222013091282040", "234180000282040");
		imsiMap.put("222013091282058", "234180000282058");
		imsiMap.put("222013091282068", "234180000282068");
		imsiMap.put("222013091282070", "234180000282070");
		imsiMap.put("222013091282078", "234180000282078");
		imsiMap.put("222013091282079", "234180000282079");
		imsiMap.put("222013091282080", "234180000282080");
		imsiMap.put("222013091282094", "234180000282094");
		imsiMap.put("222013091282095", "234180000282095");
		imsiMap.put("222013091282109", "234180000282109");
		imsiMap.put("222013091282140", "234180000282140");
		imsiMap.put("222013091282156", "234180000282156");
		imsiMap.put("222013091282169", "234180000282169");
		imsiMap.put("222013091282179", "234180000282179");
		imsiMap.put("222013091282231", "234180000282231");
		imsiMap.put("222013091282233", "234180000282233");
		imsiMap.put("222013091282236", "234180000282236");
		imsiMap.put("222013091282238", "234180000282238");
		imsiMap.put("222013091282277", "234180000282277");
		imsiMap.put("222013091282294", "234180000282294");
		imsiMap.put("222013091282359", "234180000282359");
		imsiMap.put("222013091282366", "234180000282366");
		imsiMap.put("222013091282369", "234180000282369");
		imsiMap.put("222013091282395", "234180000282395");
		imsiMap.put("222013091282422", "234180000282422");
		imsiMap.put("222013091282423", "234180000282423");
		imsiMap.put("222013091282428", "234180000282428");
		imsiMap.put("222013091282432", "234180000282432");
		imsiMap.put("222013091282445", "234180000282445");
		imsiMap.put("222013091282455", "234180000282455");
		imsiMap.put("222013091282468", "234180000282468");
		imsiMap.put("222013091282484", "234180000282484");
		imsiMap.put("222013091282485", "234180000282485");
		imsiMap.put("222013091282488", "234180000282488");
		imsiMap.put("222013091282521", "234180000282521");
		imsiMap.put("222013091282525", "234180000282525");
		imsiMap.put("222013091282532", "234180000282532");
		imsiMap.put("222013091282542", "234180000282542");
		imsiMap.put("222013091282559", "234180000282559");
		imsiMap.put("222013091282566", "234180000282566");
		imsiMap.put("222013091282567", "234180000282567");
		imsiMap.put("222013091282570", "234180000282570");
		imsiMap.put("222013091282581", "234180000282581");
		imsiMap.put("222013091282585", "234180000282585");
		imsiMap.put("222013091282593", "234180000282593");
		imsiMap.put("222013091282605", "234180000282605");
		imsiMap.put("222013091282606", "234180000282606");
		imsiMap.put("222013091282641", "234180000282641");
		imsiMap.put("222013091282671", "234180000282671");
		imsiMap.put("222013091282672", "234180000282672");
		imsiMap.put("222013091282694", "234180000282694");
		imsiMap.put("222013091282695", "234180000282695");
		imsiMap.put("222013091282697", "234180000282697");
		imsiMap.put("222013091282717", "234180000282717");
		imsiMap.put("222013091282718", "234180000282718");
		imsiMap.put("222013091282724", "234180000282724");
		imsiMap.put("222013091282735", "234180000282735");
		imsiMap.put("222013091282742", "234180000282742");
		imsiMap.put("222013091282743", "234180000282743");
		imsiMap.put("222013091282744", "234180000282744");
		imsiMap.put("222013091282758", "234180000282758");
		imsiMap.put("222013091282787", "234180000282787");
		imsiMap.put("222013091282790", "234180000282790");
		imsiMap.put("222013091282792", "234180000282792");
		imsiMap.put("222013091282826", "234180000282826");
		imsiMap.put("222013091282931", "234180000282931");
		imsiMap.put("222013091282935", "234180000282935");
		imsiMap.put("222013091282936", "234180000282936");
		imsiMap.put("222013091282944", "234180000282944");
		imsiMap.put("222013091282947", "234180000282947");
		imsiMap.put("222013091282967", "234180000282967");
		imsiMap.put("222013091282968", "234180000282968");
		imsiMap.put("222013091283023", "234180000283023");
		imsiMap.put("222013091283044", "234180000283044");
		imsiMap.put("222013091283058", "234180000283058");
		imsiMap.put("222013091283094", "234180000283094");
		imsiMap.put("222013091283114", "234180000283114");
		imsiMap.put("222013091283145", "234180000283145");
		imsiMap.put("222013091283196", "234180000283196");
		imsiMap.put("222013091283227", "234180000283227");
		imsiMap.put("222013091283276", "234180000283276");
		imsiMap.put("222013091283323", "234180000283323");
		imsiMap.put("222013091283344", "234180000283344");
		imsiMap.put("222013091283377", "234180000283377");
		imsiMap.put("222013091283408", "234180000283408");
		imsiMap.put("222013091283666", "234180000283666");
		imsiMap.put("222013091283860", "234180000283860");
		imsiMap.put("222013091283907", "234180000283907");
		imsiMap.put("222013091283975", "234180000283975");
		imsiMap.put("222013091283993", "234180000283993");
		imsiMap.put("222013091284029", "234180000284029");
		imsiMap.put("222013091284032", "234180000284032");
		imsiMap.put("222013091284044", "234180000284044");
		imsiMap.put("222013091284050", "234180000284050");
		imsiMap.put("222013091284054", "234180000284054");
		imsiMap.put("222013091284089", "234180000284089");
		imsiMap.put("222013091284132", "234180000284132");
		imsiMap.put("222013091284164", "234180000284164");
		imsiMap.put("222013091284165", "234180000284165");
		imsiMap.put("222013091284171", "234180000284171");
		imsiMap.put("222013091284430", "234180000284430");
		imsiMap.put("222013091284542", "234180000284542");
		imsiMap.put("222013091284631", "234180000284631");
		imsiMap.put("222013091284635", "234180000284635");
		imsiMap.put("222013091284636", "234180000284636");
		imsiMap.put("222013091284637", "234180000284637");
		imsiMap.put("222013091284649", "234180000284649");
		imsiMap.put("222013091284654", "234180000284654");
		imsiMap.put("222013091284668", "234180000284668");
		imsiMap.put("222013091284802", "234180000284802");
		imsiMap.put("222013091284819", "234180000284819");
		imsiMap.put("222013091284939", "234180000284939");
		imsiMap.put("222013091284940", "234180000284940");
		imsiMap.put("222013091305028", "234180000305028");
		imsiMap.put("222013091305033", "234180000305033");
		imsiMap.put("222013091305046", "234180000305046");
		imsiMap.put("222013091305049", "234180000305049");
		imsiMap.put("222013091305054", "234180000305054");
		imsiMap.put("222013091305061", "234180000305061");
		imsiMap.put("222013091305067", "234180000305067");
		imsiMap.put("222013091305083", "234180000305083");
		imsiMap.put("222013091305094", "234180000305094");
		imsiMap.put("222013091305095", "234180000305095");
		imsiMap.put("222013091305100", "234180000305100");
		imsiMap.put("222013091305102", "234180000305102");
		imsiMap.put("222013091305106", "234180000305106");
		imsiMap.put("222013091305108", "234180000305108");
		imsiMap.put("222013091305119", "234180000305119");
		imsiMap.put("222013091305161", "234180000305161");
		imsiMap.put("222013091305169", "234180000305169");
		imsiMap.put("222013091305172", "234180000305172");
		imsiMap.put("222013091305174", "234180000305174");
		imsiMap.put("222013091305180", "234180000305180");
		imsiMap.put("222013091305182", "234180000305182");
		imsiMap.put("222013091305183", "234180000305183");
		imsiMap.put("222013091305184", "234180000305184");
		imsiMap.put("222013091305189", "234180000305189");
		imsiMap.put("222013091305191", "234180000305191");
		imsiMap.put("222013091305192", "234180000305192");
		imsiMap.put("222013091305194", "234180000305194");
		imsiMap.put("222013091305197", "234180000305197");
		imsiMap.put("222013091305200", "234180000305200");
		imsiMap.put("222013091305201", "234180000305201");
		imsiMap.put("222013091305202", "234180000305202");
		imsiMap.put("222013091305203", "234180000305203");
		imsiMap.put("222013091305207", "234180000305207");
		imsiMap.put("222013091305211", "234180000305211");
		imsiMap.put("222013091305213", "234180000305213");
		imsiMap.put("222013091305215", "234180000305215");
		imsiMap.put("222013091305231", "234180000305231");
		imsiMap.put("222013091305383", "234180000305383");
		imsiMap.put("222013091305386", "234180000305386");
		imsiMap.put("222013091305392", "234180000305392");
		imsiMap.put("222013091305394", "234180000305394");
		imsiMap.put("222013091305398", "234180000305398");
		imsiMap.put("222013091305402", "234180000305402");
		imsiMap.put("222013091305407", "234180000305407");
		imsiMap.put("222013091305408", "234180000305408");
		imsiMap.put("222013091305411", "234180000305411");
		imsiMap.put("222013091305414", "234180000305414");
		imsiMap.put("222013091305416", "234180000305416");
		imsiMap.put("222013091305418", "234180000305418");
		imsiMap.put("222013091305435", "234180000305435");
		imsiMap.put("222013091305436", "234180000305436");
		imsiMap.put("222013091305441", "234180000305441");
		imsiMap.put("222013091305442", "234180000305442");
		imsiMap.put("222013091305443", "234180000305443");
		imsiMap.put("222013091305449", "234180000305449");
		imsiMap.put("222013091305450", "234180000305450");
		imsiMap.put("222013091305452", "234180000305452");
		imsiMap.put("222013091305456", "234180000305456");
		imsiMap.put("222013091305463", "234180000305463");
		imsiMap.put("222013091305464", "234180000305464");
		imsiMap.put("222013091305466", "234180000305466");
		imsiMap.put("222013091305469", "234180000305469");
		imsiMap.put("222013091305471", "234180000305471");
		imsiMap.put("222013091305473", "234180000305473");
		imsiMap.put("222013091305477", "234180000305477");
		imsiMap.put("222013091305483", "234180000305483");
		imsiMap.put("222013091305486", "234180000305486");
		imsiMap.put("222013091305503", "234180000305503");
		imsiMap.put("222013091305508", "234180000305508");
		imsiMap.put("222013091305519", "234180000305519");
		imsiMap.put("222013091305529", "234180000305529");
		imsiMap.put("222013091305533", "234180000305533");
		imsiMap.put("222013091305544", "234180000305544");
		imsiMap.put("222013091305548", "234180000305548");
		imsiMap.put("222013091305558", "234180000305558");
		imsiMap.put("222013091305560", "234180000305560");
		imsiMap.put("222013091305593", "234180000305593");
		imsiMap.put("222013091305613", "234180000305613");
		imsiMap.put("222013091305614", "234180000305614");
		imsiMap.put("222013091305624", "234180000305624");
		imsiMap.put("222013091305633", "234180000305633");
		imsiMap.put("222013091305639", "234180000305639");
		imsiMap.put("222013091305672", "234180000305672");
		imsiMap.put("222013091305693", "234180000305693");
		imsiMap.put("222013091305698", "234180000305698");
		imsiMap.put("222013091305778", "234180000305778");
		imsiMap.put("222013091305788", "234180000305788");
		imsiMap.put("222013091305791", "234180000305791");
		imsiMap.put("222013091305800", "234180000305800");
		imsiMap.put("222013091305806", "234180000305806");
		imsiMap.put("222013091305863", "234180000305863");
		imsiMap.put("222013091305877", "234180000305877");
		imsiMap.put("222013091305882", "234180000305882");
		imsiMap.put("222013091305884", "234180000305884");
		imsiMap.put("222013091305907", "234180000305907");
		imsiMap.put("222013091305928", "234180000305928");
		imsiMap.put("222013091305931", "234180000305931");
		imsiMap.put("222013091305948", "234180000305948");
		imsiMap.put("222013091305952", "234180000305952");
		imsiMap.put("222013091305957", "234180000305957");
		imsiMap.put("222013091305958", "234180000305958");
		imsiMap.put("222013091305960", "234180000305960");
		imsiMap.put("222013091305973", "234180000305973");
		imsiMap.put("222013091305983", "234180000305983");
		imsiMap.put("222013091305990", "234180000305990");
		imsiMap.put("222013091306015", "234180000306015");
		imsiMap.put("222013091306021", "234180000306021");
		imsiMap.put("222013091306026", "234180000306026");
		imsiMap.put("222013091306075", "234180000306075");
		imsiMap.put("222013091306107", "234180000306107");
		imsiMap.put("222013091306113", "234180000306113");
		imsiMap.put("222013091306117", "234180000306117");
		imsiMap.put("222013091306122", "234180000306122");
		imsiMap.put("222013091306137", "234180000306137");
		imsiMap.put("222013091306149", "234180000306149");
		imsiMap.put("222013091306169", "234180000306169");
		imsiMap.put("222013091306175", "234180000306175");
		imsiMap.put("222013091306178", "234180000306178");
		imsiMap.put("222013091306182", "234180000306182");
		imsiMap.put("222013091306183", "234180000306183");
		imsiMap.put("222013091306199", "234180000306199");
		imsiMap.put("222013091306200", "234180000306200");
		imsiMap.put("222013091306203", "234180000306203");
		imsiMap.put("222013091306210", "234180000306210");
		imsiMap.put("222013091306232", "234180000306232");
		imsiMap.put("222013091306280", "234180000306280");
		imsiMap.put("222013091306282", "234180000306282");
		imsiMap.put("222013091306283", "234180000306283");
		imsiMap.put("222013091306290", "234180000306290");
		imsiMap.put("222013091306295", "234180000306295");
		imsiMap.put("222013091306299", "234180000306299");
		imsiMap.put("222013091306303", "234180000306303");
		imsiMap.put("222013091306353", "234180000306353");
		imsiMap.put("222013091306383", "234180000306383");
		imsiMap.put("222013091306385", "234180000306385");
		imsiMap.put("222013091306420", "234180000306420");
		imsiMap.put("222013091306484", "234180000306484");
		imsiMap.put("222013091306507", "234180000306507");
		imsiMap.put("222013091306539", "234180000306539");
		imsiMap.put("222013091306552", "234180000306552");
		imsiMap.put("222013091306567", "234180000306567");
		imsiMap.put("222013091306569", "234180000306569");
		imsiMap.put("222013091306574", "234180000306574");
		imsiMap.put("222013091306578", "234180000306578");
		imsiMap.put("222013091306583", "234180000306583");
		imsiMap.put("222013091306593", "234180000306593");
		imsiMap.put("222013091306598", "234180000306598");
		imsiMap.put("222013091306603", "234180000306603");
		imsiMap.put("222013091306604", "234180000306604");
		imsiMap.put("222013091306606", "234180000306606");
		imsiMap.put("222013091306612", "234180000306612");
		imsiMap.put("222013091306626", "234180000306626");
		imsiMap.put("222013091306633", "234180000306633");
		imsiMap.put("222013091306656", "234180000306656");
		imsiMap.put("222013091306673", "234180000306673");
		imsiMap.put("222013091306700", "234180000306700");
		imsiMap.put("222013091306706", "234180000306706");
		imsiMap.put("222013091306712", "234180000306712");
		imsiMap.put("222013091306715", "234180000306715");
		imsiMap.put("222013091306717", "234180000306717");
		imsiMap.put("222013091306721", "234180000306721");
		imsiMap.put("222013091306724", "234180000306724");
		imsiMap.put("222013091306738", "234180000306738");
		imsiMap.put("222013091306742", "234180000306742");
		imsiMap.put("222013091306754", "234180000306754");
		imsiMap.put("222013091306759", "234180000306759");
		imsiMap.put("222013091306760", "234180000306760");
		imsiMap.put("222013091306762", "234180000306762");
		imsiMap.put("222013091306787", "234180000306787");
		imsiMap.put("222013091306791", "234180000306791");
		imsiMap.put("222013091306798", "234180000306798");
		imsiMap.put("222013091306803", "234180000306803");
		imsiMap.put("222013091306807", "234180000306807");
		imsiMap.put("222013091306817", "234180000306817");
		imsiMap.put("222013091306820", "234180000306820");
		imsiMap.put("222013091306838", "234180000306838");
		imsiMap.put("222013091306847", "234180000306847");
		imsiMap.put("222013091306876", "234180000306876");
		imsiMap.put("222013091306895", "234180000306895");
		imsiMap.put("222013091306899", "234180000306899");
		imsiMap.put("222013091306928", "234180000306928");
		imsiMap.put("222013091306929", "234180000306929");
		imsiMap.put("222013091306940", "234180000306940");
		imsiMap.put("222013091306959", "234180000306959");
		imsiMap.put("222013091307003", "234180000307003");
		imsiMap.put("222013091307011", "234180000307011");
		imsiMap.put("222013091307020", "234180000307020");
		imsiMap.put("222013091307036", "234180000307036");
		imsiMap.put("222013091307037", "234180000307037");
		imsiMap.put("222013091307038", "234180000307038");
		imsiMap.put("222013091307039", "234180000307039");
		imsiMap.put("222013091307042", "234180000307042");
		imsiMap.put("222013091307043", "234180000307043");
		imsiMap.put("222013091307056", "234180000307056");
		imsiMap.put("222013091307063", "234180000307063");
		imsiMap.put("222013091307075", "234180000307075");
		imsiMap.put("222013091307079", "234180000307079");
		imsiMap.put("222013091307106", "234180000307106");
		imsiMap.put("222013091307111", "234180000307111");
		imsiMap.put("222013091307173", "234180000307173");
		imsiMap.put("222013091307174", "234180000307174");
		imsiMap.put("222013091307188", "234180000307188");
		imsiMap.put("222013091307232", "234180000307232");
		imsiMap.put("222013091307253", "234180000307253");
		imsiMap.put("222013091307258", "234180000307258");
		imsiMap.put("222013091307268", "234180000307268");
		imsiMap.put("222013091307270", "234180000307270");
		imsiMap.put("222013091307271", "234180000307271");
		imsiMap.put("222013091307272", "234180000307272");
		imsiMap.put("222013091307279", "234180000307279");
		imsiMap.put("222013091307291", "234180000307291");
		imsiMap.put("222013091307296", "234180000307296");
		imsiMap.put("222013091307307", "234180000307307");
		imsiMap.put("222013091307310", "234180000307310");
		imsiMap.put("222013091307314", "234180000307314");
		imsiMap.put("222013091307325", "234180000307325");
		imsiMap.put("222013091307354", "234180000307354");
		imsiMap.put("222013091307359", "234180000307359");
		imsiMap.put("222013091307369", "234180000307369");
		imsiMap.put("222013091307380", "234180000307380");
		imsiMap.put("222013091307389", "234180000307389");
		imsiMap.put("222013091307410", "234180000307410");
		imsiMap.put("222013091307414", "234180000307414");
		imsiMap.put("222013091307417", "234180000307417");
		imsiMap.put("222013091307419", "234180000307419");
		imsiMap.put("222013091307421", "234180000307421");
		imsiMap.put("222013091307425", "234180000307425");
		imsiMap.put("222013091307432", "234180000307432");
		imsiMap.put("222013091307448", "234180000307448");
		imsiMap.put("222013091307453", "234180000307453");
		imsiMap.put("222013091307454", "234180000307454");
		imsiMap.put("222013091307464", "234180000307464");
		imsiMap.put("222013091307480", "234180000307480");
		imsiMap.put("222013091307506", "234180000307506");
		imsiMap.put("222013091307513", "234180000307513");
		imsiMap.put("222013091307519", "234180000307519");
		imsiMap.put("222013091307530", "234180000307530");
		imsiMap.put("222013091307533", "234180000307533");
		imsiMap.put("222013091307536", "234180000307536");
		imsiMap.put("222013091307538", "234180000307538");
		imsiMap.put("222013091307544", "234180000307544");
		imsiMap.put("222013091307545", "234180000307545");
		imsiMap.put("222013091307553", "234180000307553");
		imsiMap.put("222013091307563", "234180000307563");
		imsiMap.put("222013091307566", "234180000307566");
		imsiMap.put("222013091307567", "234180000307567");
		imsiMap.put("222013091307595", "234180000307595");
		imsiMap.put("222013091307598", "234180000307598");
		imsiMap.put("222013091307620", "234180000307620");
		imsiMap.put("222013091307652", "234180000307652");
		imsiMap.put("222013091307653", "234180000307653");
		imsiMap.put("222013091307655", "234180000307655");
		imsiMap.put("222013091307666", "234180000307666");
		imsiMap.put("222013091307697", "234180000307697");
		imsiMap.put("222013091307710", "234180000307710");
		imsiMap.put("222013091307714", "234180000307714");
		imsiMap.put("222013091307717", "234180000307717");
		imsiMap.put("222013091307735", "234180000307735");
		imsiMap.put("222013091307756", "234180000307756");
		imsiMap.put("222013091307813", "234180000307813");
		imsiMap.put("222013091307816", "234180000307816");
		imsiMap.put("222013091307830", "234180000307830");
		imsiMap.put("222013091307834", "234180000307834");
		imsiMap.put("222013091307835", "234180000307835");
		imsiMap.put("222013091307836", "234180000307836");
		imsiMap.put("222013091307841", "234180000307841");
		imsiMap.put("222013091307860", "234180000307860");
		imsiMap.put("222013091307862", "234180000307862");
		imsiMap.put("222013091307863", "234180000307863");
		imsiMap.put("222013091307865", "234180000307865");
		imsiMap.put("222013091307874", "234180000307874");
		imsiMap.put("222013091307879", "234180000307879");
		imsiMap.put("222013091307881", "234180000307881");
		imsiMap.put("222013091307887", "234180000307887");
		imsiMap.put("222013091307890", "234180000307890");
		imsiMap.put("222013091307905", "234180000307905");
		imsiMap.put("222013091307932", "234180000307932");
		imsiMap.put("222013091307934", "234180000307934");
		imsiMap.put("222013091307937", "234180000307937");
		imsiMap.put("222013091307941", "234180000307941");
		imsiMap.put("222013091307983", "234180000307983");
		imsiMap.put("222013091307992", "234180000307992");
		imsiMap.put("222013091308001", "234180000308001");
		imsiMap.put("222013091308019", "234180000308019");
		imsiMap.put("222013091308025", "234180000308025");
		imsiMap.put("222013091308036", "234180000308036");
		imsiMap.put("222013091308041", "234180000308041");
		imsiMap.put("222013091308053", "234180000308053");
		imsiMap.put("222013091308070", "234180000308070");
		imsiMap.put("222013091308086", "234180000308086");
		imsiMap.put("222013091308089", "234180000308089");
		imsiMap.put("222013091308096", "234180000308096");
		imsiMap.put("222013091308097", "234180000308097");
		imsiMap.put("222013091308098", "234180000308098");
		imsiMap.put("222013091308109", "234180000308109");
		imsiMap.put("222013091308122", "234180000308122");
		imsiMap.put("222013091308151", "234180000308151");
		imsiMap.put("222013091308176", "234180000308176");
		imsiMap.put("222013091308178", "234180000308178");
		imsiMap.put("222013091308180", "234180000308180");
		imsiMap.put("222013091308195", "234180000308195");
		imsiMap.put("222013091308242", "234180000308242");
		imsiMap.put("222013091308387", "234180000308387");
		imsiMap.put("222013091308439", "234180000308439");
		imsiMap.put("222013091308445", "234180000308445");
		imsiMap.put("222013091308455", "234180000308455");
		imsiMap.put("222013091308472", "234180000308472");
		imsiMap.put("222013091308475", "234180000308475");
		imsiMap.put("222013091308477", "234180000308477");
		imsiMap.put("222013091308506", "234180000308506");
		imsiMap.put("222013091308511", "234180000308511");
		imsiMap.put("222013091308529", "234180000308529");
		imsiMap.put("222013091308533", "234180000308533");
		imsiMap.put("222013091308556", "234180000308556");
		imsiMap.put("222013091308593", "234180000308593");
		imsiMap.put("222013091308886", "234180000308886");
		imsiMap.put("222013091308888", "234180000308888");
		imsiMap.put("222013091308902", "234180000308902");
		imsiMap.put("222013091308903", "234180000308903");
		imsiMap.put("222013091308905", "234180000308905");
		imsiMap.put("222013091308914", "234180000308914");
		imsiMap.put("222013091308939", "234180000308939");
		imsiMap.put("222013091308945", "234180000308945");
		imsiMap.put("222013091308960", "234180000308960");
		imsiMap.put("222013091308967", "234180000308967");
		imsiMap.put("222013091308970", "234180000308970");
		imsiMap.put("222013091308976", "234180000308976");
		imsiMap.put("222013091308996", "234180000308996");
		imsiMap.put("222013091309014", "234180000309014");
		imsiMap.put("222013091309022", "234180000309022");
		imsiMap.put("222013091309029", "234180000309029");
		imsiMap.put("222013091309037", "234180000309037");
		imsiMap.put("222013091309038", "234180000309038");
		imsiMap.put("222013091309047", "234180000309047");
		imsiMap.put("222013091309077", "234180000309077");
		imsiMap.put("222013091309093", "234180000309093");
		imsiMap.put("222013091309113", "234180000309113");
		imsiMap.put("222013091309129", "234180000309129");
		imsiMap.put("222013091309147", "234180000309147");
		imsiMap.put("222013091309150", "234180000309150");
		imsiMap.put("222013091309160", "234180000309160");
		imsiMap.put("222013091309192", "234180000309192");
		imsiMap.put("222013091309195", "234180000309195");
		imsiMap.put("222013091309203", "234180000309203");
		imsiMap.put("222013091309222", "234180000309222");
		imsiMap.put("222013091309246", "234180000309246");
		imsiMap.put("222013091309259", "234180000309259");
		imsiMap.put("222013091309266", "234180000309266");
		imsiMap.put("222013091309272", "234180000309272");
		imsiMap.put("222013091309291", "234180000309291");
		imsiMap.put("222013091309296", "234180000309296");
		imsiMap.put("222013091309303", "234180000309303");
		imsiMap.put("222013091309311", "234180000309311");
		imsiMap.put("222013091309313", "234180000309313");
		imsiMap.put("222013091309314", "234180000309314");
		imsiMap.put("222013091309319", "234180000309319");
		imsiMap.put("222013091309338", "234180000309338");
		imsiMap.put("222013091309340", "234180000309340");
		imsiMap.put("222013091309341", "234180000309341");
		imsiMap.put("222013091309343", "234180000309343");
		imsiMap.put("222013091309346", "234180000309346");
		imsiMap.put("222013091309362", "234180000309362");
		imsiMap.put("222013091309381", "234180000309381");
		imsiMap.put("222013091309400", "234180000309400");
		imsiMap.put("222013091309413", "234180000309413");
		imsiMap.put("222013091309425", "234180000309425");
		imsiMap.put("222013091309427", "234180000309427");
		imsiMap.put("222013091309430", "234180000309430");
		imsiMap.put("222013091309433", "234180000309433");
		imsiMap.put("222013091309434", "234180000309434");
		imsiMap.put("222013091309437", "234180000309437");
		imsiMap.put("222013091309440", "234180000309440");
		imsiMap.put("222013091309442", "234180000309442");
		imsiMap.put("222013091309460", "234180000309460");
		imsiMap.put("222013091309463", "234180000309463");
		imsiMap.put("222013091309468", "234180000309468");
		imsiMap.put("222013091309479", "234180000309479");
		imsiMap.put("222013091309483", "234180000309483");
		imsiMap.put("222013091309530", "234180000309530");
		imsiMap.put("222013091309532", "234180000309532");
		imsiMap.put("222013091309537", "234180000309537");
		imsiMap.put("222013091309539", "234180000309539");
		imsiMap.put("222013091309541", "234180000309541");
		imsiMap.put("222013091309555", "234180000309555");
		imsiMap.put("222013091309576", "234180000309576");
		imsiMap.put("222013091309584", "234180000309584");
		imsiMap.put("222013091309593", "234180000309593");
		imsiMap.put("222013091309594", "234180000309594");
		imsiMap.put("222013091309595", "234180000309595");
		imsiMap.put("222013091309596", "234180000309596");
		imsiMap.put("222013091309601", "234180000309601");
		imsiMap.put("222013091309615", "234180000309615");
		imsiMap.put("222013091309621", "234180000309621");
		imsiMap.put("222013091309639", "234180000309639");
		imsiMap.put("222013091309641", "234180000309641");
		imsiMap.put("222013091309642", "234180000309642");
		imsiMap.put("222013091309664", "234180000309664");
		imsiMap.put("222013091309681", "234180000309681");
		imsiMap.put("222013091309683", "234180000309683");
		imsiMap.put("222013091309706", "234180000309706");
		imsiMap.put("222013091309724", "234180000309724");
		imsiMap.put("222013091309730", "234180000309730");
		imsiMap.put("222013091309738", "234180000309738");
		imsiMap.put("222013091309749", "234180000309749");
		imsiMap.put("222013091309752", "234180000309752");
		imsiMap.put("222013091309765", "234180000309765");
		imsiMap.put("222013091309841", "234180000309841");
		imsiMap.put("222013091309844", "234180000309844");
		imsiMap.put("222013091309852", "234180000309852");
		imsiMap.put("222013091309888", "234180000309888");
		imsiMap.put("222013091309890", "234180000309890");
		imsiMap.put("222013091309913", "234180000309913");
		imsiMap.put("222013091309928", "234180000309928");
		imsiMap.put("222013091309935", "234180000309935");
		imsiMap.put("222013091309943", "234180000309943");
		imsiMap.put("222013091309949", "234180000309949");
		imsiMap.put("222013091309964", "234180000309964");
		imsiMap.put("222013091309967", "234180000309967");
		imsiMap.put("222013091309988", "234180000309988");
		imsiMap.put("222013091309996", "234180000309996");
		imsiMap.put("222013091310009", "234180000310009");
		imsiMap.put("222013091310024", "234180000310024");
		imsiMap.put("222013091310040", "234180000310040");
		imsiMap.put("222013091310091", "234180000310091");
		imsiMap.put("222013091310093", "234180000310093");
		imsiMap.put("222013091310102", "234180000310102");
		imsiMap.put("222013091310112", "234180000310112");
		imsiMap.put("222013091310116", "234180000310116");
		imsiMap.put("222013091310311", "234180000310311");
		imsiMap.put("222013091310352", "234180000310352");
		imsiMap.put("222013091310353", "234180000310353");
		imsiMap.put("222013091310375", "234180000310375");
		imsiMap.put("222013091310379", "234180000310379");
		imsiMap.put("222013091310383", "234180000310383");
		imsiMap.put("222013091310409", "234180000310409");
		imsiMap.put("222013091310434", "234180000310434");
		imsiMap.put("222013091310445", "234180000310445");
		imsiMap.put("222013091310447", "234180000310447");
		imsiMap.put("222013091310454", "234180000310454");
		imsiMap.put("222013091310456", "234180000310456");
		imsiMap.put("222013091310469", "234180000310469");
		imsiMap.put("222013091310481", "234180000310481");
		imsiMap.put("222013091310512", "234180000310512");
		imsiMap.put("222013091310520", "234180000310520");
		imsiMap.put("222013091310522", "234180000310522");
		imsiMap.put("222013091310534", "234180000310534");
		imsiMap.put("222013091310573", "234180000310573");
		imsiMap.put("222013091312996", "234180000312996");
		imsiMap.put("222013091312997", "234180000312997");
		imsiMap.put("222013091313002", "234180000313002");
		imsiMap.put("222013091313003", "234180000313003");
		imsiMap.put("222013091313005", "234180000313005");
		imsiMap.put("222013091313008", "234180000313008");
		imsiMap.put("222013091313009", "234180000313009");
		imsiMap.put("222013091313010", "234180000313010");
		imsiMap.put("222013091313012", "234180000313012");
		imsiMap.put("222013091313015", "234180000313015");
		imsiMap.put("222013091313020", "234180000313020");
		imsiMap.put("222013091313023", "234180000313023");
		imsiMap.put("222013091313024", "234180000313024");
		imsiMap.put("222013091313026", "234180000313026");
		imsiMap.put("222013091313027", "234180000313027");
		imsiMap.put("222013091313028", "234180000313028");
		imsiMap.put("222013091313032", "234180000313032");
		imsiMap.put("222013091313033", "234180000313033");
		imsiMap.put("222013091313063", "234180000313063");
		imsiMap.put("222013091313073", "234180000313073");
		imsiMap.put("222013091313074", "234180000313074");
		imsiMap.put("222013091313104", "234180000313104");
		imsiMap.put("222013091313105", "234180000313105");
		imsiMap.put("222013091313107", "234180000313107");
		imsiMap.put("222013091313120", "234180000313120");
		imsiMap.put("222013091313121", "234180000313121");
		imsiMap.put("222013091313122", "234180000313122");
		imsiMap.put("222013091313124", "234180000313124");
		imsiMap.put("222013091313172", "234180000313172");
		imsiMap.put("222013091313199", "234180000313199");
		imsiMap.put("222013091313218", "234180000313218");
		imsiMap.put("222013091313246", "234180000313246");
		imsiMap.put("222013091313247", "234180000313247");
		imsiMap.put("222013091313250", "234180000313250");
		imsiMap.put("222013091313251", "234180000313251");
		imsiMap.put("222013091313252", "234180000313252");
		imsiMap.put("222013091313253", "234180000313253");
		imsiMap.put("222013091313325", "234180000313325");
		imsiMap.put("222013091313336", "234180000313336");
		imsiMap.put("222013091313342", "234180000313342");
		imsiMap.put("222013091313380", "234180000313380");
		imsiMap.put("222013091313383", "234180000313383");
		imsiMap.put("222013091313385", "234180000313385");
		imsiMap.put("222013091313390", "234180000313390");
		imsiMap.put("222013091313466", "234180000313466");
		imsiMap.put("222013091313476", "234180000313476");
		imsiMap.put("222013091313478", "234180000313478");
		imsiMap.put("222013091313481", "234180000313481");
		imsiMap.put("222013091313489", "234180000313489");
		imsiMap.put("222013091313490", "234180000313490");
		imsiMap.put("222013091313492", "234180000313492");
		imsiMap.put("222013091313497", "234180000313497");
		imsiMap.put("222013091313502", "234180000313502");
		imsiMap.put("222013091313505", "234180000313505");
		imsiMap.put("222013091313506", "234180000313506");
		imsiMap.put("222013091313510", "234180000313510");
		imsiMap.put("222013091313513", "234180000313513");
		imsiMap.put("222013091313516", "234180000313516");
		imsiMap.put("222013091313520", "234180000313520");
		imsiMap.put("222013091313522", "234180000313522");
		imsiMap.put("222013091313523", "234180000313523");
		imsiMap.put("222013091313527", "234180000313527");
		imsiMap.put("222013091313569", "234180000313569");
		imsiMap.put("222013091313572", "234180000313572");
		imsiMap.put("222013091313623", "234180000313623");
		imsiMap.put("222013091313625", "234180000313625");
		imsiMap.put("222013091313628", "234180000313628");
		imsiMap.put("222013091313639", "234180000313639");
		imsiMap.put("222013091313697", "234180000313697");
		imsiMap.put("222013091313698", "234180000313698");
		imsiMap.put("222013091313701", "234180000313701");
		imsiMap.put("222013091313706", "234180000313706");
		imsiMap.put("222013091313709", "234180000313709");
		imsiMap.put("222013091313712", "234180000313712");
		imsiMap.put("222013091313716", "234180000313716");
		imsiMap.put("222013091313719", "234180000313719");
		imsiMap.put("222013091313720", "234180000313720");
		imsiMap.put("222013091313722", "234180000313722");
		imsiMap.put("222013091313724", "234180000313724");
		imsiMap.put("222013091313725", "234180000313725");
		imsiMap.put("222013091313726", "234180000313726");
		imsiMap.put("222013091313728", "234180000313728");
		imsiMap.put("222013091313730", "234180000313730");
		imsiMap.put("222013091313731", "234180000313731");
		imsiMap.put("222013091313733", "234180000313733");
		imsiMap.put("222013091313734", "234180000313734");
		imsiMap.put("222013091313735", "234180000313735");
		imsiMap.put("222013091313736", "234180000313736");
		imsiMap.put("222013091313737", "234180000313737");
		imsiMap.put("222013091313793", "234180000313793");
		imsiMap.put("222013091313808", "234180000313808");
		imsiMap.put("222013091313812", "234180000313812");
		imsiMap.put("222013091313813", "234180000313813");
		imsiMap.put("222013091313825", "234180000313825");
		imsiMap.put("222013091313826", "234180000313826");
		imsiMap.put("222013091313844", "234180000313844");
		imsiMap.put("222013091313845", "234180000313845");
		imsiMap.put("222013091313846", "234180000313846");
		imsiMap.put("222013091313847", "234180000313847");
		imsiMap.put("222013091313882", "234180000313882");
		imsiMap.put("222013091313938", "234180000313938");
		imsiMap.put("222013091313942", "234180000313942");
		imsiMap.put("222013091313947", "234180000313947");
		imsiMap.put("222013091313949", "234180000313949");
		imsiMap.put("222013091313951", "234180000313951");
		imsiMap.put("222013091314056", "234180000314056");
		imsiMap.put("222013091314057", "234180000314057");
		imsiMap.put("222013091314068", "234180000314068");
		imsiMap.put("222013091314071", "234180000314071");
		imsiMap.put("222013091314076", "234180000314076");
		imsiMap.put("222013091314077", "234180000314077");
		imsiMap.put("222013091314078", "234180000314078");
		imsiMap.put("222013091314080", "234180000314080");
		imsiMap.put("222013091314089", "234180000314089");
		imsiMap.put("222013091314092", "234180000314092");
		imsiMap.put("222013091314093", "234180000314093");
		imsiMap.put("222013091314094", "234180000314094");
		imsiMap.put("222013091315209", "234180000315209");
		imsiMap.put("222013091315224", "234180000315224");
		imsiMap.put("222013091316979", "234180000316979");
		imsiMap.put("222013091317641", "234180000317641");
		imsiMap.put("222013091328978", "234180000328978");
		imsiMap.put("222013091328982", "234180000328982");
		imsiMap.put("222013091328996", "234180000328996");
		imsiMap.put("222013091329050", "234180000329050");
		imsiMap.put("222013091329054", "234180000329054");
		imsiMap.put("222013091329074", "234180000329074");
		imsiMap.put("222013091329105", "234180000329105");
		imsiMap.put("222013091329126", "234180000329126");
		imsiMap.put("222013091329177", "234180000329177");
		imsiMap.put("222013091329196", "234180000329196");
		imsiMap.put("222013091329207", "234180000329207");
		imsiMap.put("222013091329245", "234180000329245");
		imsiMap.put("222013091329281", "234180000329281");
		imsiMap.put("222013091329283", "234180000329283");
		imsiMap.put("222013091329294", "234180000329294");
		imsiMap.put("222013091329315", "234180000329315");
		imsiMap.put("222013091329354", "234180000329354");
		imsiMap.put("222013091329356", "234180000329356");
		imsiMap.put("222013091329390", "234180000329390");
		imsiMap.put("222013091329391", "234180000329391");
		imsiMap.put("222013091329395", "234180000329395");
		imsiMap.put("222013091329397", "234180000329397");
		imsiMap.put("222013091329418", "234180000329418");
		imsiMap.put("222013091329489", "234180000329489");
		imsiMap.put("222013091329509", "234180000329509");
		imsiMap.put("222013091329562", "234180000329562");
		imsiMap.put("222013091329579", "234180000329579");
		imsiMap.put("222013091329596", "234180000329596");
		imsiMap.put("222013091329643", "234180000329643");
		imsiMap.put("222013091329674", "234180000329674");
		imsiMap.put("222013091329694", "234180000329694");
		imsiMap.put("222013091329705", "234180000329705");
		imsiMap.put("222013091329731", "234180000329731");
		imsiMap.put("222013091329733", "234180000329733");
		imsiMap.put("222013091329744", "234180000329744");
		imsiMap.put("222013091329776", "234180000329776");
		imsiMap.put("222013091329787", "234180000329787");
		imsiMap.put("222013091329843", "234180000329843");
		imsiMap.put("222013091329966", "234180000329966");
		imsiMap.put("222013091340778", "234180000340778");
		imsiMap.put("222013091340803", "234180000340803");
		imsiMap.put("222013091340818", "234180000340818");
		imsiMap.put("222013091340892", "234180000340892");
		imsiMap.put("222013091340900", "234180000340900");
		imsiMap.put("222013091340904", "234180000340904");
		imsiMap.put("222013091340910", "234180000340910");
		imsiMap.put("222013091346036", "234180000346036");
		imsiMap.put("222013091346038", "234180000346038");
		imsiMap.put("222013091346039", "234180000346039");
		imsiMap.put("222013091346043", "234180000346043");
		imsiMap.put("222013091346403", "234180000346403");
		imsiMap.put("222013091346435", "234180000346435");
		imsiMap.put("222013091346436", "234180000346436");
		imsiMap.put("222013091346454", "234180000346454");
		imsiMap.put("222013091346471", "234180000346471");
		imsiMap.put("222013091346512", "234180000346512");
		imsiMap.put("222013091346589", "234180000346589");
		imsiMap.put("222013091346598", "234180000346598");
		imsiMap.put("222013091346599", "234180000346599");
		imsiMap.put("222013091346625", "234180000346625");
		imsiMap.put("222013091346644", "234180000346644");
		imsiMap.put("222013091346654", "234180000346654");
		imsiMap.put("222013091346675", "234180000346675");
		imsiMap.put("222013091346685", "234180000346685");
		imsiMap.put("222013091346703", "234180000346703");
		imsiMap.put("222013091346772", "234180000346772");
		imsiMap.put("222013091346789", "234180000346789");
		imsiMap.put("222013091346799", "234180000346799");
		imsiMap.put("222013091346837", "234180000346837");
		imsiMap.put("222013091346838", "234180000346838");
		imsiMap.put("222013091346845", "234180000346845");
		imsiMap.put("222013091346875", "234180000346875");
		imsiMap.put("222013091346884", "234180000346884");
		imsiMap.put("222013091346892", "234180000346892");
		imsiMap.put("222013091346898", "234180000346898");
		imsiMap.put("222013091346941", "234180000346941");
		imsiMap.put("222013091346964", "234180000346964");
		imsiMap.put("222013091350502", "234180000350502");
		imsiMap.put("222013091350503", "234180000350503");
		imsiMap.put("222013091350505", "234180000350505");
		imsiMap.put("222013091350523", "234180000350523");
		imsiMap.put("222013091350534", "234180000350534");
		imsiMap.put("222013091350541", "234180000350541");
		imsiMap.put("222013091350542", "234180000350542");
		imsiMap.put("222013091350548", "234180000350548");
		imsiMap.put("222013091350549", "234180000350549");
		imsiMap.put("222013091350552", "234180000350552");
		imsiMap.put("222013091350556", "234180000350556");
		imsiMap.put("222013091350585", "234180000350585");
		imsiMap.put("222013091350586", "234180000350586");
		imsiMap.put("222013091350588", "234180000350588");
		imsiMap.put("222013091350589", "234180000350589");
		imsiMap.put("222013091350969", "234180000350969");
		imsiMap.put("222013091351021", "234180000351021");
		imsiMap.put("222013091351022", "234180000351022");
		imsiMap.put("222013091351062", "234180000351062");
		imsiMap.put("222013091351110", "234180000351110");
		imsiMap.put("222013091351119", "234180000351119");
		imsiMap.put("222013091351121", "234180000351121");
		imsiMap.put("222013091351142", "234180000351142");
		imsiMap.put("222013091351144", "234180000351144");
		imsiMap.put("222013091351177", "234180000351177");
		imsiMap.put("222013091351187", "234180000351187");
		imsiMap.put("222013091351199", "234180000351199");
		imsiMap.put("222013091351216", "234180000351216");
		imsiMap.put("222013091351222", "234180000351222");
		imsiMap.put("222013091351229", "234180000351229");
		imsiMap.put("222013091351261", "234180000351261");
		imsiMap.put("222013091351290", "234180000351290");
		imsiMap.put("222013091351391", "234180000351391");
		imsiMap.put("222013091351396", "234180000351396");
		imsiMap.put("222013091351397", "234180000351397");
		imsiMap.put("222013091351400", "234180000351400");
		imsiMap.put("222013091351404", "234180000351404");
		imsiMap.put("222013091351415", "234180000351415");
		imsiMap.put("222013091351416", "234180000351416");
		imsiMap.put("222013091351428", "234180000351428");
		imsiMap.put("222013091351429", "234180000351429");
		imsiMap.put("222013091351436", "234180000351436");
		imsiMap.put("222013091351438", "234180000351438");
		imsiMap.put("222013091351440", "234180000351440");
		imsiMap.put("222013091351441", "234180000351441");
		imsiMap.put("222013091351443", "234180000351443");
		imsiMap.put("222013091351445", "234180000351445");
		imsiMap.put("222013091351446", "234180000351446");
		imsiMap.put("222013091351454", "234180000351454");
		imsiMap.put("222013091351457", "234180000351457");
		imsiMap.put("222013091351459", "234180000351459");
		imsiMap.put("222013091351466", "234180000351466");
		imsiMap.put("222013091351469", "234180000351469");
		imsiMap.put("222013091351472", "234180000351472");
		imsiMap.put("222013091351480", "234180000351480");
		imsiMap.put("222013091351486", "234180000351486");
		imsiMap.put("222013091351490", "234180000351490");
		imsiMap.put("222013091351491", "234180000351491");
		imsiMap.put("222013091351492", "234180000351492");
		imsiMap.put("222013091351497", "234180000351497");
		imsiMap.put("222013091351498", "234180000351498");
		imsiMap.put("222013091352600", "234180000352600");
		imsiMap.put("222013091352605", "234180000352605");
		imsiMap.put("222013091352607", "234180000352607");
		imsiMap.put("222013091352608", "234180000352608");
		imsiMap.put("222013091352609", "234180000352609");
		imsiMap.put("222013091352612", "234180000352612");
		imsiMap.put("222013091352614", "234180000352614");
		imsiMap.put("222013091352618", "234180000352618");
		imsiMap.put("222013091352619", "234180000352619");
		imsiMap.put("222013091352620", "234180000352620");
		imsiMap.put("222013091352621", "234180000352621");
		imsiMap.put("222013091352622", "234180000352622");
		imsiMap.put("222013091352623", "234180000352623");
		imsiMap.put("222013091352624", "234180000352624");
		imsiMap.put("222013091352625", "234180000352625");
		imsiMap.put("222013091352628", "234180000352628");
		imsiMap.put("222013091352658", "234180000352658");
		imsiMap.put("222013091352663", "234180000352663");
		imsiMap.put("222013091352665", "234180000352665");
		imsiMap.put("222013091352677", "234180000352677");
		imsiMap.put("222013091352699", "234180000352699");
		imsiMap.put("222013091352700", "234180000352700");
		imsiMap.put("222013091352831", "234180000352831");
		imsiMap.put("222013091352834", "234180000352834");
		imsiMap.put("222013091352835", "234180000352835");
		imsiMap.put("222013091352836", "234180000352836");
		imsiMap.put("222013091352837", "234180000352837");
		imsiMap.put("222013091352838", "234180000352838");
		imsiMap.put("222013091352840", "234180000352840");
		imsiMap.put("222013091352842", "234180000352842");
		imsiMap.put("222013091352846", "234180000352846");
		imsiMap.put("222013091352847", "234180000352847");
		imsiMap.put("222013091352849", "234180000352849");
		imsiMap.put("222013091352853", "234180000352853");
		imsiMap.put("222013091352857", "234180000352857");
		imsiMap.put("222013091352859", "234180000352859");
		imsiMap.put("222013091352861", "234180000352861");
		imsiMap.put("222013091352862", "234180000352862");
		imsiMap.put("222013091352864", "234180000352864");
		imsiMap.put("222013091352867", "234180000352867");
		imsiMap.put("222013091352868", "234180000352868");
		imsiMap.put("222013091352869", "234180000352869");
		imsiMap.put("222013091352871", "234180000352871");
		imsiMap.put("222013091352872", "234180000352872");
		imsiMap.put("222013091352874", "234180000352874");
		imsiMap.put("222013091352876", "234180000352876");
		imsiMap.put("222013091352882", "234180000352882");
		imsiMap.put("222013091352884", "234180000352884");
		imsiMap.put("222013091352885", "234180000352885");
		imsiMap.put("222013091352886", "234180000352886");
		imsiMap.put("222013091352887", "234180000352887");
		imsiMap.put("222013091352888", "234180000352888");
		imsiMap.put("222013091352889", "234180000352889");
		imsiMap.put("222013091352890", "234180000352890");
		imsiMap.put("222013091352896", "234180000352896");
		imsiMap.put("222013091353378", "234180000353378");
		imsiMap.put("222013091353380", "234180000353380");
		imsiMap.put("222013091353381", "234180000353381");
		imsiMap.put("222013091353385", "234180000353385");
		imsiMap.put("222013091353889", "234180000353889");
		imsiMap.put("222013091356018", "234180000356018");
		imsiMap.put("222013091356025", "234180000356025");
		imsiMap.put("222013091356045", "234180000356045");
		imsiMap.put("222013091356064", "234180000356064");
		imsiMap.put("222013091356086", "234180000356086");
		imsiMap.put("222013091356090", "234180000356090");
		imsiMap.put("222013091356114", "234180000356114");
		imsiMap.put("222013091356132", "234180000356132");
		imsiMap.put("222013091356133", "234180000356133");
		imsiMap.put("222013091356144", "234180000356144");
		imsiMap.put("222013091356168", "234180000356168");
		imsiMap.put("222013091356198", "234180000356198");
		imsiMap.put("222013091356616", "234180000356616");
		imsiMap.put("222013091356662", "234180000356662");
		imsiMap.put("222013091356671", "234180000356671");
		imsiMap.put("222013091356687", "234180000356687");
		imsiMap.put("222013091356746", "234180000356746");
		imsiMap.put("222013091356748", "234180000356748");
		imsiMap.put("222013091356757", "234180000356757");
		imsiMap.put("222013091357614", "234180000357614");
		imsiMap.put("222013091357615", "234180000357615");
		imsiMap.put("222013091357819", "234180000357819");
		imsiMap.put("222013091357826", "234180000357826");
		imsiMap.put("222013091359074", "234180000359074");
		imsiMap.put("222013091359084", "234180000359084");
		imsiMap.put("222013091359105", "234180000359105");
		imsiMap.put("222013091359120", "234180000359120");
		imsiMap.put("222013091359215", "234180000359215");
		imsiMap.put("222013091359224", "234180000359224");
		imsiMap.put("222013091359230", "234180000359230");
		imsiMap.put("222013091359259", "234180000359259");
		imsiMap.put("222013091359275", "234180000359275");
		imsiMap.put("222013091359305", "234180000359305");
		imsiMap.put("222013091359308", "234180000359308");
		imsiMap.put("222013091359352", "234180000359352");
		imsiMap.put("222013091359386", "234180000359386");
		imsiMap.put("222013091359401", "234180000359401");
		imsiMap.put("222013091359404", "234180000359404");
		imsiMap.put("222013091359424", "234180000359424");
		imsiMap.put("222013091359426", "234180000359426");
		imsiMap.put("222013091359443", "234180000359443");
		imsiMap.put("222013091401216", "234180000401216");
		imsiMap.put("222013091401221", "234180000401221");
		imsiMap.put("222013091401786", "234180000401786");
		imsiMap.put("222013091404595", "234180000404595");
		imsiMap.put("222013094036379", "234180002036379");
		imsiMap.put("222013094095747", "234180004095747");
		imsiMap.put("222013094106270", "234180004106270");
		imsiMap.put("222013094106526", "234180004106526");
		imsiMap.put("222013094109408", "234180004109408");
		imsiMap.put("222013094109426", "234180004109426");
		imsiMap.put("222013094109483", "234180004109483");
		imsiMap.put("222013094109484", "234180004109484");
		imsiMap.put("222013094109630", "234180004109630");
		imsiMap.put("222013094109642", "234180004109642");
		imsiMap.put("222013094109653", "234180004109653");
		imsiMap.put("222013094109660", "234180004109660");
		imsiMap.put("222013094109662", "234180004109662");
		imsiMap.put("222013094126404", "234180004126404");
		imsiMap.put("222013094126409", "234180004126409");
		imsiMap.put("222013094126413", "234180004126413");
		imsiMap.put("222013094126415", "234180004126415");
		imsiMap.put("222013094128354", "234180004128354");
		imsiMap.put("222013094128374", "234180004128374");
		imsiMap.put("222013094128564", "234180004128564");
		imsiMap.put("222013094128565", "234180004128565");
		imsiMap.put("222013094128671", "234180004128671");
		imsiMap.put("222013094128679", "234180004128679");
		imsiMap.put("222013094128909", "234180004128909");
		imsiMap.put("222013094128919", "234180004128919");
		imsiMap.put("222013094128922", "234180004128922");
		imsiMap.put("222013094128948", "234180004128948");
		imsiMap.put("222013094128957", "234180004128957");
		imsiMap.put("222013094128959", "234180004128959");
		imsiMap.put("222013094128963", "234180004128963");
		imsiMap.put("222013094129033", "234180004129033");
		imsiMap.put("222013094129040", "234180004129040");
		imsiMap.put("222013094129053", "234180004129053");
		imsiMap.put("222013094129082", "234180004129082");
		imsiMap.put("222013094129083", "234180004129083");
		imsiMap.put("222013094129088", "234180004129088");
		imsiMap.put("222013094129095", "234180004129095");
		imsiMap.put("222013094129106", "234180004129106");
		imsiMap.put("222013094129115", "234180004129115");
		imsiMap.put("222013094129116", "234180004129116");
		imsiMap.put("222013094129117", "234180004129117");
		imsiMap.put("222013094129130", "234180004129130");
		imsiMap.put("222013094129154", "234180004129154");
		imsiMap.put("222013094129156", "234180004129156");
		imsiMap.put("222013094129157", "234180004129157");
		imsiMap.put("222013094129181", "234180004129181");
		imsiMap.put("222013094129184", "234180004129184");
		imsiMap.put("222013094129189", "234180004129189");
		imsiMap.put("222013094129213", "234180004129213");
		imsiMap.put("222013094129223", "234180004129223");
		imsiMap.put("222013094129225", "234180004129225");
		imsiMap.put("222013094141101", "234180004141101");
		imsiMap.put("222013094141132", "234180004141132");
		imsiMap.put("222013094141286", "234180004141286");
		imsiMap.put("222013094141287", "234180004141287");
		imsiMap.put("222013094141296", "234180004141296");
		imsiMap.put("222013094141348", "234180004141348");
		imsiMap.put("222013094141361", "234180004141361");
		imsiMap.put("222013094141368", "234180004141368");
		imsiMap.put("222013094141390", "234180004141390");
		imsiMap.put("222013094141399", "234180004141399");
		imsiMap.put("222013094141941", "234180004141941");
		imsiMap.put("222013094164535", "234180004164535");
		imsiMap.put("222013094164546", "234180004164546");
		imsiMap.put("222013094164558", "234180004164558");
		imsiMap.put("222013094164562", "234180004164562");
		imsiMap.put("222013094164563", "234180004164563");
		imsiMap.put("222013094164583", "234180004164583");
		imsiMap.put("222013094164593", "234180004164593");
		imsiMap.put("222013094164595", "234180004164595");
		imsiMap.put("222013094164619", "234180004164619");
		imsiMap.put("222013094164640", "234180004164640");
		imsiMap.put("222013094164667", "234180004164667");
		imsiMap.put("222013094164678", "234180004164678");
		imsiMap.put("222013094164690", "234180004164690");
		imsiMap.put("222013094164691", "234180004164691");
		imsiMap.put("222013094164701", "234180004164701");
		imsiMap.put("222013094164709", "234180004164709");
		imsiMap.put("222013094164720", "234180004164720");
		imsiMap.put("222013094164735", "234180004164735");
		imsiMap.put("222013094164736", "234180004164736");
		imsiMap.put("222013094164749", "234180004164749");
		imsiMap.put("222013094164757", "234180004164757");
		imsiMap.put("222013094164758", "234180004164758");
		imsiMap.put("222013094164759", "234180004164759");
		imsiMap.put("222013094164766", "234180004164766");
		imsiMap.put("222013094164781", "234180004164781");
		imsiMap.put("222013094164783", "234180004164783");
		imsiMap.put("222013094164785", "234180004164785");
		imsiMap.put("222013094164787", "234180004164787");
		imsiMap.put("222013094164801", "234180004164801");
		imsiMap.put("222013094164809", "234180004164809");
		imsiMap.put("222013094164812", "234180004164812");
		imsiMap.put("222013094164832", "234180004164832");
		imsiMap.put("222013094164835", "234180004164835");
		imsiMap.put("222013094164840", "234180004164840");
		imsiMap.put("222013094164844", "234180004164844");
		imsiMap.put("222013094164851", "234180004164851");
		imsiMap.put("222013094164853", "234180004164853");
		imsiMap.put("222013094164863", "234180004164863");
		imsiMap.put("222013094164865", "234180004164865");
		imsiMap.put("222013094164870", "234180004164870");
		imsiMap.put("222013094164877", "234180004164877");
		imsiMap.put("222013094164881", "234180004164881");
		imsiMap.put("222013094164894", "234180004164894");
		imsiMap.put("222013094164897", "234180004164897");
		imsiMap.put("222013094164904", "234180004164904");
		imsiMap.put("222013094164906", "234180004164906");
		imsiMap.put("222013094164907", "234180004164907");
		imsiMap.put("222013094164908", "234180004164908");
		imsiMap.put("222013094164915", "234180004164915");
		imsiMap.put("222013094164917", "234180004164917");
		imsiMap.put("222013094165906", "234180004165906");
		imsiMap.put("222013094165908", "234180004165908");
		imsiMap.put("222013094165966", "234180004165966");
		imsiMap.put("222013094166023", "234180004166023");
		imsiMap.put("222013094166024", "234180004166024");
		imsiMap.put("222013094166026", "234180004166026");
		imsiMap.put("222013094166027", "234180004166027");
		imsiMap.put("222013094166028", "234180004166028");
		imsiMap.put("222013094166030", "234180004166030");
		imsiMap.put("222013094169591", "234180004169591");
		imsiMap.put("222013094169592", "234180004169592");
		imsiMap.put("222013094169598", "234180004169598");
		return imsiMap;
	}
}
