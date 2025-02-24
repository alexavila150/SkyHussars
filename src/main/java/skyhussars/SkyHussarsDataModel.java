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
package skyhussars;

import skyhussars.engine.DataModel;
import skyhussars.engine.mission.MissionDescriptor;
import skyhussars.engine.mission.PlaneMissionDescriptor;
import com.jme3.math.Vector3f;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SkyHussarsDataModel implements DataModel, InitializingBean {

    private Map<String, MissionDescriptor> missions = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {

        PlaneMissionDescriptor planeDescriptor = new PlaneMissionDescriptor();
        planeDescriptor.player(true);
        planeDescriptor.planeType("Lockheed P-80A-1-LO Shooting Star");
        planeDescriptor.startLocation(new Vector3f(0, 3000, 0));

        List<PlaneMissionDescriptor> planes = generatePlanes(0);
        planes.add(planeDescriptor);
        MissionDescriptor missionDescriptor = new MissionDescriptor();
        missionDescriptor.name("Test mission");
        missionDescriptor.planeMissionDescriptors(planes);

        missions.put(missionDescriptor.name(), missionDescriptor);
    }

    private List<PlaneMissionDescriptor> generatePlanes(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Cannot generate less than 1 planes");
        }
        List<PlaneMissionDescriptor> planes = new LinkedList<>();
        Random r = new Random();
        int dir = 1;
        for (int i = 0; i < n; i++) {
            PlaneMissionDescriptor planeMissionDescriptor = new PlaneMissionDescriptor();
            planeMissionDescriptor.player(false);
            planeMissionDescriptor.planeType("Lockheed P-80A-1-LO Shooting Star");
            planeMissionDescriptor.startLocation(new Vector3f(dir * (r.nextInt(500) + i * 10), 3000 + r.nextInt(1500) - 750, r.nextInt(500) + i * 1));
            planes.add(planeMissionDescriptor);
            dir *= -1;
        }
        return planes;
    }

    @Override
    public MissionDescriptor getMissionDescriptor(String name) {
        return missions.get(name);
    }

    @Override
    public MissionDescriptor getNewMission(String playerPlane, int enemyCount) {
        MissionDescriptor missionDescriptor = new MissionDescriptor();
        PlaneMissionDescriptor planeMissionDescriptor = new PlaneMissionDescriptor();
        planeMissionDescriptor.player(true);
        planeMissionDescriptor.planeType(playerPlane);
        planeMissionDescriptor.startLocation(new Vector3f(0, 3000, 0));
        List<PlaneMissionDescriptor> planes = generatePlanes(enemyCount);
        planes.add(planeMissionDescriptor);
        missionDescriptor.planeMissionDescriptors(planes);
        return missionDescriptor;
    }
}
