package com.codependent.rx.samplescada;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codependent.rx.samplescada.machine.Scada;

@Controller
@RequestMapping("/scada")
public class ScadaController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private Scada scada;
	
	@RequestMapping
	public void scada(){}
	
	@MessageMapping("/start")
	public void start() {
		logger.info("received msg start");
		scada.start();
	}	
	
	@MessageMapping("/stop")
	public void stop(){
		logger.info("received msg stop");
		scada.stop();
	}
	
	@MessageMapping("/startOperating")
	public void startOperating(){
		logger.info("received msg start operating");
		scada.startOperating();
	}
	
	@MessageMapping("/stopOperating")
	public void stopOperating(){
		logger.info("received msg stop operating");
		scada.stopOperating();
	}
	
	@MessageMapping("/jarDeposit/jars")
	public void setNumberOfJarsInDeposit(Map<String, Integer> message) {
		logger.info("received setNumberOfJarsInDeposit {}", message);
		scada.setNumberOfJarsInDeposit(message.get("number"));
	}	
	
	@MessageMapping("/conveyor/speed")
	public void setConveyorSpeed(Map<String, Double> message) {
		logger.info("received setConveyorSpeed {}", message);
		scada.setBeltSpeed(message.get("speed"));
	}	
	
}