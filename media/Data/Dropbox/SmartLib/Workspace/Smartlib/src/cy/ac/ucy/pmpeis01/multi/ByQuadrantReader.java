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

package cy.ac.ucy.pmpeis01.multi;

import cy.ac.ucy.pmpeis01.BinaryBitmap;
import cy.ac.ucy.pmpeis01.ChecksumException;
import cy.ac.ucy.pmpeis01.DecodeHintType;
import cy.ac.ucy.pmpeis01.FormatException;
import cy.ac.ucy.pmpeis01.NotFoundException;
import cy.ac.ucy.pmpeis01.Reader;
import cy.ac.ucy.pmpeis01.Result;

import java.util.Map;

/**
 * This class attempts to decode a barcode from an image, not by scanning the whole image,
 * but by scanning subsets of the image. This is important when there may be multiple barcodes in
 * an image, and detecting a barcode may find parts of multiple barcode and fail to decode
 * (e.g. QR Codes). Instead this scans the four quadrants of the image -- and also the center
 * 'quadrant' to cover the case where a barcode is found in the center.
 *
 * @see GenericMultipleBarcodeReader
 */
public final class ByQuadrantReader implements Reader {

  private final Reader delegate;

  public ByQuadrantReader(Reader delegate) {
    this.delegate = delegate;
  }

  @Override
  public Result decode(BinaryBitmap image)
      throws NotFoundException, ChecksumException, FormatException {
    return decode(image, null);
  }

  @Override
  public Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints)
      throws NotFoundException, ChecksumException, FormatException {

    int width = image.getWidth();
    int height = image.getHeight();
    int halfWidth = width / 2;
    int halfHeight = height / 2;

    BinaryBitmap topLeft = image.crop(0, 0, halfWidth, halfHeight);
    try {
      return delegate.decode(topLeft, hints);
    } catch (NotFoundException re) {
      // continue
    }

    BinaryBitmap topRight = image.crop(halfWidth, 0, halfWidth, halfHeight);
    try {
      return delegate.decode(topRight, hints);
    } catch (NotFoundException re) {
      // continue
    }

    BinaryBitmap bottomLeft = image.crop(0, halfHeight, halfWidth, halfHeight);
    try {
      return delegate.decode(bottomLeft, hints);
    } catch (NotFoundException re) {
      // continue
    }

    BinaryBitmap bottomRight = image.crop(halfWidth, halfHeight, halfWidth, halfHeight);
    try {
      return delegate.decode(bottomRight, hints);
    } catch (NotFoundException re) {
      // continue
    }

    int quarterWidth = halfWidth / 2;
    int quarterHeight = halfHeight / 2;
    BinaryBitmap center = image.crop(quarterWidth, quarterHeight, halfWidth, halfHeight);
    return delegate.decode(center, hints);
  }

  @Override
  public void reset() {
    delegate.reset();
  }

}
