package cn.ringct;

import java.util.ArrayList;
import java.util.List;

public class SaltyVector {

  private final List<SaltyPoint> points;

  private SaltyVector(List<SaltyPoint> points) {
    this.points = points;
  }

  static SaltyVector create(PointVector P, NumberVector s) {
    if (P.size() != s.size()) {
      throw new IllegalArgumentException();
    }
    List<Object> result = new ArrayList<>(P.size());
  }

  public List<SaltyPoint> columns() {
    return points;
  }
}
