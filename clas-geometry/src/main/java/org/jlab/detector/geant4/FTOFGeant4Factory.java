/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.detector.geant4;

import java.util.ArrayList;
import java.util.List;
import org.jlab.geom.base.ConstantProvider;
import org.jlab.geom.geant.Geant4Basic;

/**
 *
 * @author gavalian
 */
public class FTOFGeant4Factory {

    private List<Geant4Basic> volumes = new ArrayList<Geant4Basic>();
    private String[] stringLayers = new String[]{
        "/geometry/ftof/panel1a",
        "/geometry/ftof/panel1b",
        "/geometry/ftof/panel2"};

    public FTOFGeant4Factory(ConstantProvider provider) {

    }

    public Geant4Basic createSector(ConstantProvider cp, int sector, int layer) {
        //Geant4Basic  mother = new Geant4Basic();

        double motherGap = 4.0;

        double thtilt = Math.toRadians(cp.getDouble(stringLayers[layer - 1] + "/panel/thtilt", 0));
        double thmin = Math.toRadians(cp.getDouble(stringLayers[layer - 1] + "/panel/thmin", 0));
        double dist2edge = cp.getDouble(stringLayers[layer - 1] + "/panel/dist2edge", 0);

        List<Geant4Basic> paddles = this.createLayer(cp, layer);

        double panel_width = (paddles.get(paddles.size() - 1).getPosition()[2] - paddles.get(0).getPosition()[2])
                + 2 * paddles.get(0).getParameters()[2] + motherGap;

        double panel_mother_dx1 = paddles.get(0).getParameters()[0] + motherGap;
        double panel_mother_dx2 = paddles.get(paddles.size() - 1).getParameters()[0]
                + (paddles.get(paddles.size() - 1).getParameters()[0] - paddles.get(paddles.size() - 2).getParameters()[0]) / 2.0
                + motherGap;

        double panel_mother_dy = paddles.get(0).getParameters()[1] + motherGap;
        double panel_mother_dz = panel_width / 2.0;

        double[] params = new double[5];
        params[0] = panel_mother_dx1;
        params[1] = panel_mother_dx2;
        params[2] = panel_mother_dy;
        params[3] = panel_mother_dy;
        params[4] = panel_mother_dz;

        Geant4Basic panelVolume = new Geant4Basic("FTOF_S" + sector + "_L_" + layer, "trd", params);

        double panel_pos_xy = dist2edge * Math.sin(thmin) + panel_width / 2 * Math.cos(thtilt);
        double panel_pos_x = panel_pos_xy * Math.cos(Math.toRadians(sector * 60 - 60));
        double panel_pos_y = panel_pos_xy * Math.sin(Math.toRadians(sector * 60 - 60));
        double panel_pos_z = dist2edge * Math.cos(thmin) - panel_width / 2 * Math.sin(thtilt);

        panelVolume.setPosition(panel_pos_x, panel_pos_y, panel_pos_z);

        //panelVolume.setRotation("xzy", thtilt/3, Math.toRadians(-30.0 - 1 * 60.0), 0.0);
        panelVolume.setRotation("xzy", Math.toRadians(-90) - thtilt, Math.toRadians(-30.0 - sector * 60.0), 0.0);
        for (int ipaddle = 0; ipaddle < paddles.size(); ipaddle++) {
            paddles.get(ipaddle).setName("ftof_S_" + sector + "_L_" + layer + "_P_" + (ipaddle + 1));
            panelVolume.getChildren().add(paddles.get(ipaddle));
        }
        return panelVolume;
    }

    public List<Geant4Basic> createLayer(ConstantProvider cp, int layer) {

        int numPaddles = cp.length(stringLayers[layer - 1] + "/paddles/paddle");
        double paddlewidth = cp.getDouble(stringLayers[layer - 1] + "/panel/paddlewidth", 0);
        double paddlethickness = cp.getDouble(stringLayers[layer - 1] + "/panel/paddlethickness", 0);
        double gap = cp.getDouble(stringLayers[layer - 1] + "/panel/gap", 0);
        double wrapperthickness = cp.getDouble(stringLayers[layer - 1] + "/panel/wrapperthickness", 0);
        double thtilt = Math.toRadians(cp.getDouble(stringLayers[layer - 1] + "/panel/thtilt", 0));
        double thmin = Math.toRadians(cp.getDouble(stringLayers[layer - 1] + "/panel/thmin", 0));

        String paddleLengthStr = stringLayers[layer - 1] + "/paddles/Length";

        //List<Geant4Basic>  mother = new ArrayList<Geant4Basic>();
        List<Geant4Basic> lv = new ArrayList<Geant4Basic>();

        for (int ipaddle = 0; ipaddle < numPaddles; ipaddle++) {
            double paddlelength = cp.getDouble(paddleLengthStr, ipaddle);
            String vname = String.format("sci_S%d_L%d_C%d", 1, 1, ipaddle + 1);
            Geant4Basic volume = new Geant4Basic(vname, "box",
                    paddlelength / 2., paddlethickness / 2., paddlewidth / 2.0);
            volume.setId(1, layer, ipaddle + 1);

            double zoffset = (ipaddle - numPaddles / 2.) * (paddlewidth + gap + 2 * wrapperthickness);
            volume.setPosition(0.0, 0.0, zoffset);
            lv.add(volume);
        }
        return lv;
    }

}
