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
import java.util.stream.Stream;

public final class RingCt {

  private final ECPoint g;

  private final BigInteger l;

  private final BigInteger q;

  private final List<ECPoint> ring;

  private final Key myKey;

  private final Hash hash;

  private final Stepper stepper;

  public RingCt(
      ECPoint g,
      BigInteger l,
      BigInteger q,
      List<ECPoint> ring,
      Key myKey,
      Hash hash,
      Stepper stepper) {
    this.g = g;
    this.l = l;
    this.q = q;
    this.ring = ring;
    this.myKey = myKey;
    this.hash = hash;
    this.stepper = stepper;
  }

  public SignedMessage sign(byte[] message) {

    List<BigInteger> s = Stream.generate(this::random).limit(ring.size()).collect(Collectors.toList());

    BigInteger alpha = random();
    ECPoint p1 = myKey.publicKey();
    ECPoint I = hash.curveHash(p1).multiply(myKey.privateKey());

    ECPoint L_init = g.multiply(alpha);
    ECPoint R_init = hash.curveHash(p1).multiply(alpha);
    BigInteger c_init = hash.fieldHash(message, L_init, R_init);

    SigStep prev = new SigStep(L_init, R_init, c_init);
    List<SigStep> steps = new ArrayList<>(s.size());
    for (int i = 0; i < ring.size(); ++i) {
      BigInteger si = s.get(i);
      SigStep next = stepper.step(I, message, prev, ring.get(i), si);
      steps.add(next);
      prev = next;
    }

    ArrayList<BigInteger> c = new ArrayList<>();
    for (int i = 0; i < steps.size(); i++) {
      SigStep step = steps.get(Math.floorMod(i - 1, ring.size()));
      c.add(step.cppi());
    }

    BigInteger c0 = steps.get(ring.size() - 1).cppi();
    BigInteger s0 = alpha.subtract(c0.multiply(myKey.privateKey())).mod(l);

    List<BigInteger> ss = new ArrayList<>(s.size() + 1);
    ss.add(s0);
    ss.addAll(s);

    List<ECPoint> rring = new ArrayList<>(ring.size() + 1);
    rring.add(myKey.publicKey());
    rring.addAll(ring);

    return new SignedMessage(message, I, c0, ss, rring);
  }

  private BigInteger random() {
    Random rnd = ThreadLocalRandom.current();
    BigInteger r;
    do {
      r = new BigInteger(q.bitLength(), rnd);
    } while (r.compareTo(q) >= 0);
    return r;
  }
}