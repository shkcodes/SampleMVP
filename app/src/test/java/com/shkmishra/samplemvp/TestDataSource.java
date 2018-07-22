package com.shkmishra.samplemvp;

import com.shkmishra.samplemvp.data.ApiResponse;
import com.shkmishra.samplemvp.data.Group;
import com.shkmishra.samplemvp.data.Item;
import com.shkmishra.samplemvp.data.Location;
import com.shkmishra.samplemvp.data.ResponseBody;
import com.shkmishra.samplemvp.data.Restaurant;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class TestDataSource {

    public static Observable<ApiResponse> getFakeResponse() {

        List<Group> groups = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        items.add(new Item(new Restaurant("1", "Name 1", new Location("Address goes here", "Sector 89", 350, 0.0, 0.0))));

        groups.add(new Group(items));

        ResponseBody responseBody = new ResponseBody(groups);
        ApiResponse apiResponse = new ApiResponse(responseBody);
        return Observable.just(apiResponse);
    }

    public static Observable<ApiResponse> getEmptyResponse() {

        List<Group> groups = new ArrayList<>();
        List<Item> items = new ArrayList<>();

        groups.add(new Group(items));

        ResponseBody responseBody = new ResponseBody(groups);
        ApiResponse apiResponse = new ApiResponse(responseBody);
        return Observable.just(apiResponse);
    }

    public static List<Restaurant> getVenues() {

        List<Restaurant> restaurants = new ArrayList<>();

        restaurants.add(new Restaurant("1", "Name 1", new Location("Address goes here", "Sector 89", 350, 0.0, 0.0)));

        return restaurants;
    }
}
