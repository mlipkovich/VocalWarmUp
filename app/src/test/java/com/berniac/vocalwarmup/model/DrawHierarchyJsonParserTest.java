package com.berniac.vocalwarmup.model;

import junit.framework.Assert;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by Mikhail Lipkovich on 11/30/2017.
 */
public class DrawHierarchyJsonParserTest {

    private String singleDraw = "{\n" +
            "\"type\": \"draw\",\n" +
            "\"name\": \"draw1\",\n" +
            "\"image\": \"01bbff12\",\n" +
            "\"voice\": \"25C+12D-3\",\n" +
            "\"sample\": \"09ffaf\",  \n" +
            "\"harmony\": []\n" +
            "}\n";

    private String singleCategoryNoChild = "{\"type\":\"category\",\n" +
            "    \"name\":\"Гаммы\",\n" +
            "    \"child\": []}\n";

    private String singleCategoryWithDrawChild = "{\n" +
            "\"type\": \"category\",\n" +
            "\"name\": \"А-ля мажор\",\n" +
            "\"child\": [\n" +
            "{\n" +
            "\"type\": \"draw\",\n" +
            "\"name\": \"draw1\",\n" +
            "\"image\": \"01bbff12\",\n" +
            "\"voice\": \"25C+12D-3\",\n" +
            "\"sample\": \"09ffaf\",  \n" +
            "\"harmony\": []\n" +
            "},\n" +
            "{\n" +
            "\"type\": \"draw\",\n" +
            "\"name\": \"draw2\",\n" +
            "\"image\": \"01fbff12\",\n" +
            "\"voice\": \"25C+12D-3\",\n" +
            "\"sample\": \"01bfaf\"  \n" +
            "}]\n" +
            "}";

    @Test
    public void testSingleDraw() {
        HierarchyItem actual = DrawHierarchyJsonParser.parseItem(singleDraw);
        HierarchyItem expected = new DrawItem(null, HierarchyItem.ItemType.DRAW, "draw1", "01bbff12", "25C+12D-3", "09ffaf");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSingleCategoryNoChild() {
        HierarchyItem actual = DrawHierarchyJsonParser.parseItem(singleCategoryNoChild);
        HierarchyItem expected = new CategoryItem(new HierarchyItem[]{}, null, HierarchyItem.ItemType.CATEGORY, "Гаммы");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSingleCategoryWithDrawChild() {
        HierarchyItem actual = DrawHierarchyJsonParser.parseItem(singleCategoryWithDrawChild);
        HierarchyItem[] child = new HierarchyItem[]{
                new DrawItem(null, HierarchyItem.ItemType.DRAW, "draw1", "01bbff12", "25C+12D-3", "09ffaf"),
                new DrawItem(null, HierarchyItem.ItemType.DRAW, "draw2", "01fbff12", "25C+12D-3", "01bfaf")
        };
        HierarchyItem expected = new CategoryItem(child, null, HierarchyItem.ItemType.CATEGORY, "А-ля мажор");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDrawHierarchyConsistency() throws FileNotFoundException {
        HierarchyItem[] items =
                DrawHierarchyJsonParser.parseItemArray(new FileReader("app/src/main/assets/draw_hierarchy.json"));
        Assert.assertEquals(items.length, 7);
        // TODO: More intensive testing of the main hierarchy
    }
}
