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

/**
 * <p>This provides an easy abstraction to read bits at a time from a sequence of bytes, where the
 * number of bits read is not often a multiple of 8.</p>
 *
 * <p>This class is thread-safe but not reentrant -- unless the caller modifies the bytes array
 * it passed in, in which case all bets are off.</p>
 *
 * @author Sean Owen
 */
public final class BitSource {

  private final byte[] bytes;
  private int byteOffset;
  private int bitOffset;

  /**
   * @param bytes bytes from which this will read bits. Bits will be read from the first byte first.
   * Bits are read within a byte from most-significant to least-significant bit.
   */
  public BitSource(byte[] bytes) {
    this.bytes = bytes;
  }

  /**
   * @return index of next byte in input byte array which would be read by the next call to {@link #readBits(int)}.
   */
  public int getByteOffset() {
    return byteOffset;
  }

  /**
   * @param numBits number of bits to read
   * @return int representing the bits read. The bits will appear as the least-significant
   *         bits of the int
   * @throws IllegalArgumentException if numBits isn't in [1,32] or more than is available
   */
  public int readBits(int numBits) {
    if (numBits < 1 || numBits > 32 || numBits > available()) {
      throw new IllegalArgumentException(String.valueOf(numBits));
    }

    int result = 0;

    // First, read remainder from current byte
    if (bitOffset > 0) {
      int bitsLeft = 8 - bitOffset;
      int toRead = numBits < bitsLeft ? numBits : bitsLeft;
      int bitsToNotRead = bitsLeft - toRead;
      int mask = (0xFF >> (8 - toRead)) << bitsToNotRead;
      result = (bytes[byteOffset] & mask) >> bitsToNotRead;
      numBits -= toRead;
      bitOffset += toRead;
      if (bitOffset == 8) {
        bitOffset = 0;
        byteOffset++;
      }
    }

    // Next read whole bytes
    if (numBits > 0) {
      while (numBits >= 8) {
        result = (result << 8) | (bytes[byteOffset] & 0xFF);
        byteOffset++;
        numBits -= 8;
      }

      // Finally read a partial byte
      if (numBits > 0) {
        int bitsToNotRead = 8 - numBits;
        int mask = (0xFF >> bitsToNotRead) << bitsToNotRead;
        result = (result << numBits) | ((bytes[byteOffset] & mask) >> bitsToNotRead);
        bitOffset += numBits;
      }
    }

    return result;
  }

  /**
   * @return number of bits that can be read successfully
   */
  public int available() {
    return 8 * (bytes.length - byteOffset) - bitOffset;
  }

}
