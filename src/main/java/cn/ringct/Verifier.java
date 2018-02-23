package cn.ringct;

import cn.ringct.Linker.Link;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.List;

public class Verifier {

  private final Linker linker;

  public Verifier(Linker linker) {
    this.linker = linker;
  }

  public boolean verify(SignedMessage signedMessage) {
    byte[] message = signedMessage.message();
    ECPoint I = signedMessage.keyImage();
    BigInteger c = signedMessage.c();
    Link link;

    List<SaltyPoint> ring = signedMessage.ring();

    for (SaltyPoint sk : ring) {
      link = linker.nextLink(I, message, sk, c);
      c = link.c();
    }

    return signedMessage.c().equals(c);
  }
}
