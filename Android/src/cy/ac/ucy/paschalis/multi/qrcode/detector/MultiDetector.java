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

package cy.ac.ucy.paschalis.multi.qrcode.detector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cy.ac.ucy.paschalis.DecodeHintType;
import cy.ac.ucy.paschalis.NotFoundException;
import cy.ac.ucy.paschalis.ReaderException;
import cy.ac.ucy.paschalis.ResultPointCallback;
import cy.ac.ucy.paschalis.common.BitMatrix;
import cy.ac.ucy.paschalis.common.DetectorResult;
import cy.ac.ucy.paschalis.qrcode.detector.Detector;
import cy.ac.ucy.paschalis.qrcode.detector.FinderPatternInfo;

/**
 * <p>Encapsulates logic that can detect one or more QR Codes in an image, even if the QR Code
 * is rotated or skewed, or partially obscured.</p>
 *
 * @author Sean Owen
 * @author Hannes Erven
 */
public final class MultiDetector extends Detector {

  private static final DetectorResult[] EMPTY_DETECTOR_RESULTS = new DetectorResult[0];

  public MultiDetector(BitMatrix image) {
    super(image);
  }

  public DetectorResult[] detectMulti(Map<DecodeHintType,?> hints) throws NotFoundException {
    BitMatrix image = getImage();
    ResultPointCallback resultPointCallback =
        hints == null ? null : (ResultPointCallback) hints.get(DecodeHintType.NEED_RESULT_POINT_CALLBACK);
    MultiFinderPatternFinder finder = new MultiFinderPatternFinder(image, resultPointCallback);
    FinderPatternInfo[] infos = finder.findMulti(hints);

    if (infos.length == 0) {
      throw NotFoundException.getNotFoundInstance();
    }

    List<DetectorResult> result = new ArrayList<DetectorResult>();
    for (FinderPatternInfo info : infos) {
      try {
        result.add(processFinderPatternInfo(info));
      } catch (ReaderException e) {
        // ignore
      }
    }
    if (result.isEmpty()) {
      return EMPTY_DETECTOR_RESULTS;
    } else {
      return result.toArray(new DetectorResult[result.size()]);
    }
  }

}
