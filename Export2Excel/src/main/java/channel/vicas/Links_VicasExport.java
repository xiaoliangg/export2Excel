package channel.vicas;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bean.MyCell;
import com.bean.VicasActiveNumber;
import com.bean.VicasSum;
import com.dao.IVicasDao;
import com.util.TimeUtil;  
/**
 * vicas导出cdr报表
 * @author ll
 *
 */
public class Links_VicasExport {  
    public static void main(String[] args) throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "spring-applicationContext.xml");
        IVicasDao vicasDao = (IVicasDao)context.getBean("IVicasDao");
        
        
        
        String month = "201904";
        
        
        Date d = TimeUtil.format("yyyyMM", month);
		String nextMonth = TimeUtil.getFormateNextMonth("yyyyMM",d);
		System.out.println(nextMonth);
        String startTime = month + "01000000";
        String endTime = nextMonth + "01000000";
        
        
        String srcPath = "C:/Users/ll/Desktop/workbench_sql/002_常规统计_马哥vicas统计_充值/" + "Vicas物联网卡话单统计_模板";
        String destPah = "C:/Users/ll/Desktop/" + "Vicas物联网卡话单统计（" + month + "）";
        String fileType = "xlsx";
        
        copyAndModify(srcPath, destPah, fileType,vicasDao,startTime,endTime,month);
        
        
	}
    public static void copyAndModify(String srcPath,String destPah,String fileType, IVicasDao vicasDao,String startTime,String endTime,String month) throws IOException  
    {  
        InputStream stream = new FileInputStream(srcPath+"."+fileType);  
        Workbook wb = null;  
        if (fileType.equals("xls")) {  
            wb = new HSSFWorkbook(stream);  
        }  
        else if (fileType.equals("xlsx")) {  
            wb = new XSSFWorkbook(stream);  
        }  
        else {  
            System.out.println("您输入的excel格式不正确");  
        }
        
        Workbook wb2 = null;  
        if (fileType.equals("xls")) {  
            wb2 = new HSSFWorkbook();  
        }  
        else if(fileType.equals("xlsx"))  
        {  
            wb2 = new XSSFWorkbook();  
        }  
        else  
        {  
            System.out.println("您的文档格式不正确！");  
        } 
        
        
        Sheet sheet1 = wb.getSheetAt(0);  
        Sheet sheet2 = wb2.createSheet();
        int i = -1;
        int j = 0;
        
        List<MyCell> rowList = new ArrayList<MyCell>();
        List<List> allList = new ArrayList<List>();
        MyCell myCell = null;
		for (Row row : sheet1) {
			i++;
			for (Cell cell : row) {
				myCell = new MyCell();
				// 读取到命令行
				if ("m/d/yy h:mm".equals(cell.getCellStyle().getDataFormatString())) {
					myCell.setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getDateCellValue()));
					myCell.setType(11);
				} else if ("yyyy/mm;@".equals(cell.getCellStyle().getDataFormatString())
						|| "m/d/yy".equals(cell.getCellStyle().getDataFormatString())
						|| "yy/m/d".equals(cell.getCellStyle().getDataFormatString())
						|| "mm/dd/yy".equals(cell.getCellStyle().getDataFormatString())
						|| "dd-mmm-yy".equals(cell.getCellStyle().getDataFormatString())
						|| "yyyy/m/d".equals(cell.getCellStyle().getDataFormatString())) {
					myCell.setValue(new SimpleDateFormat("yyyy/MM/dd").format(cell.getDateCellValue()));
					myCell.setType(22);
				} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					myCell.setValue(cell.getStringCellValue());
					myCell.setType(Cell.CELL_TYPE_STRING);
					System.out.print(cell.getStringCellValue() + "  ");
				} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					DecimalFormat df = new DecimalFormat("#");
					myCell.setValue(df.format(cell.getNumericCellValue()));
					myCell.setType(Cell.CELL_TYPE_NUMERIC);
					System.out.print(cell.getNumericCellValue() + "  ");
				}
				myCell.setCellStyle(cell.getCellStyle());
				rowList.add(myCell);

				j++;
			}
			allList.add(rowList);
			rowList = new ArrayList<MyCell>();
			j = 0;
			System.out.println();
		} 
        
		long allSum = 0;

		//修改格式写入文件
		sheet2.setColumnWidth(0, sheet1.getColumnWidth(0));
		sheet2.setColumnWidth(1, sheet1.getColumnWidth(1));
		sheet2.setColumnWidth(2, sheet1.getColumnWidth(2));
		sheet2.setColumnWidth(3, sheet1.getColumnWidth(3));
		sheet2.setColumnWidth(4, sheet1.getColumnWidth(4));
        XSSFCellStyle borderStyle = (XSSFCellStyle) wb2.createCellStyle();
        borderStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框  
        borderStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框  
        borderStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框  
        borderStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
        
        String imsis = getImsis(allList);
        List<VicasSum> vicasSums = new ArrayList<VicasSum>();
        List<VicasActiveNumber> vicasActiveNum = vicasDao.getActiveNumbers(startTime,endTime,imsis);
        
        
		for (int x = 0; x < allList.size(); x++) {
			Row row2 = (Row) sheet2.createRow(x);
			List<MyCell> rowList2 = allList.get(x);
			for (int y = 0; y < rowList2.size(); y++) {
				MyCell myCell2 = rowList2.get(y);
				Cell cell2 = row2.createCell(y);
				cell2.setCellStyle(borderStyle);
				
				String cdrByte = "";
				if(x != 0 && y == 3){
					//当月总用量
					cdrByte = getCdrByte(vicasSums,rowList2.get(0).getValue());
					cell2.setCellValue(cdrByte);
					continue;
				}else if(x != 0 && y == 4){
					cdrByte = getCdrByte(vicasSums,rowList2.get(0).getValue());
					if(cdrByte.equals("0")){
						continue;
					}
					
					//首次激活时间
					String firstUseTime = getFirstUseTime(vicasSums,rowList2.get(0).getValue());
					Date d1 = TimeUtil.format("yyyyMMddHHmmss", firstUseTime);
					String d2 = TimeUtil.format(d1, "yyyy-MM-dd HH:mm:ss");
					cell2.setCellValue(d2);
					continue;
				}
				
				
				if (myCell2.getType() == 11) {
					cell2.setCellValue(myCell2.getValue());
				} else if (myCell.getType() == 22) {
					cell2.setCellValue(myCell2.getValue());
				} else if (myCell.getType() == Cell.CELL_TYPE_STRING) {
					cell2.setCellValue(myCell2.getValue());
				} else if (myCell.getType() == Cell.CELL_TYPE_NUMERIC) {
					try {
						cell2.setCellValue(Integer.parseInt(myCell2.getValue()));
					} catch (Exception e) {
						cell2.setCellValue(myCell2.getValue());

					}
				}
			}
		}
		
        //创建文件流  
        OutputStream stream2 = new FileOutputStream(destPah+"."+fileType);  
        //写入数据  
        wb2.write(stream2);  
        //关闭文件流  
        stream2.close(); 
        System.out.println("1、" + month + " Vikas流量统计报告，请查收");
        System.out.println("2、vikas "+ month + "新激活码号数量:" + vicasActiveNum.size());
        System.out.println("3、已为vicas 500码号余量设置为100m。");
    }
	private static String getImsis(List<List> allList) {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < allList.size(); x++) {
			List<MyCell> rowList2 = allList.get(x);
			MyCell myCell2 = rowList2.get(0);
			sb.append(myCell2.getValue() + ",");
		}
		return sb.toString();
	}
	private static String getCdrByte(List<VicasSum> vicasSums, String imsi) {
		for(VicasSum vicasSum :vicasSums){
			if(imsi.equals(vicasSum.getImsi())){
				return vicasSum.getCdrFlow();
			}
		}
		return "0";
	}
	private static String getFirstUseTime(List<VicasSum> vicasSums, String imsi) {
		for(VicasSum vicasSum :vicasSums){
			if(imsi.equals(vicasSum.getImsi())){
				return vicasSum.getFirstUseTime();
			}
		}
		return "";
	}
}  
