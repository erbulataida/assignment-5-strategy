package kz.aida.flight;
public final class AdvanceFareStrategy implements FareStrategy {
 public long quote(FareRequest r) {
  long discount = Math.min(r.daysBeforeDeparture(), 60) * 500L;
  return Math.max(0, r.baseFare() - discount) * r.passengers();
 }
}
