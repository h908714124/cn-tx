package cn.ringct;

import cn.wallet.Hash;
import java.math.BigInteger;
import org.bouncycastle.math.ec.ECPoint;

public class Stepper {

  private final ECPoint G;

  private final Hash hash;

  public Stepper(ECPoint G, Hash hash) {
    this.G = G;
    this.hash = hash;
  }

  Step create(
      ECPoint I,
      byte[] message,
      BigInteger c,
      SaltedKey saltedKey) {
    BigInteger s = saltedKey.s();
    ECPoint P = saltedKey.P();
    ECPoint L = G.multiply(s).add(P.multiply(c));
    ECPoint R = hash.point(P).multiply(s).add(I.multiply(c));
    return create(message, saltedKey, L, R);
  }

  Step create(
      byte[] message,
      BigInteger s,
      ECPoint P) {
    ECPoint L = G.multiply(s);
    ECPoint R = hash.point(P).multiply(s);
    return create(message, SaltedKey.create(P, s), L, R);
  }

  private Step create(
      byte[] message,
      SaltedKey saltedKey,
      ECPoint L,
      ECPoint R) {
    BigInteger c = hash.scalar(message, L, R);
    return new Step(L, R, c, saltedKey);
  }

  static class Step {

    private final ECPoint L;
    private final ECPoint R;
    private final BigInteger c;

    private final SaltedKey saltedKey;

    Step(ECPoint L, ECPoint R, BigInteger c, SaltedKey saltedKey) {
      this.L = L;
      this.R = R;
      this.c = c;
      this.saltedKey = saltedKey;
    }

    ECPoint L() {
      return L;
    }

    ECPoint R() {
      return R;
    }

    BigInteger c() {
      return c;
    }

    SaltedKey key() {
      return saltedKey;
    }

    public Step updateKey(SaltedKey key) {
      return new Step(L, R, c, key);
    }
  }
}
