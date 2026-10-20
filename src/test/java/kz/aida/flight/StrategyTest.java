package kz.aida.flight;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;
class StrategyTest {
 static Stream<FareStrategy> fares() { return Stream.of(new StandardFareStrategy(), new AdvanceFareStrategy(), new DemandFareStrategy(() -> 0.5)); }
 @ParameterizedTest @MethodSource("fares") void fareNeverNegative(FareStrategy s) {
  for (long base : new long[]{0, 1000, 100_000})
   for (int days : new int[]{0, 3, 30, 90}) assertTrue(s.quote(new FareRequest(base, days, 2)) >= 0);
 }
 @ParameterizedTest @CsvSource({"0,DemandFareStrategy", "3,DemandFareStrategy", "4,StandardFareStrategy", "29,StandardFareStrategy", "30,AdvanceFareStrategy", "90,AdvanceFareStrategy"})
 void fareSelectorBoundaries(int days, String expected) {
  assertEquals(expected, new FareSelector(() -> 0.5).select(new FareRequest(1000, days, 1)).getClass().getSimpleName());
 }
 static List<Seat> seats() { return List.of(new Seat("2B",2,Seat.Position.MIDDLE,true),new Seat("5C",5,Seat.Position.AISLE,true),new Seat("12A",12,Seat.Position.WINDOW,true),new Seat("1C",1,Seat.Position.AISLE,false)); }
 static SeatRequest request(Seat.Position p, boolean quick) { return new SeatRequest(seats(),p,quick); }
 @Test void windowChoosesRearWindow() { assertEquals("12A",new WindowSeatStrategy().choose(request(Seat.Position.WINDOW,false)).orElseThrow().number()); }
 @Test void aisleChoosesAvailableAisle() { assertEquals("5C",new AisleSeatStrategy().choose(request(Seat.Position.AISLE,false)).orElseThrow().number()); }
 @Test void quickExitPrioritizesRow() { assertEquals("2B",new QuickExitSeatStrategy().choose(request(Seat.Position.WINDOW,true)).orElseThrow().number()); }
 @ParameterizedTest @CsvSource({"WINDOW,false,WindowSeatStrategy", "AISLE,false,AisleSeatStrategy", "MIDDLE,false,WindowSeatStrategy", "WINDOW,true,QuickExitSeatStrategy", "AISLE,true,QuickExitSeatStrategy"})
 void seatSelectorRules(Seat.Position preference, boolean quick, String expected) { assertEquals(expected,new SeatSelector().select(request(preference,quick)).getClass().getSimpleName()); }
 static Stream<SeatStrategy> seating() { return Stream.of(new WindowSeatStrategy(),new AisleSeatStrategy(),new QuickExitSeatStrategy()); }
 @ParameterizedTest @MethodSource("seating") void unavailableSeatsNeverChosen(SeatStrategy s) {
  SeatRequest r = request(Seat.Position.WINDOW,false);
  assertTrue(s.choose(r).orElseThrow().available());
  assertTrue(s.choose(new SeatRequest(List.of(),Seat.Position.WINDOW,false)).isEmpty());
  assertTrue(s.choose(new SeatRequest(List.of(new Seat("1A",1,Seat.Position.WINDOW,false)),Seat.Position.WINDOW,false)).isEmpty());
 }
 @Test void manualLookupRejectsUnknown() { assertThrows(IllegalArgumentException.class, () -> new FareSelector(() -> 0).byName("unknown")); }
 @Test void fareCanChangeAtRuntime() { FareContext c = new FareContext(new StandardFareStrategy()); FareRequest r = new FareRequest(100000,30,1); assertEquals(100000,c.quote(r)); c.setStrategy(new AdvanceFareStrategy()); assertEquals(85000,c.quote(r)); }
 @Test void seatCanChangeAtRuntime() { SeatContext c = new SeatContext(new WindowSeatStrategy()); assertEquals("12A",c.choose(request(Seat.Position.WINDOW,false)).orElseThrow().number()); c.setStrategy(new QuickExitSeatStrategy()); assertEquals("2B",c.choose(request(Seat.Position.WINDOW,false)).orElseThrow().number()); }
 @Test void fakeDataSourceChangesDemandResult() { FareRequest r = new FareRequest(100000,1,1); assertEquals(100000,new DemandFareStrategy(() -> 0).quote(r)); assertEquals(200000,new DemandFareStrategy(() -> 1).quote(r)); }
 @Test void invalidExternalDataRejected() { assertThrows(IllegalArgumentException.class, () -> new DemandFareStrategy(() -> Double.NaN).quote(new FareRequest(1000,1,1))); }
 @Test void windowFallsBackWhenMissing() { assertEquals("5C",new WindowSeatStrategy().choose(new SeatRequest(List.of(seats().get(0),seats().get(1)),Seat.Position.WINDOW,false)).orElseThrow().number()); }
 @Test void aisleFallsBackWhenMissing() { assertEquals("2B",new AisleSeatStrategy().choose(new SeatRequest(List.of(seats().get(0),seats().get(2)),Seat.Position.AISLE,false)).orElseThrow().number()); }
}
