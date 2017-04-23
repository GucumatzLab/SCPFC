function actualizarVistaPrevia(input) {
    var visor = document.getElementById('formulario:vistaPrevia');
    if (input.files && input.files[0]) {
        var lector = new FileReader();

        lector.onload = function(e) {
            visor.src = e.target.result;
        }

        lector.readAsDataURL(input.files[0]);
    } else {
        visor.src = '#'
    }
}
