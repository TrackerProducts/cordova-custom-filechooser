# cordova custom filechooser
The plugin has been created to extend default ionic's file chooser with multi-select functionality. Possibility to filter files with mime types has also been added.
 
Requires Cordova >= 2.8.0 

How to install using Cordova CLI
	
	$ cordova plugin add https://github.com/eugene-kiba/cordova-custom-filechooser.git
  
API

	customFileChooser.open(type, allowMultiple, successCallback, failureCallback); 
  type: string
  allowMultiple: boolean
  
  	customFileChooser.open('application/pdf, image/png', true, function (uri){
      		console.log(uri)
    	}, function(err){
      		console.log(err);
    	});
