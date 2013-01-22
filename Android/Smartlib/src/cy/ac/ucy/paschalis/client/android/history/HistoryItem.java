/*
 This file is part of SmartLib Project.

    SmartLib is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SmartLib is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SmartLib.  If not, see <http://www.gnu.org/licenses/>.
    
	Author: Paschalis Mpeis

	Affiliation:
	Data Management Systems Laboratory 
	Dept. of Computer Science 
	University of Cyprus 
	P.O. Box 20537 
	1678 Nicosia, CYPRUS 
	Web: http://dmsl.cs.ucy.ac.cy/
	Email: dmsl@cs.ucy.ac.cy
	Tel: +357-22-892755
	Fax: +357-22-892701
	

 */


package cy.ac.ucy.paschalis.client.android.history;

import cy.ac.ucy.paschalis.Result;

public final class HistoryItem {

  private final Result result;
  private final String display;
  private final String errormsg;
  
 HistoryItem(Result result, String display, String pErrormsg) {
    this.result = result;
    this.display = display;
   // this.details = details;
    
    this.errormsg=pErrormsg;
  }

  public Result getResult() {
    return result;
  }
  
  public String getErrorMsg(){
	  return errormsg;
  }
  
  /**
 * @return Strings.xml code
 */
//public String getErrorCode(){
//	  if(errorCode==CaptureActivity.INSERT_BOOK_ALREADY_EXISTED)
//		  return R.string.msgInsertBookAlreadyExists;
//	  else if(errorCode==CaptureActivity.INSERT_BOOK_DATABASE_INTERNAL_ERROR)
//		  return R.string.msgInsertBookDatabaseInternalError;
//	 else if(errorCode==CaptureActivity.INSERT_BOOK_DIDNT_EXIST_IN_GOOGLE_API)
//		  return R.string.msgInsertBookDontExistsInGAPI;
//	 else if(errorCode==CaptureActivity.INSERT_BOOK_NO_INTERNET)
//		  return R.string.msgNoInternetConnectionTitle;
//	 else if(errorCode==CaptureActivity.INSERT_BOOK_PARSING_FAILED)
//		  return R.string.msgInsertBookParseError;
//	 else if(errorCode==CaptureActivity.INSERT_BOOK_WEIRD_ERROR)
//		  return R.string.msgInsertBookParseError;
//	  
//	  //everything is fine!
//	return 0;
//  }

//  public String getDisplayAndDetails() {
//    StringBuilder displayResult = new StringBuilder();
//    if (display == null || display.length() == 0) {
//      displayResult.append(result.getText());
//    } else {
//      displayResult.append(display);
//    }
//    if (details != null && details.length() > 0) {
//      displayResult.append(" : ").append(details);
//    }
//    return displayResult.toString();
//  }
  
  
  
}
