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
 * @author Jacob Haynes
 */
final class BarcodeRow {

  private final byte[] row;
  //A tacker for position in the bar
  private int currentLocation;

  /**
   * Creates a Barcode row of the width
   *
   * @param width
   */
  BarcodeRow(int width) {
    this.row = new byte[width];
    currentLocation = 0;
  }

  /**
   * Sets a specific location in the bar
   *
   * @param x The location in the bar
   * @param value Black if true, white if false;
   */
  void set(int x, byte value) {
    row[x] = value;
  }

  /**
   * Sets a specific location in the bar
   *
   * @param x The location in the bar
   * @param black Black if true, white if false;
   */
  void set(int x, boolean black) {
    row[x] = (byte) (black ? 1 : 0);
  }

  /**
   * @param black A boolean which is true if the bar black false if it is white
   * @param width How many spots wide the bar is.
   */
  void addBar(boolean black, int width) {
    for (int ii = 0; ii < width; ii++) {
      set(currentLocation++, black);
    }
  }

  byte[] getRow() {
    return row;
  }

  /**
   * This function scales the row
   *
   * @param scale How much you want the image to be scaled, must be greater than or equal to 1.
   * @return the scaled row
   */
  byte[] getScaledRow(int scale) {
    byte[] output = new byte[row.length * scale];
    for (int i = 0; i < output.length; i++) {
      output[i] = row[i / scale];
    }
    return output;
  }
}
