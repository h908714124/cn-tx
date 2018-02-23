package cn.ringct;

import cn.wallet.Hash;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class Linker {

  private final ECPoint G;

  private final Rand random;

  private final Hash hash;

  public Linker(ECPoint G, Rand random, Hash hash) {
    this.G = G;
    this.random = random;
    this.hash = hash;
  }

  Link nextLink(
      ECPoint I,
      byte[] message,
      SaltyPoint saltyPoint,
      BigInteger c) {
    BigInteger s = saltyPoint.s();
    ECPoint P = saltyPoint.P();
    ECPoint L = G.multiply(s).add(P.multiply(c));
    ECPoint R = hash.point(P).multiply(s).add(I.multiply(c));
    return create(message, saltyPoint, L, R);
  }

  Link firstLink(
      byte[] message,
      ECPoint P) {
    SaltyPoint alpha = SaltyPoint.create(P, random.random());
    BigInteger s = alpha.s();
    ECPoint L = G.multiply(s);
    ECPoint R = hash.point(P).multiply(s);
    return create(message, alpha, L, R);
  }

  private Link create(
      byte[] message,
      SaltyPoint saltyPoint,
      ECPoint L,
      ECPoint R) {
    BigInteger c = hash.scalar(message, L, R);
    return new Link(c, saltyPoint);
  }

  static class Link {

    private final BigInteger c;

    private final SaltyPoint saltyPoint;

    Link(BigInteger c, SaltyPoint saltyPoint) {
      this.c = c;
      this.saltyPoint = saltyPoint;
    }

    BigInteger c() {
      return c;
    }

    public SaltyPoint key() {
      return saltyPoint;
    }

    public BigInteger s() {
      return saltyPoint.s();
    }

    @Override
    public String toString() {
      return c.toString(16).substring(0, 3);
    }
  }
}
