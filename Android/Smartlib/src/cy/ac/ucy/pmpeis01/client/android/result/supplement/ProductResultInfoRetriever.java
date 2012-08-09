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

package cy.ac.ucy.pmpeis01.client.android.result.supplement;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.widget.TextView;
import cy.ac.ucy.pmpeis01.client.android.HttpHelper;
import cy.ac.ucy.pmpeis01.client.android.LocaleManager;
import cy.ac.ucy.pmpeis01.client.android.R;
import cy.ac.ucy.pmpeis01.client.android.history.HistoryManager;

final class ProductResultInfoRetriever extends SupplementalInfoRetriever {

  private static final Pattern PRODUCT_NAME_PRICE_PATTERN =
      Pattern.compile("owb63p\">([^<]+).+zdi3pb\">([^<]+)");


  private final String productID;
  private final String source;
  private final Context context;

  ProductResultInfoRetriever(TextView textView,
                             String productID,
                             Handler handler,
                             HistoryManager historyManager,
                             Context context) {
    super(textView, handler, historyManager);
    this.productID = productID;
    this.source = context.getString(R.string.msg_google_product);
    this.context = context;
  }

  @Override
  void retrieveSupplementalInfo() throws IOException, InterruptedException {

    String encodedProductID = URLEncoder.encode(productID, "UTF-8");
    String uri = "http://www.google." + LocaleManager.getProductSearchCountryTLD(context)
            + "/m/products?ie=utf8&oe=utf8&scoring=p&source=zxing&q=" + encodedProductID;
    String content = HttpHelper.downloadViaHttp(uri, HttpHelper.ContentType.HTML);

    Matcher matcher = PRODUCT_NAME_PRICE_PATTERN.matcher(content);
    if (matcher.find()) {
      append(productID,
             source,
             new String[] { unescapeHTML(matcher.group(1)), unescapeHTML(matcher.group(2)) },
             uri);
    }
  }

  private static String unescapeHTML(String raw) {
    return Html.fromHtml(raw).toString();
  }

}
