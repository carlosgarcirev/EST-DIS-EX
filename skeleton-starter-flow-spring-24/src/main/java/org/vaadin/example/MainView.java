package org.vaadin.example;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.application.model.DataItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@PageTitle("Datos generales")
@Route(value = "")
public class MainView extends VerticalLayout {

    private final RestTemplate restTemplate;
    private final Grid<DataItem> grid;

    @Autowired
    public MainView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.grid = new Grid<>();

        // Configurar la grilla
        configureGrid();

        // Botón para agregar nuevo elemento
        Button addButton = new Button("Nuevo");
        addButton.addClickListener(event -> openNewDialog()); // Crear nuevo DataItem

        // Layout horizontal para los botones
        HorizontalLayout buttonsLayout = new HorizontalLayout(addButton);
        buttonsLayout.setWidthFull(); // Ocupa todo el ancho disponible

        // Configurar estilo de Grid
        grid.setHeight("75vh"); // 75% de la altura disponible
        setAlignItems(Alignment.CENTER);

        // Añadir componentes al layout principal
        add(new H1("Datos generales"), grid, buttonsLayout);

        // Botón para ir a la segunda vista
        Button goToSecondViewButton = new Button("Datos agrupados por MsCode");
        RouterLink routerLink = new RouterLink("", SecondView.class);
        routerLink.add(goToSecondViewButton);

