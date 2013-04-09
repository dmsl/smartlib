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

package cy.ac.ucy.pmpeis01.oned.rss.expanded.decoders;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 * @author Eduardo Castillejo, University of Deusto (eduardo.castillejo@deusto.es)
 */
final class DecodedInformation extends DecodedObject {

  private final String newString;
  private final int remainingValue;
  private final boolean remaining;

  DecodedInformation(int newPosition, String newString){
    super(newPosition);
    this.newString = newString;
    this.remaining = false;
    this.remainingValue = 0;
  }

  DecodedInformation(int newPosition, String newString, int remainingValue){
    super(newPosition);
    this.remaining = true;
    this.remainingValue = remainingValue;
    this.newString = newString;
  }

  String getNewString(){
    return this.newString;
  }

  boolean isRemaining(){
    return this.remaining;
  }

  int getRemainingValue(){
    return this.remainingValue;
  }
}
