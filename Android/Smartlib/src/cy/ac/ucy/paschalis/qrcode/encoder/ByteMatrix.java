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

package cy.ac.ucy.paschalis.qrcode.encoder;

/**
 * A class which wraps a 2D array of bytes. The default usage is signed. If you want to use it as a
 * unsigned container, it's up to you to do byteValue & 0xff at each location.
 *
 * JAVAPORT: The original code was a 2D array of ints, but since it only ever gets assigned
 * -1, 0, and 1, I'm going to use less memory and go with bytes.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ByteMatrix {

  private final byte[][] bytes;
  private final int width;
  private final int height;

  public ByteMatrix(int width, int height) {
    bytes = new byte[height][width];
    this.width = width;
    this.height = height;
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public byte get(int x, int y) {
    return bytes[y][x];
  }

  public byte[][] getArray() {
    return bytes;
  }

  public void set(int x, int y, byte value) {
    bytes[y][x] = value;
  }

  public void set(int x, int y, int value) {
    bytes[y][x] = (byte) value;
  }

  public void set(int x, int y, boolean value) {
    bytes[y][x] = (byte) (value ? 1 : 0);
  }

  public void clear(byte value) {
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        bytes[y][x] = value;
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder(2 * width * height + 2);
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        switch (bytes[y][x]) {
          case 0:
            result.append(" 0");
            break;
          case 1:
            result.append(" 1");
            break;
          default:
            result.append("  ");
            break;
        }
      }
      result.append('\n');
    }
    return result.toString();
  }

}
