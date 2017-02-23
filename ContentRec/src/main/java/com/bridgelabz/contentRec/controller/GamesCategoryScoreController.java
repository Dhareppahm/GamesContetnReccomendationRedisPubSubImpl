/*
* @Author:Dhareppa Metri
* File:GamesCategoryScoreController.java
* Purpose:Controller class for to calculate game category score information.
**/
package com.bridgelabz.contentRec.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bridgelabz.contentRec.model.GameCategoryScore;
import com.bridgelabz.contentRec.model.GameInfo;
import com.bridgelabz.contentRec.services.GameCategoryScoreService;
import com.bridgelabz.contentRec.services.GameInfoService;
import com.bridgelabz.contentRec.services.VisitorsInfoService;

@Controller
public class GamesCategoryScoreController {
	@Autowired
	GameCategoryScoreService mGameCategoryScoreService;
	@Autowired
	VisitorsInfoService mVisitorsInfoService;
	@Autowired
	GameInfoService mGameInfoService;
	
	@Autowired
	ListOfCategoriesInformation mListOfCategoriesInformation;

	Logger mLogger = Logger.getLogger("GAMECATEGORYSCORECONTROLLER");


	/**
	 * This method is used to calculate game category score
	 * 
	 * @param String,
	 *            is the first parameter for this method contains visitor Id
	 * @return ModelAndView,FetchAndSaveGameInfo view
	 */
	@RequestMapping(value = "/getCategoryScore", method = RequestMethod.GET)
	public ModelAndView getCatScore() {
		int lStatus;
		/*Properties lProp = new Properties();
		String lFileName = "CategoryList.properties";

		InputStream lInput = null;
		lInput = GamesCategoryScoreController.class.getClassLoader().getResourceAsStream(lFileName);
		try {

			if (lInput == null) {
				System.out.println("Sorry, unable to find " + lFileName);
			} // End of if
			lProp.load(lInput);

		} // End of try
		catch (IOException e1) {
			e1.printStackTrace();
		} // End of catch
		finally {
			if (lInput != null) {
				try {
					lInput.close();
				} // End of try
				catch (IOException e) {
					e.printStackTrace();
				} // End of catch
			} // End of if
		} // End of finally
*/		List lListOfVisitorId=mVisitorsInfoService.getDistinctVisitorId();
		for (Iterator iterator = lListOfVisitorId.iterator(); iterator.hasNext();) {
			String lVisitorId = (String) iterator.next();
			
		
		List lCategoryNameList = mVisitorsInfoService.getCategoryNamesByVisitorId(lVisitorId);
	//	CategoryList catl=new CategoryList();
	
		for (Iterator iterator1 = lCategoryNameList.iterator(); iterator1.hasNext();) {
			String lCategoryName = (String) iterator1.next();
			Boolean stt=mListOfCategoriesInformation.categoryExist(lCategoryName);
		//	System.out.println(lCategoryName);
			System.out.println(stt);
			if (stt) {
				GameCategoryScore lCatScore = mGameCategoryScoreService.CatgeoryExists(lVisitorId, lCategoryName);
				if (lCatScore != null) {
					lStatus = mGameCategoryScoreService.UpdateCategoryScore(lVisitorId, lCategoryName);

				} // End of if
				else {
					mGameCategoryScoreService.addNewCategory(lVisitorId, lCategoryName);
					lStatus = mGameCategoryScoreService.UpdateCategoryScore(lVisitorId, lCategoryName);
				} // End of else
			} else {
				continue;
			} // End of else
			if (lStatus > 0) {
				System.out.println("Succesfullly updated" + " " + lCategoryName + " " + "category score for visitrID:"
						+ lVisitorId);

			} // End of if
			else {
				System.out.println("Error occured while updating" + " " + lCategoryName + " "
						+ "category scogetCategoryNameByVisitorIdre for visitrID:" + lVisitorId);
			} // End of else

		} // End of inner for
	}// End of outer for
		return new ModelAndView("categoryScoreSuccess");

	}// End of getCatScore method

	/**
	 * This method is used to display visitor form
	 * 
	 * @return ModelAndView,VisitorFormToRecommendGamesBasedOnCategories view
	 */
	@RequestMapping(value = "/gamesCategoryNameRecommendation", method = RequestMethod.GET)
	public ModelAndView displayGamesCategoryNameRecommendation() {
		return new ModelAndView("VisitorFormToRecommendGamesBasedOnCategories");

	}// End of displayGamesCategoryNameRecommendation method

	/**
	 * This method is used to transfer games category name and games name
	 * recommendations to view part
	 * 
	 * @param String,
	 *            is the first parameter for this method contains visitor Id
	 * @return ModelAndView,gamesCategoryNameAndGamesNameRecommendations view
	 */
	@RequestMapping(value = "/gamesCategoryNameAndGamesNameRecommendation", method = RequestMethod.POST)
	public ModelAndView gamesCategoryNameRecommendation(@RequestParam("visitorId") String parVisitorId,
			Model parModel) {
		List<GameCategoryScore> lGameCategoryScore = mGameCategoryScoreService
				.gamesCategoryNameRecommendationByVisitorId(parVisitorId);
		parModel.addAttribute("visitorID", parVisitorId);
		List<GameInfo> lGameInfo = mGameInfoService.getGameNameByGameCategory(parVisitorId);
		Map<String, Object> lMap = new HashMap<String, Object>();
		lMap.put("gameCategoryScore", lGameCategoryScore);
		lMap.put("gameInfo", lGameInfo);
		return new ModelAndView("gamesCategoryNameAndGamesNameRecommendations", "map", lMap);
	}// End of gamesCategoryNameRecommendation method

}// End of GamesCategoryScoreController class
