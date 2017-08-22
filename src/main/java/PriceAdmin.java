import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by aokly on 17.08.2017.
 */
public class PriceAdmin {


    private static HashSet<Price> splitPrice(Price price, Date date) {
        //System.out.println(price.getBegin() + ";" + price.getEnd() + ";" + date);
        // если дата не попадает в период использования цены
        if (price.getBegin().compareTo(date) * price.getEnd().compareTo(date) > 0 || price.getBegin().equals(date) || price.getEnd().equals(date))
            return new HashSet<>();
        else
            return new HashSet<>(Arrays.asList(
                    new Price(price, price.getBegin(), date),
                    new Price(price, date, price.getEnd())
            ));
    }

    public static void splitOneByOther(Collection<Price> one, Collection<Price> other) {
        for (Price np : one) {
            HashSet<Price> tmpPrices = new HashSet<>();
            HashSet<Price> toDelete = new HashSet<>();
            for (Price op : other) {
                //     System.out.println(">"+np+"-"+op);
                if (np.claimCrossed(op)) {
                    //       System.out.println("claimed");
                    HashSet<Price> s2 = new HashSet<>();
                    HashSet<Price> s1 = splitPrice(op, np.getBegin());
                    //System.out.println(np+" "+op);
                    //System.out.println(s1);
                    if (s1.isEmpty()) {
                        s2 = splitPrice(op, np.getEnd());
                    } else {
                        HashSet<Price> toDeleteTmp = new HashSet<>();
                        for (Price p : s1) {
                            HashSet<Price> tmpS = splitPrice(p, np.getEnd());
                            s2.addAll(tmpS);
                            if (!tmpS.isEmpty())
                                toDeleteTmp.add(p);
                        }
                        s1.removeAll(toDeleteTmp);
                    }
                    System.out.println("s1: " + s1);
                    System.out.println("s2: " + s2);
                    System.out.println("tmp before: " + tmpPrices);
                    tmpPrices.addAll(s1);
                    tmpPrices.addAll(s2);
                    System.out.println("tmp: " + tmpPrices);

                    if (!(s1.isEmpty() && s2.isEmpty()))
                        toDelete.add(op);
                }
            }
            System.out.println("one: " + one);
            System.out.println("toDelete: " + toDelete);
            System.out.println("other before delete: " + other);
            other.removeAll(toDelete);
            System.out.println("other after delete: " + other);
            other.addAll(tmpPrices);
            System.out.println("other after add: " + other);
        }
    }

    static void dispPrices(Collection<Price> prices, String caption) {
        System.out.println("______________________________");
        System.out.println(caption);
        for (Price p : prices)
            System.out.println(p);
    }

    static void dispMap(Map<SubPrice, List<Price>> prices, String caption) {
        System.out.println("______________________________");
        System.out.println(caption);
        for (Map.Entry<SubPrice, List<Price>> p : prices.entrySet()) {
            System.out.println(p.getKey());
            for (Price price : p.getValue()) {
                System.out.println("> " + price);
            }
        }
    }


    public static Map<SubPrice, List<Price>> splitPrices(LinkedList<Price> oldPrices, LinkedList<Price> newPrices) {
        // System.out.println(oldPrices);
        // System.out.println(newPrices);
        //System.out.println("__________________");
        // разбиваем старые цены новыми
        dispPrices(newPrices, "Новые");
        dispPrices(oldPrices, "Старые");
        System.out.println("Разделение");
        splitOneByOther(newPrices, oldPrices);
        System.out.println("_____________________________");
        splitOneByOther(oldPrices, newPrices);
        Collections.sort(newPrices, Price.getComparator());
        dispPrices(newPrices, "Новые");
        dispPrices(oldPrices, "Старые");


        // System.out.println("processed");
        // System.out.println(oldPrices);
        //   System.out.println(newPrices);

        // в старых ценах оставляем только те, которых нет среди новых
        // в проверке на равенство значение цены(value) не учитывается,
        // небольшой чит, иначе пришлось бы писать больший объём кода.
        oldPrices.removeAll(newPrices);

        dispPrices(oldPrices, "После удаления старые");


        // объединяем две коллекции цен в отсортированном порядке
        HashMap<SubPrice, List<Price>> res = new HashMap<>((Stream.concat(newPrices.stream(), oldPrices.stream())).map(p -> new AbstractMap.SimpleEntry<>(new SubPrice(p), p)).
                collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList()))));

        for (Map.Entry<SubPrice, List<Price>> e : res.entrySet()) {
            e.getValue().sort(Comparator.comparing(Price::getBegin));
        }
        dispMap(res, "словарь");
        return res;
    }

    public static Collection<Price> addNewPrices(Collection<Price> _oldPrices, Collection<Price> _newPrices) {
        // копируем коллекции, чтобы не испортить данные, пришедшие извне
        LinkedList<Price> oldPrices = new LinkedList<Price>();
        oldPrices.addAll(_oldPrices);
        LinkedList<Price> newPrices = new LinkedList<Price>();
        newPrices.addAll(_newPrices);

        Map<SubPrice, List<Price>> prices = splitPrices(oldPrices, newPrices);

        // схлопываем соседние равные цены
        LinkedList<Price> res = new LinkedList<>();
        for (List<Price> ps : prices.values()) {
            LinkedHashSet<Price> needsToBeDeleted = new LinkedHashSet<>();
            // Collections.sort(ps, Comparator.comparing(Price::getBegin));
            for (int i = 0; i < ps.size() - 1; i++) {
                Price pi = ps.get(i);
                Price pip1 = ps.get(i + 1);
                if (pi.getEnd().equals(pip1.getBegin()) && pi.getValue() == pip1.getValue()) {
                    pip1.setBegin(pi.getBegin());
                    needsToBeDeleted.add(pi);
                }
            }
            ps.removeAll(needsToBeDeleted);
            res.addAll(ps);
        }
        dispPrices(res, "После объединения");
        Collections.sort(res,Price.getComparator());
        return res;
    }

    public static class SubPrice {
        private String product_code; // код товара
        private int number; // номер цены
        private int depart; // номер отдела

        public SubPrice(String product_code, int number, int depart) {
            this.product_code = product_code;
            this.number = number;
            this.depart = depart;
        }

        public SubPrice(Price p) {
            this.product_code = p.getProduct_code();
            this.number = p.getNumber();
            this.depart = p.getDepart();
        }

        public String toString() {
            return "{" + this.product_code + "|" + this.number + "|" + this.depart + "}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SubPrice subPrice = (SubPrice) o;

            if (number != subPrice.number) return false;
            if (depart != subPrice.depart) return false;
            return product_code != null ? product_code.equals(subPrice.product_code) : subPrice.product_code == null;
        }

        @Override
        public int hashCode() {
            int result = product_code != null ? product_code.hashCode() : 0;
            result = 31 * result + number;
            result = 31 * result + depart;
            return result;
        }
    }
}
