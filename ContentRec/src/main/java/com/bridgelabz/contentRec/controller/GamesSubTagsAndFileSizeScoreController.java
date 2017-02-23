package com.bridgelabz.contentRec.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

import com.bridgelabz.contentRec.model.GameInfo;
import com.bridgelabz.contentRec.model.GamesSubTagsAndFileSizeScore;
import com.bridgelabz.contentRec.services.GameInfoService;
import com.bridgelabz.contentRec.services.GamesSubTagsAndFileSizeScoreService;
import com.bridgelabz.contentRec.services.VisitorsInfoService;

@Controller
public class GamesSubTagsAndFileSizeScoreController {
	@Autowired
	VisitorsInfoService mVisitorsInfoService;

	@Autowired
	GameInfoService mGameInfoService;

	@Autowired
	GamesSubTagsAndFileSizeScoreService mGamesSubTagsAndFileSizeScoreService;
	
	@Autowired
	ListOfCategoriesInformation mListOfCategoriesInformation;
	int mCategoryStatus;
	Logger mLogger = Logger.getLogger("GAMESUBTAGSANDFILESIZECONTROLLER");

	/**
	 * This method is used to calculate the sub tags and file size score
	 * 
	 * @param String,
	 *            is the first parameter for this method contains visitor Id
	 * @return ModelAndView,UserContentInfo view
	 */
	@RequestMapping(value = "/userContentInfo", method = RequestMethod.GET)
	public ModelAndView getCatScore() {

		/*Properties lProp = new Properties();
		String lFileName = "CategoryList.properties";

		InputStream lInput = null;
		lInput = GamesSubTagsAndFileSizeScoreController.class.getClassLoader().getResourceAsStream(lFileName);
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
			String lVisitorID = (String) iterator.next();
			System.out.println(lVisitorID);
		
		List lCategoryNameList = mVisitorsInfoService.getCategoryNamesByVisitorId(lVisitorID);
		for (Iterator iterator1 = lCategoryNameList.iterator(); iterator1.hasNext();) {
			String lCategoryName = (String) iterator1.next();
			Boolean stt=mListOfCategoriesInformation.categoryExist(lCategoryName);
			//	System.out.println(lCategoryName);
				System.out.println(stt);
				if (stt) {
				GamesSubTagsAndFileSizeScore lCatScore = mGamesSubTagsAndFileSizeScoreService
						.CatgeoryExists(lVisitorID, lCategoryName);
				if (lCatScore != null) {
					mCategoryStatus = mGamesSubTagsAndFileSizeScoreService.UpdateCategoryScore(lVisitorID,
							lCategoryName);

				} // End of if
				else {
					mGamesSubTagsAndFileSizeScoreService.addNewCategory(lVisitorID, lCategoryName);
					mCategoryStatus = mGamesSubTagsAndFileSizeScoreService.UpdateCategoryScore(lVisitorID,
							lCategoryName);
				} // End of else

				List lContentIdList = mVisitorsInfoService.getContentIdByVisitorId(lVisitorID);

				for (Iterator iterator2 = lContentIdList.iterator(); iterator2.hasNext();) {
					String lContentId = (String) iterator2.next();

					String lSubCategoryTagList = mGameInfoService.getSubCategoryTagsByContentId(lContentId);
					if(lSubCategoryTagList!=null){
					String[] subTags = lSubCategoryTagList.split(",");
					for (int i = 0; i < subTags.length; i++) {
						GamesSubTagsAndFileSizeScore userContentInfo = mGamesSubTagsAndFileSizeScoreService
								.SubCatgeoryTagExists(lVisitorID, subTags[i]);
						if (userContentInfo != null) {
							mGamesSubTagsAndFileSizeScoreService.UpdateSubCategoryTagScore(lVisitorID, subTags[i]);
						} // End of if
						else {
							mGamesSubTagsAndFileSizeScoreService.addNewSubCategoryTag(lVisitorID, subTags[i],
									lContentId);
							int mCategorySubTagStatus = mGamesSubTagsAndFileSizeScoreService
									.UpdateSubCategoryTagScore(lVisitorID, subTags[i]);
						} // End of else
					}
					
					} // End of for
					String lFileSize = mGameInfoService.getFileSizeByContentId(lContentId);
					if(lFileSize!=null){
					//System.out.println(lFileSize);
					GamesSubTagsAndFileSizeScore lUserContentInfo = mGamesSubTagsAndFileSizeScoreService
							.FileSizeExists(lVisitorID, lFileSize);
					if (lUserContentInfo != null) {
						mGamesSubTagsAndFileSizeScoreService.UpdateFileSizeScore(lVisitorID, lFileSize);
					} // End of if
					else {

						mGamesSubTagsAndFileSizeScoreService.addNewFileSize(lVisitorID, lFileSize);
						mGamesSubTagsAndFileSizeScoreService.UpdateFileSizeScore(lVisitorID, lFileSize);
					} // End of else
					
					}

				} // End of inner for
			

			} // End of if
			else {
				continue;
			} // End of else
			if (mCategoryStatus > 0) {
				System.out.println("Succesfullly updated" + " " + lCategoryName + " " + "category score for visitrID:"
						+ lVisitorID);

			} // End of if
			else {
				System.out.println("Error occured while updating" + " " + lCategoryName + " "
						+ "category score for visitrID:" + lVisitorID);
			} // End of else

		} // End of if
		}//End of outer for
		return new ModelAndView("subTagandFileSizeSuccess");

	}// End of getCatScore method

	/**
	 * This method is used to display games recommendation form
	 * 
	 * @return ModelAndView,getSubTagsScore view
	 */
	@RequestMapping(value = "/gamesRecommendationBasedOnMostVisitedSubTags", method = RequestMethod.GET)
	public ModelAndView dispalyVisitorFrom() {
		return new ModelAndView("getSubTagsScore");
	}// End of dispalyVisitorFrom method

	/**
	 * This method is used to transfer visited sub tags to view part
	 * 
	 * @return ModelAndView,gamesRecommendationBasedOnMostVisitedSubTags view
	 */
	@RequestMapping(value = "/gamesRecommendationBasedOnMostVisitedSubTags", method = RequestMethod.POST)
	public ModelAndView gamesCategoryNameRecommendation(@RequestParam("visitorId") String parVisitorId,
			Model parModel) {
		List lGameInfoList = new ArrayList();
		List<GamesSubTagsAndFileSizeScore> lGameSubTagsSCore = mGamesSubTagsAndFileSizeScoreService
				.getGamesSubTagsScore(parVisitorId);
		Map<String, Object> lGameInfoAndSubTagsmap = new HashMap<String, Object>();
		List lSubTagsList = mGamesSubTagsAndFileSizeScoreService.gamesSubTagsRecommendationByVisitorId(parVisitorId);
		for (Iterator iterator = lSubTagsList.iterator(); iterator.hasNext();) {
			String lsubTagName = (String) iterator.next();

			List<GameInfo> lGameInfo = mGameInfoService.getGameNameBySubTags(lsubTagName);
			lGameInfoList.add(lGameInfo);
		} // End of for
		parModel.addAttribute("visitorID", parVisitorId);
		lGameInfoAndSubTagsmap.put("Subtags", lGameSubTagsSCore);
		lGameInfoAndSubTagsmap.put("GameInfo", lGameInfoList);

		return new ModelAndView("gamesRecommendationBasedOnMostVisitedSubTags", "GameInfoAndSubTagsmap",
				lGameInfoAndSubTagsmap);
	}// End of gamesCategoryNameRecommendation method

	/**
	 * This method is used to transfer visited file size to view part
	 * 
	 * @return ModelAndView,gamesRecommendationBasedOnMostVisitedSubTags view
	 */
	@RequestMapping(value = "/gamesRecommendationBasedOnFileSize", method = RequestMethod.POST)
	public ModelAndView gamesRecommendationBasedOnFileSize(@RequestParam("visitorId") String parVisitorId,
			Model parModel) {
		List<GamesSubTagsAndFileSizeScore> lGameFileSizeScore = mGamesSubTagsAndFileSizeScoreService
				.getGamesFileSizeScore(parVisitorId);
		parModel.addAttribute("visitorID", parVisitorId);
		List<GameInfo> lGameInfo = mGameInfoService.getGameNameByFileSize(parVisitorId);
		Map<String, Object> lMap = new HashMap<String, Object>();
		lMap.put("gameFileScore", lGameFileSizeScore);
		lMap.put("gameInfo", lGameInfo);
		return new ModelAndView("gamesRecommendationBasedOnFileSize", "map", lMap);
	}// End of gamesRecommendationBasedOnFileSize method

	/**
	 * This method is used to display games recommendation form
	 * 
	 * @return ModelAndView,gamesRecommendationBasedOnFileSizeVisitorForm view
	 */
	@RequestMapping(value = "/gamesRecommendationBasedOnFileSize", method = RequestMethod.GET)
	public ModelAndView gamesRecommendationBasedOnFileSizeVisitorForm() {
		return new ModelAndView("gamesRecommendationBasedOnFileSizeVisitorForm");

	}// End of gamesRecommendationBasedOnFileSizeVisitorForm method

}// End of GamesSubTagsAndFileSizeScoreController class
