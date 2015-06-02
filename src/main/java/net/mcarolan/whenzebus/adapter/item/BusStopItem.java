package net.mcarolan.whenzebus.adapter.item;

import com.google.common.base.Objects;

import net.mcarolan.whenzebus.BusStop;
import net.mcarolan.whenzebus.R;
import net.mcarolan.whenzebus.WhenZeBusApplication;
import net.mcarolan.whenzebus.adapter.GenericListItem;

public class BusStopItem implements GenericListItem {

    private final BusStop busStop;

    public BusStopItem(BusStop busStop) {
        this.busStop = busStop;
    }

    @Override
    public String getLegend() {
        return busStop.getStopPointIndicator().getValue();
    }

    @Override
    public String getTitle() {
        return busStop.getStopPointName().getValue();
    }

    public BusStop getBusStop() {
        return busStop;
    }

    @Override
    public String getDescription() {
        return WhenZeBusApplication.getResourceString(R.string.busstoplistadapter_towards) + busStop.getTowards().getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusStopItem that = (BusStopItem) o;
        return Objects.equal(busStop, that.busStop);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(busStop);
    }
}
