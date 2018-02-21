package cn.wallet;

import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement;

import java.math.BigInteger;

public class Key {

  private final ECPoint g;

  private final ECFieldElement x;

  public Key(ECPoint g, BigInteger x) {
    this.g = g;
    this.x = new Curve25519FieldElement(x);
  }

  public ECPoint publicKey() {
    return g.multiply(privateKey());
  }

  public BigInteger privateKey() {
    return x.toBigInteger();
  }

  @Override
  public String toString() {
    return x.toString();
  }
}
