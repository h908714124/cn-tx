package cn.ringct;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bouncycastle.math.ec.ECPoint;

public class KeyMatrix {

  private final List<PointVector> columns;

  private KeyMatrix(List<PointVector> columns) {
    if (columns.size() < 2) {
      throw new IllegalArgumentException("Error! What is c if cols = 1!");
    }
    for (PointVector vector : columns) {
      if (vector.length() != columns.get(0).length()) {
        throw new IllegalArgumentException("not rectangular");
      }
    }
    this.columns = columns;
  }

  public static KeyMatrix create(List<List<ECPoint>> rows) {
    int rowsize = rows.get(0).size();
    List<List<ECPoint>> columns = new ArrayList<>(rowsize);
    for (int i = 0; i < rowsize; i++) {
      columns.add(new ArrayList<>(rows.size()));
    }
    for (List<ECPoint> row : rows) {
      for (int c = 0; c < row.size(); c++) {
        columns.get(c).add(row.get(c));
      }
    }
    return _create(columns);
  }

  private static KeyMatrix _create(List<List<ECPoint>> columns) {
    return new KeyMatrix(columns.stream()
        .map(PointVector::new)
        .collect(Collectors.toList()));
  }

  public List<PointVector> columns() {
    return columns;
  }

  public int size() {
    return columns().size();
  }

  public int rows() {
    return columns().get(0).length();
  }
}
