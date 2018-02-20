package cn.wallet;

import org.bouncycastle.math.ec.ECPoint;

public class PublicUserKey {

  private final ECPoint A;
  private final ECPoint B;

  PublicUserKey(ECPoint A, ECPoint B) {
    this.A = A;
    this.B = B;
  }

  public ECPoint A() {
    return A;
  }

  public ECPoint B() {
    return B;
  }
}
