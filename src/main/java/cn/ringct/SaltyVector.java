package cn.ringct;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.math.ec.ECPoint;

public class SaltyVector {

  private final List<SaltyPoint> points;

  SaltyVector(List<SaltyPoint> points) {
    this.points = points;
  }

  static SaltyVector create(PointVector P, NumberVector s) {
    if (P.length() != s.length()) {
      throw new IllegalArgumentException();
    }
    List<SaltyPoint> result = new ArrayList<>(P.length());
    for (int i = 0; i < s.length(); i++) {
      result.add(SaltyPoint.create(P.get(i), s.get(i)));
    }
    return new SaltyVector(result);
  }

  public NumberVector s() {
    List<BigInteger> result = new ArrayList<>(points.size());
    for (SaltyPoint point : points) {
      result.add(point.s());
    }
    return new NumberVector(result);
  }

  public PointVector P() {
    List<ECPoint> result = new ArrayList<>(points.size());
    for (SaltyPoint point : points) {
      result.add(point.P());
    }
    return new PointVector(result);
  }
}
