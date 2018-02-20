package cn.wallet;

import java.math.BigInteger;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.math.ec.ECPoint;

public final class Hash {

  private final KeccakDigest keccak;

  private final BigInteger q;

  Hash(KeccakDigest keccak, BigInteger q) {
    this.keccak = keccak;
    this.q = q;
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
}
