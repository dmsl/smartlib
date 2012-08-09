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

package cy.ac.ucy.pmpeis01.oned.rss.expanded;

import cy.ac.ucy.pmpeis01.oned.rss.DataCharacter;
import cy.ac.ucy.pmpeis01.oned.rss.FinderPattern;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 */
final class ExpandedPair {

  private final boolean mayBeLast;
  private final DataCharacter leftChar;
  private final DataCharacter rightChar;
  private final FinderPattern finderPattern;

  ExpandedPair(DataCharacter leftChar,
               DataCharacter rightChar,
               FinderPattern finderPattern,
               boolean mayBeLast) {
    this.leftChar      = leftChar;
    this.rightChar     = rightChar;
    this.finderPattern = finderPattern;
    this.mayBeLast     = mayBeLast;
  }

  boolean mayBeLast(){
    return this.mayBeLast;
  }

  DataCharacter getLeftChar() {
    return this.leftChar;
  }

  DataCharacter getRightChar() {
    return this.rightChar;
  }

  FinderPattern getFinderPattern() {
    return this.finderPattern;
  }

  public boolean mustBeLast() {
    return this.rightChar == null;
  }
}
