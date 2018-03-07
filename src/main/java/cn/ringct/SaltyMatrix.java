package cn.ringct;

import java.util.List;

class SaltyMatrix {

  private final List<SaltyColumn> columns;

  SaltyMatrix(List<SaltyColumn> columns) {
    this.columns = columns;
  }

  int width() {
    return columns.size();
  }

  List<SaltyColumn> columns() {
    return columns;
  }
}
