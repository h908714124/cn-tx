package cn.ringct;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.math.ec.ECPoint;

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

  public PointVector multiply(ECPoint A) {
    List<ECPoint> result = new ArrayList<>(numbers.size());
    for (BigInteger number : numbers) {
      result.add(A.multiply(number));
    }
    return new PointVector(result);
  }

  public NumberVector multiply(BigInteger A) {
    List<BigInteger> result = new ArrayList<>(numbers.size());
    for (BigInteger number : numbers) {
      result.add(A.multiply(number));
    }
    return new NumberVector(result);

  }

  public NumberVector subtract(NumberVector v) {
    if (v.size() != numbers.size()) {
      throw new IllegalArgumentException();
    }
    List<BigInteger> result = new ArrayList<>(v.size());
    for (int i = 0; i < v.size(); i++) {
      result.add(numbers.get(i).subtract(v.get(i)));
    }
    return new NumberVector(result);
  }

  public NumberVector mod(BigInteger n) {
    List<BigInteger> result = new ArrayList<>(numbers.size());
    for (BigInteger number : numbers) {
      result.add(number.mod(n));
    }
    return new NumberVector(result);
  }
}
