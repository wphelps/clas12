package org.root.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.root.attr.TStyle;
import org.root.base.IDataSet;
import org.root.basic.EmbeddedCanvas;
import org.root.basic.EmbeddedPad;

public class OptionsPanel extends JPanel  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel axisOptions = new JPanel();
	ArrayList<JPanel> dataSetPanels = new ArrayList<JPanel>();
	ArrayList<String> dataSetNames = new ArrayList<String>();
	JCheckBox[] applyToAllCheckBoxes;
	double xMin, xMax, yMin, yMax;
	int ySliderMin = 0;
	int ySliderMax = 10000000;
	int xSliderMin = 0;
	int xSliderMax = 10000000;
	RangeSlider xSlider, ySlider;
	String xAxisLabel, yAxisLabel, title;
	List<String>   systemFonts = TStyle.systemFonts();
	int axisFontSize,titleFontSize,axisTitleFontSize,currentFont;
	EmbeddedCanvas canvas;
	int index;
	JTabbedPane tabbedPane;
	public OptionsPanel(EmbeddedCanvas canvas, int indx){
		this.canvas = canvas;
		this.index = indx;
		//System.out.println("Inializing Options Panel index:["+index+"]");
		xMin = canvas.getPad(index).getAxisX().getMin();
		xMax = canvas.getPad(index).getAxisX().getMax();
		yMin = canvas.getPad(index).getAxisY().getMin();
		yMax = canvas.getPad(index).getAxisY().getMax();
		//canvas.getPad(index).getAxisY().getAxisFont().getSize()
		//canvas.getPad(5).setAxisRange("X", 0.0, 5.0);
		int ndataset = canvas.getPad(index).getDataSetCount();
		for(int i = 0; i < ndataset; i++){
			IDataSet ds = canvas.getPad(index).getDataSet(i);
			String name = ds.getName();
			dataSetPanels.add(new JPanel());
			dataSetNames.add(name);
		}
		axisFontSize = canvas.getPad(index).getAxisX().getAxisFontSize();
		axisTitleFontSize = canvas.getPad(index).getAxisX().getAxisTitleFontSize();
		//currentFont = systemFonts.indexOf(canvas.getPad(index).getAxisX().getAxisFont());
		//There's probably a better way
		for(int i=0; i<systemFonts.size(); i++){
			if(canvas.getPad(index).getAxisX().getAxisFont().getName().compareTo(systemFonts.get(i)) ==0){
				currentFont = i;
			}
		}
		
		initDatasetOptions();
		initAxisOptions();
		initTabbedPanes();
	}

	private void initTabbedPanes() {
		tabbedPane = new JTabbedPane();
		tabbedPane.add("Axes", axisOptions);
		for(int i=0; i<dataSetPanels.size(); i++){
			tabbedPane.add(dataSetNames.get(i),dataSetPanels.get(i));
		}
		this.add(tabbedPane);
	}
	private int returnIndex(int[] array, int number){
		for(int i=0; i<array.length; i++){
			if(array[i] == number){
				return i;
			}
		}
		return 0;
	}
	
	private void initAxisOptions(){
		axisOptions.setLayout(new BorderLayout());
		JPanel optionsPanel = new JPanel(new GridLayout(9,1));
		optionsPanel.setBorder(new TitledBorder("Options"));
		String[] lineThickness 	= {"1","2","3","4","5","6","7","8"};
		String[] fillColor 		= {"34","35","36","37","38"};
		String[] colors 		= {"1","2","3","4","5","6","7","8","9"};
		String[] colorNames 	= {"Black","White","Red","Green","Blue","Yellow","Magenta","Cyan","Dark Green","Purple-ish"};
		String[] fontSize 		= {"6","8","10","12","14","16","18","24","36","48"};
		int[] fontSizeInts 		= {6,8,10,12,14,16,18,24,36,48};
		String[] translucency = {"100%","80%","60%","40%","20%"};
		
		//JComboBox lineWidthBox = new JComboBox(lineThickness);
		JComboBox fontsBox	= new JComboBox(systemFonts.toArray());
		fontsBox.setSelectedIndex(currentFont);
		JComboBox axisFontSizeBox	= new JComboBox(fontSize);
		axisFontSizeBox.setSelectedIndex(returnIndex(fontSizeInts,axisFontSize));
		JComboBox axisTitleFontSizeBox = new JComboBox(fontSize);
		axisTitleFontSizeBox.setSelectedIndex(returnIndex(fontSizeInts,axisTitleFontSize));
		JComboBox titleFontSizeBox 	= new JComboBox(fontSize);
		titleFontSizeBox.setSelectedIndex(returnIndex(fontSizeInts,canvas.getPad(index).getAxisFrame().getTitleFontSize()));

		fontsBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				canvas.getPad(index).getAxisX().setAxisFontName(systemFonts.get(fontsBox.getSelectedIndex()));
				canvas.getPad(index).getAxisY().setAxisFontName(systemFonts.get(fontsBox.getSelectedIndex()));
				canvas.getPad(index).getAxisX().setTitleFontName(systemFonts.get(fontsBox.getSelectedIndex()));
				canvas.getPad(index).getAxisY().setTitleFontName(systemFonts.get(fontsBox.getSelectedIndex()));
				canvas.getPad(index).getAxisFrame().setTitleFontName(systemFonts.get(fontsBox.getSelectedIndex()));
				canvas.update();
			}
		});
		
		axisFontSizeBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				canvas.getPad(index).getAxisX().setAxisFontSize(fontSizeInts[axisFontSizeBox.getSelectedIndex()]);
				canvas.getPad(index).getAxisY().setAxisFontSize(fontSizeInts[axisFontSizeBox.getSelectedIndex()]);
				canvas.update();
			}
		});
		
		
		axisTitleFontSizeBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				canvas.getPad(index).getAxisX().setTitleFontSize(fontSizeInts[axisTitleFontSizeBox.getSelectedIndex()]);
				canvas.getPad(index).getAxisY().setTitleFontSize(fontSizeInts[axisTitleFontSizeBox.getSelectedIndex()]);
				canvas.update();
			}
		});
		
		titleFontSizeBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				canvas.getPad(index).getAxisFrame().setTitleFontSize(fontSizeInts[titleFontSizeBox.getSelectedIndex()]);
				canvas.update();
			}
		});
		//JComboBox fillColorBox = new JComboBox(fillColor);
		
		//JLabel lineWidthLabel = new JLabel("Line Width");
		JLabel fontLabel 	= new JLabel("Font:");
		JLabel axisFontSizeLabel 	= new JLabel("Axis Label Font Size:");
		JLabel axisTitleFontSizeLabel 	= new JLabel("Axis Title Font Size:");
		JLabel titleFontSizeLabel 	= new JLabel("Title Font Size:");

		
		JLabel xAxisTitleLabel = new JLabel("X Axis Title:");
		JLabel yAxisTitleLabel = new JLabel("Y Axis Title:");
		JLabel titleLabel = new JLabel("Title:");

		JTextField xAxisTextField 	= new JTextField("");
		JTextField yAxisTextField 	= new JTextField("");
		JTextField titleTextField 	= new JTextField("");
		/*xAxisTextField.addKeyListener(new KeyListener(){
			 public void keyTyped(KeyEvent e) {
				 canvas.getPad(index).getAxisX().setTitle(xAxisTextField.getText());
					canvas.update();			   
				}
		});*/
		
		xAxisTextField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				canvas.getPad(index).getAxisX().setTitle(xAxisTextField.getText());
				canvas.update();
			}
		});
		
		yAxisTextField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				canvas.getPad(index).getAxisY().setTitle(yAxisTextField.getText());
				canvas.update();
			}
		});
		
		titleTextField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				canvas.getPad(index).getAxisFrame().setTitle(titleTextField.getText());
				canvas.update();
			}
		});
		
		JPanel gridPanel = new JPanel(new GridLayout(1,2));
		JCheckBox xGridBox = new JCheckBox("Grid X");
		xGridBox.setSelected(true);
		xGridBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				canvas.getPad(index).getAxisFrame().setGridX(xGridBox.isSelected());
				canvas.update();
			}
		});
		JCheckBox yGridBox = new JCheckBox("Grid Y");
		yGridBox.setSelected(true);
		yGridBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				canvas.getPad(index).getAxisFrame().setGridY(yGridBox.isSelected());
				canvas.update();
			}
		});
		
		gridPanel.add(xGridBox);
		gridPanel.add(yGridBox);
	
		JPanel applyToAllPanel = new JPanel(new BorderLayout());
		String[] applyToAllOptions = {"Font","Title Font Size","Axis Title Font Size","Axis Label Font Size","Title","X Axis Title","Y Axis Title","Grid X","Grid Y","Range X","Range Y"};
		boolean[] applyToAllDefaults = {true,true,true,true,false,false,false,true,true,false,false};
		applyToAllCheckBoxes = new JCheckBox[applyToAllOptions.length];
		for(int i=0; i<applyToAllOptions.length; i++){
			applyToAllCheckBoxes[i] = new JCheckBox(applyToAllOptions[i]);
			applyToAllCheckBoxes[i].setSelected(applyToAllDefaults[i]);
		}
		JComboCheckBox applyToAllComboCheckBox = new JComboCheckBox(applyToAllCheckBoxes);
		JButton applyToAllButton = new JButton("Apply To All");
		applyToAllButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ArrayList<EmbeddedPad> canvasPads = canvas.canvasPads;
				for(int i=0; i<canvasPads.size();i++){
					canvas.cd(i);
					if(applyToAllCheckBoxes[0].isSelected()){
						canvas.getPad(i).getAxisX().setAxisFontName(systemFonts.get(fontsBox.getSelectedIndex()));
						canvas.getPad(i).getAxisY().setAxisFontName(systemFonts.get(fontsBox.getSelectedIndex()));
						canvas.getPad(i).getAxisX().setTitleFontName(systemFonts.get(fontsBox.getSelectedIndex()));
						canvas.getPad(i).getAxisY().setTitleFontName(systemFonts.get(fontsBox.getSelectedIndex()));
						canvas.getPad(i).getAxisFrame().setTitleFontName(systemFonts.get(fontsBox.getSelectedIndex()));
					} 
					if(applyToAllCheckBoxes[1].isSelected()){
						canvas.getPad(i).getAxisFrame().setTitleFontSize(fontSizeInts[titleFontSizeBox.getSelectedIndex()]);
					}
					if(applyToAllCheckBoxes[2].isSelected()){
						canvas.getPad(i).getAxisX().setAxisFontSize(fontSizeInts[axisFontSizeBox.getSelectedIndex()]);
						canvas.getPad(i).getAxisY().setAxisFontSize(fontSizeInts[axisFontSizeBox.getSelectedIndex()]);
					} 
					if(applyToAllCheckBoxes[3].isSelected()){
						canvas.getPad(i).getAxisX().setTitleFontSize(fontSizeInts[axisTitleFontSizeBox.getSelectedIndex()]);
						canvas.getPad(i).getAxisY().setTitleFontSize(fontSizeInts[axisTitleFontSizeBox.getSelectedIndex()]);
					}
					if(applyToAllCheckBoxes[4].isSelected()){
						canvas.getPad(i).getAxisFrame().setTitle(titleTextField.getText());
					}
					if(applyToAllCheckBoxes[5].isSelected()){
						canvas.getPad(i).getAxisX().setTitle(xAxisTextField.getText());

					}
					if(applyToAllCheckBoxes[6].isSelected()){
						canvas.getPad(i).getAxisY().setTitle(yAxisTextField.getText());
					}
					if(applyToAllCheckBoxes[7].isSelected()){
						canvas.getPad(i).getAxisFrame().setGridX(xGridBox.isSelected());

					}
					if(applyToAllCheckBoxes[8].isSelected()){
						canvas.getPad(i).getAxisFrame().setGridY(yGridBox.isSelected());
					}
					if(applyToAllCheckBoxes[9].isSelected()){
						canvas.getPad(i).setAxisRange("X",xSlider.getValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin) +xMin, xSlider.getUpperValue()* (xMax-xMin)/(double)(xSliderMax-xSliderMin)+xMin);
					}
					if(applyToAllCheckBoxes[10].isSelected()){
						canvas.getPad(i).setAxisRange("Y",ySlider.getValue() * (yMax-yMin)/(double)(ySliderMax-ySliderMin) +yMin, ySlider.getUpperValue()* (yMax-yMin)/(double)(ySliderMax-ySliderMin)+yMin);
					}
				}
				canvas.cd(index);
				canvas.update();
			}
		});
		applyToAllPanel.add(applyToAllComboCheckBox,BorderLayout.WEST);
		applyToAllPanel.add(applyToAllButton,BorderLayout.EAST);
		
		JPanel fontsPanel = new JPanel(new BorderLayout());
		fontsPanel.add(fontLabel,BorderLayout.WEST);
		fontsPanel.add(fontsBox,BorderLayout.EAST);
		optionsPanel.add(fontsPanel);
		JPanel titleSizePanel = new JPanel(new BorderLayout());
		titleSizePanel.add(titleFontSizeLabel,BorderLayout.WEST);
		titleSizePanel.add(titleFontSizeBox,BorderLayout.EAST);
		optionsPanel.add(titleSizePanel);
		JPanel axisTitleSizePanel = new JPanel(new BorderLayout());
		axisTitleSizePanel.add(axisTitleFontSizeLabel,BorderLayout.WEST);
		axisTitleSizePanel.add(axisTitleFontSizeBox,BorderLayout.EAST);
		optionsPanel.add(axisTitleSizePanel);
		JPanel axisLabelPanel = new JPanel(new BorderLayout());
		axisLabelPanel.add(axisFontSizeLabel,BorderLayout.WEST);
		axisLabelPanel.add(axisFontSizeBox,BorderLayout.EAST);
		optionsPanel.add(axisLabelPanel);
		JPanel titleTextFieldPanel = new JPanel(new GridLayout(1,2));
		titleTextFieldPanel.add(titleLabel);
		titleTextFieldPanel.add(titleTextField);
		optionsPanel.add(titleTextFieldPanel);
		JPanel xAxisTextFieldPanel = new JPanel(new GridLayout(1,2));
		xAxisTextFieldPanel.add(xAxisTitleLabel);
		xAxisTextFieldPanel.add(xAxisTextField);
		optionsPanel.add(xAxisTextFieldPanel);
		JPanel yAxisTextFieldPanel = new JPanel(new GridLayout(1,2));
		yAxisTextFieldPanel.add(yAxisTitleLabel);
		yAxisTextFieldPanel.add(yAxisTextField);
		optionsPanel.add(yAxisTextFieldPanel);

		optionsPanel.add(gridPanel,BorderLayout.EAST);
		optionsPanel.add(applyToAllPanel,BorderLayout.EAST);

		//optionsPanel.add(fillColorLabel);
		//optionsPanel.add(fillColorBox);

		axisOptions.add(optionsPanel, BorderLayout.PAGE_START);

		JPanel rangePanel = new JPanel(new GridLayout(2,1));
		JPanel xRangePanel =new JPanel(new FlowLayout());
		JPanel yRangePanel =new JPanel(new FlowLayout());

		rangePanel.setBorder(new TitledBorder("Range"));
		JLabel xrangeSliderValue1 = new JLabel(""+String.format("%4.2f",xMin));
		JLabel xrangeSliderValue2 = new JLabel(""+String.format("%4.2f",xMax));
		JLabel xAxisLabel = new JLabel("X:");
		
		//x axis
		xSlider = new RangeSlider();
		xSlider.setMinimum(xSliderMin);
		xSlider.setMaximum(xSliderMax);
		xSlider.setValue(xSliderMin);
		xSlider.setUpperValue(xSliderMax);
		
		xRangePanel.add(xAxisLabel);
		xRangePanel.add(xrangeSliderValue1);
		xRangePanel.add(xSlider);
		xRangePanel.add(xrangeSliderValue2);
		
		xSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				RangeSlider slider = (RangeSlider) e.getSource();
				xrangeSliderValue1.setText(String.valueOf(""+String.format("%4.2f",slider.getValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin))));
				xrangeSliderValue2.setText(String.valueOf(""+String.format("%4.2f",slider.getUpperValue() *(xMax-xMin)/(double)(xSliderMax-xSliderMin))));
				canvas.getPad(index).setAxisRange("X",slider.getValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin)+xMin , slider.getUpperValue()* (xMax-xMin)/(double)(xSliderMax-xSliderMin)+xMin);
				//canvas.repaint();
				canvas.update();
			}
		});
		
		//y axis
		JLabel yAxisLabel = new JLabel("Y:");
		JLabel yrangeSliderValue1 = new JLabel(""+String.format("%4.2f",yMin));
		JLabel yrangeSliderValue2 = new JLabel(""+String.format("%4.2f",yMax));
		ySlider = new RangeSlider();
		ySlider.setMinimum(ySliderMin);
		ySlider.setMaximum(ySliderMax);
		ySlider.setValue(ySliderMin);
		ySlider.setUpperValue(ySliderMax);
		
		yRangePanel.add(yAxisLabel);
		yRangePanel.add(yrangeSliderValue1);
		yRangePanel.add(ySlider);
		yRangePanel.add(yrangeSliderValue2);
		
		ySlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				RangeSlider slider = (RangeSlider) e.getSource();
				yrangeSliderValue1.setText(String.valueOf(""+String.format("%4.2f",slider.getValue() * (yMax-yMin)/(double)(ySliderMax-ySliderMin))));
				yrangeSliderValue2.setText(String.valueOf(""+String.format("%4.2f",slider.getUpperValue() *(yMax-yMin)/(double)(ySliderMax-ySliderMin))));
				canvas.getPad(index).setAxisRange("Y",slider.getValue() * (yMax-yMin)/(double)(ySliderMax-ySliderMin)+yMin , slider.getUpperValue()* (yMax-yMin)/(double)(ySliderMax-ySliderMin)+yMin);
				//canvas.repaint();
				canvas.update();
			}
		});
		
		rangePanel.add(xRangePanel);
		rangePanel.add(yRangePanel);
		axisOptions.add(rangePanel, BorderLayout.PAGE_END);
	}
	
	private void initDatasetOptions(){
		for(int i=0; i<dataSetPanels.size();i++){
			dataSetPanels.get(i).setLayout(new GridLayout(3,1));
			dataSetPanels.get(i).setBorder(new TitledBorder("Data Options"));
			String[] lineThickness = {"1","2","3","4","5","6","7","8","9"};
			int[] lineThicknessInts = {1,2,3,4,5,6,7,8,9};
			String[] lineStyle = {"1","2","3","4","5"};
			int[] lineStyleInts = {1,2,3,4,5};
			String[] fillColor = {"34","35","36","37","38"};
			String[] fontSize = {"12","14","16","18"};

			
			JComboBox lineWidthBox = new JComboBox(lineThickness);
			JComboBox fillColorBox = new JComboBox(fillColor);
			JComboBox lineStyleBox = new JComboBox(lineStyle);
			
			JLabel lineWidthLabel = new JLabel("Line Width:");
			JLabel  fillColorLabel = new JLabel("Fill Color:");
			JLabel lineStyleLabel =  new JLabel("Line Style:");
			
			dataSetPanels.get(i).add(lineWidthLabel);
			dataSetPanels.get(i).add(lineWidthBox);
			dataSetPanels.get(i).add(fillColorLabel);
			dataSetPanels.get(i).add(fillColorBox);
			dataSetPanels.get(i).add(lineStyleLabel);
			dataSetPanels.get(i).add(lineStyleBox);
		}
		
	}
	
	private JPanel makeDataSetPanel(){
		return null;
	}
}
