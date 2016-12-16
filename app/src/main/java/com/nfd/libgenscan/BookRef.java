package com.nfd.libgenscan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import android.content.Intent;
import android.net.Uri;

/**
 * @author Alexander Ronsse-Tucherov
 * @version 2016-08-26.
 *          Wrapper to hold a reference to some book; provided for options for future expansion (e.g. UPC manipulation)
 */
class BookRef {
    private static ArrayList<BookRef> bookList = new ArrayList<>(); //currently dead, but likely will help with history
    private List<BarcodeFormat> allowedFormats = Arrays.asList(BarcodeFormat.ISBN10, BarcodeFormat.ISBN13);

    private String id;
    private BarcodeFormat format;
    private boolean opened;
    private MainActivity parent;


    public BookRef(String id, BarcodeFormat format, boolean opened, MainActivity parent) throws IllegalArgumentException {


        if (!isAllowed(format)) {
            throw new IllegalArgumentException("Format not supported");
        }
        this.id = id;
        this.format = format;
        this.opened = opened;
        this.parent = parent;
    }

    public boolean isAllowed(BarcodeFormat b) {
        return this.allowedFormats.contains(b);
    }

    public String getId() {
        return id;
    }

    public boolean isOpened() {
        return opened;
    }

    public BarcodeFormat getFormat() {
        return format;
    }

    static void addToList(BookRef b) {
        bookList.add(b);
    }

    //TODO: add more libraries, possibly ways of handling other types of barcodes (search by UPC?)
    public void searchBook() {
        opened = true;
        String uri = "http://libgen.io/search.php?req=" + id +
                "&lg_topic=libgen&open=0&view=simple&res=25&phrase=1&column=identifier";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        parent.fire(browserIntent);

    }


}
