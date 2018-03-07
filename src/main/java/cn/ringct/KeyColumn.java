package cn.ringct;

import cn.wallet.Key;
import java.util.List;
import java.util.stream.Collectors;

public final class KeyColumn {

  private final List<Key> keys;

  public KeyColumn(List<Key> keys) {
    this.keys = keys;
  }

  public PointColumn publicKeys() {
    return new PointColumn(keys.stream()
        .map(Key::publicKey)
        .collect(Collectors.toList()));
  }

  public NumberColumn privateKeys() {
    return new NumberColumn(keys.stream()
        .map(Key::privateKey)
        .collect(Collectors.toList()));
  }

  public int height() {
    return keys.size();
  }
}
