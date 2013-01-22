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

package cy.ac.ucy.paschalis.client.android.book;

/**
 * The underlying data for a SBC result.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
final class SearchBookContentsResult {

  private static String query;

  private final String pageId;
  private final String pageNumber;
  private final String snippet;
  private final boolean validSnippet;

    SearchBookContentsResult(String pageId,
                             String pageNumber,
                             String snippet,
                             boolean validSnippet) {
    this.pageId = pageId;
    this.pageNumber = pageNumber;
    this.snippet = snippet;
    this.validSnippet = validSnippet;
  }

  public static void setQuery(String query) {
    SearchBookContentsResult.query = query;
  }

  public String getPageId() {
    return pageId;
  }

  public String getPageNumber() {
    return pageNumber;
  }

  public String getSnippet() {
    return snippet;
  }

  public boolean getValidSnippet() {
    return validSnippet;
  }

  public static String getQuery() {
    return query;
  }
}
