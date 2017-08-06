package com.github.eborshch.nurbs;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * A Basic implementation of a NurbsCurve.
 *
 * @author sg
 * @version 1.0
 */
public class NURBsCurve implements Cloneable
{

    private ControlPoint[] controlPoints;
    private KnotVector knots;

    private GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * Generate a Nurbs from the given ControlPoints and the given KnotVector.<br>
     *
     * @param cps   Array of Controlpoints
     * @param knots KnotVector of the Nurbs
     */
    public NURBsCurve(ControlPoint[] cps, KnotVector knots)
    {
        this.controlPoints = cps;
        this.knots = knots;
        if (knots.getKnots().length != knots.getDegree() + controlPoints.length + 1)
        {
            throw new IllegalArgumentException("Nurbs Curve has wrong knot number");
        }
    }

    public ControlPoint[] getControlPoints()
    {
        return controlPoints;
    }

    public int getDegree()
    {
        return knots.getDegree();
    }

    public double[] getKnots()
    {
        return knots.getKnots();
    }

    public LineString asLineString(int numPoints)
    {
        Coordinate[] coordinates = new Coordinate[numPoints];
        for (int i = 0; i < numPoints; i++)
        {
            double scale = (double) i / (double) (numPoints - 1);

            Coordinate vec3D = pointOnCurve(scale);
            coordinates[i] = new Coordinate(vec3D.x, vec3D.y, vec3D.z);
        }
        return geometryFactory.createLineString(coordinates);
    }

    private Coordinate pointOnCurve(double u)
    {
        int span = knots.findSpan(u);
        int degree = knots.getDegree();

        if (span < degree)
        {
            return new Coordinate();
        }

        if (span > knots.getN())
        {
            return new Coordinate();
        }

        double[] bf = knots.basisFunctions(span, u);
        ControlPoint cw = new ControlPoint(new Coordinate(0, 0, 0), 0.0);
        for (int i = 0; i <= degree; i++)
        {
            ControlPoint controlPoint = controlPoints[(span - degree) + i];
            cw = cw.add(controlPoint.weighted().scale(bf[i]));
        }
        return cw.unWeighted().getCoordinate();
    }
}

