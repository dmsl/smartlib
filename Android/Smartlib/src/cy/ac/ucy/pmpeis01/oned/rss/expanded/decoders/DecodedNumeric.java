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
final class DecodedNumeric extends DecodedObject {

  private final int firstDigit;
  private final int secondDigit;

  static final int FNC1 = 10;

  DecodedNumeric(int newPosition, int firstDigit, int secondDigit){
    super(newPosition);

    this.firstDigit  = firstDigit;
    this.secondDigit = secondDigit;

    if (this.firstDigit < 0 || this.firstDigit > 10) {
      throw new IllegalArgumentException("Invalid firstDigit: " + firstDigit);
    }

    if (this.secondDigit < 0 || this.secondDigit > 10) {
      throw new IllegalArgumentException("Invalid secondDigit: " + secondDigit);
    }
  }

  int getFirstDigit(){
    return this.firstDigit;
  }

  int getSecondDigit(){
    return this.secondDigit;
  }

  int getValue(){
    return this.firstDigit * 10 + this.secondDigit;
  }

  boolean isFirstDigitFNC1(){
    return this.firstDigit == FNC1;
  }

  boolean isSecondDigitFNC1(){
    return this.secondDigit == FNC1;
  }

  boolean isAnyFNC1(){
    return this.firstDigit == FNC1 || this.secondDigit == FNC1;
  }

}
