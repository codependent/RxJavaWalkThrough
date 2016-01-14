function Scada(scadaCanvas){
	
	var conveyor = new Conveyor();
    var jamMachine = new JamMachine();
    var jar1;
    
	var canvas = scadaCanvas;
	var canvasContext = canvas.getContext("2d");
	var canvasWidth = canvas.width;
	var canvasHeight = canvas.height;
	
	var socket = new SockJS("/scada/ws");
	var stompClient = Stomp.over(socket);
	var self = this;
	
	initializeDrawing();
	startWebSocket();
	
	function initializeDrawing(){
		/*canvasContext.clearRect(0, 0, canvasWidth, canvasHeight);
	    //color in the background
		canvasContext.fillStyle = "#eee";
		canvasContext.fillRect(0, 0, canvasWidth, canvasHeight);*/
		conveyor.draw(canvasContext);
		jamMachine.draw(canvasContext);
		canvasContext.clearRect(0, 0, canvasWidth, canvasHeight);
		if(jar1!=null){
			jar1.draw(canvasContext);
		}
	}
	
	function startWebSocket(){
		stompClient.connect({}, function(frame) {
			console.log('WS CONNECTED! ' + frame);
		    stompClient.subscribe("/topic/ui", function(message) {
		    	console.log("----- WS MESSAGE -----");
		    	console.log(message);
		    	console.log("----------------------");
		    	self.refreshDrawing();
		    	var msg = JSON.parse(message.body);
		    	$("#message").text(msg.type + "-" + msg.info);
		    	if(msg.type == "JAR_IN_BELT_POSITION"){
		    		if(self.hasJar()){
		    			self.moveJar(parseFloat(msg.info));
		    		}else{
		    			self.addJar(parseFloat(msg.info));
		    		}
		    	}else if(msg.type == "JAR_IN_JARMACHINE_FILLING_INFO" && msg.info == 100){
		    		self.setJarState(true);
		    	}
		    });
		});
	}
	
	this.refreshDrawing = function(){
		conveyor.draw(canvasContext);
		jamMachine.draw(canvasContext);
		canvasContext.clearRect(0, 0, canvasWidth, canvasHeight);
		if(jar1!=null){
			jar1.draw(canvasContext);
		}
	}
	
	this.addJar = function(positionX){
		jar1 = new Jar(positionX, false);
	}
	
	this.hasJar = function(){
		return (typeof jar1 !== 'undefined');
	}
	
	this.moveJar = function(positionX){
		jar1.setPositionX(positionX);
	}
	
	this.setJarState = function(filled){
		jar1.setState(filled);
	}
	
	this.start = function(){
		stompClient.send("/app/start", {}, null);
	}
	
	this.stop = function(){
		stompClient.send("/app/stop", {}, null);
	}
	
	this.startOperating = function(){
		stompClient.send("/app/startOperating", {}, null);
	}
	
	this.stopOperating = function(){
		stompClient.send("/app/stopOperating", {}, null);
	}
	
}

function Conveyor(){
	
	this.draw = function(canvasContext){
		var conveyor1 = new Image(); 
    	conveyor1.src = "/img/cinta.jpg";
		conveyor1.onload = function() {
			canvasContext.drawImage(conveyor1, 100, 200, 600, 80);
		};
	}
	
}

function JamMachine(){
	
	this.draw = function(canvasContext){
		var jamMachine = new Image() 
    	jamMachine.src = "/img/depositoMermelada.jpg" 
    		jamMachine.onload = function() {
			canvasContext.drawImage(jamMachine, 350, 60, 80, 100);
    	};
	}
	
}

function Jar(posX, filled){
	
	this.state = !filled ? "empty" : "filled";
	this.positionX = (posX * 600/10) + 100;
	var self = this;
	
	this.draw = function(canvasContext){
		var marmaladeJar = new Image() 
		if(this.state == "empty"){
			 marmaladeJar.src = "/img/mermeladaVacio.gif" 
		}else{
			 marmaladeJar.src = "/img/mermeladaSinTapa.gif" 
		}
    	marmaladeJar.onload = function() {
			canvasContext.drawImage(marmaladeJar, self.positionX, 170, 25, 30);
    	};
	}
	
	this.setPositionX = function(posX){
		this.positionX = (posX * 600/10) + 100;
	}
	
	this.setState = function(filled){
		this.state = !filled ? "empty" : "filled";
	}
	
	
}