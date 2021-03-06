/*
 * Copyright 2019 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.gestalt.entitysystem.event.impl;

import com.google.common.collect.ImmutableList;

import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.gestalt.entitysystem.entity.EntityRef;
import org.terasology.gestalt.entitysystem.event.Event;
import org.terasology.gestalt.entitysystem.event.EventHandler;
import org.terasology.gestalt.entitysystem.event.EventResult;
import org.terasology.gestalt.entitysystem.event.exception.EventSystemException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public class ReflectionEventHandler implements EventHandler {

    private Object handler;
    private Method method;
    private ImmutableList<Class<? extends Component>> componentParams;

    public ReflectionEventHandler(Object handler,
                                  Method method,
                                  Collection<Class<? extends Component>> componentParams) {
        this.handler = handler;
        this.method = method;
        this.componentParams = ImmutableList.copyOf(componentParams);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EventResult onEvent(Event event, EntityRef entity) {
        try {
            Object[] params = new Object[EventReceiverMethodSupport.FIXED_PARAM_COUNT + componentParams.size()];
            params[0] = event;
            params[1] = entity;
            for (int i = 0; i < componentParams.size(); ++i) {
                params[i + EventReceiverMethodSupport.FIXED_PARAM_COUNT] = getComponent(entity, componentParams.get(i));
            }
            return (EventResult) method.invoke(handler, params);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new EventSystemException("Error processing event", ex);
        }
    }

    private <T extends Component<T>> T getComponent(EntityRef entity, Class<T> componentType) {
        return entity.getComponent(componentType).orElseThrow(() -> new EventSystemException("Component unexpectedly missing"));
    }
}
