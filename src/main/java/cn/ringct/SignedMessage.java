package cn.ringct;

import cn.ringct.Linker.Link;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

public class SignedMessage {

  private final byte[] message;

  private final PointColumn I;

  private final BigInteger c;

  private final SaltyMatrix matrix;

  private SignedMessage(
      byte[] message,
      PointColumn I,
      BigInteger c,
      SaltyMatrix matrix) {
    this.message = message;
    this.I = I;
    this.c = c;
    this.matrix = matrix;
  }

  static SignedMessage create(
      byte[] message,
      PointColumn I,
      BigInteger c,
      List<Link> ring) {
    List<SaltyColumn> columns = ring.stream()
        .map(Link::key)
        .collect(Collectors.toList());
    return new SignedMessage(message, I, c,
        new SaltyMatrix(columns));
  }

  public PointColumn keyImage() {
    return I;
  }

  public BigInteger c() {
    return c;
  }

  public SaltyMatrix ring() {
    return matrix;
  }

  public byte[] message() {
    return message;
  }
}
