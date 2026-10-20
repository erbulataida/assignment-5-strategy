package kz.aida.flight;
import java.util.Objects;
import java.util.Optional;
public final class SeatContext {
 private SeatStrategy strategy;
 public SeatContext(SeatStrategy strategy) { setStrategy(strategy); }
 public void setStrategy(SeatStrategy strategy) { this.strategy = Objects.requireNonNull(strategy); }
 public Optional<Seat> choose(SeatRequest request) { return strategy.choose(request); }
}
