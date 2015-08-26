/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.tools;

import org.clasfx.root.TEmbeddedCanvas;

/**
 *
 * @author gavalian
 */
public interface IDetectorDraw {
    void draw(int sector, int layer, int component, TEmbeddedCanvas canvas);
}
