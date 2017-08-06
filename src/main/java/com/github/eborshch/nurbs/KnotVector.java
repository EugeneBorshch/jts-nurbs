/*
 * jgeom: Geometry Library fo Java
 *
 * Copyright (C) 2005  Samuel Gerber
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.github.eborshch.nurbs;

/**
 * KnotVector, assembles the knots values of a NURBS and its degree.
 *
 * @author sg
 * @version 1.0
 */
public class KnotVector
{

    private boolean isOpen;
    private double knots[];
    private int degree;
    private int n;

    /**
     * Create a Knotvector from the given knot values of the desired degree.
     *
     * @param knots  knot values
     * @param degree degree of Nurbs
     */
    public KnotVector(double knots[], int degree) throws IllegalArgumentException
    {
        this.knots = knots;
        this.degree = degree;
        n = knots.length - degree - 2;
        for (int i = 1; i < knots.length; i++)
        {
            if (knots[i - 1] > knots[i])
            {
                throw new IllegalArgumentException(
                        String.format("Knots not valid knot[%d] > knot[%d]: knot[%d]=%s > knot[%d]=%s", i - 1, i, i - 1,
                                knots[i - 1], i, knots[i]));
            }
        }

        int m = knots.length - 1;

        // Check if it is an open knot vector 
        isOpen = true;
        for (int k = 0; k < degree && isOpen; k++)
        {
            if (knots[k] != knots[k + 1])
            {
                isOpen = false;
            }
        }
        for (int k = m; k > m - degree && isOpen; k--)
        {
            if (knots[k] != knots[k - 1])
            {
                isOpen = false;
            }
        }

    }

    /**
     * Calculates the basis function values for the given u value, when it's
     * already known in which span u lies.
     *
     * @param span The span u lies in
     * @param u    Value to calculate basis functions for.
     * @return basis function values
     */
    public double[] basisFunctions(int span, double u)
    {
        final int d1 = degree + 1;
        double res[] = new double[d1];
        double left[] = new double[d1];
        double right[] = new double[d1];
        res[0] = 1;
        for (int j = 1; j < d1; j++)
        {
            left[j] = u - knots[span + 1 - j];
            right[j] = knots[span + j] - u;
            double saved = 0;
            for (int r = 0; r < j; r++)
            {
                double tmp = res[r] / (right[r + 1] + left[j - r]);
                res[r] = saved + right[r + 1] * tmp;
                saved = left[j - r] * tmp;
            }
            res[j] = saved;
        }
        return res;
    }

    /**
     * Finds the span (Position of corresponding knot values in knot vector) a
     * given value belongs to.
     *
     * @param u value to find span for
     * @return Position of span.
     */
    public int findSpan(double u)
    {
        if (u >= knots[n + 1])
        {
            return n;
        }
        int low = degree;
        int high = n + 1;
        int mid = (low + high) / 2;
        while ((u < knots[mid] || u >= knots[mid + 1]) && low < high)
        {
            if (u < knots[mid])
            {
                high = mid;
            } else
            {
                low = mid;
            }
            mid = (low + high) / 2;
        }
        return mid;
    }

    /**
     * get the knot values as double array
     *
     * @return the knot values
     */
    public double[] getKnots()
    {
        return knots;
    }

    /**
     * Get the degree of the KnotVector
     *
     * @return Degree of the Knotvector
     */
    public int getDegree()
    {
        return degree;
    }

    /**
     * Return the nu
     *
     * @return Length of the KnotVector
     */
    public int getN()
    {
        return n;
    }

}