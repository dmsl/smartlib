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
 * <p>Encapsulates a finder pattern, which are the three square patterns found in
 * the corners of QR Codes. It also encapsulates a count of similar finder patterns,
 * as a convenience to the finder's bookkeeping.</p>
 *
 * @author Sean Owen
 */
public final class FinderPattern extends ResultPoint {

  private final float estimatedModuleSize;
  private int count;

  FinderPattern(float posX, float posY, float estimatedModuleSize) {
    this(posX, posY, estimatedModuleSize, 1);
  }

  private FinderPattern(float posX, float posY, float estimatedModuleSize, int count) {
    super(posX, posY);
    this.estimatedModuleSize = estimatedModuleSize;
    this.count = count;
  }

  public float getEstimatedModuleSize() {
    return estimatedModuleSize;
  }

  int getCount() {
    return count;
  }

  void incrementCount() {
    this.count++;
  }

  /**
   * <p>Determines if this finder pattern "about equals" a finder pattern at the stated
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
   * with a new estimate. It returns a new {@code FinderPattern} containing a weighted average
   * based on count.
   */
  FinderPattern combineEstimate(float i, float j, float newModuleSize) {
    int combinedCount = count + 1;
    float combinedX = (count * getX() + j) / combinedCount;
    float combinedY = (count * getY() + i) / combinedCount;
    float combinedModuleSize = (count * estimatedModuleSize + newModuleSize) / combinedCount;
    return new FinderPattern(combinedX, combinedY, combinedModuleSize, combinedCount);
  }

}
