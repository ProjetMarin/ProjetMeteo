// docs start source
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.test;

import grib.LectureGribUnidata;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JToolBar;

import org.geotools.data.DataUtilities;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.event.MapBoundsEvent;
import org.geotools.map.event.MapBoundsListener;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.Symbolizer;
import org.geotools.swing.JMapFrame;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * In this example we create a map tool to select a feature clicked
 * with the mouse. The selected feature will be painted yellow.
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/demo/example/src/main/java/org/geotools/demo/SelectionLab.java $
 */
public class SelectionLab {

    /*
     * Factories that we will use to create style and filter objects
     */
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    /*
     * Convenient constants for the type of feature geometry in the shapefile
     */
    private enum GeomType { POINT, LINE, POLYGON };

    /*
     * Some default style variables
     */
    private static final Color LINE_COLOUR = new Color(25,125,190);
    private static final Color FILL_COLOUR = new Color(70,165,230);
    private static final Color SELECTED_COLOUR = Color.YELLOW;
    private static final float OPACITY = 1.0f;
    private static final float LINE_WIDTH = 1.0f;
    private static final float POINT_SIZE = 10.0f;
	private static final SimpleFeatureType TYPE = null;
	
	static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
    static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

    private JMapFrame mapFrame;
    private SimpleFeatureSource featureSource;

    private String geometryAttributeName;
    private GeomType geometryType;
    
    private DefaultFeatureCollection lineCollection;
    
    private final GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
    private SimpleFeatureBuilder featureBuilder;
    private Layer layerLine;
    
    private LectureGribUnidata lgu;

    /*
     * The application method
     */
    public static void main(String[] args) throws Exception {
        SelectionLab me = new SelectionLab();

//        File file = JFileDataStoreChooser.showOpenFile("shp", null);
        File file = new File("ne_50m_ocean/ne_50m_ocean.shp");
//        if (file == null) {
//            return;
//        }

        me.displayShapefile(file);
    }
// docs end main

// docs start display shapefile
    /**
     * This method connects to the shapefile; retrieves information about
     * its features; creates a map frame to display the shapefile and adds
     * a custom feature selection tool to the toolbar of the map frame.
     */
    public void displayShapefile(File file) throws Exception {
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        lgu = new LectureGribUnidata();
        featureSource = store.getFeatureSource();
        setGeometry();
        
        org.opengis.style.StyleFactory sf = CommonFactoryFinder.getStyleFactory();

        /*
         * Create the JMapFrame and set it to display the shapefile's features
         * with a default line and colour style
         */
        MapContent map = new MapContent();
        map.setTitle("Feature selection tool example");
        Style style = createDefaultStyle();
        Layer layer = new FeatureLayer(featureSource, style);
        
        SimpleFeatureType TYPE = DataUtilities.createType("test", "line", "the_geom:LineString");
        featureBuilder = new SimpleFeatureBuilder((SimpleFeatureType) TYPE);

        int size = 16;
        
        DefaultFeatureCollection[] listLineCollection = new DefaultFeatureCollection [size+6];
        FeatureLayer[] listFeatureLayer = new FeatureLayer [size+6];
        
        for(int i=0;i<size+6;i++)
			listLineCollection[i] = new DefaultFeatureCollection();
        
        map.addLayer(layer);
        map.addMapBoundsListener(new MapBoundsListener() {
			
			@Override
			public void mapBoundsChanged(MapBoundsEvent arg0) {
				double length = Math.min(arg0.getNewAreaOfInterest().getWidth()*0.05,5);
				//length = arg0.getNewAreaOfInterest().getWidth();
				int j = 0;
				
				for(int i=size;i<size+6;i++){
					map.removeLayer(listFeatureLayer[i]);	
					listFeatureLayer[i] = new FeatureLayer(listLineCollection[i], createLineStyle(false));
					listLineCollection[i].add(drawWindBarb(new Coordinate(i, i), 180, length));
					
					map.addLayer(listFeatureLayer[i]);
				}
				
				for(int i=0;i<size;i++){
					map.removeLayer(listFeatureLayer[i]);	
					listFeatureLayer[i] = new FeatureLayer(listLineCollection[i], createLineStyle(true));
					if(i%4==0)
						j++;
					listLineCollection[i].add(drawWindBarb(new Coordinate((i%4)*8, (j*3)), 90, length));
					
					map.addLayer(listFeatureLayer[i]);
				}
				mapFrame.repaint();
			}
		});
        
        mapFrame = new JMapFrame(map);
        mapFrame.enableToolBar(true);
        mapFrame.enableStatusBar(true);
        
        /*
         * Before making the map frame visible we add a new button to its
         * toolbar for our custom feature selection tool
         */
        JToolBar toolBar = mapFrame.getToolBar();
        JButton btn = new JButton("Select");
        toolBar.addSeparator();
        toolBar.add(btn);

        /**
         * Finally, we display the map frame. When it is closed
         * this application will exit.
         */
        mapFrame.setSize(600, 600);
        mapFrame.setVisible(true);
    }
// docs end display shapefile


// docs start display selected
    /**
     * Sets the display to paint selected features yellow and
     * unselected features in the default style.
     *
     * @param IDs identifiers of currently selected features
     */
    public void displaySelectedFeatures(Set<FeatureId> IDs) {
        Style style;

        if (IDs.isEmpty()) {
            style = createDefaultStyle();

        } else {
            style = createSelectedStyle(IDs);
        }

        Layer layer = mapFrame.getMapContent().layers().get(0);
        ((FeatureLayer) layer).setStyle(style);
        mapFrame.getMapPane().repaint();
    }
// docs end display selected

// docs start default style
    /**
     * Create a default Style for feature display
     */
    private Style createDefaultStyle() {
        Rule rule = createRule(LINE_COLOUR, FILL_COLOUR);

        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        fts.rules().add(rule);

        Style style = sf.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }
// docs end default style

// docs start selected style
    /**
     * Create a Style where features with given IDs are painted
     * yellow, while others are painted with the default colors.
     */
    private Style createSelectedStyle(Set<FeatureId> IDs) {
        Rule selectedRule = createRule(SELECTED_COLOUR, SELECTED_COLOUR);
        selectedRule.setFilter(ff.id(IDs));

        Rule otherRule = createRule(LINE_COLOUR, FILL_COLOUR);
        otherRule.setElseFilter(true);

        FeatureTypeStyle fts = sf.createFeatureTypeStyle();
        fts.rules().add(selectedRule);
        fts.rules().add(otherRule);

        Style style = sf.createStyle();
        style.featureTypeStyles().add(fts);
        return style;
    }
// docs end selected style

// docs start create rule
    /**
     * Helper for createXXXStyle methods. Creates a new Rule containing
     * a Symbolizer tailored to the geometry type of the features that
     * we are displaying.
     */
    private Rule createRule(Color outlineColor, Color fillColor) {
        Symbolizer symbolizer = null;
        Fill fill = null;
        Stroke stroke = sf.createStroke(ff.literal(outlineColor), ff.literal(LINE_WIDTH));

        switch (geometryType) {
            case POLYGON:
                fill = sf.createFill(ff.literal(fillColor), ff.literal(OPACITY));
                symbolizer = sf.createPolygonSymbolizer(stroke, fill, geometryAttributeName);
                break;

            case LINE:
                symbolizer = sf.createLineSymbolizer(stroke, geometryAttributeName);
                break;

            case POINT:
                fill = sf.createFill(ff.literal(fillColor), ff.literal(OPACITY));

                Mark mark = sf.getCircleMark();
                mark.setFill(fill);
                mark.setStroke(stroke);

                Graphic graphic = sf.createDefaultGraphic();
                graphic.graphicalSymbols().clear();
                graphic.graphicalSymbols().add(mark);
                graphic.setSize(ff.literal(POINT_SIZE));

                symbolizer = sf.createPointSymbolizer(graphic, geometryAttributeName);
        }

        Rule rule = sf.createRule();
        rule.symbolizers().add(symbolizer);
        return rule;
    }
// docs end create rule

// docs start set geometry
    /**
     * Retrieve information about the feature geometry
     */
    private void setGeometry() {
        GeometryDescriptor geomDesc = featureSource.getSchema().getGeometryDescriptor();
        geometryAttributeName = geomDesc.getLocalName();

        Class<?> clazz = geomDesc.getType().getBinding();

        if (Polygon.class.isAssignableFrom(clazz) ||
                MultiPolygon.class.isAssignableFrom(clazz)) {
            geometryType = GeomType.POLYGON;

        } else if (LineString.class.isAssignableFrom(clazz) ||
                MultiLineString.class.isAssignableFrom(clazz)) {

            geometryType = GeomType.LINE;

        } else {
            geometryType = GeomType.POINT;
        }

    }
// docs end set geometry
    
