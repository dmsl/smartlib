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

package cy.ac.ucy.paschalis.oned;

import cy.ac.ucy.paschalis.BarcodeFormat;
import cy.ac.ucy.paschalis.DecodeHintType;
import cy.ac.ucy.paschalis.NotFoundException;
import cy.ac.ucy.paschalis.Reader;
import cy.ac.ucy.paschalis.ReaderException;
import cy.ac.ucy.paschalis.Result;
import cy.ac.ucy.paschalis.common.BitArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * <p>A reader that can read all available UPC/EAN formats. If a caller wants to try to
 * read all such formats, it is most efficient to use this implementation rather than invoke
 * individual readers.</p>
 *
 * @author Sean Owen
 */
public final class MultiFormatUPCEANReader extends OneDReader {

  private final UPCEANReader[] readers;

  public MultiFormatUPCEANReader(Map<DecodeHintType,?> hints) {
    Collection<BarcodeFormat> possibleFormats = hints == null ? null :
        (Collection<BarcodeFormat>) hints.get(DecodeHintType.POSSIBLE_FORMATS);
    Collection<UPCEANReader> readers = new ArrayList<UPCEANReader>();
    if (possibleFormats != null) {
      if (possibleFormats.contains(BarcodeFormat.EAN_13)) {
        readers.add(new EAN13Reader());
      } else if (possibleFormats.contains(BarcodeFormat.UPC_A)) {
        readers.add(new UPCAReader());
      }
      if (possibleFormats.contains(BarcodeFormat.EAN_8)) {
        readers.add(new EAN8Reader());
      }
      if (possibleFormats.contains(BarcodeFormat.UPC_E)) {
        readers.add(new UPCEReader());
      }
    }
    if (readers.isEmpty()) {
      readers.add(new EAN13Reader());
      // UPC-A is covered by EAN-13
      readers.add(new EAN8Reader());
      readers.add(new UPCEReader());
    }
    this.readers = readers.toArray(new UPCEANReader[readers.size()]);
  }

  @Override
  public Result decodeRow(int rowNumber,
                          BitArray row,
                          Map<DecodeHintType,?> hints) throws NotFoundException {
    // Compute this location once and reuse it on multiple implementations
    int[] startGuardPattern = UPCEANReader.findStartGuardPattern(row);
    for (UPCEANReader reader : readers) {
      Result result;
      try {
        result = reader.decodeRow(rowNumber, row, startGuardPattern, hints);
      } catch (ReaderException re) {
        continue;
      }
      // Special case: a 12-digit code encoded in UPC-A is identical to a "0"
      // followed by those 12 digits encoded as EAN-13. Each will recognize such a code,
      // UPC-A as a 12-digit string and EAN-13 as a 13-digit string starting with "0".
      // Individually these are correct and their readers will both read such a code
      // and correctly call it EAN-13, or UPC-A, respectively.
      //
      // In this case, if we've been looking for both types, we'd like to call it
      // a UPC-A code. But for efficiency we only run the EAN-13 decoder to also read
      // UPC-A. So we special case it here, and convert an EAN-13 result to a UPC-A
      // result if appropriate.
      //
      // But, don't return UPC-A if UPC-A was not a requested format!
      boolean ean13MayBeUPCA =
          result.getBarcodeFormat() == BarcodeFormat.EAN_13 &&
              result.getText().charAt(0) == '0';
      Collection<BarcodeFormat> possibleFormats =
          hints == null ? null : (Collection<BarcodeFormat>) hints.get(DecodeHintType.POSSIBLE_FORMATS);
      boolean canReturnUPCA = possibleFormats == null || possibleFormats.contains(BarcodeFormat.UPC_A);

      if (ean13MayBeUPCA && canReturnUPCA) {
        // Transfer the metdata across
        Result resultUPCA = new Result(result.getText().substring(1),
                                       result.getRawBytes(),
                                       result.getResultPoints(),
                                       BarcodeFormat.UPC_A);
        resultUPCA.putAllMetadata(result.getResultMetadata());
        return resultUPCA;
      }
      return result;
    }

    throw NotFoundException.getNotFoundInstance();
  }

  @Override
  public void reset() {
    for (Reader reader : readers) {
      reader.reset();
    }
  }

}
