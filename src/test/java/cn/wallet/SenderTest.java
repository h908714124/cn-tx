package cn.wallet;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import cn.ringct.KeyMatrix;
import cn.ringct.KeyVector;
import cn.ringct.Linker;
import cn.ringct.Rand;
import cn.ringct.SignedMessage;
import cn.ringct.Signer;
import cn.ringct.Verifier;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.Test;

class SenderTest {

  private static final ECParameterSpec CURVE =
      ECNamedCurveTable.getParameterSpec("curve25519");

  private final Hash hash = new Hash(
      new KeccakDigest(),
      CURVE.getN(),
      CURVE.getG());

  private final Rand random = new Rand(CURVE.getN());

  private final Linker linker = new Linker(CURVE.getG(), hash);

  private final Signer signer = new Signer(
      CURVE.getN(),
      hash,
      random,
      linker);

  private final Verifier verifier = new Verifier(linker);

  private final Sender sender = new Sender(
      CURVE.getN(),
      CURVE.getG(),
      hash);

  private final ECPoint P0 =
      point("4346f4b8d3e395a5a0c81c2241dd3c1df68233eacd9ad7b2ceaea72d81d7b4769216e49dc4140a82ff7559400c2ee1a35022f0161ea7032c4eb6c9d3a12e083cf");

  private final ECPoint P1 =
      point("42852e1dcc22765a75474aaa5614f2537d6dacdc96b406d2bc1b0bc846dd2ba3d7ddb053f77508282fde3c78c0ca339dafe6659a77e3c4fd878b68cf170ccce68");

  private final PrivateUserKey x = PrivateUserKey.create(CURVE.getG(),
      "822a7cae72d2597db07c5fe92f6544161ef134a2ae57c827977a03618fcea46",
      "ac0392e284c97fdc39af08528b567b05c8772d108ee6f417cab5ec26d4d4298");

  private final Receiver receiver = new Receiver(CURVE.getG(), x, hash);

  @Test
  void sendAndReceive() {
    PublicUserKey dest = x.publicKey();
    Transaction transaction = sender.send(dest);
    Optional<Key> spendkey = receiver.check(transaction);
    assertTrue(spendkey.isPresent());
  }

  @Test
  void receiveAndSpend() {
    PublicUserKey dest = x.publicKey();
    Transaction transaction = sender.send(dest);
    receiver.check(transaction)
        .map(List::of)
        .map(KeyVector::new)
        .ifPresentOrElse(key -> {
          String message = "test123";
          SignedMessage signedMessage = signer.sign(message.getBytes(UTF_8), key, KeyMatrix.create(List.of(List.of(P0, P1))));
          assertTrue(verifier.verify(signedMessage));
        }, () -> fail("no money"));
  }

  private ECPoint point(String bytes) {
    return CURVE.getCurve().decodePoint(number(bytes).toByteArray());
  }

  private BigInteger number(String bytes) {
    return new BigInteger(bytes, 16);
  }
}
