/**
 * Copyright 2010 Hieu Rocker & Tien Trum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sv.skunkworks.showtimes.lib.asynchronous;

import android.os.Handler;
import android.os.Message;

/**
 * Provides thead-safe callback from another thread.
 *
 * @author H&#7912;A PHAN Minh Hi&#7871;u (rockerhieu@gmail.com)
 *
 * @param <T>
 *            The real type of an object sent from {@link AsyncCallback} in
 *            <code>onSuccess</code> method.
 */
public abstract class CallbackHandler<T> extends Handler {
    public static final int HANDLER_FAIL = 0;
    public static final int HANDLER_SUCCESS = 1;

    public CallbackHandler() {
    }

    /**
     * Indicate that this callback is cancelled or not.
     */
    private boolean isCancel = false;

    /**
     * Cancel the result return by this {@link CallbackHandler}.
     */
    public void cancel() {
        isCancel = true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(Message msg) {
        if (msg != null && !isCancel)
            switch (msg.what) {
            case CallbackHandler.HANDLER_FAIL:
                if (msg.obj != null)
                    onFailure((Throwable) msg.obj);
                onFinally();
                break;

            case CallbackHandler.HANDLER_SUCCESS:
                if (msg.obj != null)
                    onSuccess((T) msg.obj);
                onFinally();
                break;

            default:
                break;
            }
        isCancel = false;
        super.handleMessage(msg);
    }

    /**
     * Callback a failure circumstance.
     *
     * @param caught
     *            A {@link Throwable} thrown by asynchronous service.
     */
    public abstract void onFailure(Throwable caught);

    /**
     * Callback a success circumstance.
     *
     * @param result
     *            An object of <code>&lt;T&gt;</code> type returned by
     *            asynchronous service.
     */
    public abstract void onSuccess(T result);

    /**
     * Automatically called after <code>onFailure</code> or
     * <code>onSuccess</code>. Usually used to dismiss any Dialog in UI-thread
     * or release resources.
     */
    public void onFinally() {
    }
}
