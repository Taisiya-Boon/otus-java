package ru.otus;

import java.util.Collections;
import java.util.Comparator;

public class TestListMain {

    public static void main(String[] args) {

        DIYarrayList list1 = new DIYarrayList();

        DIYarrayList list2 = new DIYarrayList(30);

        DIYarrayList list3 = new DIYarrayList();

        DIYarrayList list4 = new DIYarrayList(54, 64, 2, 7, 58, 54, 74, 5, 19, 4, 2, 8, 5, 6, 7, 10, 35, 84, 26, 24, 37);

        DIYarrayList list5 = new DIYarrayList(15.48, 25.34, 4.0, 45.1, 1.0001, 41.3, 1.0021, 4.0, 16.25, 27.35,
                                                        37.44, 74.0, 37.43, 11.47, 64.1, 54.3, 21.02, 31.84, 55.006, 41.03);

        DIYarrayList list6 = new DIYarrayList('a', 'i', 'k', 's', 'p', 'n', 'q', 'h', 'o', 'g',
                                                        'n', 'v', 'f', 'd', 'i', 'j', 'x', 'm', 'z', 'c');

        DIYarrayList list7 = new DIYarrayList("one", "apple", "apache", "abc", "abd", "list", "pen", "first", "fist", "null",
                                                        "pig", "zero", "frog", "boom", "otus", "joy", "learn", "l", "pink", "lock");

        DIYarrayList list8 = new DIYarrayList('k', 54, 34.15, "stop", 54, 375, true, 64.15, 'o', "hot",
                                                        "fly", 578, 3, 14, 1.24, 4.65, false, 3.0, 'l', 'g');

        //Заполнение тестовых DIYarrayList

        for (int i = 0; i < list2.size(); i++) {
            list2.set(i, i);
            list1.add(list2.size()-i);
            if ( i % 2 == 0 ) {
                list3.add(i);
            } else {
                list3.add(list2.size()-i);
            }
        }

        //Вывод состояния DIYarrayList на экран до произведения операций над ними

        System.out.print("list1. ");
        list1.listString();

        System.out.print("list2. ");
        list2.listString();

        System.out.print("list3. ");
        list3.listString();

        System.out.print("list4. ");
        list4.listString();

        System.out.print("list5. ");
        list5.listString();

        System.out.print("list6. ");
        list6.listString();

        System.out.print("list7. ");
        list7.listString();

        System.out.print("list8. ");
        list8.listString();

        System.out.println();

        //Компаратор для работы метода Collections.sort();

        Comparator c = new Comparator() {

            private Object object;

            @Override
            public int compare(Object a, Object b) {
                if ( (a instanceof Integer) && (b instanceof Integer) ) {
                    return Integer.compare((int) a, (int) b);
                } else if ( (a instanceof String) && (b instanceof String) ) {
                    return CharSequence.compare((CharSequence)a, (CharSequence)b);
                } else if ( (a instanceof Double) && (b instanceof Double) ) {
                    return Double.compare((double)a, (double)b);
                } else if ( (a instanceof Character) && (b instanceof Character) ) {
                    return Character.compare((char)a, (char)b);
                } else {
                    throw new UnsupportedOperationException("Сортировка невозможна. Выбирите Коллекцию элементы которых поддаются сравнению.");
                }
            }

            @Override
            public boolean equals(Object obj) {
                return obj == this.object;
            }
        };

        //Содержимое list3 копируется в list2

        Collections.copy(list2, list3);

        //Сортировка list4, list5, list6, list7.

        Collections.sort(list4, c);
        Collections.sort(list5, c);
        Collections.sort(list6, c);
        Collections.sort(list7, c);
//        Collections.sort(list8, c); //ошибка ("Сортировка невозможна. Выбирите Коллекцию элементы которых поддаются сравнению.")

        //Добавление элементов в list1

        Collections.addAll(list1, 5, 6, 87, 65.87, "lo");
        Collections.addAll(list1, list4.toArray());

        //Вывод состояния DIYarrayList на экран после произведения операций над ними

        System.out.print("list1. ");
        list1.listString();

        System.out.print("list2. ");
        list2.listString();

        System.out.print("list3. ");
        list3.listString();

        System.out.print("list4. ");
        list4.listString();

        System.out.print("list5. ");
        list5.listString();

        System.out.print("list6. ");
        list6.listString();

        System.out.print("list7. ");
        list7.listString();

        System.out.print("list8. ");
        list8.listString();

    }

}
