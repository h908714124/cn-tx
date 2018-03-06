package cn.ringct;

import java.util.List;

class SaltyMatrix {

  private final List<SaltyVector> columns;

  SaltyMatrix(List<SaltyVector> columns) {
    this.columns = columns;
  }

  int width() {
    return columns.size();
  }

  List<SaltyVector> columns() {
    return columns;
  }
}
