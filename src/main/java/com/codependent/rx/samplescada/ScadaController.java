package com.codependent.rx.samplescada;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.codependent.rx.samplescada.machine.Scada;

@Controller
public class ScadaController {
	
	@Autowired
	private Scada scada;
	
	@RequestMapping("/start")
	@ResponseStatus(HttpStatus.OK)
	public void start(){
		scada.start();
	}
	
	@RequestMapping("/stop")
	@ResponseStatus(HttpStatus.OK)
	public void stop(){
		scada.start();
	}
	
	@RequestMapping("/startOperating")
	@ResponseStatus(HttpStatus.OK)
	public void startOperating(){
		scada.startOperating();
	}
	
	@RequestMapping("/stopOperating")
	@ResponseStatus(HttpStatus.OK)
	public void stopOperating(){
		scada.stopOperating();
	}
	
}