import java.util.*;



public class Plabone {
    // Статистический метод для вывода в консоль двумерного массива
    public static void camelCase(double[][] l) {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 10; j++) {
                if (Double.isNaN(l[i][j])) {
                    System.out.print(String.format("NaN      ") + "    ");
                }
                else if (l[i][j] == Double.POSITIVE_INFINITY) {
                    System.out.print(String.format("Infinit  ") + "    ");
                }
                else if (l[i][j] < 0) {
                    System.out.print(String.format("%.2e", l[i][j]) + "    ");
                }
                else System.out.print(String.format("%.2e", l[i][j]) + "     ");
            }
            System.out.println();
        }
    }

    // Статистический метод для вычисления очередного элемента двумерного массива
    public static double[][] twoArray(long[] w, float[] x) {
        double[][] l = new double[11][10];
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 10; j++) {
                if (w[i] == 8) {
                    l[i][j] = Math.pow(Math.asin(0.5 * (x[j] - 2)/24),Math.cbrt(Math.tan(x[j]))/2);
                }
                else {
                    boolean fol = false;
                    int[] d = {14, 18, 20, 22, 24};
                    for (int e: d) {
                        if (w[i] == e) fol = true;
                    }
                    if (fol) {
                        l[i][j] = Math.asin(Math.sin(Math.cbrt(Math.log(Math.abs(x[j])))));

                    }
                    else {
                        l[i][j] = (Math.pow(Math.E, Math.pow(3/(1 - Math.cos(x[j])), Math.cos(x[j])))) / 2;
                    }

                }
            }
        }
        return l;
    }

    public static void main(String[] args) {
        long[] w = new long[(24-4)/2+1];
        long elem = 24;
        for (int i=0; i < w.length; i++) {
            w[i] = elem;
            elem-=2;
        }

        Random rand = new Random();
        float[] x = new float[10];
        for (int i = 0; i < 10; i++) {
            float number = -14.0f + (24.0f * rand.nextFloat());
            x[i] = number;
        }

        CamelCase(two_array(w, x));
    }
}
