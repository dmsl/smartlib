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

package cy.ac.ucy.paschalis.client.android.result.supplement;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;
import cy.ac.ucy.paschalis.client.android.HttpHelper;
import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.paschalis.client.android.history.HistoryManager;
import cy.ac.ucy.paschalis.client.result.URIParsedResult;

final class URIResultInfoRetriever extends SupplementalInfoRetriever {

  private static final int MAX_REDIRECTS = 5;

  private final URIParsedResult result;
  private final String redirectString;

  URIResultInfoRetriever(TextView textView,
                         URIParsedResult result,
                         Handler handler,
                         HistoryManager historyManager,
                         Context context) {
    super(textView, handler, historyManager);
    redirectString = context.getString(R.string.msg_redirect);
    this.result = result;
  }

  @Override
  void retrieveSupplementalInfo() throws IOException, InterruptedException {
    URI oldURI;
    try {
      oldURI = new URI(result.getURI());
    } catch (URISyntaxException e) {
      return;
    }
    URI newURI = HttpHelper.unredirect(oldURI);
    int count = 0;
    while (count++ < MAX_REDIRECTS && !oldURI.equals(newURI)) {
      append(result.getDisplayResult(), 
             null, 
             new String[] { redirectString + " : " + newURI }, 
             newURI.toString());
      oldURI = newURI;
      newURI = HttpHelper.unredirect(newURI);
    }
  }

}
