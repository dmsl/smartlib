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

package cy.ac.ucy.paschalis.pdf417.decoder;

import cy.ac.ucy.paschalis.ChecksumException;
import cy.ac.ucy.paschalis.FormatException;
import cy.ac.ucy.paschalis.common.BitMatrix;
import cy.ac.ucy.paschalis.common.DecoderResult;
import cy.ac.ucy.paschalis.pdf417.decoder.ec.ErrorCorrection;

/**
 * <p>The main class which implements PDF417 Code decoding -- as
 * opposed to locating and extracting the PDF417 Code from an image.</p>
 *
 * @author SITA Lab (kevin.osullivan@sita.aero)
 */
public final class Decoder {

  private static final int MAX_ERRORS = 3;
  private static final int MAX_EC_CODEWORDS = 512;
  private final ErrorCorrection errorCorrection;

  public Decoder() {
    errorCorrection = new ErrorCorrection();
  }

  /**
   * <p>Convenience method that can decode a PDF417 Code represented as a 2D array of booleans.
   * "true" is taken to mean a black module.</p>
   *
   * @param image booleans representing white/black PDF417 modules
   * @return text and bytes encoded within the PDF417 Code
   */
  public DecoderResult decode(boolean[][] image) throws FormatException, ChecksumException {
    int dimension = image.length;
    BitMatrix bits = new BitMatrix(dimension);
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        if (image[j][i]) {
          bits.set(j, i);
        }
      }
    }
    return decode(bits);
  }

  /**
   * <p>Decodes a PDF417 Code represented as a {@link BitMatrix}.
   * A 1 or "true" is taken to mean a black module.</p>
   *
   * @param bits booleans representing white/black PDF417 Code modules
   * @return text and bytes encoded within the PDF417 Code
   * @throws FormatException if the PDF417 Code cannot be decoded
   */
  public DecoderResult decode(BitMatrix bits) throws FormatException, ChecksumException {
    // Construct a parser to read the data codewords and error-correction level
    BitMatrixParser parser = new BitMatrixParser(bits);
    int[] codewords = parser.readCodewords();
    if (codewords.length == 0) {
      throw FormatException.getFormatInstance();
    }

    int ecLevel = parser.getECLevel();
    int numECCodewords = 1 << (ecLevel + 1);
    int[] erasures = parser.getErasures();

    correctErrors(codewords, erasures, numECCodewords);
    verifyCodewordCount(codewords, numECCodewords);

    // Decode the codewords
    return DecodedBitStreamParser.decode(codewords);
  }

  /**
   * Verify that all is OK with the codeword array.
   *
   * @param codewords
   * @return an index to the first data codeword.
   */
  private static void verifyCodewordCount(int[] codewords, int numECCodewords) throws FormatException {
    if (codewords.length < 4) {
      // Codeword array size should be at least 4 allowing for
      // Count CW, At least one Data CW, Error Correction CW, Error Correction CW
      throw FormatException.getFormatInstance();
    }
    // The first codeword, the Symbol Length Descriptor, shall always encode the total number of data
    // codewords in the symbol, including the Symbol Length Descriptor itself, data codewords and pad
    // codewords, but excluding the number of error correction codewords.
    int numberOfCodewords = codewords[0];
    if (numberOfCodewords > codewords.length) {
      throw FormatException.getFormatInstance();
    }
    if (numberOfCodewords == 0) {
      // Reset to the length of the array - 8 (Allow for at least level 3 Error Correction (8 Error Codewords)
      if (numECCodewords < codewords.length) {
        codewords[0] = codewords.length - numECCodewords;
      } else {
        throw FormatException.getFormatInstance();
      }
    }
  }

  /**
   * <p>Given data and error-correction codewords received, possibly corrupted by errors, attempts to
   * correct the errors in-place.</p>
   *
   * @param codewords   data and error correction codewords
   * @param erasures positions of any known erasures
   * @param numECCodewords number of error correction codewards that were available in codewords
   * @throws ChecksumException if error correction fails
   */
  private void correctErrors(int[] codewords,
                             int[] erasures,
                             int numECCodewords) throws ChecksumException {
    if (erasures.length > numECCodewords / 2 + MAX_ERRORS ||
        numECCodewords < 0 || numECCodewords > MAX_EC_CODEWORDS) {
      // Too many errors or EC Codewords is corrupted
      throw ChecksumException.getChecksumInstance();
    }
    errorCorrection.decode(codewords, numECCodewords, erasures);
  }

}
