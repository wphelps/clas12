/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.root.pad;

import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gavalian
 */
public class AxisNiceScale {
    
    
    
    private double minPoint;
    private double maxPoint;
    private int    maxTicks = 10;
    private double tickSpacing;
    private double range;
    private double niceMin;
    private double niceMax;
    private Boolean isAxisLogarithmic = false;
    
    
    private ArrayList<Double> niceCoordinates = new ArrayList<Double>();
    private ArrayList<Double> minorTicks      = new ArrayList<Double>();
    
    private ArrayList<String> niceCoordinateLabels = new ArrayList<String>();
    /**
     * Instantiates a new instance of the NiceScale class.
     *
     * @param min the minimum data point on the axis
     * @param max the maximum data point on the axis
     */
    public AxisNiceScale(double min, double max) {
        this.minPoint = min;
        this.maxPoint = max;
        calculate();
    }
    
    public void setMinMaxPointsLog(double minPoint, double maxPoint){
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        calculateLog();
    }
    
    private void calculateLog(){ 
        //System.out.println("CALCULATING AXIS IN LOG SCALE"); 
        double  minPointLog = this.minPoint;
        
        if(minPointLog==0){
            minPointLog = this.maxPoint*1.0e-9;
        }
        
        
        this.range = Math.floor(Math.log10(this.maxPoint)) - Math.floor(Math.log10(minPointLog));
        this.tickSpacing = this.range/(this.maxTicks-1);
        int minlog =  (int) Math.floor(Math.log10(minPointLog));
        int maxlog =  (int) Math.floor(Math.log10(this.maxPoint));
        
        this.tickSpacing = niceNum(range / (maxTicks - 1), true);
        //System.out.println("MINPOINT " + minPointLog + " " + maxPoint + "  spacing = " + this.tickSpacing);
        this.niceCoordinates.clear();
        this.niceCoordinateLabels.clear();
        this.range = maxlog;// - minlog;
        for(int loop = 0; loop < this.maxTicks;loop++){
            double value = Math.pow(10, loop);
            if(value<this.niceMax){
                this.niceCoordinates.add(value);
                this.niceCoordinateLabels.add(String.format("%.0f",
                        this.niceCoordinates.get(loop)));
            }
            //System.out.println(" TICK " + loop + " = " + this.niceCoordinateLabels.get(loop));
        }
        
        this.minorTicks.clear();
        for(int loop = 0; loop < this.maxTicks;loop++){
            
            //double rangeMin = this.niceCoordinates.get(loop-1);
            //double rangeMax = this.niceCoordinates.get(loop);
            //double step     = 0.1*(rangeMax-rangeMin);
            double  tickLog = Math.pow(10, loop);
            for(int x = 2; x <= 9; x++){
                double tick =  x*tickLog;
                if(tick<this.niceMax)
                    this.minorTicks.add(tick);
            }
        }
    }
    /**
     * Calculate and update values for tick spacing and nice
     * minimum and maximum data points on the axis.
     */
    private void calculate() {
        this.range = niceNum(maxPoint - minPoint, false);
        this.tickSpacing = niceNum(range / (maxTicks - 1), true);
        this.niceMin =
                Math.floor(minPoint / tickSpacing) * tickSpacing;
        this.niceMax =
                Math.ceil(maxPoint / tickSpacing) * tickSpacing;
        
        niceCoordinates.clear();
        this.niceCoordinateLabels.clear();
        
        double numberX = niceMin;
        while(numberX<=this.maxPoint){
            if(numberX>=this.minPoint&&numberX<=this.maxPoint){
                niceCoordinates.add(numberX);
            }
            numberX += this.tickSpacing;
        }
        /*
        for(int loop = 0; loop < maxTicks; loop++){
            double numberX = niceMin + loop*this.tickSpacing;
            if(numberX<=this.maxPoint&&numberX>=this.minPoint)
                niceCoordinates.add(numberX);
        }
        */
        int sigfig = this.getSigFig();
        String format = this.getStringFormat(sigfig+1);
        for(int loop = 0; loop < this.niceCoordinates.size();loop++){
            if(sigfig<0){
                this.niceCoordinateLabels.add(String.format("%.0f", 
                        this.niceCoordinates.get(loop)));
            } else {
                this.niceCoordinateLabels.add(String.format(format,
                        this.niceCoordinates.get(loop)));                
            }

        }
        
        this.minorTicks.clear();
        for(int loop = 1; loop < this.niceCoordinates.size();loop++){
            
            //double rangeMin = this.niceCoordinates.get(loop-1);
            //double rangeMax = this.niceCoordinates.get(loop);
            //double step     = 0.1*(rangeMax-rangeMin);
            double step = (this.niceCoordinates.get(loop) - this.niceCoordinates.get(loop-1))/4.0;
            for(int x = 1; x <= 3; x++){
                double tick =  this.niceCoordinates.get(loop-1) + step*x;
                this.minorTicks.add(tick);
            }
        }
        //System.out.println(" SIG FIG = " + this.getSigFig());
    }
    
    public double  getAxisCoordinate(double value, double length){
        double axisLength = this.maxPoint-this.minPoint;
        if(axisLength==0) return 0;
        double relative = value/axisLength;
        return relative;
    }
    
    public boolean getAxisLog(){
        return this.isAxisLogarithmic;
    }
    
    public void setAxisLog(boolean log){
        this.isAxisLogarithmic = log;
        if(this.isAxisLogarithmic==true){
            this.calculateLog();
        } else {
            this.calculate();
        }
    }
    
