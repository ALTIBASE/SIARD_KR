package ch.admin.bar.siard2.cmd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {

	//원본 파일 사이즈

	public long sourceFileSize;
	public long executeTime;

	public void copy(String sourceFile, String targetFilePath) {
		File sourceFileObj = new File(sourceFile);
//		File targetFileObj = new File(targetFilePath);
		
		
		String targetPath = targetFilePath.substring(0, targetFilePath.lastIndexOf("/") + 1);
		File targetPathObj = new File(targetPath);

		if(!sourceFileObj.exists()) {
			return;
		}

		if(!targetPathObj.exists()) {
			targetPathObj.mkdirs();
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;

		FileChannel fcin = null;
		FileChannel fcout = null;

		try {
			// 시작시간
			long startTime = System.currentTimeMillis();

			fis = new FileInputStream(sourceFileObj);
			fos = new FileOutputStream(targetPath + File.separator + sourceFileObj.getName());

			fcin = fis.getChannel();
			fcout = fos.getChannel();

			long size = fcin.size();
			fcout.transferFrom(fcin, 0, size);

			// 종료시간
    	long endTime = System.currentTimeMillis();
    	// 수행시간 = 종료시간 - 시작시간
    	executeTime = endTime - startTime;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {if(fcout != null) fcout.close();} catch (IOException ioe) {}
			try {if(fcin != null) fcin.close();} catch (IOException ioe) {}
			try {if(fos != null) fos.close();} catch (IOException ioe) {}
			try {if(fis != null) fis.close();} catch (IOException ioe) {}
		}
	}

}