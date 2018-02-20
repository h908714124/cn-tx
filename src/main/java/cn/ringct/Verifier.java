package cn.ringct;

import cn.ringct.Stepper.Step;
import java.math.BigInteger;
import java.util.List;
import org.bouncycastle.math.ec.ECPoint;

public class Verifier {

  private final Stepper stepper;

  public Verifier(Stepper stepper) {
    this.stepper = stepper;
  }

  public boolean verify(SignedMessage signedMessage) {
    byte[] message = signedMessage.message();
    ECPoint I = signedMessage.keyImage();
    BigInteger c = signedMessage.c();

    Step step = null;
    List<SaltedKey> ring = signedMessage.ring();

    for (SaltedKey sk : ring) {
      step = stepper.create(I, message, c, sk);
      c = step.c();
    }

    BigInteger final_c = step.c();
    return final_c.equals(c);
  }
}
