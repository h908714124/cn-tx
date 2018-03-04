package cn.wallet;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Sender {

  private final BigInteger l;

  private final ECPoint G;

  private final Hash hash;

  Sender(BigInteger l, ECPoint G, Hash hash) {
    this.l = l;
    this.G = G;
    this.hash = hash;
  }

  public Transaction send(PublicUserKey dest) {
    ECPoint A = dest.A();
    ECPoint B = dest.B();
    BigInteger r = keygen();

    // compute the one-time key
    ECPoint P = G.multiply(hash.scalar(A.multiply(r))).add(B);
    ECPoint R = G.multiply(r);
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
