/**
 * Copyright 2011 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.mns.mojoinvest.shared.action;

import com.gwtplatform.dispatch.shared.Action;

import java.util.Arrays;

/**
 * This provides a simple way to send multiple actions to be executed in
 * sequence. If any fail, the rules for the {@link OnException} value provided
 * in the constructor determine the outcome.
 * <p/>
 * Note: copied from original package because the no-arg constructor had only
 * default package visibility, meaning the class couldn't be extended.
 *
 * @author David Peterson
 */

//TODO: Improve error handling of BatchAction
public class BatchAction implements Action<BatchResult> {

    /**
     * {@link com.mns.mojoinvest.shared.action.BatchAction}'s OnException enumeration.
     */
    public enum OnException {
        /**
         * If specified, the batch will continue if an action fails. The matching
         * {@link com.gwtplatform.dispatch.shared.Result} in the {@link com.gwtplatform.dispatch.shared.BatchResult#getResults()} will be
         * <code>null</code>.
         */
        CONTINUE,
        /**
         * If specified, the batch will stop processing and roll back any executed
         * actions from the batch, and throw the exception.
         */
        ROLLBACK;
    }

    private Action<?>[] actions;

    private OnException onException;

    /**
     * Constructs a new batch action, which will attempt to execute the provided
     * list of actions in order. If there is a failure, it will follow the rules
     * specified by <code>onException</code>.
     *
     * @param onException If there is an exception, specify the behaviour.
     * @param actions     The list of actions to execute.
     */
    public BatchAction(OnException onException, Action<?>... actions) {
        this.onException = onException;
        this.actions = actions;
    }

    /**
     * Used for serialization only.
     */
    public BatchAction() {
    }

    /**
     * The list of actions to execute.
     *
     * @return The actions.
     */
    public Action<?>[] getActions() {
        return actions;
    }

    /**
     * The expected behaviour if any of the sub-actions throw an exception.
     *
     * @return The exception handling behaviour.
     */
    public OnException getOnException() {
        return onException;
    }

    @Override
    public String getServiceName() {
        return Action.DEFAULT_SERVICE_NAME + "BatchAction";
    }

    @Override
    public boolean isSecured() {
        return false;
    }

    @Override
    public String toString() {
        return "BatchAction{" +
                "actions=" + (actions == null ? null : Arrays.asList(actions)) +
                '}';
    }
}
