package kz.aida.flight;
public final class SeatSelector {
 public SeatStrategy select(SeatRequest r) {
  if (r.needsQuickExit()) return new QuickExitSeatStrategy();
  if (r.preferredPosition() == Seat.Position.AISLE) return new AisleSeatStrategy();
  return new WindowSeatStrategy();
 }
}
