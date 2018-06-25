package com.anderson;

import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PathTest {

    private static Array2D toArray(String[] s) {
        int height = s.length;
        int width = s[0].length();
        Array2D a = new Array2D(width, height, 0.0f, 9.0f);

        for (int y = 0; y < height; ++y) {
            String row = s[y];
            assertEquals(width, row.length(), "Array must have uniform row width");

            for (int x = 0; x < width; ++x) {
                char c = row.charAt(x);
                assertTrue(c >= '0' && c <= '9', "Can only convert 0..9");
                a.set(x, y, c - '0');
            }
        }

        return a;
    }

    private static ArrayList<Point2D> toList(String[] s) {
        int height = s.length;
        int width = s[0].length();
        Point2D[] list = new Point2D[10];

        for (int y = 0; y < height; ++y) {
            String row = s[y];
            assertEquals(width, row.length(), "Array must have uniform row width");

            for (int x = 0; x < width; ++x) {
                char c = row.charAt(x);
                if (c >= '0' && c <= '9') {
                    int i = c - '0';
                    list[i] = new Point2D.Float(x, y);
                }
            }
        }

        ArrayList<Point2D> out = new ArrayList<Point2D>();
        for (int i = 0; i < list.length; ++i) {
            if (list[i] != null) {
                out.add(list[i]);
            }
        }

        return out;
    }

    @Test
    void start_next_to_end() {
        Array2D land = toArray(new String[] {
                "00",
                "11"
        });
        List<Point2D> expected_path = toList(new String[] {
                "01",
                ".."
        });

        List<Point2D> path = Path.astar(land, 0,0, 1, 0, 0.1f);
        assertArrayEquals(expected_path.toArray(), path.toArray());
    }

    @Test
    void too_much_elevation() {
        Array2D land = toArray(new String[] {
                "01",
                "00"
        });

        List<Point2D> path = Path.astar(land, 0,0, 1, 0, 0.9f);
        assertEquals(0, path.size());
    }

    @Test
    void cant_go_over_mountain() {
        Array2D land = toArray(new String[] {
                "020",
                "020",
                "020"
        });

        List<Point2D> path = Path.astar(land, 0,0, 2, 2, 1f);
        assertEquals(0, path.size());
    }

    @Test
    void non_square() {
        Array2D land = toArray(new String[] {
                "0111100011",
                "2222222222",
        });
        List<Point2D> expected_path = toList(new String[] {
                "0123456789",
                "..........",
        });

        List<Point2D> path = Path.astar(land, 0,0, 9, 0, 1f);
        assertArrayEquals(expected_path.toArray(), path.toArray());
    }

    @Test
    void three_steps_with_corner() {
        Array2D land = toArray(new String[] {
                "00",
                "10"
        });
        List<Point2D> expected_path = toList(new String[] {
                "01",
                ".2"
        });

        List<Point2D> path = Path.astar(land, 0,0, 1, 1, 0.1f);
        assertArrayEquals(expected_path.toArray(), path.toArray());
    }

    @Test
    void simple4x4() {
        Array2D land = toArray(new String[] {
                "0010",
                "0110",
                "0010",
                "1000"
        });
        List<Point2D> expected_path = toList(new String[] {
                "10..",
                "2..9",
                "34.8",
                ".567"
        });

        List<Point2D> path = Path.astar(land, 1,0, 3, 1, 0.1f);
        assertArrayEquals(expected_path.toArray(), path.toArray());
    }

    @Test
    void shortcut_over_hill() {
        Array2D land = toArray(new String[] {
                "0011",
                "0121",
                "0111",
                "0010"
        });
        List<Point2D> expected_path = toList(new String[] {
                ".012",
                "...3",
                "....",
                "...."
        });

        List<Point2D> path = Path.astar(land, 1,0, 3, 1, 10f);
        assertArrayEquals(expected_path.toArray(), path.toArray());
    }

    @Test
    void elevation_best_path() {
        Array2D land = toArray(new String[] {
                "2195",
                "1926",
                "0175",
                "2234"
        });
        List<Point2D> expected_path = toList(new String[] {
                "10..",
                "2..9",
                "34.8",
                ".567"
        });

        List<Point2D> path = Path.astar(land, 1,0, 3, 1, 1f);
        assertArrayEquals(expected_path.toArray(), path.toArray());
    }
}