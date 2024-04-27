package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import estaciones.modelo.Usuario;

public class ServletRegistro extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ServletRegistro() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Usuario u = new Usuario();
		u.setNombre(request.getParameter("nombre"));
		u.setApellidos(request.getParameter("apellidos"));
		u.setDni(request.getParameter("dni"));
		u.setEmail(request.getParameter("email"));
		u.setUsername(request.getParameter("username"));
		u.setPassword(request.getParameter("clave"));

		String gestorParam = request.getParameter("gestor");
		boolean esGestor = false;
		if (gestorParam != null && gestorParam.equals("on")) {
			esGestor = true;
		}
		u.setGestor(esGestor);

		ServletContext app = getServletConfig().getServletContext();
		HashMap<String, Usuario> usuarios = (HashMap<String, Usuario>) app.getAttribute("usuarios");
		
		if (usuarios == null) {
			usuarios = new HashMap<String, Usuario>();
			app.setAttribute("usuarios", usuarios);
		}

		boolean error = false;
		if (usuarios.get(u.getUsername()) != null) {
			error = true;
			String referer = request.getHeader("referer");
			response.setHeader("refresh", "3; URL=" + referer);
		} else {
			usuarios.put(u.getUsername(), u);
		}
		app.setAttribute("usuarios", usuarios);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>" + "Procesamiento Datos Usuario" + "</title>");
		out.println("</head>");
		out.println("<body>");

		if (error) {
			out.println("<body><H1> Error: usuario duplicado </H1></body></html>");
		} else {
			out.println("<body><B><P> Datos Usuario Procesados </P> </B>");
		}
		out.println("</body>");
		out.println("</html>");
	    response.sendRedirect("index.xhtml");
	}
}
