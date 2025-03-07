package org.example;

public class AuxiliaryMethodsFixed {

  /**
   * Given a signed 32-bit integer x, return x with its digits reversed.
   * If reversing x causes the value to go outside the signed 32-bit integer range [-2^31, 2^31 - 1],
   * then return 0.
   *
   * @param x the input number
   * @return reverse of x
   */
  public static int reverse(int x) {
    long rev = 0; // use long to detect overflow
    while (x != 0) {
      int pop = x % 10;
      x /= 10;
      rev = rev * 10 + pop;
      // Check for overflow
      if (rev > Integer.MAX_VALUE || rev < Integer.MIN_VALUE) {
        return 0;
      }
    }
    return (int) rev;
  }

  /**
   * Given two strings s and t, determine if they are isomorphic.
   * Two strings s and t are isomorphic if the characters in s can be replaced
   * to get t. All occurrences of a character must be replaced with another
   * character while preserving the order of characters. No two characters may
   * map to the same character, but a character may map to itself.
   *
   * @param s the first string
   * @param t the second string
   * @return true if s and t are isomorphic, false otherwise.
   */
  public static boolean isIsomorphic(String s, String t) {
    // Fix: Check length first
    if (s.length() != t.length()) {
      return false;
    }

    int[] mappingDictStoT = new int[256];
    int[] mappingDictTtoS = new int[256];

    for (int i = 0; i < 256; i++) {
      mappingDictStoT[i] = -1;
      mappingDictTtoS[i] = -1;
    }

    for (int i = 0; i < s.length(); i++) {
      char c1 = s.charAt(i);
      char c2 = t.charAt(i);

      // case 1: if no mapping exists
      if (mappingDictStoT[c1] == -1 && mappingDictTtoS[c2] == -1) {
        mappingDictStoT[c1] = c2;
        mappingDictTtoS[c2] = c1;
      }
      // case 2: mismatch in existing mapping
      else if (!(mappingDictStoT[c1] == c2 && mappingDictTtoS[c2] == c1)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Count odd or positive elements.
   *
   * @param x array to search
   * @return count of odd/positive values in x
   * @throws NullPointerException if x is null
   */
  public static int oddOrPos(int[] x) {
    int count = 0;
    for (int i = 0; i < x.length; i++) {
      // fix: check for odd by (x[i] % 2 != 0)
      // and also check positive.
      if ( (x[i] % 2 != 0) || (x[i] > 0) ) {
        count++;
      }
    }
    return count;
  }
}
