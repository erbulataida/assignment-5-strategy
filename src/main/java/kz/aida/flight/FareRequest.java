package kz.aida.flight;

public record FareRequest(long baseFare, int daysBeforeDeparture, int passengers) {
 public FareRequest {
  if (baseFare < 0 || baseFare > 1_000_000_000L || daysBeforeDeparture < 0 || passengers < 1 || passengers > 100)
   throw new IllegalArgumentException("Invalid fare request");
 }
}
