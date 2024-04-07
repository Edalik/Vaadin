package abc.vaadin.views;

import abc.vaadin.components.EditForm;
import abc.vaadin.components.EditFormEvents;
import abc.vaadin.components.ProductFilters;
import abc.vaadin.data.entity.AbstractEntity;
import abc.vaadin.data.service.AbstractService;
import abc.vaadin.data.service.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouteParameters;

public abstract class GridView<T extends AbstractEntity> extends VerticalLayout implements BeforeEnterObserver {
    protected final AbstractService<T> entityService;
    protected final ProductService productService;
    protected final Grid<T> grid;
    private final ConfigurableFilterDataProvider<T, Void, String> dataProvider;
    private T selectedEntity;
    protected final ProductFilters productFilters;
    private final TextField filterText;
    private Button addButton;
    private Button editButton;
    private final HorizontalLayout toolBar;
    private EditForm<T> dialog;

    public GridView(AbstractService<T> entityService, ProductService productService) {
        this.entityService = entityService;
        this.productService = productService;
        this.dataProvider = getDataProvider();

        this.filterText = createFilterText();
        this.grid = createGrid();
        this.dialog = createDialog();
        this.productFilters = createFilters();
        this.toolBar = createToolbar();

        setPadding(true);
        setSizeFull();

        if (productFilters != null) {
            add(
                    productFilters,
                    toolBar,
                    createContent()
            );
        } else {
            add(
                    toolBar,
                    createContent()
            );
        }
    }

    private ConfigurableFilterDataProvider<T, Void, String> getDataProvider() {
        return entityService.withConfigurableFilter();
    }

    //region Field-components initialization

    private ProductFilters createFilters() {

        return createFiltersProto();
    }

    protected abstract ProductFilters createFiltersProto();

    private TextField createFilterText() {
        TextField filterText = new TextField();
        filterText.setWidth("500px");
        filterText.setPlaceholder("Поиск...");
        filterText.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> dataProvider.setFilter(e.getValue()));

        return filterText;
    }

    protected Grid<T> createGrid() {
        var grid = new Grid<T>();
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(event -> selectedEntity = event.getValue());
        grid.asSingleSelect().addValueChangeListener(event -> editButton.setEnabled(event.getValue() != null));
        grid.setDataProvider(dataProvider);

        return grid;
    }

    private EditForm<T> createDialog() {
        var dialog = createDialogProto();
        dialog.addListener(EditFormEvents.SaveEvent.class, this::saveEntity);
        dialog.addListener(EditFormEvents.DeleteEvent.class, this::deleteEntity);

        return dialog;
    }

    protected abstract EditForm<T> createDialogProto();

    protected void resetDialog() {
        this.dialog = createDialog();
    }

    //endregion

    //region Anonymous components initialization

    private HorizontalLayout createToolbar() {
        return new HorizontalLayout(filterText, createAddButton(), createEditButton());
    }

    private Component createAddButton() {
        addButton = new Button("Добавить");
        addButton.addClickListener(click -> addEntity());

        return addButton;
    }

    private Component createEditButton() {
        editButton = new Button("Изменить");
        editButton.setEnabled(false);
        editButton.addClickListener(click -> editEntity(selectedEntity));

        return editButton;
    }

    private Component createContent() {
        return grid;
    }

    //endregion

    //region Form configure
    protected void setFilterTextVisibility(boolean isVisible){
        filterText.setVisible(isVisible);
    }

    protected void setAddButtonVisibility(boolean isVisible){
        addButton.setVisible(isVisible);
    }

    protected void setEditButtonVisibility(boolean isVisible){
        editButton.setVisible(isVisible);
    }

    protected void addToToolBar(Component... components){
        toolBar.add(components);
    }
    //endregion

    //region Form logic

    private void addEntity() {
        grid.asSingleSelect().clear();
        editEntity(dialog.createEntity());
    }

    protected void saveEntity(EditFormEvents.SaveEvent event) {
        var entityToSave = dialog.getEntity();
        if (!entityService.contains(entityToSave)) {
            entityService.update(entityToSave);
        } else {
            entityService.update(entityToSave);
        }

        refreshGrid();
        dialog.close();
    }

    private void deleteEntity(EditFormEvents.DeleteEvent event) {
        var entityToDelete = dialog.getEntity();
        entityService.delete(entityToDelete);
        refreshGrid();
        dialog.close();
    }

    protected void editEntity(T entity) {
        dialog.open();
        dialog.setEntity(entity);
    }

    protected void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

    //endregion

    //region Row selection

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        try {
            var parameterName = checkEventAndGetIdParam(beforeEnterEvent);
            var id = getIdParamValue(beforeEnterEvent.getRouteParameters(), parameterName);
            grid.select(entityService.findById(id));
        } catch (Exception ignored) {
        }
    }

    private String checkEventAndGetIdParam(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.isRefreshEvent())
            return null;

        return beforeEnterEvent
                .getRouteParameters()
                .getParameterNames()
                .stream()
                .filter(param -> param.contains("Id"))
                .findFirst()
                .orElse(null);
    }

    private Long getIdParamValue(RouteParameters params, String parameterName) {
        if (parameterName == null)
            throw new IllegalArgumentException();

        var parameterValue = params.get(parameterName);
        if (parameterValue.isEmpty())
            throw new NullPointerException();
        return Long.parseLong(parameterValue.get());
    }

    //endregion
}
