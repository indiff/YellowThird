package com.pear.common.utils.files;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;


public class LogFileTrim {
	
	public final static String LOG_FILE="output/voiceHelper.log";
	
	public final static int MAX_LINE_COUT=20;
 
	/**
	 * 倒叙读取文本内容
	 * @param filename 读取的文件
	 * @param maxLineCount 最多读取多少行
	 * @return 返回读取的文本集合
	 * */
    List<String> readRollback(String filename,int maxLineCount)
    {
    	List<String> content=new ArrayList<String>();
    	RandomAccessFile rf = null;  
        try {
        	rf = new RandomAccessFile(filename, "r");  
            long len = rf.length();  
            long start = rf.getFilePointer();  
            long nextend = start + len - 1;  
            String line;  
            rf.seek(nextend);  
            int c = -1;  
            while (nextend > start) {  
                c = rf.read();  
                if (c == '\n' || c == '\r') { 
                    line = rf.readLine();  
                    if(null!=line)
                    {
                    	content.add(line);
                    	if(content.size()>=maxLineCount)
                    		break;
                    }
                    nextend--;  
                }
                nextend--;  
                rf.seek(nextend);    
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (rf != null)  
                    rf.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }
        return content;
    }
    
    StringBuffer listRollback2StingBuffer(List<String> list)
    {
    	StringBuffer sb=new StringBuffer();
    	for(int i=list.size()-1;i>=0;i--)
    	{
    		sb.append(list.get(i)).append("\n<br>"); 
    	} 
    	return sb;
    }

    /**
     * 获取最后一些简单的日记
     * */
    public String getLastSimpleLog()
    {
    	List<String> list=readRollback(LOG_FILE,MAX_LINE_COUT) ;
        StringBuffer sb=listRollback2StingBuffer(list); 
        return sb.toString();
    }

	public static void main111(String[] args) {
		LogFileTrim log=new LogFileTrim();
		//System.out.println(log.init());
	}
	
}
