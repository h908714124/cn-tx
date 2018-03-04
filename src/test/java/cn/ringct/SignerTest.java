package cn.ringct;

import cn.wallet.Hash;
import cn.wallet.Key;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SignerTest {

  private static final ECParameterSpec CURVE =
      ECNamedCurveTable.getParameterSpec("curve25519");

  private final BigInteger x =
      number("2de7089f15096ae7d45d6e85fe00669da2a91610097c932a757850f1e65102e");

  private final Key myKey = new Key(CURVE.getG(), x);

  private final Hash hash = new Hash(
      new KeccakDigest(),
      CURVE.getN(),
      CURVE.getG());

  private final Rand random = new Rand(CURVE.getN());

  private final Linker linker = new Linker(CURVE.getG(), random, hash);

  private final Verifier verifier = new Verifier(linker);

  private final ECPoint P0 =
      point("4346f4b8d3e395a5a0c81c2241dd3c1df68233eacd9ad7b2ceaea72d81d7b4769216e49dc4140a82ff7559400c2ee1a35022f0161ea7032c4eb6c9d3a12e083cf");

  private final ECPoint P1 =
      point("42852e1dcc22765a75474aaa5614f2537d6dacdc96b406d2bc1b0bc846dd2ba3d7ddb053f77508282fde3c78c0ca339dafe6659a77e3c4fd878b68cf170ccce68");

  private final Signer signer = new Signer(
      CURVE.getN(),
      hash,
      random,
      linker);

  @Test
  void signAndVerify() {
    String message = "test123";
    SignedMessage signedMessage = signer.sign(message.getBytes(UTF_8), myKey, List.of(P0, P1));
    assertTrue(verifier.verify(signedMessage));
  }

  private ECPoint point(String bytes) {
    return CURVE.getCurve().decodePoint(number(bytes).toByteArray());
  }

  private BigInteger number(String bytes) {
    return new BigInteger(bytes, 16);
  }
}
