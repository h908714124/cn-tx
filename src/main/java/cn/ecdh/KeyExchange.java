package cn.ecdh;

import cn.wallet.Hash;
import cn.wallet.Key;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class KeyExchange {

  private final Hash hash;

  private final BigInteger n;

  public KeyExchange(Hash hash, BigInteger n) {
    this.hash = hash;
    this.n = n;
  }

  public BigInteger encrypt(Key myKey, ECPoint dest, BigInteger plain) {
    BigInteger sharedSecret = hash.scalar(dest.multiply(myKey.privateKey()));
    return plain.add(sharedSecret).mod(n);
  }

  public BigInteger decrypt(Key myKey, ECPoint source, BigInteger encrypted) {
    BigInteger sharedSecret = hash.scalar(source.multiply(myKey.privateKey()));
    return encrypted.subtract(sharedSecret).mod(n);
  }
}
