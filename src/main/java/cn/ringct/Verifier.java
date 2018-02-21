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
    SigStep step = stepper.create(signedMessage.message(), L0, R0);
    List<ECPoint> ring = signedMessage.ring();
    List<SigStep> steps = new ArrayList<>(ring.size());
    steps.add(step);
    for (int i = 1; i < ring.size(); i++) {
      ECPoint point = ring.get(i);
      SigStep next = stepper.step(I, m, step, point, signedMessage.s(i));
      steps.add(next);
      step = next;
    }

    BigInteger last_c = steps.get(steps.size() - 1).c1();
    if (!last_c.equals(c0)) {
      return false;
    }
    for (SigStep s : steps) {
      if (!s.c1().equals(hash.fieldHash(m, s.L0(), s.R0()))) {
        return false;
      }
    }
    return true;
  }
}
