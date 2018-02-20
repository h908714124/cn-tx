package cn.ringct;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public final class SignedMessage {

  private final byte[] message;
  private final ECPoint I;

  private final BigInteger c0;

  private final BigInteger s0;
  private final BigInteger s1;
  private final BigInteger s2;

  private final ECPoint P0;
  private final ECPoint P1;
  private final ECPoint P2;

  public SignedMessage(
      byte[] message,
      ECPoint I,
      BigInteger c0,
      BigInteger s0,
      BigInteger s1,
      BigInteger s2,
      ECPoint P0,
      ECPoint P1,
      ECPoint P2) {
    this.message = message;
    this.I = I;
    this.c0 = c0;
    this.s0 = s0;
    this.s1 = s1;
    this.s2 = s2;
    this.P0 = P0;
    this.P1 = P1;
    this.P2 = P2;
  }

  public ECPoint keyImage() {
    return I;
  }

  public BigInteger c0() {
    return c0;
  }

  public BigInteger s0() {
    return s0;
  }

  public BigInteger s1() {
    return s1;
  }

  public BigInteger s2() {
    return s2;
  }

  public ECPoint p0() {
    return P0;
  }

  public ECPoint p1() {
    return P1;
  }

  public ECPoint p2() {
    return P2;
  }

  public byte[] message() {
    return message;
  }
}
