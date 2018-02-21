package cn.ringct;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.List;

public final class SignedMessage {

  private final byte[] message;
  private final ECPoint I;

  private final BigInteger c0;

  private final List<BigInteger> s;
  private final List<ECPoint> ring;

  public SignedMessage(
      byte[] message,
      ECPoint I,
      BigInteger c0,
      List<BigInteger> s,
      List<ECPoint> ring) {
    this.message = message;
    this.I = I;
    this.c0 = c0;
    this.s = s;
    this.ring = ring;
  }

  public ECPoint keyImage() {
    return I;
  }

  public BigInteger c0() {
    return c0;
  }

  public List<BigInteger> s() {
    return s;
  }

  public BigInteger s(int i) {
    return s.get(i);
  }

  public List<ECPoint> ring() {
    return ring;
  }

  public ECPoint p(int i) {
    return ring.get(i);
  }

  public byte[] message() {
    return message;
  }
}
