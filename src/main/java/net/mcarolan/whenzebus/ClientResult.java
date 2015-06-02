package net.mcarolan.whenzebus;

import net.mcarolan.whenzebus.api.Response;

import java.util.Set;

public class ClientResult {

    final boolean isSuccess;
    final Set<Response> responses;
    final Throwable error;

    public ClientResult(boolean isSuccess, Set<Response> responses,
                                 Throwable error) {
        this.isSuccess = isSuccess;
        this.responses = responses;
        this.error = error;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public Set<Response> getResponses() {
        return responses;
    }

    public Throwable getError() {
        return error;
    }
}
