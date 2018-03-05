package cn.ringct;

import java.math.BigInteger;
import java.util.List;

public class MatrixSignedMessage {

  private final byte[] message;

  private final PointVector I;

  private final BigInteger c;

  private final List<SaltyVector> ring;

  MatrixSignedMessage(
      byte[] message,
      PointVector I,
      BigInteger c,
      List<SaltyVector> ring) {
    this.message = message;
    this.I = I;
    this.c = c;
    this.ring = ring;
  }

  public PointVector keyImage() {
    return I;
  }

  public BigInteger c() {
    return c;
  }

  public List<SaltyVector> ring() {
    return ring;
  }

  public byte[] message() {
    return message;
  }
}
