package cn.ringct;

import cn.ringct.Stepper.Step;
import cn.wallet.Hash;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Verifier {

  private final Hash hash;

  private final Stepper stepper;

  public Verifier(Hash hash, Stepper stepper) {
    this.hash = hash;
    this.stepper = stepper;
  }

  public boolean verify(SignedMessage signedMessage) {
    byte[] m = signedMessage.message();
    ECPoint I = signedMessage.keyImage();
    BigInteger c0 = signedMessage.c();

    Step step = stepper.create(I, signedMessage.message(), c0, signedMessage.ring().get(0));
    List<SaltedKey> ring = signedMessage.ring();
    List<Step> steps = new ArrayList<>(ring.size());
    steps.add(step);
    for (int i = 1; i < ring.size(); i++) {
      SaltedKey saltedKey = ring.get(i);
      Step next = stepper.create(I, m, step.c(), saltedKey);
      steps.add(next);
      step = next;
    }

    BigInteger last_c = steps.get(steps.size() - 1).c();
    if (!last_c.equals(c0)) {
      return false;
    }
    for (Step s : steps) {
      if (!s.c().equals(hash.scalar(m, s.L(), s.R()))) {
        return false;
      }
    }
    return true;
  }
}
