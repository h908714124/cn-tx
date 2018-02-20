package cn.wallet;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement;

final class Key {

  private final ECPoint g;

  // private key
  private final ECFieldElement x;

  Key(ECPoint g, BigInteger x) {
    this.g = g;
    this.x = new Curve25519FieldElement(x);
  }

  ECPoint publicKey() {
    return g.multiply(privateKey());
  }

  BigInteger privateKey() {
    return x.toBigInteger();
  }

  @Override
  public String toString() {
    return x.toString();
  }
}
