package cn.wallet;

import java.math.BigInteger;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.math.ec.ECPoint;

public class Hash {

  private final KeccakDigest keccak;

  private final BigInteger n;

  private final ECPoint G;

  public Hash(KeccakDigest keccak, BigInteger n, ECPoint G) {
    this.keccak = keccak;
    this.n = n;
    this.G = G;
  }

  public BigInteger scalar(ECPoint A) {
    return h(A.getEncoded(false));
  }

  public byte[] bytes(ECPoint A) {
    return hb(new byte[][]{A.getEncoded(false)});
  }

  public BigInteger scalar(byte[] bytes) {
    return h(bytes);
  }

  public BigInteger scalar(byte[] message, ECPoint A, ECPoint B) {
    return h(message, A.getEncoded(false), B.getEncoded(false));
  }

  public ECPoint point(ECPoint A) {
    return G.multiply(h(A.getEncoded(false)));
  }

  private BigInteger h(byte[]... data) {
    byte[] out = hb(data);
    return new BigInteger(out).mod(n);
  }

  private byte[] hb(byte[][] data) {
    for (byte[] bytes : data) {
      keccak.update(bytes, 0, bytes.length);
    }
    int i = n.bitLength();
    while (i % 8 != 0) {
      ++i;
    }
    byte[] out = new byte[i];
    keccak.doFinal(out, 0);
    return out;
  }
}
