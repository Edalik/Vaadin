package abc.vaadin.views.layout;

import abc.vaadin.data.entity.Role;
import abc.vaadin.data.entity.User;
import abc.vaadin.data.service.UserService;
import abc.vaadin.security.SecurityService;
import abc.vaadin.views.*;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, Component icon, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.XSMALL, LumoUtility.Height.MEDIUM, LumoUtility.AlignItems.CENTER, LumoUtility.Padding.Horizontal.SMALL,
                    LumoUtility.TextColor.BODY);
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames(LumoUtility.FontWeight.MEDIUM, LumoUtility.FontSize.MEDIUM, LumoUtility.Whitespace.NOWRAP);

            if (icon != null) {
                link.add(icon);
            }
            link.add(text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

    }

    private final transient AuthenticationContext authContext;
    private SecurityService securityService;
    private UserService userService;
    User user;

    public MainLayout(SecurityService securityService, AuthenticationContext authContext, UserService userService) {
        this.securityService = securityService;
        this.userService = userService;
        this.authContext = authContext;

        user = userService.findByLogin(securityService.getAuthenticatedUser().getUsername());

        addToNavbar(createHeaderContent());
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.Width.FULL);

        Div layout = new Div();
        layout.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.Padding.Horizontal.LARGE);

        H1 appName = new H1("Vaadin");
        appName.addClassNames(LumoUtility.Margin.Vertical.MEDIUM, LumoUtility.Margin.End.AUTO, LumoUtility.FontSize.LARGE);
        layout.add(appName);


        MenuBar userMenu = new MenuBar();
        userMenu.setThemeName("tertiary-inline contrast");

        MenuItem userName = userMenu.addItem("");
        Div div = new Div();
        div.add(user.getSurname() + " " + user.getName());
        Avatar avatar = new Avatar();
        avatar.setImage(user.getAvatar());
        div.add(avatar);
        div.add(new Icon("lumo", "dropdown"));
        div.getElement().getStyle().set("display", "flex");
        div.getElement().getStyle().set("align-items", "center");
        div.getElement().getStyle().set("gap", "var(--lumo-space-s)");
        userName.add(div);
        userName.getSubMenu().addItem("Профиль", e -> {
            UI.getCurrent().navigate("profile");
        });
        userName.getSubMenu().addItem("Выйти", e -> {
            securityService.logout();
        });

        layout.add(userMenu);

        Nav nav = new Nav();
        nav.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Overflow.AUTO, LumoUtility.Padding.Horizontal.MEDIUM, LumoUtility.Padding.Vertical.XSMALL);

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames(LumoUtility.Display.FLEX, LumoUtility.Gap.SMALL, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.NONE, LumoUtility.Padding.NONE);
        nav.add(list);

        MenuItemInfo[] menuItems = createMenuItems();
        if (securityService.getAuthenticatedUser().getAuthorities().toString().contains(Role.ADMIN.toString())) {
            list.add(menuItems);
        } else {
            list.add(menuItems[0], menuItems[1]);
        }

        header.add(layout, nav);
        return header;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{
                new MenuItemInfo("Товары", VaadinIcon.DATABASE.create(), ProductView.class),
                new MenuItemInfo("Корзина", VaadinIcon.DATABASE.create(), CartView.class),
                new MenuItemInfo("Цвета", VaadinIcon.DATABASE.create(), ColorView.class),
                new MenuItemInfo("Категории", VaadinIcon.DATABASE.create(), CategoryView.class),
                new MenuItemInfo("Статусы", VaadinIcon.DATABASE.create(), StatusView.class),
                new MenuItemInfo("Города", VaadinIcon.DATABASE.create(), CityView.class),
                new MenuItemInfo("Поставщики", VaadinIcon.DATABASE.create(), ProviderView.class),
                new MenuItemInfo("Пользователи", VaadinIcon.DATABASE.create(), UserView.class)
        };
    }
}