package com.sportygroup.bet.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Domain value object: sports event outcome (Event ID, Event Name, Event Winner ID).
 */
@Getter
@Setter
@AllArgsConstructor
public final class EventOutcome {

    private final String eventId;
    private final String eventName;
    private final String eventWinnerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventOutcome that = (EventOutcome) o;
        return eventId.equals(that.eventId) && eventName.equals(that.eventName) && eventWinnerId.equals(that.eventWinnerId);
    }

    @Override
    public int hashCode() { return Objects.hash(eventId, eventName, eventWinnerId); }
}
