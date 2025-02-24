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

import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.requireNonNull;
import skyhussars.engine.physics.PlanePhysics;
import skyhussars.engine.physics.PlaneResponse;
import skyhussars.engine.physics.environment.Environment;


public class LevelFlightSimulation {
    private final PlanePhysics physics;
    private final Environment environment;
    
    public LevelFlightSimulation(PlanePhysics physics, Environment environment){
        this.physics = requireNonNull(physics);
        this.environment = requireNonNull(environment);
    }
    
    public List<PlaneResponse> simulate(float tickrate,int iterations,int sampling,PlaneResponse initial){
        ArrayList<PlaneResponse> rsps = new ArrayList((int) (iterations/sampling)+1);
        rsps.add(initial);
        PlaneResponse simulated = initial;
        for(int i = 1;i<= iterations;i++){ //it starts from 1 and goes to iterations
            PlaneResponse old = simulated;
            simulated = physics.update(tickrate, environment, simulated);          
            System.out.println("Distance: " + old.translation.distance(simulated.translation));
            System.out.println("Height: " + (old.height() - simulated.height()));
            System.out.println("Velocity diff: " + (old.velicityKmh() - simulated.velicityKmh()));
            System.out.println("Velocity Actual: " + (old.velicityKmh()));
            System.out.println("Aoa:" + old.aoa);
            if(i%sampling == 0) {rsps.add(simulated); System.out.println("Sample " + rsps.size());}
        }

        return rsps;
    }
}
