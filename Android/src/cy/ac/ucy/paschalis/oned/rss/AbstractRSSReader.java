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

import cy.ac.ucy.paschalis.NotFoundException;
import cy.ac.ucy.paschalis.oned.OneDReader;

public abstract class AbstractRSSReader extends OneDReader {

  private static final int MAX_AVG_VARIANCE = (int) (PATTERN_MATCH_RESULT_SCALE_FACTOR * 0.2f);
  private static final int MAX_INDIVIDUAL_VARIANCE = (int) (PATTERN_MATCH_RESULT_SCALE_FACTOR * 0.4f);

  private static final float MIN_FINDER_PATTERN_RATIO = 9.5f / 12.0f;
  private static final float MAX_FINDER_PATTERN_RATIO = 12.5f / 14.0f;

  private final int[] decodeFinderCounters;
  private final int[] dataCharacterCounters;
  private final float[] oddRoundingErrors;
  private final float[] evenRoundingErrors;
  private final int[] oddCounts;
  private final int[] evenCounts;

  protected AbstractRSSReader(){
    decodeFinderCounters = new int[4];
    dataCharacterCounters = new int[8];
    oddRoundingErrors = new float[4];
    evenRoundingErrors = new float[4];
    oddCounts = new int[dataCharacterCounters.length / 2];
    evenCounts = new int[dataCharacterCounters.length / 2];
  }

  protected final int[] getDecodeFinderCounters() {
    return decodeFinderCounters;
  }

  protected final int[] getDataCharacterCounters() {
    return dataCharacterCounters;
  }

  protected final float[] getOddRoundingErrors() {
    return oddRoundingErrors;
  }

  protected final float[] getEvenRoundingErrors() {
    return evenRoundingErrors;
  }

  protected final int[] getOddCounts() {
    return oddCounts;
  }

  protected final int[] getEvenCounts() {
    return evenCounts;
  }

  protected static int parseFinderValue(int[] counters,
                                        int[][] finderPatterns) throws NotFoundException {
    for (int value = 0; value < finderPatterns.length; value++) {
      if (patternMatchVariance(counters, finderPatterns[value], MAX_INDIVIDUAL_VARIANCE) <
          MAX_AVG_VARIANCE) {
        return value;
      }
    }
    throw NotFoundException.getNotFoundInstance();
  }

  protected static int count(int[] array) {
    int count = 0;
    for (int a : array) {
      count += a;
    }
    return count;
  }

  protected static void increment(int[] array, float[] errors) {
    int index = 0;
    float biggestError = errors[0];
    for (int i = 1; i < array.length; i++) {
      if (errors[i] > biggestError) {
        biggestError = errors[i];
        index = i;
      }
    }
    array[index]++;
  }

  protected static void decrement(int[] array, float[] errors) {
    int index = 0;
    float biggestError = errors[0];
    for (int i = 1; i < array.length; i++) {
      if (errors[i] < biggestError) {
        biggestError = errors[i];
        index = i;
      }
    }
    array[index]--;
  }

  protected static boolean isFinderPattern(int[] counters) {
    int firstTwoSum = counters[0] + counters[1];
    int sum = firstTwoSum + counters[2] + counters[3];
    float ratio = (float) firstTwoSum / (float) sum;
    if (ratio >= MIN_FINDER_PATTERN_RATIO && ratio <= MAX_FINDER_PATTERN_RATIO) {
      // passes ratio test in spec, but see if the counts are unreasonable
      int minCounter = Integer.MAX_VALUE;
      int maxCounter = Integer.MIN_VALUE;
      for (int counter : counters) {
        if (counter > maxCounter) {
          maxCounter = counter;
        }
        if (counter < minCounter) {
          minCounter = counter;
        }
      }
      return maxCounter < 10 * minCounter;
    }
    return false;
  }
}
