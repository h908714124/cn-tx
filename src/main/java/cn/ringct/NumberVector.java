package cn.ringct;

import java.math.BigInteger;
import java.util.List;

final class NumberVector {
  private final List<BigInteger> numbers;

  NumberVector(List<BigInteger> numbers) {
    this.numbers = numbers;
  }

  public List<BigInteger> numbers() {
    return numbers;
  }

  public int size() {
    return numbers.size();
  }

  public BigInteger get(int i) {
    return numbers.get(i);
  }
}
