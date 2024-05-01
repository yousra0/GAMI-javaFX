package Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.fxml.Initializable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class Stat implements Initializable {
    @FXML
    private PieChart pieChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(

                        new PieChart.Data("Reclamations", 50),
                        new PieChart.Data("Reponses", 3));


        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " reclamations: ", data.pieValueProperty()
                        )
                )
        );

        pieChart.getData().addAll(pieChartData);
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    void initialize() {
        assert pieChart != null : "fx:id=\"pieChart\" was not injected: check your FXML file 'stat.fxml'.";

    }

}
