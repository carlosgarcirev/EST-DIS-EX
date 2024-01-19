package org.vaadin.example;

import com.example.application.model.DataItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@PageTitle("Second View")
@Route("second-view")
public class SecondView extends VerticalLayout {

    private final RestTemplate restTemplate;
    private final Grid<DataItem> dataItemGrid;
    private final Select<String> msCodeSelect;

    public SecondView(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.dataItemGrid = new Grid<>(DataItem.class);
        this.msCodeSelect = new Select<>();
        setAlignItems(Alignment.CENTER);
        add(new H1("Datos agrupados por MSCode"), msCodeSelect, dataItemGrid);

        configureMsCodeSelect();
        configureDataItemGrid();
        loadMsCodes();
    }

    private void configureMsCodeSelect() {
        msCodeSelect.setLabel("Seleccione un código MsCode");
        msCodeSelect.setPlaceholder("Seleccione un código");
        msCodeSelect.setWidth("300px");

        msCodeSelect.addValueChangeListener(event -> {
            String selectedMsCode = event.getValue();
            if (selectedMsCode != null) {
                loadUserDataForMsCode(selectedMsCode);
            }
        });
    }

    private void configureDataItemGrid() {
        dataItemGrid.setColumns("year", "estCode", "estimate", "se", "lowerCIB", "upperCIB", "flag");
        dataItemGrid.setHeight("75vh");
    }

    private void loadMsCodes() {
        String apiUrl = "http://localhost:8081/mscode/codes";
        List<String> msCodes = restTemplate.getForObject(apiUrl, List.class);
        if (msCodes != null && !msCodes.isEmpty()) {
            Collections.sort(msCodes); // Ordenar alfabéticamente
            msCodeSelect.setItems(msCodes);
        }
    }

    private void loadUserDataForMsCode(String msCode) {
        String apiUrl = "http://localhost:8081/mscode/codes/" + msCode;
        ResponseEntity<List<DataItem>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<DataItem>>() {
                });

        if (response.getStatusCode() == HttpStatus.OK) {
            List<DataItem> dataItems = response.getBody();
            if (dataItems != null && !dataItems.isEmpty()) {
                dataItemGrid.setItems(dataItems);
            }
        }
    }
}