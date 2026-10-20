package kz.aida.flight;
import java.util.List;
public final class Main {
 public static void main(String[] args) {
  DemandSource demand = new SimulatedDemandSource(0.5);
  FareRequest fare = new FareRequest(100_000, 30, 2);
  FareContext pricing = new FareContext(new StandardFareStrategy());
  System.out.println("SAME FARE INPUT: base=100000 KZT, days=30, passengers=2");
  for (FareStrategy s : List.of(new StandardFareStrategy(), new AdvanceFareStrategy(), new DemandFareStrategy(demand))) {
   pricing.setStrategy(s);
   System.out.println(s.getClass().getSimpleName() + " -> " + pricing.quote(fare) + " KZT");
  }
  pricing.setStrategy(new StandardFareStrategy());
  System.out.println("Manual standard -> " + pricing.quote(fare));
  pricing.setStrategy(new FareSelector(demand).select(fare));
  System.out.println("Automatic, SAME context -> " + pricing.quote(fare));
  SeatRequest seats = new SeatRequest(List.of(
   new Seat("2B", 2, Seat.Position.MIDDLE, true),
   new Seat("5C", 5, Seat.Position.AISLE, true),
   new Seat("12A", 12, Seat.Position.WINDOW, true),
   new Seat("1C", 1, Seat.Position.AISLE, false)), Seat.Position.AISLE, false);
  SeatContext seating = new SeatContext(new WindowSeatStrategy());
  System.out.println("SAME SEAT INPUT:");
  for (SeatStrategy s : List.of(new WindowSeatStrategy(), new AisleSeatStrategy(), new QuickExitSeatStrategy())) {
   seating.setStrategy(s);
   System.out.println(s.getClass().getSimpleName() + " -> " + seating.choose(seats).map(Seat::number).orElse("NONE"));
  }
  seating.setStrategy(new WindowSeatStrategy());
  System.out.println("Manual window -> " + seating.choose(seats).orElseThrow().number());
  seating.setStrategy(new SeatSelector().select(seats));
  System.out.println("Automatic, SAME context -> " + seating.choose(seats).orElseThrow().number());
 }
}
