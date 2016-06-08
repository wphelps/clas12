package org.root.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	boolean modified = false;
	ArrayList<JPanel> parameterPanels = new ArrayList<JPanel>();
	ArrayList<JCheckBox> parameterLimited = new ArrayList<JCheckBox>();
	ArrayList<JCheckBox> parameterFixed = new ArrayList<JCheckBox>();
	ArrayList<JTextField> parameterMin = new ArrayList<JTextField>();
	ArrayList<JTextField> parameterMax = new ArrayList<JTextField>();
	ArrayList<JLabel> parameterName = new ArrayList<JLabel>();
	ArrayList<JLabel> parameterValueLabel = new ArrayList<JLabel>();
	ArrayList<JTextField> parameterValue = new ArrayList<JTextField>();
	ArrayList<JSlider> parameterValueSliders = new ArrayList<JSlider>();
	ArrayList<Double> lowerLim = new ArrayList<Double>();
	ArrayList<Double> upperLim = new ArrayList<Double>();

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
		if(upperLim.size()!=this.fitFunction.getNParams()){
			upperLim.clear();
		}
		if(lowerLim.size()!=this.fitFunction.getNParams()){
			lowerLim.clear();
		}
		for(int i=0; i<fitFunction.getNParams(); i++){
			parameterPanels.add(new JPanel());
			parameterLimited.add(new JCheckBox("Lim."));
			parameterLimited.get(i).setSelected(fitFunction.parameter(i).isLimited());
			parameterFixed.add(new JCheckBox("Fix"));
			if(lowerLim.size()!=this.fitFunction.getNParams()){
				lowerLim.add(new Double(fitFunction.parameter(i).value()/10.0));
			}
			if(upperLim.size()!=this.fitFunction.getNParams()){
				upperLim.add(new Double(fitFunction.parameter(i).value()*10.0));
			}
			
			parameterValue.add(new JTextField(String.format("%4.2f", fitFunction.parameter(i).value())));
			if(parameterLimited.get(i).isSelected()){
				parameterMin.add(new JTextField(String.format("%4.2f", fitFunction.parameter(i).min())));
				parameterMax.add(new JTextField(String.format("%4.2f", fitFunction.parameter(i).max())));
			}else{
				parameterMin.add(new JTextField(String.format("%4.2f", lowerLim.get(i))));
				parameterMax.add(new JTextField(String.format("%4.2f", upperLim.get(i))));
			}
			parameterMin.get(i).addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JTextField textField = (JTextField) e.getSource();
					for(int i=0; i<parameterMin.size(); i++){
						if(textField.equals(parameterMin.get(i))){
							lowerLim.remove(i);
							lowerLim.add(i,new Double(Double.parseDouble(parameterMin.get(i).getText())));
							double xLow = Double.parseDouble(parameterMin.get(i).getText());
							double xHigh = Double.parseDouble(parameterMax.get(i).getText());
							double sliderMax = parameterValueSliders.get(i).getMaximum();
							double sliderMin = parameterValueSliders.get(i).getMinimum();
							parameterValueSliders.get(i).setValue((int)((fitFunction.parameter(i).value()-xLow)/((xHigh-xLow)/(double)(sliderMax-sliderMin))));
						}
					}
				}
			});
			parameterMax.get(i).addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JTextField textField = (JTextField) e.getSource();
					for(int i=0; i<parameterMax.size(); i++){
						if(textField.equals(parameterMax.get(i))){
							upperLim.remove(i);
							upperLim.add(i,new Double(Double.parseDouble(parameterMax.get(i).getText())));
							double xLow = Double.parseDouble(parameterMin.get(i).getText());
							double xHigh = Double.parseDouble(parameterMax.get(i).getText());
							double sliderMax = parameterValueSliders.get(i).getMaximum();
							double sliderMin = parameterValueSliders.get(i).getMinimum();
							parameterValueSliders.get(i).setValue((int)((fitFunction.parameter(i).value()-xLow)/((xHigh-xLow)/(double)(sliderMax-sliderMin))));
						}
					}
				}
			});
			parameterValueSliders.add(new JSlider());
			parameterValueSliders.get(i).setMaximum(10000);
			parameterValueSliders.get(i).setMinimum(0);
			
			double xLow = Double.parseDouble(parameterMin.get(i).getText());
			double xHigh = Double.parseDouble(parameterMax.get(i).getText());
			double sliderMax = parameterValueSliders.get(i).getMaximum();
			double sliderMin = parameterValueSliders.get(i).getMinimum();
			parameterValueSliders.get(i).setValue((int)((fitFunction.parameter(i).value()-xLow)/((xHigh-xLow)/(double)(sliderMax-sliderMin))));
			System.out.println("Initialize Slider:"+(int)((fitFunction.parameter(i).value()- xLow)/((xHigh-xLow)/(double)(sliderMax-sliderMin) )));
			parameterValueLabel.add(new JLabel("Value:"));
			parameterName.add(new JLabel());
			parameterName.get(i).setText(fitFunction.parameter(i).name());
			parameterValueSliders.get(i).addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					modified = true;
					JSlider slider = (JSlider) e.getSource();
					for(int i=0; i<parameterValueSliders.size(); i++){
						if(slider.equals(parameterValueSliders.get(i))){
							//System.out.println("Slider #:"+i+" Parameter min:"+Double.parseDouble(parameterMin.get(i).getText())+ " Max:"+Double.parseDouble(parameterMax.get(i).getText())+" Parameter Value:"+fitFunction.getParameter(i));
							double xLow = Double.parseDouble(parameterMin.get(i).getText());
							double xHigh = Double.parseDouble(parameterMax.get(i).getText());
							double sliderMax = parameterValueSliders.get(i).getMaximum();
							double sliderMin = parameterValueSliders.get(i).getMinimum();
							double parameterValueDouble = slider.getValue() * (xHigh-xLow)/(double)(sliderMax-sliderMin) + xLow;
							fitFunction.setParameter(i,parameterValueDouble);
							System.out.println("ParameterValue:"+parameterValueDouble);
							parameterValue.get(i).setText(String.format("%2.2f",parameterValueDouble));
							
							if(parameterFixed.get(i).isSelected()){
								System.out.println("Nooooooooooo, I'm fixed");
							}
							updateFitFunction();
						}
					}
					double temp = slider.getValue();
					System.out.println("Changing slider Value:"+temp);
					//System.out.println("currentRangeMin:"+currentRangeMin+" xOffset:"+xOffset);
					//fitFunction.setRange(currentRangeMin, currentRangeMax);
				}
			});
			parameterPanels.get(i).add(parameterName.get(i));
			parameterPanels.get(i).add(parameterLimited.get(i));
			parameterPanels.get(i).add(parameterFixed.get(i));
			parameterPanels.get(i).add(parameterMin.get(i));
			parameterPanels.get(i).add(parameterValueSliders.get(i));
			parameterPanels.get(i).add(parameterMax.get(i));
			parameterPanels.get(i).add(parameterValueLabel.get(i));
			parameterPanels.get(i).add(parameterValue.get(i));
			this.add(parameterPanels.get(i));
		}		
	}
	public void updateFitFunction(){
		canvas.cd(index);                                
		canvas.draw(fitFunction,"sameL");
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
	public boolean modified() {
		return modified;
	}
}
