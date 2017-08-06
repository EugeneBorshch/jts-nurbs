package com.github.eborshch.nurbs;

import com.vividsolutions.jts.geom.Coordinate;

import javax.vecmath.GMatrix;
import javax.vecmath.GVector;

/**
 * Build Nurbs with JTS coordinates.
 *
 * @author Eugene Borshch
 */
public class NURBsBuilder
{

    /**
     * Interpolates a NurbCurve form the given Points using a global interpolation technique.
     *
     * @param points Points to interpolate
     * @param degree degree of the interpolated NurbsCurve
     * @return A NurbsCurve interpolating the given Points
     */
    public static NURBsCurve build(Coordinate[] points, int degree)
    {

        final int n = points.length;
        final double[] A = new double[n * n];

        final double[] uk = centripetal(points);
        KnotVector uKnots = averaging(uk, degree);
        for (int i = 0; i < n; i++)
        {
            int span = uKnots.findSpan(uk[i]);
            double[] tmp = uKnots.basisFunctions(span, uk[i]);
            System.arraycopy(tmp, 0, A, i * n + span - degree, tmp.length);
        }
        final GMatrix a = new GMatrix(n, n, A);
        final GVector perm = new GVector(n);
        final GMatrix lu = new GMatrix(n, n);

        a.LUD(lu, perm);

        final ControlPoint[] cps = new ControlPoint[n];
        for (int i = 0; i < cps.length; i++)
        {
            cps[i] = new ControlPoint(new Coordinate(0, 0, 0), 1);
        }

        // x-ccordinate
        final GVector b = new GVector(n);
        for (int j = 0; j < n; j++)
        {
            b.setElement(j, points[j].x);
        }
        final GVector sol = new GVector(n);
        sol.LUDBackSolve(lu, b, perm);

        for (int j = 0; j < n; j++)
        {
            cps[j].getCoordinate().x = sol.getElement(j);
        }

        // y-ccordinate
        for (int j = 0; j < n; j++)
        {
            b.setElement(j, points[j].y);
        }
        sol.zero();
        sol.LUDBackSolve(lu, b, perm);
        for (int j = 0; j < n; j++)
        {
            cps[j].getCoordinate().y = sol.getElement(j);
        }

        // z-ccordinate
        for (int j = 0; j < n; j++)
        {
            b.setElement(j, points[j].z);
        }
        sol.zero();
        sol.LUDBackSolve(lu, b, perm);
        for (int j = 0; j < n; j++)
        {
            cps[j].getCoordinate().z = sol.getElement(j);
        }

        return new NURBsCurve(cps, uKnots);

    }

    private static double[] centripetal(Coordinate[] points)
    {
        int n = points.length - 1;
        double d = 0;
        double[] uk = new double[n + 1];
        uk[n] = 1;
        double[] tmp = new double[n];
        for (int k = 1; k <= n; k++)
        {
            tmp[k - 1] = Math.sqrt(distanceTo(points[k], (points[k - 1])));
            d += tmp[k - 1];
        }
        d = 1f / d;
        for (int i = 1; i < n; i++)
        {
            uk[i] = uk[i - 1] + (tmp[i - 1] * d);
        }
        return uk;
    }

    private static KnotVector averaging(final double[] uk, final int p)
    {
        int m = uk.length + p;
        int n = uk.length - 1;
        double ip = 1f / p;
        double[] u = new double[m + 1];
        for (int i = 0; i <= p; i++)
        {
            u[m - i] = 1;
        }
        for (int j = 1; j <= n - p; j++)
        {
            double sum = 0;
            for (int i = j; i <= j + p - 1; i++)
            {
                sum += uk[i];
            }
            u[j + p] = sum * ip;
        }
        return new KnotVector(u, p);
    }

    private static double distanceTo(Coordinate s, Coordinate v)
    {
        if (v != null)
        {
            final double dx = s.x - v.x;
            final double dy = s.y - v.y;
            final double dz = s.z - v.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        } else
        {
            return Double.NaN;
        }
    }

}
