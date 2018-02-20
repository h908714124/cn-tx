package cn.ringct;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

final class SigStep {

  private final ECPoint Li;
  private final ECPoint Ri;
  private final BigInteger cppi;

  SigStep(ECPoint Li, ECPoint Ri, BigInteger cppi) {
    this.Li = Li;
    this.Ri = Ri;
    this.cppi = cppi;
  }

  ECPoint Li() {
    return Li;
  }

  ECPoint Ri() {
    return Ri;
  }

  BigInteger cppi() {
    return cppi;
  }
}
