package net.mcarolan.whenzebus.api.request;

import com.google.common.collect.Sets;

import net.mcarolan.whenzebus.api.field.Field;
import net.mcarolan.whenzebus.api.field.Fields;
import net.mcarolan.whenzebus.api.value.StopCode1;

import java.util.Set;

public class PredictedArrivalTimeRequest extends Request {

    private static final Set<? extends Field> PREDICTED_ARRIVAL_TIME_FIELDS =
            Sets.newHashSet(Fields.EstimatedTime, Fields.ExpireTime, Fields.DestinationText, Fields.LineName);

    public PredictedArrivalTimeRequest(StopCode1 stopCode1) {
        super(PREDICTED_ARRIVAL_TIME_FIELDS, stopCode1);
    }

    @Override
    protected String getAdditionalRequestParameters() {
        return "";
    }

}
