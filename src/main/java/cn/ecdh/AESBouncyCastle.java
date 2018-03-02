package cn.ecdh;

import java.nio.charset.StandardCharsets;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.paddings.BlockCipherPadding;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;

public class AESBouncyCastle {

  private final BlockCipherPadding bcp = new PKCS7Padding();

  public byte[] encrypt(byte[] key, byte[] input) {
    String len = Integer.toString(input.length, 16);
    byte[] prefix = (len + '_').getBytes(StandardCharsets.UTF_8);
    byte[] prefixedInput = new byte[prefix.length + input.length];
    System.arraycopy(prefix, 0, prefixedInput, 0, prefix.length);
    System.arraycopy(input, 0, prefixedInput, prefix.length, input.length);
    return processing(key, prefixedInput, true);
  }

  public byte[] decrypt(byte[] key, byte[] input) {
    byte[] prefixed = processing(key, input, false);
    String len = "";
    int i = 0;
    do {
      len += (char) prefixed[i++];
    } while (prefixed[i] != '_');
    byte[] result = new byte[Integer.parseInt(len, 16)];
    System.arraycopy(prefixed, i + 1, result, 0, result.length);
    return result;
  }

  private byte[] processing(byte[] key, byte[] input, boolean encrypt) {

    AESEngine engine = new AESEngine();
    BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(engine, bcp);
    cipher.init(encrypt, new KeyParameter(key));

    byte[] output = new byte[cipher.getOutputSize(input.length)];
    int bytesWrittenOut = cipher.processBytes(
        input, 0, input.length, output, 0);

    try {
      cipher.doFinal(output, bytesWrittenOut);
    } catch (InvalidCipherTextException e) {
      throw new RuntimeException(e);
    }

    return output;
  }
}
