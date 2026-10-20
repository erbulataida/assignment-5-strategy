package kz.aida.flight;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class FareBehaviorTest {
 private final DemandSource fake = () -> 0.5;
 @Test void standardChargesPerPassenger() { assertEquals(200_000, new FareContext("standard", fake).quote(new FareRequest(100_000, 30, 2))); }
 @Test void advanceUsesDaysRemaining() { assertEquals(170_000, new FareContext("advance", fake).quote(new FareRequest(100_000, 30, 2))); }
 @Test void advanceCapsDaysAtSixty() { assertEquals(70_000, new FareContext("advance", fake).quote(new FareRequest(100_000, 90, 1))); }
 @Test void advanceNeverDropsBelowZero() { assertEquals(0, new FareContext("advance", fake).quote(new FareRequest(1000, 30, 1))); }
 @Test void demandUsesInjectedOccupancy() { assertEquals(250_000, new FareContext("demand", fake).quote(new FareRequest(100_000, 30, 2))); }
 @Test void invalidInputRejected() { assertThrows(IllegalArgumentException.class, () -> new FareRequest(-1, 2, 1)); }
}
