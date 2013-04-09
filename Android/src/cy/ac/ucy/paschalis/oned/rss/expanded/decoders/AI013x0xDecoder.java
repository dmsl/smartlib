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

import cy.ac.ucy.paschalis.NotFoundException;
import cy.ac.ucy.paschalis.common.BitArray;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 */
abstract class AI013x0xDecoder extends AI01weightDecoder {

  private static final int HEADER_SIZE = 4 + 1;
  private static final int WEIGHT_SIZE = 15;

  AI013x0xDecoder(BitArray information) {
    super(information);
  }

  @Override
  public String parseInformation() throws NotFoundException {
    if (this.getInformation().getSize() != HEADER_SIZE + GTIN_SIZE + WEIGHT_SIZE) {
      throw NotFoundException.getNotFoundInstance();
    }

    StringBuilder buf = new StringBuilder();

    encodeCompressedGtin(buf, HEADER_SIZE);
    encodeCompressedWeight(buf, HEADER_SIZE + GTIN_SIZE, WEIGHT_SIZE);

    return buf.toString();
  }
}
