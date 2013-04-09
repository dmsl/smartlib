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

package cy.ac.ucy.paschalis.oned.rss;

/** Adapted from listings in ISO/IEC 24724 Appendix B and Appendix G. */
public final class RSSUtils {

  private RSSUtils() {}

  static int[] getRSSwidths(int val, int n, int elements, int maxWidth, boolean noNarrow) {
    int[] widths = new int[elements];
    int bar;
    int narrowMask = 0;
    for (bar = 0; bar < elements - 1; bar++) {
      narrowMask |= 1 << bar;
      int elmWidth = 1;
      int subVal;
      while (true) {
        subVal = combins(n - elmWidth - 1, elements - bar - 2);
        if (noNarrow && (narrowMask == 0) &&
            (n - elmWidth - (elements - bar - 1) >= elements - bar - 1)) {
          subVal -= combins(n - elmWidth - (elements - bar), elements - bar - 2);
        }
        if (elements - bar - 1 > 1) {
          int lessVal = 0;
          for (int mxwElement = n - elmWidth - (elements - bar - 2);
               mxwElement > maxWidth;
               mxwElement--) {
            lessVal += combins(n - elmWidth - mxwElement - 1, elements - bar - 3);
          }
          subVal -= lessVal * (elements - 1 - bar);
        } else if (n - elmWidth > maxWidth) {
          subVal--;
        }
        val -= subVal;
        if (val < 0) {
          break;
        }
        elmWidth++;
        narrowMask &= ~(1 << bar);
      }
      val += subVal;
      n -= elmWidth;
      widths[bar] = elmWidth;
    }
    widths[bar] = n;
    return widths;
  }

  public static int getRSSvalue(int[] widths, int maxWidth, boolean noNarrow) {
    int elements = widths.length;
    int n = 0;
    for (int width : widths) {
      n += width;
    }
    int val = 0;
    int narrowMask = 0;
    for (int bar = 0; bar < elements - 1; bar++) {
      int elmWidth;
      for (elmWidth = 1, narrowMask |= 1 << bar;
           elmWidth < widths[bar];
           elmWidth++, narrowMask &= ~(1 << bar)) {
        int subVal = combins(n - elmWidth - 1, elements - bar - 2);
        if (noNarrow && (narrowMask == 0) &&
            (n - elmWidth - (elements - bar - 1) >= elements - bar - 1)) {
          subVal -= combins(n - elmWidth - (elements - bar),
                            elements - bar - 2);
        }
        if (elements - bar - 1 > 1) {
          int lessVal = 0;
          for (int mxwElement = n - elmWidth - (elements - bar - 2);
               mxwElement > maxWidth; mxwElement--) {
            lessVal += combins(n - elmWidth - mxwElement - 1,
                               elements - bar - 3);
          }
          subVal -= lessVal * (elements - 1 - bar);
        } else if (n - elmWidth > maxWidth) {
          subVal--;
        }
        val += subVal;
      }
      n -= elmWidth;
    }
    return val;
  }

  private static int combins(int n, int r) {
    int maxDenom;
    int minDenom;
    if (n - r > r) {
      minDenom = r;
      maxDenom = n - r;
    } else {
      minDenom = n - r;
      maxDenom = r;
    }
    int val = 1;
    int j = 1;
    for (int i = n; i > maxDenom; i--) {
      val *= i;
      if (j <= minDenom) {
        val /= j;
        j++;
      }
    }
    while (j <= minDenom) {
      val /= j;
      j++;
    }
    return val;
  }

  static int[] elements(int[] eDist, int N, int K) {
    int[] widths = new int[eDist.length + 2];
    int twoK = K << 1;
    widths[0] = 1;
    int i;
    int minEven = 10;
    int barSum = 1;
    for (i = 1; i < twoK - 2; i += 2) {
      widths[i] = eDist[i - 1] - widths[i - 1];
      widths[i + 1] = eDist[i] - widths[i];
      barSum += widths[i] + widths[i + 1];
      if (widths[i] < minEven) {
        minEven = widths[i];
      }
    }
    widths[twoK - 1] = N - barSum;
    if (widths[twoK - 1] < minEven) {
      minEven = widths[twoK - 1];
    }
    if (minEven > 1) {
      for (i = 0; i < twoK; i += 2) {
        widths[i] += minEven - 1;
        widths[i + 1] -= minEven - 1;
      }
    }
    return widths;
  }


}
