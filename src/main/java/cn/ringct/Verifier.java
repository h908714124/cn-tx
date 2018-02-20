package cn.ringct;

import cn.wallet.Hash;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class Verifier {

  private final ECPoint g;
  private final Hash hash;
  private final Stepper stepper;

  public Verifier(ECPoint g, Hash hash, Stepper stepper) {
    this.g = g;
    this.hash = hash;
    this.stepper = stepper;
  }

  public boolean verify(SignedMessage signedMessage) {
    byte[] m = signedMessage.message();
    ECPoint I = signedMessage.keyImage();
    ECPoint p0 = signedMessage.p0();
    BigInteger s0 = signedMessage.s0();
    BigInteger c0 = signedMessage.c0();
    ECPoint L0 = g.multiply(s0).add(p0.multiply(c0));
    ECPoint R0 = hash.curveHash(p0).multiply(s0).add(I.multiply(c0));
    BigInteger c1 = hash.fieldHash(m, L0, R0);
    ECPoint p1 = signedMessage.p1();
    ECPoint p2 = signedMessage.p2();
    BigInteger s1 = signedMessage.s1();
    BigInteger s2 = signedMessage.s2();
    SigStep step0 = new SigStep(L0, R0, c1);
    SigStep step1 = stepper.step(I, m, step0, p1, s1);
    SigStep step2 = stepper.step(I, m, step1, p2, s2);
    BigInteger c3 = step2.cppi();
    if (!c3.equals(c0)) {
      return false;
    }
    if (!c1.equals(hash.fieldHash(m, step0.Li(), step0.Ri()))) {
      return false;
    }
    BigInteger c2 = step1.cppi();
    if (!c2.equals(hash.fieldHash(m, step1.Li(), step1.Ri()))) {
      return false;
    }
    if (!c0.equals(hash.fieldHash(m, step2.Li(), step2.Ri()))) {
      return false;
    }
    return true;
  }


}
