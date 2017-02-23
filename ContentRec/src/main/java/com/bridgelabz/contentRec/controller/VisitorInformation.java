/*
* @Author:Dhareppa Metri
* File:VisitorsInfoController.java
* Purpose:Controller class for to store game visitors information.
**/
package com.bridgelabz.contentRec.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.contentRec.model.VisitorsInfo;
import com.bridgelabz.contentRec.services.VisitorsInfoService;

public class VisitorInformation{
	@Autowired
	VisitorsInfoService mVisitorsInfoService;
	Logger mLogger = Logger.getLogger("VisitorInformation");
	/**
	 * This method is used to read visitors information from the CSV file
	 */
	public void getDataFromCSV() {
		FileReader lFileReader;
		String[] lData;
		String lTemp = "visitor_id";
		int i;

		try {
			lFileReader = new FileReader("/home/bridgeit/contentDb.csv");
			BufferedReader lBufferedReader = new BufferedReader(lFileReader);
			String lNextRecord;
			lNextRecord = lBufferedReader.readLine();
			lData = lNextRecord.split("\\,");
			for (i = 0; i < lData.length; i++) {
				System.out.print(i + " " + lData[i] + " ");
			} // End of for
			while (lNextRecord != null) {
				lData = lNextRecord.split("\\,");
				System.out.println(lNextRecord);
				for (i = 0; i < lData.length; i++) {
					lData[i] = lData[i].replace("\"", "");
				} // End of for
				if (!(lData[0].equals(lTemp))) {
					//VisitorInfoNew vi = new VisitorInfoNew();
					VisitorsInfo vi=new VisitorsInfo();
					vi.setmVisitorId(lData[0]);
					vi.setmContentId(lData[1]);
					vi.setmContentName(lData[2]);
					vi.setmCategoryName(lData[3]);
					vi.setmView(lData[4]);
					vi.setmDownload(lData[5]);
				//	mVisitorsInfoService.addVisitorsNew(vi);
					mVisitorsInfoService.addVisitors(vi);
				} // End of if
				lNextRecord = lBufferedReader.readLine();
			} // End of while
			lBufferedReader.close();
			lFileReader.close();
		} // End of try
		catch (FileNotFoundException e) {

			e.printStackTrace();
		} // End of catch
		catch (IOException e) {

			e.printStackTrace();
		}
		
	}// End of getDataFromCSV method
	
}//End of VisitorInfoPubSub class
