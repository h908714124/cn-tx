package cn.wallet;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Sender {

  private final BigInteger l;

  private final ECPoint g;

  private final Hash hash;

  Sender(BigInteger l, ECPoint g, Hash hash) {
    this.l = l;
    this.g = g;
    this.hash = hash;
  }

  public Transaction send(PublicUserKey dest) {
    ECPoint A = dest.A();
    ECPoint B = dest.B();
    BigInteger r = keygen();

    // compute the one-time key
    ECPoint P = g.multiply(hash.scalar(A.multiply(r))).add(B);
    ECPoint R = g.multiply(r);
    return Transaction.create(P, R);
  }

  private BigInteger keygen() {
    Random rnd = ThreadLocalRandom.current();
    BigInteger r;
    do {
      r = new BigInteger(l.bitLength(), rnd);
    } while (r.compareTo(l) >= 0);
    return r;
  }
}
