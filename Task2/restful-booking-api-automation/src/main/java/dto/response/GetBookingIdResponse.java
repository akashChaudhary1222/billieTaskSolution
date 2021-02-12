package main.java.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetBookingIdResponse {
    @JsonProperty("bookingid")
    private int bookingId;

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
}
