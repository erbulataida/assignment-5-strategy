package kz.aida.flight;
import java.util.Comparator;
import java.util.Optional;
public final class AisleSeatStrategy implements SeatStrategy {
 public Optional<Seat> choose(SeatRequest r) {
  return r.seats().stream().filter(Seat::available)
   .min(Comparator.comparing((Seat s) -> s.position() != Seat.Position.AISLE)
    .thenComparingInt(Seat::row).thenComparing(Seat::number));
 }
}
