package cn.wallet;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.Optional;

public class Receiver {

  private final PrivateUserKey myKey;
  private final Hash hash;
  private final ECPoint G;

  Receiver(ECPoint G, PrivateUserKey myKey, Hash hash) {
    this.myKey = myKey;
    this.hash = hash;
    this.G = G;
  }

  public Optional<Key> check(Transaction transaction) {
    ECPoint P = transaction.P();
    ECPoint R = transaction.R();
    BigInteger a = myKey.a();
    BigInteger b = myKey.b();
    ECPoint B = myKey.B();
    ECPoint P_ = G.multiply(hash.scalar(R.multiply(a))).add(B);
    if (!P.equals(P_)) {
      return Optional.empty();
    }
    BigInteger x = hash.scalar(R.multiply(a)).add(b);
    return Optional.of(new Key(G, x));
  }
}
