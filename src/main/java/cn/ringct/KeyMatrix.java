package cn.ringct;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bouncycastle.math.ec.ECPoint;

public class KeyMatrix {

  // Each member owns all keys in one of the columns.
  private final List<PointVector> columns;

  private KeyMatrix(List<PointVector> columns) {
    this.columns = columns;
  }

  public static KeyMatrix create(List<List<ECPoint>> rows) {
    List<List<ECPoint>> columns = transpose(rows);
    return new KeyMatrix(columns.stream()
        .map(PointVector::new)
        .collect(Collectors.toList()));
  }

  private static List<List<ECPoint>> transpose(List<List<ECPoint>> rows) {
    int width = rows.get(0).size();
    int height = rows.size();
    assert width >= 2 : "need at least 2 members";
    List<List<ECPoint>> columns = new ArrayList<>(width);
    for (int i = 0; i < width; i++) {
      columns.add(new ArrayList<>(height));
    }
    for (List<ECPoint> row : rows) {
      assert row.size() == width;
      for (int c = 0; c < width; c++) {
        columns.get(c).add(row.get(c));
      }
    }
    return columns;
  }

  public SaltyMatrix salt(Rand random) {
    return new SaltyMatrix(columns.stream()
        .map(random::salt)
        .collect(Collectors.toList()));
  }

  public int height() {
    return columns.get(0).length();
  }

  public int width() {
    return columns.size();
  }
}
