package skyhussars.engine;

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
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.File;
/**
 * 
 * At this moment the only purpose of SettingManager is to store the location of the game assets.
 */
public class SettingsManager {

    private File assetDirectory;
    /**
     * 
     * @return The root directory of all game assets
     */
    public File assetDirectory() { return assetDirectory; }
    
    /**
     * Initiate a settings manager. 
     * @param directoryPath The directory path is used to find the asset folder. It is
     * expected that the asset folder is a sibling of the given path. If that does 
     * not exist, the asset folder should be a subdirectory of the given directory.
     * If either the directory or  the asset directory not found, that results in exception.
     */
    public SettingsManager(String directoryPath) {
        checkNotNull(directoryPath);
        setupAssetRoot(directoryPath);
    }
           
    private void setupAssetRoot(String directoryPath) {
        File dirs[] = new File[]{new File(directoryPath + "/assets"),
            new File(directoryPath + "/../assets")};
        for (File dir : dirs) { if (dir.exists()) assetDirectory = dir; }
        if (assetDirectory == null) throw new IllegalStateException("Cannot find asset directory"); 
    }
}
