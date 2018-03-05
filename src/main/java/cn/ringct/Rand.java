package cn.ringct;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.bouncycastle.math.ec.ECPoint;

public class Rand {

  private final BigInteger n;

  public Rand(BigInteger n) {
    this.n = n;
  }

  SaltyPoint salt(ECPoint point) {
    return SaltyPoint.create(point, random());
  }

  SaltyVector salt(PointVector vector) {
    List<SaltyPoint> result = new ArrayList<>(vector.points().size());
    for (ECPoint point : vector.points()) {
      result.add(SaltyPoint.create(point, random()));
    }
    return new SaltyVector(result);
  }

  public BigInteger random() {
    Random rnd = ThreadLocalRandom.current();
    BigInteger r;
    do {
      r = new BigInteger(n.bitLength(), rnd);
    } while (r.compareTo(n) >= 0);
    return r;
  }

  public NumberVector randomVector(int size) {
    List<BigInteger> result = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      result.add(random());
    }
    return new NumberVector(result);
  }
}
