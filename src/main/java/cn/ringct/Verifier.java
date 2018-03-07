package cn.ringct;

import cn.ringct.Linker.Link;
import java.math.BigInteger;

public class Verifier {

  private final Linker linker;

  public Verifier(Linker linker) {
    this.linker = linker;
  }

  // MLSAG: VERIFY
  // http://eprint.iacr.org/2015/1098
  public boolean verify(SignedMessage signedMessage) {
    byte[] message = signedMessage.message();
    PointColumn I = signedMessage.keyImage();
    BigInteger c = signedMessage.c();
    Link link;

    SaltyMatrix ring = signedMessage.ring();

    for (SaltyColumn sk : ring.columns()) {
      link = linker.createLink(I, message, sk, c);
      c = link.c();
    }

    return signedMessage.c().equals(c);
  }
}