        // Añadir componentes al layout principal
        add(routerLink); // Añade el RouterLink en lugar del botón directamente
    }

    private void configureGrid() {
        // Hacer la petición GET
        String apiUrl = "http://localhost:8081/users";
        String response = restTemplate.getForObject(apiUrl, String.class);

        // Procesar la respuesta JSON
        ObjectMapper objectMapper = new ObjectMapper();
        List<DataItem> dataItems;
        try {
            dataItems = Arrays.asList(objectMapper.readValue(response, DataItem[].class));
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la respuesta JSON", e);
        }

        // Añadir las columnas con los nombres de las propiedades y habilitar la ordenación
        grid.setItems(dataItems);
        grid.addColumn(DataItem::get_id).setHeader("ID").setSortable(true);
        grid.addColumn(DataItem::getMscode).setHeader("mscode").setSortable(true);
        grid.addColumn(DataItem::getYear).setHeader("year").setSortable(true);
        grid.addColumn(DataItem::getEstCode).setHeader("estCode").setSortable(true);
        grid.addColumn(DataItem::getEstimate).setHeader("estimate").setSortable(true);
        grid.addColumn(DataItem::getSe).setHeader("se").setSortable(true);
        grid.addColumn(DataItem::getLowerCIB).setHeader("lowerCIB").setSortable(true);
        grid.addColumn(DataItem::getUpperCIB).setHeader("upperCIB").setSortable(true);
        grid.addColumn(DataItem::getFlag).setHeader("flag").setSortable(true);

        // Configurar el manejo de eventos de doble clic en la grilla
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                openEditDialog(event.getValue());
            }
        });
    }

    private void openNewDialog() {
        // Crear diálogo modal para agregar nuevos elementos
        Dialog dialog = new Dialog();
        VerticalLayout formLayout = new VerticalLayout();

        // El id será un UUID generado aleatoriamente que se convertirá a String
        String id = UUID.randomUUID().toString();

        // Campos del formulario
        TextField mscodeField = new TextField("mscode");
        formLayout.add(mscodeField);

        TextField yearField = new TextField("year");
        formLayout.add(yearField);

        // Añadir más campos según la estructura de tu clase DataItem
        TextField estCodeField = new TextField("estCode");
        formLayout.add(estCodeField);

        TextField estimateField = new TextField("estimate");
        formLayout.add(estimateField);

        TextField seField = new TextField("se");
        formLayout.add(seField);

        TextField lowerCIBField = new TextField("lowerCIB");
        formLayout.add(lowerCIBField);

        TextField upperCIBField = new TextField("upperCIB");
        formLayout.add(upperCIBField);

        TextField flagField = new TextField("flag");
        formLayout.add(flagField);

        // Botones de Aceptar y Cancelar
        Button acceptButton = new Button("Aceptar", event -> {
            // Crear un nuevo objeto DataItem con los valores del formulario
            DataItem newItem = new DataItem();
            newItem.set_id(UUID.fromString(id));
            newItem.setMscode(mscodeField.getValue());
            newItem.setYear(yearField.getValue());
            newItem.setEstCode(estCodeField.getValue());
            newItem.setEstimate(Double.parseDouble(estimateField.getValue()));
            newItem.setSe(Double.parseDouble(seField.getValue()));
            newItem.setLowerCIB(Double.parseDouble(lowerCIBField.getValue()));
            newItem.setUpperCIB(Double.parseDouble(upperCIBField.getValue()));
            newItem.setFlag(flagField.getValue());

            try {
                // Enviar una petición POST para agregar un nuevo elemento
                restTemplate.postForEntity(
                        "http://localhost:8081/users",
                        newItem,
                        DataItem.class
                );

                // Si no hay excepción, la respuesta fue exitosa
                // Pedimos un get y actualizamos la grilla
                grid.getDataProvider().refreshAll();

                // Cerrar el diálogo
                dialog.close();
            } catch (Exception ex) {
                // Manejar errores
                String responseBody = ex.getMessage();
                throw new RuntimeException("Error al crear el nuevo elemento. Cuerpo de la respuesta: " + responseBody);
            }
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        // Añadir componentes al formulario
        formLayout.add(acceptButton, cancelButton);

        // Añadir el formulario al diálogo
        dialog.add(formLayout);

        // Mostrar el diálogo modal
        dialog.open();
    }

    private void openEditDialog(DataItem item) {
        // Crear diálogo modal para editar/crear elementos
        Dialog dialog = new Dialog();
        VerticalLayout formLayout = new VerticalLayout();

        // Campos del formulario
        TextField mscodeField = new TextField("mscode");
        mscodeField.setValue(item.getMscode());
        formLayout.add(mscodeField);

        TextField yearField = new TextField("year");
        yearField.setValue(item.getYear());
        formLayout.add(yearField);

        TextField estCodeField = new TextField("estCode");
        estCodeField.setValue(item.getEstCode());
        formLayout.add(estCodeField);

        // Añadir más campos según la estructura de tu clase DataItem
        TextField estimateField = new TextField("estimate");
        estimateField.setValue(String.valueOf(item.getEstimate()));
        formLayout.add(estimateField);

        TextField seField = new TextField("se");
        seField.setValue(String.valueOf(item.getSe()));
        formLayout.add(seField);

        TextField lowerCIBField = new TextField("lowerCIB");
        lowerCIBField.setValue(String.valueOf(item.getLowerCIB()));
        formLayout.add(lowerCIBField);

        TextField upperCIBField = new TextField("upperCIB");
        upperCIBField.setValue(String.valueOf(item.getUpperCIB()));
        formLayout.add(upperCIBField);

        TextField flagField = new TextField("flag");
        flagField.setValue(item.getFlag());
        formLayout.add(flagField);

        // Botones de Aceptar y Cancelar
        Button acceptButton = new Button("Aceptar", event -> {
            // Actualizar los campos del objeto item con los valores del formulario
            item.setMscode(mscodeField.getValue());
            item.setYear(yearField.getValue());
            item.setEstCode(estCodeField.getValue());
            item.setEstimate(Double.parseDouble(estimateField.getValue()));
            item.setSe(Double.parseDouble(seField.getValue()));
            item.setLowerCIB(Double.parseDouble(lowerCIBField.getValue()));
            item.setUpperCIB(Double.parseDouble(upperCIBField.getValue()));
            item.setFlag(flagField.getValue());

            try {
                // Determinar si es una operación de creación o actualización
                if (item.get_id() == null) {
                    // Crear un nuevo elemento
                    restTemplate.postForEntity(
                            "http://localhost:8081/users",
                            item,
                            DataItem.class
                    );
                } else {
                    // Actualizar un elemento existente
                    restTemplate.exchange(
                            "http://localhost:8081/users/" + item.get_id(),
                            HttpMethod.PUT,
                            new HttpEntity<>(item),
                            DataItem.class
                    );
                }

                // Si no hay excepción, la respuesta fue exitosa
                grid.getDataProvider().refreshAll();

                // Cerrar el diálogo
                dialog.close();
            } catch (Exception ex) {
                // Manejar errores
                String responseBody = ex.getMessage();
                throw new RuntimeException("Error al actualizar/crear el elemento. Cuerpo de la respuesta: " + responseBody);
            }
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        // Añadir componentes al formulario
        formLayout.add(acceptButton, cancelButton);

        // Añadir el formulario al diálogo
        dialog.add(formLayout);

        // Mostrar el diálogo modal
        dialog.open();
    }
}
