package org.root.ui;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.root.basic.EmbeddedCanvas;
import org.root.func.F1D;


public class ParameterPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3175511650738525339L;
	int index;
	EmbeddedCanvas canvas;
	F1D fitFunction;
	ArrayList<JPanel> parameterPanels = new ArrayList<JPanel>();
	ArrayList<JCheckBox> parameterLimited = new ArrayList<JCheckBox>();
	ArrayList<JCheckBox> parameterFixed = new ArrayList<JCheckBox>();
	ArrayList<JTextField> parameterMin = new ArrayList<JTextField>();
	ArrayList<JTextField> parameterMax = new ArrayList<JTextField>();
	ArrayList<JTextField> parameterName = new ArrayList<JTextField>();
	ArrayList<JTextField> parameterValue = new ArrayList<JTextField>();
	ArrayList<JSlider> parameterValueSliders = new ArrayList<JSlider>();

	public ParameterPanel(EmbeddedCanvas canvas, int index, F1D func){
		this.index=index;
		this.fitFunction = func;
		this.canvas = canvas;
		this.setLayout(new GridLayout(func.getNParams(),1));
		initParameterPanels();
	}
	private void initParameterPanels(){
		parameterPanels = new ArrayList<JPanel>();
		ArrayList<JCheckBox> parameterLimitedCheckboxes = new ArrayList<JCheckBox>();
		for(int i=0; i<fitFunction.getNParams(); i++){
			parameterPanels.add(new JPanel());
			parameterLimited.add(new JCheckBox("Lim."));
			parameterLimited.get(i).setSelected(fitFunction.parameter(i).isLimited());
			parameterFixed.add(new JCheckBox("Fix"));
			parameterValue.add(new JTextField(String.format("%4.2f", fitFunction.parameter(i).value())));
			if(parameterLimited.get(i).isSelected()){
				parameterMin.add(new JTextField(String.format("%4.2f", fitFunction.parameter(i).min())));
				parameterMax.add(new JTextField(String.format("%4.2f", fitFunction.parameter(i).max())));
			}else{
				parameterMin.add(new JTextField(String.format("%4.2f", fitFunction.parameter(i).value()/10.0)));
				parameterMax.add(new JTextField(String.format("%4.2f", fitFunction.parameter(i).value()*10.0)));
			}
			parameterValueSliders.add(new JSlider());
			parameterValueSliders.get(i).setMaximum(10000);
			parameterValueSliders.get(i).setMinimum(0);
			parameterValueSliders.get(i).addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JSlider slider = (JSlider) e.getSource();
					for(int i=0; i<parameterValueSliders.size(); i++){
						if(slider.equals(parameterValueSliders.get(i))){
							System.out.println("Slider #:"+i+" Parameter min:"+Double.parseDouble(parameterMin.get(i).getText())+ " Max:"+Double.parseDouble(parameterMax.get(i).getText())+" Parameter Value:"+fitFunction.getParameter(i));
							//currentRangeMax = slider.getValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin) + xMin;
							if(parameterFixed.get(i).isSelected()){
								System.out.println("Nooooooooooo, I'm fixed");
							}
						}
					}
					double temp = slider.getValue();
					System.out.println("Changing slider Value:"+temp);
					//System.out.println("currentRangeMin:"+currentRangeMin+" xOffset:"+xOffset);
					//fitFunction.setRange(currentRangeMin, currentRangeMax);
				}
			});
			parameterName.add(new JTextField(fitFunction.parameter(i).name()));
			
			parameterPanels.get(i).add(parameterLimited.get(i));
			parameterPanels.get(i).add(parameterFixed.get(i));
			parameterPanels.get(i).add(parameterMin.get(i));
			parameterPanels.get(i).add(parameterValueSliders.get(i));
			parameterPanels.get(i).add(parameterMax.get(i));
			this.add(parameterPanels.get(i));
		}		
	}
	public void updateNewFunction(F1D func){
		this.fitFunction = func;
		this.removeAll();
		this.setLayout(new GridLayout(func.getNParams(),1));
		parameterPanels.clear();
		parameterLimited.clear();
		parameterFixed.clear();
		parameterMin.clear();
		parameterMax.clear();
		parameterName.clear();
		parameterValue.clear();
		parameterValueSliders.clear();
		initParameterPanels();
		//this.setPreferredSize(this.getPreferredSize());
		this.revalidate();
		this.repaint();
		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		topFrame.pack();
	}
}
