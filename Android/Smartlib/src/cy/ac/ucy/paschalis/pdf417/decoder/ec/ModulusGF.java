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

package cy.ac.ucy.paschalis.pdf417.decoder.ec;

/**
 * <p>A field based on powers of a generator integer, modulo some modulus.</p>
 *
 * @author Sean Owen
 * @see cy.ac.ucy.paschalis.common.reedsolomon.GenericGF
 */
public final class ModulusGF {

  public static final ModulusGF PDF417_GF = new ModulusGF(929, 3);

  private final int[] expTable;
  private final int[] logTable;
  private final ModulusPoly zero;
  private final ModulusPoly one;
  private final int modulus;

  public ModulusGF(int modulus, int generator) {
    this.modulus = modulus;
    expTable = new int[modulus];
    logTable = new int[modulus];
    int x = 1;
    for (int i = 0; i < modulus; i++) {
      expTable[i] = x;
      x = (x * generator) % modulus;
    }
    for (int i = 0; i < modulus-1; i++) {
      logTable[expTable[i]] = i;
    }
    // logTable[0] == 0 but this should never be used
    zero = new ModulusPoly(this, new int[]{0});
    one = new ModulusPoly(this, new int[]{1});
  }


  ModulusPoly getZero() {
    return zero;
  }

  ModulusPoly getOne() {
    return one;
  }

  ModulusPoly buildMonomial(int degree, int coefficient) {
    if (degree < 0) {
      throw new IllegalArgumentException();
    }
    if (coefficient == 0) {
      return zero;
    }
    int[] coefficients = new int[degree + 1];
    coefficients[0] = coefficient;
    return new ModulusPoly(this, coefficients);
  }

  int add(int a, int b) {
    return (a + b) % modulus;
  }

  int subtract(int a, int b) {
    return (modulus + a - b) % modulus;
  }

  int exp(int a) {
    return expTable[a];
  }

  int log(int a) {
    if (a == 0) {
      throw new IllegalArgumentException();
    }
    return logTable[a];
  }

  int inverse(int a) {
    if (a == 0) {
      throw new ArithmeticException();
    }
    return expTable[modulus - logTable[a] - 1];
  }

  int multiply(int a, int b) {
    if (a == 0 || b == 0) {
      return 0;
    }
    return expTable[(logTable[a] + logTable[b]) % (modulus - 1)];
  }

  int getSize() {
    return modulus;
  }
  
}
