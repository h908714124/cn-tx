package cn.ringct;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.List;

public class SignedMessage {

  private final byte[] message;
  private final ECPoint I;

  private final BigInteger c0;

  private final List<SaltedKey> ring;

  public SignedMessage(
      byte[] message,
      ECPoint I,
      BigInteger c0,
      List<SaltedKey> ring) {
    this.message = message;
    this.I = I;
    this.c0 = c0;
    this.ring = ring;
  }

  public ECPoint keyImage() {
    return I;
  }

  public BigInteger c0() {
    return c0;
  }

  public BigInteger s(int i) {
    return ring.get(i).s();
  }

  public List<SaltedKey> ring() {
    return ring;
  }

  public ECPoint p(int i) {
    return ring.get(i).p();
  }

  public byte[] message() {
    return message;
  }
}
