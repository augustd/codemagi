/* 
 *  Copyright 2012 CodeMagi, Inc.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.codemagi.util;

import com.sun.media.jai.codec.FileSeekableStream;
import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.ParameterBlock;
import java.io.IOException;
import javax.media.jai.*;

/**
 * This class implements a number of static methods for image processing. It is
 * not meant to be instantiated.
 */
public class ImageUtils {

    private ImageUtils() {
    }

    /**
     * Load an image from the filesystem
     */
    public static RenderedOp loadImage(String file) throws IOException {
        String extension = file.substring(file.lastIndexOf("."));

        RenderedOp source = null;
        FileSeekableStream ss = new FileSeekableStream(file);
        ParameterBlock loadParams = new ParameterBlock();
        loadParams.add(ss);

        if (".jpeg".equalsIgnoreCase(extension) || ".jpg".equalsIgnoreCase(extension)) {
            source = JAI.create("jpeg", loadParams);

        } else if (".tiff".equalsIgnoreCase(extension) || ".tif".equalsIgnoreCase(extension)) {
            source = JAI.create("tiff", loadParams);

        } else if (".png".equalsIgnoreCase(extension)) {
            source = JAI.create("png", loadParams);

        } else if (".gif".equalsIgnoreCase(extension)) {
            source = JAI.create("gif", loadParams);

        } else {
            source = JAI.create("fileload", file);

        }

        return source;
    }

    /**
     * Get the width and height of an image
     */
    public static Point getDimensions(RenderedOp source) {
        return new Point(source.getWidth(), source.getHeight());
    }

    /**
     * Returns a human readable string for the color model of an image
     */
    public static String getColorModel(RenderedOp source, String extension) {
        ColorModel cm = source.getColorModel();
        if (".png".equalsIgnoreCase(extension) && cm.getTransparency() != Transparency.OPAQUE) {
            return "RGB, with alpha channel";

        } else if (cm.getNumComponents() == 4) {
            return "CMYK";

        }

        return "RGB";
    }

}
