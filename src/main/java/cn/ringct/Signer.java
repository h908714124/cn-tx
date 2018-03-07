package cn.ringct;

import cn.ringct.Linker.Link;
import cn.wallet.Hash;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Signer {

  private final BigInteger n;

  private final Hash hash;

  private final Rand random;

  private final Linker linker;

  public Signer(
      BigInteger n,
      Hash hash,
      Rand random,
      Linker linker) {
    this.n = n;
    this.hash = hash;
    this.random = random;
    this.linker = linker;
  }

  /**
   * MLSAG: SIGN
   *
   * @param message an arbitrary message
   * @param myKey a column of private keys, of height {@code n}
   * @param members an {@code n x m} matrix of public keys
   *                (<em>excluding</em> the column of pubkeys
   *                that corresponds to {@code myKey})
   *
   * @return a tuple {@code message, pubkeys, signature}
   *
   * @see <a href="http://eprint.iacr.org/2015/1098">
   *   eprint.iacr.org/2015/1098 (Ring Confidential Transactions)
   *   </a>
   */
  public SignedMessage sign(byte[] message, KeyColumn myKey, PointMatrix members) {

    assert members.height() >= 1 : "need at least 1 row";
    assert members.width() >= 1 : "need at least 1 column";
    assert myKey.height() == members.height();

    SaltyMatrix saltyMembers = members.salt(random);

    PointColumn P = myKey.publicKeys();
    NumberColumn x = myKey.privateKeys();
    PointColumn I = hash.points(P).multiply(x);

    NumberColumn alpha = random.randomVector(members.height());
    SaltyColumn initPoint = SaltyColumn.create(P, alpha);
    Link init = linker.initLink(message, initPoint);
    List<Link> ring = createInitialRing(message, saltyMembers, I, init);

    BigInteger finalC = ring.get(members.width() - 1).c();
    Link mergeLink = createMergeLink(message, myKey, I, alpha, finalC);

    assert mergeLink.c().equals(init.c());

    // Finish the ring by adding a link that fits on both ends.
    ring.add(mergeLink);

    // Spin the ring, so the signer's index isn't known.
    random.spin(ring);

    finalC = ring.get(members.width()).c();
    return SignedMessage.create(message, I, finalC, ring);
  }

  private Link createMergeLink(
      byte[] message,
      KeyColumn myKey,
      PointColumn I,
      NumberColumn alpha,
      BigInteger finalC) {
    PointColumn P = myKey.publicKeys();
    NumberColumn x = myKey.privateKeys();
    NumberColumn magicSalt = alpha.subtract(x.multiply(finalC)).mod(n);
    SaltyColumn mySalt = SaltyColumn.create(P, magicSalt);
    return linker.createLink(I, message, mySalt, finalC);
  }

  private List<Link> createInitialRing(
      byte[] message,
      SaltyMatrix saltyMembers,
      PointColumn I,
      Link init) {
    BigInteger c = init.c();
    List<Link> links = new ArrayList<>(saltyMembers.width() + 1);
    for (SaltyColumn column : saltyMembers.columns()) {
      Link link = linker.createLink(I, message, column, c);
      links.add(link);
      c = link.c();
    }
    return links;
  }
}
