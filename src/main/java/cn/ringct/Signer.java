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

    Link init = linker.firstLink(message, P);
    List<Link> chain = createChain(message, saltyPoints, I, init);

    BigInteger last_c = chain.get(chain.size() - 1).c();
    BigInteger alpha = init.s();
    BigInteger magicSalt = alpha.subtract(last_c.multiply(x)).mod(n);

    Link mergeLink = linker.nextLink(I, message, SaltyPoint.create(P, magicSalt), last_c);

    assert mergeLink.c().equals(init.c());

    // Add merge link to finish the ring.
    List<Link> ring = concat(mergeLink, chain);

    // Spin the ring, so the signer's index isn't always 0.
    ring = rotateRandom(ring);

    return new SignedMessage(message, I, ring.get(ring.size() - 1).c(),
        ring.stream().map(Link::key).collect(Collectors.toList()));
  }

  private List<Link> createChain(byte[] message, List<SaltyPoint> saltyRing, ECPoint i, Link init) {
    List<Link> links = new ArrayList<>(saltyRing.size());
    for (SaltyPoint saltyPoint : saltyRing) {
      Link next = linker.nextLink(i, message, saltyPoint, init.c());
      links.add(next);
      init = next;
    }
    return links;
  }

  private static <E> List<E> rotateRandom(List<E> list) {
    int j = ThreadLocalRandom.current().nextInt(list.size() + 1);
    List<E> result = new ArrayList<>(list.size());
    for (int i = 0; i < list.size(); i++) {
      E element = list.get(Math.floorMod(i + j, list.size()));
      result.add(element);
    }
    return result;
  }

  private static <E> List<E> concat(E head, List<E> tail) {
    List<E> list = new ArrayList<>(tail.size() + 1);
    list.add(head);
    list.addAll(tail);
    return list;
  }
}
