package kz.aida.flight;
import java.util.Objects;
public final class FareSelector {
 private final DemandSource source;
 public FareSelector(DemandSource source) { this.source = Objects.requireNonNull(source); }
 public FareStrategy select(FareRequest r) {
  if (r.daysBeforeDeparture() <= 3) return new DemandFareStrategy(source);
  if (r.daysBeforeDeparture() >= 30) return new AdvanceFareStrategy();
  return new StandardFareStrategy();
 }
 
 public FareStrategy byName(String name) {
  return switch (name) {
   case "standard" -> new StandardFareStrategy();
   case "advance" -> new AdvanceFareStrategy();
   case "demand" -> new DemandFareStrategy(source);
   default -> throw new IllegalArgumentException("Unknown fare mode: " + name);
  };
 }
}
