package cn.ringct;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.math.ec.ECPoint;

public final class PointVector {

  private final List<ECPoint> points;

  public PointVector(List<ECPoint> points) {
    this.points = points;
  }

  public List<ECPoint> points() {
    return points;
  }

  public int length() {
    return points.size();
  }

  public PointVector multiply(NumberVector x) {
    if (x.length() != points.size()) {
      throw new IllegalArgumentException();
    }
    List<ECPoint> result = new ArrayList<>(points.size());
    for (int i = 0; i < points.size(); i++) {
      result.add(points.get(i).multiply(x.get(i)));
    }
    return new PointVector(result);
  }

  public PointVector multiply(BigInteger x) {
    List<ECPoint> result = new ArrayList<>(points.size());
    for (ECPoint point : points) {
      result.add(point.multiply(x));
    }
    return new PointVector(result);
  }

  public PointVector add(PointVector x) {
    if (x.length() != points.size()) {
      throw new IllegalArgumentException();
    }
    List<ECPoint> result = new ArrayList<>(points.size());
    for (int i = 0; i < points.size(); i++) {
      result.add(points.get(i).add(x.get(i)));
    }
    return new PointVector(result);
  }

  public ECPoint get(int i) {
    return points.get(i);
  }
}
