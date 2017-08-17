import java.util.*;

/**
 * Created by aokly on 17.08.2017.
 */
public class PriceAdmin {
    public static ReturnPrice cross(Price oldPrice, Price newPrice) throws UnpredictableCaseException {
        Date bOld = oldPrice.getBegin(); // начало старой цены
        Date bNew = newPrice.getBegin(); // начало новой цены
        Date eOld = oldPrice.getEnd(); // конец старой цены
        Date eNew = newPrice.getEnd(); // конец старой цены
        if (bOld.equals(bNew) && eOld.equals(eNew))  // если периоды совпадают
            return new ReturnPrice(newPrice);
        else if (bOld.after(eNew) || eOld.before(bNew)) // если периоды вообще не пересекаются
            return new ReturnPrice(null, oldPrice, newPrice);
            // случаи пересечения
        else {
            if (bOld.before(bNew) && eOld.before(eNew))
                return new ReturnPrice(
                        new Price(newPrice, bNew, eOld),
                        new Price(oldPrice, bOld, bNew),
                        new Price(newPrice, eOld, eNew));
            if (bOld.before(bNew) && eOld.after(eNew))
                return new ReturnPrice(
                        newPrice,
                        new HashSet<Price>(Arrays.asList(new Price(oldPrice, bOld, bNew),
                                new Price(oldPrice, eNew, eOld))),
                        new HashSet<Price>());
            if (bOld.before(bNew)&&eOld.equals(eNew))
                return  new ReturnPrice(
                        newPrice,
                        new HashSet<Price>(Collections.singletonList(new Price(oldPrice,bOld,bNew))),
                        new HashSet<Price>());
            if (bOld.after(bNew)&&eOld.after(eNew))
                return new ReturnPrice(
                        new Price(newPrice,bOld,eNew),
                        new Price(oldPrice,bNew,eOld),
                        new Price(newPrice,eNew,bOld)
                );
            if (bOld.equals(bNew)&&eNew.before(eOld))
                return new ReturnPrice(
                        newPrice,
                        new Price(oldPrice,eNew,eOld),
                        new HashSet<Price>()
                );
            if (bOld.after(bNew)&&eOld.before(eNew))
                return new ReturnPrice(
                        new Price(newPrice,bOld,eOld),
                        new HashSet<Price>(),
                        new HashSet<Price>(Arrays.asList(new Price(newPrice,bNew,bOld),new Price(newPrice,eOld,eNew)))
                );
        }
        // выбрасываем исключение о непредсказумом сочетании новой и старой цены
        throw new UnpredictableCaseException(oldPrice, newPrice);
    }


    public Collection<Price> addNewPrices(Collection<Price> oldPrices, Collection<Price> newPrices) {
        for (Price price : newPrices) {
            //fo
        }
        return null;
    }


    public static void main(String[] args) {
        //Co
    }

    static class UnpredictableCaseException extends Exception {
        UnpredictableCaseException(Price oldPrice, Price newPrice) {
            super("oldPrice: " + oldPrice + "\nnewPrice: " + newPrice);
        }
    }

    // объект, возвращаемый после сравнения цен
    public static class ReturnPrice {
        // цена, которую уже заменили, её нужно просто добавить в массив выходных цен
        Price replacedPrice;
        // старые цены, которые остались после сравнения
        HashSet<Price> remainedPrices;
        // части новой цены, которые не перекрывают старую
        HashSet<Price> needCheckCross;

        ReturnPrice(Price replacedPrice, HashSet<Price> remainedPrices, HashSet<Price> needCheckCross) {
            this.replacedPrice = replacedPrice;
            this.remainedPrices = new HashSet<Price>(remainedPrices);
            this.needCheckCross = new HashSet<Price>(needCheckCross);
        }

        ReturnPrice(Price replacedPrice, Price remainedPrice, HashSet<Price> needCheckCross) {
            this(replacedPrice,
                    new HashSet<Price>(Collections.singletonList(remainedPrice)),
                    needCheckCross);
        }

        ReturnPrice(Price replacedPrice, HashSet<Price> remainedPrice, Price needCheckCross) {
            this(replacedPrice,
                    remainedPrice,
                    new HashSet<Price>(Collections.singletonList(needCheckCross)));
        }


        ReturnPrice(Price replacedPrice, Price remainedPrice, Price needCheckCross) {
            this(replacedPrice,
                    new HashSet<Price>(Collections.singletonList(remainedPrice)),
                    new HashSet<Price>(Collections.singletonList(needCheckCross)));
        }

        ReturnPrice(Price replacedPrice) {
            this(replacedPrice, new HashSet<Price>(), new HashSet<Price>());
        }

        ReturnPrice() {
            this(null, new HashSet<Price>(), new HashSet<Price>());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ReturnPrice that = (ReturnPrice) o;

            if (replacedPrice != null ? !replacedPrice.equals(that.replacedPrice) : that.replacedPrice != null)
                return false;
            if (remainedPrices != null ? !remainedPrices.equals(that.remainedPrices) : that.remainedPrices != null)
                return false;
            return needCheckCross != null ? needCheckCross.equals(that.needCheckCross) : that.needCheckCross == null;
        }

        @Override
        public int hashCode() {
            int result = replacedPrice != null ? replacedPrice.hashCode() : 0;
            result = 31 * result + (remainedPrices != null ? remainedPrices.hashCode() : 0);
            result = 31 * result + (needCheckCross != null ? needCheckCross.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "ReturnPrice{" +
                    "replacedPrice=" + replacedPrice +
                    ", remainedPrices=" + remainedPrices +
                    ", needCheckCross=" + needCheckCross +
                    '}';
        }
    }
}
