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

package cy.ac.ucy.pmpeis01.qrcode.encoder;

import cy.ac.ucy.pmpeis01.qrcode.decoder.ErrorCorrectionLevel;
import cy.ac.ucy.pmpeis01.qrcode.decoder.Mode;
import cy.ac.ucy.pmpeis01.qrcode.decoder.Version;

/**
 * @author satorux@google.com (Satoru Takabayashi) - creator
 * @author dswitkin@google.com (Daniel Switkin) - ported from C++
 */
public final class QRCode {

  public static final int NUM_MASK_PATTERNS = 8;

  private Mode mode;
  private ErrorCorrectionLevel ecLevel;
  private Version version;
  private int maskPattern;
  private ByteMatrix matrix;

  public QRCode() {
    maskPattern = -1;
  }

  public Mode getMode() {
    return mode;
  }

  public ErrorCorrectionLevel getECLevel() {
    return ecLevel;
  }

  public Version getVersion() {
    return version;
  }

  public int getMaskPattern() {
    return maskPattern;
  }

  public ByteMatrix getMatrix() {
    return matrix;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder(200);
    result.append("<<\n");
    result.append(" mode: ");
    result.append(mode);
    result.append("\n ecLevel: ");
    result.append(ecLevel);
    result.append("\n version: ");
    result.append(version);
    result.append("\n maskPattern: ");
    result.append(maskPattern);
    if (matrix == null) {
      result.append("\n matrix: null\n");
    } else {
      result.append("\n matrix:\n");
      result.append(matrix.toString());
    }
    result.append(">>\n");
    return result.toString();
  }

  public void setMode(Mode value) {
    mode = value;
  }

  public void setECLevel(ErrorCorrectionLevel value) {
    ecLevel = value;
  }

  public void setVersion(Version version) {
    this.version = version;
  }

  public void setMaskPattern(int value) {
    maskPattern = value;
  }

  public void setMatrix(ByteMatrix value) {
    matrix = value;
  }

  // Check if "mask_pattern" is valid.
  public static boolean isValidMaskPattern(int maskPattern) {
    return maskPattern >= 0 && maskPattern < NUM_MASK_PATTERNS;
  }

}
