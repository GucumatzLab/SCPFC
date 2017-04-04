package gucumatz.scpfc.web;

import gucumatz.scpfc.modelo.Usuario;
import gucumatz.scpfc.modelo.db.FabricaControladorJpa;
import gucumatz.scpfc.modelo.db.UsuarioJpaController;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActivacionDeCuenta extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        activaCuenta(request, response);
    }

    private void activaCuenta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idUsuario = request.getParameter("id");
        String codigoDeActivacion = request.getParameter("codigo");

        if (idUsuario != null && codigoDeActivacion != null) {
            UsuarioJpaController jpaUsuario
                    = new FabricaControladorJpa().obtenerControladorJpaUsuario();
            Long id = Long.parseLong(idUsuario);

            Usuario usuario = jpaUsuario.findUsuario(id);
            if (usuario != null
                    && codigoDeActivacion.equals(usuario.getCodigoDeActivacion())) {
                usuario.setCodigoDeActivacion(null);
                usuario.setConfirmada(true);

                try {
                    jpaUsuario.edit(usuario);
                } catch (Exception ex) {

                }
            }
        }

        response.sendRedirect("index.xhtml");
    }
}
