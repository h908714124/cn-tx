package cn.ecdh;

import cn.wallet.Hash;
import cn.wallet.Key;
import java.util.Arrays;
import org.bouncycastle.math.ec.ECPoint;

public class KeyExchange {

  private final Hash hash;
  private final AESBouncyCastle aes;

  public KeyExchange(Hash hash, AESBouncyCastle aes) {
    this.hash = hash;
    this.aes = aes;
  }

  public byte[] encrypt(Key myKey, ECPoint dest, byte[] plain) {
    byte[] sharedSecret = hash.bytes(dest.multiply(myKey.privateKey()));
    byte[] key = Arrays.copyOf(sharedSecret, 32);
    return aes.encrypt(key, plain);
  }

  public byte[] decrypt(Key myKey, ECPoint source, byte[] encrypted) {
    byte[] sharedSecret = hash.bytes(source.multiply(myKey.privateKey()));
    byte[] key = Arrays.copyOf(sharedSecret, 32);
    return aes.decrypt(key, encrypted);
  }
}
