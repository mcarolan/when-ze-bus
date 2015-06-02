package net.mcarolan.whenzebus.adapter.item;

import com.google.common.base.Objects;

import net.mcarolan.whenzebus.api.PredictionModel;
import net.mcarolan.whenzebus.R;
import net.mcarolan.whenzebus.TimeRemainingCalculator;
import net.mcarolan.whenzebus.WhenZeBusApplication;
import net.mcarolan.whenzebus.adapter.GenericListItem;

public class PredictionModelItem implements GenericListItem {

    private final PredictionModel predictionModel;

    public PredictionModelItem(PredictionModel predictionModel) {
        this.predictionModel = predictionModel;
    }

    @Override
    public String getLegend() {
        return predictionModel.getLineName();
    }

    @Override
    public String getTitle() {
        return predictionModel.getDestinationText();
    }

    @Override
    public String getDescription() {
        final TimeRemainingCalculator.TimeRemaining timeRemaining = TimeRemainingCalculator.getTimeRemaining(predictionModel.getEstimatedTime());

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

        PredictionModelItem that = (PredictionModelItem) o;
        return Objects.equal(predictionModel, that.predictionModel);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(predictionModel);
    }
}
