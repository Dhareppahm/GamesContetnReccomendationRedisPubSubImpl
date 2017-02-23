/*
* @Author:Dhareppa Metri
* File:GamesCategoryScoreController.java
* Purpose:Subsciber implementation class.
**/
package com.bridgelabz.contentRec.controller;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.contentRec.services.VisitorsInfoService;

public class SubcriberImplementation {
	/* private Logger LOGGER = LoggerFactory.getLogger(SubImpl.class); */
	Logger LOGGER = Logger.getLogger("SubcriberImplementation");
	@Autowired
	GameInformation mGameInfoPubSub;

	@Autowired
	VisitorsInfoService mVisitorsInfoService;
	private CountDownLatch latch;

	@Autowired
	public SubcriberImplementation(CountDownLatch latch) {
		this.latch = latch;
	}
	
	public void receiveMessage(String message) {
		System.out.println("Reciver Method....");
		LOGGER.info("Received <" + message + ">");
		/*
		 * List lContentIdList = mVisitorsInfoService.getDistinctContentId();
		 * for (Iterator iterator = lContentIdList.iterator();
		 * iterator.hasNext();) { String lcontentId = (String) iterator.next();
		 */
		// System.out.println(lcontentId);
		// Boolean status = mGameInfoPubSub.checkRestAPIResponse(lcontentId);
		// if (status) {
		mGameInfoPubSub.fetchAndSaveGameInfo();
		// } else {
		// continue;
		// }
		/* } */
		System.out.println("Game Info updated");
		latch.countDown();
	}//End of receiveMessage Method
}//End of SubImpl class
 