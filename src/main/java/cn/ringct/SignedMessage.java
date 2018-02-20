package cn.ringct;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.List;

public class SignedMessage {

  private final byte[] message;

  private final ECPoint I;

  private final BigInteger c;

  private final List<SaltedKey> ring;

  SignedMessage(
      byte[] message,
      ECPoint I,
      BigInteger c,
      List<SaltedKey> ring) {
    this.message = message;
    this.I = I;
    this.c = c;
    this.ring = ring;
  }

  public ECPoint keyImage() {
    return I;
  }

  public BigInteger c() {
    return c;
  }

  public List<SaltedKey> ring() {
    return ring;
  }

  public byte[] message() {
    return message;
  }
}
