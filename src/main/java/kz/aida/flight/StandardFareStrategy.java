package kz.aida.flight;
public final class StandardFareStrategy implements FareStrategy {
 public long quote(FareRequest r) { return r.baseFare() * r.passengers(); }
}
