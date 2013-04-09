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

package cy.ac.ucy.paschalis;

/**
 * *
 * Enumerates barcode formats known to this package. Please keep alphabetized.
 *
 * @author Sean Owen
 */
public enum BarcodeFormat {

  /** Aztec 2D barcode format. */
  AZTEC,

  /** CODABAR 1D format. */
  CODABAR,

  /** Code 39 1D format. */
  CODE_39,

  /** Code 93 1D format. */
  CODE_93,

  /** Code 128 1D format. */
  CODE_128,

  /** Data Matrix 2D barcode format. */
  DATA_MATRIX,

  /** EAN-8 1D format. */
  EAN_8,

  /** EAN-13 1D format. */
  EAN_13,

  /** ITF (Interleaved Two of Five) 1D format. */
  ITF,

  /** MaxiCode 2D barcode format. */
  MAXICODE,

  /** PDF417 format. */
  PDF_417,

  /** QR Code 2D barcode format. */
  QR_CODE,

  /** RSS 14 */
  RSS_14,

  /** RSS EXPANDED */
  RSS_EXPANDED,

  /** UPC-A 1D format. */
  UPC_A,

  /** UPC-E 1D format. */
  UPC_E,

  /** UPC/EAN extension format. Not a stand-alone format. */
  UPC_EAN_EXTENSION

}
