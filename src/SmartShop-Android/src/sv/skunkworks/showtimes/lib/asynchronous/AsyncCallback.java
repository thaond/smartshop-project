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

import android.os.Message;

/**
 * {@link AsyncCallback} implements {@link ServiceMethodCallback}, provide
 * asynchronous mechanism for UI thread and non-UI thread.
 *
 * @author H&#7912;A PHAN Minh Hi&#7871;u (rockerhieu@gmail.com)
 * @param <T>
 *            The real type of an object return by {@link ServiceMethodCallback}
 *            in <code>onSuccess</code> method.
 *
 * @see {@link ServiceMethodCallback}
 * @see {@link CallbackHandler}
 */
public class AsyncCallback<T> implements ServiceCallback<T> {
    private CallbackHandler<T> handler;

    /**
     * Constructs an {@link AsyncCallback} from a {@link CallbackHandler}.
     * {@link CallbackHandler} will used to thread-safe callback.
     *
     * @param handler
     */
    public AsyncCallback(CallbackHandler<T> handler) {
        this.handler = handler;
    }

    public void onFailure(Exception caught) {
        if (handler == null) {
            return;
        }
        Message msg = new Message();
        msg.what = CallbackHandler.HANDLER_FAIL;
        msg.obj = caught;
        handler.sendMessage(msg);
    }

    public void onSuccess(T result) {
        if (handler == null) {
            return;
        }
        Message msg = new Message();
        msg.what = CallbackHandler.HANDLER_SUCCESS;
        msg.obj = result;
        handler.sendMessage(msg);
    }

    /**
     * Cancel the result return by this {@link AsyncCallback}.
     */
    public void cancel() {
        handler.cancel();
    }
}