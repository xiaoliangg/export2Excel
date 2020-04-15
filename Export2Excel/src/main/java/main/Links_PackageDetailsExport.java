package main;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

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
import com.bean.PackageUseDetails;
import com.bean.ResolvePackageOrderDays;
import com.constant.SysConstant;
import com.dao.IResolvePackageOrderDaysDao;
/**
 * 导出套餐的使用情况:各套餐使用的订单总数量、各套餐实际使用天数的订单数量
 * @author ll
 *
 */

public class Links_PackageDetailsExport {

	public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "spring-applicationContext.xml");

        IResolvePackageOrderDaysDao resolvePackageOrderDaysDao = (IResolvePackageOrderDaysDao)context.getBean("IResolvePackageOrderDaysDao");
        List<PackageUseDetails> details = resolvePackageOrderDaysDao.getPackageUseDetails(SysConstant.CHANNEL_DEFAULT);
        
        String path = "C:/Users/ll/Desktop/";  
//        String fileName = "test" + new Date().getTime();  
        String fileName = "resolve港台19年1月份套餐统计_docomo(新旧)";  
//        String fileName = "resolve苏州19年1月份套餐统计_docomo(新旧)";  
//        String fileName = "resolve惠州19年1月份套餐统计_docomo(新旧)";  
        
//      String fileName = "resolve港台19年1月份套餐统计_联通国际";  
//      String fileName = "resolve苏州19年1月份套餐统计_联通国际";  
//      String fileName = "resolve惠州19年1月份套餐统计_联通国际";  

//        String fileName = "淳漠19年1月份套餐统计_indosat";  
        String fileType = "xlsx";  
        writer(path, fileName, fileType,details,resolvePackageOrderDaysDao);
	}
	
    private static void writer(String path, String fileName,String fileType,List<PackageUseDetails> details, IResolvePackageOrderDaysDao resolvePackageOrderDaysDao) throws Exception { 
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
        Sheet sheet1 = (Sheet) wb.createSheet(fileName); 
		sheet1.setColumnWidth(0, 256*33);
		sheet1.setColumnWidth(1, 256*13);
		sheet1.setColumnWidth(2, 256*17);
		sheet1.setColumnWidth(3, 256*27);
		
        CellRangeAddress region = new CellRangeAddress(0,0,0,3);
        sheet1.addMergedRegion(region);

        XSSFCellStyle borderStyle = (XSSFCellStyle) wb.createCellStyle();
        borderStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框  
        borderStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框  
        borderStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框  
        borderStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
        
        XSSFCellStyle alignCenterStyle = (XSSFCellStyle) wb.createCellStyle();
        alignCenterStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 居中
        
        Row row = null;
        Cell cell = null;
		row = (Row) sheet1.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("套餐的订单数量及用量统计");
        cell.setCellStyle(alignCenterStyle);
        CellRangeAddress brodercell = new CellRangeAddress(0,0,0,3);
        setBorderForMergeCell(XSSFCellStyle.BORDER_THIN, brodercell, sheet1, wb);
        
        row = (Row) sheet1.createRow(1);
        cell = row.createCell(0);
        cell.setCellStyle(borderStyle);
        cell = row.createCell(1);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("订单数量");
        cell = row.createCell(2);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("订单平均使用流量");
        cell = row.createCell(3);
        cell.setCellStyle(borderStyle);
        cell.setCellValue("总流量");
        
        for(int i = 0;i<details.size();i++){
            row = (Row) sheet1.createRow(1+i+1);
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
        
        row = (Row) sheet1.createRow(1+details.size()+1);
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

        
        row = (Row) sheet1.createRow(details.size()+4);
        cell = row.createCell(0);
        cell.setCellValue("注释:统计应用该套餐的所有订单中，用户实际使用天数");
        //设置合并单元格的边框
        brodercell = new CellRangeAddress(1+details.size()+3,1+details.size()+3, 0, 1);
        setBorderForMergeCell(XSSFCellStyle.BORDER_THIN, brodercell, sheet1, wb);
        //合并单元格
        region = new CellRangeAddress(1+details.size()+3,1+details.size()+3,0,1);
        sheet1.addMergedRegion(region);
        
        
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
			List<DaysOrdersNumber> numbers = resolvePackageOrderDaysDao.getDaysOrdersNumber(packageName,SysConstant.CHANNEL_DEFAULT);
			List<ResolvePackageOrderDays> orderDays = resolvePackageOrderDaysDao.getUnusedOrdersNumber(packageName,SysConstant.CHANNEL_DEFAULT);
			int unUsedNumber = orderDays.size();

			//设置套餐名行
			row = (Row) sheet1.createRow(rowsNumber + cursor + 5*i);
	        cell = row.createCell(0);
	        cell.setCellValue(packageName);
	        cell.setCellStyle(alignCenterStyle);
	        //设置合并单元格的边框
	        brodercell = new CellRangeAddress(rowsNumber + cursor + 5*i,rowsNumber + cursor + 5*i, 0, 1);
	        setBorderForMergeCell(XSSFCellStyle.BORDER_THIN, brodercell, sheet1, wb);
	        //合并单元格
	        region = new CellRangeAddress(rowsNumber + cursor + 5*i,rowsNumber + cursor + 5*i,0,1);
	        sheet1.addMergedRegion(region);
			
	        //设置
			row = (Row) sheet1.createRow(rowsNumber + cursor + 5*i + 1);
	        cell = row.createCell(0);
	        cell.setCellStyle(borderStyle);
	        cell = row.createCell(1);
	        cell.setCellValue("订单数量");
	        cell.setCellStyle(borderStyle);
	        
			row = (Row) sheet1.createRow(rowsNumber + cursor + 5*i + 2);
	        cell = row.createCell(0);
	        cell.setCellValue("总量");
	        cell.setCellStyle(borderStyle);
	        cell = row.createCell(1);
	        cell.setCellValue(Long.parseLong(detail.getNumbert()));
	        cell.setCellStyle(borderStyle);
	        
			row = (Row) sheet1.createRow(rowsNumber + cursor + 5*i + 3);
	        cell = row.createCell(0);
	        cell.setCellValue("未使用");
	        cell.setCellStyle(borderStyle);
	        cell = row.createCell(1);
	        cell.setCellValue(unUsedNumber);
	        cell.setCellStyle(borderStyle);
	        
	        for(int x =1;x<=packageDays;x++){
				row = (Row) sheet1.createRow(rowsNumber + cursor + 5*i + 3 + x);
		        cell = row.createCell(0);
		        cell.setCellValue(x + "天");
		        cell.setCellStyle(borderStyle);
		        cell = row.createCell(1);
		        cell.setCellValue(getOrderNumber(numbers,x));
		        cell.setCellStyle(borderStyle);
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

	private static int getOrderNumber(List<DaysOrdersNumber> numbers, int x) {
		for(DaysOrdersNumber number:numbers){
			if(number.getDays().equals(x+"")){
				return Integer.parseInt(number.getNumbert());
			}
		}
		return 0;
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
//			throw new Exception("根据套餐名无法确定天数");
		}
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
}
