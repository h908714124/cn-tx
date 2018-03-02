package cn.ecdh;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import cn.wallet.Hash;
import cn.wallet.Key;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.junit.jupiter.api.Test;

class KeyExchangeTest {

  private static final ECNamedCurveParameterSpec CURVE =
      ECNamedCurveTable.getParameterSpec("curve25519");

  private final KeccakDigest keccak = new KeccakDigest();

  private final Hash hash = new Hash(keccak, CURVE.getN(), CURVE.getG());

  private final AESBouncyCastle aes = new AESBouncyCastle();

  private final KeyExchange keyExchange = new KeyExchange(hash, aes);

  private final BigInteger x =
      number("2de7089f15096ae7d45d6e85fe00669da2a91610097c932a757850f1e65102e");

  private final BigInteger y =
      number("6f33f8a08a87fb605e6fcc40b0a590fa064ebcdf46351a68caf966571253531");

  private final Key alice = new Key(CURVE.getG(), x);

  private final Key bob = new Key(CURVE.getG(), y);

  @Test
  void encryptDecrypt() {
    byte[] message = "test123".getBytes(StandardCharsets.UTF_8);
    byte[] encrypted = keyExchange.encrypt(alice, bob.publicKey(), message);
    byte[] decrypted = keyExchange.decrypt(bob, alice.publicKey(), encrypted);
    assertArrayEquals(message, decrypted);
  }

  private BigInteger number(String bytes) {
    return new BigInteger(bytes, 16);
  }
}
