package cn.ringct;

import cn.ringct.MatrixLinker.Link;
import cn.wallet.Hash;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class MatrixSigner {

  private final BigInteger n;

  private final Hash hash;

  private final Rand random;

  private final MatrixLinker linker;

  public MatrixSigner(
      BigInteger n,
      Hash hash,
      Rand random,
      MatrixLinker linker) {
    this.n = n;
    this.hash = hash;
    this.random = random;
    this.linker = linker;
  }

  public MatrixSignedMessage sign(byte[] message, KeyVector myKey, KeyMatrix members) {

    if (myKey.length() != members.rows()) {
      throw new IllegalArgumentException("Bad key size");
    }

    List<SaltyVector> saltyPoints = members.columns().stream()
        .map(random::salt)
        .collect(Collectors.toList());

    PointVector P = myKey.publicKeys();
    NumberVector x = myKey.privateKeys();
    PointVector I = hash.points(P).multiply(x);

    NumberVector alpha = random.randomVector(members.rows());
    SaltyVector initPoint = SaltyVector.create(P, alpha);
    Link init = linker.initLink(message, initPoint);
    List<Link> ring = addLinks(message, saltyPoints, I, init);

    BigInteger finalC = ring.get(ring.size() - 1).c();
    Link mergeLink = createMergeLink(message, myKey, I, alpha, finalC);

    assert mergeLink.c().equals(init.c());

    // Finish the ring by adding a link that fits on both ends.
    ring.add(mergeLink);

    // Spin the ring, so the signer's index isn't known.
    rotateRandom(ring);

    BigInteger c = ring.get(members.size()).c();
    return new MatrixSignedMessage(message, I, c,
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
    SaltyVector mySaltyPoint = SaltyVector.create(P, magicSalt);
    return linker.createLink(I, message, mySaltyPoint, finalC);
  }

  private ArrayList<Link> addLinks(
      byte[] message,
      List<SaltyVector> saltyPoints,
      PointVector I,
      Link init) {
    Link prev = init;
    ArrayList<Link> links = new ArrayList<>(saltyPoints.size() + 1);
    for (SaltyVector saltyPoint : saltyPoints) {
      Link link = linker.createLink(I, message, saltyPoint, prev.c());
      links.add(link);
      prev = link;
    }
    return links;
  }

  private static <E> void rotateRandom(List<E> list) {
    int j = ThreadLocalRandom.current().nextInt(list.size() + 1);
    for (int d = 0; d < j; d++) {
      E temp = list.get(0);
      for (int i = 1; i < list.size(); i++) {
        list.set(i - 1, list.get(i));
      }
      list.set(list.size() - 1, temp);
    }
  }
}
