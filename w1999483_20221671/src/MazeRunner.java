import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MazeRunner {

    // Directions for Up, Down, Left, Right
    private static final int[] DX = {-1, 1, 0, 0};
    private static final int[] DY = {0, 0, -1, 1};
    private static final String[] DIR_NAMES = {"up", "down", "left", "right"};

    private char[][] map;
    private Location start;
    private Location finish;
    private int height, width;

    // Initialize maze from file
    public MazeRunner(String filename) throws FileNotFoundException {
        parseFile(filename);
    }

    // Parse the file to initialize the maze, start, and finish locations
    private void parseFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        List<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            lines.add(line);
            width = Math.max(width, line.length());
        }
        scanner.close();

        height = lines.size();
        map = new char[height][width];

        for (int row = 0; row < height; row++) {
            char[] chars = lines.get(row).toCharArray();
            for (int col = 0; col < width; col++) {
                map[row][col] = chars[col];
                if (chars[col] == 'S') {
                    start = new Location(row, col);
                }
                if (chars[col] == 'F') {
                    finish = new Location(row, col);
                }
            }
        }

        // Ensure both start and finish Locations are set
        if (start == null || finish == null) {
            throw new IllegalArgumentException("Start (S) or finish (F) Location not found in the map.");
        }
    }

    // Solve the maze
    public void solve() {
        Map<Location, Path> path = new HashMap<>();
        Queue<Location> queue = new LinkedList<>();
        queue.offer(start);
        path.put(start, null); // Start Location has no previous Path

        while (!queue.isEmpty()) {
            Location current = queue.poll();

            // Check if current Location is null
            if (current == null) {
                continue;
            }

            // Check if we've reached the finish Location
            if (current.equals(finish)) {
                printPath(path);
                return;
            }

            // Search neighboring areas
            for (int i = 0; i < 4; i++) {
                Location next = slide(current, DX[i], DY[i]);
                if (next != null && !path.containsKey(next)) {
                    queue.offer(next);
                    path.put(next, new Path(current, next, DIR_NAMES[i]));
                }
            }
        }
        System.out.println("No path found.");
    }

    // Prints the path from start to finish
    private void printPath(Map<Location, Path> path) {
        LinkedList<String> steps = new LinkedList<>();
        Location step = finish;
        while (path.get(step) != null) {
            Path Path = path.get(step);
            steps.addFirst(String.format("Move %s to: (%d,%d)", Path.direction, Path.to.x + 1, Path.to.y + 1));
            step = Path.from;
        }
        steps.addFirst("\nStart at: (" + (start.x + 1) + "," + (start.y + 1) + ")");
        steps.addLast("Completed!");

        for (String s : steps) {
            System.out.println(s);
        }
    }

    // Slides in a specific direction from the start Location
    private Location slide(Location start, int dx, int dy) {
        int x = start.x;
        int y = start.y;
        while (isValid(x + dx, y + dy)) {
            x += dx;
            y += dy;
            if (map[x][y] == 'F') return new Location(x, y);
            if (map[x][y] == '0') return null;
        }
        return new Location(x, y);
    }

    // Verify Location validity
    private boolean isValid(int x, int y) {
        return x >= 0 && x < height && y >= 0 && y < width && map[x][y] != '0';
    }

    // Main method
    public static void main(String[] args) {
        try {
            MazeRunner solver = new MazeRunner("grid.txt");
            long startTime = System.nanoTime();
            solver.solve();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000;
            System.out.println("\nrun-time: " + duration + " ms");
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}




