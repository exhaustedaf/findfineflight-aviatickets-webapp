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

document.body.addEventListener("input", () => {
  document.getElementById("search-button").disabled =
    !document.getElementById("origin-input").value || !document.getElementById("destination-input");
});

document.getElementById("flight-type-select").addEventListener("change", () => {
    if (document.getElementById("flight-type-select").value === "one-way")
      document.getElementById("return-date").classList.add("d-none");
    else
      document.getElementById("return-date").classList.remove("d-none");
});