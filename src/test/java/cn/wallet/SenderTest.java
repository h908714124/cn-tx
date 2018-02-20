package cn.wallet;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.bouncycastle.crypto.digests.KeccakDigest;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.junit.jupiter.api.Test;

class SenderTest {

  private static final ECNamedCurveParameterSpec CURVE =
      ECNamedCurveTable.getParameterSpec("curve25519");

  private final Hash hash = new Hash(
      new KeccakDigest(),
      CURVE.getCurve().getField().getCharacteristic());

  private final Sender sender = new Sender(
      CURVE.getN(),
      CURVE.getG(),
      hash);

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

  private BigInteger keygen() {
    Random rnd = ThreadLocalRandom.current();
    BigInteger n = CURVE.getN();
    BigInteger r;
    do {
      r = new BigInteger(n.bitLength(), rnd);
    } while (r.compareTo(n) >= 0);
    return r;
  }
}
