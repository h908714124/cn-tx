package cn.ringct;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class SaltedKey {

  private final ECPoint p;
  private final BigInteger s;

  private SaltedKey(ECPoint p, BigInteger s) {
    this.p = p;
    this.s = s;
  }

  static SaltedKey create(ECPoint p, BigInteger s) {
    return new SaltedKey(p, s);
  }

  public ECPoint p() {
    return p;
  }

  public BigInteger s() {
    return s;
  }
}
