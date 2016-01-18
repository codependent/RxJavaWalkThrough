package com.codependent.rx.samplescada;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.codependent.rx.samplescada.machine.Belt;
import com.codependent.rx.samplescada.machine.JarDeposit;
import com.codependent.rx.samplescada.machine.Signal;
import com.codependent.rx.samplescada.machine.Signal.Type;
import com.codependent.rx.samplescada.machine.impl.FakeBelt;
import com.codependent.rx.samplescada.machine.impl.FakeJarDeposit;
import com.codependent.rx.samplescada.machine.sensor.PositionSensor;
import com.codependent.rx.samplescada.machine.sensor.impl.FakeBeltPositionSensor;

@Configuration
@EnableWebSocketMessageBroker
public class ScadaConfiguration extends AbstractWebSocketMessageBrokerConfigurer{
	
	@Autowired
	private List<PositionSensor> positionSensors;
	
	@Bean
	public PositionSensor jamMachineBeltSensor(){
		return new FakeBeltPositionSensor("jamMachineBeltSensor", new Double[]{0.0, 4.9}, 0.0, 1.0, new Signal(Type.JAR_IN_JARMACHINE));
	}
	
	@Bean
	public PositionSensor beltEndSensor(){
		return new FakeBeltPositionSensor("beltEndSensor", new Double[]{4.9, 10.0}, 5.0, 1.0, new Signal(Type.JAR_IN_BELT_END));
	}
	
	@Bean
	public Belt belt(){
		return new FakeBelt("belt", 10.0, 0.50, positionSensors);
	}
	
	@Bean
	public JarDeposit jarDeposit(){
		return new FakeJarDeposit("jarDeposit", 1);
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/scada/ws").withSockJS();
	}
	
	@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/topic", "/queue");
    }
	
}
