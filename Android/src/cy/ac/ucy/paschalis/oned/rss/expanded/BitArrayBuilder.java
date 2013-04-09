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

package cy.ac.ucy.paschalis.oned.rss.expanded;

import java.util.List;

import cy.ac.ucy.paschalis.common.BitArray;

/**
 * @author Pablo Orduña, University of Deusto (pablo.orduna@deusto.es)
 * @author Eduardo Castillejo, University of Deusto (eduardo.castillejo@deusto.es)
 */
final class BitArrayBuilder {

  private BitArrayBuilder() {
  }

  static BitArray buildBitArray(List<ExpandedPair> pairs) {
    int charNumber = (pairs.size() << 1) - 1;
    if (pairs.get(pairs.size() - 1).getRightChar() == null) {
      charNumber -= 1;
    }

    int size = 12 * charNumber;

    BitArray binary = new BitArray(size);
    int accPos = 0;

    ExpandedPair firstPair = pairs.get(0);
    int firstValue = firstPair.getRightChar().getValue();
    for(int i = 11; i >= 0; --i){
      if ((firstValue & (1 << i)) != 0) {
        binary.set(accPos);
      }
      accPos++;
    }

    for(int i = 1; i < pairs.size(); ++i){
      ExpandedPair currentPair = pairs.get(i);

      int leftValue = currentPair.getLeftChar().getValue();
      for(int j = 11; j >= 0; --j){
        if ((leftValue & (1 << j)) != 0) {
          binary.set(accPos);
        }
        accPos++;
      }

      if(currentPair.getRightChar() != null){
        int rightValue = currentPair.getRightChar().getValue();
        for(int j = 11; j >= 0; --j){
          if ((rightValue & (1 << j)) != 0) {
            binary.set(accPos);
          }
          accPos++;
        }
      }
    }
    return binary;
  }
}
