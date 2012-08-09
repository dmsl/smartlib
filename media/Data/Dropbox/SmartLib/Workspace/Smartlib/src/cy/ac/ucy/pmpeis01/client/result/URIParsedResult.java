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

import java.util.regex.Pattern;

/**
 * @author Sean Owen
 */
public final class URIParsedResult extends ParsedResult {

  private static final Pattern USER_IN_HOST = Pattern.compile(":/*([^/@]+)@[^/]+");

  private final String uri;
  private final String title;

  public URIParsedResult(String uri, String title) {
    super(ParsedResultType.URI);
    this.uri = massageURI(uri);
    this.title = title;
  }

  public String getURI() {
    return uri;
  }

  public String getTitle() {
    return title;
  }

  /**
   * @return true if the URI contains suspicious patterns that may suggest it intends to
   *  mislead the user about its true nature. At the moment this looks for the presence
   *  of user/password syntax in the host/authority portion of a URI which may be used
   *  in attempts to make the URI's host appear to be other than it is. Example:
   *  http://yourbank.com@phisher.com  This URI connects to phisher.com but may appear
   *  to connect to yourbank.com at first glance.
   */
  public boolean isPossiblyMaliciousURI() {
    return USER_IN_HOST.matcher(uri).find();
  }

  @Override
  public String getDisplayResult() {
    StringBuilder result = new StringBuilder(30);
    maybeAppend(title, result);
    maybeAppend(uri, result);
    return result.toString();
  }

  /**
   * Transforms a string that represents a URI into something more proper, by adding or canonicalizing
   * the protocol.
   */
  private static String massageURI(String uri) {
    uri = uri.trim();
    int protocolEnd = uri.indexOf(':');
    if (protocolEnd < 0) {
      // No protocol, assume http
      uri = "http://" + uri;
    } else if (isColonFollowedByPortNumber(uri, protocolEnd)) {
      // Found a colon, but it looks like it is after the host, so the protocol is still missing
      uri = "http://" + uri;
    }
    return uri;
  }

  private static boolean isColonFollowedByPortNumber(String uri, int protocolEnd) {
    int nextSlash = uri.indexOf('/', protocolEnd + 1);
    if (nextSlash < 0) {
      nextSlash = uri.length();
    }
    if (nextSlash <= protocolEnd + 1) {
      return false;
    }
    for (int x = protocolEnd + 1; x < nextSlash; x++) {
      if (uri.charAt(x) < '0' || uri.charAt(x) > '9') {
        return false;
      }
    }
    return true;
  }


}