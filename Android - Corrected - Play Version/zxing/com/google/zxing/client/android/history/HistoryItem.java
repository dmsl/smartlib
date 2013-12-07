/*
 * Copyright (C) 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.zxing.client.android.history;

import com.google.zxing.Result;

public final class HistoryItem {

	private final Result result;
	private final String display;
	private final String errormsg;

	HistoryItem(Result result, String display, String pErrormsg) {
		this.result = result;
		this.display = display;
		// this.details = details;

		this.errormsg = pErrormsg;
	}

	public Result getResult() {
		return result;
	}

	public String getErrorMsg() {
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
