package cn.ringct;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

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
