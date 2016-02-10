
// Function that checks whether input text is special character or not.
function specialCharValidate(inputtext){
	var iChars = "!@#$%^&*()+=-[]\\\';,./{}\\|\"<>?";
	for (var i = 0; i<inputtext.length; i++) {    
		if (iChars.indexOf(inputtext.charAt(i)) != -1) {
			return false;        
			}    
		}	
	return true;        
}

//Function that checks whether input text is numeric or not.
function textNumeric(inputtext) {
	var numericExpression = /^[0-9]+$/;
	if (inputtext.match(numericExpression)) {
		return true;
	} else {
		return false;
	}
}
// Function that checks whether input text is an alphabetic character or not.
function inputAlphabet(inputtext) {
	var alphaExp = /^[a-zA-Z]+$/;
	if (inputtext.match(alphaExp)) {
		return true;
	} else {
		return false;
	}
}
// Function that checks whether input text includes alphabetic and numeric characters.
function textAlphanumeric(inputtext) {
	var alphaExp = /^[0-9a-zA-Z\\_]+$/;
	if (inputtext.match(alphaExp)) {
		return true;
	} else {
		return false;
	}
}

// function to check whether the input for the given element is empty
function isEmpty(element) {
	 if( !$(element).val() ) {                      //if it is blank. 
         return true;  
    }
    return false;
}

// function to check if the given input is between the given numeric range
function isInNumericRange(val,min,max) {
	 if(val >= min && val <= max) {
		 return true;
	 }
	 return false;
}
