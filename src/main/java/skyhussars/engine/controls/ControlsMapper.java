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
package skyhussars.engine.controls;

import skyhussars.engine.controls.FlightKeyboardMap.Trigger;
import skyhussars.engine.gamestates.Options;
import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import java.util.Arrays;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static skyhussars.utility.Streams.*;

@Component
public class ControlsMapper {

    public static final String INCREASE_FOV = "IncreaseFov";
    public static final String DECREASE_FOV = "DecreaseFov";
    public static final String JOY_PITCH_UP = "PitchUp";
    public static final String JOY_PITCH_DOWN = "PitchDown";
    public static final String COCKPIT_VIEW = "CockpitView";
    public static final String OUTER_VIEW = "OuterView";
    public static final String MOUSE_LEFT = "MouseLeft";
    public static final String MOUSE_RIGHT = "MouseRight";
    public static final String MOUSE_DOWN = "MouseDown";
    public static final String MOUSE_UP = "MouseUp";
    public static final String CENTER_CAMERA = "CenterCamera";
    public static final String MENU_DISPLAY = "OpenMenu";
    public static final String JOY_ROLL_LEFT = "RotateLeft";
    public static final String JOY_ROLL_RIGHT = "RotateRight";
    public static final String PAUSE = "Pause";
    public static final String CAMERA = "Camera";
    public static final String RESET = "Reset";

    @Autowired
    private InputManager inputManager;
    
    @Autowired
    private Options options;
    
    public void setupFlowControls(ActionListener actionListener) {
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_HIDE_STATS);
        inputManager.addMapping(PAUSE, new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping(CAMERA, new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping(RESET, new KeyTrigger(KeyInput.KEY_R));
        inputManager.addListener(actionListener, PAUSE, CAMERA, RESET);
    }
    
    private Optional<Joystick> joy(String name){
        if(inputManager.getJoysticks() != null)
            for (Joystick joy : inputManager.getJoysticks()) 
                if(name.equals(joy.getName())) return Optional.of(joy);
        return Optional.empty();
    }
    
    private void setupJoystickMapping(Joystick joy,FlightJoystickControls flightJoystrictControls){
        inputManager.addMapping(JOY_ROLL_RIGHT, new JoyAxisTrigger(joy.getJoyId(), joy.getXAxisIndex(), false));
        inputManager.addMapping(JOY_ROLL_LEFT, new JoyAxisTrigger(joy.getJoyId(), joy.getXAxisIndex(), true));
        inputManager.addMapping(JOY_PITCH_UP, new JoyAxisTrigger(joy.getJoyId(), joy.getYAxisIndex(), false));
        inputManager.addMapping(JOY_PITCH_DOWN, new JoyAxisTrigger(joy.getJoyId(), joy.getYAxisIndex(), true));
        inputManager.addMapping(Trigger.FIRE.name(), new JoyButtonTrigger(joy.getJoyId(),0));
        inputManager.addListener(flightJoystrictControls, JOY_PITCH_UP, JOY_PITCH_DOWN, JOY_ROLL_LEFT, JOY_ROLL_RIGHT);
    }
    
    public void setupFlightJoystickControls(FlightJoystickControls flightJoystrictControls) {
        if(options.getJoy().isPresent()){
            Optional<Joystick> joy = joy(options.getJoy().get());
            if(joy.isPresent()) setupJoystickMapping(joy.get(),flightJoystrictControls);
        }
    }
    
    public void setupFlightKeyboardControls(FlightKeyboardControls flightKeyboardControls) {
        FlightKeyboardMap flightKbMap = new FlightKeyboardMap();
        for(Trigger t : Trigger.values()){
            inputManager.deleteMapping(t.name());
            inputManager.addMapping(t.name(),new KeyTrigger(flightKbMap.getMapping(t)));
        }
        inputManager.addListener(flightKeyboardControls,
                Arrays.stream(Trigger.values()).map( t -> t.name()).toArray(String[]::new));
    }
    
    public void setupCameraControls(CameraControls cameraControls) {
        inputManager.addMapping(INCREASE_FOV, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping(DECREASE_FOV, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(COCKPIT_VIEW, new KeyTrigger(KeyInput.KEY_F1));
        inputManager.addMapping(OUTER_VIEW, new KeyTrigger(KeyInput.KEY_F5));
        inputManager.addMapping(MOUSE_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping(MOUSE_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping(MOUSE_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping(MOUSE_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping(CENTER_CAMERA, new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE), new KeyTrigger(KeyInput.KEY_NUMPAD5));
        inputManager.addListener(cameraControls, INCREASE_FOV, DECREASE_FOV, COCKPIT_VIEW,
                OUTER_VIEW, MOUSE_UP, MOUSE_DOWN, MOUSE_RIGHT, MOUSE_LEFT, CENTER_CAMERA);
    }
    
    public void setupMenuControls(MenuControls menuControls) {
        inputManager.addMapping(MENU_DISPLAY, new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(menuControls, MENU_DISPLAY);
    }
    
    public void stripControl(InputListener... listeners){
        for(InputListener l : listeners) inputManager.removeListener(l);
    }
    
    public List<String> joys(){ 
        return list(sm(asList(inputManager.getJoysticks()), joy -> joy.getName()));
    }
}
