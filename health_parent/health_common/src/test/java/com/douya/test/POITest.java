package com.douya.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class POITest {
    //使用poi 读取excel文件的数据
    @Test
    public void test1() throws Exception {
        //加载指定文件，创建一个Excel对象
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("E:\\poi.xlsx")));

        //读取Excel文件的第一个sheet标签页
        XSSFSheet sheet = excel.getSheetAt(0);

        //遍历sheet标签页，获得每一行数据
        for (Row row : sheet) {
            //遍历行,获取每个单元格对象
            for (Cell cell : row) {
                System.out.println(cell.getStringCellValue());
            }
        }
        excel.close();
    }


    @Test
    public void test2() throws Exception {
        //创建工作簿
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("E:\\poi.xlsx")));
        //获取工作表，既可以根据工作表的顺序获取，也可以根据工作表的名称获取
        XSSFSheet sheet = excel.getSheetAt(0);
        //获取当前工作表最后一行的行号，行号从0开始
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("lastRowNum="+lastRowNum);
        for (int i = 0; i <= lastRowNum; i++) {
            //根据行号获取行对象
            XSSFRow row = sheet.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for (short j = 0; j < lastCellNum; j++) {
                String value = row.getCell(j).getStringCellValue();
                System.out.println(value);
            }
        }
        excel.close();
    }


    //使用poi向Excel文件写入数据 并且通过输出流将创建的excel文件保存到本地磁盘
    @Test
    public void test3() throws Exception {
        //在内存中创建一个Excel文件
        XSSFWorkbook excel = new XSSFWorkbook();
        //创建工作表，指定工作表名称
        XSSFSheet sheet = excel.createSheet("传智播客");
        //在工作表中创建行对象
        XSSFRow title =sheet.createRow(0);
        //在行中创建单元格对象
        title.createCell(0).setCellValue("姓名");
        title.createCell(1).setCellValue("地址");
        title.createCell(2).setCellValue("年龄");

        XSSFRow dataRow = sheet.createRow(1);
        title.createCell(0).setCellValue("小毛");
        title.createCell(1).setCellValue("北京");
        title.createCell(2).setCellValue("13");


        //创建一个输出流 通过输出流将内存中的excel文件写到磁盘中
        FileOutputStream out = new FileOutputStream(new File("e:\\hello2.xlsx"));
        excel.write(out);
        out.flush();
        excel.close();

    }
}