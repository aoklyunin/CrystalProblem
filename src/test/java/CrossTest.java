import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * Created by aokly on 17.08.2017.
 */
public class CrossTest {
    Price newPrice, oldPrice;

    @Before
    public void initTest() {
        Date dt = new Date();
        oldPrice = new Price(1, "1", 1, 1, dt, dt, 10);
        newPrice = new Price(1, "1", 1, 1, dt, dt, 20);
    }

    // совпадение
    @Test
    public void cross1() throws Exception {
        assertEquals(new PriceAdmin.ReturnPrice(
                new Price(newPrice, dm(1, 2), dm(10, 2))
        ), getAssertReturnPrice(
                1, 2, 10, 2,
                1, 2, 10, 2));
    }

    // совпадение
    @Test
    public void cross2() throws Exception {
        assertEquals(new PriceAdmin.ReturnPrice(
                new Price(newPrice, dm(11, 2), dm(12, 2))
        ), getAssertReturnPrice(
                11, 2, 12, 2,
                11, 2, 12, 2));
    }


    // не пересекаются
    @Test
    public void cross3() throws Exception {
        assertEquals(new PriceAdmin.ReturnPrice(
                null,
                new Price(oldPrice, dm(1, 2), dm(10, 2)),
                new Price(newPrice, dm(1, 1), dm(12, 1))
        ), getAssertReturnPrice(
                1, 2, 10, 2,
                1, 1, 12, 1));
    }

    // не пересекаются
    @Test
    public void cross4() throws Exception {
        assertEquals(new PriceAdmin.ReturnPrice(
                null,
                new Price(oldPrice, dm(1, 2), dm(10, 2)),
                new Price(newPrice, dm(1, 3), dm(12, 3))
        ), getAssertReturnPrice(
                1, 2, 10, 2,
                1, 3, 12, 3));
    }

    // пересекаются старая раньше новой
    @Test
    public void cross5() throws Exception {
        assertEquals(new PriceAdmin.ReturnPrice(
                new Price(newPrice, dm(1, 3), dm(10, 4)),
                new Price(oldPrice, dm(1, 2), dm(1, 3)),
                new Price(newPrice, dm(10, 4), dm(12, 5))
        ), getAssertReturnPrice(
                1, 2, 10, 4,
                1, 3, 12, 5));
    }

    // пересекаются старая раньше новой
    @Test
    public void cross6() throws Exception {
        assertEquals(new PriceAdmin.ReturnPrice(
                new Price(newPrice, dm(3, 4), dm(2, 5)),
                new Price(oldPrice, dm(4, 3), dm(3, 4)),
                new Price(newPrice, dm(2, 5), dm(10, 6))
        ), getAssertReturnPrice(
                4, 3, 2, 5,
                3, 4, 10, 6));
    }

    // пересекаются новая внутри старой
    @Test
    public void cross7() throws Exception {
        assertEquals(new PriceAdmin.ReturnPrice(
                new Price(newPrice, dm(3, 4), dm(6, 5)),
                new HashSet<Price>(Arrays.asList(
                        new Price(oldPrice, dm(1, 1), dm(3, 4)),
                        new Price(oldPrice, dm(6, 5), dm(10, 8)))),
                new HashSet<Price>()
        ), getAssertReturnPrice(
                1, 1, 10, 8,
                3, 4, 6, 5));
    }

    // пересекаются новая внутри старой
    @Test
    public void cross8() throws Exception {
        assertEquals(new PriceAdmin.ReturnPrice(
                new Price(newPrice, dm(5, 3), dm(16, 8)),
                new HashSet<Price>(Arrays.asList(
                        new Price(oldPrice, dm(4, 3), dm(5, 3)),
                        new Price(oldPrice, dm(16, 8), dm(5, 11)))),
                new HashSet<Price>()
        ), getAssertReturnPrice(
                4, 3, 5, 11,
                5, 3, 16, 8));
    }


    // пересекаются старая раньше новой, концы совпадают
    @Test
    public void cross9() throws Exception {
        assertEquals(new PriceAdmin.ReturnPrice(
                        new Price(newPrice, dm(3, 4), dm(7, 6)),
                        new Price(oldPrice, dm(1, 2), dm(3, 4)),
                        new HashSet<Price>()),
                getAssertReturnPrice(
                        1, 2, 7, 6,
                        3, 4, 7, 6));
    }

    // пересекаются старая раньше новой, концы совпадают
    @Test
    public void cross10() throws Exception {
        assertEquals(new PriceAdmin.ReturnPrice(
                        new Price(newPrice, dm(13, 6), dm(10, 7)),
                        new Price(oldPrice, dm(2, 5), dm(13, 6)),
                        new HashSet<Price>()),
                getAssertReturnPrice(
                        2, 5, 10, 7,
                        13, 6, 10, 7));
    }



    // сгенерировать дату по дню и месяцу
    private Date dm(int date, int month) {
        return new Date(2017, month, date, 0, 0);
    }

    // по числам получаем пересечение дат
    private PriceAdmin.ReturnPrice getAssertReturnPrice(int dOldB, int mOldB, int dOldE, int mOldE,
                                                        int dNewB, int mNewB, int dNewE, int mNewE) throws PriceAdmin.UnpredictableCaseException {
        return PriceAdmin.cross(
                new Price(oldPrice, dm(dOldB, mOldB), dm(dOldE, mOldE)),
                new Price(newPrice, dm(dNewB, mNewB), dm(dNewE, mNewE))
        );
    }
}