package org.example;

import net.jqwik.api.*;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.Assertions;


public class AuxiliaryMethodsPBT {

  /*
   * ================ Tests for reverse(int) ============
   */

  /**
   * Property 1 : Reversing a number twice should
   * return the original (unless the reversed is 0 due to overflow). Given that the number doesn't end with a zero so we don't need to deal with leading 0s
   */
  @Property
  void reverseTwiceShouldReturnOriginalUnlessOverflow(@ForAll("anyIntNotEndingWithZero") int x) {
    int reversed = AuxiliaryMethods.reverse(x);
    // If reversed == 0, it might be due to overflow, so skip the check.
    System.out.println("x = " + x);
    System.out.println("reversed = " + reversed);
    if (reversed != 0) {
      int reversedAgain = AuxiliaryMethods.reverse(reversed);
      Assertions.assertEquals(
              x,
              reversedAgain,
              () -> "Reversing twice failed for " + x
      );
    }
  }


  /**
   * Property 2 (R2): If reverse(x) != 0, it must be within the valid int range.
   * The method returns 0 if overflow happens, so non-zero results must be valid.
   */
  @Property
  void reversedNumberShouldBeWithinRange(@ForAll("anyInt") int x) {
    int reversed = AuxiliaryMethods.reverse(x);
    // Non-zero => the method should not have overflowed
    if (reversed != 0) {
      Assertions.assertTrue(
              reversed >= Integer.MIN_VALUE && reversed <= Integer.MAX_VALUE,
              () -> "Reversed value " + reversed + " is out of int range!"
      );
    }
  }

  /**
   * Property 3 (R3): If the original number contains trailing zeros,
   * the reversed number should not have leading zeros (unless it's 0 due to overflow).
   */
  @Property
  void reversingNumberWithTrailingZerosDropsLeadingZeros(@ForAll("intWithPossibleTrailingZeros") int numWithZeros) {
    int reversed = AuxiliaryMethods.reverse(numWithZeros);
    if (reversed != 0) {
      String reversedStr = String.valueOf(reversed);
      // If reversed is non-zero, it should not start with '0'
      Assertions.assertFalse(
              reversedStr.startsWith("0"),
              () -> String.format("Reversed string '%s' starts with 0 for input %d", reversedStr, numWithZeros)
      );
    }
  }

  //More property testing to find errors
  /*
  Possible Faults:
  The possible fault is that the loop in the reverse(int x) uses while (x > 0). So, if x is negative (e.g., -123), the
  loop never executes. The method simply returns 0 for all negative inputs—completely ignoring the requirement to reverse
  negative numbers (and possibly return negative reversals).
   */
  @Property
  void negativeNumbersShouldBeReversed(@ForAll("smallNegativeInt") int x) {
    // We pick "small" negative numbers so that reversing will NOT overflow.
    // e.g. -123 -> -321 is safely within int range.
    // We'll compute the expected reversed "correctly" and compare it.
    int expected = reverseManually(x);
    int actual = AuxiliaryMethods.reverse(x);

    Assertions.assertEquals(
            expected,
            actual,
            () -> String.format("Reversing negative %d failed. Expected %d, got %d", x, expected, actual)
    );
  }

  private int reverseManually(int x) {
    // A simple correct approach for demonstration
    int sign = (x < 0) ? -1 : 1;
    x = Math.abs(x);
    long reversed = 0;  // use long to check for overflow
    while (x != 0) {
      reversed = reversed * 10 + (x % 10);
      x /= 10;
    }
    reversed *= sign;
    // For the real problem, if reversed is outside int range, return 0.
    if (reversed > Integer.MAX_VALUE || reversed < Integer.MIN_VALUE) {
      return 0;
    }
    return (int) reversed;
  }




  /*
   * ============ Tests for isIsomorphic(String, String) =
   */

  /**
   * Property 1: Strings of different lengths are never isomorphic.
   */
  /*
  Possible Faults:
  The possible fault is that the code loops through the both the string by the length of one string, being inconsiderate
   of the length of the other string. This results in the index of the second string wil go out of bound.
   */
  @Property
  void stringsOfDifferentLengthsAreNotIsomorphic(@ForAll("twoStringsOfDifferentLengths") TwoStrings pair) {
    String s = pair.s();
    String t = pair.t();

    System.out.println("s: " + s);
    System.out.println("t " + t);

    boolean result = AuxiliaryMethods.isIsomorphic(s, t);
    Assertions.assertFalse(
            result,
            () -> String.format(
                    "Expected different-length strings not to be isomorphic: '%s'(%d), '%s'(%d)",
                    s, s.length(), t, t.length()
            )
    );
  }

  /**
   * Property I2: isIsomorphic(s, t) should be the same as isIsomorphic(t, s) (symmetry).
   */
  @Property
  void isIsomorphicShouldBeSymmetric(@ForAll("twoStringsOfSameLength") TwoStrings pair) {
    String s = pair.s();
    String t = pair.t();

    boolean forward = AuxiliaryMethods.isIsomorphic(s, t);
    boolean backward = AuxiliaryMethods.isIsomorphic(t, s);
    Assertions.assertEquals(
            forward,
            backward,
            () -> String.format("Symmetry failed for '%s' and '%s'", s, t)
    );
  }

