package com.wyl.excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 导出Excel的练习
 * 
 * @author Wei
 * @time 2017年5月9日 下午2:39:03
 */
public class Excel {

	public Excel() {
		// TODO Auto-generated constructor stub
	}
	@SuppressWarnings("all")
	private static List<Student> getStudent() throws Exception {
		List list = new ArrayList();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");

		Student user1 = new Student(1, "张三", 16, df.parse("1997-03-12"));
		Student user2 = new Student(2, "李四", 17, df.parse("1996-08-12"));
		Student user3 = new Student(3, "王五", 26, df.parse("1985-11-12"));
		list.add(user1);
		list.add(user2);
		list.add(user3);

		return list;
	}

	//@SuppressWarnings("all")
	/*public static void main(String[] args) throws Exception {
		// 第一步，创建一个workbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("学生表一");
		HSSFSheet sheet2 = wb.createSheet("学生表er2");
		HSSFSheet sheet3 = wb.createSheet("学生表er3");
		HSSFSheet sheet4 = wb.createSheet("学生表er4");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row.createCell((int) 0);
		cell.setCellValue("学号");
		cell.setCellStyle(style);
		cell = row.createCell((int) 1);
		cell.setCellValue("姓名");
		cell.setCellStyle(style);
		cell = row.createCell((int) 2);
		cell.setCellValue("年龄");
		cell.setCellStyle(style);
		cell = row.createCell((int) 3);
		cell.setCellValue("生日");
		cell.setCellStyle(style);

		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List list = Excel.getStudent();

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow((int) i + 1);
			Student stu = (Student) list.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell((int) 0).setCellValue((double) stu.getId());
			row.createCell((int) 1).setCellValue(stu.getName());
			row.createCell((int) 2).setCellValue((double) stu.getAge());
			cell = row.createCell((int) 3);
			cell.setCellValue(new SimpleDateFormat("yyyy-mm-dd").format(stu.getBirth()));
		}
		// 第六步，将文件存到指定位置
		try {
			FileOutputStream fout = new FileOutputStream("E:/wyl3.xls");
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	public static void main(String[] args) throws IOException {
		// 第一步，创建一个webbook，对应一个Excel文件  
		HSSFWorkbook book = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
		HSSFSheet sheet = book.createSheet();//创建sheet,默认为sheet0,sheet2...
		 // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
		HSSFRow row = sheet.createRow((int)0);
		// 第四步，创建单元格，并设置值表头 设置表头居中  
		HSSFCellStyle style = book.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//设置居中
		
		HSSFCell cell = row.createCell((int)0);//第一个单元格
		cell.setCellValue("公司名称");
		cell.setCellStyle(style);
		
		cell = row.createCell((int)1);//第一个单元格
		cell.setCellValue("法人");
		cell.setCellStyle(style);
		
		List<Company> list = getCompanies();
		for(int i=0;i<list.size();i++){
			row = sheet.createRow(i+1);
			cell = row.createCell((int)0);
			cell.setCellValue(list.get(i).getCompanyName());
			cell.setCellStyle(style);//必须写，否则不会居中
			cell = row.createCell((int)1);
			cell.setCellValue(list.get(i).getLegalPerson());
			cell.setCellStyle(style);
		}
		FileOutputStream fos = new FileOutputStream("E:/yangdandan2.xls");
		book.write(fos);
	}
	
	@SuppressWarnings("all")
	private static List<Company> getCompanies() {
		List list = new ArrayList();
		list.add(new Company("A公司", "Smith"));
		list.add(new Company("B公司", "lucy"));
		list.add(new Company("C公司", "Tiffany"));
		list.add(new Company("D公司", "Oxford"));
		return list;
	}
}
