package cn.ringct;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Rand {

  private final BigInteger n;

  public Rand(BigInteger n) {
    this.n = n;
  }

  SaltyPoint salt(ECPoint point) {
    return SaltyPoint.create(point, random());
  }

  BigInteger random() {
    Random rnd = ThreadLocalRandom.current();
    BigInteger r;
    do {
      r = new BigInteger(n.bitLength(), rnd);
    } while (r.compareTo(n) >= 0);
    return r;
  }
}
