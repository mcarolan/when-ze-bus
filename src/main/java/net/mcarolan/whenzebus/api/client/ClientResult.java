package net.mcarolan.whenzebus.api.client;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;

public class ClientResult<T> {

    private final boolean isSuccess;
    private final T responses;
    private final Throwable error;

    public ClientResult(Throwable error) {
        this(false, null, error);
    }

    public ClientResult(T result) {
        this(true, result, null);
    }

    private ClientResult(boolean isSuccess, T responses, Throwable error) {
        this.isSuccess = isSuccess;
        this.responses = responses;
        this.error = error;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public T getResponses() {
        return responses;
    }

    public <U> ClientResult<U> map(Function<T, U> g) {
        if (isSuccess) {
            return new ClientResult<U>(g.apply(responses));
        }
        else {
            return new ClientResult<U>(error);
        }
    }

    public Throwable getError() {
        return error;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("isSuccess", isSuccess)
                .add("responses", responses)
                .add("error", error)
                .toString();
    }
}
