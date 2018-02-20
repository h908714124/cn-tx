package cn.wallet;

import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.djb.Curve25519FieldElement;

import java.math.BigInteger;

public class Key {

  private final ECPoint G;

  private final ECFieldElement x;

  public Key(ECPoint G, BigInteger x) {
    this.G = G;
    this.x = new Curve25519FieldElement(x);
  }

  public ECPoint publicKey() {
    return G.multiply(privateKey());
  }

  public BigInteger privateKey() {
    return x.toBigInteger();
  }

  @Override
  public String toString() {
    return x.toString();
  }
}
