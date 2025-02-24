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

import skyhussars.engine.camera.CameraManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;

public class CameraControls implements ActionListener,AnalogListener {

    private final CameraManager cameraManager;

    public CameraControls(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
            if (name.equals(ControlsMapper.INCREASE_FOV)) {
                cameraManager.setFovMode(CameraManager.FovMode.INCREASE);
            }
            if (name.equals(ControlsMapper.DECREASE_FOV)) {
                cameraManager.setFovMode(CameraManager.FovMode.DECREASE);
            }
            if (name.equals(ControlsMapper.COCKPIT_VIEW)) {
                cameraManager.switchToView(CameraManager.CameraMode.COCKPIT_VIEW);
            }
            if (name.equals(ControlsMapper.OUTER_VIEW)) {
                cameraManager.switchToView(CameraManager.CameraMode.OUTER_VIEW);
            }
            if (name.equals(ControlsMapper.CENTER_CAMERA)) {
                cameraManager.centerCamera();
            }
        } else {
            if (name.equals(ControlsMapper.INCREASE_FOV) || name.equals(ControlsMapper.DECREASE_FOV)) {
                cameraManager.setFovMode(CameraManager.FovMode.STABLE);
            }
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        switch (name) {
            case ControlsMapper.MOUSE_LEFT:
                cameraManager.rotateCamera(CameraManager.CameraPlane.X,-value,tpf);
                break;
            case ControlsMapper.MOUSE_RIGHT:
                cameraManager.rotateCamera(CameraManager.CameraPlane.X,value,tpf);
                break;
            case ControlsMapper.MOUSE_UP:
                cameraManager.rotateCamera(CameraManager.CameraPlane.Y,value,tpf);
                break;
            case ControlsMapper.MOUSE_DOWN:
                cameraManager.rotateCamera(CameraManager.CameraPlane.Y,-value,tpf);
                break;
        }
    }
    
    
}
