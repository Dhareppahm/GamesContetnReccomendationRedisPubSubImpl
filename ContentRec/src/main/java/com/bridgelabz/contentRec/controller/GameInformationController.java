/*
* @Author:Dhareppa Metri
* File:GameInfoRestApiCallController.java
* Purpose:Controller class for to fetch game information through REST API call.
**/
package com.bridgelabz.contentRec.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GameInformationController {
	String mGbDeviceId;
	String mGbAppVersionCode;
	String mUrlString;
	Logger mLogger = Logger.getLogger("GAMEINFORMATIONCONTROLLER");

	@Autowired
	PublisherImplementaion mPublisherImplementaion;
	/**
	 * This method is used CALL Redis publisher method
	 * @return ModelAndView,VisitorInfoAndGameInfoSuccess view
	 */
	@RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
	public ModelAndView readVisitorInformationAndUpdateGameInfo() throws InterruptedException {

		mPublisherImplementaion.sendMessage();
		// this.fetchAndSaveGameData();
		return new ModelAndView("VisitorInfoAndGameInfoSuccess");
		
	}//End of readVisitorInformationAndUpdateGameInfo method

}// End of GameInformationController class
