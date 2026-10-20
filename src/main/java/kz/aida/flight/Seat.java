package kz.aida.flight;
import java.util.Objects;
public record Seat(String number, int row, Position position, boolean available) {
 public enum Position { WINDOW, MIDDLE, AISLE }
 public Seat {
  Objects.requireNonNull(number); Objects.requireNonNull(position);
  if (number.isBlank() || row < 1) throw new IllegalArgumentException("Invalid seat");
 }
}
