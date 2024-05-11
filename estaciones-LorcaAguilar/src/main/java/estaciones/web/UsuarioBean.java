package estaciones.web;

import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.IOException;
import java.io.Serializable;

@Named
@SessionScoped
public class UsuarioBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean gestor = false;
    private boolean previousGestor = false; // To track changes in gestor status

    public boolean isGestor() {
        return gestor;
    }

    public void setGestor(boolean gestor) {
        if (this.gestor != gestor) { // Check if the value is actually changing
            this.gestor = gestor;
            System.out.println("Gestor changed to: " + gestor);
            // Redirect user whenever there is a change in the gestor status
            redirectToIndex();
        }
    }

    private void redirectToIndex() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            context.getExternalContext().redirect("http://localhost:8081/index.xhtml"); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkAccess() {
        System.out.println("Entering method");
        if (!isGestor()) {
            System.out.println("In method: false - gestor is not authorized");
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("http://localhost:8081/accessDenied.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
