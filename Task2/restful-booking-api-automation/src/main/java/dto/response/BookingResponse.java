package main.java.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BookingResponse {

    @JsonProperty("booking")
    private Booking booking;

    @JsonProperty("bookingid")
    private int bookingId;

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getBookingId() {
        return bookingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingResponse that = (BookingResponse) o;
        return bookingId == that.bookingId && booking.equals(that.booking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(booking, bookingId);
    }
}