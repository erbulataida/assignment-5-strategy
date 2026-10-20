package kz.aida.flight;
public final class SimulatedDemandSource implements DemandSource {
 private final double occupancy;
 public SimulatedDemandSource(double occupancy) {
  if (!Double.isFinite(occupancy) || occupancy < 0 || occupancy > 1) throw new IllegalArgumentException("Occupancy must be 0..1");
  this.occupancy = occupancy;
 }
 public double occupancy() { return occupancy; }
}
