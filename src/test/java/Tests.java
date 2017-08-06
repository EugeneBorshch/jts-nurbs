import com.github.eborshch.nurbs.NURBsBuilder;
import com.github.eborshch.nurbs.NURBsCurve;
import com.vividsolutions.jts.geom.Coordinate;
import org.junit.Test;

/**
 * @author Eugene Borshch
 */
public class Tests
{
    @Test public void test()
    {

        //LINESTRING(11.6596806049334 48.0291903018899,11.6590315103518 48.0293619632669,11.6586077213275 48.0295014381356,11.6582697629916 48.0296194553323,11.657797694205 48.0298393964715,11.6574704647051 48.0300110578485,11.6571325063693 48.0301988124795,11.6566926240908 48.0305421352334,11.6563224792468 48.0308800935693,11.6559898853289 48.0312180519052,11.6555285453784 48.0320227146096)
        // LINESTRING (30 10, 10 30, 40 40, 50 55, 20 )
        Coordinate[] point = new Coordinate[11];
        point[0] = new Coordinate(11.6596806049334f, 48.0291903018899f, 0);
        point[1] = new Coordinate(11.6590315103518f, 48.0293619632669f, 0);
        point[2] = new Coordinate(11.6586077213275f, 48.0295014381356f, 0);
        point[3] = new Coordinate(11.6582697629916f, 48.0296194553323f, 0);
        point[4] = new Coordinate(11.657797694205f, 48.0298393964715f, 0);
        point[5] = new Coordinate(11.6574704647051f, 48.0300110578485f, 0);
        point[6] = new Coordinate(11.6571325063693f, 48.0301988124795f, 0);
        point[7] = new Coordinate(11.6566926240908f, 48.0305421352334f, 0);
        point[8] = new Coordinate(11.6563224792468f, 48.0308800935693f, 0);
        point[9] = new Coordinate(11.6559898853289f, 48.0312180519052f, 0);
        point[10] = new Coordinate(11.6555285453784f, 48.0320227146096f, 0);

        NURBsCurve basicNurbsCurve = NURBsBuilder.build(point, 3);

        System.out.println(basicNurbsCurve.asLineString(5));
        System.out.println(basicNurbsCurve.asLineString(point.length));
        System.out.println(basicNurbsCurve.asLineString(10));
        System.out.println(basicNurbsCurve.asLineString(30));
        System.out.println(basicNurbsCurve.asLineString(300));

    }

}
