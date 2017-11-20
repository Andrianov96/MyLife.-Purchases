    // Attach a submit handler to the form
$( "#myForm2" ).submit(function( event ) {

  // Stop form from submitting normally
  event.preventDefault();

 var myfun = function objectifyForm(formArray) {//serialize data function
  var returnArray = {};
  for (var i = 0; i < formArray.length; i++){
    returnArray[formArray[i]['name']] = formArray[i]['value'];
  }
  return returnArray;
}

    var ar = $( "#myForm2" ).serializeArray()
    var send = myfun(ar);

$.ajax({
    type: 'post',
    url: "http://localhost:8080/addDefiniteItem",
    data: JSON.stringify(send),
    contentType: "application/json",
    success: function(data){window.location.href = data;},
    failure: function(errMsg) {
        alert(errMsg);
    }
});
});