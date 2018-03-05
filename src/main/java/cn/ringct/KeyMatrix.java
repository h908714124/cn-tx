package cn.ringct;

import java.util.List;

public class KeyMatrix {

  public KeyMatrix(List<PointVector> pointVectors) {
    if (pointVectors.size() < 2) {
      throw new IllegalArgumentException("Error! What is c if cols = 1!");
    }
    for (PointVector vector : pointVectors) {
      if (vector.size() != pointVectors.get(0).size()) {
        throw new IllegalArgumentException("not rectangular");
      }
    }
    this.pointVectors = pointVectors;
  }

  private final List<PointVector> pointVectors;

  public List<PointVector> columns() {
    return pointVectors;
  }

  public int rows() {
    return columns().get(0).size();
  }
}
