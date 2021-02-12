package main.java.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import main.java.dto.BookingDates;

import java.util.Objects;

@JsonPropertyOrder({"firstname", "lastname", "bookingdates", "totalprice", "additionalneeds"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking {

    @JsonProperty("firstname")
    private String firstName;

    @JsonProperty("additionalneeds")
    private String additionalNeeds;

    @JsonProperty("bookingdates")
    private BookingDates bookingDates;

    @JsonProperty("totalprice")
    private int totalPrice;

    @JsonProperty("depositpaid")
    private boolean depositPaid;

    @JsonProperty("lastname")
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public String getAdditionalNeeds() {
        return additionalNeeds;
    }

    public BookingDates getBookingDates() {
        return bookingDates;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public boolean isDepositPaid() {
        return depositPaid;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return totalPrice == booking.totalPrice && depositPaid == booking.depositPaid && firstName.equals(booking.firstName) && additionalNeeds.equals(booking.additionalNeeds) && bookingDates.equals(booking.bookingDates) && lastName.equals(booking.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, additionalNeeds, bookingDates, totalPrice, depositPaid, lastName);
    }
}