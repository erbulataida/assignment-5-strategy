package kz.aida.flight;
import java.util.Objects;
public final class FareContext {
 private FareStrategy strategy;
 public FareContext(FareStrategy strategy) { setStrategy(strategy); }
 
 public FareContext(String name, DemandSource source) { this(new FareSelector(source).byName(name)); }
 public void setStrategy(FareStrategy strategy) { this.strategy = Objects.requireNonNull(strategy); }
 public long quote(FareRequest request) { return strategy.quote(request); }
}
