import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class main {
    //toDo вычленение битов и байтов при помощи побитовых операций
    //toDo сортировка по системе счисления с основанием 256, вычленяя целые байты]
    //toDo ризонно сравнивать только 3 байта у числа, т.к. число занимающее 3 байта это 16777216, что наиболее выгодно

    // неэффективная сортировка
    public static void badDefaultRadixSorting(int input[]){
        /*
        выделяется список под 10 цифр, тк тип Integer позволяет хранить числа размером в 4 байта, т.е. 32 бит
        -> числа типа Integer не будут превышать 2^32 = 4294967296. А конкертно, числа типа Integer принимают диапазон
         от -2147483648 до 2147483647, и здесь, соответственно 10 разрядов. Когда сортировка будет побайтовая, будет
         выделяться список из 4 эл-тов (под 4 байта)
        */

        List<Integer>[] digits = new ArrayList[10];
        List<Integer>[] negativeDigits = new ArrayList[10];

        // Создаётся по списку в каждый элемент списка digits и nDigits для каждой цифры
        for (int i = 0; i < digits.length; i++) {
            digits[i] = new ArrayList<Integer>();
            negativeDigits[i] = new ArrayList<Integer>();
        }

        // сигнализатор, который будет свидетельствовать о том, что пора прекратить цикл
        boolean flag = true;

        int temp = 0;
        int divisor = 1;
        while (flag) {

            flag = false;

            // цикл "раскладывает" числа с соответственным разрядом по цифрам от 0 до 9
            for (Integer i : input) {
                temp  = i / divisor;
                if(i >= 0)
                digits[temp % 10].add(i);

                else {
                    temp *= -1;
                    negativeDigits[temp % 10].add(i);
                }

                //если разряды "закончатся" - цикл просто прекратится после очередного прохода
                if (!flag && temp != 0) {
                    flag = true;
                }

            }

            // инициализируется число, которое будет означать позицию, в которую нужно добавить число
            int counter = 0;

            /*происходит прогон, соответственно, по каждой цифре из разряда и они добавляются
            в необходимом порядке, т.е. сначала цифра разряда которых равна 0, потом 1 и т.д.
            По итогу, таким образом числа сортируются*/

            /*в последующих циклах происходят операции добавления чисел по очереди, сначала отрицательные, от
            больших цифр разряда по модулю к меньшим, а потом положительные и нули, от меньших к большим*/
            for(int i = 9; i >= 0; i--){
                for (Integer j : negativeDigits[i]){
                    input[counter] = j;
                    counter++;
                }
                negativeDigits[i].clear();
            }

            for (int i = 0; i < 10; i++) {
                for (Integer j : digits[i]) {
                    input[counter] = j;
                    counter++;
                }

                /* список чисел с цифрой i соответствующего разряда очищается, чтобы потом заново их туда загрузить
                аналогично и у  отрицательных*/
                digits[i].clear();

            }
            //переходим к следующему разряду
            divisor *= 10;
        }
    }

    // эффективная сортировка для положительных и отрицательных чисел в десятичной сс
    public static void defaultRadixSorting(int input[]){

        int[] countOfDigits = new int[19];
        int[] newCountOfDigits = new int[19];

        int[] tempArray = new int[input.length];

        int temp = 0;
        int divisor = 1;

        boolean flag = true;

        while (flag){
            flag = false;

            for(int i : input){
                temp = i / divisor % 10 + 9;
                countOfDigits[temp] += 1;

                if(temp != 9)
                    flag = true;
            }

            if(!flag) return;

            for(int i = 1; i < 19; i++){
                countOfDigits[i] += countOfDigits[i - 1];
            }

            for(int i = 1; i < 19; i++){
                newCountOfDigits[i] = countOfDigits[i - 1];
            }

            for(int i : input){
                temp = i / divisor % 10 + 9;
                tempArray[newCountOfDigits[temp]] = i;
                newCountOfDigits[temp] += 1;
            }

            System.arraycopy(tempArray, 0, input, 0, tempArray.length);

            for(int i = 0; i < 19; i ++){
                countOfDigits[i] = 0;
                newCountOfDigits[i] = 0;
            }

            divisor *= 10;
        }
    }

    public static void byteRadixSorting(int input[]){

        int countOfDigits[] = new int[256];

        int[] newCountOfDigits = new int[256];

        int[] tempArray = new int[input.length];

        int temp = 0;
        int shift = 0;

        boolean flag = true;

        while (flag){
            flag = false;

            // считаем кол-во чисел с соответствующим байтом
            for (int i : input){
                temp = (i >> shift) & 255;
                countOfDigits[temp] += 1;
                if(temp != 0) flag = true;
            }

            if (!flag) return;

            for(int i = 1; i < 256; i++){
                countOfDigits[i] += countOfDigits[i - 1];
            }

            for(int i = 1; i < 256; i++){
                newCountOfDigits[i] = countOfDigits[i - 1];
            }

            for(int i : input){
                temp = i >> shift & 255;
                tempArray[newCountOfDigits[temp]] = i;
                newCountOfDigits[temp] += 1;
            }

            System.arraycopy(tempArray, 0, input, 0, tempArray.length);

            for(int i = 0; i < 256; i ++){
                countOfDigits[i] = 0;
                newCountOfDigits[i] = 0;
            }

            shift += 8;

        }
    }


    public static void main(String[] args) {

        int[] arr = {70, 6, 65, 25, 13, 62, 36, 50, 72, 47, 86, 93, 60, 48, 71, 5, 57, 73, 21, 77, 32, 12, 99, 0, 312, 42, 4};

        //defaultRadixSorting(arr);
        byteRadixSorting(arr);

        for(int i = 0; i < arr.length; i++){
            System.out.println(arr[i]);
        }
    }
}
