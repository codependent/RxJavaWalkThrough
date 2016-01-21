function Scada(scadaCanvas){
	
	var canvas = scadaCanvas;
	var canvasContext = canvas.getContext("2d");
	var canvasWidth = canvas.width;
	var canvasHeight = canvas.height;
	var fps = 60;
	
	var conveyor = new Conveyor(canvasContext, 200);
	var conveyor2 = new Conveyor(canvasContext, 450);
	var jarDeposit = new JarDeposit(canvasContext, 5);
    var jamMachine = new JamMachine(canvasContext);
    var translatingRobot = new TranslatingRobot(canvasContext);
    var jar1;
    
	var socket = new SockJS("/scada/ws");
	var stompClient = Stomp.over(socket);
	var self = this;
	
	setInterval(function(){
		draw();
	}, 1000/fps);
	
	startWebSocket();
	
	function draw(){
		/*canvasContext.clearRect(0, 0, canvasWidth, canvasHeight);
	    //color in the background
		canvasContext.fillStyle = "#eee";
		canvasContext.fillRect(0, 0, canvasWidth, canvasHeight);*/
		canvasContext.clearRect(0, 0, canvasWidth, canvasHeight);
		conveyor.draw();
		conveyor2.draw();
		jarDeposit.draw(canvasContext);
		jamMachine.draw(canvasContext);
		translatingRobot.draw(canvasContext);
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
		    	//self.refreshDrawing();
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
		    	}else if(msg.type == "JAR_IN_BELT_END"){
		    		
		    	}else if(msg.type == "JARDEPOSIT_EMPTY"){
		    		self.setNumberOfJarsInDeposit(0);
		    	}else if(msg.type == "JARDEPOSIT_DROPPED_JAR"){
		    		
		    	}else if(msg.type == "TRANSLATIONROBOT_PICKINGUP_JAR"){
		    		self.removeJarFromBelt();
		    		self.translatingRobotPickUpJar();
		    	}else if(msg.type == "TRANSLATIONROBOT_TRANSLATING"){
		    		self.translatingRobotMove(parseFloat(msg.info));
		    	}else if(msg.type == "TRANSLATIONROBOT_TRANSLATED"){
		    		self.translatingRobotDropJar();
		    	}else if(msg.type == "TRANSLATIONROBOT_RETURNING"){
		    		self.translatingRobotMove(-1.0*parseFloat(msg.info));
		    	}else if(msg.type == "TRANSLATIONROBOT_RETURNED"){

		    	}
		    });
		});
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
	/*
	this.refreshDrawing = function(){
		draw();
	}*/
	
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
	
	this.removeJarFromBelt = function(){
		jar1 = undefined;
	}
	
	this.setNumberOfJarsInDeposit = function(capacity){
		jarDeposit.setCapacity(capacity);
		stompClient.send("/app/jarDeposit/jars", {}, '{"number":'+capacity+'}');
	}
	
	this.setConveyorSpeed = function(speed){
		stompClient.send("/app/conveyor/speed", {}, '{"speed":'+parseFloat(speed).toFixed(2)+'}');
	}
	
	this.isRobotTranslatingJar = function(){
		return translatingRobot.isTranslating();
	}
	
	this.translatingRobotPickUpJar = function(){
		return translatingRobot.pickJar();
	}
	
	this.translatingRobotDropJar = function(){
		return translatingRobot.dropJar();
	}
	
	this.translatingRobotMove = function(posY){
		return translatingRobot.moveRobot(posY);
	}
}

function Conveyor(canvasContext, posY){
	var canvasContext = canvasContext;
	var positionY = posY;
	var conveyor1 = new Image(); 
	conveyor1.src = "/img/cinta.jpg";
	conveyor1.onload = function(){
		canvasContext.drawImage(conveyor1, 100, positionY, 600, 80);
	}
	this.draw = function(){
		canvasContext.drawImage(conveyor1, 100, positionY, 600, 80);
	}
}

function JarDeposit(canvasContext, capacity){
	var canvasContext = canvasContext;
	var capacity = capacity; 
	var fullJarDeposit = new Image();
	fullJarDeposit.src = "/img/depositoBotes.jpg";
	fullJarDeposit.onload = function(){
		canvasContext.drawImage(fullJarDeposit, 80, 120, 100, 60);
	}
	var emptyJarDeposit = new Image();
	emptyJarDeposit.src = "/img/depositoBotesVacio.jpg";
	
	this.draw = function(){
		if(capacity>0){
			canvasContext.drawImage(fullJarDeposit, 80, 120, 100, 60);
		}else{
			canvasContext.drawImage(emptyJarDeposit, 80, 120, 100, 60);
		}
	}
	
	this.setCapacity = function(cap){
		capacity = cap;
	}
}

function JamMachine(canvasContext){
	var canvasContext = canvasContext;
	var jamMachine = new Image() 
	jamMachine.src = "/img/depositoMermelada.jpg" 
	jamMachine.onload = function(){
		canvasContext.drawImage(jamMachine, 350, 60, 80, 100);
	}
		
	this.draw = function(canvasContext){
		canvasContext.drawImage(jamMachine, 350, 60, 80, 100);
	}
}

function TranslatingRobot(canvasContext){
	var empty = false;
	var positionY = 170.0;
	var canvasContext = canvasContext;
	var translatingRobotEmpty = new Image() ;
	translatingRobotEmpty.src = "/img/robot.jpg" 
		translatingRobotEmpty.onload = function(){
		canvasContext.drawImage(translatingRobotEmpty, 350, 60, 80, 100);
	}
	var translatingRobotWithJar = new Image() 
	translatingRobotWithJar.src = "/img/robotConBote.jpg" 
		
	this.draw = function(canvasContext){
		if(empty){
			canvasContext.drawImage(translatingRobotEmpty, 700, positionY, 100, 120);
		}else{
			canvasContext.drawImage(translatingRobotWithJar, 700, positionY, 100, 120);
		}
	}
	
	this.pickJar = function(){
		empty = false;
	}
	
	this.dropJar = function(){
		empty = true;
	}
	
	this.moveRobot = function(posY){
		if(posY >= 0){
			positionY = parseInt(posY * 250.0) + 170;
		}else{
			positionY = parseInt(posY * 250.0) + 170+250;
		}
	}
	
	this.isTranslating = function(){
		return empty == false;
	}
}

function Jar(posX, filled){
	
	var emptyMarmaladeJar = new Image();
	emptyMarmaladeJar.src = "/img/mermeladaVacio.gif" 
	var fullMarmaladeJar = new Image();
	fullMarmaladeJar.src = "/img/mermeladaSinTapa.gif" 
	this.state = !filled ? "empty" : "filled";
	this.positionX = (posX * 600/10) + 100;
	var self = this;
	
	this.draw = function(canvasContext){
		if(this.state == "empty"){
			canvasContext.drawImage(emptyMarmaladeJar, self.positionX, 170, 25, 30);
		}else{
			canvasContext.drawImage(fullMarmaladeJar, self.positionX, 170, 25, 30);
		}
	}
	
	this.setPositionX = function(posX){
		this.positionX = (posX * 600/10) + 100;
	}
	
	this.setState = function(filled){
		this.state = !filled ? "empty" : "filled";
	}
	
	
}