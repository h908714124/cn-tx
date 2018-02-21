package cn.ringct;

import cn.wallet.Hash;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class Stepper {

  private final ECPoint g;
  private final Hash hash;

  public Stepper(ECPoint g, Hash hash) {
    this.g = g;
    this.hash = hash;
  }

  SigStep step(
      ECPoint I,
      byte[] message,
      SigStep previous,
      SaltedKey saltedKey) {
    BigInteger s0 = saltedKey.s();
    ECPoint p0 = saltedKey.p();
    BigInteger c1 = previous.c1();
    ECPoint L1 = g.multiply(s0).add(p0.multiply(c1));
    ECPoint R1 = hash.point(p0).multiply(s0).add(I.multiply(c1));
    return create(message, L1, R1);
  }

  SigStep create(
      byte[] message,
      ECPoint L0,
      ECPoint R0) {
    BigInteger c1 = hash.scalar(message, L0, R0);
    return new SigStep(L0, R0, c1);
  }

  SigStep create(
      byte[] message,
      BigInteger alpha,
      ECPoint p0) {
    ECPoint L0 = g.multiply(alpha);
    ECPoint R0 = hash.point(p0).multiply(alpha);
    return create(message, L0, R0);
  }
}
