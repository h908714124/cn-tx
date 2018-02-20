package cn.ringct;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class SaltedKey {

  private final ECPoint P;

  private final BigInteger s;

  private SaltedKey(ECPoint P, BigInteger s) {
    this.P = P;
    this.s = s;
  }

  static SaltedKey create(ECPoint P, BigInteger s) {
    return new SaltedKey(P, s);
  }

  public ECPoint P() {
    return P;
  }

  public BigInteger s() {
    return s;
  }
}
