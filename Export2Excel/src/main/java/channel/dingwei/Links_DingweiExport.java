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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bean.C9LfeuTapData;
import com.bean.DingWeiExport;
import com.bean.DingWeiMonth;
import com.dao.IC9LfeuTapDataDao;
import com.dao.IM2mCardInfoDao;
import com.util.TimeUtil;

public class Links_DingweiExport {

	public static void main(String[] args) throws IOException {
		String[] months = {"2018-11","2018-12","2019-01"};
		String path = "C:/Users/ll/Desktop/"; 
//		String path = "/home/iotota/"; 
        String fileType = "xlsx";
        String fileName = "鼎为1811-1901数据分析和TAP详单_C9测试账户";
        
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "spring-applicationContext.xml");
        String [] arr  = context.getBeanDefinitionNames();
        
        IC9LfeuTapDataDao c9LfeuTapDataDao = (IC9LfeuTapDataDao)context.getBean("IC9LfeuTapDataDao");
        IM2mCardInfoDao m2mCardInfoDao = (IM2mCardInfoDao)context.getBean("IM2mCardInfoDao");
        
        Date start1 = new Date();
        List<DingWeiExport> exports = getDingWeiExports(c9LfeuTapDataDao,m2mCardInfoDao,months);
        Date end1 = new Date();
        
        
        Date start = new Date();
        export(path,fileName,fileType,exports,months,c9LfeuTapDataDao,m2mCardInfoDao);
        Date end = new Date();
        System.out.println("查询结束，耗时:" + (end1.getTime()-start1.getTime())/1000 + " 秒");
        System.out.println("导出结束，耗时:" + (end.getTime()-start.getTime())/1000 + " 秒");

	}

	private static void export(String path, String fileName, String fileType, List<DingWeiExport> exports,String[] months, IC9LfeuTapDataDao c9LfeuTapDataDao, IM2mCardInfoDao m2mCardInfoDao) throws IOException {
        Workbook wb = null;  
        if (fileType.equals("xls")) {  
            wb = new HSSFWorkbook();  
        }  
        else if(fileType.equals("xlsx"))  
        {  
//          wb = new XSSFWorkbook();  
          wb = new SXSSFWorkbook();  
        }  
        else  
        {  
            System.out.println("您的文档格式不正确！");  
        } 
        Sheet sheet = wb.createSheet("Usage and Cost");
        XSSFCellStyle borderStyle = (XSSFCellStyle) wb.createCellStyle();
        borderStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框  
        borderStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框  
        borderStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框  
        borderStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
        
        XSSFCellStyle alignCenterStyle = (XSSFCellStyle) wb.createCellStyle();
        alignCenterStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
        //创建标题行
		Row row = (Row)sheet.createRow(0);
		createHeadRow0(row,borderStyle,alignCenterStyle,months,sheet,wb);
		row = (Row)sheet.createRow(1);
		createHeadRow1(row,borderStyle,months.length);
        
        //填充数据
		for(int i = 0;i<exports.size();i++){
			DingWeiExport export = exports.get(i);
			row = (Row)sheet.createRow(i+2);

			Cell cell = row.createCell(0);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getImsi2());
	        cell = row.createCell(1);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getFirstCdr());
	        cell = row.createCell(2);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getLastCdr());
	        
	        List<DingWeiMonth> monthUsages = export.getMonthsUsage();
	        for(int j=0;j<months.length;j++){
	        	DingWeiMonth monthUsage = getMonthUsage(monthUsages,months[j]);
	        	String totalUsage = "0",totalCost = "0";
	        	if(monthUsage != null){
	        		totalUsage = monthUsage.getTotalUsage();
	        		totalCost = monthUsage.getTotalCost();
	        	}
	            cell = row.createCell(2+j*2+1);
	            cell.setCellStyle(borderStyle);
	            cell.setCellValue(Double.parseDouble(totalUsage));
	            cell = row.createCell(2+j*2+2);
	            cell.setCellStyle(borderStyle);
	            cell.setCellValue(Double.parseDouble(totalCost));
	        }
	        
	        cell = row.createCell(2+months.length*2+1);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(Double.parseDouble(export.getTotalUsage()));
	        cell = row.createCell(2+months.length*2+2);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(Double.parseDouble(export.getTotalCost()));
		
		}
        
		sheet.setColumnWidth(0, 256*17);
		sheet.setColumnWidth(1, 256*19);
		sheet.setColumnWidth(2, 256*19);
		
        for(int x=0;x<months.length;x++){
    		sheet.setColumnWidth(2+2*x+1, 256*12);
    		sheet.setColumnWidth(2+2*x+2, 256*12);

        }

		
		sheet.setColumnWidth(2+2*months.length+1, 256*15);
		sheet.setColumnWidth(2+2*months.length+2, 256*15);
		
		
		Sheet sheet2 = wb.createSheet("Raw data");
        //创建标题行
		row = (Row)sheet2.createRow(0);
		createHeadRow(row,borderStyle);
		
		Map<String,String> imsiMap = ImsiMap.getImsimap();
		List<String> allDingWeiImsis = m2mCardInfoDao.queryAllDingWeiImsi(); //查询全部鼎为imsi，其中g的码号是222打头的
		String imsis = getAllImsis(allDingWeiImsis,imsiMap);
		String startTime = getEarliestTime(months);
		String endTime = getLatestTime(months);
		//根据imsi映射文件映射得到两个imsi
		List<C9LfeuTapData> tapList = c9LfeuTapDataDao.queryAllDingWei(imsis, startTime, endTime);


		


		//填充数据
		for(int i =0;i<tapList.size();i++){
			C9LfeuTapData tap = tapList.get(i);
			row = (Row)sheet2.createRow(i+1);

			
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
		
		
		sheet2.setColumnWidth(0, 256*17);
		sheet2.setColumnWidth(1, 256*19);
		sheet2.setColumnWidth(6, 256*15);
		sheet2.setColumnWidth(7, 256*15);
		
		
		
        //创建文件流  
        OutputStream stream2 = new FileOutputStream(path+fileName+"."+fileType);  
        //写入数据  
        wb.write(stream2);  
        //关闭文件流  
        stream2.close();
		
	}

	private static String getCardImsi(String imsi1, Map<String, String> imsiMap) {
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
		Map<String,String> imsiMap = ImsiMap.getImsimap();

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

}
