package cn.wallet;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class PrivateUserKey {

  private final Key a;
  private final Key b;

  private final PublicUserKey publicKey;

  private PrivateUserKey(Key a, Key b, PublicUserKey publicKey) {
    this.a = a;
    this.b = b;
    this.publicKey = publicKey;
  }

  static PrivateUserKey create(ECPoint g, String xa, String xb) {
    Key a = new Key(g, new BigInteger(xa, 16));
    Key b = new Key(g, new BigInteger(xb, 16));
    PublicUserKey publicKey = new PublicUserKey(a.publicKey(), b.publicKey());
    return new PrivateUserKey(a, b, publicKey);
  }

  public BigInteger a() {
    return a.privateKey();
  }

  public BigInteger b() {
    return b.privateKey();
  }

  public ECPoint B() {
    return b.publicKey();
  }

  public PublicUserKey publicKey() {
    return publicKey;
  }

  @Override
  public String toString() {
    return "PrivateUserKey{" +
        "a=" + a +
        ", b=" + b +
        '}';
  }
}
