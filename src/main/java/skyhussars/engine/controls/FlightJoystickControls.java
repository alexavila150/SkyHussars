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
package skyhussars.engine.controls;

import skyhussars.engine.Pilot;
import com.jme3.input.controls.AnalogListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightJoystickControls implements AnalogListener {

    private final static Logger logger = LoggerFactory.getLogger(FlightJoystickControls.class);

    private final Pilot pilot;
    private final float deadzone;
    private final JoyStatus joyStatus;

    public FlightJoystickControls(Pilot pilot,JoyStatus joyStatus,float deadzone) {
        this.pilot = pilot;
        this.deadzone = deadzone;
        this.joyStatus = joyStatus;
    }

    @Override
    public void onAnalog(String string, float axis, float tpf) {
        if(axis/tpf < deadzone) axis = 0;
        if (ControlsMapper.JOY_ROLL_LEFT.equals(string)) {
            joyStatus.setX(-1*axis/tpf);
            pilot.setAileron(-1 * axis / tpf);
        }
        if (ControlsMapper.JOY_ROLL_RIGHT.equals(string)) {
            joyStatus.setX(axis/tpf);
            pilot.setAileron(axis / tpf);
        }
        if (ControlsMapper.JOY_PITCH_DOWN.equals(string)) {
            joyStatus.setY(-1*axis/tpf);
            pilot.setElevator(-1 * axis / tpf);
        }
        if (ControlsMapper.JOY_PITCH_UP.equals(string)) {
            joyStatus.setY(axis/tpf);
            pilot.setElevator(axis / tpf);
        }
    }

}
