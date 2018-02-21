package cn.ringct;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

class SigStep {

  private final ECPoint L0;
  private final ECPoint R0;
  private final BigInteger c1;

  private SigStep(ECPoint L0, ECPoint R0, BigInteger c1) {
    this.L0 = L0;
    this.R0 = R0;
    this.c1 = c1;
  }

  static SigStep create(ECPoint Li, ECPoint Ri, BigInteger cppi) {
    return new SigStep(Li, Ri, cppi);
  }

  ECPoint L0() {
    return L0;
  }

  ECPoint R0() {
    return R0;
  }

  BigInteger c1() {
    return c1;
  }
}
