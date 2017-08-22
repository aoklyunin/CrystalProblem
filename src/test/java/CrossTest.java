import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by aokly on 17.08.2017.
 */
public class CrossTest {

    @Before
    public void initTest() {

    }

    // пересечение одного списка другим
    @Test
    public void splitOneByOther() {
        LinkedList<Price> p1 = new LinkedList<>(Arrays.asList(
                new Price("122856", 1, 1, d(1, 1, 2013), d(31, 1, 2013), 11000),
                new Price("122856", 2, 1, d(10, 1, 2013), d(20, 1, 2013), 99000),
                new Price("6654", 1, 2, d(1, 1, 2013), d(31, 1, 2013), 5000)
        ));
        LinkedList<Price> p2 = new LinkedList<>(Arrays.asList(
                new Price("122856", 1, 1, d(20, 1, 2013), d(20, 2, 2013), 11000),
                new Price("122856", 2, 1, d(15, 1, 2013), d(25, 1, 2013), 92000),
                new Price("6654", 1, 2, d(12, 1, 2013), d(13, 1, 2013), 4000)
        ));
        PriceAdmin.splitOneByOther(p2, p1);
        Collections.sort(p1, Price.getComparator());

        assertEquals(new LinkedList<>(Arrays.asList(
                new Price("122856", 1, 1, d(1, 1, 2013), d(20, 1, 2013), 11000),
                new Price("122856", 1, 1, d(20, 1, 2013), d(31, 1, 2013), 11000),
                new Price("122856", 2, 1, d(10, 1, 2013), d(15, 1, 2013), 99000),
                new Price("122856", 2, 1, d(15, 1, 2013), d(20, 1, 2013), 99000),
                new Price("6654", 1, 2, d(1, 1, 2013), d(12, 1, 2013), 5000),
                new Price("6654", 1, 2, d(12, 1, 2013), d(13, 1, 2013), 5000),
                new Price("6654", 1, 2, d(13, 1, 2013), d(31, 1, 2013), 5000)
        )), p1);
    }

    // проверка из задания
    @Test
    public void test1() throws Exception {
        assertEquals(new LinkedList<>(Arrays.asList(
                new Price("122856", 1, 1, d(1, 1, 2013), d(20, 2, 2013), 11000),
                new Price("122856", 2, 1, d(10, 1, 2013), d(15, 1, 2013), 99000),
                new Price("122856", 2, 1, d(15, 1, 2013), d(25, 1, 2013), 92000),
                new Price("6654", 1, 2, d(1, 1, 2013), d(12, 1, 2013), 5000),
                new Price("6654", 1, 2, d(12, 1, 2013), d(13, 1, 2013), 4000),
                new Price("6654", 1, 2, d(13, 1, 2013), d(31, 1, 2013), 5000)
                )),
                PriceAdmin.addNewPrices(new LinkedList<Price>(Arrays.asList(
                        new Price("122856", 1, 1, d(1, 1, 2013), d(31, 1, 2013), 11000),
                        new Price("122856", 2, 1, d(10, 1, 2013), d(20, 1, 2013), 99000),
                        new Price("6654", 1, 2, d(1, 1, 2013), d(31, 1, 2013), 5000)
                        )),
                        new LinkedList<Price>(Arrays.asList(
                                new Price("122856", 1, 1, d(20, 1, 2013), d(20, 2, 2013), 11000),
                                new Price("122856", 2, 1, d(15, 1, 2013), d(25, 1, 2013), 92000),
                                new Price("6654", 1, 2, d(12, 1, 2013), d(13, 1, 2013), 4000)
                        ))));
    }


    // собственная проверка
    @Test
    public void test2() throws Exception {
        assertEquals(new LinkedList<>(Arrays.asList(
                new Price("122856", 1, 1, d(1, 1, 2013), d(15, 2, 2013), 92000),
                new Price("122856", 1, 1, d(15, 2, 2013), d(18, 2, 2013), 99000),
                new Price("122856", 1, 1, d(18, 2, 2013), d(20, 3, 2013), 15000),
                new Price("122856", 1, 1, d(20, 3, 2013), d(20, 4, 2013), 99000),
                new Price("122856", 1, 2, d(1, 1, 2013), d(31, 1, 2013), 11000),

                new Price("1412", 1, 2, d(1, 1, 2013), d(1, 2, 2013), 4000),
                new Price("1412", 1, 2, d(1, 2, 2013), d(15, 3, 2013), 5000),
                new Price("1412", 1, 2, d(15, 3, 2013), d(10, 4, 2013), 6000),
                new Price("6654", 1, 2, d(1, 1, 2013), d(12, 1, 2013), 5000),
                new Price("6654", 1, 2, d(12, 1, 2013), d(13, 1, 2013), 4000),
                new Price("6654", 1, 2, d(13, 1, 2013), d(31, 1, 2013), 5000)
                )),

                PriceAdmin.addNewPrices(new LinkedList<Price>(Arrays.asList(
                        new Price("122856", 1, 2, d(1, 1, 2013), d(31, 1, 2013), 11000),
                        new Price("122856", 1, 1, d(10, 2, 2013), d(20, 4, 2013), 99000),
                        new Price("6654", 1, 2, d(1, 1, 2013), d(31, 1, 2013), 5000),
                        new Price("1412", 1, 2, d(1, 1, 2013), d(12, 2, 2013), 4000),
                        new Price("1412", 1, 2, d(1, 3, 2013), d(10, 4, 2013), 6000)
                        )),
                        new LinkedList<Price>(Arrays.asList(
                                new Price("122856", 1, 1, d(18, 2, 2013), d(20, 3, 2013), 15000),
                                new Price("122856", 1, 1, d(1, 1, 2013), d(15, 2, 2013), 92000),
                                new Price("6654", 1, 2, d(12, 1, 2013), d(13, 1, 2013), 4000),
                                new Price("1412", 1, 2, d(1, 2, 2013), d(15, 3, 2013), 5000)
                        ))));
    }

    private static Date d(int days, int mounts, int years) {
        return new Date(years, mounts - 1, days, 0, 0, 0);
    }
}