package cn.ringct;

import cn.ringct.Linker.Link;
import cn.wallet.Hash;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

  // MLSAG: SIGN
  // http://eprint.iacr.org/2015/1098
  public SignedMessage sign(byte[] message, KeyVector myKey, KeyMatrix members) {

    if (myKey.length() != members.height()) {
      throw new IllegalArgumentException("Bad key size");
    }

    SaltyMatrix saltyPoints = members.salt(random);

    PointVector P = myKey.publicKeys();
    NumberVector x = myKey.privateKeys();
    PointVector I = hash.points(P).multiply(x);

    NumberVector alpha = random.randomVector(members.height());
    SaltyVector initPoint = SaltyVector.create(P, alpha);
    Link init = linker.initLink(message, initPoint);
    List<Link> ring = createInitialRing(message, saltyPoints, I, init);

    BigInteger finalC = ring.get(ring.size() - 1).c();
    Link mergeLink = createMergeLink(message, myKey, I, alpha, finalC);

    assert mergeLink.c().equals(init.c());

    // Finish the ring by adding a link that fits on both ends.
    ring.add(mergeLink);

    // Spin the ring, so the signer's index isn't known.
    random.spin(ring);

    BigInteger c = ring.get(members.width()).c();
    return new SignedMessage(message, I, c,
        ring.stream().map(Link::key).collect(Collectors.toList()));
  }

  private Link createMergeLink(
      byte[] message,
      KeyVector myKey,
      PointVector I,
      NumberVector alpha,
      BigInteger finalC) {
    PointVector P = myKey.publicKeys();
    NumberVector x = myKey.privateKeys();
    NumberVector magicSalt = alpha.subtract(x.multiply(finalC)).mod(n);
    SaltyVector mySalt = SaltyVector.create(P, magicSalt);
    return linker.createLink(I, message, mySalt, finalC);
  }

  private List<Link> createInitialRing(
      byte[] message,
      SaltyMatrix saltyVectors,
      PointVector I,
      Link init) {
    Link prev = init;
    ArrayList<Link> links = new ArrayList<>(saltyVectors.width() + 1);
    for (SaltyVector column : saltyVectors.columns()) {
      Link link = linker.createLink(I, message, column, prev.c());
      links.add(link);
      prev = link;
    }
    return links;
  }
}
