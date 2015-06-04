package net.mcarolan.whenzebus.api.request;

import com.google.common.collect.Sets;

import net.mcarolan.whenzebus.api.field.Field;
import net.mcarolan.whenzebus.api.field.Fields;
import net.mcarolan.whenzebus.api.value.StopCode1;

import java.util.Set;

public class BusStopInformationRequest extends Request {

    private static final Set<? extends Field> BUS_STOP_INFORMATION_FIELDS =
            Sets.newHashSet(Fields.StopPointIndicator, Fields.StopPointName, Fields.StopCode1, Fields.Towards, Fields.Latitude, Fields.Longitude);

    public BusStopInformationRequest(StopCode1 stopCode1) {
        super(BUS_STOP_INFORMATION_FIELDS, stopCode1);
    }

    @Override
    protected String getAdditionalRequestParameters() {
        return "&StopAlso=true";
    }

}
