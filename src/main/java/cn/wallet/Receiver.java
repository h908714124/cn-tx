package cn.wallet;

import java.math.BigInteger;
import java.util.Optional;
import org.bouncycastle.math.ec.ECPoint;

public class Receiver {

  private final PrivateUserKey myKey;
  private final Hash hash;
  private final ECPoint g;

  Receiver(ECPoint g, PrivateUserKey myKey, Hash hash) {
    this.myKey = myKey;
    this.hash = hash;
    this.g = g;
  }

  public Optional<Key> check(Transaction transaction) {
    ECPoint P = transaction.P();
    ECPoint R = transaction.R();
    BigInteger a = myKey.a();
    BigInteger b = myKey.b();
    ECPoint B = myKey.B();
    ECPoint P_ = g.multiply(hash.fieldHash(R.multiply(a))).add(B);
    if (!P.equals(P_)) {
      return Optional.empty();
    }
    BigInteger x = hash.fieldHash(R.multiply(a)).add(b);
    return Optional.of(new Key(g, x));
  }
}
