package kz.aida.flight;
import java.util.Comparator;
import java.util.Optional;
public final class QuickExitSeatStrategy implements SeatStrategy {
 public Optional<Seat> choose(SeatRequest r) {
  return r.seats().stream().filter(Seat::available)
   .min(Comparator.comparingInt(Seat::row)
    .thenComparing((Seat s) -> s.position() != Seat.Position.AISLE).thenComparing(Seat::number));
 }
}
