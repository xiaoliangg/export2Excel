package channel.resolve;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bean.DaysOrdersNumber;
import com.bean.Operator;
import com.bean.PackageUseDetails;
import com.bean.ResolveExport3;
import com.bean.ResolvePackageOrderDays;
import com.bean.Supplier;
import com.constant.SysConstant;
import com.dao.IResolveExport3Dao;
import com.dao.IResolvePackageOrderDaysDao;
import com.dao.proc.CountProc;
import com.dao.proc.InitAllTablesProc;
import com.util.TimeUtil;

public class Links_PartnerExport {

	public static void main(String[] args) throws Exception {
		String month = "2019-02";
		String partnerCode = "P000294";  //P000294  准儿翻译机
		
		List<String> exceptionMsg = new ArrayList<String>();
		//联通国际
		Supplier uc = new Supplier("1000", "联通国际");
		List<Supplier> sups1 = new ArrayList<Supplier>();
		sups1.add(uc);
		List<Operator> oper1 = new ArrayList<Operator>();
		//export(partnerCode,sups1,oper1,"0",month,exceptionMsg);
		
		
		//旧版docomo统计
		Supplier oldDocomo = new Supplier("1013", "旧DOCOMO");
		List<Supplier> sups2 = new ArrayList<Supplier>();
		sups2.add(oldDocomo);
		List<Operator> oper2 = new ArrayList<Operator>();
		//export(partnerCode,sups2,oper2,"0",month,exceptionMsg);
		
		
		//新版docomo统计,不包括Docomo新（翻译机转旅游）
		Supplier newDocomo = new Supplier("1024", "新DOCOMO");
		List<Supplier> sups3 = new ArrayList<Supplier>();
		sups3.add(newDocomo);
		Operator operator3 = new Operator("8056", "Docomo新（翻译机转旅游）");
		List<Operator> oper3 = new ArrayList<Operator>();
		oper3.add(operator3);
		export(partnerCode,sups3,oper3,"0",month,exceptionMsg);
		
		
		//新版docomo统计,仅包含Docomo新（翻译机转旅游）
		//export(partnerCode,sups3,oper3,"1",month,exceptionMsg);
		
		for(String s:exceptionMsg){
			System.out.println(s);
		}
        
	}

	private static void export(String partnerCode, List<Supplier> suppList, List<Operator> operList, String includeOperators,
			String month, List<String> exceptionMsg) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "spring-applicationContext.xml");
        
        InitAllTablesProc initAllTablesProc = (InitAllTablesProc)context.getBean("initAllTablesProc");
        CountProc countProc = (CountProc)context.getBean("countProc");
        
        IResolvePackageOrderDaysDao resolvePackageOrderDaysDao = (IResolvePackageOrderDaysDao)context.getBean("IResolvePackageOrderDaysDao");
        IResolveExport3Dao resolveExport3Dao = (IResolveExport3Dao)context.getBean("IResolveExport3Dao");

		//初始化所有中间表
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("IN_IMSI", "");
		initAllTablesProc.initAllTables(paramMap);
		
		//TODO 查询渠道使用的所有供应商并显示
		
		
		
		//统计指定供应商下在指定月份的码号产生的流量，与所有已结束订单(可能根据渠道筛选，可能根据号段筛选). 二级渠道细分
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime = month + "-01 00:00:00";
		Date d = df.parse(startTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.SECOND, -1);
		String endTime = df.format(calendar.getTime());
		System.out.println("===>>startTime=" + startTime + ",endTime=" + endTime);
		System.out.println("suppliers:" + getSupplerCodes(suppList));
		System.out.println("operators:" + getOperatorCodes(operList));
		System.out.println("includeOperators:" + includeOperators);
		paramMap = new HashMap<String, String>();
		paramMap.put("IN_PARTNER_CODE", partnerCode);
		paramMap.put("IN_START_TIME", startTime);
		paramMap.put("IN_END_TIME", endTime);
		paramMap.put("IN_SUPPLIERS", getSupplerCodes(suppList));
		paramMap.put("IN_OPERATORS", getOperatorCodes(operList));
		paramMap.put("IN_INCLUDE_OPERATORS", includeOperators);
		countProc.countPartnerProc(paramMap);
		
		
		String path = "C:/Users/Chen/Desktop/2/"; 
        String fileType = "xlsx";  

