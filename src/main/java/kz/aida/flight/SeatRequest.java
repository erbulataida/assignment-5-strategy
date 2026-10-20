package kz.aida.flight;
import java.util.List;
import java.util.Objects;
public record SeatRequest(List<Seat> seats, Seat.Position preferredPosition, boolean needsQuickExit) {
 public SeatRequest { seats = List.copyOf(seats); Objects.requireNonNull(preferredPosition); }
}
