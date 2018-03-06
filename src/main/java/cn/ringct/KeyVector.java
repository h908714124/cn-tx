package cn.ringct;

import cn.wallet.Key;
import java.util.List;
import java.util.stream.Collectors;

public final class KeyVector {

  private final List<Key> keys;

  public KeyVector(List<Key> keys) {
    this.keys = keys;
  }

  public PointVector publicKeys() {
    return new PointVector(keys.stream()
        .map(Key::publicKey)
        .collect(Collectors.toList()));
  }

  public NumberVector privateKeys() {
    return new NumberVector(keys.stream()
        .map(Key::privateKey)
        .collect(Collectors.toList()));
  }

  public int length() {
    return keys.size();
  }
}
