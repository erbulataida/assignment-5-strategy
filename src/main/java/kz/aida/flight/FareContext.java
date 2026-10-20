package kz.aida.flight;
import java.util.Objects;
public final class FareContext {
 private String mode;
 private final DemandSource source;
 public FareContext(String mode, DemandSource source) { this.mode = Objects.requireNonNull(mode); this.source = Objects.requireNonNull(source); }
 public long quote(FareRequest r) {
  return switch (mode) {
   case "standard" -> r.baseFare() * r.passengers();
   case "advance" -> Math.max(0, r.baseFare() - Math.min(r.daysBeforeDeparture(), 60) * 500L) * r.passengers();
   case "demand" -> Math.round(r.baseFare() * (1 + Math.pow(source.occupancy(), 2))) * r.passengers();
   default -> throw new IllegalArgumentException("Unknown fare mode");
  };
 }
}
