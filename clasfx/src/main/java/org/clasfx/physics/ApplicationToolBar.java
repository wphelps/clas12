/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.clasfx.physics;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 *
 * @author gavalian
 */
public class ApplicationToolBar extends HBox {
    List<Button>  actionButtons = new ArrayList<Button>();
    public ApplicationToolBar(){
        super();
    }
}
