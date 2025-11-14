//Selected element
let element;

//Allows the drag of the element selected
function dragStart(event) {
  	event.dataTransfer.effectAllowed = "move";
  	event.dataTransfer.setData("text/plain", null);
  	element = event.target;
}

//Handles the movement of the element selected
function dragOver(event) {
 	event.preventDefault();
  	element.classList.add('hidden');
  	if (isBefore(element, event.target)) {
    	event.target.parentNode.insertBefore(element, event.target);
  	} else {
   		 event.target.parentNode.insertBefore(element, event.target.nextSibling);
	}
}

//Terminates the drag of the element selected
function dragEnd() {
  element.classList.remove('hidden');
  element = null;
}

//Allows the drop of the element
function allowDrop(event) {
  event.preventDefault();
}

//Checks if the first element is placed before the second element
function isBefore(element1, element2) {
    if (element1.parentNode === element2.parentNode) {
        for (var current = element1.previousSibling; current; current = current.previousSibling) {
			if (current === element2) {
                return true;
            }
		}
    }
    return false;
}