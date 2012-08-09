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
public abstract class AbstractExpandedDecoder {

  private final BitArray information;
  private final GeneralAppIdDecoder generalDecoder;

  AbstractExpandedDecoder(BitArray information){
    this.information = information;
    this.generalDecoder = new GeneralAppIdDecoder(information);
  }

  protected final BitArray getInformation() {
    return information;
  }

  protected final GeneralAppIdDecoder getGeneralDecoder() {
    return generalDecoder;
  }

  public abstract String parseInformation() throws NotFoundException;

  public static AbstractExpandedDecoder createDecoder(BitArray information){
    if (information.get(1)) {
      return new AI01AndOtherAIs(information);
    }
    if (!information.get(2)) {
      return new AnyAIDecoder(information);
    }

    int fourBitEncodationMethod = GeneralAppIdDecoder.extractNumericValueFromBitArray(information, 1, 4);

    switch(fourBitEncodationMethod){
      case 4: return new AI013103decoder(information);
      case 5: return new AI01320xDecoder(information);
    }

    int fiveBitEncodationMethod = GeneralAppIdDecoder.extractNumericValueFromBitArray(information, 1, 5);
    switch(fiveBitEncodationMethod){
      case 12: return new AI01392xDecoder(information);
      case 13: return new AI01393xDecoder(information);
    }

    int sevenBitEncodationMethod = GeneralAppIdDecoder.extractNumericValueFromBitArray(information, 1, 7);
    switch(sevenBitEncodationMethod){
      case 56: return new AI013x0x1xDecoder(information, "310", "11");
      case 57: return new AI013x0x1xDecoder(information, "320", "11");
      case 58: return new AI013x0x1xDecoder(information, "310", "13");
      case 59: return new AI013x0x1xDecoder(information, "320", "13");
      case 60: return new AI013x0x1xDecoder(information, "310", "15");
      case 61: return new AI013x0x1xDecoder(information, "320", "15");
      case 62: return new AI013x0x1xDecoder(information, "310", "17");
      case 63: return new AI013x0x1xDecoder(information, "320", "17");
    }

    throw new IllegalStateException("unknown decoder: " + information);
  }

}
