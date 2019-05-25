package www.xinkui.com.odering.network.schedulers;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;

public interface BaseSchedulerProvider {

    Scheduler ui();

    Scheduler io();

    Scheduler computation();

    <T> ObservableTransformer<T, T> applySchedulers();

}
