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

package cy.ac.ucy.paschalis.pdf417.encoder;

/**
 * Holds all of the information for a barcode in a format where it can be easily accessable
 *
 * @author Jacob Haynes
 */
final class BarcodeMatrix {

  private final BarcodeRow[] matrix;
  private int currentRow;
  private final int height;
  private final int width;

  /**
   * @param height the height of the matrix (Rows)
   * @param width  the width of the matrix (Cols)
   */
  BarcodeMatrix(int height, int width) {
    matrix = new BarcodeRow[height + 2];
    //Initializes the array to the correct width
    for (int i = 0, matrixLength = matrix.length; i < matrixLength; i++) {
      matrix[i] = new BarcodeRow((width + 4) * 17 + 1);
    }
    this.width = width * 17;
    this.height = height + 2;
    this.currentRow = 0;
  }

  void set(int x, int y, byte value) {
    matrix[y].set(x, value);
  }

  void setMatrix(int x, int y, boolean black) {
    set(x, y, (byte) (black ? 1 : 0));
  }

  void startRow() {
    ++currentRow;
  }

  BarcodeRow getCurrentRow() {
    return matrix[currentRow];
  }

  byte[][] getMatrix() {
    return getScaledMatrix(1, 1);
  }

  byte[][] getScaledMatrix(int Scale) {
    return getScaledMatrix(Scale, Scale);
  }

  byte[][] getScaledMatrix(int xScale, int yScale) {
    byte[][] matrixOut = new byte[height * yScale][width * xScale];
    int yMax = height * yScale;
    for (int ii = 0; ii < yMax; ii++) {
      matrixOut[yMax - ii - 1] = matrix[ii / yScale].getScaledRow(xScale);
    }
    return matrixOut;
  }
}
