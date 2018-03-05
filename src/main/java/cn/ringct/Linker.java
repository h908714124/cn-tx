package cn.ringct;

import cn.wallet.Hash;
import java.math.BigInteger;
import org.bouncycastle.math.ec.ECPoint;

public class Linker {

  private final ECPoint G;

  private final Hash hash;

  public Linker(ECPoint G, Hash hash) {
    this.G = G;
    this.hash = hash;
  }

  Link createLink(
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

  Link initLink(
      byte[] message,
      SaltyPoint alpha) {
    BigInteger s = alpha.s();
    ECPoint P = alpha.P();
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

    @Override
    public String toString() {
      return c.toString(16).substring(0, 3);
    }
  }
}
