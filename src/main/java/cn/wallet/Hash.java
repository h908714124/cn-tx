package cn.wallet;

import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public final class Hash {

  private final KeccakDigest keccak;

  private final BigInteger q;
  private final BigInteger n;
  private final ECPoint g;

  public Hash(KeccakDigest keccak, BigInteger q, BigInteger n, ECPoint g) {
    this.keccak = keccak;
    this.q = q;
    this.n = n;
    this.g = g;
  }

  public BigInteger fieldHash(ECPoint point) {
    byte[] bytes = point.getEncoded(false);
    keccak.update(bytes, 0, bytes.length);
    int i = q.bitLength();
    while (i % 8 != 0) {
      ++i;
    }
    byte[] out = new byte[i];
    keccak.doFinal(out, 0);
    return new BigInteger(out).mod(q);
  }

  public BigInteger fieldHash(byte[] message, ECPoint a, ECPoint b) {
    byte[] ba = a.getEncoded(false);
    byte[] bb = b.getEncoded(false);
    keccak.update(message, 0, message.length);
    keccak.update(ba, 0, ba.length);
    keccak.update(bb, 0, bb.length);
    int i = q.bitLength();
    while (i % 8 != 0) {
      ++i;
    }
    byte[] out = new byte[i];
    keccak.doFinal(out, 0);
    return new BigInteger(out).mod(q);
  }

  public ECPoint curveHash(ECPoint point) {
    byte[] bytes = point.getEncoded(false);
    keccak.update(bytes, 0, bytes.length);
    int i = n.bitLength();
    while (i % 8 != 0) {
      ++i;
    }
    byte[] out = new byte[i];
    keccak.doFinal(out, 0);
    return g.multiply(new BigInteger(out).mod(n));
  }
}
