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
      PointColumn I,
      byte[] message,
      SaltyColumn saltyPoint,
      BigInteger c) {
    NumberColumn s = saltyPoint.s();
    PointColumn P = saltyPoint.P();
    PointColumn L = s.multiply(G).add(P.multiply(c));
    PointColumn R = hash.points(P).multiply(s).add(I.multiply(c));
    return create(message, saltyPoint, L, R);
  }

  Link initLink(
      byte[] message,
      SaltyColumn alpha) {
    NumberColumn s = alpha.s();
    PointColumn P = alpha.P();
    PointColumn L = s.multiply(G);
    PointColumn R = hash.points(P).multiply(s);
    return create(message, alpha, L, R);
  }

  private Link create(
      byte[] message,
      SaltyColumn saltyPoint,
      PointColumn L,
      PointColumn R) {
    BigInteger c = hash.scalar(message, L, R);
    return new Link(c, saltyPoint);
  }

  static class Link {

    private final BigInteger c;

    private final SaltyColumn saltyColumn;

    Link(BigInteger c, SaltyColumn saltyColumn) {
      this.c = c;
      this.saltyColumn = saltyColumn;
    }

    BigInteger c() {
      return c;
    }

    public SaltyColumn key() {
      return saltyColumn;
    }

    @Override
    public String toString() {
      return c.toString(16).substring(0, 3);
    }
  }
}