    private Style createLineStyle(boolean c) {
        Stroke stroke;
        if(c)
        	stroke = styleFactory.createStroke(filterFactory.literal(Color.BLUE),filterFactory.literal(2));
        else
        	stroke = styleFactory.createStroke(filterFactory.literal(new Color(0,0,0,0)),filterFactory.literal(2));

        /*
         * Setting the geometryPropertyName arg to null signals that we want to
         * draw the default geomettry of features
         */
        LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);

        Rule rule = styleFactory.createRule();
        rule.symbolizers().add(sym);
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(new Rule[]{rule});
        Style style = styleFactory.createStyle();
        style.featureTypeStyles().add(fts);

        return style;
    }
    
    
    private SimpleFeature drawWindBarb(Coordinate c1, int a, double l) {
    	a = - a + 90;
    	Coordinate c2 = new Coordinate(l * Math.cos(Math.toRadians(a)), l * Math.sin(Math.toRadians(a)));
    	Coordinate c3 = new Coordinate(l * 1/2 *Math.cos(Math.toRadians(a-135)), l/2 * Math.sin(Math.toRadians(a-135)));
    	Coordinate[] coords = new Coordinate[] {c1, new Coordinate(c1.x + c2.x, c1.y + c2.y),new Coordinate(c1.x +c2.x + c3.x, c1.y + c2.y + c3.y)};
    	System.out.println("length : "+l+", coord1 : "+(int)coords[0].x+", "+(int)coords[0].y+", coord2 : "+(int)coords[1].x+", "+(int)coords[1].y+", coord3 : "+(int)coords[2].x+", "+(int)coords[2].y);
        LineString line = geometryFactory.createLineString(coords);
        featureBuilder.add(line);
        return featureBuilder.buildFeature("LineString_Sample");
    }

}