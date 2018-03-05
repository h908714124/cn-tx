package cn.ringct;

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

  public int size() {
    return points.size();
  }

  public PointVector multiply(NumberVector x) {
    if (x.size() != points.size()) {
      throw new IllegalArgumentException();
    }
    List<ECPoint> result = new ArrayList<>(points.size());
    for (int i = 0; i < points.size(); i++) {
      result.add(points.get(i).multiply(x.get(i)));
    }
    return new PointVector(result);
  }
}