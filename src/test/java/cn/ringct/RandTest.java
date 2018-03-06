package cn.ringct;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.junit.jupiter.api.Test;

class RandTest {

  private static final ECParameterSpec CURVE =
      ECNamedCurveTable.getParameterSpec("curve25519");

  private final Rand random = new Rand(CURVE.getN());

  @Test
  void spin() {
    List<String> abc = new ArrayList<>(List.of("a", "b", "c"));
    random.spin(abc, 0);
    assertEquals(List.of("a", "b", "c"), abc);
    abc = new ArrayList<>(List.of("a", "b", "c"));
    random.spin(abc, 1);
    assertEquals(List.of("b", "c", "a"), abc);
    abc = new ArrayList<>(List.of("a", "b", "c"));
    random.spin(abc, 2);
    assertEquals(List.of("c", "a", "b"), abc);
    abc = new ArrayList<>(List.of("a", "b", "c"));
    random.spin(abc, 3);
    assertEquals(List.of("a", "b", "c"), abc);
  }
}