//		channel = SysConstant.CHANNEL_HUIZHOU;
//		//统计流量报表
//		String usageFileName = month + "_resolve惠州流量统计_" + getSupplerNames(sups1);
//        writeUsage(path, usageFileName, fileType,resolveExport3Dao,channel);
//		//统计套餐套餐报表
//        List<PackageUseDetails> details = resolvePackageOrderDaysDao.getPackageUseDetails(channel);
//        String packageFileName = month + "_resolve惠州套餐统计_" + getSupplerNames(sups1);  
//        writePackage(path, packageFileName, fileType,details,resolvePackageOrderDaysDao,channel);
       
        String channel = "1";
        List<PackageUseDetails> details = resolvePackageOrderDaysDao.getPackageUseDetails(channel);
        String fileName = month + "_准儿流量统计_" + getSupplerNames(suppList);
        if(operList.size() > 0 && "1".equals(includeOperators)) {
        	fileName += "仅含" + getOperatorNames(operList);
        }else if(operList.size() > 0 && "0".equals(includeOperators)){
        	fileName += "不包含" + getOperatorNames(operList);
        }
        String result = writeUsageAndPackage(path,fileName,fileType,details,resolvePackageOrderDaysDao,resolveExport3Dao,channel);
        exceptionMsg.add(fileName + "   "+ result);
        
	}
	

    private static String writeUsageAndPackage(String path, String fileName, String fileType, List<PackageUseDetails> details,
			IResolvePackageOrderDaysDao resolvePackageOrderDaysDao, IResolveExport3Dao resolveExport3Dao,
			String channel) throws Exception {  
        Workbook wb = null;  
        if (fileType.equals("xls")) {  
            wb = new HSSFWorkbook();  
        }  
        else if(fileType.equals("xlsx"))  
        {  
            wb = new XSSFWorkbook();  
        }  
        else  
        {  
            System.out.println("您的文档格式不正确！");  
        } 
        
        
        
        
        //流量统计sheet
        Sheet sheet = wb.createSheet("流量统计");
        XSSFCellStyle borderStyle = (XSSFCellStyle) wb.createCellStyle();
        borderStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框  
        borderStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框  
        borderStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框  
        borderStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
        
		long allSum = 0;
		long sum = 0;
		int samLines = 1;  //相同副号的行数
		boolean mergeLastRegion = false;  //是否合并之前的单元格
		long lastSum = 0;
		Row row = (Row)sheet.createRow(0);
		createHeadRow(row,borderStyle);

		List<ResolveExport3> allUsage = resolveExport3Dao.getResolveExport(channel);
		//修改格式写入文件
		ResolveExport3 export = null;
		for (int x = 0; x < allUsage.size(); x++) {
			row = (Row) sheet.createRow(x+1);
			export = allUsage.get(x);
			
			Cell cell = row.createCell(0);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getPartnerName());
	        cell = row.createCell(1);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getIccid());
	        cell = row.createCell(2);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getOrderCode());
	        cell = row.createCell(3);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getPackageName());
	        cell = row.createCell(4);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getActualStartTime());
	        cell = row.createCell(5);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getActualEndTime());
	        cell = row.createCell(6);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getLocalImsi());
	        cell = row.createCell(7);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getSupplierCode());
	        cell = row.createCell(8);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getSupplierName());
	        cell = row.createCell(9);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getOperatorCode());
	        cell = row.createCell(10);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getOperatorName());
	        cell = row.createCell(11);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(export.getCdrTime());
	        cell = row.createCell(12);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue(Long.parseLong(export.getCdrByte()));
	        cell = row.createCell(13);
	        cell.setCellStyle(borderStyle);
			
			


			if(x > 0){
				ResolveExport3 lastExport = allUsage.get(x-1);
				if(lastExport.getIccid().equals(export.getIccid()) && lastExport.getOrderCode().equals(export.getOrderCode()) && lastExport.getLocalImsi().equals(export.getLocalImsi())){
					sum = sum + Long.parseLong(export.getCdrByte());
					samLines++;
					if(x == allUsage.size() -1){ //最后一列特殊处理
						mergeLastRegion = true;
						lastSum = sum;
					}
				}else{
					mergeLastRegion = true;
					lastSum = sum;
					sum = Long.parseLong(export.getCdrByte());
				}
			}else{
				samLines = 1;
				sum = Long.parseLong(export.getCdrByte());
			}
			
			if(mergeLastRegion){
				if(x == allUsage.size() -1){ //最后一列特殊处理,合并时x为相同行的最后一行
					mergeRegion(sheet,x+1,samLines);
					cell = sheet.getRow(x-samLines+2).createCell(13);
				}else{
					mergeRegion(sheet,x,samLines); //合并时，x行为最新一行
					cell = sheet.getRow(x-samLines+1).createCell(13);
				}
		        cell.setCellStyle(borderStyle);
		        cell.setCellValue(lastSum);
				allSum = allSum + lastSum;
				
				//reset
				samLines = 1;
				mergeLastRegion = false;  //是否合并之前的单元格
				lastSum = 0;
			}
			
		}
		Row row2 = (Row) sheet.createRow(allUsage.size() + 1);
		Cell cell2 = row2.createCell(13);
		cell2.setCellValue(allSum);
		
		sheet.setColumnWidth(0, 256*9);
		sheet.setColumnWidth(1, 256*22);
		sheet.setColumnWidth(2, 256*17);
		sheet.setColumnWidth(3, 256*25);
		sheet.setColumnWidth(4, 256*19);
		sheet.setColumnWidth(5, 256*19);
		sheet.setColumnWidth(6, 256*18);
		sheet.setColumnWidth(7, 256*9);
		sheet.setColumnWidth(8, 256*14);
		sheet.setColumnWidth(9, 256*9);
		sheet.setColumnWidth(10, 256*14);
		sheet.setColumnWidth(11, 256*13);
		sheet.setColumnWidth(12, 256*20);
		sheet.setColumnWidth(13, 256*21);
		
		
		
		
		//套餐统计sheet
		//创建sheet对象  
        Sheet sheet2 = (Sheet) wb.createSheet("套餐统计 "); 
		sheet2.setColumnWidth(0, 256*33);
		sheet2.setColumnWidth(1, 256*13);
		sheet2.setColumnWidth(2, 256*17);
		sheet2.setColumnWidth(3, 256*27);
		
        CellRangeAddress region = new CellRangeAddress(0,0,0,3);
        sheet2.addMergedRegion(region);

        XSSFCellStyle alignCenterStyle = (XSSFCellStyle) wb.createCellStyle();
        alignCenterStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
        
        Cell cell = null;
		row = (Row) sheet2.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("套餐的订单数量及用量统计");
        cell.setCellStyle(alignCenterStyle);
        CellRangeAddress brodercell = new CellRangeAddress(0,0,0,3);
        setBorderForMergeCell(XSSFCellStyle.BORDER_THIN, brodercell, sheet2, wb);
        
        row = (Row) sheet2.createRow(1);
        cell = row.createCell(0);
        cell.setCellStyle(borderStyle);
        cell = row.createCell(1);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("订单数量");
        cell = row.createCell(2);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("订单平均使用流量(单位字节)");
        cell = row.createCell(3);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("总流量(单位字节)");
        
        for(int i = 0;i<details.size();i++){
            row = (Row) sheet2.createRow(1+i+1);
            cell = row.createCell(0);
            cell.setCellStyle(borderStyle);
            cell.setCellValue(details.get(i).getPackageName());
            cell = row.createCell(1);
            cell.setCellStyle(borderStyle);
            cell.setCellValue(Long.parseLong(details.get(i).getNumbert()));
            cell = row.createCell(2);
            cell.setCellStyle(borderStyle);
            if(details.get(i).getAverage() == null){
            	cell.setCellValue(0);
            }else{
            	cell.setCellValue(Long.parseLong(details.get(i).getAverage()));
            }
            cell = row.createCell(3);
            cell.setCellStyle(borderStyle);
            if(details.get(i).getSumt() == null){
            	cell.setCellValue(0);
            }else{
            	cell.setCellValue(Long.parseLong(details.get(i).getSumt()));
            }
            
        }
        
        row = (Row) sheet2.createRow(1+details.size()+1);
        cell = row.createCell(0);
        cell.setCellStyle(borderStyle);
        cell = row.createCell(1);
        cell.setCellStyle(borderStyle);
        cell.setCellValue(getSumOrder(details));
        cell = row.createCell(2);
        cell.setCellStyle(borderStyle);

        cell = row.createCell(3);
        cell.setCellStyle(borderStyle);
        cell.setCellValue(getSumUsage(details));

        
        row = (Row) sheet2.createRow(details.size()+4);
        cell = row.createCell(0);
        cell.setCellValue("注释:统计应用该套餐的所有订单中，用户实际使用天数");
        //设置合并单元格的边框
        brodercell = new CellRangeAddress(1+details.size()+3,1+details.size()+3, 0, 1);
        setBorderForMergeCell(XSSFCellStyle.BORDER_THIN, brodercell, sheet2, wb);
        //合并单元格
        region = new CellRangeAddress(1+details.size()+3,1+details.size()+3,0,1);
        sheet2.addMergedRegion(region);
        
        
        //套餐的实际使用天数的订单数量
        int rowsNumber = details.size()+6;
        int cursor = 0;
        String s = "没有统计实际使用天数的套餐:";
		for (int i = 0; i < details.size(); i++) {
			PackageUseDetails detail = details.get(i);
			//获取套餐天数
			int packageDays = getPackageDays(detail.getPackageName());
			if(packageDays == -1){
				s = s + detail.getPackageName() + ";";
				continue;
			}
			
			String packageName = detail.getPackageName();
			List<DaysOrdersNumber> numbers = resolvePackageOrderDaysDao.getDaysOrdersNumber(packageName,channel);
			//统计未使用天数
			List<ResolvePackageOrderDays> orderDays = resolvePackageOrderDaysDao.getUnusedOrdersNumber(packageName,channel);
			int unUsedNumber = orderDays.size();

			//设置套餐名行
			row = (Row) sheet2.createRow(rowsNumber + cursor + 5*i);
	        cell = row.createCell(0);
	        cell.setCellValue(packageName);
	        cell.setCellStyle(alignCenterStyle);
	        //设置合并单元格的边框
	        brodercell = new CellRangeAddress(rowsNumber + cursor + 5*i,rowsNumber + cursor + 5*i, 0, 1);
	        setBorderForMergeCell(XSSFCellStyle.BORDER_THIN, brodercell, sheet2, wb);
	        //合并单元格
	        region = new CellRangeAddress(rowsNumber + cursor + 5*i,rowsNumber + cursor + 5*i,0,1);
	        sheet2.addMergedRegion(region);
			
	        //设置
			row = (Row) sheet2.createRow(rowsNumber + cursor + 5*i + 1);
	        cell = row.createCell(0);
	        cell.setCellStyle(borderStyle);
	        cell = row.createCell(1);
	        cell.setCellValue("订单数量");
	        cell.setCellStyle(borderStyle);
	        
			row = (Row) sheet2.createRow(rowsNumber + cursor + 5*i + 2);
	        cell = row.createCell(0);
	        cell.setCellValue("总量");
	        cell.setCellStyle(borderStyle);
	        cell = row.createCell(1);
	        cell.setCellValue(Long.parseLong(detail.getNumbert()));
	        cell.setCellStyle(borderStyle);
	        
			row = (Row) sheet2.createRow(rowsNumber + cursor + 5*i + 3);
	        cell = row.createCell(0);
	        cell.setCellValue("未使用");
	        cell.setCellStyle(borderStyle);
	        cell = row.createCell(1);
	        cell.setCellValue(unUsedNumber);
	        cell.setCellStyle(borderStyle);
	        
	        int sumOrders = 0;
	        for(int x =1;x<=packageDays;x++){
				row = (Row) sheet2.createRow(rowsNumber + cursor + 5*i + 3 + x);
		        cell = row.createCell(0);
		        cell.setCellValue(x + "天");
		        cell.setCellStyle(borderStyle);
		        cell = row.createCell(1);
		        int number = getOrderNumber(numbers,x);
		        cell.setCellValue(number);
		        sumOrders = sumOrders + number;
		        cell.setCellStyle(borderStyle);
	        }
	        
	        if(Long.parseLong(detail.getNumbert()) != (unUsedNumber + sumOrders)){
	        	throw new Exception("渠道[" + channel + "]总订单数不等于各使用天数的订单数之和:" + packageName );
	        }
			cursor = cursor + packageDays;
		}
		
		
		
        //创建文件流  
        OutputStream stream2 = new FileOutputStream(path+fileName+"."+fileType);  
        //写入数据  
        wb.write(stream2);  
        //关闭文件流  
        stream2.close(); 
        return s;
    }

	private static void writePackage(String path, String fileName,String fileType,List<PackageUseDetails> details, IResolvePackageOrderDaysDao resolvePackageOrderDaysDao, String channel) throws Exception {
        //创建工作文档对象  
        Workbook wb = null;  
        if (fileType.equals("xls")) {  
            wb = new HSSFWorkbook();  
        }  
        else if(fileType.equals("xlsx"))  
        {  
            wb = new XSSFWorkbook();  
        }  
        else  
        {  
            System.out.println("您的文档格式不正确！");  
        }  
        //创建sheet对象  
        Sheet sheet2 = (Sheet) wb.createSheet(fileName); 
		sheet2.setColumnWidth(0, 256*33);
		sheet2.setColumnWidth(1, 256*13);
		sheet2.setColumnWidth(2, 256*17);
		sheet2.setColumnWidth(3, 256*27);
		
        CellRangeAddress region = new CellRangeAddress(0,0,0,3);
        sheet2.addMergedRegion(region);

        XSSFCellStyle borderStyle = (XSSFCellStyle) wb.createCellStyle();
        borderStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框  
        borderStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框  
        borderStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框  
        borderStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
        
        XSSFCellStyle alignCenterStyle = (XSSFCellStyle) wb.createCellStyle();
        alignCenterStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
        
        Row row = null;
        Cell cell = null;
		row = (Row) sheet2.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("套餐的订单数量及用量统计");
        cell.setCellStyle(alignCenterStyle);
        CellRangeAddress brodercell = new CellRangeAddress(0,0,0,3);
        setBorderForMergeCell(XSSFCellStyle.BORDER_THIN, brodercell, sheet2, wb);
        
        row = (Row) sheet2.createRow(1);
        cell = row.createCell(0);
        cell.setCellStyle(borderStyle);
        cell = row.createCell(1);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("订单数量");
        cell = row.createCell(2);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("订单平均使用流量(单位字节)");
        cell = row.createCell(3);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("总流量(单位字节)");
        
        for(int i = 0;i<details.size();i++){
            row = (Row) sheet2.createRow(1+i+1);
            cell = row.createCell(0);
            cell.setCellStyle(borderStyle);
            cell.setCellValue(details.get(i).getPackageName());
            cell = row.createCell(1);
            cell.setCellStyle(borderStyle);
            cell.setCellValue(Long.parseLong(details.get(i).getNumbert()));
            cell = row.createCell(2);
            cell.setCellStyle(borderStyle);
            if(details.get(i).getAverage() == null){
            	cell.setCellValue(0);
            }else{
            	cell.setCellValue(Long.parseLong(details.get(i).getAverage()));
            }
            cell = row.createCell(3);
            cell.setCellStyle(borderStyle);
            if(details.get(i).getSumt() == null){
            	cell.setCellValue(0);
            }else{
            	cell.setCellValue(Long.parseLong(details.get(i).getSumt()));
            }
            
        }
        
        row = (Row) sheet2.createRow(1+details.size()+1);
        cell = row.createCell(0);
        cell.setCellStyle(borderStyle);
        cell = row.createCell(1);
        cell.setCellStyle(borderStyle);
        cell.setCellValue(getSumOrder(details));
        cell = row.createCell(2);
        cell.setCellStyle(borderStyle);

        cell = row.createCell(3);
        cell.setCellStyle(borderStyle);
        cell.setCellValue(getSumUsage(details));

        
        row = (Row) sheet2.createRow(details.size()+4);
        cell = row.createCell(0);
        cell.setCellValue("注释:统计应用该套餐的所有订单中，用户实际使用天数");
        //设置合并单元格的边框
        brodercell = new CellRangeAddress(1+details.size()+3,1+details.size()+3, 0, 1);
        setBorderForMergeCell(XSSFCellStyle.BORDER_THIN, brodercell, sheet2, wb);
        //合并单元格
        region = new CellRangeAddress(1+details.size()+3,1+details.size()+3,0,1);
        sheet2.addMergedRegion(region);
        
        
        //套餐的实际使用天数的订单数量
        int rowsNumber = details.size()+6;
        int cursor = 0;
        String s = "没有统计实际使用天数的套餐:";
		for (int i = 0; i < details.size(); i++) {
			PackageUseDetails detail = details.get(i);
			//获取套餐天数
			int packageDays = getPackageDays(detail.getPackageName());
			if(packageDays == -1){
				s = s + detail.getPackageName() + ";";
				continue;
			}
			
			String packageName = detail.getPackageName();
			List<DaysOrdersNumber> numbers = resolvePackageOrderDaysDao.getDaysOrdersNumber(packageName,channel);
			//统计未使用天数
			List<ResolvePackageOrderDays> orderDays = resolvePackageOrderDaysDao.getUnusedOrdersNumber(packageName,channel);
			int unUsedNumber = orderDays.size();

			//设置套餐名行
			row = (Row) sheet2.createRow(rowsNumber + cursor + 5*i);
	        cell = row.createCell(0);
	        cell.setCellValue(packageName);
	        cell.setCellStyle(alignCenterStyle);
	        //设置合并单元格的边框
	        brodercell = new CellRangeAddress(rowsNumber + cursor + 5*i,rowsNumber + cursor + 5*i, 0, 1);
	        setBorderForMergeCell(XSSFCellStyle.BORDER_THIN, brodercell, sheet2, wb);
	        //合并单元格
	        region = new CellRangeAddress(rowsNumber + cursor + 5*i,rowsNumber + cursor + 5*i,0,1);
	        sheet2.addMergedRegion(region);
			
	        //设置
			row = (Row) sheet2.createRow(rowsNumber + cursor + 5*i + 1);
	        cell = row.createCell(0);
	        cell.setCellStyle(borderStyle);
	        cell = row.createCell(1);
	        cell.setCellValue("订单数量");
	        cell.setCellStyle(borderStyle);
	        
			row = (Row) sheet2.createRow(rowsNumber + cursor + 5*i + 2);
	        cell = row.createCell(0);
	        cell.setCellValue("总量");
	        cell.setCellStyle(borderStyle);
	        cell = row.createCell(1);
	        cell.setCellValue(Long.parseLong(detail.getNumbert()));
	        cell.setCellStyle(borderStyle);
	        
			row = (Row) sheet2.createRow(rowsNumber + cursor + 5*i + 3);
	        cell = row.createCell(0);
	        cell.setCellValue("未使用");
	        cell.setCellStyle(borderStyle);
	        cell = row.createCell(1);
	        cell.setCellValue(unUsedNumber);
	        cell.setCellStyle(borderStyle);
	        
	        int sumOrders = 0;
	        for(int x =1;x<=packageDays;x++){
				row = (Row) sheet2.createRow(rowsNumber + cursor + 5*i + 3 + x);
		        cell = row.createCell(0);
		        cell.setCellValue(x + "天");
		        cell.setCellStyle(borderStyle);
		        cell = row.createCell(1);
		        int number = getOrderNumber(numbers,x);
		        cell.setCellValue(number);
		        sumOrders = sumOrders + number;
		        cell.setCellStyle(borderStyle);
	        }
	        
	        if(Long.parseLong(detail.getNumbert()) != (unUsedNumber + sumOrders)){
	        	throw new Exception("渠道[" + channel + "]总订单数不等于各使用天数的订单数之和:" + packageName );
	        }
			cursor = cursor + packageDays;
		}
        

        
        
        System.out.println(s);
        //创建文件流  
        OutputStream stream = new FileOutputStream(path+fileName+"."+fileType);  
        //写入数据  
        wb.write(stream);  
        //关闭文件流  
        stream.close();  
    }
	
	  public static void writeUsage(String path,String fileName,String fileType, IResolveExport3Dao resolveExport3Dao, String channel) throws IOException  
	    {  
	        Workbook wb = null;  
	        if (fileType.equals("xls")) {  
	            wb = new HSSFWorkbook();  
	        }  
	        else if(fileType.equals("xlsx"))  
	        {  
	            wb = new XSSFWorkbook();  
	        }  
	        else  
	        {  
	            System.out.println("您的文档格式不正确！");  
	        } 
	        Sheet sheet = wb.createSheet();
	        XSSFCellStyle borderStyle = (XSSFCellStyle) wb.createCellStyle();
	        borderStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框  
	        borderStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框  
	        borderStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框  
	        borderStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
	        
			long allSum = 0;
			long sum = 0;
			int samLines = 1;  //相同副号的行数
			boolean mergeLastRegion = false;  //是否合并之前的单元格
			long lastSum = 0;
			Row row = (Row)sheet.createRow(0);
			createHeadRow(row,borderStyle);

			List<ResolveExport3> allUsage = resolveExport3Dao.getResolveExport(channel);
			//修改格式写入文件
			ResolveExport3 export = null;
			for (int x = 0; x < allUsage.size(); x++) {
				row = (Row) sheet.createRow(x+1);
				export = allUsage.get(x);
				
				Cell cell = row.createCell(0);
		        cell.setCellStyle(borderStyle);
		        cell.setCellValue(export.getPartnerName());
		        cell = row.createCell(1);
		        cell.setCellStyle(borderStyle);
		        cell.setCellValue(export.getIccid());
		        cell = row.createCell(2);
		        cell.setCellStyle(borderStyle);
		        cell.setCellValue(export.getOrderCode());
		        cell = row.createCell(3);
		        cell.setCellStyle(borderStyle);
		        cell.setCellValue(export.getPackageName());
		        cell = row.createCell(4);
		        cell.setCellStyle(borderStyle);
		        cell.setCellValue(export.getActualStartTime());
		        cell = row.createCell(5);
		        cell.setCellStyle(borderStyle);
		        cell.setCellValue(export.getActualEndTime());
		        cell = row.createCell(6);
		        cell.setCellStyle(borderStyle);
		        cell.setCellValue(export.getLocalImsi());
		        cell = row.createCell(7);
		        cell.setCellStyle(borderStyle);
		        cell.setCellValue(export.getSupplierCode());
		        cell = row.createCell(8);
		        cell.setCellStyle(borderStyle);
		        cell.setCellValue(export.getSupplierName());
		        cell = row.createCell(9);
		        cell.setCellStyle(borderStyle);
		        cell.setCellValue(export.getCdrTime());
		        cell = row.createCell(10);
		        cell.setCellStyle(borderStyle);
		        cell.setCellValue(Long.parseLong(export.getCdrByte()));
		        cell = row.createCell(11);
		        cell.setCellStyle(borderStyle);
				
				


				if(x > 0){
					ResolveExport3 lastExport = allUsage.get(x-1);
					if(lastExport.getIccid().equals(export.getIccid()) && lastExport.getOrderCode().equals(export.getOrderCode()) && lastExport.getLocalImsi().equals(export.getLocalImsi())){
						sum = sum + Long.parseLong(export.getCdrByte());
						samLines++;
						if(x == allUsage.size() -1){ //最后一列特殊处理
							mergeLastRegion = true;
							lastSum = sum;
						}
					}else{
						mergeLastRegion = true;
						lastSum = sum;
						sum = Long.parseLong(export.getCdrByte());
					}
				}else{
					samLines = 1;
					sum = Long.parseLong(export.getCdrByte());
				}
				
				if(mergeLastRegion){
					if(x == allUsage.size() -1){ //最后一列特殊处理,合并时x为相同行的最后一行
						mergeRegion(sheet,x+1,samLines);
						cell = sheet.getRow(x-samLines+2).createCell(11);
					}else{
						mergeRegion(sheet,x,samLines); //合并时，x行为最新一行
						cell = sheet.getRow(x-samLines+1).createCell(11);
					}
			        cell.setCellStyle(borderStyle);
			        cell.setCellValue(lastSum);
					allSum = allSum + lastSum;
					
					//reset
					samLines = 1;
					mergeLastRegion = false;  //是否合并之前的单元格
					lastSum = 0;
				}
				
			}
			Row row2 = (Row) sheet.createRow(allUsage.size() + 1);
			Cell cell2 = row2.createCell(11);
			cell2.setCellValue(allSum);
			
			sheet.setColumnWidth(0, 256*9);
			sheet.setColumnWidth(1, 256*22);
			sheet.setColumnWidth(2, 256*17);
			sheet.setColumnWidth(3, 256*25);
			sheet.setColumnWidth(4, 256*19);
			sheet.setColumnWidth(5, 256*19);
			sheet.setColumnWidth(6, 256*18);
			sheet.setColumnWidth(7, 256*9);
			sheet.setColumnWidth(8, 256*14);
			sheet.setColumnWidth(9, 256*13);
			sheet.setColumnWidth(10, 256*20);
			sheet.setColumnWidth(11, 256*21);
			
			
	        //创建文件流  
	        OutputStream stream2 = new FileOutputStream(path+fileName+"."+fileType);  
	        //写入数据  
	        wb.write(stream2);  
	        //关闭文件流  
	        stream2.close(); 
	    }
	  private static void createHeadRow(Row row, XSSFCellStyle borderStyle) {
			Cell cell = row.createCell(0);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("partner_name");
	        cell = row.createCell(1);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("iccid");
	        cell = row.createCell(2);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("order_code");
	        cell = row.createCell(3);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("package_name");
	        cell = row.createCell(4);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("actual_start_time");
	        cell = row.createCell(5);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("actual_end_time");
	        cell = row.createCell(6);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("local_imsi");
	        cell = row.createCell(7);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("supplier_code");
	        cell = row.createCell(8);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("supplier_name");
	        cell = row.createCell(9);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("operator_code");
	        cell = row.createCell(10);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("operator_name");
	        cell = row.createCell(11);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("cdr_time");
	        cell = row.createCell(12);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("cdr_byte(单位字节)");
	        cell = row.createCell(13);
	        cell.setCellStyle(borderStyle);
	        cell.setCellValue("sum(单位字节)");
	    }
		private static int getPackageDays(String packageName) throws Exception {
			if(packageName.contains("4天")){
				return 4;
			}else if(packageName.contains("15天")){
				return 15;
			}else if(packageName.contains("5天")){
				return 5;
			}else if(packageName.contains("6天")){
				return 6;
			}else if(packageName.contains("7天")){
				return 7;
			}else if(packageName.contains("8天")){
				return 8;
			}else if(packageName.contains("10天")){
				return 10;
			}else{
				return -1;
//				throw new Exception("根据套餐名无法确定天数");
			}
		}
		private static void mergeRegion(Sheet sheet2, int x, int samLines) {
			if(x != 0){
		        CellRangeAddress region = new CellRangeAddress(x-samLines+1,x,13,13);
		        sheet2.addMergedRegion(region);
		    }
		}

		private static int getOrderNumber(List<DaysOrdersNumber> numbers, int x) {
			for(DaysOrdersNumber number:numbers){
				if(number.getDays().equals(x+"")){
					return Integer.parseInt(number.getNumbert());
				}
			}
			return 0;
		}


		private static long getSumUsage(List<PackageUseDetails> details) {
			long i = 0;
			for(PackageUseDetails detail:details){
				if(detail.getSumt() == null){
					detail.setSumt("0");
				}
				i = i + Long.parseLong(detail.getSumt());
			}
			return i;
		}

		private static int getSumOrder(List<PackageUseDetails> details) {
			int i = 0;
			for(PackageUseDetails detail:details){
				i = i + Integer.parseInt(detail.getNumbert());
			}
			return i;
		}
	/**
     * 合并单元格设置边框
     * @param i
     * @param cellRangeTitle
     * @param sheet
     * @param wb
     */
    private static void setBorderForMergeCell(short i, CellRangeAddress cellRangeTitle, Sheet sheet, Workbook wb){
        RegionUtil.setBorderBottom(i, cellRangeTitle, sheet, wb);
        RegionUtil.setBorderLeft(i, cellRangeTitle, sheet, wb);
        RegionUtil.setBorderRight(i, cellRangeTitle, sheet, wb);
        RegionUtil.setBorderTop(i, cellRangeTitle, sheet, wb);
    }

	private static String getSupplerCodes(List<Supplier> suppList) {
		StringBuilder strB = new StringBuilder();
		for(Supplier sup:suppList){
			strB.append(sup.getSupplierCode() + ",");
		}
		return strB.toString();
	}
	
	private static String getOperatorCodes(List<Operator> operList) {
		StringBuilder strB = new StringBuilder();
		for(Operator sup:operList){
			strB.append(sup.getOperatorCode() + ",");
		}
		return strB.toString();
	}
	
	private static String getSupplerNames(List<Supplier> suppList) {
		StringBuilder strB = new StringBuilder();
		for(Supplier sup:suppList){
			strB.append(sup.getSupplierName());
		}
		return strB.toString();
	}
	
	private static String getOperatorNames(List<Operator> operList) {
		StringBuilder strB = new StringBuilder();
		for(Operator oper:operList){
			strB.append(oper.getOperatorName());
		}
		return strB.toString();
	}


}
