package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.user.model.UserShort;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Valid
@Getter
@Setter
@ToString
public class Booking {

    private Long id;

    @NotNull(message = "Booking should include item")
    private ItemShort item;

    @NotNull(message = "Booking should include booker")
    private UserShort booker;

    @Future(message = "Booking start should be in the future")
    @NotNull(message = "Booking start should not be Null")
    private LocalDateTime start;

    @Future(message = "Booking end should be in the future")
    @NotNull(message = "Booking end should not be Null")
    private LocalDateTime end;

    private BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        return id != null && id.equals(((Booking) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}