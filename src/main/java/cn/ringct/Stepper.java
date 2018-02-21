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
      ECPoint pi,
      BigInteger si) {
    BigInteger ci = previous.c1();
    ECPoint Li = g.multiply(si).add(pi.multiply(ci));
    ECPoint Ri = hash.curveHash(pi).multiply(si).add(I.multiply(ci));
    return create(message, Li, Ri);
  }

  SigStep create(byte[] message, ECPoint Li, ECPoint Ri) {
    return SigStep.create(Li, Ri, hash.fieldHash(message, Li, Ri));
  }

  SigStep create(
      byte[] message,
      BigInteger alpha,
      ECPoint p0) {
    ECPoint L_init = g.multiply(alpha);
    ECPoint R_init = hash.curveHash(p0).multiply(alpha);
    return create(message, L_init, R_init);
  }
}
