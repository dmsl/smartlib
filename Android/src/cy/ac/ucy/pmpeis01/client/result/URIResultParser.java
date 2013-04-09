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

package cy.ac.ucy.pmpeis01.client.result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cy.ac.ucy.pmpeis01.Result;

/**
 * Tries to parse results that are a URI of some kind.
 * 
 * @author Sean Owen
 */
public final class URIResultParser extends ResultParser {

  private static final String ALPHANUM_PART = "[a-zA-Z0-9\\-]";
  private static final Pattern URL_WITH_PROTOCOL_PATTERN = Pattern.compile("[a-zA-Z0-9]{2,}:");
  private static final Pattern URL_WITHOUT_PROTOCOL_PATTERN = Pattern.compile(
      '(' + ALPHANUM_PART + "+\\.)+" + ALPHANUM_PART + "{2,}" + // host name elements
      "(:\\d{1,5})?" + // maybe port
      "(/|\\?|$)"); // query, path or nothing

  @Override
  public URIParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    // We specifically handle the odd "URL" scheme here for simplicity and add "URI" for fun
    // Assume anything starting this way really means to be a URI
    if (rawText.startsWith("URL:") || rawText.startsWith("URI:")) {
      return new URIParsedResult(rawText.substring(4).trim(), null);
    }
    rawText = rawText.trim();
    return isBasicallyValidURI(rawText) ? new URIParsedResult(rawText, null) : null;
  }

  static boolean isBasicallyValidURI(CharSequence uri) {
    Matcher m = URL_WITH_PROTOCOL_PATTERN.matcher(uri);
    if (m.find() && m.start() == 0) { // match at start only
      return true;
    }
    m = URL_WITHOUT_PROTOCOL_PATTERN.matcher(uri);
    return m.find() && m.start() == 0;
  }

}