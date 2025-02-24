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
package skyhussars.engine.mission;

import skyhussars.engine.SkyComponent;
import skyhussars.engine.DayLightWeatherManager;
import skyhussars.engine.DataModel;
import skyhussars.engine.terrain.TerrainManager;
import skyhussars.engine.camera.CameraManager;
import skyhussars.engine.sound.SoundManager;
import skyhussars.engine.gamestates.MissionState;
import skyhussars.engine.controls.ControlsManager;
import skyhussars.engine.plane.Plane;
import skyhussars.engine.plane.PlaneFactory;
import skyhussars.engine.weapons.ProjectileManager;
import com.jme3.scene.Node;
import com.jme3.scene.plugins.blender.math.Vector3d;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MissionFactory {

    @Autowired
    private Node rootNode;
    @Autowired
    private DataModel dataModel;
    @Autowired
    private ProjectileManager projectileManager;
    @Autowired
    private SoundManager soundManager;
    @Autowired
    private CameraManager cameraManager;
    @Autowired
    private TerrainManager terrainManager;
    @Autowired
    private PlaneFactory planeFactory;
    @Autowired
    private DayLightWeatherManager dayLightWeatherManager;
    @Autowired
    private ControlsManager controlsManager;
    @Autowired
    private SkyComponent sky;

    public MissionState mission(String missionName) {
        MissionDescriptor missionDescriptor = dataModel.getMissionDescriptor(missionName);
        List<Plane> planes = planes(missionDescriptor);
        soundManager.clear();
        MissionState mission = new MissionState(planes, projectileManager, soundManager, cameraManager, terrainManager, dayLightWeatherManager, rootNode, sky);
        controlsManager.missionControls(mission);
        terrainManager.loadTheatre("Adria");
        sky.init();
        dayLightWeatherManager.setLights();
        return mission;
    }

    public MissionState mission(String planeType, int enemyCount,String theatre) {
        MissionDescriptor missionDescriptor = dataModel.getNewMission(planeType, enemyCount);
        List<Plane> planes = planes(missionDescriptor);
        MissionState mission = new MissionState(planes, projectileManager, soundManager, cameraManager, terrainManager, dayLightWeatherManager, rootNode, sky);
        controlsManager.missionControls(mission);
        terrainManager.loadTheatre(theatre);
        sky.init();
        dayLightWeatherManager.setLights();
        return mission;
    }

    private List<Plane> planes(MissionDescriptor missionDescriptor) {
        List<Plane> planes = new ArrayList<>();
        for (PlaneMissionDescriptor planeMission : missionDescriptor.planeMissionDescriptors()) {
            Plane plane = planeFactory.createPlane(planeMission.planeType());
            Vector3d startLocation = new Vector3d(planeMission.startLocation());
            plane.setLocation(startLocation);
            plane.velocityKmh(400);
            plane.planeMissinDescriptor(planeMission);
            plane.setThrottle(0.6f);
            rootNode.attachChild(plane.planeGeometry().root());
            planes.add(plane);
        }
        return planes;
    }

}
