package com.restful.booker.crudtest;


import com.restful.booker.model.BookingPojo;
import com.restful.booker.steps.BookingSteps;
import com.restful.booker.testbase.BaseTest;
import com.restful.booker.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;


@RunWith(SerenityRunner.class)
public class BookingCRUDTest extends BaseTest {
    static String firstname = "Diuesh" + TestUtils.getRandomValue();
    static String lastname = "Roman" + TestUtils.getRandomValue();
    static int totalprice = Integer.parseInt(1 + TestUtils.getRandomValue());
    static boolean depositpaid = true;
    static String additionalneeds = "Non-Veg. Food";
    static String token;
    static int id;

    @Steps
    BookingSteps bookingSteps;

    @Title(" create a Token")
    @Test
    public void test001() {
        ValidatableResponse response = bookingSteps.getToken().statusCode(200);
        token = response.extract().path("token");
    }

    @Title(" create a booking")
    @Test
    public void test002() {
        BookingPojo.BookingDates bookingdates = new BookingPojo.BookingDates();
        bookingdates.setCheckin("2023-11-01");
        bookingdates.setCheckout("2023-12-01");
        ValidatableResponse response = bookingSteps.createBooking(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds).statusCode(200);
        id = response.extract().path("bookingid");
    }

    @Title(" verify new Booking ID creation")
    @Test
    public void test003() {
        ValidatableResponse response = bookingSteps.getBookingInfoByID();
        ArrayList<?> booking = response.extract().path("bookingid");
        Assert.assertTrue(booking.contains(id));
    }

    @Title(" get booking with Id")
    @org.junit.Test
    public void test004() {
   bookingSteps.getSingleBookingIDs(id).statusCode(200);
    }

    @Title(" updated a booking with ID")
    @Test
    public void test005() {
        additionalneeds = "Veg. Food";
        BookingPojo.BookingDates bookingdates = new BookingPojo.BookingDates();
        bookingdates.setCheckin("2023-11-01");
        bookingdates.setCheckout("2023-12-01");
       BookingSteps.updateBookingWithID(id, token, firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
        ValidatableResponse response = bookingSteps.getSingleBookingIDs(id);
        HashMap<String, ?> update = response.extract().path("");
        Assert.assertThat(update, hasValue("Veg. Food"));
    }

    @Title("delete a booking with ID")
    @Test
    public void test006() {
    bookingSteps.deleteABookingID(id, token).statusCode(201);
     bookingSteps.getSingleBookingIDs(id).statusCode(404);
    }
}
