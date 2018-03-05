package cn.ringct;

import cn.wallet.Hash;
import java.math.BigInteger;
import org.bouncycastle.math.ec.ECPoint;

public class MatrixLinker {

  private final ECPoint G;

  private final Hash hash;

  public MatrixLinker(ECPoint G, Hash hash) {
    this.G = G;
    this.hash = hash;
  }

  Link createLink(
      PointVector I,
      byte[] message,
      SaltyVector saltyPoint,
      BigInteger c) {
    NumberVector s = saltyPoint.s();
    PointVector P = saltyPoint.P();
    PointVector L = s.multiply(G).add(P.multiply(c));
    PointVector R = hash.points(P).multiply(s).add(I.multiply(c));
    return create(message, saltyPoint, L, R);
  }

  Link initLink(
      byte[] message,
      SaltyVector alpha) {
    NumberVector s = alpha.s();
    PointVector P = alpha.P();
    PointVector L = s.multiply(G);
    PointVector R = hash.points(P).multiply(s);
    return create(message, alpha, L, R);
  }

  private Link create(
      byte[] message,
      SaltyVector saltyPoint,
      PointVector L,
      PointVector R) {
    BigInteger c = hash.scalar(message, L, R);
    return new Link(c, saltyPoint);
  }

  static class Link {

    private final BigInteger c;

    private final SaltyVector saltyVector;

    Link(BigInteger c, SaltyVector saltyVector) {
      this.c = c;
      this.saltyVector = saltyVector;
    }

    BigInteger c() {
      return c;
    }

    public SaltyVector key() {
      return saltyVector;
    }

    @Override
    public String toString() {
      return c.toString(16).substring(0, 3);
    }
  }
}
