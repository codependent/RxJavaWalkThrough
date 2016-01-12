package com.codependent.rx.samplescada;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codependent.rx.samplescada.machine.Belt;
import com.codependent.rx.samplescada.machine.impl.FakeBelt;
import com.codependent.rx.samplescada.machine.sensor.PositionSensor;
import com.codependent.rx.samplescada.machine.sensor.Signal;
import com.codependent.rx.samplescada.machine.sensor.Signal.Type;
import com.codependent.rx.samplescada.machine.sensor.impl.FakeBeltPositionSensor;

@Configuration
public class ScadaConfiguration {
	
	@Autowired
	private List<PositionSensor> positionSensors;
	
	@Bean
	public PositionSensor jamMachineBeltSensor(){
		return new FakeBeltPositionSensor(new Double[]{0.0, 4.9}, 0.0, 1.0, new Signal(Type.JAR_IN_JARMACHINE));
	}
	
	@Bean
	public PositionSensor beltEndSensor(){
		return new FakeBeltPositionSensor(new Double[]{5.0, 10.0}, 5.0, 1.0, new Signal(Type.JAR_IN_BELT_END));
	}
	
	@Bean
	public Belt belt(){
		return new FakeBelt(10.0, 1.0, positionSensors);
	}
	
}
