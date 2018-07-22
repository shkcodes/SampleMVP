package com.shkmishra.samplemvp;

import android.content.SharedPreferences;

import com.shkmishra.samplemvp.data.ApiResponse;
import com.shkmishra.samplemvp.data.Restaurant;
import com.shkmishra.samplemvp.data.api.ApiService;
import com.shkmishra.samplemvp.ui.listscreen.RestaurantListContract;
import com.shkmishra.samplemvp.ui.listscreen.RestaurantListPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RestaurantListTest {

    @Mock
    private RestaurantListContract.View restaurantView;

    @Mock
    private ApiService apiService;

    @Mock
    private SharedPreferences sharedPreferences;

    @Mock
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Captor
    private ArgumentCaptor<List<Restaurant>> venueCaptor;

    @Captor
    private ArgumentCaptor<String> messageCaptor;

    private RestaurantListPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new RestaurantListPresenter(restaurantView, apiService, sharedPreferences);

        RxAndroidPlugins.setInitMainThreadSchedulerHandler(new Function<Callable<Scheduler>, Scheduler>() {
            @Override
            public Scheduler apply(Callable<Scheduler> schedulerCallable) throws Exception {
                return Schedulers.trampoline();
            }
        });
        RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
            @Override
            public Scheduler apply(Scheduler scheduler) throws Exception {
                return Schedulers.trampoline();
            }
        });

    }

    @Test
    public void testList() {
        when(apiService.getRestaurants(anyInt(), anyInt())).thenReturn(TestDataSource.getFakeResponse());
        List<Restaurant> result = TestDataSource.getVenues();

        presenter.loadRestaurants(true);

        verify(restaurantView).showHideLoading(true);
        verify(restaurantView).showHideLoading(false);
        verify(restaurantView).showRestaurants(venueCaptor.capture());
        assertEquals(result.get(0).getName(), venueCaptor.getValue().get(0).getName());
    }

    @Test
    public void testEmptyList() {
        when(apiService.getRestaurants(anyInt(), anyInt())).thenReturn(TestDataSource.getEmptyResponse());

        presenter.loadRestaurants(true);

        verify(restaurantView).showHideLoading(true);
        verify(restaurantView).showHideLoading(false);
        verify(restaurantView).showToast(messageCaptor.capture());
        assertEquals("No more restaurants found", messageCaptor.getValue());
    }

    @Test
    public void testError() {
        when(apiService.getRestaurants(anyInt(), anyInt())).thenReturn(Observable.<ApiResponse>error(new IOException("Missing Auth")));

        presenter.loadRestaurants(true);

        verify(restaurantView).showHideLoading(true);
        verify(restaurantView).showHideLoading(false);
        verify(restaurantView).showErrorMessage(messageCaptor.capture());
        assertEquals("Unable to fetch restaurants (Missing Auth)", messageCaptor.getValue());
    }

    @Test
    public void testDislike() {
        when(sharedPreferences.edit()).thenReturn(sharedPreferencesEditor);
        when(sharedPreferences.edit().putBoolean(anyString(), anyBoolean())).thenReturn(sharedPreferencesEditor);
        when(sharedPreferences.getBoolean("1", false)).thenReturn(true);
        when(apiService.getRestaurants(anyInt(), anyInt())).thenReturn(TestDataSource.getFakeResponse());

        presenter.dislikeRestaurant("1");
        presenter.loadRestaurants(true);

        verify(restaurantView).showHideLoading(true);
        verify(restaurantView).showHideLoading(false);
        verify(sharedPreferencesEditor, times(1)).putBoolean("1", true);
        verify(restaurantView).showRestaurants(venueCaptor.capture());
        assertEquals(new ArrayList<Restaurant>(), venueCaptor.getValue());
    }
}
