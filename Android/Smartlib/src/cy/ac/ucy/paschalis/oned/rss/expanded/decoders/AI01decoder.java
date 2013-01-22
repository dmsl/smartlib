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

package cy.ac.ucy.paschalis.oned.rss.expanded.decoders;

import cy.ac.ucy.paschalis.common.BitArray;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 * @author Eduardo Castillejo, University of Deusto (eduardo.castillejo@deusto.es)
 */
abstract class AI01decoder extends AbstractExpandedDecoder {

  protected static final int GTIN_SIZE = 40;

  AI01decoder(BitArray information) {
    super(information);
  }

  protected final void encodeCompressedGtin(StringBuilder buf, int currentPos) {
    buf.append("(01)");
    int initialPosition = buf.length();
    buf.append('9');

    encodeCompressedGtinWithoutAI(buf, currentPos, initialPosition);
  }

  protected final void encodeCompressedGtinWithoutAI(StringBuilder buf, int currentPos, int initialBufferPosition) {
    for(int i = 0; i < 4; ++i){
      int currentBlock = this.getGeneralDecoder().extractNumericValueFromBitArray(currentPos + 10 * i, 10);
      if (currentBlock / 100 == 0) {
        buf.append('0');
      }
      if (currentBlock / 10 == 0) {
        buf.append('0');
      }
      buf.append(currentBlock);
    }

      appendCheckDigit(buf, initialBufferPosition);
  }

  private static void appendCheckDigit(StringBuilder buf, int currentPos){
    int checkDigit = 0;
    for (int i = 0; i < 13; i++) {
      int digit = buf.charAt(i + currentPos) - '0';
      checkDigit += (i & 0x01) == 0 ? 3 * digit : digit;
    }

    checkDigit = 10 - (checkDigit % 10);
    if (checkDigit == 10) {
      checkDigit = 0;
    }

    buf.append(checkDigit);
  }

}
