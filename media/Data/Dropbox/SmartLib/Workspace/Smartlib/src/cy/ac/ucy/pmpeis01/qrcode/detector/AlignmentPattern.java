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

package cy.ac.ucy.pmpeis01.qrcode.detector;

import cy.ac.ucy.pmpeis01.ResultPoint;

/**
 * <p>Encapsulates an alignment pattern, which are the smaller square patterns found in
 * all but the simplest QR Codes.</p>
 *
 * @author Sean Owen
 */
public final class AlignmentPattern extends ResultPoint {

  private final float estimatedModuleSize;

  AlignmentPattern(float posX, float posY, float estimatedModuleSize) {
    super(posX, posY);
    this.estimatedModuleSize = estimatedModuleSize;
  }

  /**
   * <p>Determines if this alignment pattern "about equals" an alignment pattern at the stated
   * position and size -- meaning, it is at nearly the same center with nearly the same size.</p>
   */
  boolean aboutEquals(float moduleSize, float i, float j) {
    if (Math.abs(i - getY()) <= moduleSize && Math.abs(j - getX()) <= moduleSize) {
      float moduleSizeDiff = Math.abs(moduleSize - estimatedModuleSize);
      return moduleSizeDiff <= 1.0f || moduleSizeDiff <= estimatedModuleSize;
    }
    return false;
  }

  /**
   * Combines this object's current estimate of a finder pattern position and module size
   * with a new estimate. It returns a new {@code FinderPattern} containing an average of the two.
   */
  AlignmentPattern combineEstimate(float i, float j, float newModuleSize) {
    float combinedX = (getX() + j) / 2.0f;
    float combinedY = (getY() + i) / 2.0f;
    float combinedModuleSize = (estimatedModuleSize + newModuleSize) / 2.0f;
    return new AlignmentPattern(combinedX, combinedY, combinedModuleSize);
  }

}