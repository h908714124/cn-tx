package cn.ringct;

import cn.ringct.Linker.Link;
import cn.wallet.Hash;
import cn.wallet.Key;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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

  public SignedMessage sign(byte[] message, Key myKey, List<ECPoint> members) {

    List<SaltyPoint> saltyPoints = members.stream()
        .map(random::salt)
        .collect(Collectors.toList());

    ECPoint P = myKey.publicKey();
    BigInteger x = myKey.privateKey();
    ECPoint I = hash.point(P).multiply(x);

    Link init = linker.initLink(message, P);
    ArrayList<Link> ring = addLinks(message, saltyPoints, I, init);

    Link mergeLink = createMergeLink(message, myKey, I, init.s(), ring.get(ring.size() - 1).c());

    assert mergeLink.c().equals(init.c());

    // Finish the ring by adding a link that fits on both ends.
    ring.add(mergeLink);

    // Spin the ring, so the signer's index isn't always 0.
    rotateRandom(ring);

    return new SignedMessage(message, I, ring.get(ring.size() - 1).c(),
        ring.stream().map(Link::key).collect(Collectors.toList()));
  }

  private Link createMergeLink(
      byte[] message,
      Key myKey,
      ECPoint I,
      BigInteger alpha,
      BigInteger last_c) {
    ECPoint P = myKey.publicKey();
    BigInteger x = myKey.privateKey();
    BigInteger magicSalt = alpha.subtract(last_c.multiply(x)).mod(n);
    SaltyPoint mySaltyPoint = SaltyPoint.create(P, magicSalt);
    return linker.createLink(I, message, mySaltyPoint, last_c);
  }

  private ArrayList<Link> addLinks(
      byte[] message,
      List<SaltyPoint> saltyPoints,
      ECPoint i,
      Link init) {
    Link prev = init;
    ArrayList<Link> links = new ArrayList<>(saltyPoints.size() + 1);
    for (SaltyPoint saltyPoint : saltyPoints) {
      Link link = linker.createLink(i, message, saltyPoint, prev.c());
      links.add(link);
      prev = link;
    }
    return links;
  }

  private static <E> void rotateRandom(ArrayList<E> list) {
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
