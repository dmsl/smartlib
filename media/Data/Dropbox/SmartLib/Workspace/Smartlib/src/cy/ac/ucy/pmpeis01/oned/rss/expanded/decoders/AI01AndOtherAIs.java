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

import cy.ac.ucy.pmpeis01.NotFoundException;
import cy.ac.ucy.pmpeis01.common.BitArray;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 * @author Eduardo Castillejo, University of Deusto (eduardo.castillejo@deusto.es)
 */
final class AI01AndOtherAIs extends AI01decoder {

  private static final int HEADER_SIZE = 1 + 1 + 2; //first bit encodes the linkage flag,
                          //the second one is the encodation method, and the other two are for the variable length
  AI01AndOtherAIs(BitArray information) {
    super(information);
  }

  @Override
  public String parseInformation() throws NotFoundException {
    StringBuilder buff = new StringBuilder();

    buff.append("(01)");
    int initialGtinPosition = buff.length();
    int firstGtinDigit = this.getGeneralDecoder().extractNumericValueFromBitArray(HEADER_SIZE, 4);
    buff.append(firstGtinDigit);

    this.encodeCompressedGtinWithoutAI(buff, HEADER_SIZE + 4, initialGtinPosition);

    return this.getGeneralDecoder().decodeAllCodes(buff, HEADER_SIZE + 44);
  }
}
