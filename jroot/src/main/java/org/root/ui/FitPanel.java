package org.root.ui;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.root.base.IDataSet;
import org.root.basic.EmbeddedCanvas;
import org.root.fitter.DataFitter;
import org.root.func.F1D;
import org.root.histogram.H1D;
import org.root.pad.TCanvas;
import org.root.pad.TGCanvas;

@SuppressWarnings("unused")
public class FitPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	EmbeddedCanvas canvas;
	int index;
	JPanel fitSettings, fitFunctionPanel, lowerWindow;
	F1D fitFunction;
	H1D histogram;
	IDataSet thisDataset;
	ArrayList<IDataSet> datasets = new ArrayList<IDataSet>();
	ArrayList<String> dataSetNames = new ArrayList<String>();
	DataFitter fitter = new DataFitter();
	JComboBox paramEstimationMethods;
	ArrayList<JCheckBox> optionCheckBoxes;

	//Actual low and high of the x axis

	double xMin = 0.0;
	double xMax = 100.0;
	//This mess is due to the slider only working with integer values
	int xSliderMin = 0;
	int xSliderMax = 1000000;
	
	double currentRangeMin = 0.0;
	double currentRangeMax = 100.0;
	JComboBox predefinedFunctionsSelector;
	//Fit options
	String options = "";
	String predefFunctions[] = {"gaus", "gaus+p0", "gaus+p1", "gaus+p2", "gaus+p3"};


	FitPanel() {
		init();
	}
	
	public FitPanel(EmbeddedCanvas canvas, int indx) {
		this.canvas = canvas;
		this.index = indx;
		xMin = canvas.getPad(index).getAxisX().getMin();
		xMax = canvas.getPad(index).getAxisX().getMax();
		int ndataset = canvas.getPad(index).getDataSetCount();
		for(int i = 0; i < ndataset; i++){
			IDataSet ds = canvas.getPad(index).getDataSet(i);
			String name = ds.getName();
			dataSetNames.add(name);
			datasets.add(ds);
		}
		this.thisDataset = datasets.get(0);
		init();
	}
	
	final void init(){
		this.setLayout(new BorderLayout());
		this.fitFunction = new F1D(predefFunctions[0],xMin, xMax);
		initFitFunction();
		initFitSettings();
		initRangeSelector();
	}

	void setFunction(F1D fitFunction) {
		this.fitFunction = fitFunction;
	}


	void initFitFunction() {
		fitFunctionPanel = new JPanel(new GridLayout(2,1));
		fitFunctionPanel.setBorder(new TitledBorder("Function Settings"));

		JComboBox dataSetBox = new JComboBox(dataSetNames.toArray());
		dataSetBox.setSelectedIndex(0);
		dataSetBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				thisDataset = datasets.get(dataSetBox.getSelectedIndex());
			}
		});
		
		// Labels from F1D class
		predefinedFunctionsSelector = new JComboBox(predefFunctions);
		predefinedFunctionsSelector.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//System.out.println(predefinedFunctionsSelector.getSelectedIndex());
					//fitFunction = new F1D(predefFunctions[predefinedFunctionsSelector.getSelectedIndex()],currentRangeMin,currentRangeMax);
					//fitFunction.setFunction(predefFunctions[predefinedFunctionsSelector.getSelectedIndex()]);
					fitFunction.initFunction(predefFunctions[predefinedFunctionsSelector.getSelectedIndex()], currentRangeMin, currentRangeMax);
					/*
					if(predefinedFunctionsSelector.getSelectedIndex()==3){
						System.out.println(predefFunctions[predefinedFunctionsSelector.getSelectedIndex()]);
						fitFunction.setParameter(0, 120.0);
						fitFunction.setParameter(1, 8.2);
						fitFunction.setParameter(2, 1.2);
						fitFunction.setParameter(3, 24.0);
						fitFunction.setParameter(4, 7.0);
					}else{
						for(int i=0; i<fitFunction.getNParams(); i++){
							fitFunction.setParameter(i, 5.0);
						}
					}*/
					for(int i=0; i<fitFunction.getNParams(); i++){
						fitFunction.setParameter(i, 5.0);
					}
				}
		});
		predefinedFunctionsSelector.setSelectedIndex(0);
		JLabel labelForFunction = new JLabel("Function:");
		JLabel dataSetLabel = new JLabel("Select Dataset:");
		fitFunctionPanel.add(dataSetLabel);
		fitFunctionPanel.add(dataSetBox);
		fitFunctionPanel.add(labelForFunction);
		fitFunctionPanel.add(predefinedFunctionsSelector);
		this.add(fitFunctionPanel,BorderLayout.PAGE_START);
	}
	
	void initFitSettings() {

		JPanel fitMethod = new JPanel(new FlowLayout());
		String labs[] = {"Chi-square","Chi-square (Neyman)","Chi-square (Pearson)","Chi-square (All weights=1)", "Binned Extended-MLE"};
		paramEstimationMethods = new JComboBox(labs);
		/*
		paramEstimationMethods.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(paramEstimationMethods.getSelectedIndex()==0){
					options = "NRQ";
					System.out.print("Option:"+options);
				}else{
					options = "LRQ";
					System.out.print("Option:"+options);
				}
				
			}
		});*/
		
		
		paramEstimationMethods.setSelectedIndex(0);
		fitMethod.add(new JLabel("Method:"));
		fitMethod.add(paramEstimationMethods);

		JPanel fitOptions = new JPanel(new GridLayout(1, 1));
		String[] options = {"Draw Stats"};
		optionCheckBoxes = new ArrayList<JCheckBox>();
		for (int i = 0; i < options.length; i++) {
			fitOptions.add(new JCheckBox(options[i]));
		}
		fitSettings = new JPanel(new GridLayout(2, 1));
		fitSettings.add(fitMethod);
		fitSettings.add(fitOptions);
		fitSettings.setBorder(new TitledBorder("Minimizer Settings"));
		this.add(fitSettings, BorderLayout.CENTER);
	}


	private void initRangeSelector() {
		lowerWindow = new JPanel(new GridLayout(2, 1));
		JPanel rangeSelector = new JPanel();
		JLabel xLabel = new JLabel("X:");
		RangeSlider slider = new RangeSlider();
		slider.setMinimum((int) xSliderMin);
		slider.setMaximum((int) xSliderMax);
		slider.setValue((int) xSliderMin);
		slider.setUpperValue((int) xSliderMax);
		currentRangeMin = slider.getValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin);
		currentRangeMax = slider.getUpperValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin);
	    JLabel rangeSliderValue1 = new JLabel(""+String.format("%4.2f",currentRangeMin));
	    JLabel rangeSliderValue2 = new JLabel(""+String.format("%4.2f",currentRangeMax));
		fitFunction.setRange(currentRangeMin, currentRangeMax);

		rangeSelector.add(xLabel);
	    rangeSelector.add(rangeSliderValue1);
		rangeSelector.add(slider);
	    rangeSelector.add(rangeSliderValue2);
		lowerWindow.add(rangeSelector);
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				RangeSlider slider = (RangeSlider) e.getSource();
				currentRangeMin = slider.getValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin);
				currentRangeMax = slider.getUpperValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin);
				rangeSliderValue1.setText(String.valueOf(""+String.format("%4.2f",currentRangeMin)));
				rangeSliderValue2.setText(String.valueOf(""+String.format("%4.2f",currentRangeMax)));
				//fitFunction.setRange(currentRangeMin, currentRangeMax);
			}
		});

		JButton fit = new JButton("Fit");
		fit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				//Construct options
				int method = paramEstimationMethods.getSelectedIndex();
				if(method==0){
					options = "EQR";
				}else if(method==1){
					options = "NQR";
				}else if(method==2){
					options = "PQR";
				}else if(method==3){
					options ="QR";
				}else if(method==4){
					options ="LQR";
				}
				for(int i=0; i<optionCheckBoxes.size(); i++){
					if(optionCheckBoxes.get(i).getName().compareTo("Draw Stats")==0&&optionCheckBoxes.get(i).isSelected()){
						options = options+"S";
						System.out.println("Draw stats!");
					}
					System.out.println("Options: "+optionCheckBoxes.get(i).getName()+ " is "+optionCheckBoxes.get(i).isSelected());

				}
				fitFunction.setRange(currentRangeMin, currentRangeMax);
				//histogram.fit(fitFunction,options);
				fitter.fit(thisDataset, fitFunction,options);
				//fitFunction.show(); // print on the screen fit results
				fitFunction.setLineColor(2);
				fitFunction.setLineWidth(5);
				fitFunction.setLineStyle(1);
				canvas.cd(index);                                
				canvas.draw(fitFunction,"same");
			}
		});
		lowerWindow.add(fit);
		this.add(lowerWindow,BorderLayout.PAGE_END);
	}
}
