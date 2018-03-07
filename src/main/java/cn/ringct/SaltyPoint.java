package cn.ringct;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECPoint;

class SaltyPoint {

  private final ECPoint P;

  private final BigInteger s;

  private SaltyPoint(ECPoint P, BigInteger s) {
    this.P = P;
    this.s = s;
  }

  static SaltyPoint create(ECPoint P, BigInteger s) {
    return new SaltyPoint(P, s);
  }

  ECPoint P() {
    return P;
  }

  BigInteger s() {
    return s;
  }
}
