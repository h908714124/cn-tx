package cn.wallet;

import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class Hash {

  private final KeccakDigest keccak;

  private final BigInteger n;

  private final ECPoint g;

  public Hash(KeccakDigest keccak, BigInteger n, ECPoint g) {
    this.keccak = keccak;
    this.n = n;
    this.g = g;
  }

  public BigInteger scalar(ECPoint point) {
    return h(point.getEncoded(false));
  }

  public BigInteger scalar(byte[] message, ECPoint a, ECPoint b) {
    byte[] ba = a.getEncoded(false);
    byte[] bb = b.getEncoded(false);
    return h(message, ba, bb);
  }

  public ECPoint point(ECPoint point) {
    return g.multiply(h(point.getEncoded(false)));
  }

  private BigInteger h(byte[]... data) {
    for (byte[] bytes : data) {
      keccak.update(bytes, 0, bytes.length);
    }
    int i = n.bitLength();
    while (i % 8 != 0) {
      ++i;
    }
    byte[] out = new byte[i];
    keccak.doFinal(out, 0);
    return new BigInteger(out).mod(n);
  }
}
