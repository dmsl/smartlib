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

package cy.ac.ucy.paschalis.maxicode;

import java.util.Map;

import cy.ac.ucy.paschalis.BarcodeFormat;
import cy.ac.ucy.paschalis.BinaryBitmap;
import cy.ac.ucy.paschalis.ChecksumException;
import cy.ac.ucy.paschalis.DecodeHintType;
import cy.ac.ucy.paschalis.FormatException;
import cy.ac.ucy.paschalis.NotFoundException;
import cy.ac.ucy.paschalis.Reader;
import cy.ac.ucy.paschalis.Result;
import cy.ac.ucy.paschalis.ResultMetadataType;
import cy.ac.ucy.paschalis.ResultPoint;
import cy.ac.ucy.paschalis.common.BitMatrix;
import cy.ac.ucy.paschalis.common.DecoderResult;
import cy.ac.ucy.paschalis.maxicode.decoder.Decoder;

/**
 * This implementation can detect and decode a MaxiCode in an image.
 */
public final class MaxiCodeReader implements Reader {

  private static final ResultPoint[] NO_POINTS = new ResultPoint[0];
  private static final int MATRIX_WIDTH = 30;
  private static final int MATRIX_HEIGHT = 33;

  private final Decoder decoder = new Decoder();

  Decoder getDecoder() {
    return decoder;
  }

  /**
   * Locates and decodes a MaxiCode in an image.
   *
   * @return a String representing the content encoded by the MaxiCode
   * @throws NotFoundException if a MaxiCode cannot be found
   * @throws FormatException if a MaxiCode cannot be decoded
   * @throws ChecksumException if error correction fails
   */
  @Override
  public Result decode(BinaryBitmap image) throws NotFoundException, ChecksumException, FormatException {
    return decode(image, null);
  }

  @Override
  public Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints)
      throws NotFoundException, ChecksumException, FormatException {
    DecoderResult decoderResult;
    if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
      BitMatrix bits = extractPureBits(image.getBlackMatrix());
      decoderResult = decoder.decode(bits, hints);
    } else {
      throw NotFoundException.getNotFoundInstance();
    }

    ResultPoint[] points = NO_POINTS;
    Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points, BarcodeFormat.MAXICODE);

    String ecLevel = decoderResult.getECLevel();
    if (ecLevel != null) {
      result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
    }
    return result;
  }

  @Override
  public void reset() {
    // do nothing
  }

  /**
   * This method detects a code in a "pure" image -- that is, pure monochrome image
   * which contains only an unrotated, unskewed, image of a code, with some white border
   * around it. This is a specialized method that works exceptionally fast in this special
   * case.
   *
   * @see cy.ac.ucy.paschalis.pdf417.PDF417Reader#extractPureBits(BitMatrix)
   * @see cy.ac.ucy.paschalis.datamatrix.DataMatrixReader#extractPureBits(BitMatrix)
   * @see cy.ac.ucy.paschalis.qrcode.QRCodeReader#extractPureBits(BitMatrix)
   */
  private static BitMatrix extractPureBits(BitMatrix image) throws NotFoundException {
    
    int[] enclosingRectangle = image.getEnclosingRectangle();
    if (enclosingRectangle == null) {
      throw NotFoundException.getNotFoundInstance();
    }
    
    int left = enclosingRectangle[0];
    int top = enclosingRectangle[1];
    int width = enclosingRectangle[2];
    int height = enclosingRectangle[3];

    // Now just read off the bits
    BitMatrix bits = new BitMatrix(MATRIX_WIDTH, MATRIX_HEIGHT);
    for (int y = 0; y < MATRIX_HEIGHT; y++) {
      int iy = top + (y * height + height / 2) / MATRIX_HEIGHT;
      for (int x = 0; x < MATRIX_WIDTH; x++) {
        int ix = left + (x * width + width / 2 + (y & 0x01) *  width / 2) / MATRIX_WIDTH;
        if (image.get(ix, iy)) {
          bits.set(x, y);
        }
      }
    }
    return bits;
  }

}
