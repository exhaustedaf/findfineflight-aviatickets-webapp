$(document).ready(function () {
  $('#horizontalTab').easyResponsiveTabs({
    type: 'default',
    width: 'auto',
    fit: true
  });
});

$(function() {
    $( "#datepicker,#datepicker1,#datepicker2" ).datepicker();
});

function match() {
  if (document.getElementById('password').value !=
    document.getElementById('same-password').value) {
    document.getElementById('message').innerHTML = 'Пароли не совпадают!';
    document.querySelector('button').setAttribute("disabled", "");
  } else {
    document.getElementById('message').innerHTML = '';
    document.querySelector('button').removeAttribute("disabled", "");
  }
}