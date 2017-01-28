package org.wso2.cep.geo.libs;

/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

import org.apache.log4j.Logger;
import org.wso2.siddhi.core.config.SiddhiContext;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.in.InEvent;
import org.wso2.siddhi.core.event.in.InListEvent;
import org.wso2.siddhi.core.event.in.InStream;
import org.wso2.siddhi.core.executor.expression.ExpressionExecutor;
import org.wso2.siddhi.core.query.processor.transform.TransformProcessor;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.definition.StreamDefinition;
import org.wso2.siddhi.query.api.expression.Expression;
import org.wso2.siddhi.query.api.expression.Variable;
import org.wso2.siddhi.query.api.extension.annotation.SiddhiExtension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* TODO: 1. Where is "outStreamDefinition" (spatialEventsInnerStream) reside in the CEP?
*       3. Ability to get current active `geo_` pattern execution plans count for pre initialize the static var in `observer` class
* */

@SiddhiExtension(namespace = "geo", function = "subscribeExecutionPlan")
public class executionPlanSubscriber extends TransformProcessor {
    private Map<String, Integer> paramPositions = new HashMap<String, Integer>();
    Boolean initialized = false;
    public executionPlanSubscriber() {
        System.out.println("DEBUG: Calling constructor executionPlanSubscriber...");
    }
    @Override
    protected InStream processEvent(InEvent inEvent) {
        System.out.println("DEBUG: Processing event...");
        return inEvent;
    }
    @Override
    protected InStream processEvent(InListEvent inListEvent) {
        System.out.println("DEBUG: Processing event list...");
        return inListEvent;
    }
    @Override
    protected Object[] currentState() {
        return new Object[]{paramPositions};
    }
    @Override
    protected void restoreState(Object[] objects) {
        if (objects.length > 0 && objects[0] instanceof Map) {
            paramPositions = (Map<String, Integer>) objects[0];
        }
    }
    @Override
    protected void init(Expression[] parameters, List<ExpressionExecutor> expressionExecutors, StreamDefinition inStreamDefinition, StreamDefinition outStreamDefinition, String elementId, SiddhiContext siddhiContext) {
        this.outStreamDefinition = this.getInStreamDefinition();
        if (!initialized){
            upCount();
            this.initialized = true;
        }
    }
    @Override
    public void destroy() {
        System.out.println("DEBUG: Destroying(un-deploy) extension...");
        downCount();
    }

    void upCount(){
        ExecutionPlansCount.upCount();
        System.out.println("DEBUG: daaam upCountNumberOfExecutionPlans current count after update = "+ ExecutionPlansCount.getNumberOfExecutionPlans());
    }

    void downCount(){
        ExecutionPlansCount.downCount();
        System.out.println("DEBUG: daaam downCountNumberOfExecutionPlans current count after update = "+ ExecutionPlansCount.getNumberOfExecutionPlans());
    }

}