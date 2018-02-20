package cn.ringct;

import cn.wallet.Hash;
import cn.wallet.Key;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public final class RingCt {

  private final ECPoint g;

  private final BigInteger l;

  private final ECPoint p0;

  private final ECPoint p2;

  private final Key myKey;

  private final Hash hash;

  private final Stepper stepper;

  public RingCt(
      ECPoint g,
      BigInteger l,
      ECPoint p0,
      ECPoint p2,
      Key myKey,
      Hash hash, Stepper stepper) {
    this.g = g;
    this.l = l;
    this.p0 = p0;
    this.p2 = p2;
    this.myKey = myKey;
    this.hash = hash;
    this.stepper = stepper;
  }

  public SignedMessage sign(byte[] message) {
    BigInteger s0 = new BigInteger("efe734dbde78c0b30a9170bf99bde2499d320f4c88e125fa71afbc000d5e120", 16);
    BigInteger alpha = new BigInteger("25a7b8a6d38b9eaa2b7f378928538bc2393fc512ed106369fc2fce6d554a3b8", 16);
    BigInteger s2 = new BigInteger("c292aeddc03e452697484b598870e59656ba6783a1c5d36f50623f39be8f077", 16);
    ECPoint p1 = myKey.publicKey();
    ECPoint I = hash.curveHash(p1).multiply(myKey.privateKey());

    ECPoint L1 = g.multiply(alpha);
    ECPoint R1 = hash.curveHash(p1).multiply(alpha);
    BigInteger c2 = hash.fieldHash(message, L1, R1);

    SigStep step1 = new SigStep(L1, R1, c2);
    SigStep step2 = stepper.step(I, message, step1, p2, s2);
    SigStep step0 = stepper.step(I, message, step2, p0, s0);

    BigInteger c1 = step0.cppi();
    BigInteger s1 = alpha.subtract(c1.multiply(myKey.privateKey())).mod(l);
    BigInteger c0 = step2.cppi();

    return new SignedMessage(message, I, c0, s0, s1, s2, p0, p1, p2);
  }
}
