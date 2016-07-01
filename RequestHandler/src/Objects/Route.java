package Objects;

import java.util.ArrayList;

public class Route {
    private ArrayList<RouteSegment> segmentToPoI;

    public Route() {
        this.segmentToPoI = new ArrayList<>();
    }

    public ArrayList<RouteSegment> getSegmentToPoI() {
        return segmentToPoI;
    }

    public RouteSegment getSegmentAt(int index){
        return segmentToPoI.get(index);
    }

    public void setSegmentToPoI(ArrayList<RouteSegment> segmentToPoI) {
        this.segmentToPoI = segmentToPoI;
    }
    
    public void appendRouteSegment(RouteSegment otherSegment){
    	this.segmentToPoI.add(otherSegment);
    }
    
    public void printValues(){
    	System.out.println("Printing the Segments: ");
    	for(int i=0; i<segmentToPoI.size(); i++){
    		System.out.println("=========Segment" + i + "=========");
    		segmentToPoI.get(i).printValues();
    	}	
    }
}
