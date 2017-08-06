package com.github.eborshch.nurbs;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Nurbs control point.
 *
 * @author  Eugene Borshch
 */
public class ControlPoint
{
    private static final float EPS = 1.1920928955078125E-7f;

    private Coordinate coordinate;

    private double weight;

    public ControlPoint(Coordinate coordinate, double w)
    {
        this.coordinate = coordinate;
        this.weight = w;
    }

    public Coordinate getCoordinate()
    {
        return coordinate;
    }

    public double getWeight()
    {
        return weight;
    }

    public ControlPoint add(ControlPoint v)
    {
        Coordinate newCoordinate = new Coordinate(this.coordinate.x + v.coordinate.x,
                this.coordinate.y + v.coordinate.y, this.coordinate.z + v.coordinate.z);
        return new ControlPoint(newCoordinate, weight + v.weight);
    }

    public ControlPoint scale(double s)
    {
        Coordinate newCoordinate = new Coordinate(this.coordinate.x * s, this.coordinate.y * s, this.coordinate.z * s);
        return new ControlPoint(newCoordinate, weight * s);
    }

    public ControlPoint unWeighted()
    {
        double iw = Math.abs(weight) > EPS ? 1.0 / weight : 0;
        Coordinate newCoordinate = new Coordinate(this.coordinate.x * iw, this.coordinate.y * iw,
                this.coordinate.z * iw);

        return new ControlPoint(newCoordinate, this.weight);
    }

    public ControlPoint weighted()
    {
        Coordinate newCoordinate = new Coordinate(this.coordinate.x * this.weight, this.coordinate.y * this.weight,
                this.coordinate.z * this.weight);

        return new ControlPoint(newCoordinate, this.weight);
    }


}