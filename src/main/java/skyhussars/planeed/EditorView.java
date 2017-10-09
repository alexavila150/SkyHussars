/*
 * Copyright (c) 2017, ZoltanTheHun
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package skyhussars.planeed;

import skyhussars.PlaneEd;
import skyhussars.SkyHussars;
import static skyhussars.planeed.UiHelpers.*;
import java.io.File;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditorView {

    private final FileChooser fileChooser = new FileChooser();
    {fileChooser.setTitle("Open Plane Definition");}
    {fileChooser.setInitialDirectory(new File(SkyHussars.APP_ROOT));}

    public MenuBar createMenuBar(Stage stage, PlaneEd planeEd) {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        ObservableList<MenuItem> mis = fileMenu.getItems();
        mis.add(loadMenu(stage, planeEd));
        mis.add(saveMenu(planeEd));
        menuBar.getMenus().add(fileMenu);
        return menuBar;
    }

    private MenuItem loadMenu(Stage stage, PlaneEd planeEd) {
        MenuItem loadMenu = new MenuItem("Load plane");
        loadMenu.setOnAction(t -> planeEd.loadPlane(fileChooser.showOpenDialog(stage)));
        return loadMenu;
    }

    private MenuItem saveMenu(PlaneEd planeEd) {
        MenuItem saveMenu = new MenuItem("Save");
        saveMenu.setOnAction(t -> planeEd.save());
        return saveMenu;
    }

    public GridPane items(PlaneProperties planeProperties) {
        return populateGrid(2, new Label("Name:"), textFieldFor(planeProperties.getName()),
                new Label("Mass, TakeOff Max: "), numberFieldFor(planeProperties.getMassTakeOffMax()),
                new Label("Mass, Gross: "), numberFieldFor(planeProperties.getMassTakeOffMax()),
                new Label("Mass, Empty: "), numberFieldFor(planeProperties.getMassEmpty())
        );
    }
    
    XYChart.Series series = new XYChart.Series();

    public LineChart createChart() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Test Axis");
        final LineChart<Number, Number> lineChart
                = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Test Chart");
        //defining a series
        series.setName("Test Value");
        //populating the series with data
        series.getData().add(new XYChart.Data(100, 23));
        series.getData().add(new XYChart.Data(200, 14));
        series.getData().add(new XYChart.Data(300, 15));
        series.getData().add(new XYChart.Data(400, 24));
        series.getData().add(new XYChart.Data(500, 34));
        series.getData().add(new XYChart.Data(600, 36));
        series.getData().add(new XYChart.Data(700, 22));
        series.getData().add(new XYChart.Data(800, 45));
        series.getData().add(new XYChart.Data(900, 43));
        series.getData().add(new XYChart.Data(1000, 17));
        series.getData().add(new XYChart.Data(1100, 29));
        series.getData().add(new XYChart.Data(1200, 25));

        lineChart.getData().add(series);
        return lineChart;
    }
    
    public EditorView clearChart(){
        series.getData().clear();
        return this;
    }
    
    public EditorView addChartElement(float x,float y){
        series.getData().add(new XYChart.Data(x, y));
        return this;
    }

}
