package cn.ringct;

import cn.wallet.Hash;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

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
    return create(message, L, R);
  }

  Step create(
      byte[] message,
      BigInteger s,
      ECPoint P) {
    ECPoint L = G.multiply(s);
    ECPoint R = hash.point(P).multiply(s);
    return create(message, L, R);
  }

  private Step create(
      byte[] message,
      ECPoint L,
      ECPoint R) {
    BigInteger c = hash.scalar(message, L, R);
    return new Step(L, R, c);
  }

  static class Step {

    private final ECPoint L;
    private final ECPoint R;
    private final BigInteger c;

    Step(ECPoint L, ECPoint R, BigInteger c) {
      this.L = L;
      this.R = R;
      this.c = c;
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
  }
}
