package cn.ringct;

import cn.ringct.Stepper.Step;
import cn.wallet.Hash;
import cn.wallet.Key;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RingCt {

  private final BigInteger n;

  private final List<ECPoint> ring;

  private final Key myKey;

  private final Hash hash;

  private final Stepper stepper;

  public RingCt(
      BigInteger n,
      List<ECPoint> ring,
      Key myKey,
      Hash hash,
      Stepper stepper) {
    this.n = n;
    this.ring = ring;
    this.myKey = myKey;
    this.hash = hash;
    this.stepper = stepper;
  }

  public SignedMessage sign(byte[] message) {

    List<SaltedKey> saltedRing = ring.stream()
        .map(p -> SaltedKey.create(p, random()))
        .collect(Collectors.toList());

    BigInteger alpha = random();
    ECPoint P = myKey.publicKey();
    BigInteger x = myKey.privateKey();
    ECPoint I = hash.point(P).multiply(x);

    Step prev = stepper.create(message, alpha, P);
    List<Step> steps = new ArrayList<>(ring.size());

    for (SaltedKey saltedKey : saltedRing) {
      Step next = stepper.create(I, message, prev.c(), saltedKey);
      steps.add(next);
      prev = next;
    }

    BigInteger c = steps.get(ring.size() - 1).c();
    BigInteger s0 = alpha.subtract(c.multiply(x)).mod(n);

    List<SaltedKey> extendedRing = concat(SaltedKey.create(P, s0), saltedRing);

    return new SignedMessage(message, I, c, extendedRing);
  }

  private static <E> List<E> concat(E head, List<E> tail) {
    List<E> list = new ArrayList<>(tail.size() + 1);
    list.add(head);
    list.addAll(tail);
    return list;
  }

  private BigInteger random() {
    Random rnd = ThreadLocalRandom.current();
    BigInteger r;
    do {
      r = new BigInteger(n.bitLength(), rnd);
    } while (r.compareTo(n) >= 0);
    return r;
  }
}
