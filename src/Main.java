public class Main {
    private static double[][] transition, emission, initial;
    private static int [] emissionSequence;
    private static Kattio io = new Kattio(System.in, System.out);
    private static double result = 0;

    public static void main(String[] args) {
        transition = handleInput();
        emission = handleInput();
        initial = handleInput();
        double[][] multMatrix = multiply(transition, emission);
        double[][] emissionDist = multiply(initial, multMatrix);

        emissionSequence = handleSequenceInput();

        if(emissionSequence.length == 1)
            throw new RuntimeException();


        double[] pi = new double[initial[0].length];
        for(int i = 0; i < pi.length; i++)
            pi[i] = initial[0][i];

        double lol = forwardProc(emissionSequence, transition.length, pi, transition, emission);

        for(int i = 0; i < transition.length; i++) {
            double current = pi[i] * emission[i][emissionSequence[0]];
            if(current != 0)
                forward(transition, emission, emissionSequence, i, 1, current);
        }




        System.out.println(result);
        System.out.println(lol);

        io.close();
    }

    private static void forward(double[][] transition, double[][] emission, int[] sequence, int state, int index, double prob){
        if(emissionSequence.length == index) {
            result += prob;
            return;
        }

        for(int i = 0; i < transition.length; i++) {
            double current = prob * transition[state][i] * emission[i][emissionSequence[index]];
            if(current > 0)
                forward(transition, emission, sequence, i, index+1, current);
        }
    }

    private static double[][] handleInput() {
        int rows = io.getInt();
        int cols = io.getInt();
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = io.getDouble();
            }
        }
        return matrix;
    }

    private static int[] handleSequenceInput() {
        int rows = io.getInt();
        int[] sequence = new int[rows];
        for(int i = 0; i < rows; i++)
            sequence[i] = io.getInt();
        return sequence;
    }

    private static double forwardProc(int[] o, int numStates, double[] pi,double[][] a, double[][] b) {
        int T = o.length;
        double[][] fwd = new double[numStates][T];
        double lol = 0;

    /* initialization (time 0) */
        for (int i = 0; i < numStates; i++) {
            fwd[i][0] = pi[i] * b[i][o[0]];
            if(fwd[i][0] != 0)
                lol = fwd[i][0];
        }


    /* induction */
        for (int t = 0; t <= T-2; t++) {
            for (int j = 0; j < numStates; j++) {
                fwd[j][t+1] = 0;
                for (int i = 0; i < numStates; i++)
                    fwd[j][t+1] += (fwd[i][t] * a[i][j]);
                fwd[j][t+1] *= b[j][o[t+1]];
                if(fwd[j][t+1] != 0)
                    lol = fwd[j][t+1];
            }
        }

        return lol;
    }

    private static void printMatrix(double[][] matrix) {
        int rows = 0;
        int cols = 0;
        String line = "";
        for (double[] row : matrix) {
            rows++;
            for (double j : row) {
                cols++;
                line += (j + " ");
            }
        }
        System.out.println(rows + " " + cols + " " + line);
    }

    // Delete me!
    private static double[] convertMatrix(double[][] matrix) {
        double[] vector = new double[matrix[0].length];
        int i = 0;

        for (double[] row : matrix) {
             i++;
            for (double j : row) {
                vector[i] = j;
            }
        }
        return vector;
    }

    // Delete me!
    private static void printSequence(int[] sequence) {
        for(int i : sequence)
            System.out.print(i);
        System.out.println();
    }

    // Delete me!
    private static void printPi(double[] sequence) {
        for(double i : sequence)
            System.out.print(i);
        System.out.println();
    }

    // return C = A * B
    public static double[][] multiply(double[][] A, double[][] B) {
        int mA = A.length;
        int nA = A[0].length;
        int mB = B.length;
        int nB = B[0].length;
        if (nA != mB) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] C = new double[mA][nB];
        for (int i = 0; i < mA; i++)
            for (int j = 0; j < nB; j++)
                for (int k = 0; k < nA; k++)
                    C[i][j] += (A[i][k] * B[k][j]);
        return C;
    }
}