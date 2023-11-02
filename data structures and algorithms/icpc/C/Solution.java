import java.util.*;

public class Solution{
    static Scanner input;
    static int w;
    static int h;
    static int n;

    class Part{
        int top, left, width, height, lvls;
        
        Part(int left, int top, int width, int height, int lvls) {
            this.top = top;
            this.left = left;
            this.width = width;
            this.height = height;
            this.lvls = lvls;
        }

        void addSprite() {

        }

    }
    static ArrayList<ArrayList<Integer>> grid;
    static ArrayList<Part> parts;
    public static void main(String[] args) {
        input = new Scanner(System.in);
        w = input.nextInt();
        h = input.nextInt();
        n = input.nextInt();

        grid = new ArrayList(w);
        for(int i = 0; i < w; ++i) {
            grid.add(new ArrayList(h));
            for(int j = 0; j < h; ++j) {
                grid.get(i).add(-1);
            }
        }

        parts = new ArrayList(n);

        for(int i = 0; i < n; ++i) {
            int bx = input.nextInt(), yx = input.nextInt(), bw = input.nextInt(), bh = input.nextInt();
            for(int x = bx; x < x + bw; ++x) {
                for(int y = by; y < y + by; ++y) {
                    grid.get(x).set(y, i);
                }
            }
            int k = input.nextInt();
            parts.add(new Part(bx, by, bw, by, k));
        }
    }
}