  /**
   * Property I3: A string is always isomorphic to itself.
   */
  @Property
  void stringIsIsomorphicToItself(@ForAll("randomString") String s) {
    Assertions.assertTrue(
            AuxiliaryMethods.isIsomorphic(s, s),
            () -> "A string should be isomorphic to itself but wasn't: " + s
    );
  }


  /*
   * ============== Tests for oddOrPos(int[]) ===========
   */

  /**
   * Property O1: Negative even numbers should not be counted.
   */
  @Property
  void negativeEvenNumbersAreNotCounted(@ForAll("negativeEven") int n) {
    int[] arr = {n};
    int count = AuxiliaryMethods.oddOrPos(arr);
    Assertions.assertEquals(
            0,
            count,
            () -> "Expected 0, but got " + count + " for negative even " + n
    );
  }

  /**
   * Property O2: Any positive integer should be counted.
   */
  @Property
  void positiveIntegerIsCounted(@ForAll("positiveInteger") int n) {
    int[] arr = {n};
    int count = AuxiliaryMethods.oddOrPos(arr);
    Assertions.assertEquals(
            1,
            count,
            () -> "Expected 1, but got " + count + " for positive " + n
    );
  }


  //More property testing to find errors



  /**
   * Property O3: Any odd integer (positive or negative) should be counted.
   */
  /*
  Possible Faults:
  The possible fault is that the code doesn’t count n whenever n is a negative odd. For example, if n = -3, the correct
  count is 1, but the code returns 0. That reveals the bug that x[i] % 2 == 1 does not handle negative odd values.
   */
  @Property
  void oddIntegerIsCounted(@ForAll("anyOdd") int n) {
    int[] arr = {n};
    int count = AuxiliaryMethods.oddOrPos(arr);
    Assertions.assertEquals(
            1,
            count,
            () -> "Expected 1, but got " + count + " for odd " + n
    );
  }

  //More property testing to find errors




  /**
   * Generates any int (the full 32-bit range) that doesn't end in a 0
   */
  @Provide
  Arbitrary<Integer> anyIntNotEndingWithZero() {
    return Arbitraries.integers()
            .filter(i -> i % 10 != 0);
  }


  /**
   * Generates any int (the full 32-bit range).
   */
  @Provide
  Arbitrary<Integer> anyInt() {
    return Arbitraries.integers();
  }

  /**
   * Generates integers that may have trailing zeros
   * by multiplying a base integer with a factor in {1, 10, 100, 1000}.
   */
  @Provide
  Arbitrary<Integer> intWithPossibleTrailingZeros() {
    Arbitrary<Integer> base = Arbitraries.integers().between(-1_000_000, 1_000_000);
    Arbitrary<Integer> factor = Arbitraries.of(1, 10, 100, 1000);
    return Combinators.combine(base, factor)
            .as((b, f) -> b * f);
  }


  /**
   * Provide small negative integers so that reversing them
   * won't overflow. For instance, between -9999 and -1.
   */
  @Provide
  Arbitrary<Integer> smallNegativeInt() {
    return Arbitraries.integers()
            .between(-9999, -1);
  }

  /**
   * Generates random strings (ASCII range) with length up to 10.
   */
  @Provide
  Arbitrary<String> randomString() {
    return Arbitraries.strings()
            .ascii()
            .ofMinLength(0)
            .ofMaxLength(10);
  }

  /**
   * Generates pairs of strings of DIFFERENT lengths.
   */
  @Provide
  Arbitrary<TwoStrings> twoStringsOfDifferentLengths() {
    // Create any two strings (each up to length 10)
    // and filter out cases where they have the same length.
    Arbitrary<String> s = randomString();
    Arbitrary<String> t = randomString();
    return Combinators.combine(s, t)
            .as(TwoStrings::new)
            .filter(pair -> pair.s().length() != pair.t().length());
  }

  /**
   * Generates pairs of strings of the SAME length.
   */
  @Provide
  Arbitrary<TwoStrings> twoStringsOfSameLength() {
    return Arbitraries.integers().between(0, 10).flatMap(length -> {
      Arbitrary<String> s = Arbitraries.strings().ascii().ofLength(length);
      Arbitrary<String> t = Arbitraries.strings().ascii().ofLength(length);
      return Combinators.combine(s, t).as(TwoStrings::new);
    });
  }

  /**
   * Generates negative even integers.
   */
  @Provide
  Arbitrary<Integer> negativeEven() {
    return Arbitraries.integers()
            .filter(i -> i < 0 && i % 2 == 0);
  }

  /**
   * Generates positive integers (greater than 0).
   */
  @Provide
  Arbitrary<Integer> positiveInteger() {
    return Arbitraries.integers()
            .filter(i -> i > 0);
  }

  /**
   * Generates any odd integer, positive or negative (excluding zero).
   */
  @Provide
  Arbitrary<Integer> anyOdd() {
    return Arbitraries.integers()
            .filter(i -> i % 2 != 0);
  }

  /*
   * Simple record to hold two strings.
   */
  record TwoStrings(String s, String t) {}

}
