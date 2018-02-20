package cn.wallet;

import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public final class Hash {

  private final KeccakDigest keccak;

  private final BigInteger n;
  private final ECPoint g;

  public Hash(KeccakDigest keccak, BigInteger n, ECPoint g) {
    this.keccak = keccak;
    this.n = n;
    this.g = g;
  }

  public BigInteger exponentHash(ECPoint point) {
    return bigIntHash(keccak, point, n);
  }

  private static BigInteger bigIntHash(
      KeccakDigest keccak, ECPoint point, BigInteger mod) {
    byte[] bytes = point.getEncoded(false);
    keccak.update(bytes, 0, bytes.length);
    int i = mod.bitLength();
    while (i % 8 != 0) {
      ++i;
    }
    byte[] out = new byte[i];
    keccak.doFinal(out, 0);
    return new BigInteger(out).mod(mod);
  }

  public BigInteger fieldHash(byte[] message, ECPoint a, ECPoint b) {
    byte[] ba = a.getEncoded(false);
    byte[] bb = b.getEncoded(false);
    keccak.update(message, 0, message.length);
    keccak.update(ba, 0, ba.length);
    keccak.update(bb, 0, bb.length);
    int i = n.bitLength();
    while (i % 8 != 0) {
      ++i;
    }
    byte[] out = new byte[i];
    keccak.doFinal(out, 0);
    return new BigInteger(out).mod(n);
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
