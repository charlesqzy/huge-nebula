package com.bizwell.datasource.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ReadCSVUtil {
    public static void readCSV(){
        try {
        	String fileName = "D:\\predict.csv";
        	DataInputStream in = new DataInputStream(new FileInputStream(new File(fileName)));
        	BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(in,"GBK"));
            bufferedReader.readLine();// try-catch omitted
            CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',');
            CSVParser parser = new CSVParser(bufferedReader, format);
            List<CSVRecord> records = parser.getRecords();//跳过第一行所有行的记录
            
            
            int rows = records.size();
            int cloumns = records.get(0).size();
            System.out.println(rows);
            System.out.println(cloumns);
            
            
            for(int i=0;i<records.size();i++){
                String[] temp=new String[records.get(i).size()];
                for(int j=0;j<records.get(i).size();j++){
                    temp[j]=String.valueOf(records.get(i).get(j));
                }
                for(int ii=0;ii<temp.length;ii++){
                    System.out.print(temp[ii]+"  ");
                }
                System.out.println();
            }
        }catch (Exception e){
        	e.printStackTrace();
            System.out.print("please check your upload");
        }

    }
    
    public static void main(String[] args) {
    	ReadCSVUtil.readCSV();
	}
}
