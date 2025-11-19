//AJAX call management
function makeCall(method, url, formElement, cback, reset = true) {
	
	//Creation of the request
	let request = new XMLHttpRequest();
  
    //Callback function
	request.onreadystatechange = function() {
    	cback(request);
    };
	//Sending of the request
  	request.open(method, url);
  	//Sending of the form element, if present
  	if (formElement === null) {
    	request.send();
  	} else {
    	request.send(new FormData(formElement));
  	}
  	//Reset of the form input
 	if ((formElement !== null) && (reset === true)) {
	      formElement.reset();
	}
}
