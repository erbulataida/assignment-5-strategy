package kz.aida.flight;
import java.util.Objects;
public final class DemandFareStrategy implements FareStrategy {
 private final DemandSource source;
 public DemandFareStrategy(DemandSource source) { this.source = Objects.requireNonNull(source); }
 public long quote(FareRequest r) {
  double occupancy = source.occupancy();
  if (!Double.isFinite(occupancy) || occupancy < 0 || occupancy > 1) throw new IllegalArgumentException("Invalid source occupancy");
  return Math.round(r.baseFare() * (1 + occupancy * occupancy)) * r.passengers();
 }
}
