package cn.ringct;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.math.ec.ECPoint;

public class SaltyVector {

  private final List<SaltyPoint> points;

  private SaltyVector(List<SaltyPoint> points) {
    this.points = points;
  }

  static SaltyVector create(PointVector P, NumberVector s) {
    if (P.size() != s.size()) {
      throw new IllegalArgumentException();
    }
    List<SaltyPoint> result = new ArrayList<>(P.size());
    for (int i = 0; i < s.size(); i++) {
      result.add(SaltyPoint.create(P.get(i), s.get(i)));
    }
    return new SaltyVector(result);
  }

  public List<SaltyPoint> columns() {
    return points;
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
