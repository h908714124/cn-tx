package cn.ringct;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.math.ec.ECPoint;

public final class PointColumn {

  private final List<ECPoint> points;

  public PointColumn(List<ECPoint> points) {
    this.points = points;
  }

  public List<ECPoint> points() {
    return points;
  }

  public int height() {
    return points.size();
  }

  public PointColumn multiply(NumberColumn x) {
    assert x.height() == points.size();
    List<ECPoint> result = new ArrayList<>(points.size());
    for (int i = 0; i < points.size(); i++) {
      result.add(points.get(i).multiply(x.get(i)));
    }
    return new PointColumn(result);
  }

  public PointColumn multiply(BigInteger x) {
    List<ECPoint> result = new ArrayList<>(points.size());
    for (ECPoint point : points) {
      result.add(point.multiply(x));
    }
    return new PointColumn(result);
  }

  public PointColumn add(PointColumn x) {
    assert x.height() == points.size();
    List<ECPoint> result = new ArrayList<>(points.size());
    for (int i = 0; i < points.size(); i++) {
      result.add(points.get(i).add(x.get(i)));
    }
    return new PointColumn(result);
  }

  public ECPoint get(int i) {
    return points.get(i);
  }
}
