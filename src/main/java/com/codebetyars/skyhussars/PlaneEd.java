/*
 * Copyright (c) 2016, ZoltanTheHun
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
package com.codebetyars.skyhussars;

import com.codebetyars.skyhussars.engine.loader.PlaneDescriptorMarshal;
import com.codebetyars.skyhussars.engine.plane.AirfoilDescriptor;
import com.codebetyars.skyhussars.engine.plane.PlaneDescriptor;
import com.codebetyars.skyhussars.planeed.EditorView;
import com.codebetyars.skyhussars.planeed.PlaneEdState;
import com.codebetyars.skyhussars.planeed.PlaneProperties;
import com.codebetyars.skyhussars.planeed.AirfoilTable;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.Arrays;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PlaneEd extends Application {

    private final PlaneProperties planeProperties = new PlaneProperties();

    public static void main(String[] args) {
        launch(args);
    }

    private PlaneEdState state = new PlaneEdState();
    private final PlaneDescriptorMarshal pdl = new PlaneDescriptorMarshal();
    private File openFile;


    @Override
    public void start(Stage stage) throws Exception {
        EditorView ev = new EditorView();
        stage.setTitle("SkyHussars PlaneEd");
        VBox root = new VBox();
        root.getChildren().add(ev.createMenuBar(stage, this));
        root.getChildren().add(ev.items(planeProperties));
        AirfoilDescriptor afd = new AirfoilDescriptor();
        afd.setName("MyTestName");
        root.getChildren().add(new AirfoilTable().airfoils(Lists.newArrayList(afd)).wingTable());
        stage.setScene(new Scene(root, 500, 400));
        stage.show();
    }

    public void loadPlane(File file) {
        state = state.planeDescriptor(pdl.unmarshal(file)).openFile(file);
        planeProperties.getName().setValue(state.planeDescriptor().map(p -> p.getName()).orElse(""));
        planeProperties.getMassTakeOffMax().setValue(state.planeDescriptor().map(p -> p.getMassTakeOffMax()).orElse(0.f));
        planeProperties.getMassGross().setValue(state.planeDescriptor().map(p -> p.getMassGross()).orElse(0.f));
        planeProperties.getMassEmpty().setValue(state.planeDescriptor().map(p -> p.getMassEmpty()).orElse(0.f));
        openFile = file; // we set this only if no issue unmarshalling the file
    }
    
    public void save() {
        PlaneDescriptor pd = state.planeDescriptor().orElseThrow(IllegalStateException::new);
        PlaneProperties pp = planeProperties;
        pd.setName(pp.getName().getValue());
        pd.setMassTakeOffMax(pp.getMassTakeOffMax().getValue());
        pd.setMassGross(pp.getMassGross().getValue());
        pd.setMassEmpty(pp.getMassEmpty().getValue());
        if(openFile != null) pdl.marshal(pd, openFile); //this is fine for now
    }
}
