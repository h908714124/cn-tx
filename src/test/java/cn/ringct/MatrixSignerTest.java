package cn.ringct;

import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.wallet.Hash;
import cn.wallet.Key;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.Test;

class MatrixSignerTest {

  private static final ECParameterSpec CURVE =
      ECNamedCurveTable.getParameterSpec("curve25519");

  private final BigInteger x0 =
      number("2de7089f15096ae7d45d6e85fe00669da2a91610097c932a757850f1e65102e");
  private final BigInteger x1 =
      number("9f9c1c2843dbfd8e1e8799a4740d5c26a588d64ee31c05b41538ccadbd4ebac");

  private final KeyVector myKey = new KeyVector(List.of(
      new Key(CURVE.getG(), x0),
      new Key(CURVE.getG(), x1)));

  private final Hash hash = new Hash(
      new KeccakDigest(),
      CURVE.getN(),
      CURVE.getG());

  private final Rand random = new Rand(CURVE.getN());

  private final Linker linker = new Linker(CURVE.getG(), hash);

  private final Verifier verifier = new Verifier(linker);

  private final ECPoint P0 =
      point("210f402a291357551e62eb073b3964e2b00895ee7f877a29024a6b70782f065ce");

  private final ECPoint P1 =
      point("30bd3008e813779cff8f6a629c39ae6bd4a0f3d21b87bc07a14bdb543f2b25bde");

  private final ECPoint Q0 =
      point("30c41b025cd7dbe49ade1f8f3cb2f032e0c35243dcd82055593838bcfcea4bf9f");

  private final ECPoint Q1 =
      point("255a7e973ce20cdf33f4a80d2ef4b9bb9e8805a08b40b814af6be235924c33c12");

  private final Signer signer = new Signer(
      CURVE.getN(),
      hash,
      random,
      linker);

  @Test
  void signAndVerify() {
    String message = "test123";
    KeyMatrix keyMatrix = KeyMatrix.create(List.of(
        List.of(P0, Q0),
        List.of(P1, Q1)));
    SignedMessage signedMessage = signer.sign(message.getBytes(StandardCharsets.UTF_8), myKey, keyMatrix);
    assertTrue(verifier.verify(signedMessage));
  }

  private ECPoint point(String bytes) {
    return CURVE.getCurve().decodePoint(number(bytes).toByteArray());
  }

  private BigInteger number(String bytes) {
    return new BigInteger(bytes, 16);
  }
}
