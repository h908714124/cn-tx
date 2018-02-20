package cn.wallet;

import org.bouncycastle.math.ec.ECPoint;

public class Transaction {

  private final ECPoint P;
  private final ECPoint R;

  private Transaction(ECPoint P, ECPoint R) {
    this.P = P;
    this.R = R;
  }

  static Transaction create(ECPoint P, ECPoint R) {
    return new Transaction(P, R);
  }

  public ECPoint P() {
    return P;
  }

  public ECPoint R() {
    return R;
  }
}
