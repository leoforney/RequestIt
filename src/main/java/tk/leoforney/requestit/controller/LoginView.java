package tk.leoforney.requestit.controller;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

@Route("login")
@PageTitle("RequestIt | Login")
@Theme(value = Material.class, variant = Material.DARK)
public class LoginView extends VerticalLayout implements PageConfigurator, AfterNavigationObserver {

    public LoginView() {
        add(new H2("Login to RequestIt"));

        EmailField emailField = new EmailField();
        emailField.getElement().setAttribute("name", "email");

        PasswordField passwordField = new PasswordField();
        passwordField.getElement().setAttribute("name", "password");

        /*

        Button submitButton = new Button("LoginView");
        submitButton.setId("submitbutton");
        submitButton.setVisible(false);
        submitButton.getElement().setAttribute("type", "submit");*/

        NativeButton submitButtonNative = new NativeButton("Login");
        submitButtonNative.setId("nativeSubmit");

        FormLayout formLayout = new FormLayout();
        formLayout.add(emailField, passwordField, /*submitButton,*/ submitButtonNative);

        Element formElement = new Element("form");
        formElement.setAttribute("method", "post");
        formElement.setAttribute("action", "login");
        formElement.appendChild(formLayout.getElement());

        getElement().appendChild(formElement);

        setClassName("login");
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        // Force login page to use Shady DOM to avoid problems with browsers and
        // password managers not supporting shadow DOM
        settings.addInlineWithContents(InitialPageSettings.Position.PREPEND,
                "window.customElements=window.customElements||{};" +
                        "window.customElements.forcePolyfill=true;" +
                        "window.ShadyDOM={force:true};", InitialPageSettings.WrapMode.JAVASCRIPT);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        boolean error = event.getLocation().getQueryParameters().getParameters().containsKey("error");
        if (error) {
            Notification.show("Incorrect email or password");
        }
    }

}
