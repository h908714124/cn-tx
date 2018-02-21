package cn.ringct;

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
    ECPoint p0 = myKey.publicKey();
    ECPoint I = hash.point(p0).multiply(myKey.privateKey());

    SigStep prev = stepper.create(message, alpha, p0);
    List<SigStep> steps = new ArrayList<>(saltedRing.size());

    for (SaltedKey saltedKey : saltedRing) {
      SigStep next = stepper.step(I, message, prev, saltedKey);
      steps.add(next);
      prev = next;
    }

    BigInteger c0 = steps.get(ring.size() - 1).c1();
    BigInteger s0 = alpha.subtract(c0.multiply(myKey.privateKey())).mod(n);

    List<SaltedKey> extendedRing = concat(SaltedKey.create(p0, s0), saltedRing);

    return new SignedMessage(message, I, c0, extendedRing);
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
