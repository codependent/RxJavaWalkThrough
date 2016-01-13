function Scada(scadaCanvas){
	
	var conveyor = new Conveyor();
    var jamMachine = new JamMachine();
    var jar1;
    
	var interval;
	var canvas = scadaCanvas;
	var canvasContext = canvas.getContext("2d");
	var canvasWidth = canvas.width;
	var canvasHeight = canvas.height;
	
	var socket = new SockJS("/scada/ws");
	var stompClient = Stomp.over(socket);
	var self = this
	
	stompClient.connect({}, function(frame) {
		console.log('CONNECTED! ' + frame);
	    stompClient.subscribe("/topic/ui", function(message) {
	    	console.log("got message "+message);
	    	var msg = JSON.parse(message.body);
	    	$("#message").text(msg.type + "-" + msg.info);
	    	if(msg.type == "JAR_IN_BELT_POSITION" && parseFloat(msg.info) < 0.1){
	    		self.addJar();
	    	}else if(msg.type == "JAR_IN_BELT_POSITION"){
	    		self.moveJar(parseFloat(msg.info));
	    	}
	    });
	});
	
	this.draw = function(){
		/*canvasContext.clearRect(0, 0, canvasWidth, canvasHeight);
	    //color in the background
		canvasContext.fillStyle = "#eee";
		canvasContext.fillRect(0, 0, canvasWidth, canvasHeight);*/
		interval = setInterval(function(){
			conveyor.draw(canvasContext);
			jamMachine.draw(canvasContext);
			canvasContext.clearRect(0, 0, canvasWidth, canvasHeight);
			if(jar1!=null){
				jar1.draw(canvasContext);
			}
		},40);
		
	}
	
	this.addJar = function(){
		jar1 = new Jar(0.0);
	}
	
	this.moveJar = function(positionX){
		jar1.setPositionX(positionX);
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

function Jar(posX){
	
	this.state = "empty";
	this.positionX = (posX * 600/10) + 100;
	var self = this;
	
	this.draw = function(canvasContext){
		var marmaladeJar = new Image() 
	    marmaladeJar.src = "/img/mermeladaVacio.gif" 
	    	marmaladeJar.onload = function() {
			console.log(self.positionX)
			canvasContext.drawImage(marmaladeJar, self.positionX, 170, 25, 30);
    	};
	}
	
	this.setPositionX = function(posX){
		console.log("*****>")
		console.log((posX * 600/10) + 100);
		console.log("*****<")
		this.positionX = (posX * 600/10) + 100;
	}
	
	
}