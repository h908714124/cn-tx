package cn.ringct;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bouncycastle.math.ec.ECPoint;

public class PointMatrix {

  private final List<PointColumn> columns;

  private PointMatrix(List<PointColumn> columns) {
    this.columns = columns;
  }

  public static PointMatrix create(List<List<ECPoint>> rows) {
    List<List<ECPoint>> columns = transpose(rows);
    return new PointMatrix(columns.stream()
        .map(PointColumn::new)
        .collect(Collectors.toList()));
  }

  private static List<List<ECPoint>> transpose(List<List<ECPoint>> rows) {
    int height = rows.size();
    assert height >= 1 : "need at least 1 row";
    int width = rows.get(0).size();
    assert width >= 1 : "need at least 1 column";
    List<List<ECPoint>> columns = new ArrayList<>(width);
    for (int i = 0; i < width; i++) {
      columns.add(new ArrayList<>(height));
    }
    for (List<ECPoint> row : rows) {
      assert row.size() == width : "not rectangular";
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
    return columns.get(0).height();
  }

  public int width() {
    return columns.size();
  }
}
