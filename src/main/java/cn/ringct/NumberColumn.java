package cn.ringct;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.math.ec.ECPoint;

final class NumberColumn {

  private final List<BigInteger> numbers;

  NumberColumn(List<BigInteger> numbers) {
    this.numbers = numbers;
  }

  public int height() {
    return numbers.size();
  }

  public BigInteger get(int i) {
    return numbers.get(i);
  }

  public PointColumn multiply(ECPoint A) {
    List<ECPoint> result = new ArrayList<>(numbers.size());
    for (BigInteger number : numbers) {
      result.add(A.multiply(number));
    }
    return new PointColumn(result);
  }

  public NumberColumn multiply(BigInteger A) {
    List<BigInteger> result = new ArrayList<>(numbers.size());
    for (BigInteger number : numbers) {
      result.add(A.multiply(number));
    }
    return new NumberColumn(result);

  }

  public NumberColumn subtract(NumberColumn v) {
    assert v.height() == numbers.size();
    List<BigInteger> result = new ArrayList<>(v.height());
    for (int i = 0; i < v.height(); i++) {
      result.add(numbers.get(i).subtract(v.get(i)));
    }
    return new NumberColumn(result);
  }

  public NumberColumn mod(BigInteger n) {
    List<BigInteger> result = new ArrayList<>(numbers.size());
    for (BigInteger number : numbers) {
      result.add(number.mod(n));
    }
    return new NumberColumn(result);
  }
}
