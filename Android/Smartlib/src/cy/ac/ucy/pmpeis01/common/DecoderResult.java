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

package cy.ac.ucy.pmpeis01.common;

import java.util.List;

/**
 * <p>Encapsulates the result of decoding a matrix of bits. This typically
 * applies to 2D barcode formats. For now it contains the raw bytes obtained,
 * as well as a String interpretation of those bytes, if applicable.</p>
 *
 * @author Sean Owen
 */
public final class DecoderResult {

  private final byte[] rawBytes;
  private final String text;
  private final List<byte[]> byteSegments;
  private final String ecLevel;

  public DecoderResult(byte[] rawBytes,
                       String text,
                       List<byte[]> byteSegments,
                       String ecLevel) {
    this.rawBytes = rawBytes;
    this.text = text;
    this.byteSegments = byteSegments;
    this.ecLevel = ecLevel;
  }

  public byte[] getRawBytes() {
    return rawBytes;
  }

  public String getText() {
    return text;
  }

  public List<byte[]> getByteSegments() {
    return byteSegments;
  }

  public String getECLevel() {
    return ecLevel;
  }

}