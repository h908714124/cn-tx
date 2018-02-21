package cn.ringct;

import cn.wallet.Hash;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
    ECPoint p0 = signedMessage.p(0);
    BigInteger s0 = signedMessage.s(0);
    BigInteger c0 = signedMessage.c0();
    ECPoint L0 = g.multiply(s0).add(p0.multiply(c0));
    ECPoint R0 = hash.curveHash(p0).multiply(s0).add(I.multiply(c0));
    BigInteger c1 = hash.fieldHash(m, L0, R0);
    SigStep step = new SigStep(L0, R0, c1);
    List<ECPoint> ring = signedMessage.ring();
    List<SigStep> steps = new ArrayList<>(ring.size());
    steps.add(step);
    for (int i = 1; i < ring.size(); i++) {
      ECPoint point = ring.get(i);
      SigStep next = stepper.step(I, m, step, point, signedMessage.s(i));
      steps.add(next);
      step = next;
    }

    BigInteger last_c = steps.get(steps.size() - 1).cppi();
    if (!last_c.equals(c0)) {
      return false;
    }
/*
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
*/
    return true;
  }


}
