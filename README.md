# Assignment 5 — Strategy Pattern

Ербулат Аида  
SE-2536

## Domain

Flight booking: fare calculation and seat selection.

## Run

Java 17 or newer, Maven.

```bash
mvn test
bash run.sh
```

## Strategy families

| Family | Strategies | Context | Selector |
|---|---|---|---|
| Fare calculation | Standard, Advance, Demand | FareContext | FareSelector |
| Seat selection | Window, Aisle, Quick Exit | SeatContext | SeatSelector |

Standard pricing multiplies base fare by passenger count. Advance pricing subtracts 500 KZT per day, capped at 60 days, without producing negative prices. Demand pricing uses base fare × (1 + occupancy²), rounded per passenger.

FareSelector chooses Demand for 0–3 days before departure, Standard for 4–29 days, Advance for 30 or more days.

Window selection prioritizes windows followed by rear rows. Aisle selection prioritizes aisles followed by front rows. Quick Exit prioritizes front rows followed by aisles. Unavailable seats are excluded. Seat number breaks ties. The aircraft model assumes a front exit. If no seat is available, the result is Optional.empty().

SeatSelector prioritizes a quick-exit requirement, then an aisle preference, otherwise a window strategy. Middle preference uses the window fallback policy.

DemandSource isolates occupancy data. SimulatedDemandSource supplies demonstration values. Tests inject fixed fake sources.

Main processes identical inputs with each strategy, compares manual selection with automatic selection, then replaces strategies on existing contexts.

## Responsibilities (SRP)

| Class / interface | Responsibility |
|---|---|
| FareRequest | Stores validated fare inputs. |
| DemandSource | Provides current flight occupancy. |
| SimulatedDemandSource | Supplies a fixed simulated occupancy. |
| FareStrategy | Defines a replaceable fare calculation contract. |
| StandardFareStrategy | Calculates the base fare per passenger. |
| AdvanceFareStrategy | Subtracts a capped discount based on booking lead time. |
| DemandFareStrategy | Calculates an occupancy-based quadratic surcharge. |
| FareContext | Delegates fare calculation to its current strategy. |
| FareSelector | Selects a fare algorithm from booking conditions. |
| Seat | Stores one seat’s location plus availability. |
| SeatRequest | Stores an immutable snapshot of seat selection inputs. |
| SeatStrategy | Defines a replaceable seat selection contract. |
| WindowSeatStrategy | Prioritizes windows followed by rear rows. |
| AisleSeatStrategy | Prioritizes aisles followed by front rows. |
| QuickExitSeatStrategy | Prioritizes front rows followed by aisles. |
| SeatContext | Delegates seat selection to its current strategy. |
| SeatSelector | Selects a seat algorithm from passenger needs. |
| Main | Demonstrates manual selection, automatic selection, runtime replacement. |
| FareBehaviorTest | Verifies stable fare behavior across refactoring. |
| StrategyTest | Verifies selection boundaries, algorithm invariants, runtime replacement. |

## Why Strategy, rather than State or Template Method

The changing behavior in this project is an algorithm selected for a task. A fare request can be processed by standard, advance-booking, or demand pricing. A seat request can be processed by window, aisle, or quick-exit selection. Each family has a common interface, so its context can delegate without knowing the concrete implementation. The caller can replace an algorithm through `setStrategy()` while keeping the same context instance. The demo applies every strategy to identical input so the differences are observable.

This fits Strategy because the algorithms are interchangeable policies. They do not represent lifecycle stages. In a State design, behavior would depend on an object's internal state, such as a booking being pending, confirmed, or cancelled, often with transitions between states. Here a fare strategy does not transition the booking or choose the next strategy; selection belongs to the client or a separate selector.

Template Method would define a fixed algorithm skeleton in a superclass and let subclasses override particular steps. This project has no shared superclass controlling such a skeleton. It uses composition: a context holds an interface reference and delegates the complete operation. Runtime replacement therefore does not require replacing the context itself.

A new implementation can be introduced and passed directly to a context without editing existing strategies or contexts. An automatic selection policy may intentionally require a selector rule update. This is a change to selection policy, not to the existing algorithms. The string-based constructor delegates name lookup to `FareSelector`.

Conditional expressions inside algorithms handle their own data, such as invalid occupancy. They never inspect a strategy's type. Contexts contain no type-based branching. Selectors own all branching that chooses a strategy.

## Refactoring history

- [Before refactoring](https://github.com/erbulataida/assignment-5-flight-strategy/tree/before-refactoring)
- [After refactoring](https://github.com/erbulataida/assignment-5-flight-strategy/tree/after-refactoring)

The before version calculates fares with switch. The after version delegates to interchangeable strategy objects. FareBehaviorTest remains unchanged across both versions.

## Tests

JUnit 5 tests cover every strategy, selector rules, runtime replacement, fake occupancy data, empty seat lists, unavailable seats, fallback selection, invalid input. Parameterized tests check nonnegative prices across the fare family plus availability across the seat family.
