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

package cy.ac.ucy.paschalis.common;

import cy.ac.ucy.paschalis.NotFoundException;

/**
 * Implementations of this class can, given locations of finder patterns for a QR code in an
 * image, sample the right points in the image to reconstruct the QR code, accounting for
 * perspective distortion. It is abstracted since it is relatively expensive and should be allowed
 * to take advantage of platform-specific optimized implementations, like Sun's Java Advanced
 * Imaging library, but which may not be available in other environments such as J2ME, and vice
 * versa.
 *
 * The implementation used can be controlled by calling {@link #setGridSampler(GridSampler)}
 * with an instance of a class which implements this interface.
 *
 * @author Sean Owen
 */
public abstract class GridSampler {

  private static GridSampler gridSampler = new DefaultGridSampler();

  /**
   * Sets the implementation of GridSampler used by the library. One global
   * instance is stored, which may sound problematic. But, the implementation provided
   * ought to be appropriate for the entire platform, and all uses of this library
   * in the whole lifetime of the JVM. For instance, an Android activity can swap in
   * an implementation that takes advantage of native platform libraries.
   * 
   * @param newGridSampler The platform-specific object to install.
   */
  public static void setGridSampler(GridSampler newGridSampler) {
    gridSampler = newGridSampler;
  }

  /**
   * @return the current implementation of GridSampler
   */
  public static GridSampler getInstance() {
    return gridSampler;
  }

  /**
   * Samples an image for a rectangular matrix of bits of the given dimension.
   * @param image image to sample
   * @param dimensionX width of {@link BitMatrix} to sample from image
   * @param dimensionY height of {@link BitMatrix} to sample from image
   * @return {@link BitMatrix} representing a grid of points sampled from the image within a region
   *   defined by the "from" parameters
   * @throws NotFoundException if image can't be sampled, for example, if the transformation defined
   *   by the given points is invalid or results in sampling outside the image boundaries
   */
  public abstract BitMatrix sampleGrid(BitMatrix image,
                                       int dimensionX,
                                       int dimensionY,
                                       float p1ToX, float p1ToY,
                                       float p2ToX, float p2ToY,
                                       float p3ToX, float p3ToY,
                                       float p4ToX, float p4ToY,
                                       float p1FromX, float p1FromY,
                                       float p2FromX, float p2FromY,
                                       float p3FromX, float p3FromY,
                                       float p4FromX, float p4FromY) throws NotFoundException;
  
  public abstract BitMatrix sampleGrid(BitMatrix image,
                                       int dimensionX,
                                       int dimensionY,
                                       PerspectiveTransform transform) throws NotFoundException;

  /**
   * <p>Checks a set of points that have been transformed to sample points on an image against
   * the image's dimensions to see if the point are even within the image.</p>
   *
   * <p>This method will actually "nudge" the endpoints back onto the image if they are found to be
   * barely (less than 1 pixel) off the image. This accounts for imperfect detection of finder
   * patterns in an image where the QR Code runs all the way to the image border.</p>
   *
   * <p>For efficiency, the method will check points from either end of the line until one is found
   * to be within the image. Because the set of points are assumed to be linear, this is valid.</p>
   *
   * @param image image into which the points should map
   * @param points actual points in x1,y1,...,xn,yn form
   * @throws NotFoundException if an endpoint is lies outside the image boundaries
   */
  protected static void checkAndNudgePoints(BitMatrix image,
                                            float[] points) throws NotFoundException {
    int width = image.getWidth();
    int height = image.getHeight();
    // Check and nudge points from start until we see some that are OK:
    boolean nudged = true;
    for (int offset = 0; offset < points.length && nudged; offset += 2) {
      int x = (int) points[offset];
      int y = (int) points[offset + 1];
      if (x < -1 || x > width || y < -1 || y > height) {
        throw NotFoundException.getNotFoundInstance();
      }
      nudged = false;
      if (x == -1) {
        points[offset] = 0.0f;
        nudged = true;
      } else if (x == width) {
        points[offset] = width - 1;
        nudged = true;
      }
      if (y == -1) {
        points[offset + 1] = 0.0f;
        nudged = true;
      } else if (y == height) {
        points[offset + 1] = height - 1;
        nudged = true;
      }
    }
    // Check and nudge points from end:
    nudged = true;
    for (int offset = points.length - 2; offset >= 0 && nudged; offset -= 2) {
      int x = (int) points[offset];
      int y = (int) points[offset + 1];
      if (x < -1 || x > width || y < -1 || y > height) {
        throw NotFoundException.getNotFoundInstance();
      }
      nudged = false;
      if (x == -1) {
        points[offset] = 0.0f;
        nudged = true;
      } else if (x == width) {
        points[offset] = width - 1;
        nudged = true;
      }
      if (y == -1) {
        points[offset + 1] = 0.0f;
        nudged = true;
      } else if (y == height) {
        points[offset + 1] = height - 1;
        nudged = true;
      }
    }
  }

}
