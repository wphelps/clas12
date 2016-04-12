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

public class OptionsPanel extends JPanel  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel axisOptions = new JPanel();
	ArrayList<JPanel> dataSetPanels = new ArrayList<JPanel>();
	ArrayList<String> dataSetNames = new ArrayList<String>();
	double xMin, xMax, yMin, yMax;
	int ySliderMin = 0;
	int ySliderMax = 10000000;
	int xSliderMin = 0;
	int xSliderMax = 10000000;
	String xAxisLabel, yAxisLabel, title;
	List<String>   systemFonts = TStyle.systemFonts();
	int axisFontSize,titleFontSize,axisTitleFontSize,currentFont;
	EmbeddedCanvas canvas;
	int index;
	JTabbedPane tabbedPane;
	public OptionsPanel(EmbeddedCanvas canvas, int indx){
		this.canvas = canvas;
		this.index = indx;
		xMin = canvas.getPad(index).getAxisX().getMin();
		xMax = canvas.getPad(index).getAxisX().getMax();
		yMin = canvas.getPad(index).getAxisY().getMin();
		yMax = canvas.getPad(index).getAxisY().getMax();
		//canvas.getPad(index).getAxisY().getAxisFont().getSize()
		//canvas.getPad(5).setAxisRange("X", 0.0, 5.0);
		int ndataset = canvas.getPad(indx).getDataSetCount();
		for(int i = 0; i < ndataset; i++){
			IDataSet ds = canvas.getPad(indx).getDataSet(i);
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
		JPanel optionsPanel = new JPanel(new GridLayout(8,1));
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

		optionsPanel.add(fontLabel);
		optionsPanel.add(fontsBox);
		optionsPanel.add(titleFontSizeLabel);
		optionsPanel.add(titleFontSizeBox);
		optionsPanel.add(axisTitleFontSizeLabel);
		optionsPanel.add(axisTitleFontSizeBox);
		optionsPanel.add(axisFontSizeLabel);
		optionsPanel.add(axisFontSizeBox);
		optionsPanel.add(titleLabel);
		optionsPanel.add(titleTextField);
		optionsPanel.add(xAxisTitleLabel);
		optionsPanel.add(xAxisTextField);
		optionsPanel.add(yAxisTitleLabel);
		optionsPanel.add(yAxisTextField);
		optionsPanel.add(gridPanel);
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
		RangeSlider xSlider = new RangeSlider();
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
				canvas.getPad(index).setAxisRange("X",slider.getValue() * (xMax-xMin)/(double)(xSliderMax-xSliderMin) , slider.getUpperValue()* (xMax-xMin)/(double)(xSliderMax-xSliderMin));
				//canvas.repaint();
				canvas.update();
			}
		});
		
		//y axis
		JLabel yAxisLabel = new JLabel("Y:");
		JLabel yrangeSliderValue1 = new JLabel(""+String.format("%4.2f",yMin));
		JLabel yrangeSliderValue2 = new JLabel(""+String.format("%4.2f",yMax));
		RangeSlider ySlider = new RangeSlider();
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
				canvas.getPad(index).setAxisRange("Y",slider.getValue() * (yMax-yMin)/(double)(ySliderMax-ySliderMin) , slider.getUpperValue()* (yMax-yMin)/(double)(ySliderMax-ySliderMin));
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
