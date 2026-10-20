package kz.aida.flight;
import java.util.Optional;
public interface SeatStrategy { Optional<Seat> choose(SeatRequest request); }
