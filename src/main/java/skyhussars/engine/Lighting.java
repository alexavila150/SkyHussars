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
package skyhussars.engine;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.shadow.CompareMode;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.shadow.EdgeFilteringMode;
import com.jme3.water.WaterFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class Lighting implements InitializingBean {
    
    private final static Logger logger = LoggerFactory.getLogger(Lighting.class);
    
    @Autowired
    private AssetManager assetManager;
    
    @Autowired
    private ComplexCamera camera;
    
    @Autowired
    private Node rootNode;
    
    private List<Light> lights;
    private DirectionalLight directionalLight;
    private AmbientLight ambientLight;
    
    private WaterFilter water;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        directionalLight = new DirectionalLight();
        directionalLight.setColor(ColorRGBA.White.mult(0.5f));
        directionalLight.setDirection(new Vector3f(0.0f, -1.0f, 0.0f));
        ambientLight = new AmbientLight();
        ambientLight.setColor(ColorRGBA.White.mult(0.15f));
        lights = new LinkedList<>();
        lights.add(directionalLight);
        lights.add(ambientLight);
        
        final int SHADOWMAP_SIZE = 4096;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 4);
        dlsr.setLight(directionalLight);
        dlsr.setShadowIntensity(0.3f);
        dlsr.setRenderBackFacesShadows(Boolean.TRUE);
        dlsr.setShadowCompareMode(CompareMode.Hardware);
        dlsr.setEdgeFilteringMode(EdgeFilteringMode.PCF8);
        camera.addEffect(dlsr);      
        float initialWaterHeight = 1500f; // choose a value for your scene
        /*water = new WaterFilter(rootNode, directionalLight.getDirection());
        water.setWaterHeight(initialWaterHeight);
        camera.addNearEffect(water);*/
        
        /*DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
         dlsf.setLight(directionalLight);
         dlsf.setEnabled(true);
         camera.addNearEffect(dlsf);*/
    }
    
    public List<Light> getLights() {
        return lights;
    }
    
    public void setLightingBodies(Vector3f sun, Vector3f moon) {
        float sunAt = sun.angleBetween(Vector3f.UNIT_Y);
        float moonAt = moon.angleBetween(Vector3f.UNIT_Y);
        logger.debug("Sun at: " + sunAt + ", moon at: " + moonAt);
        if (sunAt < FastMath.HALF_PI) {
            directionalLight.setDirection(sun.negate());
            ColorRGBA lightStrength = ColorRGBA.White.mult(0.8f - sunAt / 4f);
            directionalLight.setColor(lightStrength);
            ambientLight.setColor(lightStrength);
        } else if (moonAt < FastMath.HALF_PI) {
            directionalLight.setDirection(moon.negate());
            ColorRGBA lightStrength = ColorRGBA.White.mult(0.25f - moonAt / 6f);
            directionalLight.setColor(lightStrength);
            ambientLight.setColor(lightStrength);
        } else {
            directionalLight.setDirection(Vector3f.UNIT_Y.negate());
            ColorRGBA lightStrength = ColorRGBA.White.mult(0.24f);
            directionalLight.setColor(lightStrength);
            ambientLight.setColor(lightStrength);
        }
        logger.debug("Direction of light: " + directionalLight.getDirection());
        
    }
    
    public void waterEnabled(boolean enabled){
//        water.setEnabled(enabled);
    }
}
