package com.nfd.libgenscan;

import java.util.ArrayList;
import me.dm7.barcodescanner.zbar.BarcodeFormat;
import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
/**
 * @author Alexander Ronsse-Tucherov
 * @version 2016-08-26.
 *
 * Simple String wrapper to hold a reference to some book; provided for options for future expansion
 * (e.g. UPC manipulation)
 */
class BookRef {
    private static ArrayList<BookRef> list = new ArrayList<>();
    private ArrayList<BarcodeFormat> allowedFormats = new ArrayList<BarcodeFormat>();

    private String id;
    private BarcodeFormat format;
    private boolean opened;
    private MainActivity parent;


    public BookRef(String id, BarcodeFormat format, boolean opened, MainActivity parent) throws IllegalArgumentException {

        allowedFormats.add(BarcodeFormat.ISBN10);
        allowedFormats.add(BarcodeFormat.ISBN13);

        if (!this.allowedFormats.contains(format)){
            throw new IllegalArgumentException("Format not supported");
        }
        this.id = id;
        this.format = format;
        this.opened = opened;
        this.parent = parent;
    }

    public String getId(){return id;}
    public boolean isOpened(){return opened;}
    public BarcodeFormat getFormat(){return format;}

    static void addToList(BookRef b){
        list.add(b);
    }

    public void searchBook() {
        opened = true;
        //fire search intent
        String uri = "http://libgen.io/search.php?req=" + id +
                "&lg_topic=libgen&open=0&view=simple&res=25&phrase=1&column=identifier";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        //...how. How do I fire this; can't sent it to MainIntent easily because this is static
        //how bout this?
        parent.fire(browserIntent);

    }





}
