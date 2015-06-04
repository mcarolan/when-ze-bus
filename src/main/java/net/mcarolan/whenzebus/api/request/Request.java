package net.mcarolan.whenzebus.api.request;

import android.util.Log;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.mcarolan.whenzebus.api.field.Field;
import net.mcarolan.whenzebus.api.field.FieldName;
import net.mcarolan.whenzebus.api.field.Fields;
import net.mcarolan.whenzebus.api.value.StopCode1;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

public abstract class Request {

    private static final String BASE_URL = "http://countdown.api.tfl.gov.uk";

    private static final String TAG = "Request";

    private final Set<? extends Field> fields;
    private final StopCode1 stopCode1;

    public Request(final Set<? extends Field> fields, final StopCode1 stopCode1) {
        this.fields = fields;
        this.stopCode1 = stopCode1;

        if (fields.contains(Fields.EstimatedTime) && !fields.contains(Fields.ExpireTime)) {
            throw new IllegalArgumentException(fields.toString() + " contained EstimatedTime, but not ExpireTime");
        }
    }

    public URL buildURL() {
        final StringBuilder sb = new StringBuilder();

        sb.append("/interfaces/ura/instant_V1?ReturnList=");
        sb.append(createReturnList());
        sb.append("&StopCode1=");
        sb.append(stopCode1.getValue());
        sb.append(getAdditionalRequestParameters());

        final String url = BASE_URL + sb.toString();

        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            final String message = url + " is not a valid URL";
            Log.e(TAG, message, e);
            throw new IllegalStateException(message, e);
        }
    }

    protected abstract String getAdditionalRequestParameters();

    private String createReturnList() {
        final List<String> fieldNames = Lists.transform(Lists.newArrayList(fields), FieldName.transformer);
        final Joiner joiner = Joiner.on(",");
        return joiner.join(fieldNames);
    }

    public Set<? extends Field> getFields() {
        return fields;
    }

    public StopCode1 getStopCode1() {
        return stopCode1;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("fields", fields)
                .add("stopCode1", stopCode1)
                .toString();
    }
}
