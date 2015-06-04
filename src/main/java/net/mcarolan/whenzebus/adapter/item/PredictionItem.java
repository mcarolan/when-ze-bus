package net.mcarolan.whenzebus.adapter.item;

import com.google.common.base.Objects;

import net.mcarolan.whenzebus.Prediction;
import net.mcarolan.whenzebus.R;
import net.mcarolan.whenzebus.TimeRemainingCalculator;
import net.mcarolan.whenzebus.WhenZeBusApplication;
import net.mcarolan.whenzebus.adapter.GenericListItem;

public class PredictionItem implements GenericListItem {

    private final Prediction prediction;

    public PredictionItem(Prediction prediction) {
        this.prediction = prediction;
    }

    @Override
    public String getLegend() {
        return prediction.getLineName();
    }

    @Override
    public String getTitle() {
        return prediction.getDestinationText();
    }

    @Override
    public String getDescription() {
        final TimeRemainingCalculator.TimeRemaining timeRemaining = TimeRemainingCalculator.getTimeRemaining(prediction.getEstimatedTime());

        if (timeRemaining.isInPast()) {
            return WhenZeBusApplication.getResourceString(R.string.predictionmoideladapter_due);
        }
        else {
            return WhenZeBusApplication.getResourceString(R.string.predictionmoideladapter_due_in) + timeRemaining.getTimeRemainingString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PredictionItem that = (PredictionItem) o;
        return Objects.equal(prediction, that.prediction);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(prediction);
    }
}
