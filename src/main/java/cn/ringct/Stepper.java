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
    BigInteger ci = previous.cppi();
    ECPoint Li = g.multiply(si).add(pi.multiply(ci));
    ECPoint Ri = hash.curveHash(pi).multiply(si).add(I.multiply(ci));
    return create(message, Li, Ri);
  }

  SigStep create(byte[] message, ECPoint Li, ECPoint Ri) {
    return new SigStep(Li, Ri, hash.fieldHash(message, Li, Ri));
  }
}
