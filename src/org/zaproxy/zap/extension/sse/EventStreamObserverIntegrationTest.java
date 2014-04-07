package org.zaproxy.zap.extension.sse;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedWriter;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EventStreamObserverIntegrationTest extends BaseEventStreamTest  {

    @Test
    public void shouldInformObserversOnceForAnEvent() throws IOException {
        // Given
        EventStreamProxy proxy = new EventStreamProxy(getMockHttpMessage(), null, null);
        
        EventStreamObserver mockObserver = Mockito.mock(EventStreamObserver.class);
        proxy.addObserver(mockObserver);
        
        // When
        proxy.processEvent("data:blub");
        
        // Then
        verify(mockObserver, times(1)).onServerSentEvent(Mockito.any(ServerSentEvent.class));
    }

    @Test
    public void shouldCallObserversWithLowerOrderingFirst() throws IOException {
        // Given
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        EventStreamProxy proxy = new EventStreamProxy(getMockHttpMessage(), null, writer);
        
        // create two mocks and add as observer
        EventStreamObserver mockObserver1 = Mockito.mock(EventStreamObserver.class);
        when(mockObserver1.getServerSentEventObservingOrder()).thenReturn(20);
        when(mockObserver1.onServerSentEvent(Mockito.any(ServerSentEvent.class))).thenReturn(true);
        
        EventStreamObserver mockObserver2 = Mockito.mock(EventStreamObserver.class);
        when(mockObserver2.getServerSentEventObservingOrder()).thenReturn(10);
        when(mockObserver2.onServerSentEvent(Mockito.any(ServerSentEvent.class))).thenReturn(true);
        
        proxy.addObserver(mockObserver1);
        proxy.addObserver(mockObserver2);
        
        final String event1 = "data:blub";
        
        // When
        proxy.processEvent(event1);
        
        // Then
        InOrder inOrder = inOrder(mockObserver2, mockObserver1);
        inOrder.verify(mockObserver2).onServerSentEvent(Mockito.any(ServerSentEvent.class));
        inOrder.verify(mockObserver1).onServerSentEvent(Mockito.any(ServerSentEvent.class));
    }
    
    @Test
    public void shouldNotInformFurtherObserverWhenAnotherReturnedFalse() throws IOException {
        // Given
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        EventStreamProxy proxy = new EventStreamProxy(getMockHttpMessage(), null, writer);
        
        // create two mocks and add as observer
        EventStreamObserver mockObserver1 = Mockito.mock(EventStreamObserver.class);
        when(mockObserver1.onServerSentEvent(Mockito.any(ServerSentEvent.class))).thenReturn(false);
        
        EventStreamObserver mockObserver2 = Mockito.mock(EventStreamObserver.class);
        
        proxy.addObserver(mockObserver1);
        proxy.addObserver(mockObserver2);
        
        // When
        proxy.processEvent("data:blub");
        
        // Then
        verify(mockObserver1, times(1)).onServerSentEvent(Mockito.any(ServerSentEvent.class));
        verify(mockObserver2, never()).onServerSentEvent(Mockito.any(ServerSentEvent.class));
    }
}
