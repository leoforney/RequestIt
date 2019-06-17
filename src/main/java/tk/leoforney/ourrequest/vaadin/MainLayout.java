package tk.leoforney.ourrequest.vaadin;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Push
@Theme(Lumo.class)
public class MainLayout extends Div implements RouterLayout
{
    private static final long serialVersionUID = -218890586070440330L;
}