    public void updateWithFont(FontMetrics fm, int len, boolean vertical){
        double MAX_RATIO = 0.7;
        this.setMaxTicks(10);
        int currentDivision  = this.maxTicks;
        double currentRatio  = 1.0;

        while(currentRatio>MAX_RATIO){
            double labelLength = 0.0;
            for(String label : this.niceCoordinateLabels){
                if(vertical==true){
                    labelLength += fm.getHeight();
                } else {
                    labelLength += fm.stringWidth(label);
                }
            }
            currentRatio = labelLength/len;
            if(currentRatio>MAX_RATIO){
                currentDivision--;
                this.setMaxTicks(currentDivision);
            }
        }
    }
    
    public int getAxisOffset(FontMetrics fm, boolean vertical ){
        int offset = 0;
        
        if(vertical==false){
            offset  = fm.getHeight();
            return offset;
        }
        
        if(vertical==true){
            for(String label : this.niceCoordinateLabels){
                int width = fm.stringWidth(label);
                if(width>offset) offset = width;
            }
            //offset += fm.getHeight();
        }
        
        return offset;
    }
    
    public String getStringFormat(int sig){
        StringBuilder str = new StringBuilder();
        str.append('%');
        str.append('.');
        str.append(sig);
        str.append('f');
        return str.toString();
    }
    
    public ArrayList<String>  getCoordinatesLabels(){
        return this.niceCoordinateLabels;
    }
    /**
     * Returns the minor ticks. Calculated differently for linear axis,
     * and logarithmic axis.
     * @return 
     */    
    public List<Double>       getMinorTicks(){
        return this.minorTicks;
    }
    
    public ArrayList<Double>  getCoordinates(){
        return niceCoordinates;
    }
    /**
     * Returns a "nice" number approximately equal to range Rounds
     * the number if round = true Takes the ceiling if round = false.
     *
     * @param range the data range
     * @param round whether to round the result
     * @return a "nice" number to be used for the data range
     */
    private double niceNum(double range, boolean round) {
        double exponent; /** exponent of range */
        double fraction; /** fractional part of range */
        double niceFraction; /** nice, rounded fraction */
        
        exponent = Math.floor(Math.log10(range));
        fraction = range / Math.pow(10, exponent);
        
        if (round) {
            if (fraction < 1.5)
                niceFraction = 1;
            else if (fraction < 3)
                niceFraction = 2;
            else if (fraction < 7)
                niceFraction = 5;
            else
                niceFraction = 10;
        } else {
            if (fraction <= 1)
                niceFraction = 1;
            else if (fraction <= 2)
                niceFraction = 2;
            else if (fraction <= 5)
                niceFraction = 5;
            else
                niceFraction = 10;
        }
        
        return niceFraction * Math.pow(10, exponent);
    }
    
    /**
     * Sets the minimum and maximum data points for the axis.
     *
     * @param minPoint the minimum data point on the axis
     * @param maxPoint the maximum data point on the axis
     */
    public void setMinMaxPoints(double minPoint, double maxPoint) {
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        calculate();
    }
    
    /**
     * Sets maximum number of tick marks we're comfortable with
     *
     * @param maxTicks the maximum number of tick marks for the axis
     */
    public void setMaxTicks(int maxTicks) {
        this.maxTicks = maxTicks;
        if(this.isAxisLogarithmic==true){
            this.calculateLog();
        } else {
            this.calculate();
        }
    }
    
    public int getMaxTicks(){
        return this.maxTicks;
    }
    
    public int getSigFig(){
        if(niceCoordinates.size()<1) return 0;
        double min = niceCoordinates.get(0);
        double max = niceCoordinates.get(niceCoordinates.size()-1);
        double difference = max-min;
        int   placeOfDifference = (int) Math.floor(Math.log(difference) / Math.log(10));        
        //System.err.println(" AXIS = " + min + " " + max + " " + difference
        //+ "  " + placeOfDifference );
        return (int) - placeOfDifference;
    }
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        /*
        for(int loop = 0; loop < maxTicks; loop++)
            str.append(String.format("%12.4f", niceMin + loop*this.tickSpacing ));
        */
        for(int loop = 0; loop < this.niceCoordinateLabels.size(); loop++){
            str.append(" ");
            str.append(this.niceCoordinateLabels.get(loop));
        }
        str.append("\n");
        return str.toString();    
    }
    
    public static void main(String[] args){
        AxisNiceScale axis = new AxisNiceScale(5.0,25.0);
        for(int loop = 10; loop > 1 ; loop--){
            
            axis.setMaxTicks(loop);
            axis.calculate();
            System.out.println(" AXIS MAX TICKS = " + loop);
            System.err.println(axis);
        }
        
        double minp = 0.01;
        double maxp = 120.0;
        int maxticks = 10;
        
        double range = Math.floor(Math.log10(maxp)) - Math.floor(Math.log10(minp));
        double tickSpacing = range/(maxticks-1);
        System.out.println("----->>>>> RANGE = " + range +
                "  tickSpacing = " + tickSpacing);
        
        
        AxisNiceScale  axisLog = new AxisNiceScale(0,50.0);
        axisLog.setAxisLog(true);
        System.out.println(axisLog);
        //int minlog =  (int) Math.floor(Math.log10(this.minPoint));
        //int maxlog =  (int) Math.floor(Math.log10(this.maxPoint));
    }
}
