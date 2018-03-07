package cn.ringct;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.math.ec.ECPoint;

class SaltyColumn {

  private final List<SaltyPoint> points;

  SaltyColumn(List<SaltyPoint> points) {
    this.points = points;
  }

  static SaltyColumn create(PointColumn P, NumberColumn s) {
    assert P.height() == s.height();
    List<SaltyPoint> result = new ArrayList<>(P.height());
    for (int i = 0; i < s.height(); i++) {
      result.add(SaltyPoint.create(P.get(i), s.get(i)));
    }
    return new SaltyColumn(result);
  }

  NumberColumn s() {
    List<BigInteger> result = new ArrayList<>(points.size());
    for (SaltyPoint point : points) {
      result.add(point.s());
    }
    return new NumberColumn(result);
  }

  PointColumn P() {
    List<ECPoint> result = new ArrayList<>(points.size());
    for (SaltyPoint point : points) {
      result.add(point.P());
    }
    return new PointColumn(result);
  }
}
